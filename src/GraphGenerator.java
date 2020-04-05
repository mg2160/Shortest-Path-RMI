import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class GraphGenerator {

	public static void main(String[] args) throws IOException {
		new File("input").mkdir();
		// TODO Auto-generated method stub
		int n = 5000;
		Writer fw = new PrintWriter("input" + java.io.File.separator + "graph.txt");
		int m = n * 10;
		int src[] = new int[m];
		int dst[] = new int[m];
		for(int i =0;i<m;i++) {
			int a = new Random().nextInt(n);
			int b = new Random().nextInt(n);
			if(a == b) {
				i--;
				continue;
			}
			src[i] = a;
			dst[i] = b;
		}
		for(int i = 0;i<m;i++) {
			fw.write(src[i] + " " + dst[i] + "\n");
		}
		fw.write("S");
		fw.flush();
		fw.close();
		for(int z = 0;z<15;z++) {
			fw = new PrintWriter("input" + java.io.File.separator + "batch" + z + ".txt");
			List<Character> op = new ArrayList<Character>();
			List<Integer> srcQ = new ArrayList<Integer>();
			List<Integer> dstQ = new ArrayList<Integer>();
			for(int i = 0;i<m;i++) {
				op.add('Q');
				srcQ.add(new Random().nextInt(n));
				dstQ.add(new Random().nextInt(n));
				if(i % 1000 == 0) {
					op.add('D');
					int r = new Random().nextInt(n);
					srcQ.add(src[r]);
					dstQ.add(dst[r]);
				}
				if(i % 1000 == 1) {
					int a = new Random().nextInt(n);
					int b = new Random().nextInt(n);
					if(a != b) {
						op.add('A');
						srcQ.add(a);
						dstQ.add(b);
					}
				}
			}
			for(int i = 0;i<op.size();i++) {
				fw.write(op.get(i) + " " + srcQ.get(i) + " " + dstQ.get(i) + "\n");
			}
			fw.flush();
			fw.close();
		}
	}
	

}
