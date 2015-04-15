package ui;

import java.awt.BorderLayout;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JButton;






import java.awt.Component;

import javax.swing.JTabbedPane;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.util.Vector;

import javax.swing.JLabel;
import javax.swing.SwingConstants;
import javax.swing.JComboBox;

import adapter.ERPReceiver;
import adapter.OPCReceiver;
import adapter.OPCSender;
import config.configStrings;

public class OPCMainFrame extends JFrame {
	private OPCSender opcSender;
	private OPCReceiver opcReceiver;
	private ERPReceiver erpCustomerReceiver;
	private ERPReceiver erpMachineReceiver;
	private JPanel contentPane;
	private ButtonListener buttonListener;
	
	private JLabel lb_sender_status;
	private JLabel lb_receiver_status;
	private JLabel lb_erp_customer_receiver_status;
	private JLabel lb_erp_machine_receiver_status;
	
	
	private JButton btn_start_sender;


	private JButton btn_start_receiver;
	private JButton btn_start_erp_customer_receiver;
	private JButton btn_start_erp_machine_receiver;
	

	


	private static final String btn_sender = "btn_sender";
	private static final String btn_receiver = "btn_receiver";
	private static final String btn_erp_customer_receiver = "btn_erp_customer_receiver";
	private static final String btn_erp_machine_receiver = "btn_erp_machine_receiver";
	
	private static OPCMainFrame mainFrame;
	



	public OPCMainFrame() {
		OPCSender tmpSender = new OPCSender(configStrings.OPCProsysServerUrl, configStrings.dataItems);
		this.opcSender = tmpSender;
		this.opcReceiver = new OPCReceiver();
		this.erpCustomerReceiver = new ERPReceiver(configStrings.ERPCustomer_order_queue);
		this.erpMachineReceiver = new ERPReceiver(configStrings.ERPMachine_order_queue);
		
		this.buttonListener = new ButtonListener(this.opcSender, this.opcReceiver, this, this.erpCustomerReceiver, this.erpMachineReceiver);
		

		
		setTitle("Industrie 4.0");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 800, 600);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JTabbedPane tabbedPane = new JTabbedPane(JTabbedPane.TOP);
		tabbedPane.setBounds(10, 45, 764, 506);
		contentPane.add(tabbedPane);
		
		JPanel panel_main = new JPanel();
		tabbedPane.addTab("Main", null, panel_main, null);
		panel_main.setLayout(null);
		
		btn_start_sender = new JButton("Start Sender");
		btn_start_sender.setActionCommand(btn_sender);
		btn_start_sender.addActionListener(buttonListener);
		btn_start_sender.setBounds(10, 11, 231, 47);
		panel_main.add(btn_start_sender);
		
		btn_start_receiver = new JButton("Start OPC Receiver");
		btn_start_receiver.setBounds(10, 69, 231, 47);
		btn_start_receiver.setActionCommand(btn_receiver);
		btn_start_receiver.addActionListener(buttonListener);
		panel_main.add(btn_start_receiver);
		
		lb_sender_status = new JLabel("Not Running");
		lb_sender_status.setBounds(251, 11, 143, 47);
		panel_main.add(lb_sender_status);
		
		lb_receiver_status = new JLabel("Not Running");
		lb_receiver_status.setBounds(251, 69, 143, 47);
		panel_main.add(lb_receiver_status);
		
		btn_start_erp_customer_receiver = new JButton("Start ERP Customer Receiver");
		btn_start_erp_customer_receiver.setActionCommand(btn_erp_customer_receiver);
		btn_start_erp_customer_receiver.addActionListener(buttonListener);
		btn_start_erp_customer_receiver.setBounds(10, 127, 231, 47);
		panel_main.add(btn_start_erp_customer_receiver);
		
		lb_erp_customer_receiver_status = new JLabel("Not Running");
		lb_erp_customer_receiver_status.setBounds(251, 127, 143, 47);
		panel_main.add(lb_erp_customer_receiver_status);
		
		btn_start_erp_machine_receiver = new JButton("Start ERP Machine Receiver");
		btn_start_erp_machine_receiver.addActionListener(buttonListener);
		btn_start_erp_machine_receiver.setActionCommand(btn_erp_machine_receiver);
		btn_start_erp_machine_receiver.setBounds(10, 185, 231, 47);
		panel_main.add(btn_start_erp_machine_receiver);
		
		lb_erp_machine_receiver_status = new JLabel("Not Running");
		lb_erp_machine_receiver_status.setBounds(251, 185, 143, 47);
		panel_main.add(lb_erp_machine_receiver_status);
		setFocusTraversalPolicy(new FocusTraversalOnArray(new Component[]{contentPane}));
		
		mainFrame = this;
	}

	public JLabel getLb_sender_status() {
		return lb_sender_status;
	}

	public JLabel getLb_receiver_status() {
		return lb_receiver_status;
	}
	public JButton getBtn_start_sender() {
		return btn_start_sender;
	}

	public JButton getBtn_start_receiver() {
		return btn_start_receiver;
	}
	public JButton getBtn_start_erp_customer_receiver() {
		return btn_start_erp_customer_receiver;
	}

	public JButton getBtn_start_erp_machine_receiver() {
		return btn_start_erp_machine_receiver;
	}
	public JLabel getLb_erp_machine_receiver_status() {
		return lb_erp_machine_receiver_status;
	}
	public JLabel getLb_erp_customer_receiver_status() {
		return lb_erp_customer_receiver_status;
	}
	
	public static void OPCreceiverEnded(){
		mainFrame.buttonListener.toggleReceiver();
	}
	public static void ERPreceiverEnded(ERPReceiver receiver){
		if(receiver.getExchangeName()==configStrings.ERPCustomer_order_queue){
			mainFrame.buttonListener.toggleERPCustomerReceiver();
		}else if(receiver.getExchangeName()==configStrings.ERPMachine_order_queue){
			mainFrame.buttonListener.toggleERPMachineReceiver();
		}
	
	}
	
}
