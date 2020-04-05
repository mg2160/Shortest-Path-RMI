package server;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.Writer;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.HashSet;
import java.util.HashMap;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

@SuppressWarnings("serial")
public class GraphProcessing extends UnicastRemoteObject implements IGraphProcessing{

	private static final int QUERY_ARGUMENTS_LENGTH = 3;
	private Lock lck = new ReentrantLock();
	private boolean memo;
	private Writer writer;


	/**
	 * A mapping of previously source node path to a HashMap taking destination as key and result value
	 * Source -> < Destination -> Distance of shortest path >
	 */
	private HashMap<Integer, HashMap<Integer, Integer>> dpMap = new HashMap<>();
	private HashMap<Integer, HashSet<Integer>> edges = new HashMap<Integer, HashSet<Integer>>();
	
	public GraphProcessing(int port, boolean memo) throws RemoteException {
        super(port);
        this.memo = memo;
        try {
			new File("logs").mkdir();
			this.writer = new PrintWriter("logs/server-log" + (memo? "-memo": "") + ".txt");
		} catch (IOException e) {
			e.printStackTrace();
			writer = null;
		}   
    }

	@Override
	public void setGraph(List<String> graph) throws Exception {

		for (Iterator<String> iterator = graph.iterator(); iterator.hasNext();) {
			String string = iterator.next();
			String[] instruction = string.split(" ");
			int source = Integer.parseInt(instruction[0]);
			int destination = Integer.parseInt(instruction[1]);
			addEdge(source, destination);
		}
	}

	@Override
	public List<Integer> executeBatch(List<String> lines) throws Exception {
		List<Integer> outputList = new LinkedList<Integer>();
		for (Iterator<String> iterator = lines.iterator(); iterator.hasNext();) {
			String string = iterator.next();
			String[] batchLine = string.split(" ");
			if (batchLine.length != QUERY_ARGUMENTS_LENGTH) {
				throw new IllegalArgumentException("Passed wrong format of input.\n" +
						"Length passed: " + batchLine.length + " instead of: " + QUERY_ARGUMENTS_LENGTH);
			}
			char operation = batchLine[0].charAt(0);
			int execSrc = Integer.parseInt(batchLine[1]);
			int execDest = Integer.parseInt(batchLine[2]);
			Integer shortestPath = null;
			long startTime = System.nanoTime();
			if(operation == 'Q') {
				shortestPath = this.memo? lookupDP(execSrc, execDest): null;
				if(shortestPath == null) {
					lck.lock();
					shortestPath = query(execSrc,execDest);
					lck.unlock();
				}
				outputList.add(shortestPath);
			} else if (operation == 'A') {
				lck.lock();
				addEdge(execSrc, execDest);
				lck.unlock();
			} else if (operation == 'D') {
				lck.lock();
				deleteEdge(execSrc, execDest);
				lck.unlock();
			} else {
				throw new UnsupportedOperationException("Unsupported query type: " + operation);
			}
			this.writeTofile(string, shortestPath == null? -1: shortestPath, (System.nanoTime()-startTime)/ 1000000.0);
		}
		return outputList;
	}
	

	private int query(int src, int dest) {
		int sp = minEdgeBFS(src, dest);
		return sp;
	}
	
	private void addEdge(int src, int dest) {
		if(!edges.containsKey(src))
			edges.put(src, new HashSet<Integer>());
		edges.get(src).add(dest);
		clearDP();
	}

	private void deleteEdge(int src , int dest) {
		if(!edges.containsKey(src))
			return;
		HashSet<Integer> srcEdges = edges.get(src);
		srcEdges.remove(dest);
		clearDP();
	}
	
	
	private int minEdgeBFS( int u, int v) {
		HashMap<Integer, Integer> distance = dpMap.getOrDefault(u, new HashMap<Integer, Integer>());
		distance.put(u, 0);

		// queue to do BFS. 
		Queue<Integer> Q = new LinkedList<>(distance.keySet());

		while (!Q.isEmpty()) {
			int x = Q.peek();
			Q.poll();
			
			for (Integer dest : edges.getOrDefault(x, new HashSet<Integer>())) {
				if (distance.containsKey(dest)) 
					continue;
				
				int newDist = distance.get(x) + 1;
				distance.put(dest, newDist); // update distance for dest
		
				if(newDist == v)
					break; // no need to compute the rest of BFS since it may be invalidated soon.
				Q.add(dest); 
			} 
		}
		// Keep for the future
		memoResult(u, distance);
		return distance.getOrDefault(v, -1);
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
		return resultMap != null ? resultMap.getOrDefault(destination, null): null;
	}

	private void memoResult(int source, HashMap<Integer, Integer> result) {
		dpMap.put(source, result);
	}
	
	private void writeTofile(String batchLine, Integer result, double time) {
		String[] batch = batchLine.split(" ");
		String logLine = "timestamp=" + System.nanoTime();
		logLine += ", operation=" + batch[0];
		logLine += ", src=" + batch[1];
		logLine += ", dest=" + batch[2];
		logLine += ", result=" + result;
		logLine += ", latency=" + time + "\n";
		try {
			writer.append(logLine);
			writer.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
