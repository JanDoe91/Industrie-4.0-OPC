package OPCTest.Reporting;

import OPCTest.Types.Type;

public class NewOPCEvent {
	private String bezeichnung;
	private Type type;
	
	public NewOPCEvent(String bezeichnung, Type type){
		this.bezeichnung = bezeichnung;
		this.type = type;
	}

	public String getBezeichnung() {
		return bezeichnung;
	}

	public Type getType() {
		return type;
	}
	
	
	
}
