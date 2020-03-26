package client;
import server.GraphProcessingI;
import java.rmi.Naming;

public class Client {
	public static void main(String[] args) throws Exception{
		GraphProcessingI obj = (GraphProcessingI)Naming.lookup("rmi://localhost/sp");
		int n = obj.query("");
		System.out.println("Shortest path = " + n);
	}
}
