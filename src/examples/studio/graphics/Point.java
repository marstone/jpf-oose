package studio.graphics;

public class Point {

	int x;
	int y;
	
	public Point(int x, int y) {
		this.x = x;
		this.y = y;
	}
	
	public int distanceTo(int a, int b) {
		int result = 0;
		if(a > this.x) {
			result += (a - this.x);
		} else {
			result += (this.x - a);
		}
		if(b > this.y) {
			result += (b - this.y);
		} else {
			result += (this.y - b);
		}
		return result;
	}
	
	public int quadrant() {
		if(this.x > 0) {
			if(this.y > 0) {
				return 0; 
			}
			return 1;
		}
		if(this.y <= 0) {
			return 3;
		}
		return 4;
	}
	
}
