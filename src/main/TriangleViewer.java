package main;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Polygon;
import java.awt.Rectangle;
import java.text.DecimalFormat;

import javax.swing.JPanel;

/**
 * Panel responsible for drawing a triangle object with labels.
 * 
 * @author 21maffetone
 *
 */
@SuppressWarnings("serial")
public class TriangleViewer extends JPanel {
	Triangle triangle;
	
	public boolean drawText;
	private final static double SCALE = 400;
	private final static int VERTICAL_OFFSET = 30;
	
	public TriangleViewer(Triangle triangle) {
		super();
		this.triangle = triangle;
		setFont(new Font("Arial", Font.PLAIN, 15));
	}
	
	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		
		Graphics2D g2 = (Graphics2D) g;
		// color of lines
		g2.setColor(Color.BLACK);
		
		int[] xPoints = new int[3];
		int[] yPoints = new int [3];
		
		double[] sideLengths = {triangle.getSideA(), triangle.getSideB(), triangle.getSideC()};
		
		double max = 0;
		
		for (int i = 0; i < sideLengths.length; i++) {
			if (sideLengths[i] > max) {
				max = sideLengths[i];
			}
		}
		
		// make the longest side a fixed pixel length and scale the rest accordingly
		double a = (triangle.getSideA() / max) * SCALE;
		double b = (triangle.getSideB() / max) * SCALE;
		double c = (triangle.getSideC() / max) * SCALE;
		
		xPoints[0] = (TriangleSolver.WINDOW_WIDTH / 2);
		xPoints[1] = (TriangleSolver.WINDOW_WIDTH / 2) + (int) c;
		
		// https://math.stackexchange.com/questions/1583375/how-to-plot-a-triangle-given-three-side-lengths
		double cX = ((a * a) - (b * b) - (c * c)) / (-2 * c);
		
		xPoints[2] = (TriangleSolver.WINDOW_WIDTH / 2) + (int) cX;
		
		// offset to be centered at center of horizontal side
		for (int i = 0; i < xPoints.length; i++) {
			xPoints[i] = xPoints[i] - (int) (c / 2);
		}
		
		yPoints[0] = (TriangleSolver.WINDOW_WIDTH / 2);
		yPoints[1] = (TriangleSolver.WINDOW_WIDTH / 2);
		
		// https://math.stackexchange.com/questions/1583375/how-to-plot-a-triangle-given-three-side-lengths
		double cY = Math.sqrt(b * b - cX * cX);
		
		yPoints[2] = (TriangleSolver.WINDOW_WIDTH / 2) - (int) cY;
		
		for (int i = 0; i < yPoints.length; i++) {
			yPoints[i] = yPoints[i] + (int) (cY / 2) - VERTICAL_OFFSET;
		}
		
		Polygon drawableTriangle = new Polygon(xPoints, yPoints, 3);
		g2.drawPolygon(drawableTriangle);
		
		// format all measurements to 3 decimal places to draw on window
		String AText = "A = " + new DecimalFormat("#.###").format(triangle.getAngleA());
		String BText = "B = " + new DecimalFormat("#.###").format(triangle.getAngleB());
		String CText = "C = " + new DecimalFormat("#.###").format(triangle.getAngleC());
		
		String aText = "a = " + new DecimalFormat("#.###").format(triangle.getSideA());
		String bText = "b = " + new DecimalFormat("#.###").format(triangle.getSideB());
		String cText = "c = " + new DecimalFormat("#.###").format(triangle.getSideC());
		
		// text is offset by varying pixel amounts to prevent it from overlapping with sides
		if (xPoints[0] - 100 >= 30) {
			g2.drawString(AText, xPoints[0] - 100, yPoints[0] + 15);
		} else {
			g2.drawString(AText, 30, yPoints[0]);
		}
		
		g2.drawString(BText, xPoints[1] + 20, yPoints[1]);
		
		if (xPoints[2] <= TriangleSolver.WINDOW_WIDTH - 70) {
			g2.drawString(CText, xPoints[2], yPoints[2] - 40);
		} else {
			g2.drawString(CText, TriangleSolver.WINDOW_WIDTH - 70, yPoints[2] - 15);
		}
		
		// use bounds of triangle to position side labels
		Rectangle boundingBox = drawableTriangle.getBounds();
		
		// side C
		g2.drawString(cText, TriangleSolver.WINDOW_WIDTH / 2, TriangleSolver.WINDOW_WIDTH / 2 + (int) (cY / 2) - VERTICAL_OFFSET + 40);
		// side B
		g2.drawString(bText, boundingBox.x, boundingBox.y + boundingBox.height / 2);
		// side A
		if (boundingBox.x + boundingBox.width <= TriangleSolver.WINDOW_WIDTH - 70) {
			g2.drawString(aText, boundingBox.x + boundingBox.width, boundingBox.y + boundingBox.height / 2);
		} else {
			g2.drawString(aText, TriangleSolver.WINDOW_WIDTH - 70, boundingBox.y + boundingBox.height / 2);
		}
	}

}
