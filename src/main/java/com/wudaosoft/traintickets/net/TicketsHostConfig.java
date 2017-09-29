/**
 *    Copyright 2009-2017 Wudao Software Studio(wudaosoft.com)
 * 
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 * 
 *        https://www.apache.org/licenses/LICENSE-2.0
 * 
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */
package com.wudaosoft.traintickets.net;

import java.net.URL;

import org.apache.http.HttpHost;

/** 
 * @author Changsoul Wu
 * 
 */
public class TicketsHostConfig extends HostConfig {
	
	private String referer = "https://kyfw.12306.cn/otn/login/init";
	
	@Override
	public HttpHost getHost() {
		return new HttpHost("kyfw.12306.cn", 443);
	}

	@Override
	public String getReferer() {
		return referer;
	}

	@Override
	public URL getCA() {
		return this.getClass().getResource("/srca12306.jks");
	}

	@Override
	public char[] getCAPassword() {
		return "123456".toCharArray();
	}

	
}
