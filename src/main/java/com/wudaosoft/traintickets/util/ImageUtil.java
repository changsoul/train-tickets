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

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.geom.RoundRectangle2D;
import java.awt.image.BufferedImage;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Changsoul Wu
 * 
 */
public class ImageUtil {

	public static BufferedImage resizeImage(final BufferedImage bufferedimage, final int w, final int h) {
//		int type = bufferedimage.getColorModel().getTransparency();
		BufferedImage newImage = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
		Graphics2D graphics2d = newImage.createGraphics();

		Map<RenderingHints.Key, Object> mapH = new HashMap<RenderingHints.Key, Object>();
		mapH.put(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
//		mapH.put(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

		// graphics2d.setRenderingHint(
		// RenderingHints.KEY_INTERPOLATION,
		// RenderingHints.VALUE_INTERPOLATION_BILINEAR);
		graphics2d.setComposite(AlphaComposite.Src);
		graphics2d.setBackground(Color.WHITE);
		graphics2d.setRenderingHints(mapH);
		graphics2d.fill(new RoundRectangle2D.Float(0, 0, w, h, 8, 8));
		graphics2d.setComposite(AlphaComposite.SrcAtop);
		graphics2d.drawImage(bufferedimage, 0, 0, w, h, 0, 0, bufferedimage.getWidth(), bufferedimage.getHeight(),
				null);
		graphics2d.dispose();
		return newImage;
	}

}
