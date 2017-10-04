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
package com.wudaosoft.traintickets;

import java.awt.image.BufferedImage;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.TimeZone;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.JLabel;
import javax.swing.SwingUtilities;

import org.apache.http.HttpRequestInterceptor;
import org.apache.http.client.HttpRequestRetryHandler;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.impl.client.DefaultHttpRequestRetryHandler;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import com.wudaosoft.traintickets.cons.ApiCons;
import com.wudaosoft.traintickets.exception.ServiceException;
import com.wudaosoft.traintickets.form.MainForm;
import com.wudaosoft.traintickets.form.MyButton;
import com.wudaosoft.traintickets.model.ApplyStatus;
import com.wudaosoft.traintickets.model.LoginInfo;
import com.wudaosoft.traintickets.model.UserInfo;
import com.wudaosoft.traintickets.net.CookieUtil;
import com.wudaosoft.traintickets.net.HostConfig;
import com.wudaosoft.traintickets.net.Rails12306CookieStore;
import com.wudaosoft.traintickets.net.Rails12306HostConfig;
import com.wudaosoft.traintickets.net.Rails12306IEHeadersInterceptor;
import com.wudaosoft.traintickets.net.Request;
import com.wudaosoft.traintickets.util.DateUtil;
import com.wudaosoft.traintickets.util.StringUtils;

/**
 * @author Changsoul Wu
 * 
 */
public class Action {

	public static final String RESULT_CODE_KEY = "result_code";
	public static final String RESULT_MESSAGE_KEY = "result_message";
	public static final String RESULT_CODE_SUCCESS = "0";
	public static final String SERVER_TIME_PATTEN = "E, dd MMM yyyy HH:mm:ss";
	public static final String CLIENT_TIME_PATTEN = "yyyy-MM-dd HH:mm:ss";

	public static final Pattern APPLY_RECORD_PATTERN = Pattern.compile(
			"\\$\\(\"#ldlzy_gryw_grbtsb_bt_q_l\"\\).fwdatagrid\\([\\s\\S]+\"data\":[\\s\\S]*(\\{\"headers\":[\\s\\S]+\\}\\]\\})\\,\"pageSize\"");

	public static final Pattern APPLY_ID_PATTERN = Pattern.compile("onclick=\"ldlzy_viewSh\\('(.+?)'\\)");
	
	public static final ConcurrentMap<String, UserInfo> users = new ConcurrentHashMap<String, UserInfo>();

	private static final Logger log = LoggerFactory.getLogger(Action.class);

	private static Action instance;

	private final ScheduledExecutorService executorService = Executors
			.newScheduledThreadPool(Runtime.getRuntime().availableProcessors() * 2);
	private final ScheduledExecutorService scheduledExecutorService = Executors.newSingleThreadScheduledExecutor();
	private final ScheduledExecutorService timerExecutorService = Executors.newSingleThreadScheduledExecutor();

	private final HostConfig hostConfig;
	
	private final Request request;
	
	private final UserInfo defaultUser;

	private Calendar serverTime;

	private MainForm mainForm;

	private long timPeriod;

	public UserInfo getDefaultUser() {
		return defaultUser;
	}

	public long getTimPeriod() {
		return timPeriod;
	}

	public Calendar getServerTime() {
		return serverTime;
	}

	public MainForm getMainForm() {
		return mainForm;
	}

	public void setMainForm(MainForm mainForm) {
		this.mainForm = mainForm;
	}

	private Action() {
		defaultUser = new UserInfo();
		users.putIfAbsent("defaultUser", defaultUser);
		hostConfig = new Rails12306HostConfig();
		HttpRequestRetryHandler retryHandler = new DefaultHttpRequestRetryHandler(0, false);
		HttpRequestInterceptor requestInterceptor = new Rails12306IEHeadersInterceptor(hostConfig);
		request = Request.custom().setHostConfig(hostConfig).setDefaultCookieStoreClass(Rails12306CookieStore.class).setRetryHandler(retryHandler).setRequestInterceptor(requestInterceptor).build();
		timPeriod = 0;
		serverTime = Calendar.getInstance(TimeZone.getTimeZone("GMT+8"), Locale.US);
	}

