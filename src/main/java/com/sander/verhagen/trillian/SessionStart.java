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

import com.sander.verhagen.domain.Chat;

/**
 * Session entity of <code>type=&quot;start&quot;</code> in the Trillian XML log format.
 * 
 * @author Sander Verhagen
 */
public class SessionStart extends Session
{
    /**
     * Constructor.
     * 
     * @param chat
     *        total chat ({@link Chat})
     * @param to
     *        user name that is to be treated as communication partner
     */
    public SessionStart(Chat chat, String to)
    {
        super(chat, to);
    }

    public String toXML()
    {
        return toXML("start", getChat().getStart());
    }
}
