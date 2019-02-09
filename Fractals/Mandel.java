package Fractals;

import java.awt.Color;

import Input.Keyboard;

public class Mandel {
	
	private final int WIDTH, HEIGHT;
	public int pixels[];
	private int max = 4096; //was 1000
	private boolean drawn = false;
	public double zoom = 4.0; //larger is zoomed out
	private double xOffset, yOffset;
	
	public Mandel (int width, int height) {
		this.WIDTH = width;
		this.HEIGHT = height;
		
		xOffset = WIDTH/2;
		yOffset = HEIGHT/2;
		
		this.pixels = new int[WIDTH * HEIGHT];
		
	}
	
	public void setData(int mouseX, int mouseY, int zoomIn) {
		
		if (zoomIn < 0) {//zoom in
			
			double posX = (mouseX - xOffset) * zoom / WIDTH;
			double posY = (mouseY - yOffset) * zoom / WIDTH;
			
			this.zoom /=2;
			
			double centerX = (WIDTH /2 - xOffset) * zoom / WIDTH;
			double centerY = (HEIGHT /2 - yOffset) * zoom / WIDTH;
			double distX = centerX - posX;
			double distY = centerY - posY;
			xOffset += distX * WIDTH / zoom;
			yOffset += distY * WIDTH / zoom;
			
			
		} else if (zoomIn > 0) {//zoom out	
			
			double posX = (mouseX - xOffset) * zoom / WIDTH;
			double posY = (mouseY - yOffset) * zoom / WIDTH;
			
			this.zoom *= 2;
			
			double centerX = (WIDTH /2 - xOffset) * zoom / WIDTH;
			double centerY = (HEIGHT /2 - yOffset) * zoom / WIDTH;
			double distX = centerX - posX;
			double distY = centerY - posY;
			xOffset += distX * WIDTH / zoom;
			yOffset += distY * WIDTH / zoom;
		}
		
	}
	
	public void drawSet() {
		if (!drawn) {
			for (int row = 0; row < HEIGHT; row++) {
	            for (int col = 0; col < WIDTH; col++) {
	                double c_re = (col - xOffset) * zoom / WIDTH;
	                double c_im = (row - yOffset) * zoom / WIDTH;
	                double x = 0, y = 0;
	                int iterations = 0;
	                while (x*x+y*y <= 4 && iterations < max) {
	                    double x_new = x * x - y * y + c_re;
	                    y = 2 * x * y + c_im;
	                    x = x_new;
	                    iterations++;
	                } 
	                
	                pixels[col + row * WIDTH] = evalIterations(iterations);
	            }
	            drawn = true;
	        }
		}
		
	}
	
	public void drawRow(int row) {
		for (int col = 0; col < WIDTH; col++) {
            double c_re = (col - xOffset) * zoom / WIDTH;
            double c_im = (row - yOffset) * zoom / WIDTH;
            double x = 0, y = 0;
            int iterations = 0;
            while (x*x+y*y <= 4 && iterations < max) {
                double x_new = x * x - y * y + c_re;
                y = 2 * x * y + c_im;
                x = x_new;
                iterations++;
            } 
            
//            pixels[col + row * WIDTH] = evalIterations(iterations);
//            pixels[col + row * WIDTH] = blue(iterations);
            pixels[col + row * WIDTH] = darkpurple(iterations);
        }
	}
	
	private int evalIterations(int count) {
		int returnCol = 0x0000000;//black
		Color hexCol = new Color(0,0,0);
		if (count < max) {
			if (count < 64) {
				hexCol = new Color(count * 4, 0, 0);    /* 0x0000 to 0x007E */
			} else if (count < 128) {
				hexCol = new Color((((count - 64) * 128) / 126) + 128, 0, 0);    /* 0x0080 to 0x00C0 */
			} else if (count < 256) {
				hexCol = new Color((((count - 128) * 62) / 127) + 193, 0, 0);    /* 0x00C1 to 0x00FF */
			} else if (count < 512) {
				hexCol = new Color(255, (((count - 256) * 62) / 255) + 1, 0);    /* 0x01FF to 0x3FFF */
			} else if (count < 1024) {
				hexCol = new Color(255, (((count - 512) * 63) / 511) + 64, 0);   /* 0x40FF to 0x7FFF */
			} else if (count < 2048) {
				hexCol = new Color(255, (((count - 1024) * 63) / 1023) + 128, 0);   /* 0x80FF to 0xBFFF */
			} else if (count < 4096) {
				hexCol = new Color(255, (((count - 2048) * 63) / 2047) + 192, 0);   /* 0xC0FF to 0xFFFF */
			} 
			return hexCol.getRGB();
		} 
		return returnCol;
	}
	private int blue(int count) {
		int returnCol = 0x0000000;//black
		Color hexCol = new Color(0,0,0);
		if (count < max) {
			if (count < 64) {
				hexCol = new Color(0, 0, count * 4);    /* 0x0000 to 0x007E */
			} else if (count < 128) {
				hexCol = new Color(0, 0, (((count - 64) * 128) / 126) + 128);    /* 0x0080 to 0x00C0 */
			} else if (count < 256) {
				hexCol = new Color(0, 0, (((count - 128) * 62) / 127) + 193);    /* 0x00C1 to 0x00FF */
			} else if (count < 512) {
				hexCol = new Color((((count - 256) * 62) / 255) + 1, 0, 255);    /* 0x01FF to 0x3FFF */
			} else if (count < 1024) {
				hexCol = new Color((((count - 512) * 63) / 511) + 64, 0, 255);   /* 0x40FF to 0x7FFF */
			} else if (count < 2048) {
				hexCol = new Color(((count - 1024) / 1023) * 255 / 2, 0, 255);   /* 0x80FF to 0xBFFF */
			} else if (count < 4096) {
				hexCol = new Color(((count - 2048) / 2047) * 255, 0, 255);   /* 0xC0FF to 0xFFFF */
			} 
			return hexCol.getRGB();
		} 
		return returnCol;
	}
	private int darkpurple(int count) {
		int returnCol = 0x0000000;//black
		Color hexCol = new Color(0,0,0);
		if (count < max) {
			if (count < 64) {
				hexCol = new Color(count * 2, 0, count * 2); 
			} else if (count < 128) {
				hexCol = new Color(25, 0, 25);    
			} else if (count < 256) {
				hexCol = new Color(50, 0, 50);   
			} else if (count < 512) {
				hexCol = new Color(75, 0, 75);    
			} else if (count < 1024) {
				hexCol = new Color(100, 0, 100);   
			} else if (count < 2048) {
				hexCol = new Color(125, 0, 125);   
			} else if (count < 4096) {
				hexCol = new Color(150, 0, 150);   
			} 
			return hexCol.getRGB();
		} 
		return returnCol;
	}

}
