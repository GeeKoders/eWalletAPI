package com.sci.ewallet.service;

import com.sci.ewallet.bean.StatusBean;
import com.sci.ewallet.bean.StoreLoginBean;
import com.sci.ewallet.dao.StoreInfo;
import com.sci.ewallet.dao.StoreManager;

public class Store {

	public StatusBean create(String name, String password) {
		StatusBean bean = new StatusBean();
		bean.status = 0;
		bean.message = "fail";
		long now = System.currentTimeMillis();
		StoreInfo info = new StoreInfo();
		info.name = name;
		info.password = password;
		info.status = 1;
		info.create_date = now;
		info.update_date = now;
		if (StoreManager.insert(info)) {
			bean.status = 1;
			bean.message = "success";
		}
		return bean;

		// $respJson = array('status'=>'0', 'message'=>'fail');
		//
		// if(isset($_GET['name'])){
		// $name = $_GET['name'];
		// }
		// if(isset($_GET['password'])){
		// $password = $_GET['password'];
		// }
		//
		// $model = new Store();
		// $model->name=$name;
		// $model->price = $password;
		// $model->status = "1";
		// $model->create_date = date("Y-m-d H:i:s");
		// $model->update_date = date("Y-m-d H:i:s");
		// if($model->save()){
		// $respJson = array('status'=>'1', 'message'=>'success');
		// }
		//
		// echo CJSON::encode($respJson);
		// return;
	}

	public StoreLoginBean login(String name, String password) {
		StoreLoginBean bean = new StoreLoginBean();
		bean.status = 0;
		bean.message = "fail";
		try {
			StoreInfo info = StoreManager.get(name, password);
			if (info != null) {
				bean.status = 1;
				bean.message = "success";
				bean.storeId = info.id;
			}
		} catch (Exception e) {
			bean.message = e.getMessage();
		}
		return bean;
		// $respJson = array('status'=>'0', 'message'=>'fail');
		//
		// if(isset($_GET['name'])){
		// $name = $_GET['name'];
		// }
		// if(isset($_GET['password'])){
		// $password = $_GET['password'];
		// }
		// $model =
		// Store::model()->findByAttributes(array('name'=>$name,'price'=>$password));
		//
		// if($model){
		// $respJson = array('status'=>'1', 'message'=>'success',
		// 'store_id'=>$model->id);
		// }
		//
		// echo CJSON::encode($respJson);
		// return;
	}
}
