package controller;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import model.Message;

/*
 * This is sort of Some simulated Message Server
 */

public class MessageServer implements Iterable<Message> {

	private Map<Integer, List<Message>> messages;
	private List<Message> selected;// selected is basically the Array list of messages from all the severs we have
									// selected.

	public MessageServer() {
		selected = new ArrayList<Message>();
		messages = new TreeMap<Integer, List<Message>>();

		// lets add some dummy messages on server 1

		List<Message> list = new ArrayList<>();
		list.add(new Message("The Cat is missing", "Have you seen Felix anywhere?"));
		list.add(new Message("See you Later", "Are we still meeting in the pub?"));
		messages.put(1, list);

		// some dummy messages on server 2
		list = new ArrayList<>();
		list.add(new Message("All personnel to deck 5", "An emergency has arisen"));
		list.add(new Message("Database restart", "The database will be restarted at 6pm"));
		list.add(new Message("how about dinner later?", "Are you doing something later on?"));
		messages.put(2, list);

		// some dummy messages on server 3
		list = new ArrayList<>();
		list.add(new Message("I love you ", "come back soon"));
		messages.put(3, list);

		// some dummy messages on server 4
		list = new ArrayList<>();
		list.add(new Message("How many times can a man look up",
				"Before he can see the sky? More on that story later."));
		list.add(new Message("Company policy", "Please do not talk to the journalists outside."));
		list.add(new Message("New update schedule",
				"From now on we will be updating the codebase every night at 1 a.m."));
		messages.put(4, list);

		// some dummy messages on server 5
		list = new ArrayList<Message>();
		list.add(new Message("Haggis available", "Free haggis at reception."));
		list.add(new Message("Team building event",
				"There will be a team building event at the weekend. All employees must attend. Please ensure your life insurance is up to date before attending."));
		list.add(new Message("Desk policy", "Please do not take your desks home with you. They are company property."));
		list.add(new Message("Vending machine",
				"The coffee in the vending machine has been found to be contaminated with faeces. Please moderate your intake."));
		messages.put(5, list);
	}

	public void setSelectedServers(Set<Integer> servers) {// here we pass in the set of server ids
		selected.clear();
		for (Integer id : servers) {
			if (messages.containsKey(id)) {
				selected.addAll(messages.get(id));// or List<Message> serverMessage = messages.get(id);
												  // selected.addAll(serverMessage);
			}
		}
	}

	public Integer getMessageCount() {
		return selected.size();
	}
//	public List<Message> getSelectedMessages(){
//		return selected;
//	}

	@Override
	public Iterator<Message> iterator() {
		return new MessageIterator(selected);
	}
}

class MessageIterator implements Iterator {

	private Iterator<Message> iterator;

	public MessageIterator(List<Message> messages) {
		iterator = messages.iterator();
	}

	@Override
	public boolean hasNext() {
		return iterator.hasNext();
	}

	@Override
	public Object next() {
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			// e.printStackTrace();
		}
		return iterator.next();
	}

	@Override
	public void remove() {
		iterator.remove();
	}

}