	public static Action getInstance() {

		if (instance == null) {
			synchronized (Action.class) {
				if (instance == null) {
					instance = new Action();
				}
			}
		}

		return instance;
	}

	/**
	 * 关闭所有线程池
	 */
	public void shoutdownThreadPool() {
		executorService.shutdown();
		scheduledExecutorService.shutdown();
		timerExecutorService.shutdown();
		request.shutdown();
	}

	/**
	 * 预登录，获取登录页面表单相关数据
	 * 
	 * @param userInfo
	 * @return
	 * @throws Exception
	 */
	public LoginInfo preLogin(UserInfo userInfo) throws Exception {

		String loginForm = request.get(hostConfig.getHostUrl(), "", userInfo.getContext());

		LoginInfo info = new LoginInfo();
		info.set_1_(findHiddenInputValue(loginForm, "_1_"));
		info.setDesKey(findHiddenInputValue(loginForm, "des_key"));
		info.setTicket(findHiddenInputValue(loginForm, "ticket"));
		info.setSubsys(findHiddenInputValue(loginForm, "subsys"));

		// System.out.println(findHiddenInputValue(loginForm, "signature"));

		return info;
	}
	
	/**
	 * 
	 * @param userInfo
	 * @return
	 * @throws Exception
	 */
	public String getLeftTicketPage (HttpClientContext context) throws Exception {
		
		return request.get(hostConfig.getHostUrl(), ApiCons.LEFT_TICKET_PAGE, context);
	}
	
	public String getLoginPage (HttpClientContext context) throws Exception {
		
		return request.get(hostConfig.getHostUrl(), ApiCons.LOGIN_PAGE, context);
	}
	
	/**
	 * 
	 * @param userInfo
	 * @return
	 * @throws Exception
	 */
	public boolean authUamtk(UserInfo userInfo) throws Exception {
		
		Map<String, String> params = new LinkedHashMap<String, String>();
		params.put("appid", ApiCons.APP_ID);
		
		JSONObject rs = request.postAjax(hostConfig.getHostUrl(), ApiCons.AUTH_UAMTK_AJAX, params, userInfo.getContext());
		
		return checkApiResult("0", rs);
	}
	
	/**
	 * 
	 * @param answer
	 * @param userInfo
	 * @return
	 * @throws Exception
	 */
	public boolean captchaCheck(String answer, UserInfo userInfo) throws Exception {
		
		Map<String, String> params = new LinkedHashMap<String, String>();
		params.put("answer", answer);
		params.put("login_site", "E");
		params.put("rand", "sjrand");
		
		JSONObject rs = request.postAjax(hostConfig.getHostUrl(), ApiCons.CAPTCHA_CHECK_AJAX, params, userInfo.getContext());
		
		return checkApiResult("4", rs);
	}
	
	/**
	 * 登录
	 * 
	 * @param username
	 * @param password
	 * @param userInfo
	 * @return
	 * @throws Exception
	 */
	public JSONObject login(String username, String password, UserInfo userInfo) throws Exception {

		Map<String, String> params = new LinkedHashMap<String, String>();
		params.put("username", username);
		params.put("password", password);
		params.put("appid", ApiCons.APP_ID);

		return request.postAjax(hostConfig.getHostUrl(), ApiCons.LOGIN_AJAX, params, userInfo.getContext());
	}

	/**
	 * 退出登录
	 * @param userInfo
	 * @return
	 * @throws Exception
	 */
	public JSONObject logout(UserInfo userInfo) throws Exception {
		
		String sessionId = CookieUtil.getCookieValue("JSESSIONIDGDLDWQ", userInfo.getContext().getCookieStore());
		
		Map<String, String> params = new LinkedHashMap<String, String>();
		params.put("sessionid", sessionId);

		JSONObject data = request.postAjax(hostConfig.getHostUrl(), "", params,
				userInfo.getContext());

		log.debug("UserId[" + userInfo.getLoginId() + "] logout data: " + data);

		return data;
	}

