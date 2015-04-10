import java.io.StringReader;
import java.io.StringWriter;
import java.util.Map;
import java.util.Vector;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Unmarshaller;

import reporting.NewOPCEvent;
import reporting.OPCListener;
import reporting.Reporter;
import types.ProSysDouble;
import types.ProSysInt;

import com.espertech.esper.client.EPServiceProvider;
import com.espertech.esper.client.EPServiceProviderManager;
import com.espertech.esper.client.EPStatement;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.QueueingConsumer;

import config.RecConfig;

public class OPC_REC<T> {

	private static RecConfig config = new RecConfig();
	private static Reporter reporter = new Reporter();

	public static void main(String[] args) {
		try {

			ConnectionFactory factory = new ConnectionFactory();
			factory.setHost(config.getHost());
			Connection connection = factory.newConnection();
			Channel channel = connection.createChannel();
			channel.queueDeclare(config.getQUEUE_NAME(), false, false, false,
					null);
			QueueingConsumer consumer = new QueueingConsumer(channel);
			channel.basicConsume(config.getQUEUE_NAME(), true, consumer);

			// create Statements
			reporter.addExpression("select type.getValue() from reporting.NewOPCEvent.win:time(30 sec) where bezeichnung = 'Counter1'");
			reporter.addExpression("select type.getValue() from reporting.NewOPCEvent.win:time(30 sec) where bezeichnung = 'Expression1'");
			boolean messagesLeft = true;
			while (messagesLeft == true) {
				QueueingConsumer.Delivery delivery = consumer
						.nextDelivery(2000);
				if (delivery != null) {
					String message = new String(delivery.getBody());
					Map<String, Object> headers = delivery.getProperties()
							.getHeaders();
					String contentType = headers.get("type").toString();

					createXML(message, contentType);
				}else{
					messagesLeft = false;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private static void createXML(String message, String contentType) {

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

	private static void createDoubleXML(String message) {
		try {
			JAXBContext doubleContext = JAXBContext
					.newInstance(ProSysDouble.class);
			Unmarshaller jaxbUnmarshaller = doubleContext.createUnmarshaller();

			ProSysDouble newDouble = (ProSysDouble) jaxbUnmarshaller
					.unmarshal(new StringReader(message));

			NewOPCEvent opcEvent = new NewOPCEvent(newDouble.getBezeichnung(),
					newDouble);

			reporter.getEpService().getEPRuntime().sendEvent(opcEvent);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private static void createIntXML(String message) {
		try {
			JAXBContext doubleContext = JAXBContext.newInstance(ProSysInt.class);
			Unmarshaller jaxbUnmarshaller = doubleContext.createUnmarshaller();

			ProSysInt newInt = (ProSysInt) jaxbUnmarshaller
					.unmarshal(new StringReader(message));

			// Create and Send Event
			NewOPCEvent opcEvent = new NewOPCEvent(newInt.getBezeichnung(),
					newInt);
			reporter.getEpService().getEPRuntime().sendEvent(opcEvent);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
