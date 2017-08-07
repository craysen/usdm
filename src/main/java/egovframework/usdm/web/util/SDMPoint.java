/**
 * @author 김학철
 *
 */
package egovframework.usdm.web.util;

public class SDMPoint {
	
	private double x;
	private double y;
	
	public SDMPoint()
	{
		x = 0.0;
		y = 0.0;
	}
	
	public SDMPoint(double latitude, double longitude)
	{
		this.x = longitude;
		this.y = latitude;
	}

	public double getX() {
		return x;
	}

	public void setX(double x) {
		this.x = x;
	}

	public double getY() {
		return y;
	}

	public void setY(double y) {
		this.y = y;
	}
	
}
