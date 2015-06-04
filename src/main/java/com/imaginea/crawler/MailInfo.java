package com.imaginea.crawler;

/**
 * Mail Information Pojo Class.
 * 
 * @author keerathjaggi
 *
 */
public class MailInfo {

  private String from;
  private String subject;
  private String date;
  private String content;

  public String getFrom() {
    return from;
  }

  public void setFrom(String from) {
    this.from = from;
  }

  public String getSubject() {
    return subject;
  }

  public void setSubject(String subject) {
    this.subject = subject;
  }

  public String getDate() {
    return date;
  }

  public void setDate(String date) {
    this.date = date;
  }

  public String getContent() {
    return content;
  }

  public void setContent(String content) {
    this.content = content;
  }

  /**
   * Constructor for create MailInfo Objects. These are useful when extracting
   * MailInfo and saving them to a file.
   * 
   * @param from Who sent the mail
   * @param subject Subject of the mail
   * @param date Date of the mail
   * @param content Content of the mail
   */
  public MailInfo(String from, String subject, String date, String content) {

    this.from = from;
    this.subject = subject;
    this.date = date;
    this.content = content;
  }

  @Override
  public String toString() {
    return new StringBuffer(" FROM: \t").append(this.from)
        .append("\n SUBJECT: \t").append(this.subject).append("\n DATE: \t")
        .append(this.date).append("\n CONTENT: \n \n").append(this.content)
        .toString();

  }

}
