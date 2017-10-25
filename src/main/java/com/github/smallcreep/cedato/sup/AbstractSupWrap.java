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

import com.github.smallcreep.cedato.Supply;
import java.io.IOException;
import java.time.ZonedDateTime;
import java.util.Iterator;
import javax.json.JsonObject;

/**
 * Supply wrapper.
 * @author Ilia Rogozhin (ilia.rogozhin@gmail.com)
 * @version $Id$
 * @since 0.1
 */
abstract class AbstractSupWrap implements Supply {

    /**
     * Origin Supply.
     */
    private final Supply origin;

    /**
     * Ctor.
     * @param origin Origin Supply
     */
    AbstractSupWrap(final Supply origin) {
        this.origin = origin;
    }

    @Override
    public Supply group(final String group) {
        return this.origin.group(group);
    }

    @Override
    public Supply range(final ZonedDateTime start, final ZonedDateTime end) {
        return this.origin.range(start, end);
    }

    @Override
    public JsonObject json() throws IOException {
        return this.origin.json();
    }

    @Override
    public Iterator<Supply> iterator() {
        return this.origin.iterator();
    }

    @Override
    public Supply limit(final int limit) {
        return this.origin.limit(limit);
    }
}
