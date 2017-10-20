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
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;

import com.wudaosoft.traintickets.model.TrainInfo;
import com.wudaosoft.traintickets.model.TrainInfoTableModel;

/**
 * @author Changsoul Wu
 * 
 */
public class TrainTableCellRenderer extends DefaultTableCellRenderer {

	public static final Color bg1 = Color.decode("#eef1f8");
	public static final Color fg1 = Color.decode("#999999");
	public static final Color bd = Color.decode("#b0cedd");

	private static final long serialVersionUID = -2311196188678249405L;

	private ImageIcon iconIDC;

	public TrainTableCellRenderer() {
		super();
		setHorizontalTextPosition(JLabel.LEFT);
		try {
			BufferedImage imgTmp = ImageIO.read(this.getClass().getResource("/icon.png"));
			iconIDC = new ImageIcon(imgTmp.getSubimage(0, 100, 16, 13));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

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

		if (column >= 0 && column <= 3) {
			super.setForeground(table.getForeground());
		} else {
			super.setForeground(fg1);
		}

		if (column == 0) {
			TrainInfo train = ((TrainInfoTableModel) table.getModel()).getTrainInfoRow(row).getQueryLeftNewDTO();

			if (train.getIsSupportCard()) {
				setIcon(iconIDC);
			} else {
				setIcon(null);
			}

			if ("1".equals(train.getControlledTrainFlag()) || "2".equals(train.getControlledTrainFlag())) {
				setValue("<html><span style=\"color:red; font-size:12px; font-weight: 700\">" + value
						+ "</span></html>");
			} else {
				setValue("<html><span style=\"color:#26a306; font-size:12px; font-weight: 700\">" + value
						+ "</span></html>");
			}
		} else {
			setIcon(null);
			setValue(value);
		}

		return this;
	}

}
