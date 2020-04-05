import server.Server;

import java.io.FileInputStream;
import java.util.Properties;

import client.*;

public class Start {

	public static void main(String[] args) throws Exception {
		// TODO Auto-generated method stub
		Properties prop = new Properties();
		System.out.println(new java.io.File(".").getCanonicalPath());
		prop.load(new FileInputStream("system.properties"));
		
		Server s = new Server(
			Integer.parseInt(prop.get("GSP.rmiregistry.port").toString()),
			Integer.parseInt(prop.get("GSP.server.port").toString()),
			prop.get("GSP.server").toString()
		);
		s.run();
		for(int i=1; i<4; i++){
		      Client client = new Client(new Integer(i).toString());
		      client.start();
		}
		return;
	}
}