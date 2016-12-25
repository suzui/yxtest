package utils;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.google.gson.Gson;

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
		buttonVOs1.add(new ButtonVO("view", "义诊", null, BASEURL + "/wechat/yizheng", null));
		buttonVOs1.add(new ButtonVO("view", "义卖", null, BASEURL + "/wechat/yimai", null));
		buttonVOs1.add(new ButtonVO("view", "义演", null, BASEURL + "/wechat/yiyan", null));
		buttonVOs.add(new ButtonVO("click", "义工", null, null, buttonVOs1));
		List<ButtonVO> buttonVOs2 = new ArrayList<ButtonVO>();
		buttonVOs2.add(new ButtonVO("view", "助学", null, BASEURL + "/wechat/zhuxue", null));
		buttonVOs2.add(new ButtonVO("view", "助残", null, BASEURL + "/wechat/zhucan", null));
		buttonVOs2.add(new ButtonVO("view", "敬老", null, BASEURL + "/wechat/jinglao", null));
		buttonVOs.add(new ButtonVO("click", "帮助", null, null, buttonVOs2));
		List<ButtonVO> buttonVOs3 = new ArrayList<ButtonVO>();
		buttonVOs3.add(new ButtonVO("view", "培训", null, BASEURL + "/wechat/peixun", null));
		buttonVOs3.add(new ButtonVO("view", "环保", null, BASEURL + "/wechat/huanbao", null));
		buttonVOs3.add(new ButtonVO("view", "加入我们", null, BASEURL + "/back/index", null));
		String url = "";
		try {
			url = "https://open.weixin.qq.com/connect/oauth2/authorize "
					+ URLEncoder.encode("?appid=" + APPID + "&redirect_uri=" + BASEURL + "/wechat/authorize"
							+ "&response_type=code&scope=snsapi_base&state=100#wechat_redirect", "utf8");
		} catch (UnsupportedEncodingException e) {
			Logger.info("urlencodeerror", e.getMessage());
		}
		buttonVOs3.add(new ButtonVO("view", "授权", null, url, null));
		buttonVOs.add(new ButtonVO("click", "公益", null, null, buttonVOs3));
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
