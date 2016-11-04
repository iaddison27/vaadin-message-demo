package org.test;

import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.ui.Button;
import com.vaadin.ui.PasswordField;
import com.vaadin.ui.TextField;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.ValoTheme;

public class LoginForm extends VerticalLayout implements View {
	
	public static final String NAME = "Login";

	public LoginForm() {
		final TextField username = new TextField("Name");
		final PasswordField password = new PasswordField("Password");

		// Login button
		final Button login = new Button("Login");
		login.setStyleName(ValoTheme.BUTTON_PRIMARY);
	    login.addClickListener(e -> {
			// Inform the UI the user has logged in
			((MyUI) UI.getCurrent()).setUser(username.getValue());

			// Navigate to the MainForm
			UI.getCurrent().getNavigator().navigateTo(MainForm.NAME);
			
			// Clear the password field for security
			password.clear();
		});
		
	    addComponents(username, password, login);
	}

	@Override
	public void enter(ViewChangeEvent event) {
		// TODO Auto-generated method stub
		
	}
}
