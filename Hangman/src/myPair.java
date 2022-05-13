import java.util.Comparator;

public class myPair {
	private double Freq;
	private char C;
	myPair(double freq, char c){
		Freq = freq;
		C = c;
	}
	double getFreq(){
		return Freq;
	}
	char getChar(){
		return C;
	}
	static class ComPair implements Comparator<myPair>{

		@Override
		public int compare(myPair p1, myPair p2) {
			return Double.compare(p2.getFreq(), p1.getFreq());
		}
		
	}

	
}
