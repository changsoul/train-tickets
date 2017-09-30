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

import java.util.LinkedList;
import java.util.List;

import javax.swing.table.AbstractTableModel;

import com.wudaosoft.traintickets.model.UserInfo;

/**
 * @author changsoul.wu
 *
 */
@SuppressWarnings("serial")
public class UserTableModel extends AbstractTableModel {

	private List<UserInfo> users;

	public UserTableModel() {
		this.users = new LinkedList<UserInfo>();
	}

	/**
	 * @param users
	 */
	public UserTableModel(List<UserInfo> users) {
		this.users = users;
	}

	public void addUserInfo(UserInfo user) {
		this.users.add(user);
	}

	public void AddAllUserInfo(List<UserInfo> users) {
		this.users.addAll(users);
	}

	public List<UserInfo> getUsers() {
		return users;
	}

	public UserInfo getUserInfo(int row) {
		return users.get(row);
	}

	@Override
	public Object getValueAt(int row, int column) {

		if (column == 0) {
			return users.get(row).getName();
		} else if (column == 1) {
			return users.get(row).getIsLogin() ? "正常" : "未登录";
		} else if (column == 2) {
			return users.get(row).getApplyId();
		} else if (column == 3) {
			return users.get(row).getApplyStatus().getName();
		} else {
			return null;
		}
	}

	@Override
	public int getRowCount() {
		return users.size();
	}

	public int getRowNumber(UserInfo user) {

		for (int i = 0; i < users.size(); i++) {
			if (users.get(i).getLoginId().equals(user.getLoginId()))
				return i;
		}
		return -1;
	}

	@Override
	public int getColumnCount() {
		return 6;
	}

	@Override
	public boolean isCellEditable(int rowIndex, int columnIndex) {
		return (columnIndex == 4 || columnIndex == 5) ? true : false;
	}

	@Override
	public String getColumnName(int column) {
		String name = null;
		switch (column) {
		case 0:
			name = "姓名";
			break;
		case 1:
			name = "登录状态";
			break;
		case 2:
			name = "申请编号";
			break;
		case 3:
			name = "申请状态";
			break;
		case 4:
			name = "登录";
			break;
		case 5:
			name = "抢名额";
			break;
		default:
			name = super.getColumnName(column);
			break;
		}

		return name;
	}

}
