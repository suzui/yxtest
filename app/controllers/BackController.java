package controllers;

import java.util.List;

import jsons.Result;
import models.person.Person;
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
		final List<Person> personList = Person.fetchAll();
		render(personList);
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

	public static void yizhen() {
	}

	public static void yimai() {
	}

	public static void yiyan() {
	}

	public static void zhuxue() {
	}

	public static void zhucan() {
	}

	public static void jinglao() {
	}

	public static void peixun() {
	}

	public static void huanbao() {
	}

}
