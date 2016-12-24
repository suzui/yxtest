package vos;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

public class WechatVO {

	public Integer errcode;
	public String errmsg;

	public Map<String, String> toMap() {
		Map<String, String> map = new HashMap<String, String>();
		for (Field field : this.getClass().getDeclaredFields()) {
		}
		return map;
	}

}
