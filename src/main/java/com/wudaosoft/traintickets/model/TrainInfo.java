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

import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;

/**
 * @author Changsoul Wu
 * 
 */
public class TrainInfo implements Serializable {

	private static final long serialVersionUID = -1745101650429943460L;
	
	private String trainNo;
	private String stationTrainCode;
	private String startStationTelecode;
	private String endStationTelecode;
	private String fromStationTelecode;
	private String toStationTelecode;
	private String startTime;
	private String arriveTime;
	private String lishi;
	private boolean canWebBuy;
	private String ypInfo;
	private String startTrainDate;
	private String trainSeatFeature;
	private String locationCode;
	private String fromStationNo;
	private String toStationNo;
	private boolean isSupportCard;
	private String controlledTrainFlag;
	private String ggNum; //
	private String grNum;
	private String qtNum;
	private String rwNum;
	private String rzNum;
	private String tzNum;
	private String wzNum;
	private String ybNum;
	private String ywNum;
	private String yzNum;
	private String zeNum;
	private String zyNum;
	private String swzNum;
	private String srrbNum;
	private String ypEx;
	private String seatTypes;
	private String fromStationName;
	private String toStationName;

	public TrainInfo() {
		super();
	}

	public String getTrainNo() {
		return trainNo;
	}

	public void setTrainNo(String trainNo) {
		this.trainNo = trainNo;
	}

	public String getStationTrainCode() {
		return stationTrainCode;
	}

	public void setStationTrainCode(String stationTrainCode) {
		this.stationTrainCode = stationTrainCode;
	}

	public String getStartStationTelecode() {
		return startStationTelecode;
	}

	public void setStartStationTelecode(String startStationTelecode) {
		this.startStationTelecode = startStationTelecode;
	}

	public String getEndStationTelecode() {
		return endStationTelecode;
	}

	public void setEndStationTelecode(String endStationTelecode) {
		this.endStationTelecode = endStationTelecode;
	}

	public String getFromStationTelecode() {
		return fromStationTelecode;
	}

	public void setFromStationTelecode(String fromStationTelecode) {
		this.fromStationTelecode = fromStationTelecode;
	}

	public String getToStationTelecode() {
		return toStationTelecode;
	}

	public void setToStationTelecode(String toStationTelecode) {
		this.toStationTelecode = toStationTelecode;
	}

