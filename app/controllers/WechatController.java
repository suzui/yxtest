package controllers;

import java.util.List;
import java.util.Map.Entry;

import org.apache.commons.lang.StringUtils;
import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;

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
			if ((rPostVO.MsgType.equals(MsgType.event.toString())
					&& rPostVO.Event.equals(EventType.subscribe.toString()))
					|| rPostVO.MsgType.equals(MsgType.text.toString())) {
				PostVO sPostVO = new PostVO(rPostVO.FromUserName, rPostVO.ToUserName, System.currentTimeMillis() / 1000,
						MsgType.text.toString(), null,
						"感恩关注一心慈善！也许我们的援助微不足道，但，我们的帮助可以温暖人心。我们来自偶然，我们是浩瀚宇宙中的一粒尘埃，但我们有一颗共同的心，我们有一个共同的名字“一心”！", null,
						null);
				Document doc = DocumentHelper.createDocument();
				Element root = doc.addElement("xml");
				root.addElement("ToUserName").addCDATA(sPostVO.ToUserName);
				root.addElement("FromUserName").addCDATA(sPostVO.FromUserName);
				root.addElement("CreateTime").addText(sPostVO.CreateTime + "");
				root.addElement("MsgType").addCDATA(sPostVO.MsgType);
				root.addElement("Content").addCDATA(sPostVO.Content);
				response.setContentTypeIfNotSet("application/xml;charset=UTF-8");
				Logger.info("[docxml:]%s", doc.asXML());
				response.print(doc.asXML());
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
		if (StringUtils.isNotBlank(item.link)) {
			redirect(item.link);
		}
		render(item);
	}

}