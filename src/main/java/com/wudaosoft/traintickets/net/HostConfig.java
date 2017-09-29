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
import java.util.Arrays;

import org.apache.http.HttpHost;
import org.apache.http.client.config.AuthSchemes;
import org.apache.http.client.config.RequestConfig;

/**
 * @author Changsoul Wu
 * 
 */
public abstract class HostConfig {

	private String userAgent = "Mozilla/5.0 (compatible; MSIE 9.0; Windows NT 6.1; WOW64; Trident/5.0)";
	private RequestConfig defaultRequestConfig;
	
	public HostConfig() {
		defaultRequestConfig = org.apache.http.client.config.RequestConfig.custom()
				.setExpectContinueEnabled(false)
				// .setStaleConnectionCheckEnabled(true)
				.setTargetPreferredAuthSchemes(Arrays.asList(AuthSchemes.NTLM, AuthSchemes.DIGEST))
				.setProxyPreferredAuthSchemes(Arrays.asList(AuthSchemes.BASIC))
				.setConnectionRequestTimeout(500).setConnectTimeout(5000).setSocketTimeout(6000)
				.build();
	}

	public String getUserAgent() {
		return userAgent;
	}

	public String getReferer() {
		return null;
	}

	abstract public HttpHost getHost();
	
	abstract public String getHostUrl();

	public RequestConfig getRequestConfig() {
		return defaultRequestConfig;
	}

	public URL getCA() {
		return null;
	}
	
	public char[] getCAPassword() {
		return "nopassword".toCharArray();
	}
}
