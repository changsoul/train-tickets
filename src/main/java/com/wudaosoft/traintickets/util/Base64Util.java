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
import com.wudaosoft.traintickets.model.TrainInfoRow;


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
		
		System.out.println(JSON.toJSONString(TrainInfoRow.fromData("5cAidSfSXimDOL2NXkwIV6iQdVtq%2Fyjg9P5iw6PuEod3k9ktdE6BNacctI%2Fw316st71vgUqLJDA9%0AVHjTaUv7RdA7MRzSiWBqzmxwVz6VLVaXfchSJp7DtJ7YU5WFq%2F7WZzzBfHbNZbJerc2mVAojM7Qm%0AzA00tj1pACqHGuEmDVsy7oMXMXG3iHFV7%2Bv6xFsX4%2FxstUE4o4YRrEtvqbzWMMqwqIDctN%2Fv1fIV%0AM7D7VzoI4O%2Be|预订|5l00000G8523|G85|AOH|IZQ|AOH|IZQ|08:00|14:51|06:51|Y|bCW2hjgxU7L%2FSWI3X0DRUjwofXLcLC7OYFlPpi71jTwb%2FUxy|20171011|3|H2|01|05|0|0|||||||||||9|10|5||O0M090|OM9"), SerializerFeature.PrettyFormat, SerializerFeature.WriteNonStringValueAsString));
		//System.out.println(decode("Mm0X96jicesfmlP0B49spMnnPrAtNmIZy%2FWy61LbpLZWN7w%2FTki13jFi4VNZAC53ze%2F9IV0Ohnc%2F%0AmHCkkqcVUq0Cis8LMO6UGQe28n%2FKGrVoLmjDf4KKkrDZlVyodW0KwGDz1hidsM5lSN6lmjn6lHGX%0AreWD6NIw3ahj%2F05YN82W1S2OeoggYGWEsooksfwvatR2c7KFSSnEEA4d2CIQvkr3fzW5%2BDBCAikA%0AtEkER1jsGIa8|Mm0X96jicesfmlP0B49spMnnPrAtNmIZy%2FWy61LbpLZWN7w%2FTki13jFi4VNZAC53ze%2F9IV0Ohnc%2F%0AmHCkkqcVUq0Cis8LMO6UGQe28n%2FKGrVoLmjDf4KKkrDZlVyodW0KwGDz1hidsM5lSN6lmjn6lHGX%0AreWD6NIw3ahj%2F05YN82W1S2OeoggYGWEsooksfwvatR2c7KFSSnEEA4d2CIQvkr3fzW5%2BDBCAikA%0AtEkER1jsGIa8|Mm0X96jicesfmlP0B49spMnnPrAtNmIZy%2FWy61LbpLZWN7w%2FTki13jFi4VNZAC53ze%2F9IV0Ohnc%2F%0AmHCkkqcVUq0Cis8LMO6UGQe28n%2FKGrVoLmjDf4KKkrDZlVyodW0KwGDz1hidsM5lSN6lmjn6lHGX%0AreWD6NIw3ahj%2F05YN82W1S2OeoggYGWEsooksfwvatR2c7KFSSnEEA4d2CIQvkr3fzW5%2BDBCAikA%0AtEkER1jsGIa8|Mm0X96jicesfmlP0B49spMnnPrAtNmIZy%2FWy61LbpLZWN7w%2FTki13jFi4VNZAC53ze%2F9IV0Ohnc%2F%0AmHCkkqcVUq0Cis8LMO6UGQe28n%2FKGrVoLmjDf4KKkrDZlVyodW0KwGDz1hidsM5lSN6lmjn6lHGX%0AreWD6NIw3ahj%2F05YN82W1S2OeoggYGWEsooksfwvatR2c7KFSSnEEA4d2CIQvkr3fzW5%2BDBCAikA%0AtEkER1jsGIa8%2BddYcc2oVw"));
	}
}
