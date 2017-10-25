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
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
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
     * Limit.
     */
    private final Integer limit;

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
     * @checkstyle MagicNumberCheck (7 lines)
     */
    private RtSupply(
        final Supplies parent,
        final Request origin,
        final Request req
    ) {
        this(parent, origin, req, 1000);
    }

    /**
     * Ctor.
     * @param parent Supplies parent
     * @param origin Parent request
     * @param req Basic request
     * @param limit Limit
     * @checkstyle ParameterNumberCheck (6 lines)
     */
    private RtSupply(
        final Supplies parent,
        final Request origin,
        final Request req,
        final int limit
    ) {
        this.parent = parent;
        this.origin = origin;
        this.req = req;
        this.limit = limit;
    }

    @Override
    public Supply group(final String group) {
        return new RtSupply(
            this.parent,
            this.origin,
            this.req
                .uri()
                .queryParam("group_by", group)
                .back(),
            this.limit
        );
    }

    // @checkstyle MagicNumberCheck (50 lines)
    @Override
    public Supply range(final ZonedDateTime start, final ZonedDateTime end) {
        return new RtSupply(
            this.parent,
            this.origin,
            this.req
                .uri()
                .queryParam(
                    "start",
                    start.withZoneSameInstant(ZoneOffset.ofHours(-5))
                         .toLocalDateTime()
                         .toEpochSecond(ZoneOffset.UTC)
                )
                .queryParam(
                    "end",
                    end.withZoneSameInstant(ZoneOffset.ofHours(-5))
                       .toLocalDateTime()
                       .toEpochSecond(ZoneOffset.UTC)
                )
                .back(),
            this.limit
        );
    }

    @Override
    public JsonObject json() throws IOException {
        return new RtJson(this.req).fetch();
    }

    @Override
    public Iterator<Supply> iterator() {
        return new SupIterator(this.req, this, this.limit);
    }

    // @checkstyle HiddenFieldCheck (2 lines)
    @Override
    public Supply limit(final int limit) {
        return new RtSupply(
            this.parent,
            this.origin,
            this.req,
            limit
        );
    }
}
