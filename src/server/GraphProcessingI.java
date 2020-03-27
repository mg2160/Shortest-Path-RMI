package server;
import java.rmi.*;
import java.util.LinkedList;
import java.util.Vector;
public interface GraphProcessingI extends Remote{
	
	void setGraph(LinkedList<String> graph) throws Exception;
	LinkedList<Integer> executeBatch(LinkedList<String> lines) throws Exception;	

}
