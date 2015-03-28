package OPCTest.Config;

import java.util.Vector;

public class SendConfig extends MainConfig {
	
	private String serverUrl;
	private Vector<String> dataItems;
	
	public SendConfig(String serverUrl, Vector<String> dataItems) {
		super();
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
