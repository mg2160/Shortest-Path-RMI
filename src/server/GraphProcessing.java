package server;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.LinkedList;

public class GraphProcessing extends UnicastRemoteObject implements GraphProcessingI{

	protected GraphProcessing() throws RemoteException {
		super();
		// TODO Auto-generated constructor stub
	}

	@Override
	public int query(String q) throws Exception {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void addEdge(String a) throws Exception {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void deleteEdge(String d) throws Exception {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setGraph(LinkedList<String> graph) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public LinkedList<Integer> executeBatch(LinkedList<String> lines) {
		// TODO Auto-generated method stub
		return null;
	}

}
