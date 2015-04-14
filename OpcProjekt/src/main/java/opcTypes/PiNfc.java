package opcTypes;


import java.sql.Timestamp;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;


@XmlRootElement(name="PiNfc")
public class PiNfc {
	@XmlElement
	private String tag;
	
	@XmlElement
	private String readTime;
	
	public PiNfc(String tag, String readTime){
		this.tag = tag;
		this.readTime = readTime;
	}
	
	public PiNfc(){
		
	}

	public String getValue() {
		return this.tag;
	}

	public String getReadTime() {
		return readTime;
	}
	
	public String toString(){
		return "Tag: " + this.tag + "/ Timestamp: " + this.readTime;
		
	}
	public Long getDatumInLong(){
		Long tmp = Long.parseLong(this.readTime);		
		return tmp;
	}
	
	
}
