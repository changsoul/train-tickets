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

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author changsoul.wu
 *
 */
public class ExecuteShellComand {

	private static final String IPADDRESS_PATTERN = "([01]?\\d\\d?|2[0-4]\\d|25[0-5])"
		+ "\\.([01]?\\d\\d?|2[0-4]\\d|25[0-5])"
		+ "\\.([01]?\\d\\d?|2[0-4]\\d|25[0-5])"
		+ "\\.([01]?\\d\\d?|2[0-4]\\d|25[0-5])";

	private static Pattern pattern = Pattern.compile(IPADDRESS_PATTERN);

	public static void main(String[] args) {


		String domainName = "kyfw.12306.cn";
		String command = "host -t a " + domainName;

		String output = executeCommand(command);

		//System.out.println(output);

		List<String> list = getIpAddress(output);

		if (list.size() > 0) {
			System.out.printf("%s has address : %n", domainName);
			for (String ip : list) {
				System.out.println(ip);
			}
		} else {
			System.out.printf("%s has NO address. %n", domainName);
		}

	}

	public static String executeCommand(String command) {
		
		String fileEncoding = System.getProperty("file.encoding");
		if(fileEncoding == null)
			fileEncoding = "UTF-8";

		StringBuffer output = new StringBuffer();

		BufferedReader reader = null;
		Process p = null;
		try {
			p = Runtime.getRuntime().exec(command);
			p.waitFor();
			
			reader = new BufferedReader(new InputStreamReader(p.getInputStream(), fileEncoding));

			String line = null;
			while ((line = reader.readLine())!= null) {
				output.append(line + "\n");
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if(reader != null)
					reader.close();
			} catch (IOException e) {
			}
		}

		return output.toString();
	}

	public static List<String> getIpAddress(String msg) {

		List<String> ipList = new ArrayList<String>();

		if (msg == null || msg.equals(""))
			return ipList;

		Matcher matcher = pattern.matcher(msg);
		matcher.reset();
		while (matcher.find()) {
			ipList.add(matcher.group(0));
		}

		return ipList;
	}
	
	public static String findValue(String text, Pattern pattern) {
		Matcher matcher = pattern.matcher(text);
		matcher.reset();
		boolean result = matcher.find();
		if (result) {
			return matcher.group(1);
		} else {
			return "";
		}
	}
}
