package fr.ensisa.hassenforder.tp.client.view;

import java.net.URL;
import java.util.ResourceBundle;

import fr.ensisa.hassenforder.tp.client.model.Credential;
import fr.ensisa.hassenforder.tp.client.model.User;
import fr.ensisa.hassenforder.tp.client.network.ISession;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.stage.Window;

public class UpdateController implements Initializable {

	private ISession session;
	private User user;

	private StringProperty status;
	private StringProperty name;
	private StringProperty mail;
	private StringProperty passwd;

	@FXML
	private Label statusLabel;

	@FXML
	private TextField nameField;
	@FXML
	private TextField mailField;
	@FXML
	private TextField passwdField;
	@FXML
	private Button validButton;

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
    	name = new SimpleStringProperty("");
    	nameField.textProperty().bindBidirectional(name);
    	name.addListener(l -> status.set(buildStatus()));

    	mail = new SimpleStringProperty("");
    	mailField.textProperty().bindBidirectional(mail);
    	mail.addListener(l -> status.set(buildStatus()));

    	passwd = new SimpleStringProperty("");
    	passwdField.textProperty().bindBidirectional(passwd);
    	passwd.addListener(l -> status.set(buildStatus()));

    	status = new SimpleStringProperty("");
    	statusLabel.textProperty().bind(status);

    	status.set("Ready");
	}


    private String buildStatus() {
    	if (name.get().isEmpty()) return "name is empty";
    	if (passwd.get().isEmpty()) return "passwd is empty";
    	if (user.getToken().isEmpty()) return "unconnected";
    	return "connected";
	}

	@FXML
    private void onValid() {
		if (user.getState().equals("connected")) {
			Credential credential = new Credential(user.getId(), user.getName(), mail.get(), passwd.get());
			Boolean result = session.updateUser (user.getToken(), credential);
			if (result != null) {
				if (result) status.set("Successfully Updated "+name.get());
				else status.set("Cannot update "+name.get());
			} else {
				status.set("Last request failed dramatically");
			}
	    }
	}

	@FXML
    private void onCancel() {
		Window w = statusLabel.getScene().getWindow();
		((Stage) w).close();
    }

    public void setSession(ISession session) {
		this.session = session;
	}

    public void setUser(User user) {
		this.user = user;
		nameField.setDisable(true);
		if (user.getStateProperty().get().equals("connected")) {
			Credential credential = session.getCredential (user.getToken(), user.getId());
			name.set(credential.getName());
			mail.set(credential.getMail());
			passwd.set(credential.getPasswd());
		} else {
			mailField.setDisable(true);
			passwdField.setDisable(true);
			validButton.setDisable(true);
		}
	}

}
