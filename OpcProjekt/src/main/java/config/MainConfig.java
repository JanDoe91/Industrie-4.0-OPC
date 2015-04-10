package config;

import java.util.Vector;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class MainConfig {
	
	
	private String host;
	private String QUEUE_NAME;
	
	public MainConfig (){
		this.host = configStrings.queueIP;
		this.QUEUE_NAME = configStrings.queueName;
	}

	public String getHost() {
		return host;
	}

	public String getQUEUE_NAME() {
		return QUEUE_NAME;
	}

	public void setHost(String host) {
		this.host = host;
	}

	public void setQUEUE_NAME(String qUEUE_NAME) {
		QUEUE_NAME = qUEUE_NAME;
	}

	
}
