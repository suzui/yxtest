package controllers;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map.Entry;

import org.apache.commons.lang.StringUtils;

import play.Logger;
import play.mvc.Before;
import play.mvc.Controller;
import play.mvc.Http.Header;
import play.mvc.Http.Request;
import utils.CodeUtils;
import utils.WechatUtils;

public class WechatController extends Controller {
	@Before
	static void params() {
		Logger.info("[wechatparams start] time:%d", System.currentTimeMillis());
		final Request request = Request.current();
		Logger.info("[wechatparams action]:%s", request.action);
		StringBuffer stringBuffer = new StringBuffer();
		for (Entry<String, String> entry : request.params.allSimple().entrySet()) {
			stringBuffer.append("key:" + entry.getKey() + " value:" + entry.getValue());
		}
		Logger.info("[wechatparams]:%s", stringBuffer.toString());

		for (Entry<String, Header> entry : request.headers.entrySet()) {
			Logger.info("[wechatparams header]:%s,%s", entry.getValue().name, entry.getValue().value());
		}
		Logger.info("[wechatparams end] time:%d", System.currentTimeMillis());
	}

	public static void checkSignature(String signature, String timestamp, String nonce, String echostr) {
		List<String> params = Arrays.asList(WechatUtils.TOKEN, timestamp, nonce);
		Collections.sort(params);
		String encrypt = CodeUtils.sha(StringUtils.join(params, "")).toLowerCase();
		if (StringUtils.equals(encrypt, signature)) {
			response.print(echostr);
		}
	}

	public static void token() {
		WechatUtils.token();
	}

	public static void authorize() {

	}

}