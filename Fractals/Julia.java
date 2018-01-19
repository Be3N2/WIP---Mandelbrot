package Fractals;

import java.awt.Color;

public class Julia {

	private final int WIDTH, HEIGHT;
	public int pixels[];
	private int max = 4096; //was 1000 
	private boolean drawn = false;
	private double zoom = 2.0; //larger is zoomed out
	private int xOffset, yOffset;
	public double cX = -0.9999999; //first attempt -0.7  -0.9999999
    public double cY = 0.27015; //first attempt 0.27015 0.145 
//	private double cX = -0.9878787;//-0.88888;
//	private double cY = 0.232;//0.269;
	
	public Julia (int width, int height) {
		this.WIDTH = width;
		this.HEIGHT = height;
		
		xOffset = WIDTH / 2;
		yOffset = HEIGHT / 2;
		
		this.pixels = new int[WIDTH * HEIGHT];
		
	}
	
	public void setData(int centerX, int centerY, int zoomIn) {
		if (zoomIn < 0) {
		 	this.zoom /= 2;
		 	this.xOffset += zoom * (xOffset - centerX); //really not sure about this one
			this.yOffset += zoom * (yOffset - centerY);
		} else if (zoomIn > 0) {
		 	this.zoom *= 2.0;
		 	xOffset = WIDTH / 2;
		 	yOffset = HEIGHT / 2;
		}
	}
	
	public void drawRow(int row) {
		
        double x, y;
 
        for (int col = 0; col < WIDTH; col++) {
        	
            x = 1.5 * (col - xOffset) / (zoom * WIDTH);
            y = (row - yOffset) / (zoom * HEIGHT);
            int iterations = 0;
            while (x * x + y * y < 4 && iterations < max) {
                    double tmp = x * x - y * y + cX;
                    y = 2.0 * x * y + cY;
                    x = tmp;
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
				hexCol = new Color(count * 2, 0, 0);    /* 0x0000 to 0x007E */
			} else if (count < 128) {
				hexCol = new Color((((count - 64) * 128) / 126) + 128, 0, 0);    /* 0x000080 to 0x0000C0 */
			} else if (count < 256) {
				hexCol = new Color((((count - 128) * 62) / 127) + 193, 0, 0);    /* 0x0000C1 to 0x0000FF */
			} else if (count < 512) {
				hexCol = new Color(5, (((count - 256) * 62) / 255) + 1, 0);    /* 0x0001FF to 0x003FFF */
			} else if (count < 1024) {
				hexCol = new Color(5, (((count - 512) * 63) / 511) + 64, 0);   /* 0x0040FF to 0x007FFF */
			} else if (count < 2048) {
				hexCol = new Color(5, (((count - 1024) * 63) / 1023) + 128, 0);   /* 0x0080FF to 0x00BFFF */
			} else if (count < 4096) {
				hexCol = new Color(5, (((count - 2048) * 63) / 2047) + 192, 0);   /* 0x00C0FF to 0x00FFFF */
			} 
			return hexCol.getRGB();
		} 
		return returnCol;
	}
}
