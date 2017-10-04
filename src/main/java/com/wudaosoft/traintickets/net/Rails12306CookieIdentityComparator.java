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

import java.io.Serializable;
import java.util.Comparator;

import org.apache.http.annotation.Immutable;
import org.apache.http.cookie.Cookie;

/** 
 * Cookies排序，应对12306网站防外挂检测。
 * JSESSIONID 放在第一位
 * BIGipServerotn和BIGipServerpassport放在最后两位
 * 
 * @author Changsoul Wu
 * 
 */
@Immutable
public class Rails12306CookieIdentityComparator implements Serializable, Comparator<Cookie> {

	private static final long serialVersionUID = 504609612791764159L;

	@Override
    public int compare(final Cookie c1, final Cookie c2) {
    	String name1 = c1.getName();
    	String name2 = c2.getName();
    	
    	if("JSESSIONID".equals(name1) && !"JSESSIONID".equals(name2)) //放在第一位
    		return -1;
    	
    	int res = name1.compareTo(name2);
    	
    	if(name1.startsWith("BIGipServer") && name2.startsWith("BIGipServer"))
    		res = name1.compareTo(name2);
    	else if(name1.startsWith("BIGipServer")) //放在最后一位
    		res = 1;
    	else if(name2.startsWith("BIGipServer")) //放在最后一位
    		res = -1;
         
        if (res == 0) {
            // do not differentiate empty and null domains
            String d1 = c1.getDomain();
            if (d1 == null) {
                d1 = "";
            } else if (d1.indexOf('.') == -1) {
                d1 = d1 + ".local";
            }
            String d2 = c2.getDomain();
            if (d2 == null) {
                d2 = "";
            } else if (d2.indexOf('.') == -1) {
                d2 = d2 + ".local";
            }
            res = d1.compareToIgnoreCase(d2);
        }
        if (res == 0) {
            String p1 = c1.getPath();
            if (p1 == null) {
                p1 = "/";
            }
            String p2 = c2.getPath();
            if (p2 == null) {
                p2 = "/";
            }
            res = p1.compareTo(p2);
        }
        return res;
    }

}
