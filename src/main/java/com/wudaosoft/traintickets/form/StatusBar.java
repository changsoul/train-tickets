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
package com.wudaosoft.traintickets.form;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;

import javax.swing.BorderFactory;
import javax.swing.Icon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.BevelBorder;

/**
 * @author changsoul.wu
 *
 */
public class StatusBar extends JPanel {

	private static final long serialVersionUID = -6977916276004870672L;

	// Constructor
	public StatusBar() {
		setLayout(new FlowLayout(FlowLayout.LEFT, 10, 3));
		setBackground(Color.LIGHT_GRAY);
		setBorder(BorderFactory.createLineBorder(Color.DARK_GRAY));
		add(timePane);
		add(networkPane);
	}

	// Panes in the status bar
	private StatusPane timePane = new StatusPane("服务器时间：");
	private StatusPane networkPane = new StatusPane("网络延时：");
	
	public StatusPane getTimePane() {
		return timePane;
	}

	public StatusPane getNetworkPane() {
		return networkPane;
	}

	// Class defining a status bar pane
	class StatusPane extends JLabel {

		private static final long serialVersionUID = 2334170458961869210L;

		// Constructor - text only
		public StatusPane(String text) {
			super(text, LEFT);
			setupPane();
		}

		// Constructor - text with an icon
		public StatusPane(String text, Icon icon) {
			super(text, icon, LEFT);
			setupPane();
		}

		// Helper method for use by constructors
		private void setupPane() {
			setBackground(Color.LIGHT_GRAY); // Set background color
			setForeground(Color.BLACK); // Set foreground color
			setFont(paneFont); // Set the fixed font
			setBorder(BorderFactory.createCompoundBorder(BorderFactory.createBevelBorder(BevelBorder.LOWERED), // Outside
																												// border
					BorderFactory.createEmptyBorder(0, 5, 0, 3))); // Inside
																	// border
			setPreferredSize(new Dimension(80, 20));
		}

		// Font for pane text
		private Font paneFont = new Font("Serif", Font.PLAIN, 10);
	}
}
