package server;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.ArrayList;

public class GraphProcessing extends UnicastRemoteObject implements GraphProcessingI{

	private static final int MAX_GRAPH_NODES_SIZE = 5000;

	private static final int QUERY_ARGUMENTS_LENGTH = 3;



	public ArrayList<Integer> edges[] = new ArrayList<>()[MAX_GRAPH_NODES_SIZE];
	boolean mask[] = new boolean[MAX_GRAPH_NODES_SIZE];

	protected GraphProcessing() throws RemoteException {
		super();
		// TODO Auto-generated constructor stub
	}

	
	@Override
	public void setGraph(LinkedList<String> graph) throws Exception {
		// TODO Auto-generated method stub
		for (int i = 0; i < edges.length; i++) { 
            edges[i] = new ArrayList<>();
        } 
		for (Iterator iterator = graph.iterator(); iterator.hasNext();) {
			String string = (String) iterator.next();
			String[] instruction = string.split(" ");
			int source = Integer.parseInt(instruction[0]);
			int destination = Integer.parseInt(instruction[1]);
			addEdge(source-1, destination-1);
			mask[source-1]=true;
			mask[destination-1]=true;
		}
	}

	@Override
	public LinkedList<Integer> executeBatch(LinkedList<String> lines) throws Exception {
		// TODO Auto-generated method stub
		LinkedList<Integer> outputList = new LinkedList<Integer>();
		//System.out.println("First element= " + lines.get(0));
		//System.out.println("Outside for");
		for (Iterator iterator = lines.iterator(); iterator.hasNext();) {
			//System.out.println("Inside for");
			String string = (String) iterator.next();
			String[] batchLine = string.split(" ");
			if (batchLine.length != QUERY_ARGUMENTS_LENGTH) {
				throw new IllegalArgumentException("Passed wrong format of input.\n" +
						"Length passed: " + batchLine.length " instead of: " + QUERY_ARGUMENTS_LENGTH);
			}
			char operation = batchLine[0].charAt(0);
			int execSrc = Integer.parseInt(batchLine[1]);
			int execDest = Integer.parseInt(batchLine[2]);
			//System.out.println("Before");
			if(operation == 'Q') {
				if(mask[execSrc-1]==false || mask[execDest-1]==false) {
					addEdge(execSrc-1, execDest-1);
					mask[execSrc-1] = true;
					mask[execDest-1] = true;
				}
				int shortestPath = query(execSrc-1,execDest-1);
				if(shortestPath==0) {
					outputList.add(-1);
				} else {
					outputList.add(shortestPath);
				}
			} else if (operation == 'A') {
				addEdge(execSrc-1, execDest-1);
				mask[execSrc-1] = true;
				mask[execDest-1] = true;
				
			} else if (operation == 'D') {
				deleteEdge(execSrc-1, execDest-1);
				if(edges[execSrc-1].size()==0) {
					mask[execSrc-1] =false;
				}
				if(edges[execDest-1].size()==0) {
					mask[execDest-1] =false;
				}
			} else {
				throw new UnsupportedOperationException("Unsupported query type: " + operation);
			}
		}
		return outputList;
	}
	

	private int query(int src, int dest) {
		// TODO Auto-generated method stub
		int sp = minEdgeBFS(src, dest, edges.length);
		return sp;
	}
	
	private void addEdge(int src, int dest) { 
		edges[src].add(dest); 
	}

	private void deleteEdge(int src , int dest) {
		// TODO Auto-generated method stub
		int j=edges[src].indexOf(2);
		edges[src].remove(j);
	}
	
	
	private int minEdgeBFS( int u, int v, int n) { 
		// visited[n] for keeping track of visited 
		// node in BFS 
		ArrayList<Boolean> visited = new ArrayList<Boolean>(n);

		for (int i = 0; i < n; i++) { 
			visited.addElement(false); 
		} 

		// Initialize distances as 0 
		ArrayList<Integer> distance = new ArrayList<Integer>(n);

		for (int i = 0; i < n; i++) { 
			distance.addElement(0); 
		} 

		// queue to do BFS. 
		Queue<Integer> Q = new LinkedList<>(); 
		distance.setElementAt(0, u); 

		Q.add(u); 
		visited.setElementAt(true, u); 
		while (!Q.isEmpty()) { 
			int x = Q.peek(); 
			Q.poll(); 

			for (int i=0; i<edges[x].size(); i++) { 
				if (visited.elementAt(edges[x].get(i))) 
					continue; 

				// update distance for i 
				distance.setElementAt(distance.get(x) + 1,edges[x].get(i)); 
				Q.add(edges[x].get(i)); 
				visited.setElementAt(true,edges[x].get(i)); 
			} 
		} 
		return distance.get(v); 
	} 

}
