package fr.ensisa.hassenforder.tp.database;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class OperationTable {

	private static long LAST_ID = 2000;

	private Map<Long, Operation> operations = null;

	private Map<Long, Operation> getOperations () {
		if (operations == null) {
			operations = new TreeMap<>();
		}
		return operations;
	}

	public long addOperation (Operation operation) {
		operation.setId (++LAST_ID);
		getOperations ().put(operation.getId(), operation);
		return operation.getId();
	}

	public Operation getOperation (long id) {
		return getOperations().get(id);
	}

	public Collection<Operation> getAllOperations(long textId) {
		List<Operation> operations = new ArrayList<>();
		for (Operation operation : getOperations().values()) {
			if (operation.getOn() != textId) continue;
			operations.add(operation);
		}
		return operations;
	}

	public void remove(Operation operation) {
		getOperations().remove(operation.getId());
	}

}
