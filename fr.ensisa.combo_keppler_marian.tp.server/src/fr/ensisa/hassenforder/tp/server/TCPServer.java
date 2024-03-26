package fr.ensisa.hassenforder.tp.server;

import java.net.ServerSocket;
import java.net.Socket;

import fr.ensisa.hassenforder.tp.network.Protocol;
import fr.ensisa.hassenforder.tp.database.Model;
import fr.ensisa.hassenforder.tp.server.network.TCPSession;

public class TCPServer extends Thread {

	private ServerSocket server = null;
	private Model model = null;

	public TCPServer(Model model) {
		super();
		this.model = model;
	}

	public void run () {
		try {
			server = new ServerSocket (Protocol.TP_TCP_PORT);
			while (true) {
				Socket connection = server.accept();
				TCPSession session = new TCPSession (connection, model);
				session.start ();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
