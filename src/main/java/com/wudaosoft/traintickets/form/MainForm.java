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

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.HeadlessException;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.Iterator;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;
import javax.swing.JSpinner;
import javax.swing.JSplitPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.wudaosoft.traintickets.Action;
import com.wudaosoft.traintickets.model.TrainInfoRow;
import com.wudaosoft.traintickets.model.TrainInfoTableModel;
import com.wudaosoft.traintickets.model.UserInfo;
import com.wudaosoft.traintickets.util.DateUtil;
import com.wudaosoft.traintickets.util.ExtensionFileFilter;

/**
 * @author Changsoul Wu
 * 
 */
@SuppressWarnings("serial")
public class MainForm extends JFrame {
	
	private static final Logger log = LoggerFactory.getLogger(MainForm.class);

	private final Action action = Action.getInstance();

	private JTable loginTable;
	
	private JLabel timeStatusbar;
	
	private JLabel speedStatusbar;
	
	private JLabel lbStatus;
	
	private JButton btnGrabBuzu;

	private JTextArea msgTextArea;
	
	private JTextField tfThreadNum;

	private JScrollPane msgTextPane;
	
	private JSpinner timeSelector;

	private StringBuffer msgBuffer;
	
	private TrainInfoTableModel userModel;
	
	private StatusBar statusBar;
	
	private JPanel mainPanel;
	
	private JTabbedPane tabPane;
	
	private TicketsPanel ticketsPanel;

	public MainForm() throws HeadlessException {
		super();
		this.msgBuffer = new StringBuffer();
        action.setMainForm(this);
		initComponents();
		setTitle("购票助手");
		//setSize(1000, 650);
		//setResizable(false);
		ImageIcon logo = new ImageIcon(this.getClass().getResource("/Icon-60@2x.png"));
		setIconImage(logo.getImage());
//		setLocationRelativeTo(getOwner()); // 居中显示
		setExtendedState(JFrame.MAXIMIZED_BOTH); 
//		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
		addWindowListener(new WindowAdapter() {

			@Override
			public void windowClosing(WindowEvent e) {

				int i = JOptionPane.showConfirmDialog(null, "确定要退出系统吗？", "退出系统", JOptionPane.YES_NO_OPTION);
		        if(i == JOptionPane.YES_OPTION){
		        	action.shoutdownThreadPool();
		        	System.exit(0);
		        }
			}

		});
		
		addWindowListener(new WindowAdapter() {
			@Override
			public void windowOpened(WindowEvent e) {
				//initNetwork();

				ticketsPanel.setTrainInfoRow(Arrays.asList(TrainInfoRow.fromData("5cAidSfSXimDOL2NXkwIV6iQdVtq%2Fyjg9P5iw6PuEod3k9ktdE6BNacctI%2Fw316st71vgUqLJDA9%0AVHjTaUv7RdA7MRzSiWBqzmxwVz6VLVaXfchSJp7DtJ7YU5WFq%2F7WZzzBfHbNZbJerc2mVAojM7Qm%0AzA00tj1pACqHGuEmDVsy7oMXMXG3iHFV7%2Bv6xFsX4%2FxstUE4o4YRrEtvqbzWMMqwqIDctN%2Fv1fIV%0AM7D7VzoI4O%2Be|预订|5l00000G8523|G85|AOH|IZQ|AOH|IZQ|08:00|14:51|06:51|Y|bCW2hjgxU7L%2FSWI3X0DRUjwofXLcLC7OYFlPpi71jTwb%2FUxy|20171011|3|H2|01|05|0|0|||||||||||9|10|5||O0M090|OM9")));
			}
		});
		
		setVisible(true);
	}

