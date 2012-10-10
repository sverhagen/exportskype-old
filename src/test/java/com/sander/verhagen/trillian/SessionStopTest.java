
package com.sander.verhagen.trillian;

import com.sander.verhagen.domain.Chat;
/**
 * Tests for {@link SessionStop}, as mainly implemented by {@link AbstractSessionTest}.
 * 
 * @author Sander Verhagen
 */
public class SessionStopTest extends AbstractSessionTest
{
    @Override
    protected AbstractSession getSession(Chat chat, String to)
    {
        return new SessionStop(chat, to);
    }

    @Override
    protected String getType()
    {
        return "stop";
    }

    @Override
    long getExpectedTime(long start, long finish)
    {
        return finish;
    }
}
