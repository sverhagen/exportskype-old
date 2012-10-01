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

import org.apache.commons.lang.StringEscapeUtils;

import com.sander.verhagen.domain.Chat;

/**
 * Session entity in the Trillian XML log format.
 * 
 * @author Sander Verhagen
 */
abstract public class Session implements XML
{
    private Chat chat;

    /**
     * Constructor.
     * 
     * @param chat
     *        total chat ({@link Chat})
     */
    public Session(Chat chat)
    {
        this.chat = chat;
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
        result.append("to=\"" + StringEscapeUtils.escapeXml(chat.getTo()) + "\" ");
        result.append("from=\"" + StringEscapeUtils.escapeXml(chat.getFrom()) + "\" ");
        result.append("/>");
        return result.toString();
    }

    protected Chat getChat()
    {
        return chat;
    }
}