	public String getStartTime() {
		return startTime;
	}

	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}

	public String getArriveTime() {
		return arriveTime;
	}

	public void setArriveTime(String arriveTime) {
		this.arriveTime = arriveTime;
	}

	public String getLishi() {
		return lishi;
	}

	public void setLishi(String lishi) {
		this.lishi = lishi;
	}

	public boolean getCanWebBuy() {
		return canWebBuy;
	}

	public void setCanWebBuy(boolean canWebBuy) {
		this.canWebBuy = canWebBuy;
	}

	public String getYpInfo() {
		return ypInfo;
	}

	public void setYpInfo(String ypInfo) {
		if(ypInfo == null)
			return;
		try {
			this.ypInfo = URLDecoder.decode(ypInfo, "UTF-8");
		} catch (UnsupportedEncodingException e) {
		}
	}

	public String getStartTrainDate() {
		return startTrainDate;
	}

	public void setStartTrainDate(String startTrainDate) {
		this.startTrainDate = startTrainDate;
	}

	public String getTrainSeatFeature() {
		return trainSeatFeature;
	}

	public void setTrainSeatFeature(String trainSeatFeature) {
		this.trainSeatFeature = trainSeatFeature;
	}

	public String getLocationCode() {
		return locationCode;
	}

	public void setLocationCode(String locationCode) {
		this.locationCode = locationCode;
	}

	public String getFromStationNo() {
		return fromStationNo;
	}

	public void setFromStationNo(String fromStationNo) {
		this.fromStationNo = fromStationNo;
	}

	public String getToStationNo() {
		return toStationNo;
	}

	public void setToStationNo(String toStationNo) {
		this.toStationNo = toStationNo;
	}

	public boolean getIsSupportCard() {
		return isSupportCard;
	}

	public void setIsSupportCard(boolean isSupportCard) {
		this.isSupportCard = isSupportCard;
	}

	public String getControlledTrainFlag() {
		return controlledTrainFlag;
	}

	public void setControlledTrainFlag(String controlledTrainFlag) {
		this.controlledTrainFlag = controlledTrainFlag;
	}

	public String getGgNum() {
		return ggNum;
	}

	public void setGgNum(String ggNum) {
		this.ggNum = !"".equals(ggNum) ? ggNum : "--";
	}

	/**
	 * 高级软卧
	 * @return
	 */
	public String getGrNum() {
		return grNum;
	}

	public void setGrNum(String grNum) {
		this.grNum = !"".equals(grNum) ? grNum : "--";
	}

	/**
	 * 其他座位
	 * @return
	 */
	public String getQtNum() {
		return qtNum;
	}

	public void setQtNum(String qtNum) {
		this.qtNum = !"".equals(qtNum) ? qtNum : "--";
	}

	/**
	 * 软卧
	 * @return
	 */
	public String getRwNum() {
		return rwNum;
	}

	public void setRwNum(String rwNum) {
		this.rwNum = !"".equals(rwNum) ? rwNum : "--";
	}

	/**
	 * 软座
	 * @return
	 */
	public String getRzNum() {
		return rzNum;
	}

	public void setRzNum(String rzNum) {
		this.rzNum = !"".equals(rzNum) ? rzNum : "--";
	}

	/**
	 * 特等座
	 * @return
	 */
	public String getTzNum() {
		return tzNum;
	}

	public void setTzNum(String tzNum) {
		this.tzNum = !"".equals(tzNum) ? tzNum : "--";
	}

	/**
	 * 无座
	 * @return
	 */
	public String getWzNum() {
		return wzNum;
	}

	public void setWzNum(String wzNum) {
		this.wzNum = !"".equals(wzNum) ? wzNum : "--";
	}
	
	public String getYbNum() {
		return ybNum;
	}

	public void setYbNum(String ybNum) {
		this.ybNum = !"".equals(ybNum) ? ybNum : "--";
	}

	/**
	 * 硬卧
	 * @return
	 */
	public String getYwNum() {
		return ywNum;
	}

	public void setYwNum(String ywNum) {
		this.ywNum = !"".equals(ywNum) ? ywNum : "--";
	}

	/**
	 * 硬坐
	 * @return
	 */
	public String getYzNum() {
		return yzNum;
	}

	public void setYzNum(String yzNum) {
		this.yzNum = !"".equals(yzNum) ? yzNum : "--";
	}

	/**
	 * 二等坐
	 * @return
	 */
	public String getZeNum() {
		return zeNum;
	}

	public void setZeNum(String zeNum) {
		this.zeNum = !"".equals(zeNum) ? zeNum : "--";
	}

	/**
	 * 一等坐
	 * @return
	 */
	public String getZyNum() {
		return zyNum;
	}

	public void setZyNum(String zyNum) {
		this.zyNum = !"".equals(zyNum) ? zyNum : "--";
	}

	/**
	 * 商务坐
	 * @return
	 */
	public String getSwzNum() {
		return swzNum;
	}

	public void setSwzNum(String swzNum) {
		this.swzNum = !"".equals(swzNum) ? swzNum : "--";
	}

	/**
	 * 动卧
	 * @return
	 */
	public String getSrrbNum() {
		return srrbNum;
	}

	public void setSrrbNum(String srrbNum) {
		this.srrbNum = !"".equals(srrbNum) ? srrbNum : "--";
	}

	public String getYpEx() {
		return ypEx;
	}

	public void setYpEx(String ypEx) {
		this.ypEx = ypEx;
	}

	/**
	 * 座位类型
	 * @return
	 */
	public String getSeatTypes() {
		return seatTypes;
	}

	public void setSeatTypes(String seatTypes) {
		this.seatTypes = seatTypes;
	}

	public String getFromStationName() {
		return fromStationName;
	}

	public void setFromStationName(String fromStationName) {
		this.fromStationName = fromStationName;
	}

	public String getToStationName() {
		return toStationName;
	}

	public void setToStationName(String toStationName) {
		this.toStationName = toStationName;
	}

	@Override
	public String toString() {
		return "TrainInfo [trainNo=" + trainNo + ", stationTrainCode=" + stationTrainCode + ", startStationTelecode="
				+ startStationTelecode + ", endStationTelecode=" + endStationTelecode + ", fromStationTelecode="
				+ fromStationTelecode + ", toStationTelecode=" + toStationTelecode + ", startTime=" + startTime
				+ ", arriveTime=" + arriveTime + ", lishi=" + lishi + ", canWebBuy=" + canWebBuy + ", ypInfo=" + ypInfo
				+ ", startTrainDate=" + startTrainDate + ", trainSeatFeature=" + trainSeatFeature + ", locationCode="
				+ locationCode + ", fromStationNo=" + fromStationNo + ", toStationNo=" + toStationNo
				+ ", isSupportCard=" + isSupportCard + ", controlledTrainFlag=" + controlledTrainFlag + ", ggNum="
				+ ggNum + ", grNum=" + grNum + ", qtNum=" + qtNum + ", rwNum=" + rwNum + ", rzNum=" + rzNum + ", tzNum="
				+ tzNum + ", wzNum=" + wzNum + ", ybNum=" + ybNum + ", ywNum=" + ywNum + ", yzNum=" + yzNum + ", zeNum="
				+ zeNum + ", zyNum=" + zyNum + ", swzNum=" + swzNum + ", srrbNum=" + srrbNum + ", ypEx=" + ypEx
				+ ", seatTypes=" + seatTypes + ", fromStationName=" + fromStationName + ", toStationName="
				+ toStationName + "]";
	}

}
