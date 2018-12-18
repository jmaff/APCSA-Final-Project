package main;

import java.text.DecimalFormat;

/**
 * Triangle object responsible for storing angles and sides and solving 
 * 
 * @author 21maffetone
 *
 */
public class Triangle {
	// values are -1 when unknown
	
	// Angles
	private double A = -1;
	private double B = -1;
	private double C = -1;
	
	// sides
	private double a = -1;
	private double b = -1;
	private double c = -1;
	
	private AngleMode angleMode;
	
	// radians not used due to inability to precisely enter pi in a terminal setting
	public enum AngleMode {
		DEGREES,
		RADIANS
	}
	
	public Triangle(AngleMode angleMode) {
		this.angleMode = angleMode;
	}
	
	// "default" constructor defaults to degrees
	public Triangle() {
		this(AngleMode.DEGREES);
	}

	public void solve() throws UnsolvableTriangleException {
		
		// determine third angle measure if two are known
		if (A != -1 && B != -1) {
			C = Math.PI - (A + B);
		} else if (B != -1 && C != -1) {
			A = Math.PI - (C + B);
		} else if (A != -1 && C != -1) {
			B = Math.PI - (A + C);
		}
		
		// A and a are known
		if (A != -1 && a != -1) {
			if (B != -1) {
				C = Math.PI - (A + B);
			} else if (C != -1) {
				B = Math.PI - (A + C);
			// only one angle known (angle A)
			} else {
				// law of sines to find angle B
				if (b != -1) {
					B = Math.asin(Math.sin(A) * b / a);
					C = Math.PI - (A + B);
				} else if (c != -1) {
					// law of sines to find angle C
					C = Math.asin(Math.sin(A) * c / a);
					B = Math.PI - (A + C);
				}
			}
			// law of sines to solve for two other sides
			b = (Math.sin(B) * a) / Math.sin(A);
			c = (Math.sin(C) * a) / Math.sin(A);
		// B and b are known
		} else if (B != -1 && b != -1) {
			if (A != -1) {
				C = Math.PI - (A + B);
			} else if (C != -1) {
				A = Math.PI - (B + C);
			// only one angle known (B)
			} else {
				if (a != -1) {
					// law of sines to find angle A
					A = Math.asin(Math.sin(B) * a / b);
					C = Math.PI - (A + B);
				} else if (c != -1) {
					// law of sines to find angle C
					C = Math.asin(Math.sin(B) * c / b);
					A = Math.PI - (B + C);
				}
			}
			// law of sines to find two other sides
			a = (Math.sin(A) * b) / Math.sin(B);
			c = (Math.sin(C) * b) / Math.sin(B);
		// C and c are known
		} else if (C != -1 && c != -1) {
			if (B != -1) {
				A = Math.PI - (B + C);
			} else if (A != -1) {
				B = Math.PI - (A + C);
			// only one angle known (C)
			} else {
				if (b != -1) {
					// law of sides to find angle B
					B = Math.asin(Math.sin(C) * b / c);
					A = Math.PI - (C + B);
				} else if (a != -1) {
					// law of sines to find angle A
					A = Math.asin(Math.sin(C) * a / c);
					B = Math.PI - (A + C);
				}
			}
			// law of sines to find two other sides
			b = (Math.sin(B) * c) / Math.sin(C);
			a = (Math.sin(A) * c) / Math.sin(C);
			// SAS Triangle (can't be solved with law of sines)
		} else {
			if (a == -1 && A != -1) {
				// law of cosines to find side a
				a = Math.sqrt((b * b) + (c * c) - 2 * b * c * Math.cos(A));
				
				// law of sines to find angle B
				B = Math.asin(Math.sin(A) * b / a);
				C = Math.PI - (A + B);
			} else if (b == -1 && B != -1) {
				// law of cosines to find side b
				b = Math.sqrt((a * a) + (c * c) - 2 * a * c * Math.cos(B));
				
				// law of sines to find angle A
				A = Math.asin(Math.sin(B) * a / b);
				C = Math.PI - (A + B);
			} else if (c == -1 && C != -1) {
				// law of cosines to find side c
				c = Math.sqrt((b * b) + (a * a) - 2 * a * b * Math.cos(C));
				
				// law of sines to find angle B
				B = Math.asin(Math.sin(C) * b / c);
				A = Math.PI - (C + B);
			} else {
				// SSS - law of cosines to find angles
				A = Math.acos( ( ( (a * a) - (b*b) - (c*c) ) ) / (-2 * b * c));
				B = Math.acos( ( ( (b * b) - (a*a) - (c*c) ) ) / (-2 * a * c));
				C = Math.PI - (A + B);
			}
		}
		checkTriangle();
	}
	
