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
 * Simple Cedato reports.
 * @author Ilia Rogozhin (ilia.rogozhin@gmail.com)
 * @version $Id$
 * @since 0.1
 */
public final class RtReports implements Reports {

    /**
     * Basic request.
     */
    private final Request req;

    /**
     * Origin Cedato.
     */
    private final Cedato cedato;

    /**
     * Ctor.
     * @param cedato Origin Cedato
     */
    RtReports(final Cedato cedato) {
        this(cedato, cedato.request());
    }

    /**
     * Ctor.
     * @param cedato Origin Cedato
     * @param req Basic request
     */
    RtReports(final Cedato cedato, final Request req) {
        this.cedato = cedato;
        this.req = req.uri()
                      .path("/reports")
                      .back();
    }

    @Override
    public Supplies supplies() {
        return new RtSupplies(this, this.req);
    }

    @Override
    public Cedato cedato() {
        return this.cedato;
    }
}
