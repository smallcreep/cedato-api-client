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

import com.jcabi.http.Request;

/**
 * Simple Cedato Reports Supplies.
 * @author Ilia Rogozhin (ilia.rogozhin@gmail.com)
 * @version $Id$
 * @since 0.1
 */
public final class RtSupplies implements Supplies {

    /**
     * Parent reports.
     */
    private final Reports reports;

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
     * @param reports Parent reports
     * @param req Origin request
     */
    RtSupplies(final Reports reports, final Request req) {
        this(
            reports,
            req,
            req.uri()
               .path("/supplies")
               .back()
        );
    }

    /**
     * Ctor.
     * @param reports Parent reports
     * @param origin Origin request
     * @param req Basic request
     */
    private RtSupplies(
        final Reports reports,
        final Request origin,
        final Request req
    ) {
        this.reports = reports;
        this.req = req;
        this.origin = origin;
    }

    @Override
    public Supplies extended() {
        return new RtSupplies(
            this.reports,
            this.origin,
            this.req
                .uri()
                .path("/extended")
                .back()
        );
    }

    @Override
    public Supply supply(final int id) {
        return new RtSupply(this, this.req, id);
    }

    @Override
    public Reports reports() {
        return new RtReports(this.reports.cedato(), this.origin);
    }
}
