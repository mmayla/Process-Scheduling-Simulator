package gui;

import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JTextField;
import java.awt.Label;
import java.awt.Font;
import java.awt.Color;
import javax.swing.UIManager;
import java.awt.SystemColor;

public class AboutUI extends JFrame
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
					AboutUI frame = new AboutUI();
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
	 * @throws IOException 
	 */
	public AboutUI() throws IOException
	{
		setAlwaysOnTop(true);
		setResizable(false);
		setTitle("About");
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 450, 300);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JLabel lblNewLabel = new JLabel("");
		//lblNewLabel.setIcon(new ImageIcon("images/Infinity_Dream_logo_small.png"));
		lblNewLabel.setIcon(new ImageIcon(getClass().getResource("/images/Infinity_Dream_logo_small.png")));
		lblNewLabel.setBounds(111, 12, 208, 104);
		contentPane.add(lblNewLabel);
		
		Label label = new Label("Mohamed Mayla");
		label.setForeground(Color.BLACK);
		label.setFont(new Font("URW Chancery L", Font.BOLD, 20));
		label.setBounds(31, 169, 199, 21);
		contentPane.add(label);
		
		Label label_1 = new Label("Islam Hashem");
		label_1.setForeground(Color.BLACK);
		label_1.setFont(new Font("URW Chancery L", Font.BOLD, 20));
		label_1.setBounds(268, 169, 145, 21);
		contentPane.add(label_1);
		
		Label label_2 = new Label("Ahmed Alzogby");
		label_2.setForeground(Color.BLACK);
		label_2.setFont(new Font("URW Chancery L", Font.BOLD, 20));
		label_2.setBounds(156, 199, 145, 28);
		contentPane.add(label_2);
		
		Label label_3 = new Label("Team");
		label_3.setForeground(SystemColor.desktop);
		label_3.setFont(new Font("Dialog", Font.BOLD, 20));
		label_3.setBounds(187, 142, 100, 21);
		contentPane.add(label_3);
		
		Label label_4 = new Label("software by");
		label_4.setBounds(31, 74, 88, 21);
		contentPane.add(label_4);
		
	}
}
