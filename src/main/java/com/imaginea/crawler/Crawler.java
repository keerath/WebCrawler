package com.imaginea.crawler;

import java.io.File;
import java.util.Arrays;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.regex.Pattern;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import edu.uci.ics.crawler4j.crawler.Page;
import edu.uci.ics.crawler4j.crawler.WebCrawler;
import edu.uci.ics.crawler4j.url.WebURL;

public class Crawler {
    private static List<String> urlList = new LinkedList<>();

    public static List<String> getUrlList() {
        return urlList;
    }

    public void addURLS() {
        File file = new File("/home/keerathj/Desktop/mail-archives.apache.org/mod_mbox/maven-users");
        for (File f : file.listFiles()) {
            urlList.add(f.getAbsolutePath());
        }
    }
}
