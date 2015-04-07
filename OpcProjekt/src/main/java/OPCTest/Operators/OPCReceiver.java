package OPCTest.Operators;

import java.io.StringReader;
import java.io.StringWriter;
import java.util.Map;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Unmarshaller;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.QueueingConsumer;

import OPCTest.Config.RecConfig;
import OPCTest.Reporting.NewOPCEvent;
import OPCTest.Reporting.Reporter;
import OPCTest.Types.OpcDouble;
import OPCTest.Types.OpcInt;

public class OPCReceiver<T> implements Runnable{
	private RecConfig config;
	private Reporter reporter;
	private Connection connection;
	private ConnectionFactory factory;
	private Channel channel;
	private QueueingConsumer consumer;

	public OPCReceiver() {
		this.config = new RecConfig();
		this.reporter = new Reporter();
		this.factory = new ConnectionFactory();
		this.connection = null;
		this.channel = null;
		this.consumer = null;
	}

	public void startReceiver() {
		try {

			this.factory.setHost(this.config.getHost());
			this.connection = this.factory.newConnection();
			this.channel = this.connection.createChannel();
			this.channel.queueDeclare(this.config.getQUEUE_NAME(), false,
					false, false, null);
			this.consumer = new QueueingConsumer(this.channel);
			this.channel.basicConsume(this.config.getQUEUE_NAME(), true,
					consumer);

		} catch (Exception e) {
			e.printStackTrace();
		}
		
		boolean messagesLeft = true;
		while (messagesLeft == true) {
			try{
			QueueingConsumer.Delivery delivery = this.consumer.nextDelivery(2000);
			if (delivery != null) {
				String message = new String(delivery.getBody());
				Map<String, Object> headers = delivery.getProperties()
						.getHeaders();
				String contentType = headers.get("type").toString();

				this.createXML(message, contentType);
			}else{
				messagesLeft = false;
			}}catch(Exception e){
				e.printStackTrace();
			}
		}
	}
	
	private void createXML(String message, String contentType) {

		StringWriter sw = new StringWriter();
		sw.write(message);

		switch (contentType) {
		case "Counter1":
			this.createIntXML(message);
			this.createIntXML(message);
			break;
		case "Square1":
			this.createIntXML(message);
			this.createIntXML(message);
		case "Expression1":
			this.createDoubleXML(message);
			this.createDoubleXML(message);
			break;
		case "Random1":
			this.createDoubleXML(message);
			this.createDoubleXML(message);
		case "Sinusoid1":
			this.createDoubleXML(message);
			this.createDoubleXML(message);
		case "Sawtooth1":
			this.createDoubleXML(message);
			this.createDoubleXML(message);
		case "Triangle1":
			this.createDoubleXML(message);
			this.createDoubleXML(message);
		}

	}

	private void createDoubleXML(String message) {
		try {
			JAXBContext doubleContext = JAXBContext
					.newInstance(OpcDouble.class);
			Unmarshaller jaxbUnmarshaller = doubleContext.createUnmarshaller();

			OpcDouble newDouble = (OpcDouble) jaxbUnmarshaller
					.unmarshal(new StringReader(message));

			NewOPCEvent opcEvent = new NewOPCEvent(newDouble.getBezeichnung(),
					newDouble);

			this.reporter.getEpService().getEPRuntime().sendEvent(opcEvent);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void createIntXML(String message) {
		try {
			JAXBContext doubleContext = JAXBContext.newInstance(OpcInt.class);
			Unmarshaller jaxbUnmarshaller = doubleContext.createUnmarshaller();

			OpcInt newInt = (OpcInt) jaxbUnmarshaller
					.unmarshal(new StringReader(message));

			// Create and Send Event
			NewOPCEvent opcEvent = new NewOPCEvent(newInt.getBezeichnung(),
					newInt);
			this.reporter.getEpService().getEPRuntime().sendEvent(opcEvent);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void stopReceiver() {
		this.consumer = null;
		try {
			this.channel.close();
			this.connection.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void run() {
		
	}
}
