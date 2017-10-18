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
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

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

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.wudaosoft.traintickets.Action;
import com.wudaosoft.traintickets.cons.ApiCons;
import com.wudaosoft.traintickets.exception.ServiceException;
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
				
				JSONObject rs = JSON.parseObject("{\"validateMessagesShowId\":\"_validatorMessage\",\"status\":true,\"httpstatus\":200,\"data\":{\"result\":[\"|预订|5l00000G8523|G85|AOH|IZQ|AOH|IZQ|08:00|14:51|06:51|N|YTHBNjKwG%2FvO7lAewuFW4EOwRkPxwtqV4seTRCluFwyY47jv|20171019|3|H2|01|05|0|0|||||||||||无|无|无||O0M090|OM9\",\"FyVVIaRV431uEz56YAsUdrFpg7mZUWrthoNyRontBAtpW2m5VjV2hl8vuZhoYbZQehe1uDW8W4%2Fp%0AL4a7tfH1e8yp9trPKfqgEsLs8UCjVQ8bFM%2F2uqf3c6ZP5K2Es19c%2BL4mVReDF16I1On3tVNp7Vva%0AZw9tabFNPyRPD73pxlQMdzPpCEi4pYIShf5NJ%2Fe7eclQY7FwNWFSmJBQX1IfoMhae7cKSR7NyoYy%0AXi%2B%2FXD4m3ucH|预订|5l000G130151|G1301|AOH|IZQ|AOH|IZQ|10:23|19:03|08:40|Y|7v1DUFgE1mRsP8tZ8irIhGmzD2CTB1aTpzpoVM5%2FXKnzhDAG|20171019|3|H2|01|19|0|0|||||||||||有|有|4||O0M090|OM9\",\"1JBLsOPvwVrsPZ1Hl8P2pk%2BpcFuf1Rixa9zPp6SrBvaTd6tGPcjgTBT2a6BHZpvW4rttW81KTMjg%0Aleb%2FbRBS78yIpYvC59N1xBFtO5KaZrwof2g1qHZ23X82brdgGYR7GCi7H88BFvbiCNiElkr4oCXa%0AtQ6v0i1OhV5%2FZh26Ol3ef7LvSu1snqcGviYcJbTSRe%2FlwsnALbh5kFoSd%2BdT%2FHQWNd7Hmx9u9%2FlR%0ApiUpRxA7HfytFQCAsg%3D%3D|预订|550000T16941|T169|SNH|GZQ|SNH|GZQ|10:50|05:24|18:34|Y|cIdGwxO5WmKlJN3kBt3pO1bx%2B%2Fh53ioE1uULGFD6vdLs39rR4I3ceLQ%2FZ1g%3D|20171019|3|H6|01|12|0|0||||无|||有||无|7|||||10401030|1413\",\"%2FWtb3EGd04GNywT0LamSQ5uDODyCQvaAh2Xu2lA5WrQP0V2H7Et8%2FXm%2BFpMfGSJsRU%2FiM6kUF7Tg%0AsSL5R6VBtUIsZ8UXfN%2BZ%2B1euoh1nIdL%2FB9u7Dq2W2jZvtWhSYO2Jv2sUjUkZzQ3mSpysfWCoqKdV%0AYTE6cl3t8ngBhFpFohw%2FJkN4sN75kLEYuL9CdvumYlNLGzKyWWeO7kBiZA9QnVqTsQXNpmpUX8Ff%0AECRGeZ%2FplFxa|预订|5l000G130342|G1303|AOH|IZQ|AOH|IZQ|14:10|22:45|08:35|Y|CtUwx%2Fz7jlixT1g8pXS7AInj%2F9F98nP69i5BtDF11E%2FMg1F4|20171019|3|H2|01|18|0|0|||||||||||有|20|11||O0M090|OM9\",\"r%2BCrf7m51VQLGwfCCKt%2B2kM%2BII6HS6oimekLxjFAeu84wr1xfdb3UvborNkfY0gT9LqUMej4sOPp%0AxEd1TaBGVRKXyZ3S%2BUTIJp2Y9hpqZXA9Wo%2FnF1Vzw%2B0FPe%2FqhJFNwbvrcAjGEW9x4JykYqOCTQme%0AQm8dZJIMveBVjciL47124A29891yOgSNLd1xonXFYIt%2FkEBY3rnkhNfcybX1qhtNwqxYf4rFtBNx%0AFwfb%2FFWjMCAT|预订|5l000G130520|G1305|AOH|IZQ|AOH|IZQ|15:25|23:20|07:55|Y|nEuI8WH1ish8o%2FrU5U%2Bn%2F6%2BGe0i2%2FkII0qoA4jfBg8lg55jF|20171019|3|H2|01|13|0|0|||||||||||有|有|3||O0M090|OM9\",\"VfoFytHOIzLQ2ilurhNNFhZxHY5dMkeMrqRnqfGSWwuw0s2PiqU%2BQeR7EzPHMQLw9lMx9wayhl4X%0AjoD0cl7LR38n7mgQvYTY4HVN%2BBeobyJZnlrcjajjeVto%2BK4h8qxAmZRYI4WFQFr8Ju4pFi0sevLq%0Aq%2FiS0Wzb4E160s8kIVs4xAy6iU7mY9Yu4JFPQ6v9kH5h26Mtcu5ZYNMmmIzFppJD52ZvkJtdxWd7%0AqA%3D%3D|预订|5500000Z9930|Z99|SHH|JQO|SHH|GGQ|17:45|10:08|16:23|Y|5rFyJdqSlcDqB8v8K9coN9M83Z%2BLLIGGNHbfKuwvsfVaPUAY|20171019|3|H3|01|04|0|0|||||||有||无|有|||||101030|113\",\"cTZcVmyWKaJuc%2BkYvpLFp8rP7dn4NxQz6eRuD9cBIwD%2Fdj6wRk%2BItMJU4KDSi1ALMNoTvXqEWm9J%0AvdvT%2F54ukVYcxvKr48NsarTI4pVVBDiNpEvFVZ%2B6kVtGR0lC31VRnbrYwQwG8MmAMhq6mbMi0nFd%0AsrdxwwtY6QxvgEZkNMWLlPR92frrEcwD%2BKE6uef4ZlvgOH%2FOKz5Iaxhh2yTyjWZ73qeIHjzjMRfZ%0AJ8%2BkMpWmaEugYSWKoQ%3D%3D|预订|540000K527A4|K527|NJH|GZQ|SNH|GZQ|19:15|18:48|23:33|Y|9mzjXOWnO6BX54gVZTsU5%2F7hbBjNC5EXtI%2FSqk4BOk%2BrSlh1XX1qbuj%2FcBI%3D|20171019|3|H3|06|28|0|0||||无|||有||有|有|||||10401030|1413\",\"gA7%2B3p3yC5d5yM9SpCNo15RbJoM79b12TCQVin0fClwIXGbFB%2FbQ84E6peltvly8nL%2BpeqY0mwbX%0ArNIwE5qUYdyqvFOtb6q5XIAghALnzy9rVU4duAF3J3KeLpRZmZcZ2TauTkSjD56%2Fnwq8pYK2z9XV%0AIt%2F3FwGcid4bJDVIpWnNWZ85DN%2FKcLjdewQmm1we1%2Fd1%2BLNkWSKNRL2hsWUwsIeoQEbXpkJHnwjy%0A7pTWo2DbMJvd0NzE0g%3D%3D|预订|550000K51163|K511|SNH|VUQ|SNH|GZQ|19:21|17:14|21:53|Y|OsECk%2BxHXjHpZ44f53OF5r2PF6VNtWK3Gx6npC43Ur6xT1WhEteccuS%2BOGQ%3D|20171019|3|H2|01|17|0|0||||无|||有||无|有|||||10401030|1413\"],\"flag\":\"1\",\"map\":{\"GGQ\":\"广州东\",\"AOH\":\"上海虹桥\",\"SHH\":\"上海\",\"GZQ\":\"广州\",\"SNH\":\"上海南\",\"IZQ\":\"广州南\"}},\"messages\":[],\"validateMessages\":{}}");
				
				String messages = rs.getString("messages");
				if(!"[]".equals(messages))
					throw new ServiceException(messages);
				
				JSONObject data = rs.getJSONObject("data");
				
				if(data == null)
					return;
				
				JSONArray result = data.getJSONArray("result");
				
				if(result.isEmpty())
					return;
				
				String[] rows = new String[result.size()];
				result.toArray(rows);
				
				List<TrainInfoRow>  rowList = new ArrayList<TrainInfoRow>(rows.length);
				
				for(String r : rows) {
					rowList.add(TrainInfoRow.fromData(r));
				}

				ticketsPanel.setTrainInfoRow(rowList);
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
