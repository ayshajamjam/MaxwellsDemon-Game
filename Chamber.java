import java.awt.*;
import java.awt.event.*;
import javax.swing.*;


import java.math.*;
import java.text.DecimalFormat;


public class Chamber {
	
	int downX, downY = -1;
	int mouseX, mouseY = -1;
	
	boolean openDoor = false;
	
	// Model: State of the balls
	Ball[] balls;
	int ballCount;
	
	// View: Displaying the balls
	JFrame f;

	Game gamePanel;
	
	JPanel buttonPanel = new JPanel();
	TempDisplay tempDisp;
	
	JButton addB = new JButton("add");
	JButton resetB = new JButton("reset");
	
	
	class ButtonListener implements ActionListener {
		
		@Override
		public void actionPerformed(ActionEvent e) {
			

			if(e.getSource() == addB) {
				if(ballCount < 100) {
					balls[ballCount++] = new Ball("blue", "left");
					balls[ballCount++] = new Ball("blue", "right");
					balls[ballCount++] = new Ball("red","left");
					balls[ballCount++] = new Ball("red", "right");
				}
			}
			if(e.getSource() == resetB) {
				ballCount = 0;
			}
		}
	}
	
	
	public String computeTempLeft() {
		
		// T = (mv^2)/3 (Replacing mass with 1 and Boltzmann constant with constant 1 for convenience)
		// Units: degreesMagic
		
		double tempSum = 0;
		double tempAvg = 0;
		
		for(int i = 0; i < ballCount; i++) {
			if(balls[i].x < 285) {
				tempSum += Math.pow(balls[i].speed, 2);
			}
		}
		
		tempAvg = tempSum / balls.length;
		
		return String.valueOf(tempAvg);
	}
	
	public String computeTempRight() {
		
		// T = (mv^2)/3 (Replacing Boltzmann constant with constant 1 for convenience)
		// Units: degreesMagic
		
		double tempSum = 0;
		double tempAvg = 0;
		
		for(int i = 0; i < ballCount; i++) {
			if(balls[i].x > 285) {
				tempSum += Math.pow(balls[i].speed, 2);
			}
		}
		
		tempAvg = tempSum / balls.length;
		
		return String.valueOf(tempAvg);
	}
	
	
	public static void main(String[] args) {
		new Chamber();
	}
	
	public Chamber() {
		
		// Setup the frame
		f = new JFrame();
		f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		f.setTitle("Maxwell's Demon");
		f.setBackground(Color.white);
		f.setResizable(false);
		f.setLayout(new BorderLayout());
		
		gamePanel = new Game();
		tempDisp = new TempDisplay();
	
		
		// Set up the tempDisp, gamePanel and buttonPanel
		tempDisp.setBackground(Color.decode("#f7DDe4"));
		gamePanel.setBackground(Color.white);
		buttonPanel.setBackground(Color.decode("#f7d5e4"));
		
		buttonPanel.setLayout(new GridLayout(1,2));
		buttonPanel.add(addB);
		buttonPanel.add(resetB);
		
		ButtonListener b = new ButtonListener();
		addB.addActionListener(b);
		resetB.addActionListener(b);
		
		// Initialize the model; initial setup
		ballCount = 0; // start with empty chambers
		balls = new Ball[100];
		for(int i = 0; i < ballCount; i++) {
			balls[i] = new Ball();
			
		}

		// Create a timer
		Timer tick = new Timer(100, new Animator());
		tick.start();
		
		f.add(tempDisp, BorderLayout.NORTH);
		f.add(gamePanel, BorderLayout.CENTER);
		f.add(buttonPanel, BorderLayout.SOUTH);
		
		
		// An anonymous MouseAdapter to open the door
		gamePanel.addMouseListener(new MouseAdapter() {
			public void mousePressed(MouseEvent e) {
				// Open door
				openDoor = true;
			}
			public void mouseReleased(MouseEvent e) {
				// Close door
				openDoor = false;
			}
			public void mouseDragged(MouseEvent e) {}
		});
		
		f.setSize(570,590);
		f.setVisible(true);
	}
	
	
	// Model: encapsulation of a single ball
	public class Ball {
		private int x, y;
		private int vx, vy; // pixels per second
		private double oldx, oldy;
		
		// pixels per inch
		double pixelsperinch = Toolkit.getDefaultToolkit().getScreenResolution();
		// pixels per cm
		double pixelspercm = pixelsperinch / 2.54;
		
		private String colorTemp = null;
		private int speed = 0;
		private int theta = 0;
		
