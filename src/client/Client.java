package client;
import server.IGraphProcessing;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.Writer;
import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.List;
import java.util.LinkedList;
import java.util.Scanner;

public class Client extends Thread{
	private String clientId;
	private Writer writer;
	public Client(String clientId) {
		this.clientId = clientId;
		try {
			new File("logs").mkdir();
			this.writer = new PrintWriter("logs/log" + clientId + ".txt");
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			writer = null;
		}
	}

	public void run(){
		File batchFile = new File("batch" + clientId + ".txt"); 
		Scanner sc2=null;
		try {
			sc2 = new Scanner(batchFile);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		
		IGraphProcessing obj = null;
		try {
			obj = (IGraphProcessing)Naming.lookup("rmi://localhost/sp");
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (RemoteException e) {
			e.printStackTrace();
		} catch (NotBoundException e) {
			e.printStackTrace();
		}
		
		while (sc2.hasNext()) {
			List<String> batch = new LinkedList<String>();
			String batchLine = sc2.nextLine();
			if(batchLine.equals("F")) {
				break;
			}
			batch.add(batchLine);
			try {
				long startTime = System.nanoTime();
				List<Integer> result = obj.executeBatch(batch);
				long endTime = System.nanoTime();
				this.writeTofile(batchLine, result, (endTime - startTime) / 1000000.0);
			} catch (Exception e) {
				System.out.println("Failed to execute request: " + batchLine);
				e.printStackTrace();
			}
		}
		
		try {
			this.writer.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private void writeTofile(String batchLine, List<Integer> results, double time) {
		if(results.isEmpty())
			results.add(-1);
		String logLine = "";
		String[] batch = batchLine.split(" ");
		logLine += "operation=" + batch[0] + ", src=" + batch[1] + ", dest=" + batch[2] + ", result=" + results.get(0) + ", time=" + time + "\n";
		try {
			writer.append(logLine);
			writer.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
