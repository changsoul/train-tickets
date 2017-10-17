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
import java.awt.Insets;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;

import org.apache.commons.codec.binary.Base64;

import com.wudaosoft.traintickets.model.TrainInfoRow;
import com.wudaosoft.traintickets.util.ImageUtil;

/**
 * @author changsoul.wu
 *
 */
@SuppressWarnings("serial")
public class MyButton extends JButton {

	private int row;

	private int column;
	
	private TrainInfoRow trainInfoRow;

	public int getRow() {
		return row;
	}

	public void setRow(int row) {
		this.row = row;
	}

	public int getColumn() {
		return column;
	}

	public void setColumn(int column) {
		this.column = column;
	}

	public TrainInfoRow getTrainInfoRow() {
		return trainInfoRow;
	}

	public void setTrainInfoRow(TrainInfoRow trainInfoRow) {
		this.trainInfoRow = trainInfoRow;
	}

	public MyButton() {
		init();
	}

	public MyButton(String name) {
		super(name);
		
		init();
	}

	protected void init() {
//		set
		//setMargin(new Insets(2, 5, 2, 5));
		setPreferredSize(new Dimension(72, 30));
		
		try {
			
			BufferedImage bgBtn = ImageIO.read(new ByteArrayInputStream(Base64.decodeBase64("iVBORw0KGgoAAAANSUhEUgAAAAUAAAFKCAIAAABadAK/AAAAA3NCSVQICAjb4U/gAAAACXBIWXMAAAsSAAALEgHS3X78AAAAHHRFWHRTb2Z0d2FyZQBBZG9iZSBGaXJld29ya3MgQ1M1cbXjNgAAAHBJREFUWIXt0yEOgEAMRNGWVHELbgo3IOE0yL3PBrFrcMWiMC3JQKbuuS866rPcbxChf2R1d6Ser9nObUTqoXPNfTzbjlqReuhc8/9jtt4aUg+da+7jXVspO1IPnWvuJ2bTBaqHzjX3EbNNK1QPnesLmOggp6QPa+8AAAAASUVORK5CYII=".getBytes())));
			Icon ic1 = new ImageIcon(ImageUtil.resizeImage(bgBtn.getSubimage(0, 250, 2, 30), 72, 30));
			setIcon(ic1);
//			setBackground(Color.decode("#07f"));
			setForeground(Color.white);
			
			
			setHorizontalTextPosition(JButton.CENTER);  
			setVerticalTextPosition(JButton.CENTER);
			setOpaque(true);//设置控件是否透明，true为不透明，false为透明  
//			setContentAreaFilled(false);//设置图片填满按钮所在的区域  
//			setMargin(new Insets(0, 0, 0, 0));//设置按钮边框和标签文字之间的距离  
//			setFocusPainted(false);//设置这个按钮是不是获得焦点  
//			setBorderPainted(false);//设置是否绘制边框  
//			setBorder(null);//设置边框
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
