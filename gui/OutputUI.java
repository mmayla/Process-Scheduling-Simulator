package gui;

import java.awt.BorderLayout;
import java.awt.Desktop;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.JButton;
import javax.swing.JRadioButton;
import javax.swing.JMenuBar;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.UIManager;
import javax.swing.JComboBox;

import app_pack.PSSManager;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.io.File;
import java.io.IOException;
import java.util.Vector;

public class OutputUI extends JFrame
{

	private JPanel contentPane;
	private JTextField output_text;
	JComboBox comboBox;

	// variables
	PSSManager pssmang;
	Vector<String> combolist;
	Vector<Integer> combo_converter;
	
	OutputUI outframe;
	
	/**
	 * Launch the application.
	 */
//	public static void main(String[] args)
//	{
//		EventQueue.invokeLater(new Runnable()
//		{
//			public void run()
//			{
//				try
//				{
//					OutputUI frame = new OutputUI(new PSSManager());
//					frame.setVisible(true);
//				} catch (Exception e)
//				{
//					e.printStackTrace();
//				}
//			}
//		});
//	}

	/**
	 * Create the frame.
	 */
	public OutputUI(PSSManager psman, InputUI inframe)
	{
		setResizable(false);
		outframe = this;
		
		inframe.hide();
		pssmang = psman;
		combolist=new Vector<String>();
		combo_converter = new Vector<Integer>();
		//make combo list
		for(int i=0;i<7;i++)
		{
			if(pssmang.run_algorrithms[i])
			{
				combo_converter.add(i);
				combolist.add(pssmang.getAlgoName(i));
			}
		}
		
		
		setTitle("Infinity Dream PSS");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 473, 255);

		JMenuBar menuBar = new JMenuBar();
		setJMenuBar(menuBar);

		JMenu mnFile = new JMenu("File");
		menuBar.add(mnFile);

		JMenuItem mntmStartNewSimulation = new JMenuItem("Start new simulation");
		mntmStartNewSimulation.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) 
			{
				//TODO
				outframe.hide();
				InputUI inptframe = new InputUI();
				inptframe.setVisible(true);
			}
		});
		mntmStartNewSimulation.setBackground(UIManager
				.getColor("Button.shadow"));
		mnFile.add(mntmStartNewSimulation);

		JMenu mnHelp = new JMenu("Help");
		menuBar.add(mnHelp);

		JMenuItem mntmAbout = new JMenuItem("About");
		mntmAbout.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				AboutUI abtframe;
				try
				{
					abtframe = new AboutUI();
					abtframe.setVisible(true);
				} catch (IOException e)
				{
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
			}
		});
		mnHelp.add(mntmAbout);

		JMenuItem mntmHelp = new JMenuItem("Help");
		mnHelp.add(mntmHelp);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);

		JLabel label = new JLabel("Output directory");
		label.setBounds(12, 12, 151, 15);
		contentPane.add(label);

		output_text = new JTextField();
		output_text.setEditable(false);
		output_text.setColumns(10);
		output_text.setBounds(12, 31, 310, 26);
		contentPane.add(output_text);

		JButton open_button = new JButton("Open");
		open_button.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				// TODO open directory
				File userHome = new File(output_text.getText());
				try
				{
					Desktop.getDesktop().open(userHome);
				} catch (IOException e1)
				{
					JOptionPane.showMessageDialog(null,
							"Error opening the folder");
				}
			}
		});
		open_button.setBounds(334, 31, 117, 25);
		contentPane.add(open_button);

		JLabel lblChooseSimulation = new JLabel("Choose simulation");
		lblChooseSimulation.setBounds(12, 78, 151, 15);
		contentPane.add(lblChooseSimulation);

		JButton btnShow = new JButton("Show");
		btnShow.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				pssmang.drawGraph(combo_converter.elementAt(comboBox.getSelectedIndex()));
			}
		});
		btnShow.setBounds(175, 161, 117, 35);
		contentPane.add(btnShow);

		 final DefaultComboBoxModel<String> model = new DefaultComboBoxModel<String>(combolist);
		comboBox = new JComboBox<String>(model);
		comboBox.setBounds(22, 105, 250, 24);
		contentPane.add(comboBox);

		// update outdir text
		output_text.setText(pssmang.get_Output());
	}
}
