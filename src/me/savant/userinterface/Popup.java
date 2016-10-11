package me.savant.userinterface;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

public class Popup extends JDialog
{

	private final JPanel contentPanel = new JPanel();
	
	public static void Popup(String text)
	{
		new Popup(text);
	}

	public Popup(String message)
	{
		setTitle("Error");
		setAlwaysOnTop(true);
		setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		setResizable(false);
		setIconImage(Toolkit.getDefaultToolkit().getImage(Popup.class.getResource("/com/sun/javafx/scene/control/skin/caspian/dialog-error@2x.png")));
		setBounds(100, 100, 387, 115);
		getContentPane().setLayout(new BorderLayout());
		setLocationRelativeTo(null);
		contentPanel.setLayout(new FlowLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		{
			JLabel lblThisIsA = new JLabel(message);
			contentPanel.add(lblThisIsA);
		}
		{
			JPanel buttonPane = new JPanel();
			getContentPane().add(buttonPane, BorderLayout.SOUTH);
			buttonPane.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
			{
				JButton button = new JButton("Ok");
				button.addActionListener(new ActionListener()
				{
					@Override
					public void actionPerformed(ActionEvent e)
					{
						setVisible(false);
						dispose();
					}
					
				});
				
				buttonPane.add(button);
			}
		}
		setVisible(true);
	}

}
