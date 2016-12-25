package controllers;

import play.mvc.Controller;

public class Application extends Controller {

	public static void index() {
		if (request.getBase().endsWith("yixincishan.cn")) {
			BackController.index();
		}
		render();
	}

}
