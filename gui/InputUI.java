package gui;

import java.awt.BorderLayout;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JFileChooser;
import javax.swing.JMenuBar;
import javax.swing.JMenu;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;

import java.awt.FlowLayout;
import javax.swing.JTextField;
import javax.swing.JLabel;
import net.miginfocom.swing.MigLayout;
import javax.swing.BoxLayout;
import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.ColumnSpec;
import com.jgoodies.forms.factories.FormFactory;
import com.jgoodies.forms.layout.RowSpec;

import exception_error_pack.PSSexception;

import javax.swing.JButton;
import javax.swing.UIManager;
import javax.swing.JCheckBox;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.io.File;
import java.io.IOException;

import app_pack.PSSManager;

public class InputUI extends JFrame
{
	private PSSManager pssmang;

	private JPanel contentPane;
	private JTextField input_text;
	private JTextField output_text;
	private JTextField timequantum;
	private JTextField contextswitching;

	// check boxes
	JCheckBox chckbxall;
	JCheckBox chckbxFcfs;
	JCheckBox chckbxSjf;
	JCheckBox chckbxRr;
	JCheckBox chckbxHpf;
	JCheckBox chckbxHpfstaticpreemptive;
	JCheckBox chckbxHpfdynamic;
	JCheckBox chckbxWrr;

	// variables
	private float timeq = 0;
	private float cs = 0;
	private String in_path;
	private String out_path;

	// lists
	private JCheckBox[] chkboxlist = new JCheckBox[7];
	private boolean[] run_algorithms = new boolean[7];

	private InputUI inptframe;
	
//	public static void main(String[] args)
//	{
//		EventQueue.invokeLater(new Runnable()
//		{
//			public void run()
//			{
//				try
//				{
//					InputUI frame = new InputUI();
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
	public InputUI()
	{
		inptframe = this;
		
		setResizable(false);
		setTitle("Infinity Dream PSS");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 471, 401);

		JMenuBar menuBar = new JMenuBar();
		setJMenuBar(menuBar);

		JMenu mnNewMenu = new JMenu("File");
		menuBar.add(mnNewMenu);

		JMenuItem mntmNewMenuItem = new JMenuItem("Start new simulation");
		mntmNewMenuItem.setBackground(UIManager.getColor("Button.shadow"));
		mnNewMenu.add(mntmNewMenuItem);

