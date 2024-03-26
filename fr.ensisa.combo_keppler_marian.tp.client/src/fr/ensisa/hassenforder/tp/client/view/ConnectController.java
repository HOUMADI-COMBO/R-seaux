package fr.ensisa.hassenforder.tp.client.view;

import fr.ensisa.hassenforder.tp.client.model.User;
import fr.ensisa.hassenforder.tp.client.network.ISession;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.stage.Window;

public class ConnectController {

	private ISession session;
	private User user;

	private StringProperty status;
	private StringProperty mail;
	private StringProperty passwd;

	@FXML
	private Label statusLabel;

	@FXML
	private TextField mailField;
	@FXML
	private PasswordField passwdField;

	public void initialize() {
		mail = new SimpleStringProperty("i@m");
		mailField.textProperty().bindBidirectional(mail);
		mail.addListener(l -> status.set(buildStatus()));

    	passwd = new SimpleStringProperty("iii");
    	passwdField.textProperty().bindBidirectional(passwd);
    	passwd.addListener(l -> status.set(buildStatus()));

    	status = new SimpleStringProperty("");
    	statusLabel.textProperty().bind(status);

    	status.set("Ready");
	}

    private String buildStatus() {
    	if (mail.get().isEmpty()) return "mail is empty";
    	if (passwd.get().isEmpty()) return "passwd is empty";
    	if (user.getToken() == null) return "unconnected";
    	if (user.getToken().isEmpty()) return "unconnected";
    	return "connected";
	}

	@FXML
    private void onConnect() {
		User user = session.connect(mail.get(), passwd.get());
		if (user != null) {
			this.user.setId(user.getId());
			this.user.setName(user.getName());
			this.user.setToken(user.getToken());
			Window w = statusLabel.getScene().getWindow();
			if (w instanceof Stage) ((Stage) w).close();
		} else {
			this.user.setId(0);
			this.user.setName("");
			this.user.setToken("");
		}
		status.set(buildStatus());
    }

    public void setSession(ISession session) {
		this.session = session;
	}

	public void setUser(User user) {
		this.user = user;
	}

}