	/**
	 * 
	 */
	protected void initComponents() {
		
		mainPanel = new JPanel(new BorderLayout());
		setContentPane(mainPanel);
		JMenu jm = new JMenu("文件"); // 创建JMenu菜单对象
		JMenu jmHelp = new JMenu("帮助"); // 创建JMenu菜单对象
		JMenu jmMy = new JMenu("我的12306"); // 创建JMenu菜单对象
		JMenuItem mnImportNew = new JMenuItem("导入新用户"); // 菜单项
		JMenuItem mnImportOld = new JMenuItem("追加新用户");// 菜单项
		jm.add(mnImportNew); // 将菜单项目添加到菜单
		jm.add(mnImportOld); // 将菜单项目添加到菜单
		JMenuItem jmiAbout = new JMenuItem("关于");
		jmHelp.add(jmiAbout);
		JMenuItem jmiLogin = new JMenuItem("登录");
		jmMy.add(jmiLogin);
		JMenuBar br = new JMenuBar(); // 创建菜单工具栏
		br.add(jm); // 将菜单增加到菜单工具栏
		br.add(jmMy);
		br.add(jmHelp);
		this.setJMenuBar(br); // 为 窗体设置 菜单工具栏
		
		mnImportNew.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				mnImportClick(true);
			}
		});
		
		mnImportOld.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				mnImportClick(false);
			}
		});
		
		jmiLogin.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				openCaptchaForm(action.getDefaultUser());
			}
		});
		
		
		tabPane = new JTabbedPane(JTabbedPane.TOP);
		mainPanel.add(tabPane, BorderLayout.CENTER);
		
		setLayout();
		
		tabPane.setSelectedIndex(0);
		
		statusBar = new StatusBar();
		mainPanel.add(statusBar, BorderLayout.SOUTH);
		   
//		JSplitPane splitPane = new JSplitPane();
//		splitPane.setOneTouchExpandable(true);
//		splitPane.setContinuousLayout(true);
		// splitPane.setPreferredSize(new Dimension(50, 100));
//		splitPane.setOrientation(JSplitPane.HORIZONTAL_SPLIT);
//
//		setLeftPanel(splitPane);
//		setMessageTextArea(splitPane);
		// splitPane.setLeftComponent(p1);
		// splitPane.setRightComponent(p2);
