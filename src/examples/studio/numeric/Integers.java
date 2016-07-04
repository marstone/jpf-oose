package studio.numeric;

public class Integers {
	
	public static void main(String[] args) {
		add(2, 8);
	}
	
	public static int add(int x, int y) {
		int s = x + y;
		System.out.println(s);
		if(s > 100) {
			System.out.println("bigger than 100");
		}
		return s;
	}
	
}
