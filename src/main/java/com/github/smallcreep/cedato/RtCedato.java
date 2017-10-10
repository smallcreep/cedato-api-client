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
import com.jcabi.http.request.ApacheRequest;
import com.jcabi.manifests.Manifests;
import javax.ws.rs.core.HttpHeaders;

/**
 * Cedato API entrypoint.
 * @author Ilia Rogozhin (ilia.rogozhin@gmail.com)
 * @version $Id$
 * @since 0.1
 */
public final class RtCedato implements Cedato {

    /**
     * Version of us.
     */
    private static final String USER_AGENT = String.format(
        "cedato-api-client %s %s %s",
        Manifests.read("Cedato-Version"),
        Manifests.read("Cedato-Build"),
        Manifests.read("Cedato-Date")
    );

    /**
     * Basic request.
     */
    private final Request req;

    /**
     * Ctor.
     * @param token Cedato token
     */
    public RtCedato(final String token) {
        this(Cedato.BASE_URL, token);
    }

    /**
     * Ctor.
     * @param url Base url Cedato
     * @param token Cedato token
     */
    public RtCedato(final String url, final String token) {
        this(
            new ApacheRequest(url)
                .uri()
                .path("/api")
                .back()
                .header(HttpHeaders.ACCEPT, "application/json")
                .header(HttpHeaders.USER_AGENT, USER_AGENT)
                .header("api-version", "1")
                .header(HttpHeaders.AUTHORIZATION, token)
        );
    }

    /**
     * Ctor.
     * @param req Basic request
     */
    private RtCedato(final Request req) {
        this.req = req;
    }

    @Override
    public Request request() {
        return this.req;
    }

}