//		splitPane.setDividerSize(10);
//		splitPane.setDividerLocation(430);

		//setContentPane(splitPane);
		
		pack();
	}
	
	protected void setLayout() {
		ticketsPanel = new TicketsPanel();
        
        tabPane.addTab("购票", ticketsPanel);
        tabPane.addTab("记录", new JPanel());
	}

	protected void setMessageTextArea(JSplitPane splitPane) {
		msgTextArea = new JTextArea();
		msgTextPane = new JScrollPane(msgTextArea);
		msgTextArea.setFont(new Font("微软雅黑", Font.PLAIN, 14));
		msgTextArea.setEditable(false);

		splitPane.setRightComponent(msgTextPane);
	}
	
	public void initNetwork() {
		initTimeBar();
		initSpeedBar();
	}
	
	protected void initTimeBar() {
		action.getServerTime(timeStatusbar, action.getDefaultUser().getContext());
	}
	
	protected void initSpeedBar() {	
		action.setSpeedScheduled(speedStatusbar, action.getDefaultUser());
	}

	public JTable getLoginTable() {
		return loginTable;
	}
	
	/**
	 * 导入用户
	 */
	protected void mnImportClick(boolean isNew) {

		JFileChooser dlg = new JFileChooser();
		dlg.setFileFilter(new ExtensionFileFilter("txt"));
		dlg.setDialogTitle("导入用户");
		int result = dlg.showOpenDialog(this); // 打开"打开文件"对话框
		// int result = dlg.showSaveDialog(this); // 打"开保存文件"对话框
		if (result == JFileChooser.APPROVE_OPTION) {
			
			File file = dlg.getSelectedFile();
			
		}
	}

	protected void openCaptchaForm(UserInfo user) {
		CaptchaForm fmLogin = new CaptchaForm(this, user, true);
		fmLogin.setVisible(true);
	}
	
	public void showLoginForm() {
		LoginForm fmLogin = new LoginForm(this, action.getDefaultUser(), true);
		fmLogin.setVisible(true);
	}
	
	protected void grabSingleBuzu(MyButton button) {
		try {
			
//			if(!button.getUserInfo().getIsLogin()) {
//				JOptionPane.showMessageDialog(this, "请先登录！");
//				return;
//			}
//			
//			action.applyOnece(button.getUserInfo());
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		}
	}
	
	protected void startBrabBuzu() {
		
		if(Action.users.values().isEmpty()) {
			JOptionPane.showMessageDialog(this, "请先导入用户！");
			return;
		}
		
		Date beginTime = (Date)timeSelector.getValue();
		int threadNum = 0;
		try {
			threadNum = Integer.parseInt(tfThreadNum.getText().trim());
		} catch (NumberFormatException e) {
			JOptionPane.showMessageDialog(this, "请输入数字！");
			tfThreadNum.setText("");
			tfThreadNum.grabFocus();
			return;
		}
		
//		if(beginTime.getTime() < System.currentTimeMillis() + action.getTimPeriod()) {
//			JOptionPane.showMessageDialog(this, "开始时间必须大于当前服务器时间！");
//			return;
//		}
		
		if(threadNum < 0 || threadNum > 5) {
			JOptionPane.showMessageDialog(this, "线程数不得小于1或大于5！");
			tfThreadNum.setText("");
			tfThreadNum.grabFocus();
			return;
		}
		
		long period = 200; //间隔毫秒数
		
		int count = 0;
		Iterator<UserInfo> users = Action.users.values().iterator();
		while (users.hasNext()) {
			UserInfo user = (UserInfo) users.next();
			
			if(user.getIsLogin()) {
				action.setApplyJob(beginTime.getTime(), period, threadNum, user);
				writeMsg("添加任务成功...", user);
				count++;
			}
		}
		
		if(count > 0) {
			lbStatus.setText("运行中");
			lbStatus.setForeground(Color.green);
			btnGrabBuzu.setText("停止任务");
			
			writeMsg("已添加" + count + "个任务，每个任务启用" + threadNum + "个线程！");
			writeMsg("所有任务将在服务器时间" + DateUtil.date2String(beginTime, Action.CLIENT_TIME_PATTEN)  + "运行！");
		}else{
			JOptionPane.showMessageDialog(this, "请先登录！");
		}
	}
	
	public void stopBrabBuzu() {
		
		Iterator<UserInfo> users = Action.users.values().iterator();
		while (users.hasNext()) {
			UserInfo user = (UserInfo) users.next();
			user.cancelAllScheduled();
		}
		
		lbStatus.setText("已停止");
		lbStatus.setForeground(Color.RED);
		btnGrabBuzu.setText("启动任务");
		
		writeMsg("所有任务已停止！");
	}

	public void writeMsg(String msg) {
		writeMsg(msg, null);
	}
	
	public void writeMsg(String msg, UserInfo user) {

		if (msgBuffer.lastIndexOf("\n") > 20000) {
			msgBuffer.delete(0, 18000);
		}

		msgBuffer.append("[")
				.append(new SimpleDateFormat("HH:mm:ss").format(action.getServerTime().getTime()))
				.append("] ");
		
		if(user != null) {
			msgBuffer.append("- [")
			.append(user.getName())
			.append("] ");
		}
		
		msgBuffer.append(msg)
			.append('\n');

		msgTextArea.setText(msgBuffer.toString());

		JScrollBar verticalScrollBar = msgTextPane.getVerticalScrollBar();
		verticalScrollBar.setValue(verticalScrollBar.getMaximum());
	}
	
	public MyButton getTableButton(int row, int column) {
		
		return (MyButton) loginTable.getColumnModel().getColumn(column).getCellEditor().getTableCellEditorComponent(loginTable, null, false, row, column);
	}
	
	public void disableAllButton(UserInfo user) {
//		int row = ((TrainInfoTableModel)loginTable.getModel()).getRowNumber(user);
//		if(row == -1)
//			return;
//		
//		getTableButton(row, 4).setEnabled(false);
//		getTableButton(row, 5).setEnabled(false);
	}
}
