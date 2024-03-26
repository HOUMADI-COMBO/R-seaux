package fr.ensisa.hassenforder.tp.client.view;

import java.io.IOException;
import java.util.Collection;

import fr.ensisa.hassenforder.tp.client.model.SharedText;
import fr.ensisa.hassenforder.tp.client.model.User;
import fr.ensisa.hassenforder.tp.client.network.ISession;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Callback;

public class TPController {

	private ISession session;
	private User user;
	private ObservableList<SharedText> sharedTexts = FXCollections.observableArrayList();

	private StringProperty status;

	@FXML
	private Label statusLabel;

	@FXML
	private Label nameField;
	@FXML
	private Label stateField;

    @FXML
    private TableView<SharedText> sharedTextTable;
    @FXML
    private TableColumn<SharedText, String> sharedTextCreatedColumn;
    @FXML
    private TableColumn<SharedText, String>  sharedTextContentColumn;
    @FXML
    private TableColumn<SharedText, String>  sharedTextRoleColumn;
    @FXML
    private TableColumn<SharedText, String>  sharedTextOwnerColumn;
    @FXML
    private TableColumn<SharedText, Integer>  sharedTextEditCountColumn;
    @FXML
    private TableColumn<SharedText, Integer>  sharedTextCommentCountColumn;
    @FXML
    private TableColumn<SharedText, Void>  sharedTextUpdateColumn;
    @FXML
    private TableColumn<SharedText, Void>  sharedTextManageColumn;
    @FXML
    private TableColumn<SharedText, Void>  sharedTextDeleteColumn;

    @FXML
    private void initialize() {
    	user = new User(0, "", "");
    	nameField.textProperty().bind(user.getNameProperty());
    	stateField.textProperty().bind(user.getStateProperty());

    	status = new SimpleStringProperty();
    	statusLabel.textProperty().bind(status);

    	status.set("Ready");

    	sharedTextCreatedColumn.setCellValueFactory(cellData -> cellData.getValue().getCreated());
    	sharedTextContentColumn.setCellValueFactory(cellData -> cellData.getValue().getContent());
    	sharedTextRoleColumn.setCellValueFactory(cellData -> cellData.getValue().getRole());
    	sharedTextOwnerColumn.setCellValueFactory(cellData -> cellData.getValue().getOwner());
    	sharedTextEditCountColumn.setCellValueFactory(cellData -> cellData.getValue().getEditCount().asObject());
    	sharedTextCommentCountColumn.setCellValueFactory(cellData -> cellData.getValue().getCommentCount().asObject());
    	sharedTextUpdateColumn.setCellFactory(
        		new Callback<TableColumn<SharedText, Void>, TableCell<SharedText, Void>>() {
                    @Override
                    public TableCell<SharedText, Void> call(final TableColumn<SharedText, Void> param) {
                        final TableCell<SharedText, Void> cell = new TableCell<SharedText, Void>() {
                            private final Button btn = new Button("Update");
                            {
                                btn.setOnAction((ActionEvent event) -> {
                                	SharedText st = getTableView().getItems().get(getIndex());
                                	onEditText(st);
                                });
                            }

                            @Override
                            public void updateItem(Void item, boolean empty) {
                                super.updateItem(item, empty);
                                if (empty) {
                                    setGraphic(null);
                                } else {
                                    setGraphic(btn);
                                }
                            }
                        };
                        return cell;
                    }
                }
        );
    	sharedTextManageColumn.setCellFactory(
        		new Callback<TableColumn<SharedText, Void>, TableCell<SharedText, Void>>() {
                    @Override
                    public TableCell<SharedText, Void> call(final TableColumn<SharedText, Void> param) {
                        final TableCell<SharedText, Void> cell = new TableCell<SharedText, Void>() {
                            private final Button btn = new Button("Manage");
                            {
                                btn.setOnAction((ActionEvent event) -> {
                                	SharedText st = getTableView().getItems().get(getIndex());
                                	onManageText(st);
                                });
                            }

                            @Override
                            public void updateItem(Void item, boolean empty) {
                                super.updateItem(item, empty);
                                if (empty) {
                                    setGraphic(null);
                                } else {
                                    setGraphic(btn);
                                }
                            }
                        };
                        return cell;
                    }
                }
        );
    	sharedTextDeleteColumn.setCellFactory(
        		new Callback<TableColumn<SharedText, Void>, TableCell<SharedText, Void>>() {
                    @Override
                    public TableCell<SharedText, Void> call(final TableColumn<SharedText, Void> param) {
                        final TableCell<SharedText, Void> cell = new TableCell<SharedText, Void>() {
                            private final Button btn = new Button("Delete");
                            {
                                btn.setOnAction((ActionEvent event) -> {
                                	SharedText st = getTableView().getItems().get(getIndex());
                                	onDeleteText(st);
                                });
                            }

                            @Override
                            public void updateItem(Void item, boolean empty) {
                                super.updateItem(item, empty);
                                if (empty) {
                                    setGraphic(null);
                                } else {
                                    setGraphic(btn);
                                }
                            }
                        };
                        return cell;
                    }
                }
        );

    	sharedTextTable.setItems(sharedTexts);
    }

