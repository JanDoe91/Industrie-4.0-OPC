import java.util.Vector;

import org.opcfoundation.ua.builtintypes.NodeId;
import org.opcfoundation.ua.core.Identifiers;

import OPCTest.Adapter.OPC;
import OPCTest.Config.Config;


public class OPC_SEND {

	//Demo -> erzeuge Config, �bergebe an Adapter, erstelle DataItems(Types)

	public static void main(String[] args) {
		
		//Erzeuge Config
		Vector<String> dataItemsOne = new Vector();
		dataItemsOne.add("5;Counter1");
		dataItemsOne.add("5;Expression1");
		
		Config opcConfig1 = new Config("opc.tcp://localhost:53530/OPCUA/SimulationServer", dataItemsOne);
		
		//�bergebe Config an Adapter
		OPC opcAdapter1 = new OPC(opcConfig1);
		try {
			opcAdapter1.importData();
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

}
