package com.sci.ewallet.service;

import java.util.ArrayList;

import com.sci.ewallet.bean.MenuNameBean;
import com.sci.ewallet.bean.MenuNameListBean;
import com.sci.ewallet.dao.MenuNameInfo;
import com.sci.ewallet.dao.MenuNameManager;

public class MenuName {

	public MenuNameListBean list() {
		MenuNameListBean bean = new MenuNameListBean();
		bean.message = "fail";

		try {
			ArrayList<MenuNameInfo> list = MenuNameManager.getList();

			MenuNameBean[] data = new MenuNameBean[list.size()];
			for (int i = 0; i < list.size(); i++) {
				MenuNameInfo info = list.get(i);
				data[i] = new MenuNameBean();
				data[i].id = info.id;
				data[i].categoryId = info.category_id;
				data[i].name = info.name;
				// data[i].price = info.price;
			}
			bean.status = 1;
			bean.message = "success";
			bean.list = data;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return bean;
	}
}
