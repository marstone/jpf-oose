package studio.graphics;

public class Fields {

	public static void main(String[] args) {
		Point p = new Point(2, 8);
		fields(p, 5);
	}
	
	public static void fields(Point p, int x) {
		// p.x = x;
		// int x2 = p.x;
		// if(x2 > 0) {
		if(p.x > 0) {
			System.out.println("field x > 0");
		} else {
			System.out.println("field x not > 0");		
		}
	}
	
}
