package server;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.HashMap;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class GraphProcessing extends UnicastRemoteObject implements GraphProcessingI{

	private static final int MAX_GRAPH_NODES_SIZE = 5000;

	private static final int QUERY_ARGUMENTS_LENGTH = 3;

	private static ReentrantReadWriteLock rwl = new ReentrantReadWriteLock();


	/**
	 * A mapping of previously source node path to a hashmap taking destination as key and result value
	 * Source -> < Destination -> Distance of shortest path >
	 */
	private static HashMap<Integer, HashMap<Integer, Integer>> dpMap = new HashMap<>();

	private ArrayList<Integer>[] edges = new ArrayList[MAX_GRAPH_NODES_SIZE];
	private boolean nodesSet[] = new boolean[MAX_GRAPH_NODES_SIZE];

	protected GraphProcessing() throws RemoteException {
		super();
	}

	
	@Override
	public void setGraph(LinkedList<String> graph) throws Exception {
		for (int i = 0; i < edges.length; i++) { 
            edges[i] = new ArrayList<>();
        } 
		for (Iterator iterator = graph.iterator(); iterator.hasNext();) {
			String string = (String) iterator.next();
			String[] instruction = string.split(" ");
			int source = Integer.parseInt(instruction[0]);
			int destination = Integer.parseInt(instruction[1]);
			addEdge(source-1, destination-1);
			nodesSet[source-1]=true;
			nodesSet[destination-1]=true;
		}
	}

	@Override
	public LinkedList<Integer> executeBatch(LinkedList<String> lines) throws Exception {
		LinkedList<Integer> outputList = new LinkedList<Integer>();
		//System.out.println("First element= " + lines.get(0));
		//System.out.println("Outside for");
		for (Iterator iterator = lines.iterator(); iterator.hasNext();) {
			//System.out.println("Inside for");
			String string = (String) iterator.next();
			String[] batchLine = string.split(" ");
			if (batchLine.length != QUERY_ARGUMENTS_LENGTH) {
				throw new IllegalArgumentException("Passed wrong format of input.\n" +
						"Length passed: " + batchLine.length + " instead of: " + QUERY_ARGUMENTS_LENGTH);
			}
			char operation = batchLine[0].charAt(0);
			int execSrc = Integer.parseInt(batchLine[1]);
			int execDest = Integer.parseInt(batchLine[2]);
			//System.out.println("Before");
			if(operation == 'Q') {



				rwl.readLock().lock();



				if(nodesSet[execSrc-1]==false || nodesSet[execDest-1]==false) {
					addEdge(execSrc-1, execDest-1);
					nodesSet[execSrc-1] = true;
					nodesSet[execDest-1] = true;
				}

				Integer dpResult = lookupDP(execSrc-1, execDest-1);

				int shortestPath = dpResult == null ? shortestPath = query(execSrc-1,execDest-1) : dpResult;


				rwl.readLock().unlock();


				if(shortestPath==0) {
					outputList.add(-1);
				} else {
					outputList.add(shortestPath);
				}
			} else if (operation == 'A') {



				rwl.writeLock().lock();



				dpMap.clear();

				addEdge(execSrc-1, execDest-1);
				nodesSet[execSrc-1] = true;
				nodesSet[execDest-1] = true;



				rwl.writeLock().unlock();



			} else if (operation == 'D') {



				rwl.writeLock().lock();



				dpMap.clear();

				deleteEdge(execSrc-1, execDest-1);
				if(edges[execSrc-1].size()==0) {
					nodesSet[execSrc-1] =false;
				}
				if(edges[execDest-1].size()==0) {
					nodesSet[execDest-1] =false;
				}



				rwl.writeLock().unlock();



			} else {
				throw new UnsupportedOperationException("Unsupported query type: " + operation);
			}
		}
		return outputList;
	}
	

	private int query(int src, int dest) {
		int sp = minEdgeBFS(src, dest, edges.length);
		return sp;
	}
	
	private void addEdge(int src, int dest) { 
		edges[src].add(dest); 
	}

	private void deleteEdge(int src , int dest) {
		int j=edges[src].indexOf(2);
		edges[src].remove(j);
	}
	
	
	private int minEdgeBFS( int u, int v, int n) { 
		// visited[n] for keeping track of visited 
		// node in BFS 
		ArrayList<Boolean> visited = new ArrayList<Boolean>(n);

		for (int i = 0; i < n; i++) {
			visited.add(false); 
		} 

		// Initialize distances as 0 
		ArrayList<Integer> distance = new ArrayList<Integer>(n);

		for (int i = 0; i < n; i++) { 
			distance.add(0); 
		} 

		// queue to do BFS. 
		Queue<Integer> Q = new LinkedList<>();
		distance.set(u, 0); 

		Q.add(u); 
		visited.set(u, true); 
		while (!Q.isEmpty()) { 
			int x = Q.peek(); 
			Q.poll(); 

			for (int i=0; i<edges[x].size(); i++) {
				if (visited.get(edges[x].get(i))) 
					continue; 

				distance.set(edges[x].get(i), distance.get(x) + 1);// update distance for i

				/* Memo in O(1) constant time */
				memoResult(u, edges[x].get(i), distance.get(x) + 1);
				memoResult(x, edges[x].get(i), 1);

				Q.add(edges[x].get(i)); 
				visited.set(edges[x].get(i), true); 
			} 
		}
		return distance.get(v); 
	} 


	/* Dynamic Programming optimization methods */
	private void clearDP() {
		dpMap.clear();
	}

	/**
	 * Looks up the dynamic programming memoized result of shortest path distance from source to destination
	 * if exists.
	 * If memoization does not exist, returns null.
	 */
	private Integer lookupDP(int source, int destination) {
		HashMap<Integer, Integer> resultMap = dpMap.get(source);
		return resultMap != null ? resultMap.get(destination) : null;
	}

	private void memoResult(int source, int destination, int result) {
		HashMap<Integer, Integer> resultMap = dpMap.getOrDefault(source, new HashMap<>());

		resultMap.put(destination, result);

		dpMap.put(source, resultMap);
	}

}
