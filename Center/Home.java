package Center;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;

import javax.swing.AbstractButton;
import javax.swing.JButton;
import javax.swing.JFrame;

import Fractals.Julia;
import Fractals.Mandel;
import Input.Keyboard;
import Input.Mouse;

public class Home {

		public static class Game extends Canvas implements Runnable {
			private static final long serialVersionUID = 1L;
		
			public static final int width = 1920;
			public static final int height = 1080;
			public static final int scale = 1;
			
			private Thread thread;
			private JFrame frame;
			private Keyboard key;
			private Mouse mouse;
			private Mandel mandel;
			private Julia julia;
			private boolean running = false;
			private int rowCounter = 0;
			private boolean zoomed = false;
			private int timeOut = 0;
			private int switchTime = 0;
			private boolean overlay = true;
			private int switchTimer2 = 0;
			private boolean mandelMode = false;
			
			public static String title = "Mandelbrot";
			
			private BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
			private int[] pixels = ((DataBufferInt)image.getRaster().getDataBuffer()).getData();
			
			public Game() {
				Dimension size = new Dimension(width * scale, height * scale);
				setPreferredSize(size);
				
				//screen = new Screen(width, height);
				frame = new JFrame();
				key = new Keyboard();
				mandel = new Mandel(width, height);
				julia = new Julia(width, height);
				addKeyListener(key);
				
				mouse = new Mouse();
				addMouseListener(mouse);
				addMouseMotionListener(mouse);
			}
			
			public synchronized void start() {
				running = true;
				thread = new Thread(this, "Thing Displayed");
				thread.start();
			}

			public synchronized void stop(){
				running = false;
				try {
					thread.join(); 
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}	
			
			public void run() {
				long lastTime = System.nanoTime();
				long timer = System.currentTimeMillis();
				final double ns = 1000000000.0 / 60.0;
				double delta = 0;
				int frames = 0;
				int updates = 0;
				requestFocus();
				while(running){	
					long now = System.nanoTime();
					delta += (now - lastTime) / ns;
					lastTime = now;
					while (delta >= 1){
						update();
						updates++;
						delta--;
						}
					render();
					frames++;
					
					if(System.currentTimeMillis() - timer > 1000){
						timer += 1000;
						//System.out.println(updates + "ups," + frames + "fps" );
						frame.setTitle(title + "  |  " + updates + "ups," + frames + "fps");
						updates = 0;
						frames = 0;
						}
				}	
				stop();
			}
			
			public void update() {	
				key.update();
				
				switchTime++;
				if (key.m && switchTime > 60) {
					mandelMode = !mandelMode;
					switchTime = 0;
					mandel = new Mandel(width, height);
					julia = new Julia(width, height);
					rowCounter = 0;
				}
				if (key.u) {
					julia.cX -= 0.00001;
					rowCounter = 0;
					julia.setData(mouse.getX(), mouse.getY(), 1);
				}
				if (key.i) {
					julia.cX += 0.00001;
					rowCounter = 0;
					julia.setData(mouse.getX(), mouse.getY(), 1);
				}
				
				switchTimer2++;
				if (switchTimer2 > 60 && key.w) {
					switchTimer2 = 0;
					overlay = !overlay;
				}
				
				//change zoom
				if (mouse.getB() > 0) {
					if (!zoomed) {
						if (mouse.getB() == 1) {
							zoomed = true;
							if (mandelMode)
								mandel.setData(mouse.getX(), mouse.getY(), -1);
							else 
								julia.setData(mouse.getX(), mouse.getY(), -1);
							rowCounter = 0;
						} else if (mouse.getB() == 3) {
							zoomed = true;
							if (mandelMode)
								mandel.setData(mouse.getX(), mouse.getY(), 1);
							else 
								julia.setData(mouse.getX(), mouse.getY(), 1);
							rowCounter = 0;
						}
					}
				}
				
				//time out zoom input
				if (zoomed) {
					timeOut++;
					if (timeOut >= 60) {
						timeOut = 0;
						zoomed = false;
					}
				}
				
				if (mandelMode) {
					
					if (rowCounter != height) {  
						mandel.drawRow(rowCounter);
						rowCounter++;
					}				
				} else {
					//julia set
					if (rowCounter != height) {  
						julia.drawRow(rowCounter);
						rowCounter++;
					}	
				}
				
				if (key.esc) {
					System.exit(0);
				}
			}
			
			public void render() {
				BufferStrategy bs = getBufferStrategy();
				if(bs == null) {
					createBufferStrategy(3);
					return;
				}
				
				
				for (int i = 0; i < pixels.length; i++){
					if (mandelMode)
						pixels[i] = mandel.pixels[i];
					else 
						pixels[i] = julia.pixels[i];
				}
				
				Graphics g = bs.getDrawGraphics();
				g.drawImage(image, 0 , 0 , getWidth(), getHeight(), null);
				Graphics2D g2d = (Graphics2D) g;
				g.setColor(Color.WHITE);
				Font h = new Font("Helvetica", Font.PLAIN, 50);
				g.setFont(h);
				
				if (overlay) {
					if (mandelMode) {
						g.drawString("Zoom:", 50, 100);
						g.drawString(String.valueOf(mandel.zoom), 200, 100);
					} else {
						g.drawString("Zoom:", 50, 100);
						g.drawString(String.valueOf(julia.zoom), 200, 100);
						g.drawString(String.valueOf(julia.cX), 600, 1070);
						g.drawString(String.valueOf(julia.cY), 900, 1070);
					}
				}
				
//				g.fillOval(width/2 - 12,height/2 - 12, 24,24);
				g.dispose();
				bs.show();
			}
			
		public static void main(String[] args) {
		
			Game game = new Game();
			game.frame.setResizable(true);
			game.frame.setUndecorated(true);
			game.frame.setTitle(Game.title);
			game.frame.add(game);
			game.frame.pack();
			game.frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			game.frame.setLocationRelativeTo(null);
			game.frame.setVisible(true);
			
			game.start();
		
		}
		
	}
}
