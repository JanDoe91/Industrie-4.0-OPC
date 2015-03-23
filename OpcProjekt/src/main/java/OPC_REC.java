import java.io.StringReader;
import java.io.StringWriter;
import java.util.Map;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Unmarshaller;

import OPCTest.Types.OpcDouble;
import OPCTest.Types.OpcInt;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.QueueingConsumer;


public class OPC_REC {
	private final static String host = "localhost";
	private final static String QUEUE_NAME = "ProSys_OPC";
	
	public static void main(String[] args) {
		try{
		ConnectionFactory factory = new ConnectionFactory();
		factory.setHost(host);
		Connection connection = factory.newConnection();
		Channel channel = connection.createChannel();
		channel.queueDeclare(QUEUE_NAME, false, false, false, null);
		QueueingConsumer consumer = new QueueingConsumer(channel);
		channel.basicConsume(QUEUE_NAME, true, consumer);
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
		System.out.println(newDouble.toString());
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	private static void createIntXML(String message){
		try{
		JAXBContext doubleContext = JAXBContext.newInstance(OpcInt.class);
		Unmarshaller jaxbUnmarshaller = doubleContext.createUnmarshaller();
		
		
		OpcInt newInt =  (OpcInt) jaxbUnmarshaller.unmarshal(new StringReader(message));
		System.out.println(newInt.toString());
		}catch(Exception e){
			e.printStackTrace();
		}
	}
}
