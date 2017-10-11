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

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;

import org.apache.commons.codec.binary.Base64;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.wudaosoft.traintickets.model.TicketRow;


/** 
 * @author Changsoul Wu
 * 
 */
public class Base64Util {

	public static String decode(String input) {
		
		try {
			input = URLDecoder.decode(input, "UTF-8");
			return new String(Base64.decodeBase64(input.getBytes("UTF-8")), "UTF-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		
		return "";
	}
	
	public static void main(String[] args) {
		
		System.out.println(JSON.toJSONString(TicketRow.fromData("xTsyPGNL530V7xvlktDVX6KAPO088p0hoZPXSCe6vHTWbKzuEm49RlOhIGRRcejE%2FPsv7k%2BZ6%2FK%2B%0AyvQgov3e%2B554IOb09YD%2Blgy64ilq4U5NJrH4hZikFjGVH3a1rbYQvbNVfZ5nsgQ0sKYiAjURipO4%0ApjXNdp495iFtp6tGJ2eHE%2FBEZxMaJdXBZltGT7WB7HPjRRlofwLz0pSLECLdrH61dcgzFdwNTHYZ%0ALw%3D%3D|预订|5500000Z9930|Z99|SHH|JQO|SHH|GGQ|17:45|10:08|16:23|Y|arJAQalD24Et25P0OMsezQhHcb5A2h%2Bm0876Tlhn%2FVzM4cTI|20171011|3|H3|01|04|0|0|||||||有||无|无|||||101030|113"), SerializerFeature.PrettyFormat, SerializerFeature.WriteNonStringValueAsString));
		//System.out.println(decode("Mm0X96jicesfmlP0B49spMnnPrAtNmIZy%2FWy61LbpLZWN7w%2FTki13jFi4VNZAC53ze%2F9IV0Ohnc%2F%0AmHCkkqcVUq0Cis8LMO6UGQe28n%2FKGrVoLmjDf4KKkrDZlVyodW0KwGDz1hidsM5lSN6lmjn6lHGX%0AreWD6NIw3ahj%2F05YN82W1S2OeoggYGWEsooksfwvatR2c7KFSSnEEA4d2CIQvkr3fzW5%2BDBCAikA%0AtEkER1jsGIa8|Mm0X96jicesfmlP0B49spMnnPrAtNmIZy%2FWy61LbpLZWN7w%2FTki13jFi4VNZAC53ze%2F9IV0Ohnc%2F%0AmHCkkqcVUq0Cis8LMO6UGQe28n%2FKGrVoLmjDf4KKkrDZlVyodW0KwGDz1hidsM5lSN6lmjn6lHGX%0AreWD6NIw3ahj%2F05YN82W1S2OeoggYGWEsooksfwvatR2c7KFSSnEEA4d2CIQvkr3fzW5%2BDBCAikA%0AtEkER1jsGIa8|Mm0X96jicesfmlP0B49spMnnPrAtNmIZy%2FWy61LbpLZWN7w%2FTki13jFi4VNZAC53ze%2F9IV0Ohnc%2F%0AmHCkkqcVUq0Cis8LMO6UGQe28n%2FKGrVoLmjDf4KKkrDZlVyodW0KwGDz1hidsM5lSN6lmjn6lHGX%0AreWD6NIw3ahj%2F05YN82W1S2OeoggYGWEsooksfwvatR2c7KFSSnEEA4d2CIQvkr3fzW5%2BDBCAikA%0AtEkER1jsGIa8|Mm0X96jicesfmlP0B49spMnnPrAtNmIZy%2FWy61LbpLZWN7w%2FTki13jFi4VNZAC53ze%2F9IV0Ohnc%2F%0AmHCkkqcVUq0Cis8LMO6UGQe28n%2FKGrVoLmjDf4KKkrDZlVyodW0KwGDz1hidsM5lSN6lmjn6lHGX%0AreWD6NIw3ahj%2F05YN82W1S2OeoggYGWEsooksfwvatR2c7KFSSnEEA4d2CIQvkr3fzW5%2BDBCAikA%0AtEkER1jsGIa8%2BddYcc2oVw"));
	}
}
