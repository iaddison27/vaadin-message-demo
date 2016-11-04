package org.test;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import javax.servlet.annotation.WebServlet;

import com.vaadin.annotations.Push;
import com.vaadin.annotations.Theme;
import com.vaadin.annotations.VaadinServletConfiguration;
import com.vaadin.navigator.Navigator;
import com.vaadin.server.VaadinRequest;
import com.vaadin.server.VaadinServlet;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;

@Theme("mytheme")
@Push
public class MyUI extends UI implements BroadcastListener {

	private final static List<String> users = new CopyOnWriteArrayList<>();
	private String username;
	
	private final LoginForm loginForm = new LoginForm();
	private final MainForm mainForm = new MainForm();
	
	@WebServlet(urlPatterns = "/*", name = "MyUIServlet", asyncSupported = true)
    @VaadinServletConfiguration(ui = MyUI.class, productionMode = false)
    public static class MyUIServlet extends VaadinServlet {
    }
	
    @Override
    protected void init(VaadinRequest vaadinRequest) {
    	
    	final VerticalLayout layout = new VerticalLayout();
		layout.setMargin(true);
		layout.setSpacing(true);
		layout.setSizeFull();
		setContent(layout);
		
    	CssLayout viewArea = new CssLayout();
		viewArea.setSizeFull();
		layout.addComponent(viewArea);
		
    	setNavigator(new Navigator(this, viewArea));
		getNavigator().addView(LoginForm.NAME, loginForm);
		getNavigator().addView(MainForm.NAME, mainForm);
		getNavigator().navigateTo(LoginForm.NAME);
    }
    
    public static List<String> getUsers() {
    	return users;
    }
    
    public String getUser() {
		return username;
	}

	public void setUser(String username) {
    	this.username = username;
    	users.add(username);
    	
    	// Register this UI as a listener
    	Broadcaster.register(username, this);
    	Broadcaster.broadcastLogin(username);
    }
    
    /**
     * Unregister this listener when the UI expires
     * TODO: test this works
     */
    @Override
    public void detach() {
        Broadcaster.unregister(username);
        super.detach();
    }

	@Override
	public void receiveBroadcast(String sender, String time, String message) {
		
		access(() -> {
			// Show the message
        	mainForm.addMessage(sender, time, message);
		});
	}
	
	@Override
	public void receiveBroadcastLogin(String username) {
		access(() -> {
			// Ensure the user is added to the list of available users
    		mainForm.addRecipient(username);
		});
	}
	
	@Override
	public void receiveBroadcastLogout(String username) {
		final String thisUser = this.username;
		access(() -> {
            // Ensure the user that logged out is removed to the list of available users
        	mainForm.removeRecipient(username);
        	
        	// If this UI belongs to the logged out user
        	if (thisUser.equals(username)) {
            	// Unregister the BroadcastListener for this user
            	Broadcaster.unregister(username);
            	// Navigate to the LoginForm
            	UI.getCurrent().getNavigator().navigateTo(LoginForm.NAME);
        	}
        });		
	}
}
