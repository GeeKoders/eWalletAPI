package com.sci.ewallet.service;

import java.security.GeneralSecurityException;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.security.cert.CertificateEncodingException;
import java.security.cert.X509Certificate;
import java.security.spec.X509EncodedKeySpec;
import java.util.Random;

import javax.jws.WebMethod;
import javax.jws.WebService;

import com.jrsys.commons.codec.binary.Base64;
import com.jrsys.mpki.IdPasswordEncUtil;
import com.jrsys.mpki.MCryptoException;
import com.sci.ewallet.Config;
import com.sci.ewallet.Utility;
import com.sci.ewallet.bean.EWalletCertBean;
import com.sci.ewallet.bean.EWalletLoginBean;
import com.sci.ewallet.bean.EWalletRigisterBean;
import com.sci.ewallet.bean.StatusBean;
import com.sci.ewallet.dao.EWalletInfo;
import com.sci.ewallet.dao.EWalletManager;
@WebService
public class EWallet {
	private static final long EXPIRE_PERIOD = 3600000*24;
	private Random rnd = new Random(System.currentTimeMillis());
	private Utility utility = Utility.getInstance();
	@WebMethod
	public StatusBean validate(String account, String email) {
		StatusBean bean = new StatusBean();
		bean.status = StatusBean.STATUS_FAIL;
		bean.message = "fail";
		//
		EWalletInfo info_account = EWalletManager.getByAccount(account);
		EWalletInfo info_email = EWalletManager.getByEmail(email);
		if (info_account != null) {
			bean.status = StatusBean.STATUS_FAIL;
			bean.message = "duplicate account";
		} else if (info_email != null) {
			bean.status = StatusBean.STATUS_FAIL;
			bean.message = "duplicate email";
		} else {
			bean.status = StatusBean.STATUS_SUCCESS;
			bean.message = "success";
		}
		return bean;
	}

	public EWalletRigisterBean rigister(String account, String email,
			String password, String name, String mobile, String address,
			String deviceId) {
		EWalletRigisterBean bean = new EWalletRigisterBean();
		bean.status = StatusBean.STATUS_FAIL;
		bean.message = "fail";
		long now = System.currentTimeMillis();
		// check duplicate
		EWalletInfo info_account = EWalletManager.getByAccount(account);
		EWalletInfo info_email = EWalletManager.getByEmail(email);
		if (info_account != null || info_email != null) {
			bean.message = "duplicate";
			return bean;
		}
		//
		try {
			EWalletInfo info = new EWalletInfo();
			info.status = EWalletInfo.STATUS_INIT;
			info.account = account;
			info.email = email;
			info.accountHash = new String(Base64.encodeBase64(IdPasswordEncUtil.enc(account, password)));
			info.emailHash = new String(Base64.encodeBase64(IdPasswordEncUtil.enc(email, password)));
			info.name = name;
			info.mobile = mobile;
			info.address = address;
			info.deviceId = deviceId;
			info.create_date = now;
			info.update_date = now;
			info.checkExpire = System.currentTimeMillis() + EXPIRE_PERIOD;
			info.mailCode = rnd.nextInt(999999);
			info.smsCode = rnd.nextInt(999999);
			if (EWalletManager.insert(info)) {
				bean.status = StatusBean.STATUS_SUCCESS;
				bean.message = "success";
				bean.id = info.id;
			} else {
				bean.message = "db lock";
			}
		} catch (NoSuchAlgorithmException e) {
			bean.message = "algorithm error!";
		} catch (Exception e) {
			bean.message = "unknow error!";
		}
		return bean;
	}


	public StatusBean askMailCheck(String account, String password) {
		StatusBean bean = new StatusBean();
		bean.status = StatusBean.STATUS_FAIL;
		bean.message = "fail";
		EWalletInfo info = EWalletManager.login(account, password);
		if (info != null) {
			info.status = EWalletInfo.STATUS_INIT;
			info.checkExpire = System.currentTimeMillis()
					+ EXPIRE_PERIOD;
			info.mailCode = rnd.nextInt(999999);
			info.smsCode = rnd.nextInt(999999);
			if (EWalletManager.save(info)) {
				String url = Config.URL_BASE + "/AskSMSCheck.jsp?account=" + info.account + "&mailCode=" + info.mailCode;
				String message = info.name + " 您好, 您申請的電子錢包已經核發下來了，請點選下列連結發送PIN碼簡訊: 取得交易PIN碼\n" + url;
				utility.sendMail("mail.smartcatch.com.tw", "sci.wallet", "25121683",
						"sci.wallet@smartcatch.com.tw", info.email, "SCI EMail Check",
						message);
				bean.status = StatusBean.STATUS_SUCCESS;
				bean.message = "success";
			} else {
				bean.message = "db lock";
			}
		}
		return bean;
	}

//	public StatusBean askSMSCheck(String account, String password, int mailCode) {
	public StatusBean askSMSCheck(String account, int mailCode) {
		StatusBean bean = new StatusBean();
		bean.status = StatusBean.STATUS_FAIL;
		bean.message = "fail";
		try {
			EWalletInfo info = EWalletManager.getByAccount(account);
//			EWalletInfo info = EWalletManager.login(account, password);
			if (info != null && mailCode != 0 && info.mailCode == mailCode
					&& System.currentTimeMillis() < info.checkExpire) {
				info.status = EWalletInfo.STATUS_MAIL_CHECKED;
				info.mailCode = 0;
				if (EWalletManager.save(info)) {
					String message = info.name + " 您好, 您的交易 PIN 碼為:" + info.smsCode + "，請登入後進行變更";
					bean.status = StatusBean.STATUS_SUCCESS;
					StatusBean tmp = utility.sendSMS(info.mobile, new String(message.getBytes("UTF-8"), "ISO-8859-1"));
					if (tmp.status == 0) {
						bean.status = StatusBean.STATUS_SUCCESS;
						bean.message = "success";
					} else {
						bean.message = "API:" + tmp.message;
					}
				} else {
					bean.message = "db lock";
				}
			} else {
				if (info == null) {
					bean.message = "login fail!";
				} else {
					bean.message = "" + info.mailCode + " " + info.checkExpire;
				}
			}
		} catch (Exception e) {
			bean.message = e.getMessage();
			e.printStackTrace();
		}
		return bean;
	}

