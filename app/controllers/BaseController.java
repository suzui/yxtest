package controllers;

import java.util.Map;
import java.util.Map.Entry;

import models.person.Person;
import play.Logger;
import play.mvc.Before;
import play.mvc.Controller;
import play.mvc.Http.Cookie;
import play.mvc.Http.Request;
import play.mvc.Http.Response;
import play.mvc.Scope.Session;
import play.mvc.Util;

public class BaseController extends Controller {
	public static final int DEFAULT_LIST_SIZE = 20;
	public static final String CURRENT_PERSON_ID = "currentPersonId";
	public static final String KEEP_PERSON_ID = "keepPersonId";

	@Before
	static void filter() {
		Logger.info("[filter start] time:%d", System.currentTimeMillis());
		final Person person = getCurrPerson();
		if (person == null) {
			Logger.info("[filter person]:null");
			// forbidden();
		}
		// Logger.info("[filter person]:%s", person.username);
		final Request request = Request.current();
		Logger.info("[filter action]:%s", request.action);
		StringBuffer stringBuffer = new StringBuffer();
		for (Entry<String, String> entry : request.params.allSimple().entrySet()) {
			stringBuffer.append("key:" + entry.getKey() + " value:" + entry.getValue());
		}
		Logger.info("[filter]:%s", stringBuffer.toString());
		Logger.info("[filter end] time:%d", System.currentTimeMillis());
	}

	@Util
	protected static void setPersonIdToSession(Long personId) {
		Session.current().put(CURRENT_PERSON_ID, personId + "");
	}

	@Util
	protected static void setPersonIdToCookie(Long personId) {
		Response.current().setCookie(KEEP_PERSON_ID, personId + "", "365d");
	}

	@Util
	protected static Long getPersonIdFromSession() {
		Session session = Session.current();
		return session.contains(CURRENT_PERSON_ID) ? Long.parseLong(session.get(CURRENT_PERSON_ID)) : null;
	}

	@Util
	protected static Long getPersonIdFromCookie() {
		Map<String, Cookie> cookies = Request.current().cookies;
		Cookie cookie = cookies.get(KEEP_PERSON_ID);
		if (null != cookie) {
			return Long.parseLong(cookie.value);
		}
		return null;
	}

	@Util
	protected static Person getCurrPerson() {
		Long personId = getPersonIdFromSession();
		if (personId == null) {
			return null;
		}
		return Person.findOneById(personId);
	}

}