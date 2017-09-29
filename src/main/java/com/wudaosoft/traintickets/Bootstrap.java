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
package com.wudaosoft.traintickets;

import java.awt.Font;
import java.util.Enumeration;

import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.plaf.FontUIResource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.wudaosoft.traintickets.form.MainForm;

/**
 * 应该程序启动类
 * 
 * @author Changsoul Wu
 * 
 */
public final class Bootstrap {
	
	private static final Logger log = LoggerFactory.getLogger(Bootstrap.class);

	/**
	 * 程序启动入口
	 * @param argv
	 * @throws Exception
	 */
	public static void main(String[] argv) throws Exception {
		
		init();
	}
	
	/**
	 * 初始化
	 */
	protected static void init() {
		try {
			SwingUtilities.invokeAndWait(new Runnable() {
				public void run() {
					initGlobal();
					new MainForm().setVisible(true);
				}
			});
		} catch (Exception exc) {
			log.error("Can't create because of " + exc.getMessage(), exc);
		}
	}
	
	protected static void initGlobal() {
		
		String os = System.getProperty("os.name");
		
		//设置windows系统界面风格
		if(os != null && os.toLowerCase().contains("windows")) {
			try {
				UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
				
				FontUIResource fontUIResource = new FontUIResource(new Font("微软雅黑", Font.PLAIN, 12));
				for (Enumeration<Object> keys = UIManager.getDefaults().keys(); keys.hasMoreElements();) {
					Object key = keys.nextElement();
					Object value = UIManager.get(key);
					if (value instanceof FontUIResource) {
						UIManager.put(key, fontUIResource);
					}
				}
			} catch (Exception e) {
				log.error(e.getMessage(), e);
			}
		}
		
	}
}

