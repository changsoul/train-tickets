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
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.Serializable;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
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
	private JLabel lbC = new JLabel();
	private JLabel lbE = new JLabel();
	
	private ImageIcon iconS;
	private ImageIcon iconP;
	private ImageIcon iconE;
	
	public StationTableCellRenderer() {
		super();
		setLayout(new FlowLayout(FlowLayout.CENTER, 5, 11));
		
		lbS.setIconTextGap(3);
		lbE.setIconTextGap(3);
		lbE.setHorizontalTextPosition(JLabel.LEFT);
		add(lbS);
		add(lbC);
		add(lbE);
		
		try {
			BufferedImage imgTmp = ImageIO.read(this.getClass().getResource("/icon.png"));
			iconS = new ImageIcon(imgTmp.getSubimage(0, 546, 14, 18));
			iconP = new ImageIcon(imgTmp.getSubimage(0, 446, 14, 18));
			iconE = new ImageIcon(imgTmp.getSubimage(0, 496, 14, 18));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/* (non-Javadoc)
	 * @see javax.swing.table.TableCellRenderer#getTableCellRendererComponent(javax.swing.JTable, java.lang.Object, boolean, boolean, int, int)
	 */
	@Override
	public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus,
			int row, int column) {
		
		if (row % 2 == 0) {
			super.setBackground(TrainTableCellRenderer.bg1);
		} else {
			super.setBackground(table.getBackground());
		}
		
		TrainInfo train = ((TrainInfoTableModel)table.getModel()).getTrainInfoRow(row).getQueryLeftNewDTO();
		
		if (train.getFromStationTelecode() != null && train.getFromStationTelecode().equals(train.getStartStationTelecode())) {
			lbS.setIcon(iconS);
		} else {
			lbS.setIcon(iconP);
		}
		
		if (train.getToStationTelecode() != null && train.getToStationTelecode().equals(train.getEndStationTelecode())) {
			lbE.setIcon(iconE);
		} else {
			lbS.setIcon(iconP);
		}
		
		lbS.setText(train.getFromStationName());
		lbC.setText((Integer.parseInt(train.getToStationNo()) - Integer.parseInt(train.getFromStationNo())) + "");
		lbE.setText(train.getToStationName());
		return this;
	}

}
