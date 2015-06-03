package com.imaginea.training;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.gargoylesoftware.htmlunit.FailingHttpStatusCodeException;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlAnchor;
import com.gargoylesoftware.htmlunit.html.HtmlPage;

import akka.actor.ActorRef;
import akka.actor.Props;
import akka.actor.UntypedActor;
import akka.routing.ActorRefRoutee;
import akka.routing.RoundRobinPool;
import akka.routing.RoundRobinRouter;
import akka.routing.RoundRobinRoutingLogic;
import akka.routing.Routee;
import akka.routing.Router;

public class MasterDownloader extends UntypedActor {

	
	
	private final String year = "2014";
	WebClient webClient = new WebClient();

	public MasterDownloader() {

	}
	Router router;
	  {
	    List<Routee> routees = new ArrayList<Routee>();
	    for (int i = 0; i < Crawler.getUrlList().size() -1; i++) {
	      ActorRef r = getContext().actorOf(Props.create(SlaveDownloader.class));
	      getContext().watch(r);
	      routees.add(new ActorRefRoutee(r));
	    }
	    router = new Router(new RoundRobinRoutingLogic(), routees);
	  }

	
	static class Download {

	}

	@Override
	public void onReceive(Object message) throws Exception {

		if (message instanceof Download) {
			String month;
			String folderForMailDownloads = "~/Desktop/mails/";
			List<String> urlList = Crawler.getUrlList();

			for (int i = 0; i < urlList.size(); i++) {
				
				String url = urlList.get(i);
				if(url.endsWith("?0"))
					continue;
				else
				{
				month = url.substring(url.lastIndexOf(".") -2, url.lastIndexOf("."));
				if (!url.contains("?")) {
					
					new File(folderForMailDownloads + month).mkdir();
				}
					
						router.route(new DownloadWork(url, month),
								getSelf());
				}
			}
			}
		}	
}
