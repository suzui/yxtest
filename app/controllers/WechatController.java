package controllers;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.apache.commons.lang.StringUtils;

import play.Logger;
import utils.CodeUtils;

public class WechatController extends BaseController {

	private static final String TOKEN = "yechenfan";

	public static void checkSignature(String signature, String timestamp, String nonce, String echostr) {
		List<String> params = Arrays.asList(TOKEN, timestamp, nonce);
		Collections.sort(params);
		Logger.info("message:%s", StringUtils.join(params, ""));
		String encrypt = CodeUtils.sha(StringUtils.join(params, "")).toLowerCase();
		Logger.info("encrypt:%s", encrypt);
		Logger.info(StringUtils.equals(encrypt, signature) + "");
		if (StringUtils.equals(encrypt, signature)) {
			response.print(echostr);
			renderJSON(true);
		}
		renderJSON(false);
	}

}