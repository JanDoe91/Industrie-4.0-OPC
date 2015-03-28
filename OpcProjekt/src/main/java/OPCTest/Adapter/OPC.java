package OPCTest.Adapter;

import java.io.File;
import java.io.IOException;
import java.net.UnknownHostException;
import java.util.Locale;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;

import org.opcfoundation.ua.builtintypes.DataValue;
import org.opcfoundation.ua.builtintypes.DateTime;
import org.opcfoundation.ua.builtintypes.LocalizedText;
import org.opcfoundation.ua.builtintypes.NodeId;
import org.opcfoundation.ua.builtintypes.StatusCode;
import org.opcfoundation.ua.builtintypes.UnsignedShort;
import org.opcfoundation.ua.builtintypes.Variant;
import org.opcfoundation.ua.core.ApplicationDescription;
import org.opcfoundation.ua.core.ApplicationType;
import org.opcfoundation.ua.core.Attributes;
import org.opcfoundation.ua.core.MonitoringMode;
import org.opcfoundation.ua.transport.security.SecurityMode;

import OPCTest.Config.MainConfig;
import OPCTest.Config.SendConfig;
import OPCTest.Types.OpcDouble;
import OPCTest.Types.OpcInt;

import com.prosysopc.ua.ApplicationIdentity;
import com.prosysopc.ua.SecureIdentityException;
import com.prosysopc.ua.client.MonitoredDataItem;
import com.prosysopc.ua.client.MonitoredDataItemListener;
import com.prosysopc.ua.client.Subscription;
import com.prosysopc.ua.client.UaClient;

public class OPC extends Adapter {

	public OPC(SendConfig config) {
		super(config);

	}

	protected static void initialize(UaClient client)
			throws SecureIdentityException, IOException, UnknownHostException {
		// *** Application Description is sent to the server
		ApplicationDescription appDescription = new ApplicationDescription();
		appDescription.setApplicationName(new LocalizedText("DHBW Client",
				Locale.GERMAN));

		// 'localhost' (all lower case) in the URI is converted to the actual
		// host name of the computer in which the application is run
		appDescription.setApplicationUri("urn:localhost:UA:DHBWClient");
		appDescription.setProductUri("urn:prosysopc.com:UA:DHBWClient");
		appDescription.setApplicationType(ApplicationType.Client);

		final ApplicationIdentity identity = new ApplicationIdentity();
		identity.setApplicationDescription(appDescription);
		client.setApplicationIdentity(identity);
	}

	public UaClient createClient() throws Exception {
		// Create client object
		UaClient client = new UaClient(config.getServerUrl());
		client.setSecurityMode(SecurityMode.NONE);

		initialize(client);
		client.connect();

		return client;
	}

	public void importData() throws Exception {
		// Verbindungsaufbau
		UaClient client = createClient();

		for (int i = 0; i < config.getDataItems().size(); i++) {
			// Addresierung
			String string = config.getDataItems().get(i);
			String[] parts = string.split(";");
			int part1 = Integer.parseInt(parts[0]); // 004
			final String part2 = parts[1]; // 034556

			NodeId target = new NodeId(part1, part2);
			System.out.println(target);

			Subscription subscription = new Subscription();
			MonitoredDataItem item = new MonitoredDataItem(target,
					Attributes.Value, MonitoringMode.Reporting);
			System.out.println(item);

			subscription.addItem(item);
			client.addSubscription(subscription);

			item.setDataChangeListener(new MonitoredDataItemListener() {

				@Override
				public void onDataChange(MonitoredDataItem arg0,
						DataValue arg1, DataValue arg2) {

					switch (part2) {
					case "Counter1":
						createIntWriteXML(arg0.getNodeId(), arg1);
						createIntWriteXML(arg0.getNodeId(), arg2);
						break;
					case "Square1":
						createIntWriteXML(arg0.getNodeId(), arg1);
						createIntWriteXML(arg0.getNodeId(), arg2);
					case "Expression1":
						createDoubleWriteXML(arg0.getNodeId(), arg1);
						createDoubleWriteXML(arg0.getNodeId(), arg2);
						break;
					case "Random1":
						createDoubleWriteXML(arg0.getNodeId(), arg1);
						createDoubleWriteXML(arg0.getNodeId(), arg2);
					case "Sinusoid1":
						createDoubleWriteXML(arg0.getNodeId(), arg1);
						createDoubleWriteXML(arg0.getNodeId(), arg2);
					case "Sawtooth1":
						createDoubleWriteXML(arg0.getNodeId(), arg1);
						createDoubleWriteXML(arg0.getNodeId(), arg2);
					case "Triangle1":
						createDoubleWriteXML(arg0.getNodeId(), arg1);
						createDoubleWriteXML(arg0.getNodeId(), arg2);
					}

				}
			});
		}
	}

	public static void createIntWriteXML(NodeId nodeId, DataValue arg1) {
		if (arg1 != null) {
			Variant a = arg1.getValue();
			StatusCode b = arg1.getStatusCode();
			DateTime c = arg1.getSourceTimestamp();
			UnsignedShort d = arg1.getSourcePicoseconds();
			DateTime e = arg1.getServerTimestamp();
			UnsignedShort f = arg1.getServerPicoseconds();
			String bezeichnung = "" + nodeId.getValue();
			OpcInt opcInt = new OpcInt(bezeichnung, a, b, c, d, e, f);
			opcInt.writeXML();
		}
	}

	public static void createDoubleWriteXML(NodeId nodeId, DataValue arg1) {
		if (arg1 != null) {
			String bezeichnung = "" + nodeId.getValue();
			OpcDouble opcDouble = new OpcDouble(bezeichnung, arg1.getValue(),
					arg1.getStatusCode(), arg1.getSourceTimestamp(),
					arg1.getSourcePicoseconds(), arg1.getServerTimestamp(),
					arg1.getServerPicoseconds());
			opcDouble.writeXML();
		}
	}
}