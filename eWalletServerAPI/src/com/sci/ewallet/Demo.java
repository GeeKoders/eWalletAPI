package com.sci.ewallet;

import java.io.IOException;
import java.io.OutputStream;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

import com.jrsys.commons.codec.binary.Base64;
import com.jrsys.mpki.MCryptoException;
import com.jrsys.security.helper.cms.CSRGenerator;
import com.jrsys.service.RAService;

public class Demo {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Demo demo = new Demo();
		demo.onClickGenPFX();
		// demo.onClickEncDec();
	}

	// /*
	// * 加解密demo
	// * */
	// public void onClickEncDec() {
	// do_encryptAnddecrypt();
	// }

	/*
	 * 在此Button申請PFX
	 */
	public void onClickGenPFX() {
		String subjectDN = "C=TW, ST=Taiwan, O=Jrsys International Co. Ltd, CN=JoashDemo, E=joash@jrsys.com.tw";
		String subjectDNAltName = "joash@jrsys.com.tw";
		final String pfxPassword = "1qaz2wsx";
		final String keyAlias = "jrsysKey";
		String csrBase64 = "";

		try {
			// psDialog = ProgressDialog.show(this, "訊息", "請稍等，憑證申請中...");
			final KeyPair keyPair = generateKeyPair(2048); // 產生2048的金鑰對
			csrBase64 = GenCSR(subjectDN, subjectDNAltName, keyPair); // Generate
			System.out.println("p10:" + csrBase64);															// CSR

			// ========================================================

			final String genCsr64 = csrBase64;

			Thread thread = new Thread() {
				public void run() {
					String reqCertuid;
					try {
						reqCertuid = certEnroll(genCsr64); // 送出憑證請求
						GenCertAndPFX(reqCertuid, pfxPassword, keyAlias,
								keyPair);// 建立PFX及CER
						// SetText("憑證建立成功!\n");
						System.out.println("憑證建立成功! " + reqCertuid + "\n");
					} catch (MCryptoException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					// finally{
					// psDialog.dismiss();
					// }
				}
			};

			thread.start();

		} catch (MCryptoException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	// private void SetText(final String text) {
	// runOnUiThread(new Runnable() {
	// @Override
	// public void run() {
	// msgTxt.setText(text);
	// }
	// });
	// }

	/**
	 * 建立CSR
	 * */
	public String GenCSR(String subjectDN, String subjectDNAltName,
			KeyPair keyPair) throws MCryptoException {
		String p10Csr = "";
		CSRGenerator csrGenerate = new CSRGenerator();
		byte[] csr = csrGenerate.generate(subjectDN, subjectDNAltName, keyPair);
		String csrBase64 = new String(Base64.encodeBase64(csr));
		p10Csr = csrBase64;
		// Log.d("mylog", "p10CSR : " + p10Csr);
		return p10Csr;
	}

	/**
	 * 憑證請求，並透過http get取回憑證內容
	 **/
	public String certEnroll(String csrBase64) throws MCryptoException {
		RAService raService = new RAService();
		raService.setServiceEndpoint(Config.CERT_SERVICE_ENDPOINT);
		String reqCertUid = "";
		try {
			reqCertUid = raService.reqCertByP10(csrBase64, "b64");

			// Thread.sleep(5000); //設定處理時間
			//
			// String url =
			// "http://www.jrsys.com.tw/jrsysra/service/mailAuth.jsp?uid=";
			// String getCertUrl = url + reqCertUid;
			// HttpClient client = new DefaultHttpClient();
			// HttpGet get = new HttpGet(getCertUrl);
			// HttpResponse resp = client.execute(get);
			// HttpEntity resEntity = resp.getEntity();

			// } catch (InterruptedException e) {
			// // TODO Auto-generated catch block
			// throw new MCryptoException("InterruptedException:" + e);
			// // e.printStackTrace();
			// } catch (ClientProtocolException e) {
			// // TODO Auto-generated catch block
			// throw new MCryptoException("InterruptedException:" + e);
			// } catch (IOException e) {
			// // TODO Auto-generated catch block
			// throw new MCryptoException("InterruptedException:" + e);
		} finally {

		}

		return reqCertUid;
	}

	/**
	 * 建立憑證.cer及.pfx
	 * */
	public void GenCertAndPFX(String reqCertUid, String pfxPassword,
			String keyAlias, KeyPair keyPair) {
		RAService raService = new RAService();
		raService.setServiceEndpoint(Config.CERT_SERVICE_ENDPOINT);
		X509Certificate mycert = raService.fetchCertByUid(reqCertUid);

		// try {
		// if (mycert != null) {
		// FileOutputStream certOs;
		// String sdpath = Environment.getExternalStorageDirectory()
		// .getPath();
		// String certpath = sdpath + "/mcrypto/test.cer";
		// certOs = new FileOutputStream(certpath);
		// certOs.write(mycert.getEncoded());
		// certOs.flush();
		// certOs.close();
		//
		// String pfxpath = sdpath + "/mcrypto/test.pfx";
		// FileOutputStream fos = new FileOutputStream(pfxpath);
		// KeyStore ks = generatePFX(keyPair, mycert, pfxPassword,
		// keyAlias, fos);
		// }
		// } catch (FileNotFoundException e) {
		// // TODO Auto-generated catch block
		// e.printStackTrace();
		// } catch (CertificateEncodingException e) {
		// // TODO Auto-generated catch block
		// e.printStackTrace();
		// } catch (IOException e) {
		// // TODO Auto-generated catch block
		// e.printStackTrace();
		// } catch (KeyStoreException e) {
		// // TODO Auto-generated catch block
		// e.printStackTrace();
		// } catch (NoSuchAlgorithmException e) {
		// // TODO Auto-generated catch block
		// e.printStackTrace();
		// } catch (CertificateException e) {
		// // TODO Auto-generated catch block
		// e.printStackTrace();
		// }

	}

	// /**
	// * 加解密測試
	// * */
	// public void do_encryptAnddecrypt() {
	// String sdpath = Environment.getExternalStorageDirectory().getPath();
	// String pfxPath = sdpath + "/mcrypto/test.pfx";
	// String cerPath = sdpath +"/mcrypto/test.cer";
	//
	// MCrypto mCrypto = new MCryptoPFXImpl(MainActivity.this, pfxPath,
	// cerPath);
	// String plaintext = "Hello World";
	// try {
	// mCrypto.init("license-smartcatch.properties");
	// mCrypto.login("1qaz2wsx");
	// X509Certificate cert = mCrypto.getX509Certificate();
	// outputString += "cert DN=" + cert.getSubjectDN().getName() + "\n";
	// msgTxt.setText(outputString);
	// byte[] plaixBytes = plaintext.getBytes();
	// byte[] encipher = mCrypto.encrypt(plaixBytes);
	// outputString += "Encipher: " + ToHexString(encipher) + "\n";
	// msgTxt.setText(outputString);
	// byte[] decipher = mCrypto.decrypt(encipher);
	// outputString += "Decipher: " + new String(decipher) + "\n";
	// msgTxt.setText(outputString);
	// } catch (MCryptoException e) {
	// // TODO Auto-generated catch block
	// e.printStackTrace();
	// }
	// }

	public static KeyPair generateKeyPair(int keyLength)
			throws NoSuchAlgorithmException {
		KeyPairGenerator keyGen = KeyPairGenerator.getInstance("RSA");
		keyGen.initialize(keyLength); // default SecureRandom
		return keyGen.genKeyPair();
	}

	public static KeyStore generatePFX(KeyPair keypair, X509Certificate cert,
			String pfxPassword, String keyAlias, OutputStream out)
			throws KeyStoreException, IOException, NoSuchAlgorithmException,
			CertificateException {
		KeyStore ks = KeyStore.getInstance("PKCS12");
		ks.load(null, pfxPassword.toCharArray());

		java.security.cert.Certificate[] chain = new java.security.cert.Certificate[1];
		chain[0] = (java.security.cert.Certificate) cert;

		ks.setKeyEntry(keyAlias, keypair.getPrivate(),
				pfxPassword.toCharArray(), chain);

		ks.store(out, pfxPassword.toCharArray());
		return ks;
	}

	public String ToHexString(byte[] data) {
		String HexString = "";

		for (int i = 0; i < data.length; i++) {
			HexString += " " + String.format("%02X", data[i]);
		}
		return HexString;
	}
}
