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
package com.wudaosoft.traintickets.net;

import java.util.Iterator;
import java.util.List;

import org.apache.http.client.CookieStore;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.cookie.Cookie;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.cookie.BasicClientCookie;

/** 
 * @author Changsoul Wu
 * 
 */
public class CookieUtil {

	public static String getCookieValue(String name, CookieStore cookieStore) {
		
		if (cookieStore == null || name == null)
			return null;
		
		List<Cookie> list = cookieStore.getCookies();
		
		if(list == null)
			return null;
		
		Iterator<Cookie> iter = list.iterator();

		while(iter.hasNext()) {
			Cookie e = iter.next();
			if(e.getName().equals(name))
				return e.getValue();
		}
		
		return null;
	}
	
	/**
	 * 设置负载均衡服务器，选择时间最早的那台。
	 * @param context
	 */
	public static void setLoadBalancingCookie(HttpClientContext context) {
		final BasicClientCookie cookie = new BasicClientCookie("Cookie1", DomainConfig.SERVER_ID);
        cookie.setPath("/");
        cookie.setDomain(DomainConfig.DOMAIN.split(":")[0]);
        
		CookieStore cookieStore = context.getCookieStore();
		if(cookieStore == null)
			cookieStore = new BasicCookieStore();
		
		cookieStore.addCookie(cookie);
		context.setCookieStore(cookieStore);
	}
}