		public Ball() {
			x = (int) (Math.random() * 400 + 100); // [100, 500)
			y = (int) (Math.random() * 400 + 100);
			
			theta = (int)(Math.random() * (2 * Math.PI));
			
			speed = (int) (Math.random() * (4 * pixelspercm) + (2 * pixelspercm));
			vx = (int) (speed * Math.cos(theta));
			vy = (int) (speed * Math.sin(theta));
			
			speed = (int)(Math.sqrt(Math.pow((vx/pixelspercm), 2) + Math.pow((vy/pixelspercm), 2)));
			
			if(speed >= 2 && speed < 4) {
				colorTemp = "blue";
			}
			else if(speed >= 4 && speed < 6) {
				colorTemp = "red";
			}
		}
	
		public Ball(String sp, String pos) {
			

			// Determine position
			y = (int) (Math.random() * 400 + 100); //[100, 500)
			
			if(pos == "left") {
				x = (int) (Math.random() * 200 + 50); // [50, 250)
			}
			else if(pos == "right") {
				x = (int) (Math.random() * 200 + 300); // [300, 500)
			}
			
			theta = (int)(Math.random() * (2 * Math.PI));
			
			// Determine speed
			if(sp == "red") {
				speed = (int) (Math.random() * (2 * pixelspercm) + (4 * pixelspercm));
			}
			else if(sp == "blue") {
				speed = (int) (Math.random() * (2 * pixelspercm) + (2 * pixelspercm));
			}
			
			vx = (int) (speed * Math.cos(theta));
			vy = (int) (speed * Math.sin(theta));
			
			speed = (int)(Math.sqrt(Math.pow((vx/pixelspercm), 2) + Math.pow((vy/pixelspercm), 2)));
			
			colorTemp = sp;	
			
		}
		
		// Door open
		public void moveFree(double delta) {
			x += vx * delta;
			y += vy * delta;
			
			stayOnScreen(delta);
		}
		
		// Door closed
		public void move(double delta) {
			
			double newx = (x + (vx * delta));
			
			if (x < 285 && newx >= 284) { // from left
				vx *= -1;

				if (newx >= 285) {
					x = 284;
					y = (int)(y + (vy * delta * ((284-x)/vx)));
				}
				
			}
			else if(x > 285 && newx <= 286) { // from right
				vx *= -1;
				
				if (newx <= 285) {
					x = 286;
					y = (int)(y + (vy * delta * ((286-x)/vx)));
				}
			}
			else {
				x += vx * delta;
			}
			y += vy * delta;
			
			stayOnScreen(delta);
		}

		public void stayOnScreen(double delta) {
			// Check bounces off each edge of screen
			
			double newx = (x + (vx * delta));
			double newy = (y + (vy * delta));
			

			if (x > 0 && newx <= 2) { //left wall
				vx *= -1;
				x = 1;
				y = (int)(y + (vy * delta * ((1-x)/vx)));
			
			}
			if (x < 570 && newx >= 568) { //right wall
				vx *= -1;
				x = 567;
				y = (int)(y + (vy * delta * ((567-x)/vx)));
				
			}
			if (y > 0 && newy <= 4) { //top wall
				vy *= -1;
				y = 1;
				x = (int)(x + (vx * delta * ((1-y)/vy)));
				
			}
			if (y < 590 && newy >= 524) { //bottom wall
				vy *= -1;
				y = 523;
				x = (int)(x + (vx * delta * ((523-y)/vy)));
				
			}
			
		}
		
		// Balls draw themselves at current position
		public void draw(Graphics g, Ball b) {
			
			// Set color based on temperature
			if(b.colorTemp == "red") {
				g.setColor(Color.red);
			}
			else if(b.colorTemp == "blue") {
				g.setColor(Color.blue);
			}
			g.fillOval((int) (x - 2), (int) (y - 2), 5, 5);
		}
		
	}
	
	public class TempDisplay extends JPanel {
		
		@Override
		public void paintComponent(Graphics g) {
			super.paintComponent(g);
			
			g.setColor(Color.black);
			Font myFont=new Font("TimesRoman",Font.BOLD,13);
			g.setFont(myFont);
			g.drawString(computeTempLeft(), 0, 10 );
			g.drawString(computeTempRight(), 290, 10 );
			
		}
		
	}
	
	
	// View: displays current ball positions
	public class Game extends JPanel {
		
		@Override
		public void paintComponent(Graphics g) {
			super.paintComponent(g);

			if(openDoor == false) {
				g.drawLine(285, 0, 285, 570);
			}
			
			for(int i = 0; i < ballCount; i++) {
				balls[i].draw(g, balls[i]);
			}
		}
		
	}
	

	// Controller: responds to events
	public class Animator implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			
			// While the button is being pressed
			if(openDoor == true) {
				
				for(int i = 0; i < ballCount; i++) {
					balls[i].moveFree(0.1);
				}
				
			}
			else {
				for(int i = 0; i < ballCount; i++) {
					balls[i].move(0.1);
				}
			}
			
			gamePanel.repaint(); // draws the line
			tempDisp.repaint();
			
		}
	}
	
}