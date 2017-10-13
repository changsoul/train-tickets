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

	public void AddAllTrainInfoRow(List<TrainInfoRow> trainInfoRows) {
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
		if(train == null)
			return null;
		
		Object value = null;
		
		switch (column) {
			case 0:
				value = train.getStationTrainCode();
				break;
			case 1:
				value = train.getFromStationNo() + "\n" + train.getToStationName();
				break;
			case 2:
				value = train.getStartTime() + "\n" + train.getArriveTime();
				break;
			case 3:
				value = train.getLishi();
				break;
			case 4:
				value = train.getArriveTime();
				break;
			case 5:
				value = train.getLishi();
				break;
			case 6:
				value = train.getStationTrainCode();
				break;
			case 7:
				value = train.getStationTrainCode();
				break;
			case 8:
				value = train.getStationTrainCode();
				break;
			case 9:
				value = train.getStationTrainCode();
				break;
			case 10:
				value = train.getStationTrainCode();
				break;
			case 11:
				value = train.getStationTrainCode();
				break;
			case 12:
				value = train.getStationTrainCode();
				break;
			case 13:
				value = train.getStationTrainCode();
				break;
			case 14:
				value = train.getStationTrainCode();
				break;
			case 15:
				value = train.getStationTrainCode();
				break;
			case 16:
				value = rowInfo.getButtonTextInfo();
				break;
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
		return 6;
	}

	@Override
	public boolean isCellEditable(int rowIndex, int columnIndex) {
		return (columnIndex == 4 || columnIndex == 5) ? true : false;
	}

	@Override
	public String getColumnName(int column) {
		String name = culomnNames.get(column);
		
		if (name == null)
			name = super.getColumnName(column);

		return name;
	}
	
	private Object getTicketNum(TrainInfo train, String cn, String cv, String cu) {
		String co = train.getYpEx() != null ? train.getYpEx().replace("F", "4").replace("A", "6") : null;
		String cp = train.getControlledTrainFlag();
		int ct = co != null ? co.indexOf(cu) : -1;
        boolean cs = false;
        
        if (ct > -1 && (ct % 2) == 0) {
            cs = true;
        }
        
        if ("1".equals(cp) || "2".equals(cp)) {
            cq.push(' <td width="46" align="center" style="cursor: pointer;"  id="');
            cq.push(cv);
            cq.push(cr);
            cq.push('">');
            cq.push(cn);
            cq.push("</td>")
        } else {
            if ("有".equals(cn)) {
                if (cv.equals("SWZ_") || cv.equals("TZ_")) {
                    cq.push('<td width="46" align="center" class="yes" onclick="showTicketPrice(this)"　style="cursor: pointer;" id="');
                    cq.push(cv);
                    cq.push(cr);
                    cq.push('">');
                    if (cs) {
                        cq.push('<div class="sale" title="本席别票价打折">' + cn + '<span class="i-mark">折</span></div>')
                    } else {
                        cq.push(cn)
                    }
                    cq.push("</td>")
                } else {
                    if (cv.equals("ZY_") || cv.equals("ZE_")) {
                        cq.push('<td width="46" align="center" class="yes" style="cursor: pointer;" onclick="showTicketPrice(this)" id="');
                        cq.push(cv);
                        cq.push(cr);
                        cq.push('">');
                        if (cs) {
                            cq.push('<div class="sale" title="本席别票价打折">' + cn + '<span class="i-mark">折</span></div>')
                        } else {
                            cq.push(cn)
                        }
                        cq.push("</td>")
                    } else {
                        cq.push('<td width="46" align="center" style="cursor: pointer;" class="yes" onclick="showTicketPrice(this)" id="');
                        cq.push(cv);
                        cq.push(cr);
                        cq.push('">');
                        if (cs) {
                            cq.push('<div class="sale" title="本席别票价打折">' + cn + '<span class="i-mark">折</span></div>')
                        } else {
                            cq.push(cn)
                        }
                        cq.push("</td>")
                    }
                }
            } else {
                if (cn.equals("无") || isNum(cn) >= 0) {
                    String cm = ' class="t-num" ';
                    if (cn.equals("无") || isNum(cn).equals(0) {
                        cm = ""
                    }
                    if (cv.equals("SWZ_" || cv.equals("TZ_" || cv.equals("ZY_" || cv.equals("ZE_") {
                        cq.push('<td width="46" align="center" style="cursor: pointer;" ' + cm + ' onclick="showTicketPrice(this)" id="');
                        cq.push(cv);
                        cq.push(cr);
                        cq.push('">');
                        cq.push("<div>");
                        if (cs) {
                            cq.push('<div class="sale" title="本席别票价打折">' + cn + '<span class="i-mark">折</span></div>')
                        } else {
                            cq.push(cn)
                        }
                        cq.push("</td>")
                    } else {
                        cq.push('<td width="46" align="center" style="cursor: pointer;" ' + cm + ' onclick="showTicketPrice(this)" id="');
                        cq.push(cv);
                        cq.push(cr);
                        cq.push('">');
                        if (cs) {
                            cq.push('<div class="sale" title="本席别票价打折">' + cn + '<span class="i-mark">折</span></div>')
                        } else {
                            cq.push(cn)
                        }
                        cq.push("</td>")
                    }
                } else {
                    cq.push(' <td width="46" align="center" style="cursor: pointer;" onclick="showTicketPrice(this)"  id="');
                    cq.push(cv);
                    cq.push(cr);
                    cq.push('">');
                    cq.push(cn);
                    cq.push("</td>")
                }
            }
        }
		
		return null;
	}
	
	private void init() {
		culomnNames.add("车次");
		culomnNames.add("出发站/到达站");
		culomnNames.add("出发时间/到达时间");
		culomnNames.add("历时");
		culomnNames.add("商务座/特等座");
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
		culomnNames.add("备注");
	}

}
