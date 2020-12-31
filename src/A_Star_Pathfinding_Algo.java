import java.awt.Color;
import java.util.ArrayList;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.Timer;

public class A_Star_Pathfinding_Algo{
	private Node start,end,current;
	private ArrayList<Node> openList, closedList, obstacles, path, surroundings;
	private Grid grid;
	private int size;
	private int Gcost = 0;
	private int Hcost = 0;
	private int Fcost = 0;
	
	
	private int lowestG = 0;
	private int lowestF = 0;
	
	
	private boolean isInOpen = false;
	
	
	
	public A_Star_Pathfinding_Algo(Grid grid, int size) {
		this.grid = grid;
		this.size = size;
		this.start = getStart();
		this.end = getEnd();
		
		openList = new ArrayList<Node>();
		closedList = new ArrayList<Node>();
		obstacles = new ArrayList<Node>();
		path = new ArrayList<Node>();
		surroundings = new ArrayList<Node>();
		
	
	}
	public void startFinding() {
		openList.add(start);
		surroundings.add(end);
		start.setG(0);
		start.setH(0);
		start.setF(0);
		searchNodes();
	}
	public void clear() {
		openList.removeAll(openList);
		closedList.removeAll(closedList);
		obstacles.removeAll(obstacles);
		path.removeAll(path);
		surroundings.removeAll(surroundings);
		start = null;
		end = null;
		current = null;
		
		int boundI = 0;
		int boundJ = 0;
		int numEND = grid.getNumEnd();	
		int numSTART = grid.getNumStart();
		
		boundI = grid.getRowSquare();
		boundJ = grid.getColSquare();
		
		for(int i = 0; i < boundI; i++) {
			for(int j = 0; j < boundJ; j++) {
				
				if(!grid.getSquare()[i][j].getBackground().equals(Color.white)) {
					grid.getSquare()[i][j].setBackground(Color.white);
					grid.getSquare()[i][j].setText("");
				}
			}
		}
		numEND--;
		numSTART--;
		grid.setNumEnd(numEND);
		grid.setNumStart(numSTART);
	}
	public void searchNodes() {
		
		this.current = start;
		
		while(openList.size() != 0) {
		
		//System.out.println("Current: " + current.getX() + " " + current.getY() + " with f: " + current.getF());
		//Searching node with lowest f cost from open list and adding it to closed list
		lowestF = openList.get(0).getF();
		for(int x = 0; x < openList.size(); x++) {
			int currentF = openList.get(x).getF();
			if(currentF < lowestF) {
				lowestF = currentF;
			}
		}
		
		
		for(int x = 0; x < openList.size(); x++) {
			
			if(openList.get(x).getF() == lowestF) {
				current = openList.get(x);
				//System.out.println("CURRENT CHANGED TO: " + current.getX() + " " + current.getY() + " with f: " + current.getF());
				
				if(current.getY() == end.getY() && current.getX() == end.getX()) {
					//System.out.println("current X: "+current.getX() + ", end X: " + end.getX());
					//System.out.println("current Y: " + current.getY() + ", end Y: " + end.getY());
					//System.out.println("END NODE FOUND");
					
					findPath();
					return;
				}
				
				openList.remove(x);
				//System.out.println("Removing node from OPEN...");
				//System.out.println("Size of open: " + openList.size());
				closedList.add(current);
				//System.out.println("Adding node to CLOSED...");
				generateSurroundings(current);
				
			
				
				grid.displayNodes();
				
				
				
			}
		}
		
		
		}
			
			System.out.println("NO PATH FOUND");
		}
	
	public void generateSurroundings(Node current) {
		int boundI = 0;
		int boundJ = 0;
		
		boundI = grid.getRowSquare();
		boundJ = grid.getColSquare();
		//Explore current node surroundings(open nodes) and calculate f cost
		for(int i = 0; i < boundI; i++) {
			for(int j = 0; j < boundJ; j++) {
				Node aNode = new Node(i,j);
				
				if(obstacles.contains(aNode)) {
					//System.out.println("Node already in obstacle " + i + " " + j);
					continue;
				}
				
				if(closedList.contains(aNode)) {
					//System.out.println("Node already in closed");
					continue;
				}
				
				
				if(Math.abs(current.getX() - i) == 1 && current.getY() - j == 0) {
					
					//System.out.println("Adding node to OPEN...");
					if(!openList.contains(aNode)) {
						isInOpen = false;
						openList.add(aNode);
					}
					else {
						isInOpen = true;
					}
					calculateF(aNode);
					
				}
				if(Math.abs(current.getY() - j) == 1 && current.getX() - i == 0) {
					
					//System.out.println("Adding node to OPEN...");
					if(!openList.contains(aNode)) {
						isInOpen = false;
						openList.add(aNode);
					}
					else {
						isInOpen = true;
					}
					calculateF(aNode);
					
				}
				if(Math.abs(current.getX() - i) == 1 && Math.abs(current.getY() - j) == 1) {
					
					//System.out.println("Adding node to OPEN...");
					if(!openList.contains(aNode)) {
						isInOpen = false;
						openList.add(aNode);
					}
					else {
						isInOpen = true;
					}
					calculateF(aNode);
					
				}

			}
		}
		return;
	}
	public void calculateF(Node open) {
		
		int g = 0;
		int h = 0;
		int f = 0;
		
		g =(int)(Math.sqrt(Math.pow(Math.abs(open.getX() - current.getX()), 2) + Math.pow(Math.abs(open.getY() - current.getY()),2))*10)  + current.getG();
		h = (int)(Math.sqrt(Math.pow(Math.abs(open.getX() - end.getX()), 2) + Math.pow(Math.abs(open.getY() - end.getY()),2))*10);
		f = g + h;
		
		for(int x = 0; x < openList.size(); x++) {
			
			if(isInOpen == true && openList.get(x).equals(open) && openList.get(x).getF() > f) {
				//System.out.println("surrounding already in open, checking if f cost update needed...");
				//System.out.println("Old f: " + openList.get(x).getF());
				//System.out.println("New f: " + f);
				//System.out.println("f cost will be updated!");
				openList.get(x).setG(g);
				openList.get(x).setH(h);
				openList.get(x).setF(f);
			}
			
			if(isInOpen == true && openList.get(x).equals(open) && openList.get(x).getF() < f) {
				//System.out.println("surrounding already in open, checking if f cost update needed...");
				//System.out.println("Old f: " + openList.get(x).getF());
				//System.out.println("New f: " + f);
				//System.out.println("f cost will not be updated, function will return...");
				return;
			}
			
		}
		open.setG(g);
		open.setH(h);
		open.setF(f);
		//System.out.println("Open X: "+open.getX() + " Open Y: "+open.getY());
		//System.out.println("Open g cost: "+g);
		//System.out.println("Open h cost: "+h);
		//System.out.println("F cost: "+f);
		return;
	}
	