	/**
	 * 初始化用户数据
	 * 
	 * @param userInfo
	 */
	public void initUser(final MyButton button, final UserInfo userInfo) {

		executorService.execute(new Runnable() {

			@Override
			public void run() {
				mainForm.writeMsg("正在初始化用户数据...", userInfo);

				String applyId = null;
				
				try {
					applyId = getApplyId(userInfo);
				} catch (Exception e) {
					log.error("初始化用户数据失败！" + e.getMessage(), e);
				}

				if (applyId == null) {
					userInfo.reset();
					button.setEnabled(true);
					mainForm.getLoginTable().selectAll();
					mainForm.writeMsg("未获取到申请数据，请先在官网设置申请数据。", userInfo);
					mainForm.writeMsg("初始化用户数据失败，请重新登录！", userInfo);
					try {
						logout(userInfo);
					} catch (Exception e) {
					}
					return;
				}
				
				userInfo.setApplyId(applyId);
				
				ApplyStatus status = userInfo.getApplyStatus();
				if (status != ApplyStatus.unsubmit) {
					userInfo.setIsLogin(false);
					button.setEnabled(false);
					mainForm.getTableButton(button.getRow(), 5).setEnabled(false);
					mainForm.getLoginTable().selectAll();
					
					String msg = "已受理";
					
					if (status == ApplyStatus.appling)
						msg = "申请已提交，正在审核中，请耐心等待！";
					
					if (status == ApplyStatus.passed)
						msg = "恭喜你！你的申请已办理成功！";
					
					mainForm.writeMsg(msg, userInfo);
					mainForm.writeMsg("当前申请状态不是未提交，不需要重新申请，已自动注销登录！", userInfo);
					
					try {
						logout(userInfo);
					} catch (Exception e) {
					}
					return;
				}
				
				Map<String, Object> applyData = null;
				try {
					applyData = getApplyPageData(userInfo);
				} catch (Exception e) {
					log.error("初始化用户数据失败！" + e.getMessage(), e);
				}
				
				if (applyData == null) {
					userInfo.reset();
					button.setEnabled(true);
					mainForm.getLoginTable().selectAll();
					mainForm.writeMsg("未获取到申请数据，请先在官网设置申请数据。", userInfo);
					mainForm.writeMsg("初始化用户数据失败，请重新登录！", userInfo);
					try {
						logout(userInfo);
					} catch (Exception e) {
					}
					return;
				}
				
				userInfo.setApplyData(applyData);
				mainForm.getLoginTable().selectAll();
				mainForm.writeMsg("初始化用户数据成功，申请ID：" + applyId, userInfo);
			}
		});
	}

	/**
	 * 获取服务器时间
	 * 
	 * @param timeStatusbar
	 * @param context
	 */
	public void getServerTime(final JLabel timeStatusbar, final HttpClientContext context) {
		executorService.execute(new Runnable() {

			@Override
			public void run() {
				try {
					
					getLoginPage(context);

					mainForm.writeMsg("正在获取服务器时间...");

					SimpleDateFormat format = new SimpleDateFormat(SERVER_TIME_PATTEN, Locale.US);
					format.setTimeZone(TimeZone.getTimeZone("GMT"));

					String dateStr = request.getServerTime(hostConfig.getHostUrl(), ApiCons.LOGIN_PAGE,
							context);
					setTimer(timeStatusbar);

					dateStr = dateStr.substring(0, dateStr.length() - 4);
					Date clientTime = format.parse(dateStr);

					timPeriod = clientTime.getTime() - System.currentTimeMillis();

					serverTime.setTimeInMillis(clientTime.getTime());
					String timeStr = DateUtil.date2String(serverTime.getTime(), CLIENT_TIME_PATTEN);
					timeStatusbar.setText("服务器时间：" + timeStr);
					mainForm.writeMsg("同步服务器时间到本地");
					mainForm.writeMsg("系统初始化完成！");
				} catch (Exception e) {
					log.error("获取服务器时间失败！" + e.getMessage(), e);
				}

			}
		});
	}

