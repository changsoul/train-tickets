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
import java.awt.FlowLayout;
import java.io.Serializable;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.table.TableCellRenderer;

import com.wudaosoft.traintickets.model.TrainInfo;
import com.wudaosoft.traintickets.model.TrainInfoTableModel;

/** 
 * @author Changsoul Wu
 * 
 */
public class StationTableCellRenderer extends JPanel implements TableCellRenderer, Serializable{

	private static final long serialVersionUID = 4998499295179511220L;
	
	private JLabel lbS = new JLabel();
	private JLabel lbE = new JLabel();
	
	public StationTableCellRenderer() {
		super();
		setLayout(new FlowLayout(FlowLayout.CENTER));
		add(lbS);
		add(lbE);
	}

	/* (non-Javadoc)
	 * @see javax.swing.table.TableCellRenderer#getTableCellRendererComponent(javax.swing.JTable, java.lang.Object, boolean, boolean, int, int)
	 */
	@Override
	public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus,
			int row, int column) {
		
		TrainInfo trian = ((TrainInfoTableModel)table.getModel()).getTrainInfoRow(row).getQueryLeftNewDTO();
		
		if (trian.getFromStationTelecode() != null && trian.getFromStationTelecode().equals(trian.getStartStationTelecode())) {
			
		} else {
			
		}
		
		if (trian.getToStationTelecode() != null && trian.getToStationTelecode().equals(trian.getEndStationTelecode())) {
			
		} else {
			
		}
		
		lbS.setText(trian.getFromStationName());
		lbE.setText(trian.getToStationName());
		return this;
	}

}
