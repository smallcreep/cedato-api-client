/**
 * The MIT License (MIT)
 *
 * Copyright (c) 2017 Ilia Rogozhin (ilia.rogozhin@gmail.com)
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"),
 * to deal in the Software without restriction,
 * including without limitation the rights to use, copy, modify, merge, publish,
 * distribute, sublicense, and/or sell copies of the Software,
 * and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included
 * in all copies or substantial portions of the Software.
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
import java.io.IOException;
import org.cactoos.text.FormattedText;

/**
 * Entrypoint Cedato API.
 * @author Ilia Rogozhin (ilia.rogozhin@gmail.com)
 * @version $Id$
 * @since 0.1
 */
public interface Cedato {

    /**
     * Get origin request.
     * @return Request
     * @throws IOException If fails
     */
    Request request() throws IOException;

    /**
     * Simple Cedato API entrypoint.
     */
    final class Simple implements Cedato {

        /**
         * Basic request.
         */
        private final Request req;

        /**
         * Access key.
         */
        private final String access;

        /**
         * Client secret.
         */
        private final String secret;

        /**
         * Ctor.
         * @param access Access key
         * @param secret Client secret
         */
        public Simple(
            final String access,
            final String secret
        ) {
            this(
                new ApacheRequest("https://api.cedato.com/api")
                    .header("Accept", "application/json")
                    .header("api-version", "1"),
                access,
                secret
            );
        }

        /**
         * Ctor.
         * @param req Basic request
         * @param access Access key
         * @param secret Client secret
         */
        private Simple(
            final Request req,
            final String access,
            final String secret
        ) {
            this.req = req;
            this.access = access;
            this.secret = secret;
        }

        @Override
        public Request request() throws IOException {
            this.req
                .method(Request.POST)
                .header(
                    "Authorization",
                    new FormattedText(
                        "Basic %s",
                        ""
                    ).asString()
                ).fetch();
            return null;
        }

    }
}
