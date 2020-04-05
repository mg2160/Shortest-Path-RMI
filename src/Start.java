import server.Server;
import client.*;

public class Start {

	public static void main(String[] args) throws Exception {
		// TODO Auto-generated method stub
		Server s = new Server();
		s.runServer();
		for(int i=1; i<4; i++){
		      new Client(i+""){}.start();
		}

}
}