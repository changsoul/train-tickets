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
import java.awt.image.BufferedImage;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

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
public class LoginForm extends JDialog {

	private static final Logger log = LoggerFactory.getLogger(LoginForm.class);

	private boolean loning = false;

	private UserInfo user;

	private MyButton senderButton;

	private Action action = Action.getInstance();

	Future<BufferedImage> cheimgFuture = null;

	private JLabel checkCodelabel;
	private JTextField codeText;

	public LoginForm(Frame owner, MyButton senderButton, UserInfo user, boolean modal) throws HeadlessException {
		super(owner, modal);
		this.senderButton = senderButton;
		this.user = user;

		try {
			cheimgFuture = action.getCheckImage(user);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		}
		initComponents();
		setTitle("用户登录");
		setSize(350, 275);
		setResizable(false);
		setLocationRelativeTo(getOwner()); // 居中显示
		// setAlwaysOnTop(true);
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		addWindowListener(new WindowAdapter() {
			@Override
			public void windowOpened(WindowEvent e) {
				super.windowOpened(e);
				setFistCheckImage();
			}
		});

		// setVisible(true);
	}

	private void setFistCheckImage() {
		try {
			BufferedImage img = cheimgFuture.get();
			checkCodelabel.setIcon(new ImageIcon(img));
			cheimgFuture = null;
		} catch (InterruptedException | ExecutionException e) {
			log.error(e.getMessage(), e);
		}
	}

	private void refeshCheckImage() {
		try {
			checkCodelabel.setIcon(new ImageIcon(action.getCheckImage(user).get()));
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

		JLabel titleLabel = new JLabel(user.getName() + "(" + user.getLoginId() + ")", JLabel.CENTER);
		titleLabel.setBounds(0, 18, 350, 25);
		titleLabel.setFont(new Font("微软雅黑", Font.PLAIN, 18));
		panel.add(titleLabel);

		checkCodelabel = new JLabel();
		checkCodelabel.setBounds(67, 50, 216, 100);
		checkCodelabel.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		panel.add(checkCodelabel);

		JLabel userLabel = new JLabel("验证码：");
		userLabel.setBounds(67, 160, 53, 25);
		panel.add(userLabel);

		codeText = new JTextField(20);
		codeText.setFont(new Font("Verdana", Font.PLAIN, 16));
		codeText.setBounds(120, 160, 163, 25);
		panel.add(codeText);

		// 创建登录按钮
		JButton loginButton = new JButton("登录");
		loginButton.setBounds(67, 200, 216, 25);
		panel.add(loginButton);

		add(panel);

		checkCodelabel.addMouseListener(new MouseAdapter() {

			@Override
			public void mouseClicked(MouseEvent e) {
				refeshCheckImage();
			}
		});

		codeText.addMouseListener(new MouseAdapter() {

			@Override
			public void mousePressed(MouseEvent e) {
				codeText.selectAll();
			}

		});

		codeText.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				doLogin();
			}
		});

		loginButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				doLogin();
			}
		});
	}

	private void doLogin() {

		if (loning)
			return;

		String checkCode = codeText.getText();

		if (checkCode.trim().length() < 4) {
			codeText.grabFocus();
			return;
		}

		loning = true;

		try {
			
			JSONObject loginRs = action.login(user, checkCode);

			String flag = loginRs.getString("flag");
			String message = loginRs.getString("message");

			if (!"-1".equals(flag)) {

				MainForm mainForm = (MainForm) this.getOwner();

				user.setIsLogin(true);
				senderButton.setEnabled(false);
				mainForm.getLoginTable().selectAll();
				// JOptionPane.showMessageDialog(this, "登录成功！", "提示", JOptionPane.INFORMATION_MESSAGE);
				mainForm.writeMsg("登录成功！", user);
				action.initUser(senderButton, user);
				dispose();
			} else {

				JOptionPane.showMessageDialog(this, message, "登录错误", JOptionPane.WARNING_MESSAGE);

				if (!"NOTBINDCA".equals(message)) {
					codeText.setText("");
					codeText.grabFocus();
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