	/**
	 * 设置服务器时间定时更新任务，每一秒执行一次
	 * 
	 * @param timeStatusbar
	 */
	protected void setTimer(final JLabel timeStatusbar) {

		timerExecutorService.scheduleAtFixedRate(new Runnable() {
			public void run() {
				serverTime.add(Calendar.SECOND, 1);
				timeStatusbar.setText("服务器时间：" + DateUtil.date2String(serverTime.getTime(), CLIENT_TIME_PATTEN));
			}
		}, 1000, 1000, TimeUnit.MILLISECONDS);
	}

	/**
	 * 设置网络延时定时任务，每5秒执行一次
	 * 
	 * @param speedStatusbar
	 * @param systemContext
	 */
	public void setSpeedScheduled(final JLabel speedStatusbar, final UserInfo userInfo) {

		scheduledExecutorService.scheduleWithFixedDelay(new Runnable() {
			public void run() {
				try {
					
					long daly = request.testNetworkDalyByGet(hostConfig.getHostUrl(), ApiCons.NETWORK_CHECK, userInfo.getContext());
					speedStatusbar.setText("网络延时：" + daly + "ms");
					
					if(!authUamtk(userInfo)) {
						try {
							SwingUtilities.invokeAndWait(new Runnable() {
								public void run() {
									mainForm.showLoginForm();
								}
							});
						} catch (Exception exc) {
							log.error("Can't create because of " + exc.getMessage(), exc);
						}
					}
						
				} catch (Exception e) {
					log.error("测式网络延时失败！" + e.getMessage(), e);
				}
			}
		}, 1000, 5000, TimeUnit.MILLISECONDS);
	}

	/**
	 * 核心！ 设置并添加“提交申请”任务，可人工结束线程。
	 * 
	 * @param beginTimeMillis
	 *            开始时间
	 * @param period
	 *            执行间隔时间（毫秒）
	 * @param threadNum
	 *            一个任务所开线程数
	 * @param userInfo
	 *            用户上下文
	 */
	public void setApplyJob(long beginTimeMillis, long period, int threadNum, UserInfo userInfo) {

		if (threadNum < 1)
			return;

		for (int i = 0; i < threadNum; i++) {
			long initialDelay = beginTimeMillis - timPeriod - System.currentTimeMillis();

			if (initialDelay < 0)//起始时间小于当前服务器时间则立即执行
				initialDelay = 0;

			ScheduledFuture<?> future = executorService.scheduleWithFixedDelay(new ApplyRunnable(userInfo),
					initialDelay, period, TimeUnit.MILLISECONDS);

			userInfo.addFutures(future);//添加任务线程到用户上下文，以便手动停止线程
		}
	}

	/**
	 * 获取结果页面源码
	 * 
	 * @param userInfo
	 * @throws Exception
	 */
	public String getResultPageSourceCode(UserInfo userInfo) throws Exception {

		return request.get(hostConfig.getHostUrl(), "", userInfo.getContext());
	}

	
	public JSONObject getApplyRecord(UserInfo userInfo) throws Exception {

		String source = getResultPageSourceCode(userInfo);
		String dataStr = findValue(source, APPLY_RECORD_PATTERN);
		JSONObject data = null;
		try {
			data = JSON.parseObject(dataStr);
		} catch (JSONException e) {
		}

		log.debug("UserId[" + userInfo.getLoginId() + "] apply record data: " + data);

		return data;
	}
	 

	/*public JSONObject getApplyRecord(UserInfo userInfo) throws Exception {
		Map<String, String> params = new LinkedHashMap<String, String>();
		params.put("confid", "ldlzy_gryw_grbtsb_bt_q_l");
		params.put("dynDictWhereCls", "null");
		params.put("dsId", "");
		params.put("rowstart", "1");
		params.put("pageSize", "20");
		params.put("whereCls", "  a.BCC859 = c.BCC859(+)  and a.BCC857 = '2553150' and a.BOE545='01'");

		JSONObject data = request.postAjax(hostConfig.getHostUrl(), DomainConfig.GLT_PAGE, params, userInfo.getContext());

		log.debug("UserId[" + userInfo.getLoginId() + "] apply record data: " + data);

		return data;
	}*/

