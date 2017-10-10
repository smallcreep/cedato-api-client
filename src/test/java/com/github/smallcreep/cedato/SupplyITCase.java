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

import javax.json.JsonObject;
import org.hamcrest.CoreMatchers;
import org.hamcrest.MatcherAssert;
import org.junit.Test;

/**
 * Integration Test Case for {@link RtSupply}.
 * @author Ilia Rogozhin (ilia.rogozhin@gmail.com)
 * @version $Id$
 * @since 0.1
 */
public final class SupplyITCase {

    /**
     * Check supply return correct json.
     * @throws Exception If fails
     * @checkstyle MagicNumberCheck (50 lines)
     */
    @Test
    public void supplyReturn() throws Exception {
        final JsonObject json = new Auth.Simple(
            System.getProperty("failsafe.cedato.service"),
            System.getProperty("failsafe.cedato.secret")
        ).cedato()
            .reports()
            .supplies()
            .extended()
            .supply(
                Integer.parseInt(
                    System.getProperty("failsafe.cedato.supplyId")
                )
            )
            .range("1507536000", "1507539599")
            .json();
        MatcherAssert.assertThat(
            json.getJsonObject("data")
                .getJsonArray("supplies")
                .getJsonObject(0)
                .getString("supply_name"),
            CoreMatchers.equalTo(
                System.getProperty("failsafe.cedato.supplyName")
            )
        );
    }
}