package com.imaginea.crawler;

public class DownloadWork {

  private final String url;
  private final String month;
  
  
  /**
   * Constructs the Work to be given to the slaves.
   * @param url URL of page containing mails
   * @param month Month in which these mails are mailed
   */
  public DownloadWork(String url, String month) {
    super();
    this.url = url;
    this.month = month;
  }

  public String getUrl() {
    return url;
  }

  public String getMonth() {
    return month;
  }

}
