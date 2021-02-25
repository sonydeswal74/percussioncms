/*
 *     Percussion CMS
 *     Copyright (C) 1999-2021 Percussion Software, Inc.
 *
 *     This program is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU Affero General Public License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version.
 *
 *     This program is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU Affero General Public License for more details.
 *
 *     Mailing Address:
 *
 *      Percussion Software, Inc.
 *      PO Box 767
 *      Burlington, MA 01803, USA
 *      +01-781-438-9900
 *      support@percussion.com
 *      https://www.percusssion.com
 *
 *     You should have received a copy of the GNU Affero General Public License along with this program.  If not, see <https://www.gnu.org/licenses/>
 */

package com.percussion.utils.date;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Thread safe wrapper for SimpleDateFormat.
 */
public class PSConcurrentDateFormat {

    private String defaultFormat="yyyy MM dd";

    public PSConcurrentDateFormat(String format){

    }
    private ThreadLocal<DateFormat> df = new ThreadLocal<DateFormat> () {

        @Override
        public DateFormat get() {
            return super.get();
        }

        @Override
        protected DateFormat initialValue() {
            return new SimpleDateFormat(defaultFormat);
        }

        @Override
        public void remove() {
            super.remove();
        }

        @Override
        public void set(DateFormat value) {
            super.set(value);
        }

    };

    public Date toDate(String dateString) throws ParseException {
        return df.get().parse(dateString);
    }

    public String toString(Date d) {
        return df.get().format(d);
    }

}