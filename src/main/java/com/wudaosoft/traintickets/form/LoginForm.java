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

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
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

	private final UserInfo user;

	private Action action = Action.getInstance();

	private JTextField txtAccount;
	
	private JPasswordField txtPassword;
	
	private CaptchaView captchaView;

	public LoginForm(Frame owner, final UserInfo user, boolean modal) throws HeadlessException {
		super(owner, modal);
		this.user = user;

		initComponents();
		setTitle("用户登录");
		setSize(414, 410);
		setResizable(false);
		setLocationRelativeTo(getOwner()); // 居中显示
//		setAlwaysOnTop(true);
		setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
		
		addWindowListener(new WindowAdapter() {

			@Override
			public void windowClosing(WindowEvent e) {
				super.windowClosing(e);

		        if(!user.getIsLogin()){
		        	action.shoutdownThreadPool();
		        	System.exit(0);
		        } else {
		        	dispose();
		        }
			}

		});
		
		addWindowListener(new WindowAdapter() {
			@Override
			public void windowOpened(WindowEvent e) {
				super.windowOpened(e);
				try {
					action.getLoginPage(user.getContext());
				} catch (Exception e1) {
					log.error(e1.getMessage(), e1);
				}
				refeshCheckImage();
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

		String title = "登录";
		
		if(user.getIsLogin()) {
			user.setIsLogin(false);
			title = "用户已在别的地方登录，请重新登录";
		}
		
		JLabel titleLabel = new JLabel(title, JLabel.CENTER);
		titleLabel.setBounds(0, 18, 414, 25);
		titleLabel.setFont(new Font("微软雅黑", Font.PLAIN, 18));
		panel.add(titleLabel);

		JLabel lbAccount = new JLabel("登录名：");
		lbAccount.setBounds(34, 55, 53, 25);
		panel.add(lbAccount);
		
		txtAccount = new JTextField(20);
		//txtAccount.setFont(new Font("微软雅黑", Font.PLAIN, 16));
		txtAccount.setBounds(83, 55, 301, 25);
		panel.add(txtAccount);
		
		JLabel pwdLabel = new JLabel("密   码：");
		pwdLabel.setBounds(34, 90, 53, 25);
		panel.add(pwdLabel);

		txtPassword = new JPasswordField(20);
		//txtPassword.setFont(new Font("Verdana", Font.PLAIN, 16));
		txtPassword.setBounds(83, 90, 301, 25);
		panel.add(txtPassword);
		
		JLabel lbCaptcha = new JLabel("验证码：");
		lbCaptcha.setBounds(34, 125, 53, 25);
		panel.add(lbCaptcha);

		// 创建登录按钮
		JButton btnLogin = new JButton("登录");
		btnLogin.setBounds(83, 335, 301, 40);
		//btnLogin.setFont(new Font("微软雅黑", Font.PLAIN, 18));
		panel.add(btnLogin);
		
		captchaView = new CaptchaView();
		captchaView.setBounds(87, 125, 293, 190);
		captchaView.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		panel.add(captchaView);

		captchaView.addMouseListener(new MouseAdapter() {

			@Override
			public void mousePressed(MouseEvent e) {
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

		add(panel);

//		checkCodelabel.addMouseListener(new MouseAdapter() {
//
//			@Override
//			public void mouseClicked(MouseEvent e) {
//				refeshCheckImage();
//			}
//		});

		txtAccount.addMouseListener(new MouseAdapter() {

			@Override
			public void mousePressed(MouseEvent e) {
				txtAccount.selectAll();
			}

		});

		txtAccount.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				doLogin();
			}
		});

		btnLogin.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				doLogin();
			}
		});
	}

	private void doLogin() {

		if (loning)
			return;

		String account = txtAccount.getText();
		String password = new String(txtPassword.getPassword());
		String captcha = captchaView.getResult();

		if (account.trim().length() == 0) {
			txtAccount.grabFocus();
			return;
		}
		
		if (password.trim().length() == 0) {
			txtPassword.grabFocus();
			return;
		}
		
		if (captcha == null) {
			JOptionPane.showMessageDialog(this, "请点击图片验证码!", "提示", JOptionPane.INFORMATION_MESSAGE);
			return;
		}

		loning = true;

		try {
			
			if(action.captchaCheck(captcha, user)) {
				
				captchaView.tipSuccess();
			} else {
				
				captchaView.tipFail();
				refeshCheckImage();
				return;
			}
			
			JSONObject loginRs = action.login(account, password, user);

			String flag = loginRs.getString(Action.RESULT_CODE_KEY);
			String message = loginRs.getString(Action.RESULT_MESSAGE_KEY);

			if (Action.RESULT_CODE_SUCCESS.equals(flag)) {

				MainForm mainForm = (MainForm) this.getOwner();

				user.setIsLogin(true);
				mainForm.getLoginTable().selectAll();
				// JOptionPane.showMessageDialog(this, "登录成功！", "提示", JOptionPane.INFORMATION_MESSAGE);
				mainForm.writeMsg("登录成功！", user);
				dispose();
			} else {

				JOptionPane.showMessageDialog(this, message, "登录错误", JOptionPane.WARNING_MESSAGE);

				if (!"NOTBINDCA".equals(message)) {
					txtAccount.setText("");
					txtAccount.grabFocus();
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
