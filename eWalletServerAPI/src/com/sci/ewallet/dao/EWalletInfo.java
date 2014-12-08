package com.sci.ewallet.dao;

public class EWalletInfo {
	public static final int STATUS_INIT = 0;
	public static final int STATUS_MAIL_CHECKED = 1;
	public static final int STATUS_SMS_CHECKED = 2;
	public static final int STATUS_CERT_CHECKED = 3;
	public static final int STATUS_LOCK = 4;
	public int id;
	public int status;
	public String account;
	public String accountHash;
	public String email;
	public String emailHash;
	public String name;
	public String mobile;
	public String address;
	public String deviceId;
	public String p10Value;
	public byte[] pkData = null;
	public byte[] cert = null;
	public long checkExpire;
	public int mailCode;
	public int smsCode;
	public long create_date;
	public long update_date;
}
