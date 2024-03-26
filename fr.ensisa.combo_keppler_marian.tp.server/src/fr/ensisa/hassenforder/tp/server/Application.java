package fr.ensisa.hassenforder.tp.server;

import fr.ensisa.hassenforder.tp.database.Model;

public class Application {

	private Model model = null;
	private TCPServer tcp = null;

	public void start () {
		model = new Model();
		model.initialize();
		tcp = new TCPServer(model);
		tcp.start();
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		Application m = new Application ();
		m.start();
	}

}
