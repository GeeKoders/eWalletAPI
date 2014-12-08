package com.sci.ewallet.service;

import org.apache.axis.MessageContext;
import org.apache.axis.session.Session;

import com.sci.ewallet.bean.MemberCreateBean;
import com.sci.ewallet.bean.MemberLoginBean;
import com.sci.ewallet.bean.StatusBean;
import com.sci.ewallet.dao.MemberInfo;
import com.sci.ewallet.dao.MemberManager;

public class Member {
	public MemberCreateBean create(String username, String name,
			String password, String mobile, String email, String address,
			String issuer, String serial_no, String mac_address) {
		MemberCreateBean bean = new MemberCreateBean();
		bean.status = 0;
		bean.message = "fail";
		long now = System.currentTimeMillis();
		MemberInfo info = new MemberInfo();
		info.username = username;
		info.name = name;
		info.password = password;
		info.mobile = mobile;
		info.email = email;
		info.address = address;
		info.issuer = issuer;
		info.serial_no = serial_no;
		info.status = 1;
		info.create_date = now;
		info.update_date = now;
		if (MemberManager.insert(info)) {
			bean.status = 1;
			bean.message = "success";
			bean.memberId = info.id;
		}
		return bean;
		// $respJson = array('status'=>'0', 'message'=>'fail');
		// $mobile = "";
		// $address = "";
		// $model = new Member();
		// if(!isset($_GET['username'])){
		// $respJson = array('status'=>'0', 'message'=>'need username');
		// echo CJSON::encode($respJson);
		// return;
		// }else{
		// $username = $_GET['username'];
		// }
		// if(!isset($_GET['name'])){
		// $respJson = array('status'=>'0', 'message'=>'need name');
		// echo CJSON::encode($respJson);
		// return;
		// }else{
		// $name = $_GET['name'];
		// }
		// if(!isset($_GET['password'])){
		// $respJson = array('status'=>'0', 'message'=>'need password');
		// echo CJSON::encode($respJson);
		// return;
		// }else{
		// $password = $_GET['password'];
		// }
		// if(isset($_GET['mobile'])){
		// $mobile = $_GET['mobile'];
		// }
		// if(!isset($_GET['email'])){
		// $respJson = array('status'=>'0', 'message'=>'need email');
		// echo CJSON::encode($respJson);
		// return;
		// }else{
		// $email = $_GET['email'];
		// }
		// if(isset($_GET['address'])){
		// $address = $_GET['address'];
		// }
		// if(!isset($_GET['issuer'])){
		// $respJson = array('status'=>'0', 'message'=>'need issuer');
		// echo CJSON::encode($respJson);
		// return;
		// }else{
		// $issuer = $_GET['issuer'];
		// }
		// if(!isset($_GET['serial_no'])){
		// $respJson = array('status'=>'0', 'message'=>'need serial_no');
		// echo CJSON::encode($respJson);
		// return;
		// }else{
		// $serialNo = $_GET['serial_no'];
		// }
		//
		// $model->username=$username;
		// $model->name=$name;
		// $model->password = $password;
		// $model->mobile = $mobile;
		// $model->email = $email;
		// $model->address = $address;
		// $model->issuer = $issuer;
		// $model->serial_no = $serialNo;
		// $model->status = "1";
		// $model->create_date = date("Y-m-d H:i:s");
		// $model->update_date = date("Y-m-d H:i:s");
		// if($model->save()){
		// $respJson = array('status'=>'1', 'message'=>'success',
		// 'member_id'=>$model->id);
		// }
		//
		// echo CJSON::encode($respJson);
		// return;
	}

