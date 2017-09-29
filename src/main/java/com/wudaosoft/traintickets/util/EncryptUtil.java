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

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;

import javax.script.Invocable;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;

/**
 * @author changsoul.wu
 *
 */
public class EncryptUtil {

	public static String encrypt(String key, String password)
			throws ScriptException, IOException, NoSuchMethodException {
		ScriptEngineManager manager = new ScriptEngineManager();
		ScriptEngine engine = manager.getEngineByName("javascript");

		Reader reader = null;
		try {
			reader = new InputStreamReader(EncryptUtil.class.getResourceAsStream("/encrypt.js"));
			engine.eval(reader);

			if (engine instanceof Invocable) {
				Invocable invoke = (Invocable) engine;

				String pwd = (String) invoke.invokeFunction("encrypt", key, password);

				return (String) invoke.invokeFunction("stringToHex", pwd);
			}
		} finally {
			if (reader != null) {
				try {
					reader.close();
				} catch (IOException ex) {
				}
			}
		}

		throw new ScriptException("javascript file is not Invocable.");
	}
}
