package ui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.ObjectStreamException;

import javax.xml.bind.JAXBException;

import operators.ERPReceiver;
import operators.OPCReceiver;
import operators.OPCSender;

public class ButtonListener implements ActionListener {
	private OPCSender opcSender;
	private OPCReceiver opcReceiver;
	private OPCMainFrame mainFrame;
	private ERPReceiver erpCustomerReceiver;
	private ERPReceiver erpMachineReceiver;

	private boolean opcSenderRunning;
	private boolean opcReceiverRunning;
	private boolean erpCustomerReceiverRunning;
	private boolean erpMachineReceiverRunning;

	private static final String btn_sender = "btn_sender";
	private static final String btn_receiver = "btn_receiver";
	private static final String btn_erp_customer_receiver = "btn_erp_customer_receiver";
	private static final String btn_erp_machine_receiver = "btn_erp_machine_receiver";

	public ButtonListener(OPCSender opcSender, OPCReceiver opcReceiver,
			OPCMainFrame mainFrame, ERPReceiver erpCustomerReceiver,
			ERPReceiver erpMachineReceiver) {
		this.opcReceiver = opcReceiver;
		this.opcSender = opcSender;
		this.opcReceiverRunning = false;
		this.opcSenderRunning = false;
		this.erpCustomerReceiverRunning = false;
		this.erpMachineReceiverRunning = false;
		this.mainFrame = mainFrame;
		this.erpCustomerReceiver = erpCustomerReceiver;
		this.erpMachineReceiver = erpMachineReceiver;

	}

	@Override
	public void actionPerformed(ActionEvent e) {
		String comand = e.getActionCommand();

		switch (comand) {
		case btn_sender:
			this.toggleSender();
			break;
		case btn_receiver:
			this.toggleReceiver();
			break;
		case btn_erp_customer_receiver:
			this.toggleERPCustomerReceiver();
			break;
		case btn_erp_machine_receiver:
			this.toggleERPMachineReceiver();
			break;
		}
	}



	public void toggleSender() {
		if (this.opcSenderRunning == false) {
			this.opcSender.startOPCAdapter();
			this.opcSenderRunning = true;
			this.mainFrame.getLb_sender_status().setText("Running");
			this.mainFrame.getBtn_start_sender().setText("Stop Sender");
		} else {
			this.opcSender.stopOPCAdapter();
			this.opcSenderRunning = false;
			this.mainFrame.getLb_sender_status().setText("Not Running");
			this.mainFrame.getBtn_start_sender().setText("Start Sender");
		}
	}

	public void toggleReceiver() {
		if (this.opcReceiverRunning == false) {
			this.opcReceiverRunning = true;
			this.mainFrame.getLb_receiver_status().setText("Running");
			this.mainFrame.getBtn_start_receiver().setText("Running");
			this.opcReceiver.startReceiver();
		} else {
			this.opcReceiverRunning = false;
			this.mainFrame.getLb_receiver_status().setText("Not Running");
			this.mainFrame.getBtn_start_receiver().setText("Start OPC Receiver");
		}

	}
	public void toggleERPMachineReceiver() {
		try {
			if (this.erpMachineReceiverRunning == false) {
				this.erpMachineReceiverRunning = true;
				this.mainFrame.getLb_erp_machine_receiver_status().setText("Running");
				this.mainFrame.getBtn_start_erp_machine_receiver().setText("Running");
				this.erpMachineReceiver.receive();
			} else {
				this.erpMachineReceiverRunning = false;
				this.mainFrame.getLb_erp_machine_receiver_status().setText("Not Running");
				this.mainFrame.getBtn_start_erp_machine_receiver().setText("Start ERP Machine Receiver");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public void toggleERPCustomerReceiver() {
		try {
			if (this.erpCustomerReceiverRunning == false) {
				this.erpCustomerReceiverRunning = true;
				this.mainFrame.getLb_erp_customer_receiver_status().setText("Running");
				this.mainFrame.getBtn_start_erp_customer_receiver().setText("Running");
				this.erpCustomerReceiver.receive();
			} else {
				this.erpCustomerReceiverRunning = false;
				this.mainFrame.getLb_erp_customer_receiver_status().setText("Not Running");
				this.mainFrame.getBtn_start_erp_customer_receiver().setText("Start ERP Costumer Receiver");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

	}
}
