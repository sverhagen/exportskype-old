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

import java.awt.Component;
import java.awt.font.FontRenderContext;
import java.awt.font.LineBreakMeasurer;
import java.text.AttributedCharacterIterator;
import java.text.AttributedString;

import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.table.TableCellRenderer;

import org.apache.commons.lang3.StringUtils;

/**
 * {@link TableCellRenderer} implementation that wraps text contents of cells.
 * 
 * @author Sander Verhagen
 */
@SuppressWarnings("serial")
class LineWrapCellRenderer extends JTextArea implements TableCellRenderer
{
    public LineWrapCellRenderer()
    {
        setLineWrap(true);
        setOpaque(true);
        setWrapStyleWord(true);
    }

    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected,
            boolean hasFocus, int row, int column)
    {
        String text = value.toString();
        setForeground(isSelected ? table.getSelectionForeground() : table.getForeground());
        setBackground(isSelected ? table.getSelectionBackground() : table.getBackground());
        int columnWidth = table.getColumnModel().getColumn(column).getWidth();
        setSize(columnWidth, 0 /* don't know yet */);
        setText(text);

        int fontHeight = this.getFontMetrics(this.getFont()).getHeight();
        if (StringUtils.isEmpty(text))
        {
            table.setRowHeight(row, fontHeight);
        }
        else
        {
            table.setRowHeight(row, fontHeight * getLineCount(this));
        }
        return this;
    }

    private int getLineCount(JTextArea textArea)
    {
        AttributedString string = new AttributedString(textArea.getText());
        FontRenderContext fontRenderContext =
                textArea.getFontMetrics(textArea.getFont()).getFontRenderContext();
        AttributedCharacterIterator characterIterator = string.getIterator();
        LineBreakMeasurer lineBreakMeasurer =
                new LineBreakMeasurer(characterIterator, fontRenderContext);
        lineBreakMeasurer.setPosition(characterIterator.getBeginIndex());

        int lineCount = 0;
        while (lineBreakMeasurer.getPosition() < characterIterator.getEndIndex())
        {
            lineBreakMeasurer.nextLayout(textArea.getSize().width);
            lineCount++;
        }
        return lineCount;
    }
}