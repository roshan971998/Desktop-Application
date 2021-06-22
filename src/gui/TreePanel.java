package gui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;
import java.util.concurrent.ExecutionException;

import javax.swing.DefaultListModel;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTree;
import javax.swing.SwingWorker;
import javax.swing.event.CellEditorListener;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreeSelectionModel;

import controller.MessageServer;
import model.Message;

public class TreePanel extends JPanel implements ProgressDialogListener {

	private static final long serialVersionUID = 1L;
	private JTree serverTree;
	private CustomServerTreeCellRenderer customTreeCellRenderer;
	private CustomServerTreeCellEditor customServerTreeCellEditor;
	private ProgressDialog progressDialog;

	private Set<Integer> selectedServers;
	private MessageServer messageServer;
	private SwingWorker<List<Message>, Integer> worker;

	private TextPanel textPanel;
	private JList<Message> messageList;
	private JSplitPane upperpane;
	private JSplitPane lowerpane;

	private DefaultListModel<Message> messagesListModel;

	public TreePanel(JFrame parent) {

		messagesListModel = new DefaultListModel<>();

		progressDialog = new ProgressDialog(parent, "Messages Downloading...");

		messageServer = new MessageServer();

		progressDialog.setListener(this);

		selectedServers = new TreeSet<Integer>();// we have to keep the declaration of set i.e selected servers here
													// only
		selectedServers.add(1);// just before the creation of nodes as for creation of nodes on line 36 JTree
								// calls createTree()method
		//selectedServers.add(5);// in which DMTN objects calls selectedServers.contains()method hence if set or
								// selectedServers should
		//selectedServers.add(3);// be initialized before any function i.e contains() is called else we get Null
								// Pointer Exception

		customTreeCellRenderer = new CustomServerTreeCellRenderer();
		customServerTreeCellEditor = new CustomServerTreeCellEditor();

		serverTree = new JTree(createTree());
		serverTree.setCellRenderer(customTreeCellRenderer);
		serverTree.setCellEditor(customServerTreeCellEditor);
		serverTree.setEditable(true);// in order to make our tree really editable
		serverTree.getSelectionModel().setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);
		messageServer.setSelectedServers(selectedServers);

//		serverTree.addTreeSelectionListener(new TreeSelectionListener() {
//			@Override
//			public void valueChanged(TreeSelectionEvent e) {
//				DefaultMutableTreeNode node = (DefaultMutableTreeNode) serverTree.getLastSelectedPathComponent();
//				Object userObject = node.getUserObject();
//				if (userObject instanceof ServerInfo2) {
//					int id = ((ServerInfo2) userObject).getId();
//					System.out.println(id);
//				}
//				System.out.println(userObject);
//			}
//		});
        //we have called fireEditingStoppped() method in CustomServerTreeCellEditor class 
		//getTreeCellEditorComponent() method and thats the event we will now listen for
		// that is whenever we tick or unTick the checkBox fireEditingStoppped()
		//will be called and then it will call editingStopped() method below here 
		customServerTreeCellEditor.addCellEditorListener(new CellEditorListener() {
			@Override
			public void editingCanceled(ChangeEvent e) {
			}

			@Override
			public void editingStopped(ChangeEvent e) {
				ServerInfo2 info = (ServerInfo2) customServerTreeCellEditor.getCellEditorValue();
				// System.out.println(info +" : "+info.getId()+" "+info.isIschecked());

				int serverID = info.getId();

				if (info.ischecked()) {
					selectedServers.add(serverID);
				} else {
					selectedServers.remove(serverID);
				}
				messageServer.setSelectedServers(selectedServers);
				
//              now we want to fetch the messages so we have 3 ways
				
				
//(1)			for (Message message : messageServer.getSelectedMessages()) {
//					
//					System.out.println(message.getTitle());
//				}
				
//				or we can make use of Iterable property of MessageServer class 
//(2)			for (Message message : messageServer) {
//					
//					System.out.println(message.getTitle());
//				}
				
//				but both the above methods make our application slow hence we need to execute this code in separate thread 
//              so we are creating a method for this code now 
//(3)
				retrieveMessages();
			}

		});

		setLayout(new BorderLayout());

		textPanel = new TextPanel();
		messageList = new JList<>(messagesListModel);

		messageList.setCellRenderer(new MessageListRenderer());
		messageList.addListSelectionListener(new ListSelectionListener() {
			@Override
			public void valueChanged(ListSelectionEvent e) {
				Message currentMessage = (Message) messageList.getSelectedValue();
				//wait().atMost(10, SECONDS).until(() -> currentMessage!=null);
//				while(currentMessage == null) {
//					try {
//						Thread.sleep(1000);
//						break;
//					} catch (InterruptedException ie) {
//						ie.printStackTrace();
//					}
//				}
				System.out.println(currentMessage);
				//textPanel.setText(currentMessage.getContents());
			}
		});

