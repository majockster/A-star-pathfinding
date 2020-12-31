import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

public class Grid extends JPanel implements ActionListener,MouseListener, KeyListener, MouseMotionListener{
	
	private JFrame window;
	private JPanel map;
	private JPanel controlPanel;
	private JButton [][]Square;
	private JButton Start;
	private JButton Reset;
	private JButton SloMo;
	private int size;
	private int height;
	private int width;
	

	private int RowSquare;
	private int ColSquare;
	private int numStart = 0;
	private int numEnd = 0;
	private int i = 0;
	private int j = 0;
	private int h = 0;
	private int g = 0;
	private int f = 0;
	
	
	
	private char key = (char)0;
	private A_Star_Pathfinding_Algo algo;
	private Node aNode;
	private Timer timer;
	
	public Grid() {
		
		size = 60; 
		height = 1000;
		width = 1500;
		
		
		RowSquare = height/size;
		ColSquare = width/size;
		
		setFocusable(true);
		setFocusTraversalKeysEnabled(false);
		
		algo = new A_Star_Pathfinding_Algo(this, size);
		
		window = new JFrame();
		map = new JPanel();
		controlPanel = new JPanel();
		
		
		
		window.setContentPane(this);
		
		window.setTitle("Welcome to A*Pathfinding Game!");
		window.getContentPane().setPreferredSize(new Dimension(height, width));
		
		window.getContentPane().setLayout(new BorderLayout());
		
		
		controlPanel.setPreferredSize(new Dimension((int)width/8,height));
		
		window.getContentPane().add(map, BorderLayout.CENTER);
		window.getContentPane().add(controlPanel, BorderLayout.WEST);
		
		map.setLayout(new GridLayout(RowSquare,ColSquare));
		controlPanel.setLayout(new BoxLayout(controlPanel,BoxLayout.Y_AXIS));
		addingButtons();
		
		
		
		Start.addActionListener(this);
		Reset.addActionListener(this);
		
		window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		window.pack();
		window.setLocationRelativeTo(null);//Centers window
		window.setVisible(true);
		this.revalidate();
		
	}
	
	public void start() {
		
		if(algo.getStart() != null && algo.getEnd() != null) {
			algo.startFinding();
			
		}
		else {
			System.out.println("MISSING END AND/OR START NODE");
		}
	}
	
	public void clearBoard() {
		
		algo.clear();
		
	}
	public void addingButtons() {
		
		RowSquare = height/size;
		ColSquare = width/size;
	
		setRowSquare(RowSquare);
		setColSquare(ColSquare);
		
		Square = new JButton[RowSquare][ColSquare];
		
		for(int i = 0; i < RowSquare; i++) {
			for(int j = 0; j < ColSquare; j++) {
				Square[i][j] = new JButton();
				Square[i][j].setBackground(Color.white);
				map.add(Square[i][j]);
				Square[i][j].addMouseListener(this);
				Square[i][j].addKeyListener(this);
				Square[i][j].addMouseMotionListener(this);
			
				
			}
		}
		
		Start = new JButton("Start finding path");
		
		Reset = new JButton("Reset");
		//SloMo = new JButton("Show steps");
		
		
		controlPanel.add(Box.createVerticalStrut(130));
		controlPanel.add(Start);
		Start.setAlignmentX(Component.CENTER_ALIGNMENT);
		controlPanel.add(Box.createVerticalStrut(100));
		controlPanel.add(Reset);
		controlPanel.add(Box.createVerticalStrut(100));
		Reset.setAlignmentX(Component.CENTER_ALIGNMENT);
		/*
		controlPanel.add(SloMo);
		controlPanel.add(Box.createVerticalStrut(100));
		SloMo.setAlignmentX(Component.CENTER_ALIGNMENT);
		*/
		Start.setMaximumSize(new Dimension(150,26*3));
		Reset.setMaximumSize(new Dimension(150,26*3));
		//SloMo.setMaximumSize(new Dimension(150,26*3));
	}
	
	
	public void displayNodes() {
		
		i = 0;
		j = 0;
		f = 0;
		h = 0;
		g = 0;
		Node open = null;
		
		//System.out.println("Displaying open nodes");
		
		for(int x = 0; x < algo.getOpenList().size(); x++) {
		
			open = algo.getOpenList().get(x);
			
			if(open.equals(algo.getEnd())) {
				continue;
			}
			f = open.getF();
			g = open.getG();
			h = open.getH();
			
			i = open.getX();
			j = open.getY();
			
			
			
			//System.out.println("Open node at: " + i + "," + j);
			Square[i][j].setBackground(Color.green);
			Square[i][j].setText("<html><b>F="+f+"</b><br>G="+g+"</b><br>H="+h+"</html>");
			Square[i][j].setFont(new Font("Dialog",Font.PLAIN, 9));
			
		}
		
		Node closed = null;
		
		//System.out.println("Displaying closed nodes");
		
		for(int x = 0; x < algo.getClosedList().size(); x++) {
			
			closed = algo.getClosedList().get(x);
			
			if(closed == algo.getStart()) {
				continue;
			}
			f = closed.getF();
			g = closed.getG();
			h = closed.getH();
			
			i = closed.getX();
			j = closed.getY();
			
			
			//System.out.println("Closed node at: " + i + "," + j);
			Square[i][j].setBackground(Color.GRAY);
			Square[i][j].setText("<html><b>F="+f+"</b><br>G="+g+"</b><br>H="+h+"</html>");
			Square[i][j].setFont(new Font("Dialog",Font.PLAIN, 9));
			
		}
		
	
		return;
	}
	
