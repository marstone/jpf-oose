package studio.numeric;

public class Max {

	public static void main(String[] args) {
		max(3, 5);
	}
	
	public static void max(int x, int y) {
		if(x > y) {
			System.out.println("x bigger");
		} else {
			System.out.println("x not bigger");			
		}
	}
	
}
