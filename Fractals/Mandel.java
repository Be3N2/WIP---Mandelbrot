package Fractals;

import java.awt.Color;

import Input.Keyboard;

public class Mandel {
	
	private final int WIDTH, HEIGHT;
	public int pixels[];
	private int max = 4096; //was 1000
	private boolean drawn = false;
	public double zoom = 4.0; //larger is zoomed out
	private double initial = 0.5;
	private double xOffset, yOffset;
	
	public Mandel (int width, int height) {
		this.WIDTH = width;
		this.HEIGHT = height;
		
		xOffset = WIDTH/2;
		yOffset = HEIGHT/2;
		//System.out.println("offset;" + (1165 - (WIDTH / 2)));
		//System.out.println("offset;" + (542 - (HEIGHT / 2)));
		
		this.pixels = new int[WIDTH * HEIGHT];
		
	}
	
	public void setData(int mouseX, int mouseY, int zoomIn) {
		/*if (zoomIn < 0) { //zoom in
			double posX = centerX / zoom;
			double posY = centerY / zoom;
			double centerrX = (WIDTH/2)/zoom + xOffset/zoom;
			double centerrY = (HEIGHT/2)/zoom + yOffset/zoom;
			double distX = centerrX - posX;
			double distY = centerrY - posY;
			xOffset += distX;
			yOffset += distY;
//		 	this.xOffset += zoom * (xOffset - centerX); //really not sure about this one
//			this.yOffset += zoom * (yOffset - centerY);
//			this.zoom /= 2;
		} else if (zoomIn > 0) { //zoom out
			double posX = centerX / zoom;
			double posY = centerY / zoom;
			double centerrX = (WIDTH/2)/zoom + xOffset/zoom;
			double centerrY = (HEIGHT/2)/zoom + yOffset/zoom;
			double distX = centerrX - posX;
			double distY = centerrY - posY;
			xOffset += distX;
			yOffset += distY;
//		 	this.zoom *= 2.0;
//		 	xOffset = WIDTH / 2;
//		 	yOffset = HEIGHT / 2;
		}*/
		if (zoomIn < 0) {//zoom in
			
			double posX = mouseX * zoom / WIDTH;
			double posY = mouseY * zoom / WIDTH;
			double centerX = (WIDTH /2 - xOffset) * zoom / WIDTH;
			double centerY = (HEIGHT /2 - yOffset) * zoom / WIDTH;
			double distX = posX - centerX;
			double distY = posY - centerY;
			xOffset += distX * WIDTH / zoom;
			yOffset += distY * WIDTH / zoom;
			
//			this.zoom /=2;
		} else if (zoomIn > 0) {//zoom out
//			this.zoom *= 2;
			int distX = WIDTH/2 - mouseX;
			int distY = HEIGHT/2 - mouseY;
			xOffset += distX / zoom;
			yOffset += distY / zoom;
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
	                
	                /*
	                if (iterations < max) {
	                	//int color = 0xffffff / iterations;
	                	double value = ((double) iterations / (double) max);
	                	int color = (int) (0xffffff * ((double) max / (double) iterations));
	                	System.out.println(value);
	                	pixels[col + row * WIDTH] = color;
	                } else pixels[col + row * WIDTH] = 0x000000;
	                */
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
            
            pixels[col + row * WIDTH] = evalIterations(iterations);
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
	
	
}
