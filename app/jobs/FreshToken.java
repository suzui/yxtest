package jobs;

import models.person.Person;
import play.jobs.Every;
import play.jobs.Job;
import play.jobs.OnApplicationStart;
import utils.WechatUtils;

@OnApplicationStart
@Every("1h")
public class FreshToken extends Job {

	@Override
	public void doJob() throws Exception {
		WechatUtils.token();
	}
}
