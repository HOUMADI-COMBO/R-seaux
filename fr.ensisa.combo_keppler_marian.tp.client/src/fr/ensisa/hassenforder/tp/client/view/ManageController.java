package fr.ensisa.hassenforder.tp.client.view;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import fr.ensisa.hassenforder.tp.client.model.Participant;
import fr.ensisa.hassenforder.tp.client.model.Role;
import fr.ensisa.hassenforder.tp.client.model.SharedText;
import fr.ensisa.hassenforder.tp.client.model.User;
import fr.ensisa.hassenforder.tp.client.network.ISession;
import fr.ensisa.hassenforder.tp.client.network.ParticipantOperationRequest;
import javafx.beans.property.LongProperty;
import javafx.beans.property.SimpleLongProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.util.Callback;

public class ManageController {

	private ISession session;
	private User user;
	private SharedText sharedText;

	private ObservableList<Participant> participants = FXCollections.observableArrayList();
	private ObservableList<User> users = FXCollections.observableArrayList();

	private StringProperty status;
	private LongProperty id;
	private StringProperty content;

	@FXML
	private Label statusLabel;

	@FXML
	private Label idField;
	@FXML
	private Label contentField;

    @FXML
    private TableView<Participant> participantTable;
    @FXML
    private TableColumn<Participant, Long> participantIdColumn;
    @FXML
    private TableColumn<Participant, String>  participantNameColumn;
    @FXML
    private TableColumn<Participant, String>  participantRoleColumn;
    @FXML
    private TableColumn<Participant, Void>  participantCanEditColumn;
    @FXML
    private TableColumn<Participant, Void>  participantCanCommentColumn;
    @FXML
    private TableColumn<Participant, Void>  participantCanReadColumn;
    @FXML
    private TableColumn<Participant, Void>  participantRemoveColumn;

    @FXML
    private TableView<User> userTable;
    @FXML
    private TableColumn<User, String>  userNameColumn;
    @FXML
    private TableColumn<User, Void>  userAddColumn;

