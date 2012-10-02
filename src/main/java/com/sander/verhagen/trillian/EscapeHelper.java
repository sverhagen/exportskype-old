/**
 * Copyright 2012 Sander Verhagen
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 */

package com.sander.verhagen.trillian;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

/**
 * Helper to escape in the style of Trillian XML log format, which seems a little awkward.
 * 
 * @author Sander Verhagen
 */
public class EscapeHelper
{
    /**
     * Hidden constructor.
     */
    private EscapeHelper()
    {
        ;
    }

    /**
     * Escape the given string for use in Trillian XML log format.
     * @param original original string
     * @return escaped string
     */
    public static String escape(String original)
    {
        String escaped = new String(original);
        try
        {
            escaped = URLEncoder.encode(escaped, "utf-8");
        }
        catch (UnsupportedEncodingException exception)
        {
            throw new RuntimeException(exception);
        }
        return escaped.replace("+", "%20");
    }
}
