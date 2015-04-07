package OPCTest.UI;

import java.awt.BorderLayout;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JButton;


import OPCTest.Operators.OPCReceiver;
import OPCTest.Operators.OPCSender;

import java.awt.Component;

import javax.swing.JTabbedPane;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.util.Vector;

import javax.swing.JLabel;
import javax.swing.SwingConstants;

public class OPCMainFrame extends JFrame {
	private OPCSender opcSender;
	private OPCReceiver opcReceiver;
	private JPanel contentPane;
	private ButtonListener buttonListener;
	
	private JLabel lb_sender_status;
	private JLabel lb_receiver_status;
	
	private static final String btn_sender = "btn_sender";
	private static final String btn_receiver = "btn_receiver";

	/**
	 * Launch the application.
	 */
//	public static void main(String[] args) {
//		EventQueue.invokeLater(new Runnable() {
//			public void run() {
//				try {
//					OPCMainFrame frame = new OPCMainFrame();
//					frame.setVisible(true);
//				} catch (Exception e) {
//					e.printStackTrace();
//				}
//			}
//		});
//	}

	/**
	 * Create the frame.
	 */
	public OPCMainFrame(String ServerUrl, Vector<String> dataItems) {
		OPCSender tmpSender = new OPCSender(ServerUrl, dataItems);
		this.opcSender = tmpSender;
		this.opcReceiver = new OPCReceiver();
		this.buttonListener = new ButtonListener(this.opcSender, this.opcReceiver, this);
		
		Thread opcReceiverThread = new Thread(this.opcReceiver);
		opcReceiverThread.start();
		
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
		
		JButton btn_start_sender = new JButton("Start Sender");
		btn_start_sender.setActionCommand(btn_sender);
		btn_start_sender.addActionListener(buttonListener);
		btn_start_sender.setBounds(10, 11, 143, 47);
		panel_main.add(btn_start_sender);
		
		JButton btn_start_receiver = new JButton("Start Receiver");
		btn_start_receiver.setBounds(10, 69, 143, 47);
		btn_start_receiver.setActionCommand(btn_receiver);
		btn_start_receiver.addActionListener(buttonListener);
		panel_main.add(btn_start_receiver);
		
		lb_sender_status = new JLabel("Not Running");
		lb_sender_status.setBounds(163, 11, 143, 47);
		panel_main.add(lb_sender_status);
		
		lb_receiver_status = new JLabel("Not Running");
		lb_receiver_status.setBounds(163, 69, 143, 47);
		panel_main.add(lb_receiver_status);
		
		JPanel panel_1 = new JPanel();
		tabbedPane.addTab("Counter1", null, panel_1, null);
		panel_1.setLayout(null);
		
		JPanel panel_2 = new JPanel();
		tabbedPane.addTab("Expression1", null, panel_2, null);
		panel_2.setLayout(null);
		setFocusTraversalPolicy(new FocusTraversalOnArray(new Component[]{contentPane}));
	}

	public JLabel getLb_sender_status() {
		return lb_sender_status;
	}

	public JLabel getLb_receiver_status() {
		return lb_receiver_status;
	}
	
}
