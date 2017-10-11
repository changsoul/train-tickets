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
package com.wudaosoft.traintickets.model;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import com.wudaosoft.traintickets.util.FileUtil;

/**
 * @author changsoul.wu
 *
 */
public class Stations {
	
	private static final List<String> stations = new ArrayList<String>(2800);
	
	static { 
		try {
			FileUtil.readLine(stations, Stations.class.getResourceAsStream("/station_name.txt"));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private String code;
	
	private String name;
	
	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	/**
	 * 
	 */
	public Stations() {
		super();
	}

	/**
	 * @param code
	 * @param name
	 */
	public Stations(String code, String name) {
		super();
		this.code = code;
		this.name = name;
	}

	@Override
	public String toString() {
		return "Stations [code=" + code + ", name=" + name + "]";
	}

	public static List<Stations> search (String key) {
		if(key == null || key.trim().length() < 2)
			return Collections.emptyList();
		
		List<Stations> list = new ArrayList<Stations>();
		
		Iterator<String> iter = stations.iterator();
		
		while (iter.hasNext()) {
			String line = iter.next();
			
			if (line.contains(key)) {
				String ls[] = line.split("\\|");
				
				list.add(new Stations(ls[2], ls[1]));
			}
			
			if(list.size() > 5)
				break;
		}
		
		return list;
	}
	
}
