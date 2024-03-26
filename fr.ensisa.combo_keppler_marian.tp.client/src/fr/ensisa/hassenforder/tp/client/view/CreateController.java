package fr.ensisa.hassenforder.tp.client.view;

import java.net.URL;
import java.util.ResourceBundle;

import fr.ensisa.hassenforder.tp.client.model.Credential;
import fr.ensisa.hassenforder.tp.client.network.ISession;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.stage.Window;

public class CreateController implements Initializable {

	private ISession session;

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
    	if (mail.get().isEmpty()) return "mail is empty";
    	if (passwd.get().isEmpty()) return "passwd is empty";
    	return "ready ... may be";
	}

	@FXML
    private void onValid() {
		Credential credential = new Credential(0, name.get(), mail.get(), passwd.get());
		Boolean result = session.createUser (credential);
		if (result) status.set("Successfully created "+name.get());
		else status.set("cannot create "+name.get());
    }

	@FXML
    private void onCancel() {
		Window w = statusLabel.getScene().getWindow();
		((Stage) w).close();
    }

    public void setSession(ISession session) {
		this.session = session;
	}

}
