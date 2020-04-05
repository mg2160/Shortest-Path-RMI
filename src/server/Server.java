package server;
import java.rmi.*;
import java.rmi.Naming;
import java.rmi.registry.LocateRegistry;
public class Server {
	public void runServer() throws Exception{
		/*GraphProcessing obj = new GraphProcessing();
		Naming.rebind("rmi://localhost/sp", obj);
		System.out.println("Server Started !");*/
		int inport = 9000;
		GraphProcessing obj = new GraphProcessing(inport);
        System.out.println("impl = " + obj);
        String url = "rmi://localhost/sp";
        LocateRegistry.createRegistry(1099);
        Naming.rebind(url, obj);
        System.out.println("GSP Server bound at port " + inport + " in rmiregistry.");
        return;
	}
}
