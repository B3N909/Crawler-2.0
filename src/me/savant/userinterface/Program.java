package me.savant.userinterface;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.SystemColor;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyVetoException;
import java.util.ArrayList;

import javax.swing.AbstractAction;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JInternalFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JSlider;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.JTree;
import javax.swing.KeyStroke;
import javax.swing.border.TitledBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;

import me.savant.search.WindowTab;
import javax.swing.JSeparator;
import javax.swing.SwingConstants;
import javax.swing.JSplitPane;

public class Program
{
	private JFrame frmCrawler;
	private JTextField searchText;
	private JButton searchButton;
	private JPanel freeformPanel;
	private ArrayList<WindowTab> windows = new ArrayList<WindowTab>();
	
	public static void main(String[] args)
	{
		Program window = new Program();
		window.frmCrawler.setVisible(true);
	}

	public Program()
	{
		initialize();
	}

	private void initialize()
	{
		/** MENU **/
		frmCrawler = new JFrame();
		frmCrawler.setTitle("Crawler 2.0");
		frmCrawler.setIconImage(Toolkit.getDefaultToolkit().getImage("C:\\Users\\Ben\\workspace6\\Crawler\\res\\placeholder_icon.png"));
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		frmCrawler.setBounds(100, 100, (int) screenSize.getWidth(), (int) screenSize.getHeight());
		frmCrawler.setLocationRelativeTo(null);

		frmCrawler.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		JMenuBar menuBar = new JMenuBar();
		menuBar.setBackground(SystemColor.control);
		frmCrawler.setJMenuBar(menuBar);
		
		JMenu mnFile = new JMenu("File");
		mnFile.setBackground(SystemColor.control);
		menuBar.add(mnFile);
		
		JMenuItem mntmClose = new JMenuItem("Close");
		mntmClose.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F10, 0));
		mnFile.add(mntmClose);
		
		JPanel topPanel = new JPanel();
		menuBar.add(topPanel);
		topPanel.setBackground(SystemColor.control);
		
		searchButton = new JButton("Search");
		searchButton.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				WindowTab selected = getSelected();
				if(selected == null)
				{
					java.awt.Toolkit.getDefaultToolkit().beep();
					Popup.Popup("You have no window selected!");
				}
				else
				{
					getSelected().query(searchText.getText());
				}
			}
		});
		topPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
		topPanel.add(searchButton);
		
		searchText = new JTextField();
		topPanel.add(searchText);
		searchText.setColumns(10);
		searchText.addActionListener(new AbstractAction()
		{
			@Override
			public void actionPerformed(ActionEvent arg0)
			{
				searchButton.doClick();
			}
		});
		frmCrawler.getContentPane().setLayout(null);
		
		freeformPanel = new JPanel(); /** THIS IS THE PROBLEM **/
		freeformPanel.setBounds(0, 0, 1904, 993);
		frmCrawler.getContentPane().add(freeformPanel);
		freeformPanel.setLayout(null);
		
		JButton createWindowButton = new JButton("Create Window");
		createWindowButton.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{
				createWindow();
			}
		});
		topPanel.add(createWindowButton);
		
		/** OPTIONS WINDOW **/
		JInternalFrame options = new JInternalFrame("Options");
		options.setFrameIcon(new ImageIcon(Program.class.getResource("/com/sun/java/swing/plaf/windows/icons/Question.gif")));
		options.setBounds(708, 302, 257, 245);
		freeformPanel.add(options);
		
		JPanel panel_1 = new JPanel();
		options.getContentPane().add(panel_1, BorderLayout.CENTER);
		panel_1.setLayout(null);
		
		final JRadioButton updateContent = new JRadioButton("Continously Update Content");
		updateContent.setBounds(6, 7, 229, 23);
		updateContent.addChangeListener(new ChangeListener()
		{
			@Override
			public void stateChanged(ChangeEvent e)
			{
				CONTINOUSLY_LOAD = updateContent.isSelected();
			}
		});
		panel_1.add(updateContent);
		
		JLabel lblContentLoadAmount = new JLabel("Content Load Amount");
		lblContentLoadAmount.setBounds(6, 56, 219, 14);
		panel_1.add(lblContentLoadAmount);
		
		
		final JLabel sliderLabel = new JLabel("1");
		sliderLabel.setBounds(189, 82, 46, 14);
		panel_1.add(sliderLabel);
		
		final JSlider slider = new JSlider();
		slider.setMinimum(1);
		slider.setPaintLabels(true);
		slider.setSnapToTicks(true);
		slider.setMaximum(4);
		slider.setValue(1);
		slider.setBounds(6, 76, 173, 29);
		slider.addChangeListener(new ChangeListener()
		{
			@Override
			public void stateChanged(ChangeEvent e)
			{
				sliderLabel.setText(slider.getValue() + "");
				LOAD_AMOUNT = slider.getValue();
			}
		});
		
		panel_1.add(slider);
		options.setVisible(true);
		
		/** WINDOWS **/
		createWindow();
	}
	
	public static boolean CONTINOUSLY_LOAD = false;
	public static int LOAD_AMOUNT = 0;
	
	public void createWindow()
	{
		final JInternalFrame tab1 = new JInternalFrame("New Window");
		tab1.setBounds(97, 449, 642, 533);
		freeformPanel.add(tab1);
		tab1.setMaximizable(true);
		tab1.setResizable(true);
		tab1.setFocusable(true);
		tab1.addMouseListener(new MouseListener()
		{
			@Override
			public void mouseClicked(MouseEvent e)
			{
				tab1.toFront();
				tab1.repaint();	
			}

			@Override
			public void mouseEntered(MouseEvent e)
			{
				tab1.toFront();
				tab1.repaint();
								
				deselectAll(tab1);
				select(tab1);
			}

			@Override
			public void mouseExited(MouseEvent e)
			{
				tab1.toBack();
				tab1.repaint();
			}

			@Override
			public void mousePressed(MouseEvent e)
			{
				tab1.toFront();
				tab1.repaint();
				
				deselectAll(tab1);
				select(tab1);
			}

			@Override
			public void mouseReleased(MouseEvent e) {}
		});
		tab1.setFrameIcon(new ImageIcon(Program.class.getResource("/com/sun/javafx/scene/control/skin/caspian/dialog-fewer-details.png")));
		tab1.setClosable(true);
		GridBagLayout gridBagLayout = new GridBagLayout();
		gridBagLayout.columnWidths = new int[]{603, 0};
		gridBagLayout.rowHeights = new int[]{0, 0, 0};
		gridBagLayout.columnWeights = new double[]{1.0, Double.MIN_VALUE};
		gridBagLayout.rowWeights = new double[]{1.0, 1.0, Double.MIN_VALUE};
		tab1.getContentPane().setLayout(gridBagLayout);
		tab1.setVisible(true);
		
		JSplitPane splitPane = new JSplitPane();
		splitPane.setContinuousLayout(true);
		splitPane.setOneTouchExpandable(true);
		splitPane.setResizeWeight(0.35);
		GridBagConstraints gbc_splitPane = new GridBagConstraints();
		gbc_splitPane.gridheight = 2;
		gbc_splitPane.fill = GridBagConstraints.BOTH;
		gbc_splitPane.gridx = 0;
		gbc_splitPane.gridy = 0;
		tab1.getContentPane().add(splitPane, gbc_splitPane);
		
		JScrollPane treeScroll = new JScrollPane();
		splitPane.setLeftComponent(treeScroll);
		
		JScrollPane tableScroll = new JScrollPane();
		splitPane.setRightComponent(tableScroll);
		
		/** PREVIOUS CODE **/
		JTree tree = new JTree();
		tree.setModel(new DefaultTreeModel(
			new DefaultMutableTreeNode("Results") {
				{
					add(new DefaultMutableTreeNode("Google"));
					add(new DefaultMutableTreeNode("Yahoo"));
					add(new DefaultMutableTreeNode("Bing"));
				}
			}
		));
		tree.setShowsRootHandles(true);
		tree.setEditable(true);
		treeScroll.setViewportView(tree);
		
		JTable table_1 = new JTable();
		table_1.setModel(new DefaultTableModel(
			new Object[][] {
			},
			new String[] {
				"Relevance", "Engine", "Header", "Site"
			}
		) {
			Class[] columnTypes = new Class[] {
				Integer.class, String.class, String.class, String.class
			};
			public Class getColumnClass(int columnIndex) {
				return columnTypes[columnIndex];
			}
		});
		table_1.setBorder(new TitledBorder(null, "", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		tableScroll.setViewportView(table_1);
		
		windows.add(new WindowTab(tab1, tree, table_1));
	}
	
	public void deselectAll(JInternalFrame whitelist)
	{
		for(WindowTab window : windows)
		{
			JInternalFrame frame = window.getInternalFrame();
			if(frame.isSelected() && frame.getX() != whitelist.getX() && frame.getY() != whitelist.getY())
			{
				try
				{
					frame.setSelected(false);
				}
				catch (PropertyVetoException e)
				{
					e.printStackTrace();
				}
			}
		}
	}
	
	public void select(JInternalFrame frame)
	{
		try
		{
			frame.setSelected(true);
		}
		catch (PropertyVetoException e)
		{
			e.printStackTrace();
		}
	}
	
	public WindowTab getSelected()
	{
		for(WindowTab window : windows)
		{
			if(window.getInternalFrame().isSelected())
			{
				return window;
			}
		}
		return null;
	}
}
