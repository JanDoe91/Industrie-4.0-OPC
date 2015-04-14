package reporting;

import opcTypes.ProSysType;

public class NewOPCEvent {
	private String bezeichnung;
	private ProSysType type;
	
	public NewOPCEvent(String bezeichnung, ProSysType type){
		this.bezeichnung = bezeichnung;
		this.type = type;
	}

	public String getBezeichnung() {
		return bezeichnung;
	}

	public ProSysType getType() {
		return type;
	}
	
	
	
}
