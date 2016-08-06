package studio.graphics;

public class Max {
		
	public static void main(String[] args) {
		Point p1 = new Point(2, 8);
		Point p2 = new Point(3, 5);
		max(p1, p2);
	}
	
	public static void max(Point p1, Point p2) {
		if(p1.distanceTo(0, 0) > p2.distanceTo(0, 0)) {
			System.out.println("p1 is farther");
		} else {
			System.out.println("p1 is not farther");	
		}
	}
	
}
