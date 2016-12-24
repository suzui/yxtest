package jobs;

import models.person.Person;
import play.jobs.Job;
import play.jobs.OnApplicationStart;

@OnApplicationStart
public class StartUp extends Job {

	@Override
	public void doJob() throws Exception {
		Person.initAdmin();
	}
}
