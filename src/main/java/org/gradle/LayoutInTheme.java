package org.gradle;

import java.util.ArrayList;
import java.util.List;

public class LayoutInTheme {

	private String externalKey;
	private List<IrisUrl> iris = new ArrayList<IrisUrl>();
	
	public String getExternalKey() {
		return externalKey;
	}
	public void setExternalKey(String externalKey) {
		this.externalKey = externalKey;
	}
	public List<IrisUrl> getIris() {
		return iris;
	}
	public void setIris(List<IrisUrl> iris) {
		this.iris = iris;
	}

	
}
