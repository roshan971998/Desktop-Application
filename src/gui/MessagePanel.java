package gui;

import java.awt.BorderLayout;
import java.net.URL;

import javax.swing.ImageIcon;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTree;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;
import javax.swing.tree.TreeSelectionModel;

public class MessagePanel extends JPanel {

	private static final long serialVersionUID = 1L;
	private JTree serverTree;
	private DefaultTreeCellRenderer treeCellRenderer;// DefaultTreeCellRenderer is used to set icons on the nodes and
														// leafs

	public MessagePanel() {
		setLayout(new BorderLayout());
		
		treeCellRenderer = new DefaultTreeCellRenderer();
		serverTree = new JTree(createTree());
		serverTree.getSelectionModel().setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);
		serverTree.addTreeSelectionListener(new TreeSelectionListener() {
			@Override
			public void valueChanged(TreeSelectionEvent e) {
				DefaultMutableTreeNode node = (DefaultMutableTreeNode) serverTree.getLastSelectedPathComponent();
				Object userObject = node.getUserObject();
				if (userObject instanceof ServerInfo) {
					int id = ((ServerInfo) userObject).getId();
					System.out.println(id);
				}
				System.out.println(userObject);
			}
		});

		
		serverTree.setCellRenderer(treeCellRenderer);
		treeCellRenderer.setLeafIcon(createIcon("/images/Server16.gif")); // for all leaf or for all lower level
																			// nodes(i.e leafs)
		treeCellRenderer.setOpenIcon(createIcon("/images/WebComponent16.gif"));// for all open nodes
		treeCellRenderer.setClosedIcon(createIcon("/images/WebComponentAdd16.gif"));// for all closed nodes

		add(new JScrollPane(serverTree), BorderLayout.CENTER);
	}

	private ImageIcon createIcon(String path) {
		URL url = getClass().getResource(path);
		if (url == null)
			System.out.println("Unable to laod Images " + path);
		ImageIcon icon = new ImageIcon(url);
		return icon;

	}

	private DefaultMutableTreeNode createTree() {
		DefaultMutableTreeNode top = new DefaultMutableTreeNode("servers");

		DefaultMutableTreeNode branch1 = new DefaultMutableTreeNode("USA");
		DefaultMutableTreeNode server1 = new DefaultMutableTreeNode(new ServerInfo("New York", 1));
		DefaultMutableTreeNode server2 = new DefaultMutableTreeNode(new ServerInfo("Boston", 2));
		DefaultMutableTreeNode server3 = new DefaultMutableTreeNode(new ServerInfo("Los Angeles", 3));
		branch1.add(server1);
		branch1.add(server2);
		branch1.add(server3);
		DefaultMutableTreeNode branch2 = new DefaultMutableTreeNode("UK");
		DefaultMutableTreeNode server4 = new DefaultMutableTreeNode(new ServerInfo("London", 4));
		DefaultMutableTreeNode server5 = new DefaultMutableTreeNode(new ServerInfo("Edinburgh", 5));
		branch2.add(server4);
		branch2.add(server5);

		top.add(branch1);
		top.add(branch2);
		return top;
	}

}

class ServerInfo {
	private String name;
	int id;

	public ServerInfo(String name, int id) {
		super();
		this.name = name;
		this.id = id;
	}

	public int getId() {
		return id;
	}

	@Override
	public String toString() {
		return name;
	}

}
