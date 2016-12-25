package models.person;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;

import models.BaseModel;
import play.data.validation.MaxSize;
import play.data.validation.MinSize;
import play.data.validation.Required;
import utils.CodeUtils;

@Entity
public class Person extends BaseModel {

	@Required
	@MinSize(2)
	@MaxSize(10)
	public String username;

	public String password;

	@Column(length = 100)
	public String name;

	public String avatar;

	@Enumerated(EnumType.STRING)
	public Sex sex;

	@Enumerated(EnumType.STRING)
	public Role role;

	public String cellphone;

	public String address;

	public String email;

	public String openid;

	public enum Role {
		管理员(100), 普通用户(101);
		private int value;

		private Role(int value) {
			this.value = value;
		}

		public int value() {
			return this.value;
		}

		public static Role convert(int value) {
			if (value == 100) {
				return Role.管理员;
			}
			return Role.普通用户;
		}
	}

	public enum Sex {
		女(100), 男(101), 无(102);
		private int value;

		private Sex(int value) {
			this.value = value;
		}

		public int value() {
			return this.value;
		}

		public static Sex convert(int value) {
			if (value == 100) {
				return Sex.女;
			} else if (value == 101) {
				return Sex.男;
			}
			return Sex.无;
		}
	}

	public static Person add(String username, String password, String name, int sex, String cellphone, String openid) {
		Person person = findByOpenid(openid);
		if (person == null) {
			person = new Person();
		}
		person.username = username;
		person.name = name;
		person.password = CodeUtils.md5(password);
		person.sex = Sex.convert(sex);
		person.role = Role.普通用户;
		person.openid = openid;
		return person.save();
	}

	public static void initAdmin() {
		if (fetchAdmin().size() == 0) {
			addAdmin();
		}
	}

	public static Person addAdmin() {
		Person person = new Person();
		person.username = "admin";
		person.password = CodeUtils.md5("111111");
		person.role = Role.管理员;
		return person.save();
	}

	public void edit(String name) {
		this.name = name;
		this.save();
	}

	public static Person validate(String username, String password) {
		Person person = findByUsername(username);
		if (person == null) {
			return null;
		}
		if (!CodeUtils.md5(password).equals(person.password)) {
			return null;
		}
		return person;
	}

	public void del() {
		this.logicDelete();
	}

	public static Person findOneById(long id) {
		return Person.find("select p from Person p where p.isDeleted=false and p.id=?", id).first();
	}

	public static Person findByUsername(String username) {
		return Person.find("select p from Person p where p.isDeleted=false and p.username=?", username).first();
	}

	public static Person findByOpenid(String openid) {
		return Person.find("select p from Person p where p.isDeleted=false and p.openid=?", openid).first();
	}

	public static Person findByName(String name) {
		return Person.find("select p from Person p where p.isDeleted=false and p.name=?", name).first();
	}

	public static List<Person> fetchAll() {
		return Person.find("select p from Person p where p.isDeleted=false and p.role=?", Role.普通用户).fetch();
	}

	public static List<Person> fetchAdmin() {
		return Person.find("select p from Person p where p.isDeleted=false and p.role=?", Role.管理员).fetch();
	}

}
