
package models;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.stream.Collectors;

import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import play.test.Fixtures;
import play.test.UnitTest;

@Ignore
@TestDataSource
public class BaseModelTest extends UnitTest {

	private static final String sql1 = "username=?";
	private static final String sql2 = "select username from Member";
	private static final String sql3 = "select username from Member where email=?";
	private static final String sql4 = "select username from Member m where m.email=?";
	private static final String sql5 = "select count(gm.groupMember) from GenericGroup_Member gm where gm.groupMember.class = Student and gm.genericGroup in (select innergm.genericGroup from GenericGroup_Member as innergm where innergm.groupMember=? group by innergm.genericGroup) group by gm.groupMember";
	private static final String sql6 = "from a where b = 1 and c not in (select * from d d where d.e = 1 and d.f = 0)";

	private static final String exp1 = "isDeleted=false and username=?";
	private static final String exp2 = "select username from Member where isDeleted=false";
	private static final String exp3 = "select username from Member where isDeleted=false and email=?";
	private static final String exp4 = "select username from Member m where m.isDeleted=false and m.email=?";
	private static final String exp5 = "select count(gm.groupMember) from GenericGroup_Member gm where gm.isDeleted=false and gm.groupMember.class = Student and gm.genericGroup in (select innergm.genericGroup from GenericGroup_Member as innergm where innergm.isDeleted=false and innergm.groupMember=? group by innergm.genericGroup) group by gm.groupMember";
	private static final String exp6 = "from a where isDeleted=false and b = 1 and c not in (select * from d d where d.isDeleted=false and d.e = 1 and d.f = 0)";

	private static final Map<String, String> data = new HashMap<String, String>();

	static {
		data.put(sql1, exp1);
		data.put(sql2, exp2);
		data.put(sql3, exp3);
		data.put(sql4, exp4);
		data.put(sql5, exp5);
		data.put(sql6, exp6);
	}

	@Before
	public void setUp() {
		Class clazz = this.getClass();
		TestDataSource testDataSource = (TestDataSource) clazz.getAnnotation(TestDataSource.class);
		if (testDataSource == null) {
			return;
		}
		if (testDataSource.source().length == 0) {
			Fixtures.loadModels("datas/" + clazz.getSimpleName().replace("Test", "") + ".yml");
		} else {
			List<String> sources = Arrays.asList(testDataSource.source());
			Fixtures.loadModels(sources.stream().map(s -> s = "datas/" + s + ".yml").collect(Collectors.toList()));
		}
	}

	@Test
	public void testGetDefaultContitionSql() {
		for (Entry<String, String> entry : data.entrySet()) {
			assertEquals(entry.getValue(), BaseModel.getDefaultContitionSql(entry.getKey()));
		}
	}

	@After
	public void setDown() {
		Fixtures.deleteDatabase();
	}
}
