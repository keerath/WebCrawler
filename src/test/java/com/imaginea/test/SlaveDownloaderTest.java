package com.imaginea.test;

import static org.junit.Assert.assertTrue;

import java.io.IOException;

import org.junit.Before;
import org.junit.Test;

import akka.actor.ActorSystem;
import akka.actor.Props;
import akka.testkit.TestActorRef;

import com.imaginea.crawler.MailInfo;
import com.imaginea.crawler.SlaveDownloader;

public class SlaveDownloaderTest {

	ActorSystem actorSystem;
	MailInfo mailInfo;
	private Props props;
	@Before
	public void beforeTest()
	{
		 actorSystem = ActorSystem.create("DownloadingMails");
		 props = Props.create(SlaveDownloader.class);
		 mailInfo = new MailInfo("","","","");
	}

	@Test
	public void testLinksPointingToMails() throws IOException {
		
		final TestActorRef<SlaveDownloader> ref = TestActorRef.create(
				actorSystem, props, "Test SlaveDownloader - findLinksPointingToMails");
		final SlaveDownloader actor = ref.underlyingActor();
		assertTrue(actor.findLinksPointingToMails("http://mail-archives.apache.org/mod_mbox/maven-users/201506.mbox/browser").size()== 24);
	}
	/*
	@Test
	public void writeMailInfo()
	{
		final TestActorRef<SlaveDownloader> ref = TestActorRef.create(actorSystem, props,"Test SlaveDownloader - writeMailInfo");
		final SlaveDownloader actor = ref.underlyingActor();
		assertTrue(actor.writeMailInfo(mailInfo,"12") == -1);
	} */
}
