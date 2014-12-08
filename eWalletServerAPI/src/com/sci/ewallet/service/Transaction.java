package com.sci.ewallet.service;

import java.util.ArrayList;
import java.util.Date;

import com.sci.ewallet.bean.TransactionBean;
import com.sci.ewallet.bean.TransactionCreateBean;
import com.sci.ewallet.bean.TransactionListBean;
import com.sci.ewallet.bean.TransactionStatusBean;
import com.sci.ewallet.dao.TransactionInfo;
import com.sci.ewallet.dao.TransactionManager;

public class Transaction {
	public TransactionStatusBean listenStatus(int tx_id) {
		TransactionStatusBean bean = new TransactionStatusBean();
		bean.status = 0;
		bean.message = "fail";
		bean.tstatus = 0;
		try {
			TransactionInfo info = TransactionManager.get(tx_id);
			if (info != null) {
				bean.status = 1;
				bean.message = "success";
				bean.tstatus = info.status;
			}
		} catch (Exception e) {
		}
		return bean;
	}

	public TransactionCreateBean create(int source_id, int source_count,
			String tpaytype, int price) {
		TransactionCreateBean bean = new TransactionCreateBean();
		bean.status = 0;
		bean.message = "fail";
		bean.txId = 0;
		long now = System.currentTimeMillis();
		TransactionInfo info = new TransactionInfo();
		info.price = price;
		info.type = tpaytype;
		info.source_id = source_id;
		info.source_count = source_count;
		info.status = 0;
		info.create_date = now;
		info.update_date = now;
		if (TransactionManager.insert(info)) {
			bean.status = 1;
			bean.message = "success";
			bean.txId = info.id;
		}
		return bean;
	}

	public TransactionStatusBean updateInfo(int tx_id, int source_id,
			String tpaytype, int price) {
		TransactionStatusBean bean = new TransactionStatusBean();
		bean.status = 0;
		bean.message = "fail";
		bean.tstatus = 0;
		// TransactionInfo info = TransactionManager.getTransaction(tx_id);
		TransactionInfo info = TransactionManager.get(tx_id, tpaytype,
				source_id, price);
		if (info != null) {
			if (info.status == 0) {
				info.status = 1;
				info.update_date = System.currentTimeMillis();
				if (TransactionManager.update(info)) {
					bean.status = 1;
					bean.message = "success";
					bean.tstatus = 1;
				}
			} else {
				bean.status = 2;
				bean.message = "duplicate";
			}
		}
		return bean;
	}

	public TransactionStatusBean scanUpdateInfo(int source_id,
			int source_count, int dest_id, int dest_count, String tpaytype,
			int price, int tx_id, String issuer, String serial_no, int card_id,
			String card_no) {
		TransactionStatusBean bean = new TransactionStatusBean();
		bean.status = 0;
		bean.message = "fail";
		bean.tstatus = 0;
		TransactionInfo info = TransactionManager.get(tx_id);
		if (info != null) {
			if (info.status == 0) {
				info.source_count = source_count;
				info.dest_id = dest_id;
				info.dest_count = dest_count;
				info.issuer = issuer;
				info.serial_no = serial_no;
				info.card_id = card_id;
				info.card_no = card_no;
				info.status = 1;
				info.update_date = System.currentTimeMillis();
				if (TransactionManager.update(info)) {
					bean.status = 1;
					bean.message = "success";
					bean.tstatus = 1;
				}
			} else {
				bean.status = 2;
				bean.message = "duplicate";
			}
		}
		return bean;
	}

