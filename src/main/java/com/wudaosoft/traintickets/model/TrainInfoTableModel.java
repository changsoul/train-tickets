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
package com.wudaosoft.traintickets.model;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import javax.swing.table.AbstractTableModel;

/**
 * @author changsoul.wu
 *
 */
@SuppressWarnings("serial")
public class TrainInfoTableModel extends AbstractTableModel {

	private static final List<String> culomnNames = new ArrayList<String>(16);

	private List<TrainInfoRow> trainInfoRows;

	public TrainInfoTableModel() {
		init();
		this.trainInfoRows = new LinkedList<TrainInfoRow>();
	}

	/**
	 * @param ustrainInfoRowsers
	 */
	public TrainInfoTableModel(List<TrainInfoRow> ustrainInfoRowsers) {
		init();
		this.trainInfoRows = ustrainInfoRowsers;
	}

	public void addTrainInfoRow(TrainInfoRow trainInfoRows) {
		this.trainInfoRows.add(trainInfoRows);
	}

	public void setTrainInfoRow(List<TrainInfoRow> trainInfoRows) {
		this.trainInfoRows.clear();
		this.trainInfoRows.addAll(trainInfoRows);
	}

	public List<TrainInfoRow> getTrainInfoRows() {
		return trainInfoRows;
	}

	public TrainInfoRow getTrainInfoRow(int row) {
		return trainInfoRows.get(row);
	}

	@Override
	public Object getValueAt(int row, int column) {

		TrainInfoRow rowInfo = trainInfoRows.get(row);
		TrainInfo train = rowInfo.getQueryLeftNewDTO();
		if (train == null)
			return null;

		Object value = null;

		switch (column) {
		case 0:
			value = train.getStationTrainCode();
			break;
		case 1:
			// value = train.getFromStationName() + " - " +
			// train.getToStationName();
			break;
		case 2:
			value = train.getStartTime() + " - " + train.getArriveTime();
			break;
		case 3:
			value = train.getLishi();
			break;
		case 4:
			if (!"--".equals(train.getSwzNum()) && !"0".equals(train.getSwzNum()) && !"无".equals(train.getSwzNum())) {
				value = getCellValue(train, train.getSwzNum(), "SWZ_", "91");
			} else {
				if (!"--".equals(train.getTzNum()) && !"0".equals(train.getTzNum()) && !"无".equals(train.getTzNum())) {
					value = getCellValue(train, train.getTzNum(), "TZ_", "P1");
				} else {
					if ("无".equals(train.getSwzNum())) {
						value = getCellValue(train, train.getSwzNum(), "SWZ_", "91");
					} else {
						value = getCellValue(train, train.getTzNum(), "TZ_", "P1");
					}
				}
			}
			break;
		case 5:
			value = getCellValue(train, train.getZyNum(), "ZY_", "M1");
			break;
		case 6:
			value = getCellValue(train, train.getZeNum(), "ZE_", "O1");
			break;
		case 7:
			value = getCellValue(train, train.getGrNum(), "GR_", "61");
			break;
		case 8:
			value = getCellValue(train, train.getRwNum(), "RW_", "41");
			break;
		case 9:
			value = getCellValue(train, train.getSrrbNum(), "SRRB_", "F1");
			break;
		case 10:
			value = getCellValue(train, train.getYwNum(), "YW_", "31");
			break;
		case 11:
			value = getCellValue(train, train.getRzNum(), "RZ_", "21");
			break;
		case 12:
			value = getCellValue(train, train.getYzNum(), "YZ_", "11");
			break;
		case 13:
			value = getCellValue(train, train.getWzNum(), "WZ_", "W1");
			break;
		case 14:
			value = getCellValue(train, train.getQtNum(), "QT_", "");
			break;
		// case 15:
		// value = rowInfo.getButtonTextInfo();
		// break;
		default:
			break;
		}

		return value;
	}

	@Override
	public int getRowCount() {
		return trainInfoRows.size();
	}

	@Override
	public int getColumnCount() {
		return 17;
	}

	@Override
	public boolean isCellEditable(int rowIndex, int columnIndex) {
		return (columnIndex == 15 || columnIndex == 16) ? true : false;
	}

	@Override
	public String getColumnName(int column) {
		String name = culomnNames.get(column);

		if (name == null)
			name = super.getColumnName(column);

		return name;
	}

	private Object getCellValue(TrainInfo train, String cn, String cv, String cu) {

		String v2 = getTicketNum(train, cn, cv, cu);
		String v = v2.replaceAll(" [<|\\(].+[\\)|>]", "");
		
		if ("有".equals(v)) {
			return "<html><span style=\"color:#26a306; font-weight: 700\">" + v2 + "</span></html>";
		} else if (hasTickets(v)) {
			return "<html><span style=\"color:#333333; font-weight: 700\">" + v2+ "</span></html>";
		} else {
			return v2;
		}
	}

	private String getTicketNum(TrainInfo train, String cn, String cv, String cu) {
		String co = train.getYpEx() != null ? train.getYpEx().replace("F", "4").replace("A", "6") : null;
		String cp = train.getControlledTrainFlag();
		int ct = (co != null && !"".equals(cu)) ? co.indexOf(cu) : -1;
		int cnNum = 0;

		try {
			cnNum = Integer.parseInt(cn);
		} catch (NumberFormatException e) {
		}

		boolean cs = false;

		if (ct > -1 && (ct % 2) == 0) {
			cs = true;
		}

		if ("1".equals(cp) || "2".equals(cp)) {
			return cn;
		} else {
			if ("有".equals(cn)) {
				if (cv.equals("SWZ_") || cv.equals("TZ_")) {
					if (cs) {
						return cnNum + "折";
					} else {
						return cn;
					}
				} else {
					if (cv.equals("ZY_") || cv.equals("ZE_")) {
						if (cs) {
							return cnNum + "折";
						} else {
							return cn;
						}
					} else {
						if (cs) {
							return cnNum + "折";
						} else {
							return cn;
						}
					}
				}
			} else {
				if (cn.equals("无") || cnNum >= 0) {
					if (cv.equals("SWZ_") || cv.equals("TZ_") || cv.equals("ZY_") || cv.equals("ZE_")) {
						if (cs) {
							return cnNum + "折";
						} else {
							return cn;
						}
					} else {
						if (cs) {
							return cnNum + "折";
						} else {
							return cn;
						}
					}
				} else {
					return cn;
				}
			}
		}
	}

	private void init() {
		culomnNames.add("车次");
		culomnNames.add("出发站/到达站");
		culomnNames.add("发时/到时");
		culomnNames.add("历时");
		culomnNames.add("商/特座");
		culomnNames.add("一等座");
		culomnNames.add("二等座");
		culomnNames.add("高级软卧");
		culomnNames.add("软卧");
		culomnNames.add("动卧");
		culomnNames.add("硬卧");
		culomnNames.add("软座");
		culomnNames.add("硬座");
		culomnNames.add("无座");
		culomnNames.add("其他");
		culomnNames.add("票价");
		culomnNames.add("备注");
	}

	private boolean hasTickets(String input) {
		if ("有".equals(input))
			return true;

		for (int i = 0; i < input.length(); i++) {
			if (!Character.isDigit(input.charAt(i))) {
				return false;
			}
		}
		return true;
	}
}
