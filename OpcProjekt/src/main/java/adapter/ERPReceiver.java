package adapter;

import java.io.ObjectStreamException;

import javax.xml.bind.JAXBException;

import reporting.Reporter;
import ui.OPCMainFrame;

import com.espertech.esper.client.EPServiceProvider;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.ConsumerCancelledException;
import com.rabbitmq.client.QueueingConsumer;
import com.rabbitmq.client.ShutdownSignalException;
import com.thoughtworks.xstream.XStreamer;

import config.RecConfig;
import config.configStrings;
import erpTypes.CustomerOrder;
import erpTypes.MachineOrder;



public class ERPReceiver {
	private RecConfig config;
	private Reporter reporter;
	private Connection connection;
	private ConnectionFactory factory;
	private Channel channel;
	private QueueingConsumer consumer;
	private XStreamer xstream = new XStreamer();
	private String exchangeName;
	private EPServiceProvider epService;

	public ERPReceiver(String exchangeName) {
		this.config = new RecConfig();
		this.reporter = new Reporter();
		this.factory = new ConnectionFactory();
		this.connection = null;
		this.channel = null;
		this.consumer = null;
		this.exchangeName = exchangeName;
		try {
//			String queueName ="";
//			if(exchangeName == configStrings.ERPCustomer_order_queue){
//				queueName = configStrings.ERPCustomer_order_queue;
//			}else if (exchangeName == configStrings.ERPMachine_order_queue){
//				queueName = configStrings.ERPMachine_order_queue;
//			}
			this.factory.setHost(this.config.getHost());
			// this.factory.setUsername(configStrings.OPCQueueUserName);
			// this.factory.setPassword(configStrings.OPCQueuePassword);
			// this.factory.setPort(configStrings.OPCQueuePort);
			this.connection = this.factory.newConnection();
			this.channel = this.connection.createChannel();
			this.channel.exchangeDeclare(exchangeName, "fanout");
			String queueName = this.channel.queueDeclare().getQueue();
			this.channel.queueBind(queueName, exchangeName, "");
			//this.channel.queueDeclare(queueName, false,
			//false, false, null);
			this.consumer = new QueueingConsumer(this.channel);
			this.channel.basicConsume(queueName, true, this.consumer);

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public void process(String message) throws JAXBException,
			ClassNotFoundException, ObjectStreamException {
		if (this.exchangeName == configStrings.ERPCustomer_order_queue) {
			// JAXBContext customerContext =
			// JAXBContext.newInstance(CustomerOrder.class);
			// Unmarshaller um = customerContext.createUnmarshaller();
			CustomerOrder co = (CustomerOrder) xstream.fromXML(message);
			// um.unmarshal(new StreamSource(new StringReader(message)));
			epService.getEPRuntime().sendEvent(co);
		} else if (this.exchangeName == configStrings.ERPMachine_order_queue) {
			// JAXBContext machineContext =
			// JAXBContext.newInstance(MachineOrder.class);
			// Unmarshaller um = machineContext.createUnmarshaller();
			MachineOrder mo = (MachineOrder) xstream.fromXML(message);
			// (MachineOrder) um.unmarshal(new StreamSource(new
			// StringReader(message)));
			epService.getEPRuntime().sendEvent(mo);
		}
	}

	public void receive() throws JAXBException, ClassNotFoundException,
			ObjectStreamException {
		boolean messagesLeft = true;
		while (messagesLeft==true) {
			QueueingConsumer.Delivery delivery;
			try {
				delivery = this.consumer.nextDelivery(200);
				if(delivery != null){
				String message = new String(delivery.getBody());
				System.out.println(" [x] Received '" + message + "'");
				process(message);
				}else{
					messagesLeft = false;
					OPCMainFrame.ERPreceiverEnded(this);
				}
			} catch (ShutdownSignalException e) {
				e.printStackTrace();
			} catch (ConsumerCancelledException e) {
				e.printStackTrace();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

	public String getExchangeName() {
		return exchangeName;
	}
	

}
