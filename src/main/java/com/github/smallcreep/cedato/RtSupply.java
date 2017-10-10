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

package com.github.smallcreep.cedato;

import com.github.smallcreep.cedato.sup.SupIterator;
import com.jcabi.http.Request;
import java.io.IOException;
import java.util.Iterator;
import javax.json.JsonObject;

/**
 * Simple Cedato Reports Supply.
 * @author Ilia Rogozhin (ilia.rogozhin@gmail.com)
 * @version $Id$
 * @since 0.1
 */
public final class RtSupply implements Supply {

    /**
     * Parent Supplies.
     */
    private final Supplies parent;

    /**
     * Basic request.
     */
    private final Request req;

    /**
     * Origin request.
     */
    private final Request origin;

    /**
     * Ctor.
     * @param parent Supplies parent
     * @param req Parent request
     * @param id Supply id
     */
    RtSupply(final Supplies parent, final Request req, final int id) {
        this(
            parent,
            req,
            req.uri()
               .queryParam("supply_id", id)
               .back()
        );
    }

    /**
     * Ctor.
     * @param parent Supplies parent
     * @param origin Parent request
     * @param req Basic request
     */
    private RtSupply(
        final Supplies parent,
        final Request origin,
        final Request req
    ) {
        this.parent = parent;
        this.origin = origin;
        this.req = req;
    }

    @Override
    public Supply group(final String group) {
        return new RtSupply(
            this.parent,
            this.origin,
            this.req
                .uri()
                .queryParam("group_by", group)
                .back()
        );
    }

    @Override
    public Supply range(final String start, final String end) {
        return new RtSupply(
            this.parent,
            this.origin,
            this.req
                .uri()
                .queryParam("start", start)
                .queryParam("end", end)
                .back()
        );
    }

    @Override
    public JsonObject json() throws IOException {
        return new RtJson(this.req).fetch();
    }

    @Override
    public Iterator<Supply> iterator() {
        return new SupIterator(this.req, this);
    }
}
