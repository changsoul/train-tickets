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

import java.io.File;

import javax.swing.filechooser.FileFilter;

/**
 * @author Changsoul Wu
 * 
 */
public final class ExtensionFileFilter extends FileFilter {

	private String[] extension;

	public ExtensionFileFilter(String... extension) {
		super();
		this.extension = extension;
	}

	public boolean accept(File f) {
		if (f != null) {
			if (f.isDirectory()) {
				return true;
			}
			
			String subfix = getExtension(f);
			
			if (subfix == null)
				return false;
					
			for(String ext : this.extension) {
				
				if (subfix.equalsIgnoreCase(ext)) {
					return true;
				}
			}
		}
		return false;
	}

	private String getExtension(File f) {
		if (f != null) {
			String filename = f.getName();
			int i = filename.lastIndexOf('.');
			if (i > 0 && i < filename.length() - 1) {
				return filename.substring(i + 1).toLowerCase();
			}
		}
		return null;
	}

	/* (non-Javadoc)
	 * @see javax.swing.filechooser.FileFilter#getDescription()
	 */
	@Override
	public String getDescription() {
		return "文本文档";
	}

}
