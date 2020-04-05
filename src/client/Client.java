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
import java.util.Random;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Scanner;

public class Client extends Thread{
	private String clientId;
	private Writer writer;
	private String serverUrl;
	public Client(String clientId, String serverUrl) {
		this.clientId = clientId;
		this.serverUrl = serverUrl;
		try {
			new File("logs").mkdir();
			this.writer = new PrintWriter("logs/log" + clientId + ".txt");
		} catch (IOException e) {
			e.printStackTrace();
			writer = null;
		}
	}

	public void run(){
		File batchFile = new File("input" + java.io.File.separator + "batch" + clientId + ".txt"); 
		Scanner sc2=null;
		try {
			sc2 = new Scanner(batchFile);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		
		IGraphProcessing obj = null;
		try {
			obj = (IGraphProcessing)Naming.lookup(serverUrl);
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (RemoteException e) {
			e.printStackTrace();
		} catch (NotBoundException e) {
			e.printStackTrace();
		}
		double initialTime = System.nanoTime();
		List<Integer> results = new ArrayList<Integer>();
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
				results.addAll(result);
				double requestTime = (System.nanoTime() - startTime) / 1000000.0;
				this.writeTofile(batchLine, result, requestTime, false);
//				Thread.sleep(new Random().nextInt(10001));
			} catch (Exception e) {
				System.out.println("Failed to execute request: " + batchLine);
				e.printStackTrace();
			}
		}
		this.writeTofile("", null, (System.nanoTime() - initialTime) / 1000000.0, true);
		try {
			this.writer.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		System.out.println("Client " + this.clientId + " shortest paths = " + results.toString());
		System.out.println("Client " + this.clientId + " has finished executing!");
	}
	
	private void writeTofile(String batchLine, List<Integer> results, double time, boolean isBatch) {
		if(results != null && results.isEmpty())
			results.add(-1);

		String[] batch = batchLine.split(" ");
		String logLine = "timestamp=" + System.nanoTime();
		logLine += isBatch? ", operation=B": ", operation=" + batch[0];
		logLine += isBatch? ", src=-1": ", src=" + batch[1];
		logLine += isBatch? ", dest=-1": ", dest=" + batch[2];
		logLine += isBatch? ", result=-1": ", result=" + results.get(0); 
		logLine += ", latency=" + time + "\n";
		try {
			writer.append(logLine);
			writer.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