	public TransactionListBean list(int source_id) {
		TransactionListBean bean = new TransactionListBean();
		bean.status = 0;
		bean.message = "fail";
		try {
			ArrayList<TransactionInfo> list = TransactionManager.list(
					source_id, 1);
			TransactionBean[] data = new TransactionBean[list.size()];
			for (int i = 0; i < list.size(); i++) {
				TransactionInfo info = list.get(i);
				data[i] = new TransactionBean();
				data[i].sourceId = info.source_id;
				data[i].destId = info.dest_id;
				data[i].price = info.price;
				data[i].issuer = info.issuer;
				data[i].serialNo = info.serial_no;
				data[i].cardId = info.card_no;
				data[i].updateDate = new Date(info.update_date);
			}
			bean.status = 1;
			bean.message = "success";
			bean.list = data;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return bean;
	}

	public TransactionListBean listByTime(int source_id, Date time_start,
			Date time_end) {
		TransactionListBean bean = new TransactionListBean();
		bean.status = 0;
		bean.message = "fail";
		try {
			// TODO Auto-generated method stub
			// ArrayList<TransactionInfo> list =
			// TransactionManager.listByTime(source_id, time_start, time_end,
			// 1);
			// TransactionBean[] data = new TransactionBean[list.size()];
			// for (int i = 0; i < list.size();i ++) {
			// TransactionInfo info = list.get(i);
			// data[i] = new TransactionBean();
			// data[i].source_id = info.source_id;
			// data[i].dest_id = info.dest_id;
			// data[i].price = info.price;
			// data[i].issuer = info.issuer;
			// data[i].serial_no = info.serial_no;
			// data[i].card_id = info.card_no;
			// data[i].update_date = new Date(info.update_date);
			// }
			// bean.status = 1;
			// bean.message = "success";
			// bean.list = data;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return bean;
	}

	// /**
	// * Creates a new model.
	// * If creation is successful, the browser will be redirected to the 'view'
	// page.
	// */
	// public function actionWebCreate()
	// {
	// $model=new Transaction;
	//
	// // Uncomment the following line if AJAX validation is needed
	// // $this->performAjaxValidation($model);
	//
	// if(isset($_POST['Transaction']))
	// {
	// $model->attributes=$_POST['Transaction'];
	// $model->create_date = date("Y-m-d H:i:s");
	// $model->update_date = date("Y-m-d H:i:s");
	// if($model->save())
	// $this->redirect(array('view','id'=>$model->id));
	// }
	//
	// $this->render('create',array(
	// 'model'=>$model,
	// ));
	// }
	//
	// /**
	// * Updates a particular model.
	// * If update is successful, the browser will be redirected to the 'view'
	// page.
	// * @param integer $id the ID of the model to be updated
	// */
	// public function actionWebUpdate($id)
	// {
	// $model=$this->loadModel($id);
	//
	// // Uncomment the following line if AJAX validation is needed
	// // $this->performAjaxValidation($model);
	//
	// if(isset($_POST['Transaction']))
	// {
	// $model->attributes=$_POST['Transaction'];
	// if($model->save())
	// $this->redirect(array('view','id'=>$model->id));
	// }
	//
	// $this->render('update',array(
	// 'model'=>$model,
	// ));
	// }
	//
	//
	//
	// /**
	// * Displays a particular model.
	// * @param integer $id the ID of the model to be displayed
	// */
	// public function actionView($id)
	// {
	// $this->render('view',array(
	// 'model'=>$this->loadModel($id),
	// ));
	// }
	//
	//
	// // Scan Part
	// /**
	// * Creates a new model.
	// * If creation is successful, the browser will be redirected to the 'view'
	// page.
	// */
	// public function actionScanCreate()
	// {
	//
	// $respJson = array('status'=>'0', 'message'=>'fail');
	//
	// if(isset($_GET['source_id'])){
	// $sourceId = $_GET['source_id'];
	// }
	// if(isset($_GET['source_count'])){
	// $sourceCount = $_GET['source_count'];
	// }
	// if(isset($_GET['dest_id'])){
	// $destId = $_GET['dest_id'];
	// }
	// if(isset($_GET['dest_count'])){
	// $destCount = $_GET['dest_count'];
	// }
	// if(isset($_GET['price'])){
	// $price = $_GET['price'];
	// }
	// if(isset($_GET['tpaytype'])){
	// $type = $_GET['tpaytype'];
	// }
	// $model = new Transaction();
	// $model->price = $price;
	// $model->source_id = $sourceId;
	// $model->source_count = $sourceCount;
	// $model->dest_id = $destId;
	// $model->dest_count = $destCount;
	// $model->type = $type;
	// $model->status = '0';
	//
	// $model->create_date = date("Y-m-d H:i:s");
	// $model->update_date = date("Y-m-d H:i:s");
	// if($model->save()){
	// $respJson = array('status'=>'1',
	// 'message'=>'success','scan_id'=>$model->id);
	// }
	// echo CJSON::encode($respJson);
	// return;
	// }
	//
	//
	// /**
	// * Updates a particular model.
	// * If update is successful, the browser will be redirected to the 'view'
	// page.
	// * @param integer $id the ID of the model to be updated
	// */
	// public function actionUpdate($id)
	// {
	// $model=$this->loadModel($id);
	//
	// // Uncomment the following line if AJAX validation is needed
	// // $this->performAjaxValidation($model);
	//
	// if(isset($_POST['Transaction']))
	// {
	// $model->attributes=$_POST['Transaction'];
	// if($model->save())
	// $this->redirect(array('view','id'=>$model->id));
	// }
	//
	// $this->render('update',array(
	// 'model'=>$model,
	// ));
	// }
	//
	// public function actionUpdateStatus()
	// {
	// $respJson = array('status'=>'0', 'message'=>'fail');
	// if (isset($_POST['tx_id'])){
	// /*$model =
	// Transaction::model()->findByAttributes(array("id"=>$_POST['tx_id']));
	// if($model){
	// $model->type = 'store';
	// }
	//
	//
	// if($model->save()){
	// $respJson = array('status'=>'1', 'message'=>'success');
	// } */
	// $model=$this->loadModel($_POST['tx_id']);
	// }
	//
	// echo CJSON::encode($model);
	// return;
	// }
	//
	// public function actionList()
	// {
	// $respJson = array('status'=>'0', 'message'=>'fail');
	// if (isset($_POST['source_id'])){
	// $model=Transaction::model()->findAllByAttributes(array('source_id'=>$_POST['source_id'],'status'=>'1'));
	// if($model){
	// $aryTrans = array();
	// $one = array();
	// foreach($model as $oneModel){
	// $one['source_id']= $oneModel->source_id;
	// $one['dest_id']= $oneModel->dest_id;
	// $one['price']= $oneModel->price;
	// $one['issuer']= $oneModel->issuer;
	// $one['serial_no']= $oneModel->serial_no;
	// $one['card_id']= $oneModel->card_no;
	// $one['update_date']= $oneModel->update_date;
	// array_push($aryTrans, $one);
	// }
	// $respJson = array('status'=>'1', 'message'=>'success','list'=>$aryTrans);
	// }
	// }
	//
	// echo CJSON::encode($respJson);
	// return;
	// }
	// /**
	// * Deletes a particular model.
	// * If deletion is successful, the browser will be redirected to the
	// 'admin' page.
	// * @param integer $id the ID of the model to be deleted
	// */
	// public function actionDelete($id)
	// {
	// $this->loadModel($id)->delete();
	//
	// // if AJAX request (triggered by deletion via admin grid view), we should
	// not redirect the browser
	// if(!isset($_GET['ajax']))
	// $this->redirect(isset($_POST['returnUrl']) ? $_POST['returnUrl'] :
	// array('admin'));
	// }
	//
	// /**
	// * Lists all models.
	// */
	// public function actionIndex()
	// {
	// $dataProvider=new CActiveDataProvider('Transaction');
	// $this->render('index',array(
	// 'dataProvider'=>$dataProvider,
	// ));
	// }
	//
	// /**
	// * Manages all models.
	// */
	// public function actionAdmin()
	// {
	// $model=new Transaction('search');
	// $model->unsetAttributes(); // clear any default values
	// if(isset($_GET['Transaction']))
	// $model->attributes=$_GET['Transaction'];
	//
	// $this->render('admin',array(
	// 'model'=>$model,
	// ));
	// }

}
