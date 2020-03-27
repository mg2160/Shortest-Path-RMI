package server;

import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Vector;

public class Graph<T> {
	public static void main(String[] args) {
		Vector <Integer> edges[] = new Vector[10];
		for (int i = 0; i < edges.length; i++) { 
            edges[i] = new Vector<>(); 
        }
		System.out.println(edges[0]);
		
		/*LinkedList<String> gab = new LinkedList<String>();
		gab.add("Q 1 2");
		gab.add("A 2 3");
		gab.add("Q 3 4");
		gab.add("Q 6 7");
		for (Iterator iterator = gab.iterator(); iterator.hasNext();) {
			String string = (String) iterator.next();
			String[] batchLine = string.split(" ");
			char operation = batchLine[0].charAt(0);
			int execSrc = Integer.parseInt(batchLine[1]);
			int execDest = Integer.parseInt(batchLine[2]);
			System.out.println("Before");
			if(operation == 'Q') {
				/*int shortestPath = query(execSrc,execDest);
				outputList.add(shortestPath);*/
				/*System.out.println("Q");
			} else if (operation == 'A') {
				//addEdge(execSrc, execDest);
				System.out.println("Add");
				
			} else if (operation == 'D') {
				//deleteEdge(execSrc, execDest);
				System.out.println("Delete");
			}
		}*/
	}
    
}
 