	public StatusBean askActive(String account, String password, int smsCode) {
		StatusBean bean = new StatusBean();
		bean.status = StatusBean.STATUS_FAIL;
		bean.message = "fail";
		EWalletInfo info = EWalletManager.login(account, password);
		if (info != null && info.smsCode == smsCode
				&& System.currentTimeMillis() < info.checkExpire) {
			info.status = EWalletInfo.STATUS_SMS_CHECKED;
			if (EWalletManager.save(info)) {
				bean.status = StatusBean.STATUS_SUCCESS;
				bean.message = "success";
			} else {
				bean.message = "db lock";
			}
		}
		return bean;
	}

	public EWalletCertBean askCert(String account, String password, String pkData, String p10Value) {
		EWalletCertBean bean = new EWalletCertBean();
		bean.status = StatusBean.STATUS_FAIL;
		bean.message = "fail";
		//
		try {
			byte[] pkData2 = Base64.decodeBase64(pkData);
			PublicKey publicKey = KeyFactory.getInstance("RSA").generatePublic(new X509EncodedKeySpec(pkData2));
			EWalletInfo info = EWalletManager.login(account, password);
			if (info != null && info.status == EWalletInfo.STATUS_SMS_CHECKED) {
				info.status = EWalletInfo.STATUS_CERT_CHECKED;
				X509Certificate cert = utility.getCertUid(p10Value);
				if (cert != null) {
					info.p10Value = p10Value;
					info.pkData = pkData2;
					info.cert = cert.getEncoded();
					if (EWalletManager.save(info)) {
						bean.status = StatusBean.STATUS_SUCCESS;
						bean.message = "success";
						bean.cert = new String(Base64.encodeBase64(info.cert));
					} else {
						bean.message = "db lock";
					}
				}
			}
		} catch (MCryptoException e) {
			bean.message = "cert error!";
			e.printStackTrace();
		} catch (CertificateEncodingException e) {
			bean.message = "cert error!";
			e.printStackTrace();
		} catch (Exception e) {
			bean.message = e.getMessage();
			e.printStackTrace();
		}
		return bean;
	}
	public EWalletLoginBean login(String account, String password) {
		EWalletLoginBean bean = new EWalletLoginBean();
		bean.status = StatusBean.STATUS_FAIL;
		bean.message = "fail";
		//
		EWalletInfo info = EWalletManager.login(account, password);
		if (info != null) {
			bean.status = StatusBean.STATUS_SUCCESS;
			bean.message = "success";
			bean.id = info.id;
			bean.accountStatus = info.status;
		}
		return bean;
	}

	public StatusBean update(String account, String password, String name,
			String mobile, String address) {
		StatusBean bean = new StatusBean();
		bean.status = StatusBean.STATUS_FAIL;
		bean.message = "fail";
		//
		if (!(isValidName(name) && isValidMobile(mobile) && isValidAddress(address)))
			return bean;
		EWalletInfo info = EWalletManager.login(account, password);
		if (info != null) {
			info.name = name;
			info.mobile = mobile;
			info.address = address;
			if (EWalletManager.save(info)) {
				bean.status = StatusBean.STATUS_SUCCESS;
				bean.message = "success";
			} else {
				bean.message = "db lock";
			}
		}
		return bean;
	}

	public StatusBean changePassword(String account, String password,
			String passwordNew) {
		StatusBean bean = new StatusBean();
		bean.status = StatusBean.STATUS_FAIL;
		bean.message = "fail";
		// TODO: Check Password Format
		if (!isValidPassword(passwordNew))
			return bean;
		//
		try {
			EWalletInfo info = EWalletManager.login(account, password);
			if (info != null) {
				info.accountHash = new String(Base64.decodeBase64(IdPasswordEncUtil.enc(info.account, password)));
				info.emailHash = new String(Base64.decodeBase64(IdPasswordEncUtil.enc(info.email, password)));
				if (EWalletManager.save(info)) {
					bean.status = StatusBean.STATUS_SUCCESS;
					bean.message = "success";
				} else {
					bean.message = "db lock";
				}
			}
		} catch (NoSuchAlgorithmException e) {
			bean.message = "algorithm error!";
		} catch (GeneralSecurityException e) {
			e.printStackTrace();
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
