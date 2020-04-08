import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class GraphGenerator {

	public static void main(String[] args) throws IOException {
		new File("input").mkdir();
		int n = args.length > 2 ? Integer.parseInt(args[2]): 1000;
		Writer fw = new PrintWriter("input" + java.io.File.separator + "graph.txt");
		int m = args.length > 3 ? Integer.parseInt(args[3]): n * 10;
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
		int ratio = args.length > 1 ? Integer.parseInt(args[1]): 1000;
		for(int z = 0; z < (args.length > 0 ? Integer.parseInt(args[0]): Start.CLIENTS_COUNT); z++) {
			fw = new PrintWriter("input" + java.io.File.separator + "batch" + z + ".txt");
			List<Character> op = new ArrayList<Character>();
			List<Integer> srcQ = new ArrayList<Integer>();
			List<Integer> dstQ = new ArrayList<Integer>();
			for(int i = 0;i<m;i++) {
				op.add('Q');
				srcQ.add(new Random().nextInt(n));
				dstQ.add(new Random().nextInt(n));
				if(i % ratio == 0) {
					op.add('D');
					int r = new Random().nextInt(n);
					srcQ.add(src[r]);
					dstQ.add(dst[r]);
				}
				if(i % ratio == 1) {
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
