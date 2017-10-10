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

import java.util.regex.Pattern;

/**
 * @author changsoul.wu
 *
 */
public class PingUtil {
	
	private static final Pattern WINDOWS_PACKET_LOSS_PATTERN = Pattern.compile(" \\((\\d+?)% 丢失\\)");
	private static final Pattern WINDOWS_TTL_PATTERN = Pattern.compile(" TTL=(\\d+)");
	private static final Pattern WINDOWS_PING_PATTERN = Pattern.compile("平均 = (\\d+?)ms");
	private static final Pattern LINUX_PACKET_LOSS_PATTERN = Pattern.compile(", (\\S+?)% packet loss");
	private static final Pattern LINUX_TTL_PATTERN = Pattern.compile(" ttl=(\\d+?) ");
	private static final Pattern LINUX_PING_PATTERN = Pattern.compile(" min/avg/max/\\S* = (.+?) ms");
	
	public static PingResult ping(String host, int times) {
		
		String fileEncoding = System.getProperty("file.encoding");
		if(fileEncoding == null)
			fileEncoding = "UTF-8";
		
		String cmdStr = null;
		boolean isWindows = isWindows();
		
		if (isWindows) {
			fileEncoding = "GBK";
			cmdStr = String.format("ping -n %s -w 1 %s", times, host);
		} else {
			
			cmdStr = String.format("ping -c %s -i 0.6 %s", times, host);
		}
		
		String rs = ExecuteShellComand.executeCommand(cmdStr, fileEncoding);
		
		//System.out.println(rs);
		
		String packetLoss = "";
		String ttl =  "";
		String avgTime =  "";
		
		if (isWindows) {
			packetLoss = ExecuteShellComand.findValue(rs, WINDOWS_PACKET_LOSS_PATTERN);
			ttl = ExecuteShellComand.findValue(rs, WINDOWS_TTL_PATTERN);
			avgTime = ExecuteShellComand.findValue(rs, WINDOWS_PING_PATTERN);
			avgTime = "".equals(avgTime) ? "5000" : avgTime;
		} else {
			packetLoss = ExecuteShellComand.findValue(rs, LINUX_PACKET_LOSS_PATTERN);
			ttl = ExecuteShellComand.findValue(rs, LINUX_TTL_PATTERN);
			avgTime = ExecuteShellComand.findValue(rs, LINUX_PING_PATTERN);
			avgTime = "".equals(avgTime) ? "5000" : avgTime.split("\\/")[1];
		}
		
		int ttlNum = Integer.valueOf("".equals(ttl) ? "1000" : ttl).intValue();
		
		return new PingResult(host, Double.valueOf(avgTime).doubleValue(), getRouteNum(ttlNum), Double.valueOf("".equals(packetLoss) ? "100" : packetLoss).doubleValue());
	}
	
	private static boolean isWindows() {
		return System.getProperty("os.name").toLowerCase().contains("windows");
	}
	
	private static int getRouteNum(int ttl) {
		
		if(ttl <= 64) {
			return 64 - ttl;
		} else if(ttl <= 128) {
			return 128 - ttl;
		}  else if(ttl <= 255) {
			return 255 - ttl;
		} else {
			return 100;
		}
	}
	
	public static void main(String[] args) {
		
		System.out.println(ping("14.18.201.47", 4));
	}
}
