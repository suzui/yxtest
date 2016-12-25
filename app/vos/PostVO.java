package vos;

import java.lang.reflect.Field;

import javax.xml.parsers.DocumentBuilder;

import org.apache.commons.lang.StringUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import play.Logger;
import play.libs.XML;

public class PostVO {

	public String ToUserName;
	public String FromUserName;
	public Long CreateTime;
	public String MsgType;
	public Long MsgId;
	public String Content;
	public String Event;
	public String EventKey;

	public enum MsgType {
		text, image, voice, video, location, link, event
	}

	public enum EventType {
		subscribe, unsubscribe, SCAN, LOCATION, CLICK, VIEW
	}

	public PostVO() {
	}

	public PostVO(String ToUserName, String FromUserName, Long CreateTime, String MsgType, Long MsgId, String Content,
			String Event, String EventKey) {
		this.ToUserName = ToUserName;
		this.FromUserName = FromUserName;
		this.CreateTime = CreateTime;
		this.MsgType = MsgType;
		this.MsgId = MsgId;
		this.Content = Content;
		this.Event = Event;
		this.EventKey = EventKey;
	}

	public static PostVO fromXML(String xml) {
		PostVO postVO = new PostVO();
		try {
			Document document = XML.getDocument(xml);
			Element element = document.getDocumentElement();
			for (Field field : PostVO.class.getDeclaredFields()) {
				NodeList nodeList = element.getElementsByTagName(field.getName());
				if (nodeList != null && nodeList.item(0) != null) {
					field.setAccessible(true);
					String type = field.getType().toString();
					if (type.endsWith("String")) {
						field.set(postVO, nodeList.item(0).getTextContent());
					} else if (type.endsWith("long") || type.endsWith("Long")) {
						if (StringUtils.isNotBlank(nodeList.item(0).getTextContent())) {
							field.set(postVO, Long.parseLong(nodeList.item(0).getTextContent()));
						}

					}
				}
			}
		} catch (Exception e) {
			Logger.error("[fromxmlerror:]%s", e.getMessage());
		}
		Logger.info("[fromxmlsuccess:]%s", postVO.MsgType);
		return postVO;
	}

	public String toXML() {
		DocumentBuilder documentBuilder = XML.newDocumentBuilder();
		Document document = documentBuilder.newDocument();
		Element element = document.createElement("xml");
		StringBuffer sb = new StringBuffer("<xml>");
		try {
			for (Field field : PostVO.class.getDeclaredFields()) {
				String type = field.getType().toString();
				if (field.get(this) != null) {
					if (type.endsWith("String")) {
						sb.append("<" + field.getName() + ">" + "<![CDATA[" + field.get(this) + "]]" + "</"
								+ field.getName() + ">");
					} else {
						sb.append("<" + field.getName() + ">" + field.get(this) + "</" + field.getName() + ">");
					}
				}
			}
			sb.append("</xml>");
		} catch (Exception e) {
			Logger.error("[toxmlerror:]%s", e.getMessage());
		}
		Logger.info("[toxmlsuccess:]%s", sb);
		return sb.toString();
	}
}
