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
			args.length > 2? args[2].equals("memo"): false
		);
		int waitingTime = args.length > 1? Integer.parseInt(args[1]): 0;
		double totalAverageTime = 0;
		
		s.run();
		long startTime = System.currentTimeMillis();
		Client clients[] = new Client[clientsCount];
		for(int i=0; i<clientsCount; i++){
		      clients[i] = new Client(new Integer(i).toString(), serverUrl, waitingTime);
		      clients[i].start();
		}
		for(int i =0;i<clientsCount;i++) {
			clients[i].join();
			totalAverageTime += clients[i].averageTime;
		}
		System.out.println("Processing Time: " + (System.currentTimeMillis() - startTime) / 1000.0 + " seconds");
		System.out.println("Average client time: " + totalAverageTime/clientsCount);
		Naming.unbind(serverUrl);
		return;
	}
}