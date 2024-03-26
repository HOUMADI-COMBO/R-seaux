package fr.ensisa.hassenforder.tp.client;

import fr.ensisa.hassenforder.tp.client.fake.FakeSession;
import fr.ensisa.hassenforder.tp.client.network.ISession;
import fr.ensisa.hassenforder.tp.client.network.NetworkSession;
import fr.ensisa.hassenforder.tp.client.view.TPController;
import fr.ensisa.hassenforder.tp.network.Protocol;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;


public class Main extends Application {

	private final boolean withNetwork = true;

	@Override
	public void start(Stage primaryStage) {
		try {
	        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("view/Client.fxml"));
	        Parent root1 = (Parent) fxmlLoader.load();
	        primaryStage.setScene(new Scene(root1));
	        primaryStage.setTitle("Shared Text");
            TPController controller = fxmlLoader.getController();
            controller.setSession(getSession());
	        primaryStage.show();
		} catch(Exception e) {
			e.printStackTrace();
		}
	}

	private ISession getSession() {
		ISession session = withNetwork ? new NetworkSession("localhost", Protocol.TP_TCP_PORT) : new FakeSession();
		session.open();
		return session;
	}

	public static void main(String[] args) {
		launch(args);
	}
}
