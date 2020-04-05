package server;
import java.rmi.*;
import java.util.List;
public interface IGraphProcessing extends Remote{
	
	void setGraph(List<String> graph) throws Exception;
	List<Integer> executeBatch(List<String> lines) throws Exception;	

}
