package fr.ensisa.hassenforder.tp.client.view;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import fr.ensisa.hassenforder.tp.client.model.SharedText;
import fr.ensisa.hassenforder.tp.client.model.TextOperation;
import fr.ensisa.hassenforder.tp.client.model.User;
import fr.ensisa.hassenforder.tp.client.network.ISession;
import fr.ensisa.hassenforder.tp.client.network.TextOperationRequest;
import javafx.beans.property.LongProperty;
import javafx.beans.property.SimpleLongProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

public class TextController {

	static boolean debug = false;

	private ISession session;
	private User user;
	private long textId;

	private ObservableList<TextOperation> operations = FXCollections.observableArrayList();

	private StringProperty status;
	private LongProperty id;
	private StringProperty owner;
	private StringProperty content;
	private StringProperty comment;

	@FXML
	private Label statusLabel;

	@FXML
	private Label idField;
	@FXML
	private Label ownerField;
	@FXML
	private TextArea contentField;
	@FXML
	private TextField commentField;

    @FXML
    private TableView<TextOperation> operationTable;
    @FXML
    private TableColumn<TextOperation, String> operationDateColumn;
    @FXML
    private TableColumn<TextOperation, String>  operationNameColumn;
    @FXML
    private TableColumn<TextOperation, String>  operationWhatColumn;
    @FXML
    private TableColumn<TextOperation, Integer>  operationWhereColumn;
    @FXML
    private TableColumn<TextOperation, String>  operationTextColumn;

    @FXML
    private void initialize() {
    	id = new SimpleLongProperty();
    	idField.textProperty().bind(id.asString());

    	owner = new SimpleStringProperty();
    	ownerField.textProperty().bind(owner);

    	content = new SimpleStringProperty();
    	contentField.textProperty().bindBidirectional(content);
    	contentField.textProperty().addListener(new ChangeListener<String>() {
			@Override
			public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
		    	updateContent(oldValue, newValue);
			}
    	});
    	status = new SimpleStringProperty();
    	statusLabel.textProperty().bind(status);

    	status.set("Ready");

    	operationDateColumn.setCellValueFactory(cellData -> cellData.getValue().getDate());
    	operationNameColumn.setCellValueFactory(cellData -> cellData.getValue().getWho());
    	operationWhatColumn.setCellValueFactory(cellData -> cellData.getValue().getWhat());
    	operationWhereColumn.setCellValueFactory(cellData -> cellData.getValue().getWhere().asObject());
    	operationTextColumn.setCellValueFactory(cellData -> cellData.getValue().getText());
    	operationTable.setItems(operations);