	/**
	 * 获取在官网上的申请ID
	 * 
	 * @param userInfo
	 * @return
	 * @throws Exception
	 */
	public String getApplyId(UserInfo userInfo) throws Exception {

		JSONObject data = getApplyRecord(userInfo);

		if (data == null)
			return null;

		int rowCount = data.getIntValue("total");
		String id = null;

		String year = DateUtil.date2String(serverTime.getTime(), "yyyy");
		
		if (rowCount > 0) {

			for (int i = rowCount - 1; i >= 0; i--) {

				JSONObject row = data.getJSONArray("rows").getJSONObject(i);
						
				String aab080 = row.getString("AAB080");
				if (aab080 == null || !aab080.startsWith(year))
					continue;
				
				String acc78b = row.getString("ACC78B");

				if (acc78b == null)
					continue;
				
				id = findValue(acc78b, APPLY_ID_PATTERN);
				id = "".equals(id) ? null : id;
				
				log.debug("UserId[" + userInfo.getLoginId() + "] applyId: " + id);

				if (acc78b.contains("未提交")) {//审核中

					userInfo.setApplyStatus(ApplyStatus.unsubmit);
					
				} else if (acc78b.contains("审核中")) {
					
					userInfo.setApplyStatus(ApplyStatus.appling);
				} else if (acc78b.contains("办理成功")) {
					
					userInfo.setApplyStatus(ApplyStatus.passed);
				}
				
				break;
			}
		}

		return id;
	}
	
	public Map<String, Object> getApplyPageData(UserInfo userInfo) throws Exception {
		
		String html = request.get(hostConfig.getHostUrl(), "", userInfo.getContext());
		
		//log.debug("UserId[" + userInfo.getLoginId() + "] getApplyPageData source code: " + html);
		
		Document doc = Jsoup.parse(html);
		Elements elements = doc.select("form[name='gtForm'] input, form[name='gtForm'] select");
		Map<String, Object> data = new LinkedHashMap<String, Object>();
		
//		elements.forEach(e -> {
//			
//			boolean isSelect = "select".equals(e.tagName());
//			
//			String name = e.attr("name");
//			String value = isSelect ? e.getElementsByAttribute("selected").val() : e.val();
//			
//			if(StringUtils.isNotBlank(name)) {
//				
//				if(data.containsKey(name) && StringUtils.isBlank(data.get(name)) && StringUtils.isNotBlank(value)) {
//					data.put(name, value);
//				} else {
//					data.put(name, value);
//				}
//				
//				if(isSelect) {
//					String text = e.text();
//					String[] texts = text.split("-");
//					if("CCE029".equals(name) && texts.length == 2) {
//						data.put("_DIC_" + name, texts[1]);
//					} else
//						data.put("_DIC_" + name, text);
//				}
//			}
//			//System.out.println(e.attr("vldStr") + "[=]" +  e.attr("name") + "=" + e.val());
//		});
		
		//data.put("readOnly", "false");
		data.put("edit", "true");
		data.put("AAB080", DateUtil.today());
		data.put("_multiple", Arrays.asList("", ""));
		
		log.debug("UserId[" + userInfo.getLoginId() + "] getApplyPageData: " + JSON.toJSONString(data));
		
		return data;
	}

	/**
	 * 检查是否已提交成功
	 * 
	 * @param id
	 * @param userInfo
	 * @return
	 * @throws Exception
	 */
	public boolean checkIfHasSubmitted(String id, UserInfo userInfo) throws Exception {
		Map<String, Object> params = new LinkedHashMap<String, Object>();
		params.put("BCC859", id);

		String rs = doService("btxxService.checkIfHasSubmitted", params, userInfo);

		log.debug("UserId[" + userInfo.getLoginId() + "] checkIfHasSubmitted: " + rs);

		return "[true]".equals(rs);
	}

