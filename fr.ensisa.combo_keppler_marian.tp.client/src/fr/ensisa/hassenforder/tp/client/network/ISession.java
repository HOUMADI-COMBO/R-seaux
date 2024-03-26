package fr.ensisa.hassenforder.tp.client.network;

import java.util.Collection;

import fr.ensisa.hassenforder.tp.client.model.Credential;
import fr.ensisa.hassenforder.tp.client.model.Participant;
import fr.ensisa.hassenforder.tp.client.model.SharedText;
import fr.ensisa.hassenforder.tp.client.model.TextOperation;
import fr.ensisa.hassenforder.tp.client.model.User;

/**
 *
 * @author hassenforder
 */
public interface ISession {

    boolean open ();
    boolean close ();

    User connect(String mail, String passwd);
	Boolean createUser(Credential credential);
	Credential getCredential(String token, long whoId);
	Boolean updateUser(String token, Credential credential);

	Collection<SharedText> getAllTexts(String token, long whoId);
	SharedText getText(String token, long textId, long whoId);
	Boolean saveTextContent(String token, long textId, long whoId, String content);
	Boolean deleteText(String token, long textId, long whoId);
	Boolean newText(String token, long whoId);

	Collection<Participant> getAllTextParticipants(String token, long textId, long whoId);
	Boolean saveTextParticipants(String token, long textId, long whoId, Collection<ParticipantOperationRequest> toSave);
	Collection<User> getAllUsers(String token);

	Collection<TextOperation> getAllTextOperations(String token, long textId, long whoId);
	Boolean saveTextOperations(String token, long textId, long whoId, Collection<TextOperationRequest> toSave);

}
