package vos;

import java.util.List;

public class MenuVO {

	public MenuInnerVO menu;

	public static class MenuInnerVO {
		public List<ButtonVO> button;

		public MenuInnerVO(List<ButtonVO> button) {
			this.button = button;
		}
	}

	public static class ButtonVO {
		public String type;
		public String name;
		public String key;
		public String url;
		public List<ButtonVO> sub_button;

		public ButtonVO(String type, String name, String key, String url, List<ButtonVO> sub_button) {
			this.type = type;
			this.name = name;
			this.key = key;
			this.url = url;
			this.sub_button = sub_button;
		}
	}

}
