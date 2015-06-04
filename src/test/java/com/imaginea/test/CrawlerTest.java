package com.imaginea.test;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;

import com.imaginea.crawler.Crawler;

import edu.uci.ics.crawler4j.crawler.Page;
import edu.uci.ics.crawler4j.url.WebURL;

public class CrawlerTest {

  Page page;
  WebURL webUrl2;

  @Before
  public void beforeTest() {
    WebURL webUrl = new WebURL();
    webUrl
        .setURL("http://mail-archives.apache.org/mod_mbox/maven-users/201412.mbox/thread");
    page = new Page(webUrl);
    webUrl2 = new WebURL();
    webUrl2
        .setURL("http://mail-archives.apache.org/mod_mbox/maven-users/201412.mbox/thread?1");
  }

  @Test
  public void testShouldVisit() {
    Crawler crawler = new Crawler();
    assertEquals(crawler.shouldVisit(page, webUrl2), true);
  }

}
