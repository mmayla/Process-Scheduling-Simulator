package gui;

import java.awt.BorderLayout;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JLabel;
import javax.swing.JRadioButton;
import javax.swing.JButton;

public class AdvancedSettingsUI extends JFrame
{

	private JPanel contentPane;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args)
	{
		EventQueue.invokeLater(new Runnable()
		{
			public void run()
			{
				try
				{
					AdvancedSettingsUI frame = new AdvancedSettingsUI();
					frame.setVisible(true);
				} catch (Exception e)
				{
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public AdvancedSettingsUI()
	{
		setResizable(false);
		setTitle("Advanced Settings");
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 450, 300);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JLabel lblNewLabel = new JLabel("The time spends on a background is considered");
		lblNewLabel.setBounds(12, 12, 424, 15);
		contentPane.add(lblNewLabel);
		
		JRadioButton rdbtnNewRadioButton = new JRadioButton(" Idle time");
		rdbtnNewRadioButton.setBounds(70, 35, 149, 23);
		contentPane.add(rdbtnNewRadioButton);
		
		JRadioButton rdbtnNewRadioButton_1 = new JRadioButton("Busy time");
		rdbtnNewRadioButton_1.setBounds(223, 35, 149, 23);
		contentPane.add(rdbtnNewRadioButton_1);
		
		JButton btnApply = new JButton("Apply");
		btnApply.setBounds(319, 234, 117, 25);
		contentPane.add(btnApply);
	}

}
