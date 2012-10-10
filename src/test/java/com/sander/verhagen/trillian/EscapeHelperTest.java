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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.lang.reflect.Constructor;

import org.junit.Test;

/**
 * Test for {@link EscapeHelper}.
 * 
 * @author Sander Verhagen
 */
public class EscapeHelperTest
{
    /**
     * Test for {@link EscapeHelper#escape}. Important is that spaces should not be escaped as
     * &quot;+&quot;
     */
    @Test
    public void testEscapeHelper()
    {
        assertEquals("Test%20%21%21%21", EscapeHelper.escape("Test !!!"));
        assertEquals("How%20are%20you%3F", EscapeHelper.escape("How are you?"));
    }

    /**
     * Test for the private constructor of {@link EscapeHelper}. This is just to not having to keep
     * track of missing code coverage due to code that isn't supposed to be executed in the first
     * place
     * 
     * @throws Exception
     *         something wrong
     */
    @Test
    public void testPrivateConstructor() throws Exception
    {
        Constructor constructor = EscapeHelper.class.getDeclaredConstructor();
        constructor.setAccessible(true);
        constructor.newInstance();
    }

    /**
     * Test that an unsupported encoding throws an exception.
     */
    @Test
    public void testUnsupportedEncoding()
    {
        try
        {
            EscapeHelper.setEncoding("should not exist");
            EscapeHelper.escape("Test");
            fail("should have given RuntimeException wrapping UnsupportedEncodingException");
        }
        catch (RuntimeException exception)
        {
        }
        finally
        {
            EscapeHelper.setEncoding("utf-8");
        }
    }
}
