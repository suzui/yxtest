package controllers;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.gson.Gson;
import com.ning.http.client.AsyncHttpClient;
import com.ning.http.client.Response;
import com.ning.http.client.multipart.FilePart;
import com.ning.http.client.multipart.StringPart;

import jsons.Result;
import models.person.Person;
import models.post.Item;
import models.post.Item.Type;
import utils.WechatUtils;
import vos.MenuVO;

public class BackController extends BaseController {

	public static void demo() {
		redirectToStatic("public/sbadmin/pages/index.html");
	}

	public static void index() {
		final Person person = getCurrPerson();
		if (person != null) {
			home();
		}
		redirectToStatic("public/sbadmin/pages/login.html");
	}

	public static void login(String username, String password) {
		Person person = Person.validate(username, password);
		if (person == null) {
			renderJSON(new Result(false));
		}
		setPersonIdToSession(person.id);
		renderJSON(new Result(true));
	}

	public static void logout() {
		removePersonIdToSession();
		removePersonIdToCookie();
		index();
	}

	public static void home() {
		final Person person = getCurrPerson();
		render(person);
	}

	public static void persons() {
		final List<Person> persons = Person.fetchAll();
		render(persons);
	}

	public static void editPerson(String username, String name, String password, int sex, String cellphone) {
		renderJSON(true);
	}

	public static void menus() {
		MenuVO menuVO = WechatUtils.getMenus();
		render(menuVO);
	}

	public static void createMenu() {
		WechatUtils.createMenu();
		renderJSON(new Result(true, "创建菜单成功"));
	}

	public static void items(int typeValue) {
		Type type = Type.convert(typeValue);
		List<Item> items = Item.fetchByType(type);
		render(items, type);
	}

	public static void itemAdd(int typeValue) {
		Type type = Type.convert(typeValue);
		render(type);
	}

	public static void addItem(int type, String title, String author, String cover, String summary, String link,
			String content) {
		Item.add(Type.convert(type), title, author, cover, summary, link, content);
		renderJSON(new Result(true));
	}

	public static void delItem(Long id) {
		Item item = Item.findOneById(id);
		item.del();
		items(item.type.value);
	}

	public static void uploadByUeditor(String fileName, String pictitle, File upfile) throws Exception {
		response.contentType = "text/html; charset=utf-8";
		Map<String, String> json = new HashMap<String, String>();
		if (upfile != null) {
			final AsyncHttpClient client = new AsyncHttpClient();
			final Response response = client.preparePost("http://oss.iclass.cn/formImage")
					.addBodyPart(new FilePart("qqfile", upfile)).addBodyPart(new StringPart("bucketName", "smallfiles"))
					.addBodyPart(new StringPart("source", "WEB")).execute().get();
			final String responseBody = response.getResponseBody("utf8");
			client.close();
			Result result = new Gson().fromJson(responseBody, Result.class);
			json.put("url", result.html);
			json.put("original", fileName);
			json.put("title", pictitle);
			json.put("state", "SUCCESS");
			renderJSON(json);
		} else {
			json.put("state", "ERROR");
			renderJSON(json);
		}
	}

}
