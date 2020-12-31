
public class Node {
	
	private int x;
	private int y;
	private int G;
	private int H;
	private int F;
	private Node parent;
	public Node() {
		
	}
	public Node(int x, int y) {
		this.x = x;
		this.y = y;
	}
	
	public int getX() {
		return x;
	}
	public void setX(int x) {
		this.x = x;
	}
	public int getY() {
		return y;
	}
	public void setY(int y) {
		this.y = y;
	}
	public int getG() {
		return G;
	}
	public void setG(int g) {
		G = g;
	}
	public int getH() {
		return H;
	}
	public void setH(int h) {
		H = h;
	}
	public int getF() {
		return F;
	}
	public void setF(int f) {
		F = f;
	}
	public Node getParent() {
		return parent;
	}
	public void setParent(Node parent) {
		this.parent = parent;
	}
	public boolean equals(Object b) {
		if(b == null || this == null ||this.getClass() != b.getClass()) {
	
			return false;
		}
		else {
			Node x = (Node) b;
			return (this.x == x.x && this.y == x.y);
		}
	}

}
