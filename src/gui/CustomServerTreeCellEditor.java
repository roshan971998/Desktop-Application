package gui;

import java.awt.Component;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.MouseEvent;
import java.util.EventObject;

import javax.swing.AbstractCellEditor;
import javax.swing.JCheckBox;
import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreeCellEditor;
import javax.swing.tree.TreePath;

public class CustomServerTreeCellEditor extends AbstractCellEditor implements TreeCellEditor {

	private static final long serialVersionUID = 1L;
	private CustomServerTreeCellRenderer renderer;
	private JCheckBox checkBox;
	private ServerInfo2 info;

	public CustomServerTreeCellEditor() {
		renderer = new CustomServerTreeCellRenderer();
	}

	@Override
	public boolean isCellEditable(EventObject event) {
		// inside this method we need to look which node or branch have been clicked and
		// then we need to say for which node we want to use editor

		if (!(event instanceof MouseEvent))
			return false;

		MouseEvent mouseEvent = (MouseEvent) event;
		
		//now as we know we want to edit only leaf nodes or this method should return true
		//only for leaf nodes(so we can also use getLastSelectedPathComponent() method 
		//but it then lags to give the exact answer sometimes hence we will do it in different
		//way i.e we will trace the mouse click on our tree(so we need to first have the tree    
		//using JTree tree = (JTree) event.getSource();)and we will trace using getPathForLocation
		//() which return a TreePath which is path in tree in form of branches and nodes and the we will 
		// go for last component in that path using getLastPathComponent()
 
		JTree tree = (JTree) event.getSource();
		TreePath path = tree.getPathForLocation(mouseEvent.getX(), mouseEvent.getY());
		if (path == null)
			return false;
		Object lastComponent = path.getLastPathComponent();
		if (lastComponent == null)
			return false;

		DefaultMutableTreeNode treeNode = (DefaultMutableTreeNode) lastComponent;
		// if (treeNode.isLeaf() == false) return false;// or return treeNode.isLeaf();
		// else return true;//because if it s a leaf node then we want to edit it with a
		// component(here it is checkBox in pour case)
		// so we have to return true
		// so that tree will now call getTreeCellEditorComponent method and this method
		// has a return type component
		// and this method will give us the required component we want to edit the node
		// with here checkBoxes
		return treeNode.isLeaf();

	}

	@Override
	public Component getTreeCellEditorComponent(JTree tree, Object value, boolean isSelected, boolean expanded,
			boolean leaf, int row) {
		Component component = renderer.getTreeCellRendererComponent(tree, value, isSelected, expanded, leaf, row, true);

		if (leaf) {

			DefaultMutableTreeNode treeNode = (DefaultMutableTreeNode) value;
			info = (ServerInfo2) treeNode.getUserObject();

			checkBox = (JCheckBox) component;

			ItemListener itemListener = new ItemListener() {
				@Override
				public void itemStateChanged(ItemEvent e) {
					fireEditingStopped();
					checkBox.removeItemListener(this);
					// checkBox.removeItemListener(itemListener);//illegal to use this as item
					// listener is at the moment local variable

				}
			};
			checkBox.addItemListener(itemListener);// the main motive inside this function is to call
			// fireEditingStoppped method
			// and after that we know that tree will call getcellEditor value to set the
			// edited value to checkBox(our rendered component for leaf nodes).
			// so in case of checkBox we know it can be only ticked or unTicked
			// hence as soon as the state of checkBox changes we need to stop editing
			// (thus we need to call fireEditingStopped() method inside the item listener
			// and this will tell the tree to call getCellEditorValue()) and set the
			// checkBox
			// as ticked or unTicked(but this has to be done by tree so we do not need to
			// set the checkBox as ticked or unTicked through code ,tree will do it itself
			// by calling getCellEditorValue() method).

		} // if its a leaf then only we can cast that into JCheckBox else we can't and
			// hence we are not doing else part here

		return component;
	}

	@Override
	public Object getCellEditorValue() {
		// inside this function we need to return our server info object and we can get
		// server info object from getTreeCellEditorComponent() method thats  why we have
		// used the following code in getTreeCellEditorComponent()
		// DefaultMutableTreeNode treeNode = (DefaultMutableTreeNode) value;
		// info = (ServerInfo2) treeNode.getUserObject();

		info.setchecked(checkBox.isSelected());//but before returning the server info object we want to 
        //update the info object with latest information from checkBox
		return info;
	}

}
