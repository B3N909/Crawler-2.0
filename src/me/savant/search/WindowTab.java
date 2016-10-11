package me.savant.search;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import javax.swing.JInternalFrame;
import javax.swing.JTable;
import javax.swing.JTree;
import javax.swing.table.DefaultTableModel;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeNode;

import me.savant.data.ResultElement;
import me.savant.userinterface.EngineType;
import me.savant.userinterface.Google;


public class WindowTab
{
	private static final SearchEngine[] searchEngines = new SearchEngine[] { new Google() };
	private JInternalFrame internalFrame;
	private JTree tree;
	private DefaultTreeModel treeModel;
	private JTable table;
	private DefaultTableModel tableModel;
	private boolean isActive = false;
	
	public WindowTab(JInternalFrame internalFrame, JTree tree, JTable table)
	{
		this.internalFrame = internalFrame;
		this.tree = tree;
		this.table = table;
		this.treeModel = (DefaultTreeModel) tree.getModel();
		this.tableModel = (DefaultTableModel) table.getModel();
	}
	
	public JInternalFrame getInternalFrame()
	{
		return internalFrame;
	}
	
	public JTree getTree()
	{
		return tree;
	}
	
	public JTable getTable()
	{
		return table;
	}
	
	public void query(final String query)
	{
		//TODO: Group together data
		
		isActive = true;
		Thread connectionThread = new Thread()
		{
			public void run()
			{
				for(SearchEngine engine : searchEngines)
				{
					engine.isActive = true;
					engine.reset();
					engine.query(query);
				}
			}
		};
		connectionThread.start();
		
		Thread updateThread = new Thread()
		{
			public void run()
			{
				while(isActive && !internalFrame.isClosed())
				{
					
					
					/** DISPLAY RESULTS IN TABLE **/

					for(Entry<Integer, ResultElement> entry : getData().entrySet())
					{
						tableModel.addRow(new Object[] { entry.getKey(), "Google", entry.getValue().getHeader(), entry.getValue().getLink() });
					}
					
					table.setModel(tableModel);
					table.repaint();
					
					/** DISPLAY RESULTS IN THE TREE **/
					updateTree();
					
					/** CLEANUP DATA - MUST BE AT END **/
					int activeEngines = 0;
					for(SearchEngine engine : searchEngines)
					{
						if(engine.isActive)
							activeEngines++;
						engine.data = new HashMap<Integer, ResultElement>();
					}
					if(activeEngines == 0)
					{
						isActive = false;
						System.out.println("Ended");
					}
					
					/** LOOP **/
					try
					{
						Thread.sleep(500);
					}
					catch (InterruptedException e)
					{
						e.printStackTrace();
					}
				}
				for(SearchEngine engine : searchEngines)
				{
					engine.isActive = false;
				}
			}
		};
		updateThread.start();
	}
	
	private void updateTree()
	{
		TreeNode n = (TreeNode) treeModel.getRoot();
		
		for(SearchEngine engine : searchEngines)
		{
			if(engine.type == EngineType.GOOGLE)
			{
				DefaultMutableTreeNode googleNode = (DefaultMutableTreeNode) n.getChildAt(0);

				for(Entry<Integer, ResultElement> entry : engine.data.entrySet())
				{
					String title = entry.getValue().getHeader() + " (" + entry.getValue().getLink() + ")";
					treeModel.insertNodeInto(new DefaultMutableTreeNode(title), googleNode, googleNode.getChildCount());
				}
			}
			else if(engine.type == EngineType.YAHOO)
			{
				DefaultMutableTreeNode yahooNode = (DefaultMutableTreeNode) n.getChildAt(1);

				for(Entry<Integer, ResultElement> entry : engine.data.entrySet())
				{
					String title = entry.getValue().getHeader() + " (" + entry.getValue().getLink() + ")";
					treeModel.insertNodeInto(new DefaultMutableTreeNode(title), yahooNode, yahooNode.getChildCount());
				}
			}
			else if(engine.type == EngineType.BING)
			{
				DefaultMutableTreeNode bingNode = (DefaultMutableTreeNode) n.getChildAt(2);

				for(Entry<Integer, ResultElement> entry : engine.data.entrySet())
				{
					String title = entry.getValue().getHeader() + " (" + entry.getValue().getLink() + ")";
					treeModel.insertNodeInto(new DefaultMutableTreeNode(title), bingNode, bingNode.getChildCount());
				}
			}
			else
			{
				System.out.println("Unknown Search Engine: " + engine.type.toString());
			}
		}
	}
	
	public Map<Integer, ResultElement> getData()
	{
		Map<Integer, ResultElement> data = new HashMap<Integer, ResultElement>();
		int bestIndex = 0;
		for(SearchEngine engine : searchEngines)
		{
			if(engine.data.size() > bestIndex)
			{
				bestIndex = engine.data.size();
			}
		}
		for(int i = 0; i < bestIndex; i++)
		{
			for(SearchEngine engine : searchEngines)
			{
				if(engine.data.containsKey(i))
				{
					data.put(i, engine.data.get(i));
				}
			}
		}
		return data;
	}
	
	public void pause()
	{
		isActive = false;
	}
	
	
}
