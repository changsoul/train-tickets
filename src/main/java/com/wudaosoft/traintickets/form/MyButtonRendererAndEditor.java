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

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;
import java.util.Map;

import javax.swing.DefaultCellEditor;
import javax.swing.JButton;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.TableCellRenderer;

/**
 * @author changsoul.wu
 *
 */
@SuppressWarnings("serial")
public class MyButtonRendererAndEditor extends DefaultCellEditor implements TableCellRenderer, ActionListener {

	private MyEvent event;
	
	private Map<String, MyButton> buttons;

	private String text;

	public MyButtonRendererAndEditor(String text) {
		super(new JTextField());
		setClickCountToStart(1);
		this.buttons = new HashMap<>(30);
		this.text = text;
	}

	public MyButtonRendererAndEditor(String text, MyEvent e) {
		this(text);
		this.event = e;
	}
	
	/* (non-Javadoc)
	 * @see javax.swing.table.TableCellRenderer#getTableCellRendererComponent(javax.swing.JTable, java.lang.Object, boolean, boolean, int, int)
	 */
	@Override
	public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus,
			int row, int column) {
		return getButton(table, row, column);
	}

	/*
	 * 重写编辑器方法，返回一个按钮给JTable
	 */
	@Override
	public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
		return getButton(table, row, column);
	}

	@Override
	public Object getCellEditorValue() {
		return null;
	}

	/* (non-Javadoc)
	 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
	 */
	@Override
	public void actionPerformed(ActionEvent e) {
		event.invoke(e);
	}

	private JButton getButton(JTable table, int row, int column) {
		synchronized (this) {
			String key = row + "" + column + "";
			MyButton button = buttons.get(key);
			if (button == null) {
				button = new MyButton(text);
				button.setRow(row);
				button.setColumn(column);
				button.setUserInfo(((UserTableModel)table.getModel()).getUserInfo(row));
				button.addActionListener(this);
				buttons.put(key, button);
			}

			return button;
		}
	}
}
