package OPCTest.Config;

import java.util.Vector;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class Config {
	private String serverUrl;
	private Vector<String> dataItems;
	
	public Config (String serverUrl, Vector<String> dataItems){
		this.serverUrl = serverUrl;
		this.dataItems = dataItems;
	}

	public String getServerUrl() {
		return serverUrl;
	}

	public void setServerUrl(String serverUrl) {
		this.serverUrl = serverUrl;
	}

	public Vector<String> getDataItems() {
		return dataItems;
	}

	public void setDataItems(Vector<String> dataItems) {
		this.dataItems = dataItems;
	}
}
