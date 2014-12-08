package com.sci.ewallet.service;

import com.sci.ewallet.bean.CreditCardCreateBean;
import com.sci.ewallet.dao.CreditCardInfo;
import com.sci.ewallet.dao.CreditCardManager;

public class CreditCard {

	public CreditCardCreateBean create(int member_id, String card_number,
			String expire_date, int valid_number) {

		CreditCardCreateBean bean = new CreditCardCreateBean();
		bean.message = "fail";
		long now = System.currentTimeMillis();
		CreditCardInfo info = new CreditCardInfo();

		info.member_id = member_id;
//		info.card_name = card_name;
		info.card_number = card_number;
		info.expire_date = expire_date;
		info.valid_number = valid_number;
		info.status = 1;
		info.create_date = now;
		info.update_date = now;

		if (CreditCardManager.insert(info)) {
			bean.status = 1;
			bean.message = "success";
			bean.cardId = info.id;
		}

		return bean;

	}
}