    @FXML
    private void onConnect() {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("Connect.fxml"));
		try {
	        Parent root = (Parent) fxmlLoader.load();
	        Stage stage = new Stage();
	        stage.initModality(Modality.APPLICATION_MODAL);
	        stage.setTitle("Connection");
	        stage.setScene(new Scene(root));
            ConnectController controller = fxmlLoader.getController();
            controller.setSession(session);
            controller.setUser(user);
	        stage.show();
		} catch (IOException e) {
			e.printStackTrace();
		}
    }

    @FXML
    private void onCreate() {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("Create.fxml"));
		try {
	        Parent root = (Parent) fxmlLoader.load();
	        Stage stage = new Stage();
	        stage.initModality(Modality.APPLICATION_MODAL);
	        stage.setTitle("Create");
	        stage.setScene(new Scene(root));
            CreateController controller = fxmlLoader.getController();
            controller.setSession(session);
	        stage.show();
		} catch (IOException e) {
			e.printStackTrace();
		}
    }

    @FXML
    private void onUpdate() {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("Update.fxml"));
		try {
	        Parent root = (Parent) fxmlLoader.load();
	        Stage stage = new Stage();
	        stage.initModality(Modality.APPLICATION_MODAL);
	        stage.setTitle("Update");
	        stage.setScene(new Scene(root));
            UpdateController controller = fxmlLoader.getController();
            controller.setSession(session);
            controller.setUser(user);
	        stage.show();
		} catch (IOException e) {
			e.printStackTrace();
		}
    }

    @FXML
    private void onNewText() {
		Boolean result = session.newText(user.getToken(), user.getId());
		if (result != null) {
			if (result) {
				status.set("Shared Text created");
			} else {
				status.set("Cannot create Shared Text ");
			}
		} else {
			status.set("Last request failed dramatically");
		}
    }

    @FXML
    private void onListTexts() {
		Collection<SharedText> result = session.getAllTexts (user.getToken(), user.getId());
		sharedTexts.clear();
		if (result != null) {
			sharedTexts.addAll(result);
			status.set("Fetched all shared texts for "+user.getName());
		} else {
			status.set("Last request failed dramatically");
		}
    }

    private void onEditText(SharedText st) {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("Text.fxml"));
		try {
	        Parent root = (Parent) fxmlLoader.load();
	        Stage stage = new Stage();
	        stage.initModality(Modality.APPLICATION_MODAL);
	        stage.setTitle("Edition of a Shared Text");
	        stage.setScene(new Scene(root));
            TextController controller = fxmlLoader.getController();
            controller.setSession(session);
            controller.setModel(user, st.getId());
	        stage.show();
		} catch (IOException e) {
			e.printStackTrace();
		}
    }

    private void onManageText(SharedText st) {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("Manage.fxml"));
		try {
	        Parent root = (Parent) fxmlLoader.load();
	        Stage stage = new Stage();
	        stage.initModality(Modality.APPLICATION_MODAL);
	        stage.setTitle("Manage Participants to a Shared Text");
	        stage.setScene(new Scene(root));
            ManageController controller = fxmlLoader.getController();
            controller.setSession(session);
            controller.setModel(user, st);
	        stage.show();
		} catch (IOException e) {
			e.printStackTrace();
		}
    }

    private void onDeleteText(SharedText st) {
		Boolean result = session.deleteText(user.getToken(), st.getId(), user.getId());
		if (result != null) {
			if (result) {
				status.set("Shared Text deleted");
			} else {
				status.set("Cannot delete Shared Text "+st.getId());
			}
		} else {
			status.set("Last request failed dramatically");
		}
    }

    public void setSession(ISession session) {
		this.session = session;
	}

}
