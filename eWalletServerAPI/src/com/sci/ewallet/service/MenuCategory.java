package com.sci.ewallet.service;

import java.util.ArrayList;

import com.sci.ewallet.bean.MenuCategoryBean;
import com.sci.ewallet.bean.MenuCategoryListBean;
import com.sci.ewallet.dao.MenuCategoryInfo;
import com.sci.ewallet.dao.MenuCategoryManager;

public class MenuCategory {

	public MenuCategoryListBean list() {
		MenuCategoryListBean bean = new MenuCategoryListBean();
		bean.message = "fail";

		try {
			ArrayList<MenuCategoryInfo> list = MenuCategoryManager.getList();
			MenuCategoryBean[] data = new MenuCategoryBean[list.size()];
			for (int i = 0; i < list.size(); i++) {
				MenuCategoryInfo info = list.get(i);
				data[i] = new MenuCategoryBean();
				data[i].id = info.id;
				data[i].name = info.name;
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
