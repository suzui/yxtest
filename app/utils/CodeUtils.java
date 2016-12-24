
package utils;

import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import org.apache.commons.codec.digest.DigestUtils;

import com.mchange.lang.ByteUtils;

import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

public class CodeUtils {

	private static String encrypt;
	private static String decrypt;

	public static String md5(String message) {
		encrypt = DigestUtils.md5Hex(message);
		System.err.println("md5 encode:" + encrypt);
		try {
			encrypt = ByteUtils.toHexAscii(MessageDigest.getInstance("md5").digest(message.getBytes())).toLowerCase();
			System.err.println("md5 encode:" + encrypt);
		} catch (NoSuchAlgorithmException e) {
			System.err.println("md5 error:" + e.getMessage());
		}
		return encrypt;
	}

	public static String sha(String message) {
		encrypt = DigestUtils.sha1Hex(message);
		System.err.println("sha encode:" + encrypt);
		try {
			encrypt = ByteUtils.toHexAscii(MessageDigest.getInstance("sha").digest(message.getBytes()));
			System.err.println("sha encode:" + encrypt);
		} catch (NoSuchAlgorithmException e) {
			System.err.println("sha error:" + e.getMessage());
		}
		return encrypt;
	}

	public static String base64(String message) {
		encrypt = new BASE64Encoder().encode(message.getBytes());
		System.err.println("base64 encode:" + encrypt);
		try {
			decrypt = new String(new BASE64Decoder().decodeBuffer(encrypt));
			System.err.println("base64 decode:" + decrypt);
		} catch (IOException e) {
			System.err.println("base64 error:" + e.getMessage());
		}
		return encrypt;
	}

}
