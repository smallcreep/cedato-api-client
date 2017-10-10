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

package com.github.smallcreep.text;

import org.cactoos.TextHasString;
import org.cactoos.text.StringAsText;
import org.hamcrest.MatcherAssert;
import org.junit.Test;

/**
 * Test Case for {@link TextAsBase64}.
 * @author Ilia Rogozhin (ilia.rogozhin@gmail.com)
 * @version $Id$
 * @since 0.1
 * @checkstyle MethodNameCheck (500 lines)
 */
public final class TextAsBase64Test {

    /**
     * Check that string correct encode to Base64.
     * @throws Exception If fails
     */
    @Test
    public void stringToBase64() throws Exception {
        MatcherAssert.assertThat(
            "The string doesn't correct encode to Base64",
            new TextAsBase64("test"),
            new TextHasString("dGVzdA==")
        );
    }

    /**
     * Check that text correct encode to Base64.
     * @throws Exception If fails
     */
    @Test
    public void textToBase64() throws Exception {
        MatcherAssert.assertThat(
            "The text doesn't correct encode to Base64",
            new TextAsBase64(
                new StringAsText("test")
            ),
            new TextHasString("dGVzdA==")
        );
    }
}