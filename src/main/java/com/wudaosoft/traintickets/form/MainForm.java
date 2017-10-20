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
				
				JSONObject rs = JSON.parseObject("{\"validateMessagesShowId\":\"_validatorMessage\",\"status\":true,\"httpstatus\":200,\"data\":{\"result\":[\"|预订|6i00000G720F|G72|NZQ|BXP|IZQ|BXP|07:46|18:21|10:35|N|iKl20iifL1n0%2FqlHfzdBM3hGu7yl0EsbEJaQO%2BNTT%2FPyzJqc|20171020|3|Q6|02|16|1|0|||||||||||无|无|无||O0M090|OM9\",\"4riUo0oCBWkcbTW5HoepDJXdUI6rcRIR3Rews9yDLL7CPOKMipBemW%2FLr5CcMg%2Fq5M3kFxlwWZ2Y%0A73rDy3mEU01oCWlS6%2FktaH%2BaBQys22y8QaJxNKA73WV4TVFl89Pdtoh8NG%2FQeS6iS%2FWN48u0rKkL%0ALYD7nn0ffLPE0H4zAcLotDTV2RvC7CFnmeXxQIc4Xac3JboMwQgqPoBuCQ%2Bh9lTFye0cUgxG57xx%0AIfXGi%2ByAS%2FLIUm%2FrAttNM%2FFSocE8HihVKQ%3D%3D|预订|6b0000Z20202|Z202|SEQ|BXP|GZQ|BXP|08:50|06:46|21:56|Y|fSZ6KyWGZNUuLcqirq2KRODpCyVYaG%2BOwxP%2FHDJyOraneG8IQuLTvU1r7DR8tMaiUIWk3jzXJ5I%3D|20171019|3|Q6|09|16|0|0||8||无|||有||无|有|||||1040106030|14163\",\"KbC1%2FDl%2FqUbHaYRIRN00cm5nO59oaSZmf1tIA7gHlpZuxQO%2BMrr2qPKSP%2F0lLSgm7D9m7S7HuNam%0APUb1o8W5z72eEWQ%2Fu1x2alz%2BbdVUfhIW2mx4d2FjQEAwjOoSIfKpKV5PlRdgOCKs3n2PJ1gepomV%0AddnyguAfMrdu8gIFRp0KGn3OY8NHVE7HMy8Wjxv%2FphwGfjLylakpHRXNsKlpYh%2BL025G2EQPaqFE%0Ae4AS5ts%3D|预订|6c00000G6605|G66|IZQ|BXP|IZQ|BXP|10:00|18:00|08:00|Y|zRQ%2FUoah5UEHwuZf5W0Uy8H%2BUQqFCYNpZm7Yj%2B2G9NrriTTa|20171020|3|Q9|01|06|1|0|||||||||||无|有|10||O0M090|OM9\",\"1uZ0vXyui7W6LS6LoSETUiv7VSZFaSPHDrVPOLLKc6aJUhH48Obgbzw%2BOHxF0JuaQZq6GVeCKuPj%0ALIzKIFJPrPoJIga%2FVBH8OQ3T2ryNQFeC%2FnzS4sY08aIVfzmTEsWgI68bTtrU%2BfEW%2FOFViKA1tUXO%0Ad8KEEuW9%2B2wln32t8WstzVOiY3QAApkQU6mKfPrmgI8soHdwA%2BHs4qAterkb3MF6D%2BPECdtoGswF%0AZ2cwrNw%3D|预订|6c00000G680B|G68|IZQ|BXP|IZQ|BXP|11:13|20:56|09:43|Y|GR3mv%2BT6Y4LcxF8Xv%2F%2FYpSrFHs5b1nzkYoD68c8KUaGByPwZ|20171020|3|Q7|01|16|1|0|||||||||||有|4|4||O0M090|OM9\",\"I4sC%2F%2BQfcXlGX4K2AJcQM0RNwWdy2Zlvx%2FBikFUH1X7zyryommBuhiYZg0TBRw9%2Fr9JMBzSJDwFW%0A02vLwoSxKI5MwsE1RCJA3zuIOyaVqJrXQZqF3MOLhy8Mfl1sBCet0SwjFWpRqBe6nX%2FRxfWJUM1f%0Awzo9YfwNaq0dfAbR1ZMfKwLOhPMcCFcPVnhcSy8PF7GBUCgNPnQFOF6zKNSgr2ETcIPBe%2BcV%2BBD1%0AtvOB9ivbybSrjV7weP6cYec%3D|预订|6i00000G8009|G80|NZQ|BXP|IZQ|BXP|12:11|21:49|09:38|Y|rQ56R3A%2Bboya64lDHCkwm72kr3RpMKOqyS3qA0x%2BdQ5WncfWYCD30aQxYak%3D|20171020|3|Q7|03|18|1|0|||||||无||||6|无|无||O090O0M0|O9OM\",\"0S0dT%2FG5nNsZ1lqVckWI%2B16dkU1GOPBVM57MPx%2F98cAqDxD6yTDVWk7oBvRvyie4dGQy3oXdyfuv%0AGG6lY4ZjI9VTDdv55TgdwQTw9UFZnbnj1WkX%2BkKzDsDqa9D5UOmsxiguP3NxxOAxALoRAWlSvcz0%0AUhKRkA492YoEPF2Fz4VSroMawtZfTUur79K%2FvVIaCkcl5Qn54MdBlGuUFafaS83u7B%2BfyyS22bOY%0A9RRQY%2Bg%3D|预订|6c00000G700B|G70|IZQ|BXP|IZQ|BXP|12:50|22:28|09:38|Y|YF%2FOmoQk0uLyos9LQCweXzZ0eCUvjxFg35Mzuf43VjF001G8|20171020|3|Q7|01|16|1|0||||||10|||||无|有|||O0M0P0|OMP\",\"Ot%2FqUd8Fripx5%2BvnHZyG%2BUNGtUjzvcFb5sCV%2FZn8hdGBLxg73SbJgGG8BQ8VFnT7Mwls2qrb7wBJ%0AX9q6uuRK9Ddhb3LsBX3LeCinTMwlO2oixiInqMKhGRSQSu9sStcfCP2KdWx5B4Hsc%2F8tLx3PJJy6%0AJIkgaQhil3iof%2BEddLTZCPUtBk%2FdmJqHebo9ujMFAXt92PRUsfVO0KkbIjwfhLW4pRKA%2BK1fN%2Bd1%0AxWD0iD9hpxpFdJB9OQ%3D%3D|预订|630000K6000E|K600|GZQ|BTC|GZQ|BXP|15:02|21:00|29:58|Y|DZOgOGHEDKpS5m8XprziyG8HwiDr4cpKl5iK5Pf9%2FEmeXf0utdqqZYpWK9k%3D|20171020|3|Q6|01|29|0|0||||8|||有||19|有|||||10401030|1413\",\"TwKy%2BuYqPQgzbFKIGM26ayed212SCF69xjj5zmv60wocZCsHUM0wnMLvNxvMETmgaAgXv06o3ZHW%0ACEBovxzt5H0tXLNOgMqskU6hWuuRU4NNxeXeNNggRMPOcIYP329fKDOODPcr9HCDgOVQqlb%2Bug36%0AhBPEIf%2BzEDID1F3%2FEhlYZ9zozxWuaIWtLSKdSTm2kFFUwndu2xo8DYz1P1mnYvJIc72s6g2J%2Fdr2%0AAymjplCYtlfAh%2B5gw%2FGxLT3aX0Vc|预订|6300000Z3600|Z36|GZQ|BXP|GZQ|BXP|16:25|13:44|21:19|Y|1R4L82E6YNgQiSykpCHcWfYwgAybWntXm0AJGGTOvWX4i8PNav507kL1k%2Fbtn9Ho7HbCd9IcPKY%3D|20171020|3|Q9|01|07|0|0||14||6|||有||16|有|||||1040106030|14163\",\"C1svIW35Z8I3gN1IFTNcAcDSJuvQi6G8909I%2Bo95XG0tq%2FBYeJZGBWjbt7x0AG8gP6Pux4UdcHTo%0AomslWu9izYImKexUkqPih4WWj%2F5n3AVrW0StIrmFS4ETYeZDXsyWuVQSwr0mnNtXtCeo87u4Jmbn%0AZevVwikUKLBeTK8BIYh5FhAomsH8hbGXrV4%2FSCcbKZI392XrdLZmLRkmUgtV1d5LjxAw5KffsGwP%0A2C1e72Yj6TEcYHlsdod%2FKQN3D7MfA2kZZg%3D%3D|预订|6500000Z9804|Z98|GGQ|BXP|GGQ|BXP|18:06|15:30|21:24|Y|pN6HgegzRF3zvQDTg2XGZT0tihcRwlbqxiCvdaiVpQ9d7jdh8U6QuymUowlbwSAUShFHNv%2BvspQ%3D|20171020|3|Q9|01|07|0|0||16||9|||无||1|有|||||1040601030|14613\",\"KoguZ%2FSuPxRgc%2BFEC8BbB9XnJFY6wwSrIT0W1rdm8zF5BpLeHpp1QdKFvlsrChgUxgB40SyHpG8q%0AfFm4JuEtvqUv%2F682JkeYO5nmRb9XHgwTMqAt6J6e3pii3NgloVUWg2CzpBpnUoKpQUZGUxt8fOY%2B%0AKJwQZyCLtvwkVD9l%2FlIqFuE4rD3soYfGnDwhwf%2FJuf3SpLoVdALYLaMa0eyRw1etRdWpr7iv|预订|6e0000D92408|D924|ZHQ|BXP|IZQ|BXP|20:10|06:51|10:41|Y|rWipSM9z45ySIRbLhDNdATWUKBbFijch|20171020|3|Q7|03|07|1|0|||||||||||7|||有|F0O0|FO\",\"UgWgmmz%2BUiz1XBTV16RqJGahWqWN6duCJ%2BGBpBJGA%2FW75G6zG0ocyq716osLThtVzFimdfuFeqNo%0A7TCCvDGOPmYcmgIkOw0nupA5ATN1nZyRJZb5IB%2F5gBLWheatxnpYVzu4VQJgvogBRFhyQpQNyven%0AchocFENyRYRcactHjqVIaMr9vyA8hsb4%2FlydMdTG2h%2F5DP4tOJEyHRChuLy%2BQenvo6QSxkSB|预订|6i0000D9020E|D902|IOQ|BXP|IZQ|BXP|20:15|06:56|10:41|Y|9Iv4jtubvZ79V0i9pQb8Bd5htMzSn5NP|20171020|3|Q6|02|05|1|0|||||||||||有|||有|O0F0|OF\",\"A9Au0%2BX%2Bz0ES1ohcEkI3D4xYryCMtZe9CJBNB9lYHXunGAz8cmqmH682X9zFScZYOju9GzFKfIOq%0Auk3Jw5CCBV52x9%2FHDtlxWklK9RipABivi6QoCWXZXQDhPb%2Fi2LsP0JaYAlDl4vllenlCpxDQlhb6%0AbIhmXCsEMJ%2BYP3ygpP9w8Fy1YaBJlW3zKpW8UJMes2ruvcFX7FzgTgbx9lfxhGhY3kqSJMLp|预订|6i0000D9040E|D904|IOQ|BXP|IZQ|BXP|20:20|07:01|10:41|Y|y7QzFVrDnMTGDLmwA1%2BOpH3yUqEoAB9y|20171020|3|Q9|02|06|1|0|||||||||||有|||有|F0O0|FO\",\"44kndLAnQjnqNFAGooPGg4AMKSZN%2B09Zjc6Yn8AgDS36xXWyJJD1j8M%2Fd0Jg9muKbO%2BxqqDKz8tm%0AYEO7%2FIjuv8HynhRNC81uK8weEuUcAjICFfFT%2FW%2FbJuI2MST6QHM1KJpV9axqSYfOuG0ApFzPk4dR%0AT8Gpii6%2B28j%2FIoT6C4Nwa%2BHg01Yw6Bs6H8qp%2B7MOPGPmHT%2Fg2T%2BKDQDviiW%2Fg9So%2FFuEajh%2B|预订|6i0000D9100A|D910|IOQ|BXP|IZQ|BXP|20:25|07:13|10:48|Y|r3eak%2BfFU9sdBMdRIqe13dOtEsIYJiMC|20171020|3|Q7|02|07|1|0|||||||||||有|||有|F0O0|FO\",\"4tUiBQdXTKtWBhMVoSvz7JyQqZVBaEClfnDnQPlNNs4yGyP0f02LeKgwGc1e6MJR2JYpGCBTb8LM%0AwmlO9u7HdFx%2F6Nt2bpSi4ooQNNZMXrlLMl9tWUVqD5hQ%2Fr4P3I4Gq7AVboaPpKoBA5qwboZWlPIe%0AQYInv16X5otIZZHSjdHgeueHNuH4pn56gJidpQW5gSMoGM7wcwdqMlVOrCL%2Foo53GPyQtpe4|预订|6i0000D9280D|D9288|IOQ|BXP|IZQ|BXP|20:30|07:18|10:48|Y|7Uwj5IyiTnC6bNXBO50TyFiKv0Lz6mYY|20171020|3|Q7|02|07|1|0|||||||||||3|||1|O0F0|OF\"],\"flag\":\"1\",\"map\":{\"GGQ\":\"广州东\",\"BXP\":\"北京西\",\"GZQ\":\"广州\",\"IZQ\":\"广州南\"}},\"messages\":[],\"validateMessages\":{}}");
				
				String messages = rs.getString("messages");
				if(!"[]".equals(messages))
					throw new ServiceException(messages);
				
				JSONObject data = rs.getJSONObject("data");
				
				if(data == null)
					return;
				
				JSONArray result = data.getJSONArray("result");
				
				if(result == null || result.isEmpty())
					return;
				
				String[] rows = new String[result.size()];
				result.toArray(rows);
				
				List<TrainInfoRow>  rowList = new ArrayList<TrainInfoRow>(rows.length);
				
				for(String r : rows) {
					rowList.add(TrainInfoRow.fromData(r));
				}

				ticketsPanel.updateTrainTable(rowList);
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
