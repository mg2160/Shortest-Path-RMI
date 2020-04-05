package client;
import server.GraphProcessingI;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.rmi.Naming;
import java.util.LinkedList;
import java.util.Scanner;

public class Client2 {
	public static void main(String[] args) throws Exception{
		//File file = new File("src" + java.io.File.separator + "input.txt");
		File file = new File("input.txt");
		Scanner sc=null;
		try {
			sc = new Scanner(file);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		LinkedList<String> inputGraph = new LinkedList<String>(); 
		while (sc.hasNext()) {
			String line=sc.nextLine();
			if(line.equals("S")) {
				break;
			}
			inputGraph.add(line);
			System.out.println(line);
		}
		
		//File batchFile = new File("src" + java.io.File.separator + "batch.txt"); 
		File batchFile = new File("batch2.txt");
		Scanner sc2=null;
		try {
			sc2 = new Scanner(batchFile);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		LinkedList<String> batch = new LinkedList<String>(); 
		while (sc2.hasNext()) {
			String batchLine=sc2.nextLine();
			if(batchLine.equals("F")) {
				break;
			}
			batch.add(batchLine);
			System.out.println(batchLine);
		}
		
		GraphProcessingI obj = (GraphProcessingI)Naming.lookup("rmi://localhost/sp");
		obj.setGraph(inputGraph);
		
		LinkedList<Integer> batchOutput = new LinkedList<Integer>();
		batchOutput = obj.executeBatch(batch);
		System.out.println("Shortest path = " + batchOutput.toString());
	}
}