    	comment = new SimpleStringProperty();
    	commentField.textProperty().bindBidirectional(comment);
    }

    private TextOperation previousNotComment () {
    	if (operations.isEmpty()) return null;
    	for (int i=operations.size()-1; i != 0; --i) {
    		TextOperation operation = operations.get(i);
        	if (operation.getWhat().get().equals("comment")) continue;
        	return operation;
    	}
    	return null;
    }

    private boolean trySuccessives(TextOperation previous, TextOperation operation) {
    	if (debug) System.out.print ("Successives");
    	if (debug) System.out.print (" Previous ="+previous.toString());
    	if (debug) System.out.print (" Current  ="+operation.toString());
    	if (debug) System.out.println ();
    	if (previous.getWhere().get()+previous.getText().get().length() == operation.getWhere().get()) {
    		previous.getText().set(previous.getText().get()+operation.getText().get());
    		if (debug) System.out.println ("just after");
        	return true;
    	}
    	if (previous.getWhere().get() == operation.getWhere().get()) {
    		previous.getText().set(operation.getText().get()+previous.getText().get());
    		if (debug) System.out.println ("just before");
        	return true;
    	}
    	return false;
    }

    private boolean tryInsertDelete(TextOperation previous, TextOperation operation) {
    	if (debug) System.out.print ("Insert/Delete");
    	if (debug) System.out.print (" Previous ="+previous.toString());
    	if (debug) System.out.print (" Current  ="+operation.toString());
    	if (debug) System.out.println ();
    	if (previous.getText().get().equals(operation.getText().get())) {
        	if (previous.getWhere().get() == operation.getWhere().get()) {
        		if (debug) System.out.println ("just same");
	    		operations.remove(previous);
	        	return true;
	    	}
    	}
    	int initialLen = previous.getText().get().length();
    	int removedLen = operation.getText().get().length();
    	if (removedLen >= initialLen) return false;
    	String rr = previous.getText().get().substring(initialLen-removedLen-1, initialLen-1);
    	String ss = operation.getText().get();
    	if (debug) System.out.println ("rr="+rr+" ss="+ss);
    	if (previous.getText().get().substring(initialLen-removedLen, initialLen).equals(operation.getText().get())) {
    		if (debug) System.out.println ("just last");
    		previous.getText().set(previous.getText().get().substring(0, initialLen-removedLen));
        	return true;
    	}
    	return false;
	}

    private boolean tryMerge(TextOperation previous, TextOperation operation) {
		if (previous == null) return false;
		if (debug) System.out.print ("Previous ="+previous.toString());
		if (debug) System.out.print (" Current  ="+operation.toString());
		if (debug) System.out.println ();
    	if (operation.getWhat().get().equals("comment")) return false;
		if (! previous.getWho().get().equals(operation.getWho().get())) return false;
		if (previous.getWhat().get().equals(operation.getWhat().get()))
			return trySuccessives (previous, operation);
		if (previous.getWhat().get().equals("insert") && operation.getWhat().get().equals("delete"))
			return tryInsertDelete (previous, operation);
		if (previous.getWhat().get().equals("delete") && operation.getWhat().get().equals("insert"))
			return tryInsertDelete (previous, operation);
		return false;
	}

    private void addOperation (TextOperation operation) {
		TextOperation previous = previousNotComment ();
		if (! tryMerge (previous, operation)) {
	    	operations.add(operation);
    	}
    }

	static long LOCAL_OPERATION_ID = 0;
    private void updateContent (String oldContent, String newContent) {
    	if (oldContent == null) return;
    	if (newContent == null) return;
    	int maxBeginingLength = Math.min(oldContent.length(), newContent.length());
    	int beginingLength = maxBeginingLength;
    	for (int i=0; i<maxBeginingLength;++i) {
    		if (oldContent.charAt(i) == newContent.charAt(i)) continue;
    		beginingLength = i;
    		break;
    	}
    	int maxEndingLength = Math.min(oldContent.length()-beginingLength, newContent.length()-beginingLength);
    	int endingLength = maxEndingLength;
    	for (int i=0; i<maxEndingLength;++i) {
    		if (oldContent.charAt(oldContent.length()-i-1) == newContent.charAt(newContent.length()-i-1)) continue;
    		endingLength = i;
    		break;
    	}
    	if (beginingLength != oldContent.length()-endingLength) {
        	String deleted = oldContent.substring(beginingLength, oldContent.length()-endingLength);
        	addOperation(new TextOperation(--LOCAL_OPERATION_ID, new Date(), user.getName(), TextOperation.What.DELETE, beginingLength, deleted));
    	}
    	if (beginingLength != newContent.length()-endingLength) {
        	String inserted = newContent.substring(beginingLength, newContent.length()-endingLength);
        	addOperation(new TextOperation(--LOCAL_OPERATION_ID, new Date(), user.getName(), TextOperation.What.INSERT, beginingLength, inserted));
    	}
    }

    @FXML
    private void onAddComment() {
    	addOperation(new TextOperation(--LOCAL_OPERATION_ID, new Date(), user.getName(), TextOperation.What.COMMENT, 0, comment.get()));
    }

    private void loadShareTextOperations () {
		Collection<TextOperation> result = session.getAllTextOperations (user.getToken(), textId, user.getId());
		operations.clear();
		if (result != null) {
			operations.addAll(result);
			status.set("Fetched all operations "+textId);
		} else {
			status.set("Last request failed dramatically");
		}
	}

    private void saveShareTextOperations () {
    	List<TextOperationRequest> toSave = new ArrayList<>();
    	for (TextOperation operation : operations) {
    		if (operation.getId() > 0) continue;
    		toSave.add(new TextOperationRequest(
    				new Date(),
    				TextOperationRequest.What.valueOf(operation.getWhat().get().toUpperCase()),
    				operation.getWhere().get(),
    				operation.getText().get()
				)
			);
    	}
		Boolean result = session.saveTextOperations (user.getToken(), textId, user.getId(), toSave);
		if (result != null) {
			if (result) {
				status.set("Operations saved");
			} else {
				status.set("Cannot save operations "+textId);
			}
		} else {
			status.set("Last request failed dramatically");
		}
    }

    @FXML
    private void onSave() {
		Boolean result = session.saveTextContent(user.getToken(), textId, user.getId(), content.get());
		if (result != null) {
			if (result) {
				status.set("Operations saved");
			} else {
				status.set("Cannot save operations "+textId);
			}
		} else {
			status.set("Last request failed dramatically");
		}
    	saveShareTextOperations ();
    }

    @FXML
    private void onLoad() {
		SharedText result = session.getText(user.getToken(), textId, user.getId());
		if (result != null) {
			status.set("Text loaded");
		} else {
			status.set("Last request failed dramatically");
		}
		content.set(result.getContent().get());
		id.set(result.getId());
		owner.set(result.getOwner().get());
    	loadShareTextOperations ();
    }

    public void setSession(ISession session) {
		this.session = session;
	}

	public void setModel(User user, long textId) {
		this.user = user;
		this.textId = textId;
		onLoad();
	}

}
