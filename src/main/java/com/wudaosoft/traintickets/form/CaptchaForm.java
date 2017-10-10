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

import java.applet.Applet;
import java.applet.AudioClip;
import java.awt.Cursor;
import java.awt.Font;
import java.awt.Frame;
import java.awt.HeadlessException;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSONObject;
import com.wudaosoft.traintickets.Action;
import com.wudaosoft.traintickets.model.UserInfo;

/**
 * @author Changsoul Wu
 * 
 */
@SuppressWarnings("serial")
public class CaptchaForm extends JDialog {

	private static final Logger log = LoggerFactory.getLogger(CaptchaForm.class);

	private boolean loning = false;

	private UserInfo user;

	private Action action = Action.getInstance();

	private CaptchaView captchaView;
	
	private AudioClip noteMusic;

	public CaptchaForm(Frame owner, UserInfo user, boolean modal) throws HeadlessException {
		super(owner, modal);
		this.user = user;

		initComponents();
		setTitle("验证吗");
		setSize(414, 335);
		setResizable(false);
		setLocationRelativeTo(getOwner()); // 居中显示
//		setAlwaysOnTop(true);
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		addWindowListener(new WindowAdapter() {
			@Override
			public void windowOpened(WindowEvent e) {
				super.windowOpened(e);
				refeshCheckImage();
				
				noteMusic = Applet.newAudioClip(this.getClass().getResource("/message.wav"));
		        noteMusic.play();
			}
		});

		// setVisible(true);
	}

	private void refeshCheckImage() {
		try {
			captchaView.setCaptchaImage(action.getCaptchaImage(user));
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		}
	}

	/**
	 * 
	 */
	public void initComponents() {

		JPanel panel = new JPanel();
		panel.setLayout(null);

		JLabel titleLabel = new JLabel("根据提示选择图片", JLabel.CENTER);
		titleLabel.setBounds(120, 18, 174, 25);
		titleLabel.setFont(new Font("微软雅黑", Font.PLAIN, 18));
		panel.add(titleLabel);
		
		// 创建登录按钮
		JButton loginButton = new JButton("确定");
		loginButton.setBounds(120, 268, 174, 25);
		panel.add(loginButton);
	
		add(panel);

		captchaView = new CaptchaView();
		captchaView.setBounds(60, 60, 293, 190);
		captchaView.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		panel.add(captchaView);

		captchaView.addMouseListener(new MouseAdapter() {

			@Override
			public void mouseClicked(MouseEvent e) {
				int x = e.getX();
				int y = e.getY();
				
				if(x > 236 && y > 3 && y < 28) {
					refeshCheckImage();
				}
				
				if(y > 30) {
					captchaView.mark(x, y);
				}
			}
		});

		loginButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				//doLogin();
				captchaView.tipSuccess();
				System.out.println(captchaView.getResult());
			}
		});
	}

	private void doLogin() {

		if (loning)
			return;

		loning = true;

		try {
			
			JSONObject loginRs = action.login("", "", user);

			String flag = loginRs.getString("flag");
			String message = loginRs.getString("message");

			if (!"-1".equals(flag)) {

				MainForm mainForm = (MainForm) this.getOwner();

				user.setIsLogin(true);
				mainForm.getLoginTable().selectAll();
				// JOptionPane.showMessageDialog(this, "登录成功！", "提示", JOptionPane.INFORMATION_MESSAGE);
				mainForm.writeMsg("登录成功！", user);
//				action.initUser(senderButton, user);
				dispose();
			} else {

				JOptionPane.showMessageDialog(this, message, "登录错误", JOptionPane.WARNING_MESSAGE);

				if (!"NOTBINDCA".equals(message)) {
					refeshCheckImage();
				}
			}
		} catch (Exception e) {
			JOptionPane.showMessageDialog(this, "登录出错：" + e.getMessage(), "登录错误", JOptionPane.ERROR_MESSAGE);
		} finally {
			loning = false;
		}
	}

}