	public boolean submitBtxx(final Map<String, Object> params, UserInfo userInfo) throws Exception {

		String rs = doService("btxxService.submitBtxx", params, userInfo);

		log.debug("UserId[" + userInfo.getLoginId() + "] submitBtxx: " + rs);
		
		JSONObject obj = JSON.parseArray(rs).getJSONObject(0);
		
		if("1".equals(obj.getString("flag"))){
			mainForm.writeMsg("操作成功", userInfo);
			return true;
		}
		
		if("true".equals(obj.getString("SFSBKJYYC"))){
			//不是成功提交
			
			params.put("BCC859", obj.getString("YWLSH"));
			
			mainForm.writeMsg(obj.getString("SBKERRMSG"), userInfo);
			
			return false;
		}
		else{
			//CFW.oWin.fnAlert('申请信息已经提交到'+document.gtForm.BCC864NAME.value+'人力资源和社会保障局，请于5个工作日内携带规定的资料去受理部门书面申请培训补贴！');
//			document.gtForm.BCC859.value = obj.getString("YWLSH");
//			document.gtForm.readOnly.value = "true" ;
//			document.gtForm.action = "/gdweb/ggfw/web/wsyw/app/ldlzy/gryw/grbtsb/btxx!toTjS.do";
//			document.gtForm.submit();
			params.put("BCC859", obj.getString("YWLSH"));
			params.put("readOnly", "true");
			
			Map<String, String> pars = new LinkedHashMap<String, String>();
			
			//params.entrySet().forEach(e -> pars.put(e.getKey(), e.getValue().toString()));
			
			String subRs = request.post(hostConfig.getHostUrl(), "", pars, userInfo.getContext());
			
			log.debug("UserId[" + userInfo.getLoginId() + "] submitApplyData: " + subRs);
			
			mainForm.writeMsg("申请信息已经提交到" + params.get("BCC864NAME") + "人力资源和社会保障局，请于5个工作日内携带规定的资料去受理部门书面申请培训补贴！", userInfo);
		}

		return true;
	}

	public Map<String, Object> getZsxx(UserInfo userInfo) throws Exception {
		
		Map<String, Object> data = userInfo.getApplyData();
		Map<String, Object> params = new LinkedHashMap<String, Object>();
		
		params.put("BHE034", data.get("BHE034"));// 证书类别
		params.put("CCE029", data.get("CCE029"));// 证书级别
		params.put("BCC867", data.get("BCC867"));// 工种流水号
		params.put("AAB080", data.get("AAB080"));// 申报日期
		params.put("AAC058", data.get("AAC058"));// 身份证件类型
		params.put("AAC147", data.get("AAC147"));// 身份证件号码
		params.put("AAC003", data.get("AAC003"));// 姓名
		params.put("BOE531", data.get("BOE531"));// 户口类别
		params.put("BCC870", data.get("BCC870"));// 是否贫困家庭学员
		params.put("BOD215", data.get("BOD215"));// 现居住地（行政区划）
		params.put("BCC864", data.get("BCC864"));// 申请鉴定地
		params.put("BCC859", data.get("BCC859"));// 个人补贴申请流水号
		params.put("BHB321", data.get("BHB321"));// 工种别名信息维护表流水号
		params.put("edit", data.get("edit"));// 编辑标志
		
		
		log.debug("UserId[" + userInfo.getLoginId() + "] getZsxx parametes: " + JSON.toJSONString(params));

		if (StringUtils.isBlank(data.get("BCC864")) || StringUtils.isBlank(data.get("BHE034"))
				|| StringUtils.isBlank(data.get("CCE029")) || StringUtils.isBlank(data.get("BCC867"))
				|| StringUtils.isBlank(data.get("AAB080")) || StringUtils.isBlank(data.get("AAC058"))
				|| StringUtils.isBlank(data.get("AAC147")) || StringUtils.isBlank(data.get("AAC003"))) {
			
			throw new ServiceException("getZsxx error. Illegal parameters: " + JSON.toJSONString(params));
		}

		String rs = doService("btxxService.getZsxx", params, userInfo);

		log.debug("UserId[" + userInfo.getLoginId() + "] getZsxx: " + rs);

		JSONObject obj = JSON.parseArray(rs).getJSONObject(0);

		int bcc229 = obj.getIntValue("BCC229");
		int bca060 = obj.getIntValue("BCC869");
		int syzbs = obj.getIntValue("SY2");
		boolean isLackOfIndicators = obj.getBooleanValue("isLackOfIndicators");

		data.put("BCC290", obj.get("BCC290"));
		data.put("BCC868", obj.get("BCC868"));
		data.put("BCA060", bca060 + "");
		data.put("BCC869", bca060 + "");
		data.put("ZSBAE001", obj.get("ZSBAE001"));
		data.put("ACC560", obj.get("ZSBAE001"));
		data.put("BCC229", bcc229 + "");
		data.put("BCC871", bcc229 + "");
		data.put("BCC856", obj.get("BCC856"));
		data.put("SYZBS", syzbs);
		data.put("BCEA6Z", obj.get("BCEA6Z"));
		data.put("isLackOfIndicators", isLackOfIndicators);
		data.put("ZJ", (bcc229 + bca060) + "");
		
		log.debug("UserId[" + userInfo.getLoginId() + "] getZsxx data: " + JSON.toJSONString(data));

		return data;
	}

