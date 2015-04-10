package ui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import operators.OPCReceiver;
import operators.OPCSender;

public class ButtonListener implements ActionListener {
	private OPCSender opcSender;
	private OPCReceiver opcReceiver;
	private OPCMainFrame mainFrame;
 
	
	private boolean opcSenderRunning;
	private boolean opcReceiverRunning;
	
	private static final String btn_sender = "btn_sender";
	private static final String btn_receiver = "btn_receiver";
	
	public ButtonListener(OPCSender opcSender, OPCReceiver opcReceiver, OPCMainFrame mainFrame){
		this.opcReceiver = opcReceiver;
		this.opcSender = opcSender;
		this.opcReceiverRunning = false;
		this.opcSenderRunning = false;
		this.mainFrame = mainFrame;
		
	}
	@Override
	public void actionPerformed(ActionEvent e) {
		String comand = e.getActionCommand();

		switch(comand){
		case btn_sender: 
			this.toggleSender();
			break;
		case btn_receiver:
			this.toggleReceiver();
			break;
		}
	}
	public void toggleSender(){
		if (this.opcSenderRunning == false){
			this.opcSender.startOPCAdapter();
			this.opcSenderRunning = true;
			this.mainFrame.getLb_sender_status().setText("Running");
			this.mainFrame.getBtn_start_sender().setText("Stop Sender");
		}else{
			this.opcSender.stopOPCAdapter();
			this.opcSenderRunning = false;
			this.mainFrame.getLb_sender_status().setText("Not Running");
			this.mainFrame.getBtn_start_sender().setText("Start Sender");
		}
	}

	public void toggleReceiver(){
		if(this.opcReceiverRunning == false){
			this.opcReceiverRunning = true;
			this.opcReceiver.startReceiver();
			this.mainFrame.getLb_receiver_status().setText("Running");
			this.mainFrame.getBtn_start_receiver().setText("Running");
		}else{
			this.opcReceiverRunning = false;
			this.mainFrame.getLb_receiver_status().setText("Not Running");
			this.mainFrame.getBtn_start_receiver().setText("Start Receiver");
		}
		
	}
}
