package com.imaginea.crawler;

import java.io.File;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.gargoylesoftware.htmlunit.WebClient;

import akka.actor.ActorRef;
import akka.actor.Props;
import akka.actor.UntypedActor;
import akka.routing.ActorRefRoutee;
import akka.routing.RoundRobinRoutingLogic;
import akka.routing.Routee;
import akka.routing.Router;

public class MasterDownloader extends UntypedActor {

	private final Logger logger = LoggerFactory
			.getLogger(MasterDownloader.class);
	WebClient webClient = new WebClient();
	private final long start = System.currentTimeMillis();

	private final String mailDownloadFolder = Constants.mailDownloadFolder;
	private int numOfResults = 0;
	private DecimalFormat f = new DecimalFormat("##.00");
	public MasterDownloader() {

	}

	Router router;
	{
		List<Routee> routees = new ArrayList<Routee>();
		for (int i = 0; i < Crawler.getUrlList().size(); i++) {
			ActorRef r = getContext().actorOf(
					Props.create(SlaveDownloader.class));
			getContext().watch(r);
			routees.add(new ActorRefRoutee(r));
		}
		router = new Router(new RoundRobinRoutingLogic(), routees);
	}

	 static class Download {
		 	private final List<String> urlList;
		 	public List<String> getUrlList()
		 	{
		 		return urlList;
		 	}
		 	public Download(List<String>urlList)
		 	{
		 		this.urlList = urlList;
		 	}
	}

	@Override
	public void onReceive(Object message) throws Exception {

		if (message instanceof Download) {
			String month;
			Download totalDownloadWork = (Download)message;
			List<String> urlList = totalDownloadWork.getUrlList();

			for (int i = 0; i < urlList.size(); i++) {

				String url = urlList.get(i);
				if (url.endsWith("?0"))
					continue;
				else {
					month = findMonth(url);
					if (!url.contains("?")) {

						new File(mailDownloadFolder + month).mkdir();
					}
					router.route(new DownloadWork(url, month), getSelf());
				}
			}
		} else if (message instanceof SlaveDownloader.Terminate) {
			numOfResults += 1;
			if (numOfResults == Crawler.getUrlList().size()) {
				long duration = System.currentTimeMillis() - start;
				
				logger.info("Total Time To Download All Mails "
						+ f.format((duration / 1000.0) / 60.0)+" minutes");
				getContext().stop(getSelf());
				getContext().system().shutdown();
			}
		} else {
			logger.info("Invalid Message!");
			unhandled(message);
		}
	}
	public String findMonth(String url)
	{
		return url.substring(url.lastIndexOf(".") - 2,
				url.lastIndexOf("."));
	}
}
