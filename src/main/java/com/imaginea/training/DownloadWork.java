package com.imaginea.training;
import com.gargoylesoftware.htmlunit.html.HtmlAnchor;

public class DownloadWork {

	private final String url;
	private final String month;

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
