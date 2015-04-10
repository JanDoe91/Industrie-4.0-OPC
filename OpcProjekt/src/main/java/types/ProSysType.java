package types;

import java.io.File;
import java.io.IOException;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.Map;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;
import javax.xml.transform.Result;

import org.opcfoundation.ua.builtintypes.DateTime;
import org.opcfoundation.ua.builtintypes.NodeId;
import org.opcfoundation.ua.builtintypes.StatusCode;
import org.opcfoundation.ua.builtintypes.UnsignedShort;
import org.opcfoundation.ua.builtintypes.Variant;

import com.rabbitmq.client.AMQP.BasicProperties;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.Channel;

import config.configStrings;

//@XmlType(name="",propOrder={
//		"nodeId",
//		"value",
//		"statusCode",
//		"sourceTimestamp",
//		"sourcePicoseconds",
//		"serverTimestamp",
//		"serverPicoseconds"
//})
@XmlRootElement
// (name = "Type")
public abstract class ProSysType <T>{

	private final static String host = "localhost";


	@XmlElement
	// (required=true)
	private int statusCode;

	@XmlElement
	// (required=true)
	private long sourceTimestamp;

	@XmlElement
	// (required=true)
	private int sourcePicoseconds;

	@XmlElement
	// (required=true)
	private long serverTimestamp;

	@XmlElement
	// (required=true)
	private int serverPicoseconds;

	@XmlElement
	// (required=true)
	private String bezeichnung;

	public ProSysType() {

	}

	public ProSysType(String bezeichnung, StatusCode statusCode,
			DateTime sourceTimestamp, UnsignedShort sourcePicoseconds,
			DateTime serverTimestamp, UnsignedShort serverPicoseconds) {
		this.statusCode = statusCode.getValueAsIntBits();
		this.sourceTimestamp = sourceTimestamp.getValue();
		this.sourcePicoseconds = sourcePicoseconds.getValue();
		if (serverTimestamp != null) {
			this.serverTimestamp = serverTimestamp.getValue();
		}

		this.serverPicoseconds = serverPicoseconds.getValue();
		this.bezeichnung = bezeichnung;
	}

	public void writeXML() {

		try {
			// JAXB initialisieren
			JAXBContext context = null;
			switch (this.bezeichnung) {
			case "Counter1":
				context = JAXBContext.newInstance(ProSysInt.class);
				break;
			case "Square1":
				context = JAXBContext.newInstance(ProSysDouble.class);
				break;
			case "Expression1":
				context = JAXBContext.newInstance(ProSysDouble.class);
				break;
			case "Random1":
				context = JAXBContext.newInstance(ProSysDouble.class);
				break;
			case "Sinusoid1":
				context = JAXBContext.newInstance(ProSysDouble.class);
				break;
			case "Sawtooth1":
				context = JAXBContext.newInstance(ProSysDouble.class);
				break;
			case "Triangle1":
				context = JAXBContext.newInstance(ProSysDouble.class);
				break;
			default:
				context = null;
			}
			if (context == null) {
				System.out.println("Context ist Null. Klasse Type. Methode writeXML");
			} else {
				Marshaller jaxbMarshaller = context.createMarshaller();
				Unmarshaller jaxbUnmarshaller = context.createUnmarshaller();
				jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT,
						true);

				// Datei muss nicht mehr erzeugt werden
				// File f = File.createTempFile(this.bezeichnung ,".xml");
				StringWriter sw = new StringWriter();

				// tmp Datei muss nicht mehr erzeugt werden
				// Objekt in tmp-Datei schreiben
				// jaxbMarshaller.marshal(this, f);

				// Marschal into Stringwriter
				jaxbMarshaller.marshal(this, sw);
				// Get String from StrinWriter
				String message = sw.toString();
				// Print the Message

				createQueue(message, this.bezeichnung);
				// System.out.println("XML geschrieben in Datei: " +
				// f.toString());
			}
		} catch (JAXBException e) {
			System.out.println("XML konnte nicht generiert werden");
			e.printStackTrace();
		}

	}

	private static void createQueue(String message, String bezeichnung) {
		Connection connection = null;
		Channel channel = null;
		try {
			ConnectionFactory factory = new ConnectionFactory();
			factory.setHost(configStrings.queueIP); //+ ":" + configStrings.queuePort);
			connection = factory.newConnection();
			channel = connection.createChannel();
			channel.queueDeclare(configStrings.queueName, false, false, false, null);
			
			//Set Header of Message with bezeichnung
			BasicProperties.Builder prop = new BasicProperties.Builder();
			Map<String, Object> headers = new HashMap <String, Object>();
			headers.put(configStrings.headerType, bezeichnung);
			headers.put(configStrings.headerSourceSystem, configStrings.sourceSystemProSys);
			prop.headers(headers);
			BasicProperties theProp = prop.build();
			
			//Send the Message to Queue
			channel.basicPublish("", configStrings.queueName, theProp, message.getBytes());
			
			System.out.println(" [x] Sent '" + message + "'");
			channel.close();
			connection.close();
		} catch (Exception e) {
			System.out.println("Schreiben in die Queue nicht möglich");
			if (connection != null) {
				try {
					connection.close();
				} catch (Exception f) {
					System.out
							.println("Connection konnte nicht geschlossen werden");
					f.printStackTrace();
				}
			}// ifconnection
			if (channel != null) {
				try {
					channel.close();
				} catch (Exception f) {
					System.out
							.println("Channel konnte nicht geschlossen werden");
					f.printStackTrace();
				}
			}
		}
	}

	public String getBezeichnung() {
		return bezeichnung;
	}
	
	public T getValue(){
		if( this instanceof ProSysDouble){
			ProSysDouble tmp = (ProSysDouble) this;
			T tmp2 = (T) Double.valueOf(tmp.getValue2());

			return tmp2;
		}else if(this instanceof ProSysInt){
			ProSysInt tmp = (ProSysInt) this;
			T tmp2 = (T) Integer.valueOf(tmp.getValue2());

			return tmp2;
		}
		return null;
	}


}
