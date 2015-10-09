package com.imaginea.crawler;

import java.io.File;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import akka.actor.ActorRef;
import akka.actor.Props;
import akka.actor.UntypedActor;
import akka.routing.ActorRefRoutee;
import akka.routing.RoundRobinRoutingLogic;
import akka.routing.Routee;
import akka.routing.Router;

import com.gargoylesoftware.htmlunit.WebClient;

public class MasterDownloader extends UntypedActor {

    private final Logger logger = LoggerFactory.getLogger(MasterDownloader.class);
    private final long start = System.currentTimeMillis();
    private final String mailDownloadFolder = Constants.mailDownloadFolder;
    private int numOfResults = 0;
    private DecimalFormat formatter = new DecimalFormat("##.00");

    public MasterDownloader() {

    }

    Router downloadRouter;

    {
        List<Routee> routees = new ArrayList<Routee>();
        for (int i = 0; i < Crawler.getUrlList().size(); i++) {
            ActorRef ref = getContext().actorOf(Props.create(SlaveDownloader.class));
            getContext().watch(ref);
            routees.add(new ActorRefRoutee(ref));
        }
        downloadRouter = new Router(new RoundRobinRoutingLogic(), routees);
    }

    static class Download {
        private final List<String> urlList;

        public List<String> getUrlList() {
            return urlList;
        }

        public Download(List<String> urlList) {
            this.urlList = urlList;
        }
    }

    @Override
    public void onReceive(Object message) throws Exception {

        if (message instanceof Download) {
            String month;
            Download totalDownloadWork = (Download) message;
            List<String> fileList = totalDownloadWork.getUrlList();

            for (int i = 0; i < fileList.size(); i++) {
                String filePath = fileList.get(i);
                month = findMonth(filePath);
                new File(mailDownloadFolder + month).mkdir();
                downloadRouter.route(new DownloadWork(filePath, month), getSelf());
            }
        } else if (message instanceof SlaveDownloader.Terminate) {
            numOfResults += 1;
            if (numOfResults == Crawler.getUrlList().size()) {
                long duration = System.currentTimeMillis() - start;

                logger.info("Total Time To Download All Mails "
                        + formatter.format((duration / 1000.0) / 60.0) + " minutes");
                getContext().stop(getSelf());
                getContext().system().shutdown();
            }
        } else {
            logger.info("Invalid Message!");
            unhandled(message);
        }
    }

    /**
     * Extracts the month from a url.
     *
     * @param url URL of the page containing mails
     * @return Month in which the mail was originated
     */
    public String findMonth(String url) {
        return url.substring(url.lastIndexOf(".") - 2, url.lastIndexOf("."));
    }
}
