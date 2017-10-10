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
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.atomic.AtomicReference;
import javax.json.JsonObject;

/**
 * Smart Supply.
 * @author Ilia Rogozhin (ilia.rogozhin@gmail.com)
 * @version $Id$
 * @since 0.1
 */
public final class SmartSupply extends SupWrap {

    /**
     * Origin Supply.
     */
    private final Supply origin;

    /**
     * Json object.
     */
    private final AtomicReference<JsonObject> json;

    /**
     * Ctor.
     * @param origin Origin Supply
     */
    public SmartSupply(final Supply origin) {
        super(origin);
        this.origin = origin;
        this.json = new AtomicReference<>();
    }

    @Override
    public JsonObject json() throws IOException {
        if (this.json.get() == null) {
            this.json.set(this.origin.json());
        }
        return this.json.get();
    }

    public String id() throws IOException {
        return this.json().getString("supply_id");
    }

    public int impressions() throws IOException {
        return Integer.parseInt(this.json().getString("impressions"));
    }

    public int adStarts() throws IOException {
        return Integer.parseInt(this.json().getString("ad_starts"));
    }

    public int hour() throws IOException {
        return Integer.parseInt(this.json().getString("hour"));
    }

    public LocalDate day() throws IOException {
        return LocalDate.parse(
            this.json().getString("day"),
            DateTimeFormatter.BASIC_ISO_DATE
        );
    }

    public String subId() throws IOException {
        return this.json().getString("sub_id");
    }

    public double revenue() throws IOException {
        return Double.parseDouble(this.json().getString("revenue"));
    }
}
