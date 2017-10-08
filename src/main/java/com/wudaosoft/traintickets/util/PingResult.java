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
package com.wudaosoft.traintickets.util;

/**
 * @author changsoul.wu
 *
 */
public class PingResult {
	private String ip;

	private double time;

	private int routeNum;

	private double loss;

	/**
	 * 
	 */
	public PingResult() {
	}

	/**
	 * @param ip
	 * @param time
	 * @param ttl
	 * @param loss
	 */
	public PingResult(String ip, double time, int routeNum, double loss) {
		this.ip = ip;
		this.time = time;
		this.routeNum = routeNum;
		this.loss = loss;
	}

	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	public double getTime() {
		return time;
	}

	public void setTime(double time) {
		this.time = time;
	}

	public int getRouteNum() {
		return routeNum;
	}

	public void setRouteNum(int routeNum) {
		this.routeNum = routeNum;
	}

	@Override
	public String toString() {
		return "PingResult [ip=" + ip + ", time=" + time + ", routeNum=" + routeNum + ", loss=" + loss + "]";
	}
}
