import java.io.StringReader;
import java.io.StringWriter;
import java.util.Map;
import java.util.Vector;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Unmarshaller;

import OPCTest.Reporting.NewOPCEvent;
import OPCTest.Reporting.OPCListener;
import OPCTest.Types.OpcDouble;
import OPCTest.Types.OpcInt;

import com.espertech.esper.client.EPAdministrator;
import com.espertech.esper.client.EPServiceProvider;
import com.espertech.esper.client.EPServiceProviderManager;
import com.espertech.esper.client.EPStatement;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.QueueingConsumer;


public class OPC_REC <T>{
	private final static String host = "localhost";
	private final static String QUEUE_NAME = "ProSys_OPC";
	private static EPServiceProvider epService;
	
	public static void main(String[] args) {
		try{
		ConnectionFactory factory = new ConnectionFactory();
		factory.setHost(host);
		Connection connection = factory.newConnection();
		Channel channel = connection.createChannel();
		channel.queueDeclare(QUEUE_NAME, false, false, false, null);
		QueueingConsumer consumer = new QueueingConsumer(channel);
		channel.basicConsume(QUEUE_NAME, true, consumer);
		
		
		//create Statements
		epService = EPServiceProviderManager.getDefaultProvider();
		Vector<String> expressions = new Vector<String>();
		Vector<EPStatement> statements = new Vector <EPStatement>();

		
		expressions.add("select type.getValue() from OPCTest.Reporting.NewOPCEvent.win:time(30 sec) where bezeichnung = 'Counter1'");
		expressions.add("select type.getValue() from OPCTest.Reporting.NewOPCEvent.win:time(30 sec) where bezeichnung = 'Expression1'");
		
		
		for(int i = 0; i<expressions.size(); i++){
			EPStatement statement = epService.getEPAdministrator().createEPL(expressions.get(i));
			OPCListener listen = new OPCListener();
			statement.addListener(listen);
			//Zeile ist momentan nicht nötig
			//Falls in Zukunft auf Statements zugegriffen werden muss besteht über den Vector
			//statements ein Zugriff auf die einzelenen Objekte
			statements.add(statement);
			
		}

		
		
//		
//		EPStatement statement2= epService.getEPAdministrator().createEPL(expression2);
//		OPCListener listener = new OPCListener();
//		OPCListener listener2 = new OPCListener();
//		statement.addListener(listener);
//		statement2.addListener(listener2);
		
		while (true) {
			QueueingConsumer.Delivery delivery = consumer.nextDelivery();
			String message = new String(delivery.getBody());
			Map<String, Object> headers = delivery.getProperties().getHeaders();
			String contentType = headers.get("type").toString();
			
			
			createXML(message, contentType);
		}
	}catch(Exception e){
		e.printStackTrace();
		}
	}
	private static void createXML(String message, String contentType){
		
		
		StringWriter sw = new StringWriter();
		sw.write(message);
		
		
		switch (contentType) {
		case "Counter1":
			createIntXML(message);
			createIntXML(message);
			break;
		case "Square1":
			createIntXML(message);
			createIntXML(message);
		case "Expression1":
			createDoubleXML(message);
			createDoubleXML(message);
			break;
		case "Random1":
			createDoubleXML(message);
			createDoubleXML(message);
		case "Sinusoid1":
			createDoubleXML(message);
			createDoubleXML(message);
		case "Sawtooth1":
			createDoubleXML(message);
			createDoubleXML(message);
		case "Triangle1":
			createDoubleXML(message);
			createDoubleXML(message);
		}

		
		
	}
	
	private static void createDoubleXML(String message){
		try{
		JAXBContext doubleContext = JAXBContext.newInstance(OpcDouble.class);
		Unmarshaller jaxbUnmarshaller = doubleContext.createUnmarshaller();
		
		
		OpcDouble newDouble =  (OpcDouble) jaxbUnmarshaller.unmarshal(new StringReader(message));

		NewOPCEvent opcEvent = new NewOPCEvent(newDouble.getBezeichnung(), newDouble);
		
		epService.getEPRuntime().sendEvent(opcEvent);
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	private static void createIntXML(String message){
		try{
		JAXBContext doubleContext = JAXBContext.newInstance(OpcInt.class);
		Unmarshaller jaxbUnmarshaller = doubleContext.createUnmarshaller();
		
		
		OpcInt newInt =  (OpcInt) jaxbUnmarshaller.unmarshal(new StringReader(message));
		
		//Create and Send Event
		NewOPCEvent opcEvent = new NewOPCEvent(newInt.getBezeichnung(), newInt);
		epService.getEPRuntime().sendEvent(opcEvent);
		}catch(Exception e){
			e.printStackTrace();
		}
	}
}
