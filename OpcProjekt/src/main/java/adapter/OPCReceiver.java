package adapter;

import java.io.StringReader;
import java.io.StringWriter;
import java.util.Map;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Unmarshaller;

import opcTypes.PiNfc;
import opcTypes.ProSysDouble;
import opcTypes.ProSysInt;
import reporting.NewOPCEvent;
import reporting.Reporter;
import ui.OPCMainFrame;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.QueueingConsumer;

import config.RecConfig;
import config.configStrings;

public class OPCReceiver<T> {
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
		try {

			this.factory.setHost(this.config.getHost());
			this.factory.setUsername(configStrings.OPCQueueUserName);
			this.factory.setPassword(configStrings.OPCQueuePassword);
			this.factory.setPort(configStrings.OPCQueuePort);
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
	}

	public void startReceiver() {

		reporter.addExpression("select type.getValue() from reporting.NewOPCEvent.win:time(30 sec) where bezeichnung = 'Counter1'");
		reporter.addExpression("select type.getValue() from reporting.NewOPCEvent.win:time(30 sec) where bezeichnung = 'Expression1'");

		boolean messagesLeft = true;
		while (messagesLeft == true) {
			try {
				QueueingConsumer.Delivery delivery = this.consumer
						.nextDelivery(200);
				if (delivery != null) {
					String message = new String(delivery.getBody());
					Map<String, Object> headers = delivery.getProperties()
							.getHeaders();
					String contentType = headers.get(configStrings.OPCHeaderType)
							.toString();
					this.createXML(message, contentType);
				} else {
					messagesLeft = false;
					OPCMainFrame.OPCreceiverEnded();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	private void createXML(String message, String contentType) {

		StringWriter sw = new StringWriter();
		sw.write(message);

		switch (contentType) {
		case "Counter1":
			this.createProSysIntXML(message);
			break;
		case "Square1":
			this.createProSysIntXML(message);
			break;
		case "Expression1":
			this.createProSysDoubleXML(message);
			break;
		case "Random1":
			this.createProSysDoubleXML(message);
			break;
		case "Sinusoid1":
			this.createProSysDoubleXML(message);
			break;
		case "Sawtooth1":
			this.createProSysDoubleXML(message);
			break;
		case "Triangle1":
			this.createProSysDoubleXML(message);
			break;
		case "PiTag":
			this.createPiNfcTag(message);
			break;
		}

	}

	private void createPiNfcTag(String message) {
		try {
			JAXBContext stringContext = JAXBContext.newInstance(PiNfc.class);
			Unmarshaller jaxbUnmarshaller = stringContext.createUnmarshaller();

			StringReader tmp = new StringReader(message);
			PiNfc newString = (PiNfc) jaxbUnmarshaller.unmarshal(tmp);
			System.out.println(newString.toString());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void createProSysDoubleXML(String message) {
		try {
			JAXBContext doubleContext = JAXBContext
					.newInstance(ProSysDouble.class);
			Unmarshaller jaxbUnmarshaller = doubleContext.createUnmarshaller();

			ProSysDouble newDouble = (ProSysDouble) jaxbUnmarshaller
					.unmarshal(new StringReader(message));

			NewOPCEvent opcEvent = new NewOPCEvent(newDouble.getBezeichnung(),
					newDouble);

			this.reporter.getEpService().getEPRuntime().sendEvent(opcEvent);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void createProSysIntXML(String message) {
		try {
			JAXBContext doubleContext = JAXBContext
					.newInstance(ProSysInt.class);
			Unmarshaller jaxbUnmarshaller = doubleContext.createUnmarshaller();
			StringReader tmp = new StringReader(message);
			ProSysInt newInt = (ProSysInt) jaxbUnmarshaller.unmarshal(tmp);

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

}
