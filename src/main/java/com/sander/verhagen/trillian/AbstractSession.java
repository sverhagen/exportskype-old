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
 * Session entity in the Trillian XML log format.
 * 
 * @author Sander Verhagen
 */
abstract public class AbstractSession implements XML
{
    private Chat chat;

    private String to;

    /**
     * Constructor.
     * 
     * @param chat
     *        total chat ({@link Chat})
     * @param to
     *        user name that is to be treated as communication partner
     */
    public AbstractSession(Chat chat, String to)
    {
        this.chat = chat;
        this.to = to;
    }

    /**
     * Format session entity XML. <br/>
     * <br/>
     * Looks like: <code>&lt;session type=&quot;stop&quot; time=&quot;1348768766&quot;
     * ms=&quot;128&quot; medium=&quot;SKYPE&quot; to=&quot;some.user&quot;
     * from=&quot;sander.verhagen&quot;/&gt;</code>
     * 
     * @param type
     *        type of the session entity, either <code>start</code> or <code>stop</code>
     * @param time
     *        time of the session start/stop
     * @return entity XML
     */
    protected String toXML(String type, long time)
    {
        StringBuilder result = new StringBuilder();
        result.append("<session ");
        result.append("type=\"" + type + "\" ");
        result.append("time=\"" + time + "\" ");
        result.append("medium=\"SKYPE\" ");
        // TODO: can it never happen that to and from end up being the same?
        result.append("to=\"" + EscapeHelper.escape(this.to) + "\" ");
        result.append("from=\"" + EscapeHelper.escape(chat.getFrom()) + "\" ");
        result.append("/>");
        return result.toString();
    }

    protected Chat getChat()
    {
        return chat;
    }
}
