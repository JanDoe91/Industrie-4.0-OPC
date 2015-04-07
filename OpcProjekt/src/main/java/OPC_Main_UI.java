import java.util.Vector;

import ui.OPCMainFrame;
import config.SendConfig;

public class OPC_Main_UI {
	public static void main(String[] args) {
		Vector<String> dataItemsOne = new Vector<String>();
		dataItemsOne.add("5;Counter1");
		dataItemsOne.add("5;Expression1");
		String ServerUrl = "opc.tcp://localhost:53530/OPCUA/SimulationServer";
		OPCMainFrame frame = new OPCMainFrame(ServerUrl, dataItemsOne);
		frame.setVisible(true);
	}
}
