package com.sci.ewallet.service;

import com.sci.ewallet.bean.ScanCreateBean;
import com.sci.ewallet.dao.ScanInfo;
import com.sci.ewallet.dao.ScanManager;

public class Scan {

	public ScanCreateBean create(int source_id, int source_count, int dest_id,
			int dest_count, int type, int price) {
		ScanCreateBean bean = new ScanCreateBean();
		bean.status = 0;
		bean.message = "fail";
		long now = System.currentTimeMillis();
		ScanInfo info = new ScanInfo();
		info.price = price;
		info.type = type;
		info.source_id = source_id;
		info.source_count = source_count;
		info.dest_id = dest_id;
		info.dest_count = dest_count;
		info.status = 0;
		info.create_date = now;
		info.update_date = now;
		if (ScanManager.insert(info)) {
			bean.status = 1;
			bean.message = "success";
			bean.scanId = info.id;
		}
		return bean;

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
		// if(isset($_GET['type'])){
		// $type = $_GET['type'];
		// }
		// $model = new Scan();
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
		// $respJson = array('status'=>'0',
		// 'message'=>'fail','scan_id'=>$model->id);
		// }
		// echo CJSON::encode($respJson);
		// return;
	}
}
