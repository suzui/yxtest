package controllers;

import java.util.List;

import jsons.Result;
import models.person.Person;

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

	public static void home() {
		final Person person = getCurrPerson();
		render(person);
	}

	public static void managePerson() {
		final List<Person> personList = Person.fetchAll();
		render(personList);
	}

	public static void addPerson(String username, String name, String password, int sex, String cellphone) {
		Person.add(username, password, name, sex, cellphone);
		renderJSON(true);
	}

}