	/**
	 * Ensures that the resulting measurements follow the rules of a triangle
	 * @throws IllegalTriangleException
	 */
	private void checkTriangle() {
		// all measurements should be numbers
		if (Double.isNaN(A) || Double.isNaN(B) || Double.isNaN(C) || Double.isNaN(a) || Double.isNaN(b) || Double.isNaN(c)) {
			throw new IllegalTriangleException();
		}
		// all measurements should be greater than 0
		if (a <= 0 || b <= 0 || c <= 0 || A <= 0 || B <= 0 || C <= 0) {
			throw new IllegalTriangleException();
		}
		// the sum of any two sides of the triangle must be greater than the other side
		if (a + b <= c || b + c <= a || a + c <= b) {
			throw new IllegalTriangleException();
		}
		// sum of angles must be 180 degrees (.1 tolerance for rounding errors)
		double angleSum = getAngleA() + getAngleB() + getAngleC();
		if (angleSum <= 179.9 || angleSum >= 180.1) {
			throw new IllegalTriangleException();
		}
	}

	public double getAngleA() {
		if (angleMode == AngleMode.DEGREES) {
			return Math.toDegrees(A);
		}
		return A;
	}

	public double getAngleB() {
		if (angleMode == AngleMode.DEGREES) {
			return Math.toDegrees(B);
		}
		return B;
	}

	public double getAngleC() {
		if (angleMode == AngleMode.DEGREES) {
			return Math.toDegrees(C);
		}
		return C;
	}

	public double getSideA() {
		return a;
	}

	public double getSideB() {
		return b;
	}

	public double getSideC() {
		return c;
	}

	public void setAngleA(double a) {
		if (angleMode == AngleMode.DEGREES && a != -1) {
			A = Math.toRadians(a);
		} else {
			A = a;
		}
	}

	public void setAngleB(double b) {
		if (angleMode == AngleMode.DEGREES && b != -1) {
			B = Math.toRadians(b);
		} else {
			B = b;
		}
	}

	public void setAngleC(double c) {
		if (angleMode == AngleMode.DEGREES && c != -1) {
			C = Math.toRadians(c);
		} else {
			C = c;
		}
	}

	public void setSideA(double a) {
		this.a = a;
	}

	public void setSideB(double b) {
		this.b = b;
	}

	public void setSideC(double c) {
		this.c = c;
	}
	
	public void print() {
		System.out.println("******");
		System.out.println("ANGLES");
		System.out.println("******");
		
		System.out.print("A = ");
		System.out.println(new DecimalFormat("#.###").format(getAngleA()));
		
		System.out.print("B = ");
		System.out.println(new DecimalFormat("#.###").format(getAngleB()));
		
		System.out.print("C = ");
		System.out.println(new DecimalFormat("#.###").format(getAngleC()));
		
		System.out.println();
		
		System.out.println("*****");
		System.out.println("SIDES");
		System.out.println("*****");
		
		System.out.print("a = ");
		System.out.println(new DecimalFormat("#.###").format(getSideA()));
		
		System.out.print("b = ");
		System.out.println(new DecimalFormat("#.###").format(getSideB()));
		
		System.out.print("c = ");
		System.out.println(new DecimalFormat("#.###").format(getSideC()));
	}
}
