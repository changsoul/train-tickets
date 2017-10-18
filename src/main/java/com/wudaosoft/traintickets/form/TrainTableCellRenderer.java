/**
 *    Copyright 2009-2017 Wudao Software Studio(wudaosoft.com)
 * 
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 * 
 *        http://www.apache.org/licenses/LICENSE-2.0
 * 
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */
package com.wudaosoft.traintickets.form;

import java.awt.Color;
import java.awt.Component;

import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;

/**
 * @author Changsoul Wu
 * 
 */
public class TrainTableCellRenderer extends DefaultTableCellRenderer {

	public static final Color bg1 = Color.decode("#eef1f8");
	public static final Color fg1 = Color.decode("#999999");
	public static final Color bd = Color.decode("#b0cedd");

	private static final long serialVersionUID = -2311196188678249405L;

	@Override
	public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus,
			int row, int column) {

		if (row % 2 == 0) {
			super.setBackground(bg1);
		} else {
			super.setBackground(table.getBackground());
		}
		
		setBorder(noFocusBorder);
		setFont(table.getFont());
		
		if(column >=0 && column <= 3) {
			super.setForeground(table.getForeground());
		} else {
			super.setForeground(fg1);
		}

		setValue(value);

		return this;
	}

}
