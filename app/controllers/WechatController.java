package controllers;

import java.util.List;
import java.util.Map.Entry;

import org.apache.commons.lang.StringUtils;

import models.person.Person;
import models.post.Item;
import models.post.Item.Type;
import play.Logger;
import play.mvc.Before;
import play.mvc.Controller;
import play.mvc.Http.Header;
import play.mvc.Http.Request;
import utils.WechatUtils;
import vos.PostVO;
import vos.PostVO.EventType;
import vos.PostVO.MsgType;
import vos.UserVO;

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

	public static void checkSignature(String signature, String timestamp, String nonce, String echostr, String openid,
			String body) {
		if (WechatUtils.check(signature, timestamp, nonce, echostr)) {
			response.print(echostr);
			renderJSON(true);
		}

		if (StringUtils.isNotBlank(openid)) {
			UserVO userVO = WechatUtils.getUserInfo(openid);
			if (userVO.subscribe == 1) {
				Person.add(userVO.nickname, userVO.headimgurl, userVO.sex + 100, openid);
			}
			PostVO rPostVO = PostVO.fromXML(body);
			if (rPostVO.MsgType.equals(MsgType.event.toString())
					&& rPostVO.Event.equals(EventType.subscribe.toString())) {
				PostVO sPostVO = new PostVO(rPostVO.FromUserName, rPostVO.ToUserName, System.currentTimeMillis(),
						MsgType.text.toString(), null, "hello", null, null);
				response.print(sPostVO.toXML());
			}
		}
	}

	public static void token() {
		WechatUtils.token();
	}

	public static void authorize() {

	}

	public static void items(int typeValue) {
		Type type = Type.convert(typeValue);
		List<Item> items = Item.fetchByType(type);
		render(items, type);
	}

	public static void item(long id) {
		Item item = Item.findOneById(id);
		item.viewed();
		render(item);
	}

}