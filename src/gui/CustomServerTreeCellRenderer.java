package gui;

import java.awt.Color;
import java.awt.Component;
import java.net.URL;

import javax.swing.ImageIcon;
import javax.swing.JCheckBox;
import javax.swing.JTree;
import javax.swing.UIManager;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;
import javax.swing.tree.TreeCellRenderer;

public class CustomServerTreeCellRenderer implements TreeCellRenderer {

	private JCheckBox leafRenderer;// to render leaf nodes
	private DefaultTreeCellRenderer nonLeafRenderer;// to render non leaf nodes

	private Color textForeground;
	private Color textBackground;
	private Color selectionForeground;
	private Color selectionBackground;

	public CustomServerTreeCellRenderer() {
		leafRenderer = new JCheckBox();
		nonLeafRenderer = new DefaultTreeCellRenderer();
		
		//setting icon only on nonLeaf nodes  
		nonLeafRenderer.setIcon(createIcon("/images/Server16.gif")); // for all leaf or for all lower level
																		// nodes(i.e leafs)
		nonLeafRenderer.setOpenIcon(createIcon("/images/WebComponent16.gif"));// for all open nodes
		nonLeafRenderer.setClosedIcon(createIcon("/images/WebComponentAdd16.gif"));// for all closed nodes

		textForeground = UIManager.getColor("Tree.textForeground");
		textBackground = UIManager.getColor("Tree.textBackground");
		selectionForeground = UIManager.getColor("Tree.selectionForeground");
		selectionBackground = UIManager.getColor("Tree.selectionBackground");

	}

	private ImageIcon createIcon(String path) {
		URL url = getClass().getResource(path);
		if (url == null)
			System.out.println("Unable to laod Images " + path);
		ImageIcon icon = new ImageIcon(url);
		return icon;

	}

	@Override
	public Component getTreeCellRendererComponent(JTree tree, Object value, boolean selected, boolean expanded,
			boolean leaf, int row, boolean hasFocus) {
		// so if it is a leaf node then we will return our special component i.e
		// CheckBox else we return the DefaultTreeCellrender set in TreeCellRenderer
		// class for nodes
		if (leaf) {

			DefaultMutableTreeNode node = (DefaultMutableTreeNode) value;
			ServerInfo2 nodeInfo = (ServerInfo2) node.getUserObject();

			if (selected) {
				leafRenderer.setForeground(selectionForeground);
				leafRenderer.setBackground(selectionBackground);
			} else {
				leafRenderer.setForeground(textForeground);
				leafRenderer.setBackground(textBackground);
			}

			leafRenderer.setText(nodeInfo.toString());
			leafRenderer.setSelected(nodeInfo.ischecked());

			return leafRenderer;

		} else {

			return nonLeafRenderer.getTreeCellRendererComponent(tree, value, selected, expanded, leaf, row, hasFocus);
		}
	}

}
