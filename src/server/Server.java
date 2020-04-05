package server;
import java.io.File;
import java.io.FileNotFoundException;
import java.rmi.Naming;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.LinkedList;
import java.util.Scanner;
public class Server {
	private int regPort;
	private int objPort;
	private String url;
	
	public Server(int regPort, int objPort, String serverUrl) {
		this.regPort = regPort;
		this.objPort = objPort;
		this.url = serverUrl;
	}
	
	public void run() throws Exception{
		IGraphProcessing obj = new GraphProcessing(objPort);
        System.out.println("impl = " + obj);
        Registry reg = LocateRegistry.createRegistry(regPort);
        Naming.rebind(url, obj);
        System.out.println("GSP Server bound at port " + objPort + " in rmiregistry.");
        System.out.println("Registry: " + reg);
        System.out.println("Setting up initial graph");
        if(!this.setInitialGraph(obj)) {
        	System.out.println("Failed to setup initial graph.");
        	return;
        }
        System.out.println("System is running!");
        return;
	}
	
	private boolean setInitialGraph(IGraphProcessing obj) {
		File file = new File("input.txt");
		Scanner sc=null;
		try {
			sc = new Scanner(file);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		LinkedList<String> inputGraph = new LinkedList<String>(); 
		while (sc.hasNext()) {
			String line=sc.nextLine();
			if(line.equals("S")) {
				break;
			}
			inputGraph.add(line);
		}
		try {
			obj.setGraph(inputGraph);
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}
}
