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

import java.awt.Graphics;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.ContainerEvent;
import java.awt.event.ContainerListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.JPanel;

import com.wudaosoft.traintickets.util.Point;

/**
 * @author changsoul.wu
 *
 */
public class CaptchaView extends JPanel {

	private static final long serialVersionUID = 9146510559987458061L;

	private final List<Point> positions = new ArrayList<Point>();
	private final List<Point> results = new ArrayList<Point>();

	private BufferedImage captchaTemplate;
	
	private BufferedImage checkImg;
	
	private BufferedImage refreshImg;
	
	private BufferedImage refreshPressImg;
	
	private BufferedImage failImg;
	
	private BufferedImage successImg;

	private BufferedImage captchaImage;
	
	private boolean isPressRefresh = false;

	public CaptchaView() {
		setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));
		try {
			captchaTemplate = ImageIO.read(this.getClass().getResourceAsStream("/captcha.png"));
			
			checkImg = captchaTemplate.getSubimage(0, 96, 27, 27);
			refreshImg = captchaTemplate.getSubimage(0, 0, 54, 25);
			refreshPressImg = captchaTemplate.getSubimage(0, 25, 54, 25);
			failImg = captchaTemplate.getSubimage(0, 203, 293, 190);
			successImg = captchaTemplate.getSubimage(0, 393, 293, 190);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		addMouseListener(new MouseAdapter() {

			@Override
			public void mousePressed(MouseEvent e) {
				int x = e.getX();
				int y = e.getY();
				
				if(x > 236 && y > 3 && y < 28) {
					isPressRefresh = true;
					repaint();
				}
			}
		});
		
	}

	public void setCaptchaImage(BufferedImage captcha) {
		if(captcha == null)
			return;
		
		isPressRefresh = false;
		positions.clear();
		if(captcha != successImg)
			results.clear();

		BufferedImage localIcon = captchaImage;
		captchaImage = captcha;

		firePropertyChange("captchaImage", localIcon, captchaImage);

		if ((accessibleContext != null) && (localIcon != captchaImage)) {
			accessibleContext.firePropertyChange("AccessibleVisibleData", localIcon, captchaImage);
		}

		if (captchaImage != localIcon) {
			if ((captchaImage == null) || (localIcon == null)
					|| (captchaImage.getWidth() != localIcon.getWidth())
					|| (captchaImage.getHeight() != localIcon.getHeight())) {

				revalidate();
			}
			repaint();
		}
	}
	
	public void mark(int x, int y) {
		
		if(captchaImage == successImg || captchaImage == failImg)
			return;
		
		Point p = new Point(x, y);
		
		if(positions.contains(p)) {
			positions.remove(p);
			results.remove(p);
		} else {
			positions.add(p);
			results.add(p);
		}
		
		repaint();
	}
	
	public void tipFail() {
		setCaptchaImage(failImg);
		try {
			Thread.sleep(2000);
		} catch (InterruptedException e) {
		}
	}
	
	public void tipSuccess() {
		setCaptchaImage(successImg);
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
		}
	}
	
	public String getResult() {
		if(results.isEmpty())
			return null;
		
		StringBuilder sb = new StringBuilder(40);
		for (Point d : results) {
			if(sb.length() != 0)
				sb.append(",");
			sb.append(d.getX()).append(",").append(d.getY() - 30);
		}
		
		positions.clear();
		results.clear();
		
		return sb.toString();
	}

	@Override
	protected void paintComponent(Graphics g) {
		
		if(captchaImage == null)
			return;
		
		int x = 0;
		int y = 0;

		//绘制验证码图片
		g.drawImage(captchaImage, x, y, captchaImage.getWidth(), captchaImage.getHeight(), this);
		//绘制刷新按钮图片
		BufferedImage rImg = isPressRefresh ? refreshPressImg : refreshImg;
		g.drawImage(rImg, 236, 3, rImg.getWidth(), rImg.getHeight(), this);
	}

	@Override
	protected void paintChildren(Graphics g) {

		//绘制选择标记图片
		for (Point d : positions) {
			int x = d.getX() - 13;
			int y = d.getY() - 14;
			x = x >= 0 ? x : 0;
			y = y >= 0 ? y : 0;
			
			g.drawImage(checkImg, x, y, checkImg.getWidth(), checkImg.getHeight(), this);
		}
	}

}
