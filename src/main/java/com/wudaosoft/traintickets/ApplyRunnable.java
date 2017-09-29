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

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.wudaosoft.traintickets.model.ApplyStatus;
import com.wudaosoft.traintickets.model.UserInfo;

/**
 * @author changsoul.wu
 *
 */
public class ApplyRunnable implements Runnable {
	
	private static final Logger log = LoggerFactory.getLogger(ApplyRunnable.class);
	
	private UserInfo userInfo;
	
	private Action action = Action.getInstance();
	
	/**
	 * @param userInfo
	 * @param request
	 * @param action
	 */
	public ApplyRunnable(UserInfo userInfo) {
		super();
		this.userInfo = userInfo;
	}


	/* (non-Javadoc)
	 * @see java.lang.Runnable#run()
	 */
	@Override
	public void run() {

		try {
			action.getMainForm().writeMsg("线程" + Thread.currentThread().getName() + "开始运行...", userInfo);
			
			String applyId = userInfo.getApplyId();
			Map<String, Object> applyData = userInfo.getApplyData();
			
			boolean isSubmit = action.checkIfHasSubmitted(applyId, userInfo);
			
			if(!isSubmit) {
				
				applyData = action.getZsxx(userInfo);
				
				int syzbs = (int)applyData.get("SYZBS");
				boolean isLackOfIndicators = (boolean)applyData.get("isLackOfIndicators");
				
				if(isLackOfIndicators) {
					action.getMainForm().writeMsg("提交失败，缺乏指标", userInfo);
					return;
				}
				
				if(syzbs <= 0) {
					action.getMainForm().writeMsg("提交失败，本月剩余补贴指标为0", userInfo);
					return;
				}
				
				applyData.put("SYZBS", syzbs + "");
				applyData.put("isLackOfIndicators", isLackOfIndicators ? "true" : "false");
				
				isSubmit = action.submitBtxx(applyData, userInfo);
			}
			
			if(isSubmit) {
				userInfo.setIsLogin(false);
				userInfo.setApplyStatus(ApplyStatus.submited);
				userInfo.cancelAllScheduled();
				action.getMainForm().disableAllButton(userInfo);
				action.getMainForm().getLoginTable().selectAll();
				action.getMainForm().writeMsg("恭喜你，提交申请成功！停止任务！", userInfo);
				
				try {
					action.logout(userInfo);
				} catch (Exception e) {
				}
				
				log.info("UserId[" + userInfo.getLoginId() + "] Do apply submit successful.");
			}
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		}

	}

}
