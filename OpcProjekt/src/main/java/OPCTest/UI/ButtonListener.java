package OPCTest.UI;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import OPCTest.Operators.OPCReceiver;
import OPCTest.Operators.OPCSender;

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
		}else{
			this.opcSender.stopOPCAdapter();
			this.opcSenderRunning = false;
			this.mainFrame.getLb_sender_status().setText("Not Running");
		}
	}
	public void toggleReceiver(){
		if(this.opcReceiverRunning == false){
			this.opcReceiver.startReceiver();
			this.opcReceiverRunning = true;
			this.mainFrame.getLb_receiver_status().setText("Running");
		}else{
			this.opcReceiver.stopReceiver();
			this.opcReceiverRunning = false;
			this.mainFrame.getLb_receiver_status().setText("Not Running");
		}
		
	}
}
