import server.Server;

import java.io.FileInputStream;
import java.rmi.Naming;
import java.util.Properties;

import client.*;

public class Start {
	private static int CLIENTS_COUNT = 15;
	public static void main(String[] args) throws Exception {
		// TODO Auto-generated method stub
		Properties prop = new Properties();
		prop.load(new FileInputStream("system.properties"));
		String serverUrl = prop.get("GSP.server").toString(); 
		Server s = new Server(
			Integer.parseInt(prop.get("GSP.rmiregistry.port").toString()),
			Integer.parseInt(prop.get("GSP.server.objPort").toString()),
			serverUrl,
			false
		);
		s.run();
		long startTime = System.currentTimeMillis();
		Client clients[] = new Client[CLIENTS_COUNT];
		for(int i=0; i<CLIENTS_COUNT; i++){
		      clients[i] = new Client(new Integer(i).toString(), serverUrl);
		      clients[i].start();
		}
		for(int i =0;i<CLIENTS_COUNT;i++) {
			clients[i].join();
		}
		System.out.println("Processing Time: " + (System.currentTimeMillis() - startTime) / 1000.0 + " seconds");
		Naming.unbind(serverUrl);
		return;
	}
}