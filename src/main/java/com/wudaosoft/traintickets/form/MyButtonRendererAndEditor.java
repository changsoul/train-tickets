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
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.TableCellRenderer;

import com.wudaosoft.traintickets.model.TrainInfoTableModel;

/**
 * @author changsoul.wu
 *
 */
@SuppressWarnings("serial")
public class MyButtonRendererAndEditor extends DefaultCellEditor implements TableCellRenderer, ActionListener {

	private MyEvent event;
	
	private Map<String, MyButton> buttons;

	private String type;
	
	private String text;

	public MyButtonRendererAndEditor(String text) {
		super(new JTextField());
		setClickCountToStart(1);
		this.buttons = new HashMap<String, MyButton>(30);
		this.text = text;
	}

	public MyButtonRendererAndEditor(String text, MyEvent e, String type) {
		this(text);
		this.event = e;
		this.type = type;
	}
	
	/* (non-Javadoc)
	 * @see javax.swing.table.TableCellRenderer#getTableCellRendererComponent(javax.swing.JTable, java.lang.Object, boolean, boolean, int, int)
	 */
	@Override
	public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus,
			int row, int column) {
		
		return getCellComponent(table, value, isSelected, row, column);
	}

	/*
	 * 重写编辑器方法，返回一个按钮给JTable
	 */
	@Override
	public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
		
		return getCellComponent(table, value, isSelected, row, column);
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
		fireEditingStopped();
	}

	private MyButton getButton(JTable table, int row, int column) {
		synchronized (this) {
			String key = row + "" + column + "";
			MyButton button = buttons.get(key);
			if (button == null) {
				button = new MyButton(text, type);
				button.setRow(row);
				button.setColumn(column);
				button.setTrainInfoRow(((TrainInfoTableModel)table.getModel()).getTrainInfoRow(row));
				button.addActionListener(this);
				if (row % 2 == 0) {
					button.setBackground(TrainTableCellRenderer.bg1);
				} else {
					button.setBackground(table.getBackground());
				}
				
				buttons.put(key, button);
			}

			return button;
		}
	}
	
	private Component getCellComponent(JTable table, Object value, boolean isSelected, int row, int column) {
		TrainInfoTableModel model = (TrainInfoTableModel)table.getModel();
		
		if(model.getTrainInfoRow(row).getQueryLeftNewDTO().getCanWebBuy()) {
			return getButton(table, row, column);
		}
		
		String text = column == 15 ? "--" : model.getTrainInfoRow(row).getButtonTextInfo();
		
		JLabel lb = new JLabel(text, JLabel.CENTER);
		lb.setForeground(TrainTableCellRenderer.fg1);
		if (row % 2 == 0) {
			lb.setBackground(TrainTableCellRenderer.bg1);
		} else {
			lb.setBackground(table.getBackground());
		}
		return lb;
	}
}
