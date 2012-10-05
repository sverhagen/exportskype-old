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

import static org.junit.Assert.*;

import org.junit.Test;

/**
 * Test for {@link TrillianOutputHandler}.
 * 
 * @author Sander Verhagen
 */
public class TrillianOutputHandlerTest
{
    /**
     * Test for <code>TrillianOutputHandler.testGetValidFileName</code>.
     */
    @Test
    public void testGetValidFileName()
    {
        String original;
        String result;

        original = "#sander.verhagen/$some.user;d2bee9fda29d99bc";
        result = TrillianOutputHandler.getValidFileName(original);
        assertEquals("#sander.verhagen$d2bee9fda29d99bc", result);

        original = "#sander.verhagen/$457c96a25e9b9bf5";
        result = TrillianOutputHandler.getValidFileName(original);
        assertEquals("#sander.verhagen$457c96a25e9b9bf5", result);
    }

}