	public String doService(String serviceName, Map<String, Object> params, UserInfo userInfo) throws Exception {

		String[] acts = serviceName.split("\\.");

		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();

		Map<String, Object> map = new LinkedHashMap<String, Object>();

		map.put("serviceId", acts[0]);
		map.put("method", acts[1]);
		map.put("parameters", params);

		list.add(map);

		Map<String, String> tmpParams = new LinkedHashMap<String, String>();
		tmpParams.put("parameters", JSON.toJSONString(list));
		tmpParams.put("method", "{}");
		tmpParams.put("shareArguments", "{}");

		log.debug("UserId[" + userInfo.getLoginId() + "] doService: " + serviceName + ", parameter: " + JSON.toJSONString(tmpParams));
		
		JSONObject result = request.postAjax(hostConfig.getHostUrl(), "", tmpParams,
				userInfo.getContext());

		log.debug("UserId[" + userInfo.getLoginId() + "] doService: " + serviceName + ", result: " + result);

		// String token = result.getString("Token");
		String fhz = result.getString("FHZ");
		// String msg = result.getString("MSG");

		if (!"1".equals(fhz)) {
			// if ("loginTimeout".equals(fhz)) {
			//
			// JOptionPane.showMessageDialog(mainForm, "登录超时，请重新登录！" + msg,
			// "提示", JOptionPane.WARNING_MESSAGE);
			// } else if ("CSRF".equals(fhz)) {
			//
			// JOptionPane.showMessageDialog(mainForm, msg, "提示",
			// JOptionPane.WARNING_MESSAGE);
			// } else {
			//
			// JOptionPane.showMessageDialog(mainForm, msg, "错误",
			// JOptionPane.WARNING_MESSAGE);
			// }
			// return "";

			throw new ServiceException("Do Service[" + serviceName + "] error. msg: " + result);
		} else {
			return result.getString("RTN");
		}
	}

	public String findHiddenInputValue(String text, String keyName) {
		Pattern pattern = Pattern.compile("<input type=\"hidden\" name=\"" + keyName + "\" value=\"(.+?)\" />");
		return findValue(text, pattern);
	}

	public String findValue(String text, Pattern pattern) {
		Matcher matcher = pattern.matcher(text);
		matcher.reset();
		boolean result = matcher.find();
		if (result) {
			return matcher.group(1);
		} else {
			return "";
		}
	}
	
	public BufferedImage getCaptchaImage(UserInfo userInfo) throws Exception {
		
		String api = ApiCons.CAPTCHA_IMAGE + "?login_site=E&module=login&rand=sjrand&" + Math.random();
		
		return request.getImage(hostConfig.getHostUrl(), api, userInfo.getContext());
	}

	/**
	 * @param userInfo
	 */
	public void applyOnece(UserInfo userInfo) {
		executorService.execute(new ApplyRunnable(userInfo));
	}
	
	private boolean checkApiResult(String code, JSONObject rs) {
		return code.equals(rs.getString(RESULT_CODE_KEY)) ? true : false;
	}

}
