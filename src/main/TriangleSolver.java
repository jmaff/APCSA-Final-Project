package main;

import java.util.Scanner;

import javax.swing.JFrame;

/**
 * Main class for the program responsible for user input, setting values to the triangle,
 * and displaying the drawn triangle on a window.
 * 
 * @author 21maffetone
 *
 */
public class TriangleSolver {
	public static int WINDOW_WIDTH = 600;
	public static int WINDOW_HEIGHT = 600;
	
	public static void main(String[] args) {
		boolean active = true;
		
		Scanner scanner = new Scanner(System.in);
		
		// program runs "infinitely" until a break condition is met and active is set to false
		// like the user saying "no" to creating another triangle
		while (active) {
			Triangle triangle;
			boolean ambiguous = false;
			
			System.out.println("***************************");
			System.out.println("Welcome To Triangle Solver!");
			System.out.println("***************************");
			
			sleep(1000);
			
			System.out.println("Please select a mode - solve by type (1) or manual (2): ");
			String mode = "";
			
			// input sanitization
			while (!mode.equals("1") && !mode.equals("2")) {
				mode = scanner.next();
			}
	
			// create a "blank" triangle to set values to from user input
			triangle = new Triangle();
			
			/*
			 * 'Setup mode' is where the user provides the type of known information they have
			 * about the triangle, such as SSA, SSS, etc. This results in reduced entry time
			 * and has the functionality of detecting an ambiguous case for SSA
			 */
			if (mode.equals("1")) {
				System.out.println("Enter type of setup (AAS, ASA, etc.)");
				
				boolean validInput = false;
				String type = "";
				
				// loop until a valid setup is input
				while (!validInput) {
					
					type = scanner.next();
					type = type.toUpperCase();
					
					switch (type) {
					
					case "AAA":
						validInput = true;
						
						/* AAA setups cannot be solved for actual side lengths, only the ratios 
						 * of the sides. As such, I simply set the longest side to length 1
						 * to make the resulting ratios easier to read and possible to solve
						 * for
						 */
						System.out.println("Defaulting longest side to length 1...");
						sleep(1000);
						
						System.out.println("Enter first angle");
						triangle.setAngleA(scanner.nextDouble());
						
						System.out.println("Enter second angle");
						triangle.setAngleB(scanner.nextDouble());
						
						System.out.println("Enter third angle");
						triangle.setAngleC(scanner.nextDouble());
						
						// determining the longest side (opposite the largest angle and setting
						// its length to 1
						if (triangle.getAngleA() > triangle.getAngleB() && 
								triangle.getAngleA() > triangle.getAngleC()) {
							triangle.setSideA(1);
						} else if (triangle.getAngleB() > triangle.getAngleC()) {
							triangle.setSideB(1);
						} else {
							triangle.setSideC(1);
						}
						
						break;
					
					case "AAS":
						validInput = true;
						System.out.println("Enter an angle");
						triangle.setAngleA(scanner.nextDouble());
						
						System.out.println("Enter other angle");
						triangle.setAngleB(scanner.nextDouble());
						
						System.out.println("Enter side");
						triangle.setSideA(scanner.nextDouble());
						
						break;
						
					case "ASA":
						validInput = true;
						System.out.println("Enter an angle");
						triangle.setAngleA(scanner.nextDouble());
						
						System.out.println("Enter included side");
						triangle.setSideC(scanner.nextDouble());
						
						System.out.println("Enter other angle");
						triangle.setAngleB(scanner.nextDouble());
						
						break;
						
					case "SAS":
						validInput = true;
						System.out.println("Enter a side");
						triangle.setSideA(scanner.nextDouble());
						
						System.out.println("Enter included angle");
						triangle.setAngleB(scanner.nextDouble());
						
						System.out.println("Enter other side");
						triangle.setSideC(scanner.nextDouble());
						
						break;
						
					case "SSA":
						validInput = true;
						// SSA setups could have two possible triangles, so I set this boolean
						// here to use later when solving
						ambiguous = true;
						
						System.out.println("Enter a side");
						triangle.setSideB(scanner.nextDouble());
						
						System.out.println("Enter other side");
						triangle.setSideC(scanner.nextDouble());
						
						System.out.println("Enter angle");
						triangle.setAngleC(scanner.nextDouble());
						
						break;
						
					case "SSS":
						validInput = true;
						System.out.println("Enter first side");
						triangle.setSideA(scanner.nextDouble());
						
						System.out.println("Enter second side");
						triangle.setSideB(scanner.nextDouble());
						
						System.out.println("Enter third side");
						triangle.setSideC(scanner.nextDouble());
						
						break;
		
					default:
						// input isn't valid, so validInput isn't set and the loop repeats
						break;
					}
				}
			/*
			 * 'Manual' mode is where the user goes through each side and angle of the triangle,
			 * inputting -1 if it is unknown. To add testing functionality, this mode does NOT 
			 * automatically jump to solving once enough info is known, as this allows the user
			 * to double check just one or two sides/angles 	
			 */
			} else {
				// Manual mode
				System.out.println("Enter angles (-1 for unknown)");
				
				System.out.println("Enter A: ");
				triangle.setAngleA(scanner.nextDouble());
				
				System.out.println("Enter a: ");
				triangle.setSideA(scanner.nextDouble());
				
				System.out.println("Enter B: ");
				triangle.setAngleB(scanner.nextDouble());
				
				System.out.println("Enter b: ");
				triangle.setSideB(scanner.nextDouble());
				
				System.out.println("Enter C: ");
				triangle.setAngleC(scanner.nextDouble());
				
				System.out.println("Enter c: ");
				triangle.setSideC(scanner.nextDouble());
			}
			
			System.out.println("Solving......");
			sleep(600);
			
			boolean solved;
			
			try {
				// solve() will throw an exception if the triangle can't be solved with the
				// measurements provided
				triangle.solve();
				solved = true;
			} catch (UnsolvableTriangleException e) {
				System.out.println("You did not provide enough information to solve a triangle.");
				solved = false;
			} catch (IllegalTriangleException e) {
				System.out.println("Your triangle was not a triangle.");
				solved = false;
			}
			
			// if the triangle was successfully solved, go on to determine if it's case was
			// ambiguous (SSA only), and then display it if desired
			if (solved) {
				Triangle ambiguousTriangle = new Triangle();
				
				if (ambiguous) {
					// other result of arcsin when using law of sines on the SSA case
					ambiguousTriangle.setAngleB(180 - triangle.getAngleB());
					
					ambiguousTriangle.setSideB(triangle.getSideB());
					ambiguousTriangle.setSideC(triangle.getSideC());
					ambiguousTriangle.setAngleC(triangle.getAngleC());
					
					try {
						ambiguousTriangle.solve();
						System.out.println("This case is ambiguous. Both possible triangles will be shown.");
						sleep(600);
						// couldn't be solved
					} catch (RuntimeException e) {
						// try to use the other value of arcsin for the other angle
						ambiguousTriangle = new Triangle();
						ambiguousTriangle.setAngleA(180 - triangle.getAngleA());
						
						ambiguousTriangle.setSideB(triangle.getSideB());
						ambiguousTriangle.setSideC(triangle.getSideC());
						ambiguousTriangle.setAngleC(triangle.getAngleC());
						
						try {
							ambiguousTriangle.solve();
							System.out.println("This case is ambiguous. Both possible triangles will be shown.");
							sleep(600);
						} catch (RuntimeException e2) {
							// if both fail, there is no abmiguous case
							ambiguous = false;
						}
					}
				}
				
				// printing results
				System.out.println();
				System.out.println("RESULTS:");
				System.out.println();
				
				triangle.print();
				if (ambiguous) {
					System.out.println();
					System.out.println("OTHER POSSIBLE TRIANGLE:");
					System.out.println();
					ambiguousTriangle.print();
				}
				
				sleep(600);
				System.out.println("Would you like to view your triangle? (y/n): ");
				
				String view = "";
				
				while (!view.equals("y") && !view.equals("n")) {
					view = scanner.next();
				}
				
				// creating windows to display the triangle(s)
				if (view.equals("y")) {
					TriangleViewer triangleViewer = new TriangleViewer(triangle);
			
					JFrame frame = new JFrame("Triangle Solver");
					
					frame.setSize(WINDOW_WIDTH, WINDOW_HEIGHT);
					frame.setLocationRelativeTo(null);
					frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);	
						
					frame.setContentPane(triangleViewer);
						
					frame.setVisible(true);
					
					if (ambiguous) {
						TriangleViewer otherTriangleViewer = new TriangleViewer(ambiguousTriangle);
			
						JFrame otherFrame = new JFrame("Triangle Solver (Ambiguous)");
						
						otherFrame.setSize(WINDOW_WIDTH, WINDOW_HEIGHT);
						otherFrame.setLocationRelativeTo(null);
						otherFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);	
							
						otherFrame.setContentPane(otherTriangleViewer);
							
						otherFrame.setVisible(true);
					}
				}
			}
			
			sleep(600);
			System.out.println("Would you like to solve another triangle? (y/n): ");
			
			String again = "";
			
			while (!again.equals("y") && !again.equals("n")) {
				// false if the user wants to quit the program - loop will stop
				again = scanner.next();
			}
			
			active = again.equals("y");
		}
		scanner.close();
	}
	
	// sleep on the main thread of the program to allow for delay of output to help readability
	private static void sleep(long ms) {
		try {
			Thread.sleep(ms);
		} catch (InterruptedException e) {
			Thread.currentThread().interrupt();
		}
	}
}
