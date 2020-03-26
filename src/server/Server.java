package server;

import java.rmi.Naming;
public class Server {
	public static void main(String[] args) throws Exception{
		GraphProcessing obj = new GraphProcessing();
		Naming.rebind("rmi://localhost/sp", obj);
		System.out.println("Server Started !");
	}
}
