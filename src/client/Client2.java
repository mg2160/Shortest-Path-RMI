package client;
import server.IGraphProcessing;

import java.io.File;
import java.io.FileNotFoundException;
import java.rmi.Naming;
import java.util.List;
import java.util.LinkedList;
import java.util.Scanner;

public class Client2 {
	public static void main(String[] args) throws Exception{
		File batchFile = new File("batch.txt");
		Scanner sc2=null;
		try {
			sc2 = new Scanner(batchFile);
		} catch (FileNotFoundException e) {
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
		
		IGraphProcessing obj = (IGraphProcessing)Naming.lookup("rmi://localhost/sp");		
		List<Integer> batchOutput = obj.executeBatch(batch);
		System.out.println("Shortest path = " + batchOutput.toString());
	}
}
