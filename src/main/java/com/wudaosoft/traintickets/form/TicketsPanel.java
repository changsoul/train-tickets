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

import java.awt.BorderLayout;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSpinner;
import javax.swing.JTable;
import javax.swing.JTextArea;

import com.wudaosoft.traintickets.Action;
import com.wudaosoft.traintickets.model.TrainInfoTableModel;

/** 
 * @author Changsoul Wu
 * 
 */
public class TicketsPanel extends JPanel{

	private static final long serialVersionUID = -3275367955942443332L;
	
	private final Action action = Action.getInstance();
	
	private JTable ticketTable;
	
	private JButton btnTicket;

	private JTextArea msgTextArea;

	private JScrollPane msgTextPane;
	
	private JSpinner timeSelector;

	private StringBuffer msgBuffer;
	
	private TrainInfoTableModel ticketModel;
	
	private StatusBar statusBar;
	
	public TicketsPanel() {
		setLayout(new BorderLayout());
		
		statusBar = new StatusBar();
        add(statusBar, BorderLayout.SOUTH);
	}

}
