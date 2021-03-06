package utils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.lang.StringUtils;

import com.google.gson.Gson;

import models.post.Item;
import models.post.Item.Type;
import play.Logger;
import play.libs.WS;
import play.libs.WS.HttpResponse;
import vos.AccessTokenVO;
import vos.MenuVO;
import vos.MenuVO.ButtonVO;
import vos.MenuVO.MenuInnerVO;
import vos.UserVO;

public class WechatUtils {
	public static final String BASEURL = "http://yx.iclass.cn";
	public static final String TOKEN = "yechenfan";
	public static final String APPID = "wx06fa62c56153ace7";
	public static final String SECRET = "bdd89414abe28b41b5b1250054ac58a6";

	public static String ACCESSTOKEN = "";
	private static String ERRCODE = "";

	private static String get(String url, Map<String, String> map) {
		StringBuffer sb = new StringBuffer();
		map.put("access_token", ACCESSTOKEN);
		for (Entry<String, String> entry : map.entrySet()) {
			sb.append("key:" + entry.getKey() + " value:" + entry.getValue());
		}
		Logger.info("[wechat params]:%s", sb);
		Logger.info("[wechat url]:%s", url);
		HttpResponse response = WS.url(url).setParameters(map).get();
		Logger.info("[wechat response]:%s", response.getString());
		return response.getString();
	}

	private static String post(String url, Object data) {
		Logger.info("[wechat data]:%s", data);
		Logger.info("[wechat url]:%s", url);
		HttpResponse response = WS.url(url + "?access_token=" + ACCESSTOKEN).body(data).post();
		Logger.info("[wechat response]:%s", response.getString());
		return response.getString();
	}

	public static boolean check(String signature, String timestamp, String nonce, String echostr) {
		if (StringUtils.isBlank(signature) || StringUtils.isBlank(timestamp) || StringUtils.isBlank(nonce)
				|| StringUtils.isBlank(echostr)) {
			return false;
		}
		List<String> params = Arrays.asList(TOKEN, timestamp, nonce);
		Collections.sort(params);
		String encrypt = CodeUtils.sha(StringUtils.join(params, "")).toLowerCase();
		return StringUtils.equals(encrypt, signature);
	}

	public static void token() {
		Map<String, String> map = new HashMap<String, String>();
		map.put("grant_type", "client_credential");
		map.put("appid", APPID);
		map.put("secret", SECRET);
		String reponse = get("https://api.weixin.qq.com/cgi-bin/token", map);
		AccessTokenVO tokenVO = new Gson().fromJson(reponse, AccessTokenVO.class);
		ACCESSTOKEN = tokenVO.access_token;
	}

	public static MenuVO getMenus() {
		String reponse = get("https://api.weixin.qq.com/cgi-bin/menu/get", new HashMap<>());
		return new Gson().fromJson(reponse, MenuVO.class);
	}

	public static void createMenu() {
		List<ButtonVO> buttonVOs = new ArrayList<ButtonVO>();
		List<ButtonVO> buttonVOs1 = new ArrayList<ButtonVO>();
		List<ButtonVO> buttonVOs2 = new ArrayList<ButtonVO>();
		List<ButtonVO> buttonVOs3 = new ArrayList<ButtonVO>();
		for (Type type : Type.getByValue(100)) {
			buttonVOs1.add(new ButtonVO("view", type.toString(), null, BASEURL + "/wechat/items/" + type.value, null));
		}
		buttonVOs.add(new ButtonVO("click", Item.navs[0], null, null, buttonVOs1));
		for (Type type : Type.getByValue(200)) {
			buttonVOs2.add(new ButtonVO("view", type.toString(), null, BASEURL + "/wechat/items/" + type.value, null));
		}
		buttonVOs.add(new ButtonVO("click", Item.navs[1], null, null, buttonVOs2));
		for (Type type : Type.getByValue(300)) {
			buttonVOs3.add(new ButtonVO("view", type.toString(), null, BASEURL + "/wechat/items/" + type.value, null));
		}
		buttonVOs.add(new ButtonVO("click", Item.navs[2], null, null, buttonVOs3));
		MenuInnerVO menuInfoVO = new MenuInnerVO(buttonVOs);
		String menuinfo = new Gson().toJson(menuInfoVO);
		post("https://api.weixin.qq.com/cgi-bin/menu/create", menuinfo);
	}

	public static UserVO getUserInfo(String openid) {
		Map<String, String> map = new HashMap<String, String>();
		map.put("openid", openid);
		map.put("lang", "zh_CN");
		String reponse = get("https://api.weixin.qq.com/cgi-bin/user/info", map);
		UserVO userVO = new Gson().fromJson(reponse, UserVO.class);
		return userVO;
	}

}
