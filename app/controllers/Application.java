package controllers;

public class Application extends BaseController {

	public static void index() {
		if (request.getBase().endsWith("yixincishan.cn")) {
			BackController.index();
		}
		render();
	}

}
