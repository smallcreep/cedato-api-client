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
 * Supply Iterator.
 * @author Ilia Rogozhin (ilia.rogozhin@gmail.com)
 * @version $Id$
 * @since 0.1
 * @checkstyle ClassDataAbstractionCouplingCheck (500 lines)
 */
public final class SupIterator implements Iterator<Supply> {

    /**
     * Basic request.
     */
    private final Request req;

    /**
     * Supply origin.
     */
    private final Supply origin;

    /**
     * Offset.
     */
    private final AtomicLong offset = new AtomicLong(0);

    /**
     * Limit.
     */
    private final Integer limit;

    /**
     * Current index Supply in json.
     */
    private final AtomicInteger current = new AtomicInteger(0);

    /**
     * Temporarily Supply.
     */
    private final AtomicReference<Supply> temp = new AtomicReference<>();

    /**
     * Ctor.
     * @param req Request origin
     * @param origin Supply origin
     * @param limit Limit
     */
    public SupIterator(
        final Request req,
        final Supply origin,
        final Integer limit
    ) {
        this.limit = limit;
        this.req = req.uri()
                      .queryParam("limit", this.limit)
                      .back();
        this.origin = origin;
    }

    @Override
    public boolean hasNext() {
        final AtomicBoolean result = new AtomicBoolean(false);
        if (this.json() != null
            && (
            (this.current.get() < this.limit
                && this.supply(this.current.get()) != null)
                || this.navigationNext())) {
            result.set(true);
        }
        return result.get();
    }

    /**
     * Check json has navigation next link.
     * @return True if json has navigation next link,
     *          Else if json hasn't navigation next link
     */
    private boolean navigationNext() {
        final AtomicBoolean next = new AtomicBoolean(false);
        final JsonObject data;
        try {
            data = this.temp.get()
                            .json()
                            .getJsonObject("data");
        } catch (final IOException error) {
            throw new UncheckedIOException(error);
        }
        try {
            final JsonObject navigation = data.getJsonObject("navigation");
            next.set(navigation != null
                && navigation
                .getString("next", null) != null);
        } catch (final ClassCastException exception) {
            // Do nothing
        }
        return next.get();
    }

    @Override
    public Supply next() {
        if (this.hasNext()) {
            final Supply result = supply(this.current.get());
            this.current.set(this.current.get() + 1);
            if (result != null) {
                return result;
            }
        }
        throw new NoSuchElementException();
    }

    /**
     * Get supply by index.
     * @param index Supply index
     * @return Supply
     */
    private Supply supply(final int index) {
        final JsonArray supplies = this.json()
                                       .getJsonObject("data")
                                       .getJsonArray("supplies");
        final AtomicReference<Supply> result = new AtomicReference<>();
        if (supplies.size() > index) {
            result.set(
                new JsSupply(
                    this.origin,
                    supplies.getJsonObject(index)
                )
            );
        }
        return result.get();
    }

    /**
     * Get current json.
     * @return Json
     */
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
