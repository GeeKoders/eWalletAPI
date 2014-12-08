package com.sci.ewallet.dao;

public class TransactionInfo {
	public int id;
	public int source_id;
	public String type = "";
	public int source_count;
	public int dest_id;
	public int dest_count;
	public int price;
	public String issuer = "";
	public String serial_no = "";
	public int card_id;
	public String card_no = "";
	public int status;
	public long create_date;
	public long update_date;
}