		JMenuItem mntmAdvancedSetting = new JMenuItem("Advanced settings");
		mntmAdvancedSetting.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				AdvancedSettingsUI advancedframe = new AdvancedSettingsUI();
				advancedframe.setVisible(true);
			}
		});
		mnNewMenu.add(mntmAdvancedSetting);

		JMenu mnHelp = new JMenu("Help");
		menuBar.add(mnHelp);

		JMenuItem mntmAbout = new JMenuItem("About");
		mntmAbout.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				AboutUI aboutframe;
				try
				{
					aboutframe = new AboutUI();
					aboutframe.setVisible(true);
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

		JLabel lblInputFile = new JLabel("Input file");
		lblInputFile.setBounds(12, 12, 70, 15);
		contentPane.add(lblInputFile);

		input_text = new JTextField();
		input_text.setBounds(12, 31, 310, 26);
		contentPane.add(input_text);
		input_text.setColumns(10);

		JButton inputbrowse_button = new JButton("Browse");
		inputbrowse_button.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent arg0)
			{
				// TODO browse input
				getInputFile();
			}
		});
		inputbrowse_button.setBounds(334, 31, 117, 25);
		contentPane.add(inputbrowse_button);

		JLabel lblOutputFile = new JLabel("Output directory");
		lblOutputFile.setBounds(12, 66, 151, 15);
		contentPane.add(lblOutputFile);

		output_text = new JTextField();
		output_text.setColumns(10);
		output_text.setBounds(12, 85, 310, 26);
		contentPane.add(output_text);

		JButton outputbrowse_button = new JButton("Browse");
		outputbrowse_button.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				// TODO output browse
				getOutputDir();
			}
		});
		outputbrowse_button.setBounds(334, 85, 117, 25);
		contentPane.add(outputbrowse_button);

		JLabel lblAlgorithms = new JLabel("Algorithms");
		lblAlgorithms.setBounds(12, 202, 151, 15);
		contentPane.add(lblAlgorithms);

		chckbxall = new JCheckBox("All");
		chckbxall.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				if (chckbxall.isSelected())
					enableAll();
				else
					disableAll();
			}
		});
		chckbxall.setBounds(12, 229, 129, 23);
		contentPane.add(chckbxall);

		chckbxFcfs = new JCheckBox("FCFS");
		chckbxFcfs.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				adjustFlags();
			}
		});
		chckbxFcfs.setBounds(12, 256, 70, 23);
		contentPane.add(chckbxFcfs);

		chckbxSjf = new JCheckBox("SJF");
		chckbxSjf.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				adjustFlags();
			}
		});
		chckbxSjf.setBounds(86, 256, 70, 23);
		contentPane.add(chckbxSjf);

		chckbxRr = new JCheckBox("RR");
		chckbxRr.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				adjustFlags();
			}
		});
		chckbxRr.setBounds(160, 256, 70, 23);
		contentPane.add(chckbxRr);

		chckbxHpf = new JCheckBox("HPF (static,non-preemptive)");
		chckbxHpf.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				adjustFlags();
			}
		});
		chckbxHpf.setBounds(228, 256, 231, 23);
		contentPane.add(chckbxHpf);

		chckbxHpfstaticpreemptive = new JCheckBox("HPF (static,preemptive)");
		chckbxHpfstaticpreemptive.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				adjustFlags();
			}
		});
		chckbxHpfstaticpreemptive.setBounds(12, 283, 201, 23);
		contentPane.add(chckbxHpfstaticpreemptive);

		chckbxHpfdynamic = new JCheckBox("HPF (dynamic)");
		chckbxHpfdynamic.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				adjustFlags();
			}
		});
		chckbxHpfdynamic.setBounds(228, 283, 135, 23);
		contentPane.add(chckbxHpfdynamic);

		chckbxWrr = new JCheckBox("WRR");
		chckbxWrr.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				adjustFlags();
			}
		});
		chckbxWrr.setBounds(381, 283, 70, 23);
		contentPane.add(chckbxWrr);

		JButton simulate_button = new JButton("Simulate");
		simulate_button.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				// TODO: simulate button

				try
				{
					pssmang = new PSSManager();
					pssmang.set_inputfile(input_text.getText());
					pssmang.set_OutputDir(output_text.getText());
					cs = Float.parseFloat(contextswitching.getText());
					timeq = Float.parseFloat(timequantum.getText());
					pssmang.setAdditionalInputs(cs, timeq);
					pssmang.setAll_algorithms(false);
					for (int i = 0; i < 7; i++)
					{
						if (run_algorithms[i])
							pssmang.enableAlgorithm(i);
					}

					pssmang.simulate();
					
					OutputUI frame = new OutputUI(pssmang,inptframe);
					frame.setVisible(true);
					
				} catch (PSSexception e1)
				{
					JOptionPane.showMessageDialog(null,
							e1.getDiscription());
				} catch (Exception ex)
				{
					ex.printStackTrace();
					JOptionPane.showMessageDialog(null,
							"please check the input");
				}

			}
		});
		simulate_button.setBounds(176, 314, 117, 25);
		contentPane.add(simulate_button);

		JLabel lblAdditionalInputs = new JLabel("Additional inputs");
		lblAdditionalInputs.setBounds(12, 123, 151, 15);
		contentPane.add(lblAdditionalInputs);

		timequantum = new JTextField("5");
		timequantum.setBounds(32, 171, 114, 19);
		contentPane.add(timequantum);
		timequantum.setColumns(10);

		JLabel lblTimeQuantum = new JLabel("Time quantum");
		lblTimeQuantum.setBounds(22, 150, 151, 15);
		contentPane.add(lblTimeQuantum);

		JLabel lblContextSwitching = new JLabel("Context switching");
		lblContextSwitching.setBounds(300, 150, 151, 15);
		contentPane.add(lblContextSwitching);

		contextswitching = new JTextField("0");
		contextswitching.setColumns(10);
		contextswitching.setBounds(310, 171, 114, 19);
		contentPane.add(contextswitching);

		// link
		linkCheckBoxes();
		
		//input_text.setText("/home/mohamedmayla/Desktop/PSS test input/input.txt");
		//output_text.setText("/home/mohamedmayla/Desktop/PSS test output");
	}

	// file chooser
	private void getInputFile()
	{
		// Create a file chooser
		JFileChooser fc = new JFileChooser();
		// In response to a button click:

		fc.setCurrentDirectory(new java.io.File("."));
		fc.setDialogTitle("Choose input file");

		fc.setFileSelectionMode(JFileChooser.FILES_ONLY);
		// disable the "All files" option.
		fc.setAcceptAllFileFilterUsed(false);

		if (fc.showOpenDialog(fc) == JFileChooser.APPROVE_OPTION)
		{

			in_path = fc.getSelectedFile().getAbsolutePath();
			input_text.setText(in_path);

		}
	}

	private void getOutputDir()
	{
		// Create a file chooser
		JFileChooser fc = new JFileChooser();
		// In response to a button click:

		fc.setCurrentDirectory(new java.io.File("."));
		fc.setDialogTitle("Choose output directory");

		fc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
		// disable the "All files" option.
		fc.setAcceptAllFileFilterUsed(false);

		if (fc.showOpenDialog(fc) == JFileChooser.APPROVE_OPTION)
		{

			out_path = fc.getSelectedFile().getAbsolutePath();
			output_text.setText(out_path);

		}
	}

	// Adjust running algorithms flage
	private void adjustFlags()
	{
		for (int i = 0; i < 7; i++)
		{
			if (chkboxlist[i].isSelected())
				run_algorithms[i] = true;
			else
			{
				run_algorithms[i] = false;
				chckbxall.setSelected(false);
			}
		}
	}

	private void enableAll()
	{
		for (int i = 0; i < 7; i++)
		{
			chkboxlist[i].setSelected(true);
			run_algorithms[i] = true;
		}
	}

	private void disableAll()
	{
		for (int i = 0; i < 7; i++)
		{
			chkboxlist[i].setSelected(false);
			run_algorithms[i] = false;
		}
	}

	private void linkCheckBoxes()
	{
		chkboxlist[0] = chckbxFcfs;
		chkboxlist[1] = chckbxSjf;
		chkboxlist[2] = chckbxRr;
		chkboxlist[3] = chckbxHpf;
		chkboxlist[4] = chckbxHpfstaticpreemptive;
		chkboxlist[5] = chckbxHpfdynamic;
		chkboxlist[6] = chckbxWrr;
	}
}
