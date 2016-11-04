package org.test;

import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.ui.Button;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.TextField;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.ValoTheme;

public class MainForm extends VerticalLayout implements View {

	public static final String NAME = "Main";
	
	private final VerticalLayout messages = new VerticalLayout();
	private final HorizontalLayout sendContainer = new HorizontalLayout();
	private final ComboBox recipient = new ComboBox();
	private final TextField message = new TextField();
	private final Button send = new Button("Send");
	private final Button logout = new Button("Logout");
	
	public MainForm() {
		messages.setSpacing(true);
		
		// Send message UI components
        send.addClickListener(e -> {
        	// Broadcast message
        	Broadcaster.broadcast(((MyUI) UI.getCurrent()).getUser(), (String)recipient.getValue(), message.getValue());
        	// Clear input fields
        	recipient.clear();
        	message.clear();
        });
        sendContainer.addComponents(recipient, message, send);
        sendContainer.setMargin(true);
        sendContainer.setSpacing(true);
        
        // Logout button pressed
        logout.addClickListener(e -> {
        	Broadcaster.broadcastLogout(((MyUI) UI.getCurrent()).getUser());
        });
        
        setCssClasses();
        
        addComponents(messages, sendContainer, logout);
	}
	
	private void setCssClasses() {
		messages.setStyleName("sd-messages-container");
		sendContainer.setStyleName("sd-send-container");
		send.setStyleName(ValoTheme.BUTTON_PRIMARY);
		recipient.setStyleName("sd-recipient");
		message.setStyleName("sd-message");
	}
	
	/**
	 * Called by the UI to notify that a user has logged in and needs adding to the list
	 * @param user user who has logged in
	 */
	public void addRecipient(String user) {
		recipient.addItem(user);
	}
	
	/**
	 * Called by the UI to notify that a user has logged out and needs removed from the list
	 * @param user user who has logged out
	 */
	public void removeRecipient(String user) {
		recipient.removeItem(user);
	}
	
	/**
	 * Called by the UI to notify that a message has been sent to this user
	 * @param message message to display
	 */
	public void addMessage(String sender, String time, String message) {
		VerticalLayout messageContainer = new VerticalLayout();
		messageContainer.setStyleName("msg-label");
		Label from = new Label(sender + " (" + time + "):");
		from.setStyleName("from-label");
		Label msg = new Label(message);
		messageContainer.addComponents(from, msg);
		messages.addComponent(messageContainer);
	}
	
	@Override
	public void enter(ViewChangeEvent event) {
		recipient.addItem("All");
		// Add all logged in users
		recipient.addItems(MyUI.getUsers());
	}
}
