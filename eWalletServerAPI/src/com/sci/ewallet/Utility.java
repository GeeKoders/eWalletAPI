package com.sci.ewallet;

import java.io.IOException;
import java.security.cert.X509Certificate;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;

import com.jrsys.mpki.MCryptoException;
import com.jrsys.service.RAService;
import com.sci.ewallet.bean.StatusBean;

public class Utility {

	private static Utility instance = null;
	private SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmm");

	public static Utility getInstance() {
		if (instance == null) {
			instance = new Utility();
		}
		return instance;
	}

	public X509Certificate getCertUid(String p10Value)
			throws MCryptoException {
		String certUid = certEnroll(p10Value);
		RAService raService = new RAService();
		raService.setServiceEndpoint(Config.CERT_SERVICE_ENDPOINT);
		X509Certificate cert = raService.fetchCertByUid(certUid);
		return cert;
	}

	public String certEnroll(String p10Value) throws MCryptoException {
		RAService raService = new RAService();
		raService.setServiceEndpoint(Config.CERT_SERVICE_ENDPOINT);
		String reqCertUid = "";
		try {
			reqCertUid = raService.reqCertByP10(p10Value, "b64");
		} finally {
		}
		return reqCertUid;
	}

	public void sendMail(String host, final String username, final String password, String from, String to, String subject,
			String text) {
		Properties props = System.getProperties();
		props.setProperty("mail.smtp.host", host);
		props.put("mail.smtp.auth", "true");
		props.put("mail.smtp.starttls.enable", "true");
		Session session = Session.getDefaultInstance(props,
				  new javax.mail.Authenticator() {
					protected PasswordAuthentication getPasswordAuthentication() {
						return new PasswordAuthentication(username, password);
					}
				  });
		try {
			Message message = new MimeMessage(session);
			message.setFrom(new InternetAddress(from));
			message.addRecipient(Message.RecipientType.TO, new InternetAddress(
					to));
			message.setSubject(subject);
			message.setText(text);
			Transport.send(message);
		} catch (MessagingException mex) {
			mex.printStackTrace();
		}
	}

	public StatusBean sendSMS(String mobile, String message) {
		StatusBean bean = new StatusBean();
		bean.status = 1;
		bean.message = "fail";
		try {
			long startDateTime = System.currentTimeMillis();
			long StopDateTime = startDateTime + 3600000;
			String url = "http://xsms.aptg.com.tw/XSMSAP/api/APIRTFastHttpRequest";
			StringBuffer content = new StringBuffer();
			content.append("<Request>");
			content.append("<Subject>SCI TEST</Subject>");
			content.append("<Retry>N</Retry>");
			content.append("<AutoSplit>N</AutoSplit>");
			content.append("<StartDateTime>" + sdf.format(startDateTime) + "</StartDateTime>");
			content.append("<StopDateTime>" + sdf.format(StopDateTime) + "</StopDateTime>");
			content.append("<Message>" + message + "</Message>");
			content.append("<MDNList>");
			content.append("<MSISDN>" + mobile + "</MSISDN>");
			content.append("</MDNList>");
			content.append("</Request>");
			List<NameValuePair> params = new ArrayList<NameValuePair>();
			params.add(new BasicNameValuePair("MDN", "0977608836"));
			params.add(new BasicNameValuePair("UID", "smartcatch"));
			params.add(new BasicNameValuePair("UPASS", "26560986"));
			params.add(new BasicNameValuePair("Content", content.toString()));
			String result = callServicePost(url, params);
	        String patternStr = "<Reason>([^<]*)</Reason><Code>([0-9]+)</Code>";
	        Pattern pattern = Pattern.compile(patternStr);
	        Matcher matcher = pattern.matcher(result);
	        if (matcher.find()) {
		        bean.status = Integer.parseInt(matcher.group(2));;
		        bean.message = matcher.group(1);;
	        }
		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return bean;
	}
	private String callServicePost(String url, List<NameValuePair> params) throws ClientProtocolException, IOException {
		// TODO Auto-generated method stub
		String result = "";
		HttpClient client = new DefaultHttpClient();
		HttpPost post = new HttpPost(url);
		if (params != null) {
			post.setEntity(new UrlEncodedFormEntity(params, HTTP.UTF_8));
		}
		HttpResponse resp = client.execute(post);
		if (resp.getStatusLine().getStatusCode() == 200) {
			result = EntityUtils.toString(resp.getEntity());
		}
		client.getConnectionManager().shutdown();
		return result;
	}

}
