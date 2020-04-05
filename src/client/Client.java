package client;
import server.GraphProcessingI;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.LinkedList;
import java.util.Scanner;

public class Client extends Thread{
	private String threadName="";
	public Client(String string) {
		// TODO Auto-generated constructor stub
		threadName = string;
	}

	public void run(){
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
		File batchFile = new File("batch"+threadName+".txt");
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
		
		GraphProcessingI obj = null;
		try {
			obj = (GraphProcessingI)Naming.lookup("rmi://localhost/sp");
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NotBoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			obj.setGraph(inputGraph);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		LinkedList<Integer> batchOutput = new LinkedList<Integer>();
		try {
			batchOutput = obj.executeBatch(batch);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		PrintWriter writer=null;
		try {
			writer = new PrintWriter("log"+threadName+".txt");
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		writer.print(batchOutput.toString());
		System.out.println("Shortest path = " + batchOutput.toString());
	}
}
