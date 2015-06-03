package com.imaginea.crawler;

import org.apache.bcel.generic.INSTANCEOF;

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

	public MailInfo(String from, String subject, String date, String content) {

		this.from = from;
		this.subject = subject;
		this.date = date;
		this.content = content;
	}

	@Override
	public String toString() {
		return new StringBuffer(" FROM: \t").append(this.from)
				.append("\n SUBJECT: \t").append(this.subject)
				.append("\n DATE: \t").append(this.date)
				.append("\n CONTENT: \n \n").append(this.content).toString();

	}
	@Override 
	public boolean equals(Object o)
	{
		if(o instanceof MailInfo)
		{
			MailInfo mailInfo = (MailInfo) o;
			if(getFrom().equals(mailInfo.getFrom()) && getSubject().equals(mailInfo.getSubject()) && getDate().equals(mailInfo.getDate()) && getContent().equals(mailInfo.getContent()) )
				return true;
			else
				return false;
		}
		else
			return false;
		
	}
}
