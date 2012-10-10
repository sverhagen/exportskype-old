
package com.sander.verhagen.trillian;

import com.sander.verhagen.domain.Chat;
/**
 * Tests for {@link SessionStart}, as mainly implemented by {@link AbstractSessionTest}.
 * 
 * @author Sander Verhagen
 */
public class SessionStartTest extends AbstractSessionTest
{
    @Override
    protected AbstractSession getSession(Chat chat, String to)
    {
        return new SessionStart(chat, to);
    }

    @Override
    protected String getType()
    {
        return "start";
    }

    @Override
    long getExpectedTime(long start, long finish)
    {
        return start;
    }
}
