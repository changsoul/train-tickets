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
	
	public static final Pattern WINDOWS_TTL_PATTERN = Pattern.compile("onclick=\"ldlzy_viewSh\\('(.+?)'\\)");
	public static final Pattern LINUX_PACKET_LOSS_PATTERN = Pattern.compile(", (\\S+?)% packet loss");
	public static final Pattern LINUX_TTL_PATTERN = Pattern.compile(" ttl=(\\d+?) ");
	public static final Pattern LINUX_PING_PATTERN = Pattern.compile(" min/avg/max/\\S* = (.+?) ms");
	
	
	public static PingResult ping(String host, int times, int timeout) {
		
		String fileEncoding = System.getProperty("file.encoding");
		
		if(fileEncoding == null)
			fileEncoding = "UTF-8";
		
		String cmdStr = null;
		
		if (isWindows()) {
			
			cmdStr = String.format("ping -n %s -w %s %s", times, timeout, host);
		} else {
			
			cmdStr = String.format("ping -c %s -i 0.5 %s", times, host);
		}
		
		String rs = ExecuteShellComand.executeCommand(cmdStr);
		
		System.out.println(rs);
		
		String packetLoss = ExecuteShellComand.findValue(rs, LINUX_PACKET_LOSS_PATTERN);
		String ttl = ExecuteShellComand.findValue(rs, LINUX_TTL_PATTERN);
		String avgTime = ExecuteShellComand.findValue(rs, LINUX_PING_PATTERN);
		
		avgTime = "".equals(avgTime) ? "5000" : avgTime.split("\\/")[1];
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
		
//		System.getProperties().list(System.out);
		
		System.out.println(ping("14.18.201.47", 5, 1));
		
	}
}
