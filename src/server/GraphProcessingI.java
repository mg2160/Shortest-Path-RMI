package server;
import java.rmi.*;
import java.util.LinkedList;
public interface GraphProcessingI extends Remote{
	
	void setGraph(LinkedList<String> graph);
	LinkedList<Integer> executeBatch(LinkedList<String> lines);
	
	int query(String q) throws Exception;
	
	void addEdge(String a) throws Exception;
	
	void deleteEdge(String d) throws Exception;

}