    @FXML
    private void initialize() {
    	id = new SimpleLongProperty();
    	idField.textProperty().bind(id.asString());

    	content = new SimpleStringProperty();
    	contentField.textProperty().bind(content);

    	status = new SimpleStringProperty();
    	statusLabel.textProperty().bind(status);

    	status.set("Ready");

    	participantIdColumn.setCellValueFactory(cellData -> cellData.getValue().getId().asObject());
    	participantNameColumn.setCellValueFactory(cellData -> cellData.getValue().getName());
    	participantRoleColumn.setCellValueFactory(cellData -> cellData.getValue().getRoleProperty());
    	participantCanEditColumn.setCellFactory(
        		new Callback<TableColumn<Participant, Void>, TableCell<Participant, Void>>() {
                    @Override
                    public TableCell<Participant, Void> call(final TableColumn<Participant, Void> param) {
                        final TableCell<Participant, Void> cell = new TableCell<Participant, Void>() {
                            private final CheckBox btn = new CheckBox("edit");

                            @Override
                            public void updateItem(Void item, boolean empty) {
                                super.updateItem(item, empty);
                                if (empty) {
                                    setGraphic(null);
                                } else {
                                    setGraphic(btn);
                                	btn.selectedProperty().bindBidirectional(getTableView().getItems().get(getIndex()).getRole().getCanEdit());
                                }
                            }
                        };
                        return cell;
                    }
                }
        );
    	participantCanCommentColumn.setCellFactory(
        		new Callback<TableColumn<Participant, Void>, TableCell<Participant, Void>>() {
                    @Override
                    public TableCell<Participant, Void> call(final TableColumn<Participant, Void> param) {
                        final TableCell<Participant, Void> cell = new TableCell<Participant, Void>() {
                            private final CheckBox btn = new CheckBox("comment");

                            @Override
                            public void updateItem(Void item, boolean empty) {
                                super.updateItem(item, empty);
                                if (empty) {
                                    setGraphic(null);
                                } else {
                                    setGraphic(btn);
                                	btn.selectedProperty().bindBidirectional(getTableView().getItems().get(getIndex()).getRole().getCanComment());
                                }
                            }
                        };
                        return cell;
                    }
                }
        );
    	participantCanReadColumn.setCellFactory(
        		new Callback<TableColumn<Participant, Void>, TableCell<Participant, Void>>() {
                    @Override
                    public TableCell<Participant, Void> call(final TableColumn<Participant, Void> param) {
                        final TableCell<Participant, Void> cell = new TableCell<Participant, Void>() {
                            private final CheckBox btn = new CheckBox("read");

                            @Override
                            public void updateItem(Void item, boolean empty) {
                                super.updateItem(item, empty);
                                if (empty) {
                                    setGraphic(null);
                                } else {
                                    setGraphic(btn);
                                	btn.selectedProperty().bindBidirectional(getTableView().getItems().get(getIndex()).getRole().getCanRead());
                                }
                            }
                        };
                        return cell;
                    }
                }
        );
    	participantRemoveColumn.setCellFactory(
        		new Callback<TableColumn<Participant, Void>, TableCell<Participant, Void>>() {
                    @Override
                    public TableCell<Participant, Void> call(final TableColumn<Participant, Void> param) {
                        final TableCell<Participant, Void> cell = new TableCell<Participant, Void>() {
                            private final Button btn = new Button("remove");
                            {
                                btn.setOnAction((ActionEvent event) -> {
                                	participants.remove(getIndex());
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

    	participantTable.setItems(participants);

    	userNameColumn.setCellValueFactory(cellData -> cellData.getValue().getNameProperty());
    	userAddColumn.setCellFactory(
        		new Callback<TableColumn<User, Void>, TableCell<User, Void>>() {
                    @Override
                    public TableCell<User, Void> call(final TableColumn<User, Void> param) {
                        final TableCell<User, Void> cell = new TableCell<User, Void>() {
                            private final Button btn = new Button("add");
                            {
                                btn.setOnAction((ActionEvent event) -> {
                                	User user = getTableView().getItems().get(getIndex());
                                	participants.add(
                            			new Participant (user.getId(), user.getName(), new Role(Role.READER))
                            		);
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
    	userTable.setItems(users);

    }

    private void loadPartipants () {
		Collection<Participant> result = session.getAllTextParticipants (user.getToken(), sharedText.getId(), user.getId());
		participants.clear();
		if (result != null) {
			participants.addAll(result);
			status.set("Fetched all participants "+sharedText.getId());
		} else {
			status.set("Last request failed dramatically");
		}
    }

    @FXML
    private void onLoad() {
    	loadPartipants ();
    }

    private void savePartipants () {
    	Collection<ParticipantOperationRequest> toSave = new ArrayList<>();
    	for (Participant participant : participants) {
    		toSave.add(new ParticipantOperationRequest(
    				participant.getId().get(),
    				participant.getRole()
				)
			);
    	}
    	Boolean result = session.saveTextParticipants (
			user.getToken(), sharedText.getId(), user.getId(),
			toSave
		);
		if (result != null) {
			if (result) {
				status.set("Participant list saved");
			} else {
				status.set("Cannot save participant list");
			}
		} else {
			status.set("Last request failed dramatically");
		}

    }

    @FXML
    private void onSave() {
    	savePartipants ();
    }

    private boolean contains (User incoming) {
    	for (Participant p : participants) {
    		if (p.getId().get() == incoming.getId())
    			return true;
    	}
    	return false;
    }

    private Collection<User> filter (Collection<User> incomings) {
    	List<User> result = new ArrayList<>();
    	for (User p : incomings) {
    		if (! contains(p)) result.add(p);
    	}
    	return result;
    }

    @FXML
    private void onListUsers() {
		Collection<User> result = session.getAllUsers (user.getToken());
		users.clear();
		if (result != null) {
			users.addAll(filter(result));
			status.set("Fetched all users ");
		} else {
			status.set("Last request failed dramatically");
		}
    }

    public void setSession(ISession session) {
		this.session = session;
	}

	public void setModel(User user, SharedText sharedText) {
		this.user = user;
		this.sharedText = sharedText;
		id.set(sharedText.getId());
		content.set(sharedText.getContent().get());
		loadPartipants ();
	}

}
