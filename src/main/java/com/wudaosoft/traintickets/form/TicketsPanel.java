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
import java.awt.event.ActionEvent;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSpinner;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.ListSelectionModel;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.JTableHeader;

import com.wudaosoft.traintickets.Action;
import com.wudaosoft.traintickets.model.TrainInfoRow;
import com.wudaosoft.traintickets.model.TrainInfoTableModel;

/** 
 * @author Changsoul Wu
 * 
 */
public class TicketsPanel extends JPanel{

	private static final long serialVersionUID = -3275367955942443332L;
	
	private final Action action = Action.getInstance();
	
	private JTable trainTable;
	
	private JButton btnTicket;

	private JTextArea msgTextArea;

	private JScrollPane msgTextPane;
	
	private JSpinner timeSelector;

	private StringBuffer msgBuffer;
	
	private TrainInfoTableModel trainModel;
	
	private JPanel northPane;
	private JPanel centerhPane;
	private JPanel southPane;
	
	public TicketsPanel() {
		setLayout(new BorderLayout());
		
		northPane =new JPanel();
		centerhPane =new JPanel();
		southPane=new JPanel();
		
		northPane.setLayout(new BorderLayout());
        centerhPane.setLayout(new BorderLayout());
        southPane.setLayout(new BorderLayout());
        
        initTrainTable();
        
        northPane.add(new JButton("asdfasdff"), BorderLayout.CENTER);
        
        add(northPane,BorderLayout.NORTH);
        add(centerhPane, BorderLayout.CENTER);
        add(southPane,BorderLayout.SOUTH);
	}
	
	public void initTrainTable() {
		trainModel = new TrainInfoTableModel();

		trainTable = new JTable(trainModel); // 创建一个列表框
		trainTable.setSelectionMode(ListSelectionModel.SINGLE_INTERVAL_SELECTION);// 一次只能选择一个列表索引
//		trainTable.setBorder(BorderFactory.createTitledBorder("车次列表"));
		trainTable.setRowHeight(50);
//		trainTable.setSelectionBackground(UIManager.getColor("Tree.textBackground"));
		trainTable.setCellSelectionEnabled(false);
		trainTable.setColumnSelectionAllowed(false);
		trainTable.setRowSelectionAllowed(false);
		DefaultTableCellRenderer tcr = new DefaultTableCellRenderer();// 设置table内容居中  
        // tcr.setHorizontalAlignment(JLabel.CENTER);  
        tcr.setHorizontalAlignment(SwingConstants.CENTER);// 这句和上句作用一样  
        trainTable.setDefaultRenderer(Object.class, tcr);
        
        JTableHeader tableHeader = trainTable.getTableHeader();  
        tableHeader.setReorderingAllowed(false);//表格列不可移动  
        DefaultTableCellRenderer hr = (DefaultTableCellRenderer) tableHeader  
                .getDefaultRenderer();  
        hr.setHorizontalAlignment(DefaultTableCellRenderer.CENTER);//列名居中 

		MyEvent priceEvent = new MyEvent() {
			@Override
			public void invoke(ActionEvent e) {
				MyButton button = (MyButton) e.getSource();
				//loginClick(button);
				
				
			}
		};

		MyEvent buyEvent = new MyEvent() {
			@Override
			public void invoke(ActionEvent e) {
				MyButton button = (MyButton) e.getSource();
				//grabSingleBuzu(button);
			}
		};

		MyButtonRendererAndEditor btnPrice = new MyButtonRendererAndEditor("票价", priceEvent);
		MyButtonRendererAndEditor btnBuy = new MyButtonRendererAndEditor("预定", buyEvent);

		trainTable.getColumnModel().getColumn(15).setCellRenderer(btnPrice);
		trainTable.getColumnModel().getColumn(15).setCellEditor(btnPrice);

		trainTable.getColumnModel().getColumn(16).setCellRenderer(btnBuy);
		trainTable.getColumnModel().getColumn(16).setCellEditor(btnBuy);
		
		centerhPane.add(new JScrollPane(trainTable), BorderLayout.CENTER);
	}

	public void setTrainInfoRow(List<TrainInfoRow> trainInfoRows) {
		trainModel.setTrainInfoRow(trainInfoRows);
		updateUI();
	}
}
