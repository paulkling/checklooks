package org.gradle;

import java.util.ArrayList;
import java.util.List;

public class IrisUrl {

	private String id;
	private String url;
	private List<String> graphicsUrls = new ArrayList<String> ();
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public List<String> getGraphicsUrls() {
		return graphicsUrls;
	}
	public void setGraphicsUrls(List<String> graphicsUrls) {
		this.graphicsUrls = graphicsUrls;
	}
	
}