		lowerpane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, new JScrollPane(messageList),
				new JScrollPane(textPanel));
		upperpane = new JSplitPane(JSplitPane.VERTICAL_SPLIT, new JScrollPane(serverTree), lowerpane);

		textPanel.setMinimumSize(new Dimension(50, 130));
		messageList.setMinimumSize(new Dimension(50, 130));

		lowerpane.setResizeWeight(0.4750);
		upperpane.setResizeWeight(0.4);

		add(upperpane, BorderLayout.CENTER);
	}

	public void refresh() {
		retrieveMessages();
	}

	private void retrieveMessages() {
		// System.out.println("Messages waiting "+ messageServer.getMessageCount());

		progressDialog.setMaximum(messageServer.getMessageCount());// we also need to set the maximum value i progress
																	// bar
		progressDialog.setVisible(true);

		worker = new SwingWorker<List<Message>, Integer>() {
			@Override
			protected List<Message> doInBackground() throws Exception {

				List<Message> retrievedMessages = new ArrayList<Message>();

				int count = 0;

				for (Message message : messageServer) {
					
					if (isCancelled()) break;
					// System.out.println(message.getTitle());
					retrievedMessages.add(message);
					count++;
					publish(count);
				//whatever the things we publish is received by process() method and it takes the 
				//published thing in form of list i.e we 1st publish (count=)1 then 2 ten 3 and so on 
				//but then we know process is not called everyTime we call publish so let say process
				//is called after count = 3 then list passed to process is [1,2,3] and since count =3
				//means we have retrieved 3 messages till now and hence to show that thing we use 
				//counts.size()-1 as 3 in this list is at index 2.so whenever process is called it 
				//need to access only the last element of list 	
				}
				return retrievedMessages;
			}

			@Override
			protected void process(List<Integer> counts) {
				int retrieved = counts.get(counts.size() - 1);//counts.size()-1 is las argument in the list 
				// System.out.println("Got : "+retrieved +" messages");
				// above SwiingUtilities invoke later method make progress bar appear at right
				// time and
				// in done method we set the progress bar to invisible after the thread has done
				// its work
				// now here in this method we need to make progress bar functional i.e we have
				// to add values to bar
				progressDialog.setValue(retrieved);
			}

			@Override
			protected void done() {// this method is called in last by the thread when it is done and at last it
									// have to return list of messages it processed through get()method
				progressDialog.setVisible(false);
				if (isCancelled())
					return;

				try {

					List<Message> retrievedMessages = get();

					messagesListModel.removeAllElements();

					for (Message message : retrievedMessages) {
						// messagesListModel.addElement(message.getTitle());
						messagesListModel.addElement(message);
					}
					//messageList.setSelectedIndex(0);

					// System.out.println("Retrieved " +retrievedMessages.size() + " messages");
				} catch (InterruptedException | ExecutionException e) {
					e.printStackTrace();
				}

			}

		};
		worker.execute();

	}

	private DefaultMutableTreeNode createTree() {
		DefaultMutableTreeNode top = new DefaultMutableTreeNode("servers");

		DefaultMutableTreeNode branch1 = new DefaultMutableTreeNode("USA");
//		DefaultMutableTreeNode server1 = new DefaultMutableTreeNode(new ServerInfo2("New York", 1,false));
//		DefaultMutableTreeNode server2 = new DefaultMutableTreeNode(new ServerInfo2("Boston", 2,false));
//		DefaultMutableTreeNode server3 = new DefaultMutableTreeNode(new ServerInfo2("Los Angles", 3,false));
		DefaultMutableTreeNode server1 = new DefaultMutableTreeNode(
				new ServerInfo2("New York", 1, selectedServers.contains(1)));
		DefaultMutableTreeNode server2 = new DefaultMutableTreeNode(
				new ServerInfo2("Boston", 2, selectedServers.contains(2)));
		DefaultMutableTreeNode server3 = new DefaultMutableTreeNode(
				new ServerInfo2("Los Angles", 3, selectedServers.contains(3)));
		branch1.add(server1);
		branch1.add(server2);
		branch1.add(server3);
		DefaultMutableTreeNode branch2 = new DefaultMutableTreeNode("UK");
		DefaultMutableTreeNode server4 = new DefaultMutableTreeNode(
				new ServerInfo2("London", 4, selectedServers.contains(4)));
		DefaultMutableTreeNode server5 = new DefaultMutableTreeNode(
				new ServerInfo2("Edinburgh", 5, selectedServers.contains(5)));
		branch2.add(server4);
		branch2.add(server5);

		top.add(branch1);
		top.add(branch2);
		return top;
	}

	@Override
	public void progressDialogCancelled() {
		if (worker != null) {
			worker.cancel(true);
		}

	}

}

class ServerInfo2 {
	private String name;
	Integer id;
	private boolean checked;

	public boolean ischecked() {
		return checked;
	}

	public void setchecked(boolean checked) {
		this.checked = checked;
	}

	public ServerInfo2(String name, int id, boolean checked) {
		super();
		this.name = name;
		this.id = id;
		this.checked = checked;
	}

	public int getId() {
		return id;
	}

	@Override
	public String toString() {
		return name;
	}

}
