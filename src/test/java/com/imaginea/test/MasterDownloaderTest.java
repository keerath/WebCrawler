package com.imaginea.test;


import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

import com.imaginea.crawler.MasterDownloader;

import akka.actor.ActorSystem;
import akka.actor.Props;
import akka.testkit.TestActorRef;

public class MasterDownloaderTest {

	ActorSystem actorSystem;
	@Before
	public void beforeTest()
	{
		 actorSystem = ActorSystem.create("DownloadingMails");
	}
@Test
public void testFindMonth()
{
	final Props props = Props.create(MasterDownloader.class);
	final TestActorRef<MasterDownloader> ref = TestActorRef.create(actorSystem, props, "Test MasterDownloader");
	final MasterDownloader actor = ref.underlyingActor();
	assertTrue(actor.findMonth("http://mail-archives.apache.org/mod_mbox/maven-users/201412.mbox/thread").equals("12"));
}
}
