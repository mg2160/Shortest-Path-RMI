import server.Server;

import java.io.FileInputStream;
import java.rmi.Naming;
import java.util.Properties;

import client.*;

public class Start {
	public static int CLIENTS_COUNT = 10;
	public static void main(String[] args) throws Exception {
		int clientsCount = args.length > 0? Integer.parseInt(args[0]): CLIENTS_COUNT;
		Properties prop = new Properties();
		prop.load(new FileInputStream("system.properties"));
		String serverUrl = prop.get("GSP.server").toString(); 
		Server s = new Server(
			Integer.parseInt(prop.get("GSP.rmiregistry.port").toString()),
			Integer.parseInt(prop.get("GSP.server.objPort").toString()),
			serverUrl,
			args.length > 1? args[1].equals("memo"): false
		);
		s.run();
		long startTime = System.currentTimeMillis();
		Client clients[] = new Client[clientsCount];
		for(int i=0; i<clientsCount; i++){
		      clients[i] = new Client(new Integer(i).toString(), serverUrl);
		      clients[i].start();
		}
		for(int i =0;i<clientsCount;i++) {
			clients[i].join();
		}
		System.out.println("Processing Time: " + (System.currentTimeMillis() - startTime) / 1000.0 + " seconds");
		Naming.unbind(serverUrl);
		return;
	}
}