	public void findPath() {
		
		end.setG(0);
		this.current = end;
		
		while(surroundings.size() != 0) {
		
		//System.out.println("Current: " + current.getX() + " " + current.getY() + " with G: " + current.getG());	
		//Searching node with lowest g cost from closed list and adding it to path list
		lowestG = surroundings.get(0).getG();
		for(int x = 0; x < surroundings.size(); x++) {
			int currentG = surroundings.get(x).getG();
			if(currentG < lowestG) {
				lowestG = currentG;
			}
		}
		//System.out.println(lowestG);
		for(int x = 0; x < surroundings.size(); x++) {
			
			if(surroundings.get(x).getG() == lowestG) {
				current = surroundings.get(x);
				//System.out.println("CURRENT CHANGED TO: " + current.getX() + " " + current.getY() + " with G: " + current.getG());
				if(current.getY() == start.getY() && current.getX() == start.getX()) {
					grid.displayPath();
					return;
				}
				
				surroundings.remove(x);
				path.add(current);
				generateSurroundings2(current);
				break;
			}
		}
		
		
		}
	}
	
	public void generateSurroundings2(Node current) {
		
		int boundI = 0;
		int boundJ = 0;
		
		boundI = grid.getRowSquare();
		boundJ = grid.getColSquare();
		//Explore current node surroundings(closed nodes)
		for(int i = 0; i < boundI; i++) {
			for(int j = 0; j < boundJ; j++) {
				Node aNode = new Node(i,j);
				
				if(obstacles.contains(aNode)) {
					//System.out.println("Node already in obstacle " + i + " " + j);
					continue;
				}
				
				if(openList.contains(aNode)) {
					//System.out.println("Node already in open");
					continue;
				}
				
				if(Math.abs(current.getX() - i) == 1 && current.getY() - j == 0) {
					
					for(int x = 0; x < closedList.size(); x++) {
						if(closedList.get(x).equals(aNode)) {
							surroundings.add(closedList.get(x));
							break;
						}
					}
					
				}
				if(Math.abs(current.getY() - j) == 1 && current.getX() - i == 0) {
					
					for(int x = 0; x < closedList.size(); x++) {
						if(closedList.get(x).equals(aNode)) {
							surroundings.add(closedList.get(x));
							break;
						}
					}
					
				}
				if(Math.abs(current.getX() - i) == 1 && Math.abs(current.getY() - j) == 1) {
					
					for(int x = 0; x < closedList.size(); x++) {
						if(closedList.get(x).equals(aNode)) {
							surroundings.add(closedList.get(x));
							break;
						}
					}
					
				}
				
				
			}
		}
		
	}
	public Node getStart() {
		return start;
	}

	public void setStart(Node start) {
		this.start = start;
	}

	public Node getEnd() {
		return end;
	}

	public void setEnd(Node end) {
		this.end = end;
	}

	public ArrayList<Node> getOpenList() {
		return openList;
	}

	public void setOpenList(ArrayList<Node> openList) {
		this.openList = openList;
	}

	public ArrayList<Node> getClosedList() {
		return closedList;
	}

	public void setClosedList(ArrayList<Node> closedList) {
		this.closedList = closedList;
	}

	public ArrayList<Node> getObstacles() {
		return obstacles;
	}

	public void setObstacles(ArrayList<Node> obstacles) {
		this.obstacles = obstacles;
	}


	public Node getCurrent() {
		return current;
	}


	public void setCurrent(Node current) {
		this.current = current;
	}


	public int getGcost() {
		return Gcost;
	}


	public void setGcost(int gcost) {
		Gcost = gcost;
	}


	public int getHcost() {
		return Hcost;
	}


	public void setHcost(int hcost) {
		Hcost = hcost;
	}


	public int getFcost() {
		return Fcost;
	}


	public void setFcost(int fcost) {
		Fcost = fcost;
	}


	public int getLowestG() {
		return lowestG;
	}


	public void setLowestG(int lowestG) {
		this.lowestG = lowestG;
	}

	public int getLowestF() {
		return lowestF;
	}

	public void setLowestF(int lowestF) {
		this.lowestF = lowestF;
	}
	
	public ArrayList<Node> getPath() {
		return path;
	}
	
	public void setPath(ArrayList<Node> path) {
		this.path = path;
	}
	
	
	
}
