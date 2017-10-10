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

import com.github.smallcreep.text.TextAsBase64;
import com.jcabi.http.Request;
import com.jcabi.http.request.ApacheRequest;
import com.jcabi.manifests.Manifests;
import java.io.IOException;
import javax.json.JsonObject;
import javax.ws.rs.core.HttpHeaders;
import org.cactoos.text.FormattedText;

/**
 * Authorization in Cedato.
 * @author Ilia Rogozhin (ilia.rogozhin@gmail.com)
 * @version $Id$
 * @since 0.1
 */
public interface Auth {

    /**
     * Get Cedato with authorization.
     * @return Cedato
     * @throws IOException If fails
     */
    Cedato cedato() throws IOException;

    /**
     * Authorization by Access key and Client secret in Cedato.
     */
    final class Simple implements Auth {

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
        public Simple(final String access, final String secret) {
            this(Cedato.BASE_URL, access, secret);
        }

        /**
         * Ctor.
         * @param url Base Cedato url.
         * @param access Access key
         * @param secret Client secret
         */
        Simple(final String url, final String access, final String secret) {
            this(
                new ApacheRequest(url)
                    .header(HttpHeaders.USER_AGENT, USER_AGENT),
                access,
                secret
            );
        }

        /**
         * Ctor.
         * @param req Base request
         * @param access Access key
         * @param secret Client secret
         */
        Simple(final Request req, final String access, final String secret) {
            this.req = req;
            this.access = access;
            this.secret = secret;
        }

        @Override
        public Cedato cedato() throws IOException {
            System.out.println(
                new FormattedText(
                    "Basic %s",
                    new TextAsBase64(
                        new FormattedText(
                            "%s:%s",
                            this.access,
                            this.secret
                        )
                    ).asString()
                ).asString()
            );
            final JsonObject auth = new RtJson(
                this.req
                    .header(HttpHeaders.ACCEPT, "application/json")
                    .header("api-version", "1")
                    .uri()
                    .path("/api/token")
                    .back()
                    .method(Request.POST)
                    .header(
                        HttpHeaders.CONTENT_TYPE,
                        "application/x-www-form-urlencoded"
                    )
                    .header(
                        HttpHeaders.AUTHORIZATION,
                        new FormattedText(
                            "Basic %s",
                            new TextAsBase64(
                                new FormattedText(
                                    "%s:%s",
                                    this.access,
                                    this.secret
                                )
                            ).asString()
                        ).asString()
                    )
                    .body()
                    .formParam("grant_type", "client_credentials")
                    .back()
            ).fetch().getJsonObject("data");
            return new RtCedato(
                new FormattedText(
                    "%s %s",
                    auth.getString("token_type"),
                    auth.getString("access_token")
                ).asString()
            );
        }
    }

}