	public StatusBean validate(String username, String email) {
		StatusBean bean = new StatusBean();
		bean.status = 0;
		bean.message = "fail";
		try {
			MemberInfo info_username = MemberManager.getByUsername(username);
			MemberInfo info_email = MemberManager.getByEmail(email);
			if (info_username != null) {
				bean.status = 2;
				bean.message = "duplicate username";
			} else if (info_email != null) {
				bean.status = 2;
				bean.message = "duplicate email";
			} else {
				bean.status = 1;
				bean.message = "success";
			}
		} catch (Exception e) {
		}
		return bean;
		// $respJson = array('status'=>'0', 'message'=>'fail');
		// if(isset($_POST['username'])){
		// $username = $_POST['username'];
		// }
		// if(isset($_POST['email'])){
		// $email = $_POST['email'];
		// }
		// $username =
		// Member::model()->findByAttributes(array('username'=>$username));
		// $email = Member::model()->findByAttributes(array('email'=>$email));
		// if($username){
		// $respJson = array('status'=>'2', 'message'=>'duplicate username');
		// }else if($email){
		// $respJson = array('status'=>'2', 'message'=>'duplicate email');
		// }else{
		// $respJson = array('status'=>'1', 'message'=>'success');
		// }
		// echo CJSON::encode($respJson);
		// return;
	}

	public MemberLoginBean login(String username, String password) {
		MemberLoginBean bean = new MemberLoginBean();
		bean.status = 0;
		bean.message = "fail";
		try {
			MemberInfo info = MemberManager.getByLogin(username, password);
			if (info != null) {
				MessageContext mc = MessageContext.getCurrentContext();
				Session session = mc.getSession();
				session.set("uid", info.id);
				bean.status = 1;
				bean.message = "success";
				bean.memberId = info.id;
			}
		} catch (Exception e) {
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
		// Member::model()->findByAttributes(array('name'=>$name,'password'=>$password));
		//
		// if($model){
		// $respJson = array('status'=>'1', 'message'=>'success',
		// 'member_id'=>$model->id);
		// }
		//
		// echo CJSON::encode($respJson);
		// return;
	}

	public StatusBean update(String username, String password, String name,
			String mobile, String address) {
		MessageContext mc = MessageContext.getCurrentContext();
		Session session = mc.getSession();
		// int uid = (int) session.get("uid");
		//
		StatusBean bean = new StatusBean();
		bean.status = 0;
		bean.message = "fail";

		// TODO: Valid the format of name, mobile, and address
		if (!(isValidName(name) && isValidMobile(mobile) && isValidAddress(address)))
			return bean;

		try {
			MemberInfo info = MemberManager.getByLogin(username, password);
			// MemberInfo info = MemberManager.getById(uid);
			if (info != null) {
				info.name = name;
				info.mobile = mobile;
				info.address = address;
				MemberManager.update(info);
				bean.status = 1;
				bean.message = "success";
			}
		} catch (Exception e) {
		}
		return bean;
	}

	public StatusBean updatePassword(String username, String password,
			String new_password) {
		MessageContext mc = MessageContext.getCurrentContext();
		Session session = mc.getSession();
		// int uid = (int) session.get("uid");
		//
		StatusBean bean = new StatusBean();
		bean.status = 0;
		bean.message = "fail";

		// TODO: Check Password Format
		if (!isValidPassword(new_password))
			return bean;

		try {
			// TODO Auto-generated method stub
			MemberInfo info = MemberManager.getByLogin(username, password);
			// MemberInfo info = MemberManager.getById(uid);
			if (info != null) {
				info.password = new_password;
				MemberManager.update(info);
				bean.status = 1;
				bean.message = "success";
			}
		} catch (Exception e) {
		}
		return bean;
	}

	// TODO: Check the form of the password
	private Boolean isValidPassword(String password) {
		return true;
	}

	// TODO: Check the form of the name
	private Boolean isValidName(String name) {
		return true;
	}

	// TODO: Check the form of the mobile
	private Boolean isValidMobile(String mobile) {
		return true;
	}

	// TODO: Check the form of the address
	private Boolean isValidAddress(String address) {
		return true;
	}
}
