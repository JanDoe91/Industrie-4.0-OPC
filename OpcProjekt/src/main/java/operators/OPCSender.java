package operators;

import java.util.Vector;

import config.SendConfig;
import adapter.OPC;

public class OPCSender {

	
	private SendConfig opcConfig;
	private OPC opcAdapter;
	
	public OPCSender(String ServerUrl, Vector<String> dataItemsOne){
		SendConfig tmp = new SendConfig(ServerUrl, dataItemsOne);
		this.opcConfig = tmp;
		this.opcAdapter = new OPC(this.opcConfig);
	}
	
	public void startOPCAdapter(){

		try {
			this.opcAdapter.importData();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	public void stopOPCAdapter(){
		this.opcAdapter.disconnect();
	}
}
