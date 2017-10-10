/**
 * The MIT License (MIT)
 *
 * Copyright (c) 2017, Ilia Rogozhin (ilia.rogozhin@gmail.com)
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included
 *  in all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package com.github.smallcreep.cedato.sup;

import com.github.smallcreep.cedato.RtJson;
import com.github.smallcreep.cedato.Supply;
import com.jcabi.http.Request;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.AtomicReference;
import javax.json.JsonArray;
import javax.json.JsonObject;

/**
 *
 * @author Ilia Rogozhin (ilia.rogozhin@gmail.com)
 * @version $Id$
 * @since 0.1
 */
public class SupIterator implements Iterator<Supply> {

    /**
     * Basic request.
     */
    private final Request req;

    private final Supply origin;

    private final AtomicLong offset = new AtomicLong(0);

    private final Integer limit = 1000;

    private final AtomicInteger current = new AtomicInteger(0);

    private final AtomicReference<Supply> temp = new AtomicReference<>();

    public SupIterator(final Request req, final Supply origin) {
        this.req = req.uri()
                      .queryParam("limit", this.limit)
                      .back();
        this.origin = origin;
    }

    @Override
    public boolean hasNext() {
        final AtomicBoolean result = new AtomicBoolean(false);
        if (this.json() != null
            && this.current.get() < this.limit
            && this.navigationNext()) {
            result.set(true);
        }
        return result.get();
    }

    private boolean navigationNext() {
        try {
            final JsonObject navigation = this.temp.get()
                                                   .json()
                                                   .getJsonObject("data")
                                                   .getJsonObject("navigation");
            return navigation != null
                && navigation
                .getString("next", null) != null;
        } catch (final IOException error) {
            throw new UncheckedIOException(error);
        }
    }

    @Override
    public Supply next() {
        if (this.hasNext()) {
            final JsonArray supplies = this.json()
                                           .getJsonObject("data")
                                           .getJsonArray("supplies");
            final int index = this.current.get();
            if (!supplies.isNull(index)) {
                final JsSupply result = new JsSupply(
                    this.origin,
                    supplies.getJsonObject(index)
                );
                this.current.set(index + 1);
                return result;
            }
        }
        throw new NoSuchElementException();
    }

    private JsonObject json() {
        try {
            if (this.temp.get() == null
                || (this.current.get() >= this.limit
                && this.navigationNext())) {
                if (temp.get() != null) {
                    this.offset.set(this.offset.get() + this.limit);
                    this.current.set(0);
                }
                this.temp.set(
                    new JsSupply(
                        this.origin,
                        new RtJson(
                            this.req.uri()
                                    .queryParam("offset", this.offset.get())
                                    .back()
                        ).fetch()
                    )
                );
            }
            return temp.get().json();
        } catch (final IOException error) {
            throw new UncheckedIOException(error);
        }
    }
}
