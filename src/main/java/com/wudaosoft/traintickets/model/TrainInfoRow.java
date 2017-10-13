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

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.List;

/**
 * @author Changsoul Wu
 * 
 */
public class TrainInfoRow {

	private String secretStr;

	private String buttonTextInfo;

	private TrainInfo queryLeftNewDTO;

	public TrainInfoRow() {
		super();
	}

	public String getSecretStr() {
		return secretStr;
	}

	public void setSecretStr(String secretStr) {
//		this.secretStr = secretStr;
		if(secretStr == null)
			return;
		try {
			this.secretStr = URLDecoder.decode(secretStr, "UTF-8");
		} catch (UnsupportedEncodingException e) {
		}
	}

	public String getButtonTextInfo() {
		return buttonTextInfo;
	}

	public void setButtonTextInfo(String buttonTextInfo) {
		this.buttonTextInfo = buttonTextInfo;
	}

	public TrainInfo getQueryLeftNewDTO() {
		return queryLeftNewDTO;
	}

	public void setQueryLeftNewDTO(TrainInfo queryLeftNewDTO) {
		this.queryLeftNewDTO = queryLeftNewDTO;
	}
	
	@Override
	public String toString() {
		return "TicketRow [secretStr=" + secretStr + ", buttonTextInfo=" + buttonTextInfo + ", queryLeftNewDTO="
				+ queryLeftNewDTO + "]";
	}

	public static TrainInfoRow fromData(String row) {
		
		String[] cells = row.split("\\|");
		if(cells.length < 35)
			return null;
		
		TrainInfoRow tRow = new TrainInfoRow();
		TrainInfo ins = new TrainInfo();
		
		ins.setTrainNo(cells[2]);
		ins.setStationTrainCode(cells[3]);
		ins.setStartStationTelecode(cells[4]);
		ins.setEndStationTelecode(cells[5]);
		ins.setFromStationTelecode(cells[6]);
		ins.setToStationTelecode(cells[7]);
		ins.setStartTime(cells[8]);
		ins.setArriveTime(cells[9]);
		ins.setLishi(cells[10]);
		ins.setCanWebBuy("Y".equals(cells[11]));
		ins.setYpInfo(cells[12]);
		ins.setStartTrainDate(cells[13]);
		ins.setTrainSeatFeature(cells[14]);
		ins.setLocationCode(cells[15]);
		ins.setFromStationNo(cells[16]);
		ins.setToStationNo(cells[17]);
		ins.setIsSupportCard(!"0".equals(cells[18]));
		ins.setControlledTrainFlag(cells[19]);
		ins.setGgNum(cells[20]);
		ins.setGrNum(cells[21]);
		ins.setQtNum(cells[22]);
		ins.setRwNum(cells[23]);
		ins.setRzNum(cells[24]);
		ins.setTzNum(cells[25]);
		ins.setWzNum(cells[26]);
		ins.setYbNum(cells[27]);
		ins.setYwNum(cells[28]);
		ins.setYzNum(cells[29]);
		ins.setZeNum(cells[30]);
		ins.setZyNum(cells[31]);
		ins.setSwzNum(cells[32]);
		ins.setSrrbNum(cells[33]);
		ins.setYpEx(cells[34]);
		ins.setSeatTypes(cells[35]);
		
		List<Stations> fStations = Stations.search(cells[6]);
		List<Stations> tStations = Stations.search(cells[7]);
		
		ins.setFromStationName(fStations.isEmpty() ? "" : fStations.get(0).getName());
		ins.setToStationName(tStations.isEmpty() ? "" : tStations.get(0).getName());
		
		tRow.setSecretStr(cells[0]);
		tRow.setButtonTextInfo(cells[1]);
		tRow.setQueryLeftNewDTO(ins);
		
		return tRow;
	}
}
