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

package com.sander.verhagen.frame;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Desktop;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.net.URI;
import java.net.URISyntaxException;

import javax.swing.AbstractAction;
import javax.swing.JOptionPane;
import javax.swing.JTextPane;
import javax.swing.event.MouseInputAdapter;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.Element;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.Style;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyleContext;
import javax.swing.text.StyledDocument;

/**
 * Text pane that easily shows hyperlinks.
 * 
 * @author Sander Verhagen
 */
@SuppressWarnings("serial")
class JHyperlinkTextPane extends JTextPane
{
    private final static String LINK_ACTION_ATTRIBUTE = "linkAction";

    private StyledDocument document;

    private Style regular;

    private Style hyperlink;

    public JHyperlinkTextPane()
    {
        addMouseListener(new TextClickListener());
        addMouseMotionListener(new TextMotionListener());
        setEditable(false);

        regular = StyleContext.getDefaultStyleContext().getStyle(StyleContext.DEFAULT_STYLE);

        document = getStyledDocument();
        hyperlink = document.addStyle("hyperlink", regular);
        StyleConstants.setForeground(hyperlink, Color.BLUE);
        StyleConstants.setUnderline(hyperlink, true);

        setCaretPosition(0);
    }

    public void addText(String text)
    {
        try
        {
            document.insertString(document.getLength(), text, regular);
        }
        catch (BadLocationException exception)
        {
            ; // should not happen as position is determined on basis of the document itself
        }
    }

    public void addHyperlinkText(String text, String uri) throws URISyntaxException
    {
        addHyperlinkText(text, new URI(uri));
    }

    public void addHyperlinkText(String text, URI uri)
    {
        Style localHyperlink = document.addStyle(null, hyperlink);
        localHyperlink.addAttribute(LINK_ACTION_ATTRIBUTE, new URLLinkAction(uri));
        try
        {
            document.insertString(document.getLength(), text, localHyperlink);
        }
        catch (BadLocationException exception)
        {
            ; // should not happen as position is determined on basis of the document itself
        }
    }

    private class TextClickListener extends MouseAdapter
    {
        @Override
        public void mouseClicked(MouseEvent event)
        {
            int viewToModel = viewToModel(event.getPoint());
            Element element = document.getCharacterElement(viewToModel);
            AttributeSet attributeSet = element.getAttributes();
            URLLinkAction action = (URLLinkAction) attributeSet.getAttribute(LINK_ACTION_ATTRIBUTE);
            if (action != null)
            {
                action.execute();
            }
        }
    }

    private class TextMotionListener extends MouseInputAdapter
    {
        @Override
        public void mouseMoved(MouseEvent e)
        {
            int viewToModel = viewToModel(e.getPoint());
            Element element = document.getCharacterElement(viewToModel);
            AttributeSet attributeSet = element.getAttributes();
            if (attributeSet.getAttribute(LINK_ACTION_ATTRIBUTE) == null)
            {
                setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
            }
            else
            {
                setCursor(new Cursor(Cursor.HAND_CURSOR));
            }
        }
    }

    private class URLLinkAction extends AbstractAction
    {
        private URI uri;

        URLLinkAction(URI uri)
        {
            this.uri = uri;
        }

        protected void execute()
        {
            try
            {
                Desktop.getDesktop().browse(uri);
            }
            catch (Exception exception)
            {
                JOptionPane.showMessageDialog(JHyperlinkTextPane.this, "Cannot open URI: " + uri);
            }
        }

        public void actionPerformed(ActionEvent e)
        {
            execute();
        }
    }

    public void setAlignment(int alignment)
    {
        SimpleAttributeSet attributeSet = new SimpleAttributeSet();
        StyleConstants.setAlignment(attributeSet, alignment);
        document.setParagraphAttributes(0, document.getLength(), attributeSet, false);
    }
}