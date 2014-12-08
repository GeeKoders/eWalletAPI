package com.sci.ewallet.service;

import com.sci.ewallet.bean.DataCreateBean;
import com.sci.ewallet.dao.DataInfo;
import com.sci.ewallet.dao.DataManager;

public class Data {

	public DataCreateBean create(String issuer, String serial_no, String cert,
			String pfx) {
		DataCreateBean bean = new DataCreateBean();
		bean.status = 0;
		bean.message = "fail";
		long now = System.currentTimeMillis();
		DataInfo info = new DataInfo();
		info.issuer = issuer;
		info.serial_no = serial_no;
		info.cert = cert;
		info.pfx = pfx;
		info.status = 1;
		info.create_date = now;
		info.update_date = now;
		if (DataManager.insert(info)) {
			bean.status = 1;
			bean.message = "success";
			bean.companyId = info.id;
		}
		return bean;
		// $respJson = array('status'=>'0', 'message'=>'fail');
		//
		// if(isset($_POST['issuer'])){
		// $issuer = $_POST['issuer'];
		// }
		// if(isset($_POST['serial_no'])){
		// $serialNo = $_POST['serial_no'];
		// }
		// if(isset($_POST['cert'])){
		// $cert = $_POST['cert'];
		// }
		// if(isset($_POST['pfx'])){
		// $pfx = $_POST['pfx'];
		// }
		// $model = new Data();
		// $model->issuer=$issuer;
		// $model->serial_no = $serialNo;
		// $model->cert = $cert;
		// $model->pfx = $pfx;
		// $model->status = "1";
		// $model->create_date = date("Y-m-d H:i:s");
		// $model->update_date = date("Y-m-d H:i:s");
		// if($model->save()){
		// $respJson = array('status'=>'1',
		// 'message'=>'success','company_id'=>$model->id);
		// }
		//
		// echo CJSON::encode($respJson);
		// return;
	}
}