	public void displayPath() {
		
		int i = 0;
		int j = 0;
		
		Node path = null;
		
		//System.out.println("Displaying final path");
		
		for(Node x: algo.getPath()) {
			path = x;
			
			if(path.equals(algo.getStart()) || path.equals(algo.getEnd())) {
				continue;
			}
			i = path.getX();
			j = path.getY();
			Square[i][j].setBackground(Color.orange);
		}
		return;
	}
	
	public void setupPath(MouseEvent e) {
		
		Object source = e.getSource();//An event has a source
		
		if(SwingUtilities.isLeftMouseButton(e)) {
				
				for(int i = 0; i < RowSquare; i++) {
					for(int j = 0; j < ColSquare; j++) {
						
						aNode = new Node(i,j);
						
						if(source == Square[i][j] && numStart == 0 && key == 's') {
							Square[i][j].setBackground(Color.CYAN);
							algo.setStart(aNode);
							numStart++;
						}
						else if (source == Square[i][j] && numEnd == 0 && key == 'e') {
							Square[i][j].setBackground(Color.RED);
							algo.setEnd(aNode);
							numEnd++;
						}
						
					}
				}
			
		}
		
		if(SwingUtilities.isRightMouseButton(e)) {
			for(int i = 0; i < RowSquare; i++) {
				for(int j = 0; j < ColSquare; j++) {
					
					if(source == Square[i][j] && Square[i][j].getBackground().equals(Color.CYAN) && key == 's') {
						Square[i][j].setBackground(Color.white);
						algo.setStart(null);
						numStart--;
					}
					else if (source == Square[i][j] && Square[i][j].getBackground().equals(Color.RED) && key == 'e') {
						Square[i][j].setBackground(Color.white);
						algo.setEnd(null);
						numEnd--;
					}
				
				}
			}
		}
	}
	
	@Override
	public void mouseClicked(MouseEvent e) {
		// TODO Auto-generated method stub
		setupPath(e);
	}
	@Override
	public void mousePressed(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void mouseReleased(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void mouseEntered(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	public void keyTyped(KeyEvent e) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void keyPressed(KeyEvent e) {
		// TODO Auto-generated method stub
		char pressed = e.getKeyChar();
		key = pressed;
	}
	@Override
	public void keyReleased(KeyEvent e) {
		// TODO Auto-generated method stub
		key = (char)0;
	}
	@Override
	public void mouseDragged(MouseEvent e) {
		// TODO Auto-generated method stub
		Object source = e.getSource();
		int offsetX = 0;
		int offsetY = 0;
		int hovingI = 0;
		int hovingJ = 0;
		int buttonHeight = 0;
		int buttonWidth = 0;
		for(int i = 0; i < RowSquare; i++) {
			for(int j = 0; j < ColSquare; j++) {
				
				if(source == Square[i][j]) {
					offsetX = Square[i][j].getX();
					offsetY = Square[i][j].getY();	
					buttonHeight = Square[i][j].getHeight();
					buttonWidth = Square[i][j].getWidth();
				}
			}
		}
		
		hovingJ = (e.getX()+offsetX)/buttonWidth;
		hovingI = (e.getY()+offsetY)/buttonHeight;
		//System.out.println("DRAGGED: " + hovingI + " " + hovingJ);
		//System.out.println("Real: " + e.getY() + " " + e.getX());
		
		if(SwingUtilities.isLeftMouseButton(e)) {
			for(int i = 0; i < RowSquare; i++) {
				for(int j = 0; j< ColSquare; j++) {
					if(hovingI == i && hovingJ == j && Square[i][j].getBackground().equals(Color.CYAN)) {
						return;
					}
					if(hovingI == i && hovingJ == j && Square[i][j].getBackground().equals(Color.RED)) {
						return;
					}
					aNode = new Node(i,j);
					if(hovingI == i && hovingJ == j && key == (char)0) {
						Square[i][j].setBackground(Color.BLACK);
						if(!algo.getObstacles().contains(aNode)) {
							algo.getObstacles().add(aNode);
						}
					}
				
				}
			}
		}
		
		if(SwingUtilities.isRightMouseButton(e)) {
			for(int i = 0; i < RowSquare; i++) {
				for(int j = 0; j< ColSquare; j++) {
				
					if(hovingI == i && hovingJ == j && Square[i][j].getBackground().equals(Color.BLACK) && key == (char)0) {
						Node aNode = new Node(i,j);
						Square[i][j].setBackground(Color.white);
						
						if(algo.getObstacles().contains(aNode)) {
							//System.out.println("OBSTACLE REMOVED");
							algo.getObstacles().remove(aNode);
							/*
							for (Node x : algo.getObstacles()) {
								System.out.println(x.getX() + " " + x.getY());
							}
							*/
						}
						
					}
				}
			}
		}
	}
	@Override
	public void mouseMoved(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		

		Object source = e.getSource();
		
		
		if(Start == source) {
			System.out.println("Starting pathfinding simulation");
			start();
			
		}
		if(Reset == source) {
			System.out.println("Stopping and restarting pathfinding simulation");
			clearBoard();
			
		}
	}
	
	
	public int getRowSquare() {
		return RowSquare;
	}
	public void setRowSquare(int rowSquare) {
		RowSquare = rowSquare;
	}
	public int getColSquare() {
		return ColSquare;
	}
	public void setColSquare(int colSquare) {
		ColSquare = colSquare;
	}
	public JButton[][] getSquare() {
		return Square;
	}
	public void setSquare(JButton[][] square) {
		Square = square;
	}
	public int getNumStart() {
		return numStart;
	}
	public void setNumStart(int numStart) {
		this.numStart = numStart;
	}
	public int getNumEnd() {
		return numEnd;
	}
	public void setNumEnd(int numEnd) {
		this.numEnd = numEnd;
	}
	
	
}
