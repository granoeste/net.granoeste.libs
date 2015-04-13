/*
 * Copyright (C) 2014 granoeste.net http://granoeste.net/
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package net.granoeste.commons.util;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;

/**
 * 暗号化、復号を行うクラス<BR>
 * 文字列の暗号化、復号を行うユーティリティメソッド群です。<BR>
 * @author k.ohnishi
 * @version 1.0
 * 2009/08/21 k.ohnishi 新規作成<BR>
 */
public class CryptUtils {
	/**
	 * バイト配列を復号した文字列を返します。<BR>
	 * バイト配列を復号した文字列を返します。<BR>
	 * @param key 鍵
	 * @param encrypted 暗号化されたバイト配列
	 * @param algorithm アルゴリズム
	 * @return 復号された文字列
	 * @throws javax.crypto.IllegalBlockSizeException
	 * @throws javax.crypto.BadPaddingException
	 * @throws java.security.InvalidKeyException
	 * @throws java.security.NoSuchAlgorithmException
	 * @throws javax.crypto.NoSuchPaddingException
	 *
	 * @see http://java.sun.com/javase/ja/6/docs/ja/technotes/guides/security/crypto/CryptoSpec.html#AppA
	 * @see http://java.sun.com/javase/ja/6/docs/ja/technotes/guides/security/StandardNames.html
	 */
	public static String decrypt(final String key,
								final byte[] encrypted,
								final String algorithm) throws IllegalBlockSizeException,
																BadPaddingException,
																InvalidKeyException,
																NoSuchAlgorithmException,
																NoSuchPaddingException {
		SecretKeySpec skSpec = new SecretKeySpec(key.getBytes(), algorithm);
		Cipher cipher = Cipher.getInstance(algorithm);
		cipher.init(Cipher.DECRYPT_MODE, skSpec);

		return new String(cipher.doFinal(encrypted));
	}

	/**
	 * 文字列を暗号化します。<BR>
	 * 文字列を暗号化したバイト配列を返します。
	 * 暗号化・復号用アルゴリズムを指定する必要があります。<BR>
	 * @param key 鍵
	 * @param text 対象文字列
	 * @param algorithm 暗号化・復号用アルゴリズム
	 * @return 暗号化したバイト列
	 * @throws java.io.UnsupportedEncodingException
	 * @throws java.security.NoSuchAlgorithmException
	 * @throws javax.crypto.NoSuchPaddingException
	 * @throws java.security.InvalidKeyException
	 * @throws javax.crypto.IllegalBlockSizeException
	 * @throws javax.crypto.BadPaddingException
	 *
	 * @see http://java.sun.com/javase/ja/6/docs/ja/technotes/guides/security/crypto/CryptoSpec.html#AppA
	 * @see http://java.sun.com/javase/ja/6/docs/ja/technotes/guides/security/StandardNames.html
	 */
	public static  byte[] encrypt(final String key,
						final String text,
						final String algorithm)	throws 	UnsupportedEncodingException,
															NoSuchAlgorithmException,
															NoSuchPaddingException,
															InvalidKeyException,
															IllegalBlockSizeException,
															BadPaddingException {
		SecretKeySpec skSpec = new SecretKeySpec(key.getBytes(), algorithm);

		Cipher cipher = Cipher.getInstance(algorithm);

		cipher.init(Cipher.ENCRYPT_MODE, skSpec);

		return cipher.doFinal(text.getBytes());
	}

	/**
	 * 指定文字列の暗号化、複合化を行います。<BR>
	 * メッセージに従い、パラメータを入力してください。
	 * @param args
	 */
	public static void main(String[] args) {

		try {
			BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

			System.out.print("Select enc or dec: ");
			String mode;
			while ((mode = br.readLine()) != null) {
				if (mode.equalsIgnoreCase("enc") || mode.equalsIgnoreCase("dec")) {
					break;

				} else {
					System.out.print("Select enc or dec: ");
				}
			}

			System.out.print("Input key: ");
			String passphrase = br.readLine();

			System.out.print("Input Password: ");
			String password = br.readLine();

			System.out.print("Input Algorithm: ");
			String algorithm = br.readLine();

//			if ("enc".equals(mode)) {
//				System.out.println( encodeBase64( encrypt( passphrase, password, algorithm)));
//			} else if ("dec".equals(mode)) {
//				System.out.println( decrypt( passphrase, decodeBase64(password), algorithm));
//			}

		} catch (Exception e) {
			e.printStackTrace();
		}

	}
}
