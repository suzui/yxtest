package models.post;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Lob;

import models.BaseModel;

@Entity
public class Item extends BaseModel {

	public String title;
	public String author;
	@Column(length = 1000)
	public String cover;
	public String summary;
	public String link;
	@Lob
	public String content;

	@Enumerated(EnumType.STRING)
	public Type type;

	public Integer view;

	public static final String[] navs = { "公益活动", "公益培训", "关爱义工" };

	public enum Type {
		义诊(101), 义卖(102), 义演(103), 助学(104), 助残(105), 敬老(106), 环保(107), 怀老(108), 教育培训(201), 义工培训(202), 环保培训(203), 文艺培训(
				204), 舞蹈培训(205), 茶道培训(206), 医疗培训(207), 慰问义工(301), 生日宴(302), 义工救助(303);

		public int value;

		private Type(int value) {
			this.value = value;
		}

		public static Type convert(int value) {
			for (Type type : Type.values()) {
				if (type.value == value) {
					return type;
				}
			}
			return null;
		}

		public static List<Type> getByValue(int value) {
			List<Type> types = new ArrayList<>();
			for (Type type : Type.values()) {
				if (type.value > value && type.value < value + 100) {
					types.add(type);
				}
			}
			return types;
		}
	}

	public static Item add(Type type, String title, String author, String cover, String summary, String link,
			String content) {
		Item item = new Item();
		item.type = type;
		item.title = title;
		item.author = author;
		item.cover = cover;
		item.summary = summary;
		item.link = link;
		item.content = content;
		return item.save();
	}

	public void del() {
		this.logicDelete();
	}

	public static Item findOneById(long id) {
		return Item.find("select i from Item i where i.isDeleted=false and i.id=?", id).first();
	}

	public static List<Item> fetchByType(Type type) {
		return Item.find("select i from Item i where i.isDeleted=false and i.type=? order by id desc", type).fetch();
	}

	public static List<Item> fetchAll() {
		return Item.find("select i from Item i where i.isDeleted=false").fetch();
	}

	public void viewed() {
		if (this.view == null) {
			this.view = 0;
		}
		this.view++;
		this.save();
	}

	public int view() {
		if (this.view == null) {
			return 0;
		}
		return this.view;
	}

}
