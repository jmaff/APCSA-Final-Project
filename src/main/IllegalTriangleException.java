package main;

@SuppressWarnings("serial")
public class IllegalTriangleException extends RuntimeException {
	@Override
	public String getMessage() {
		return "The sum of any two side lengths of a triangle must be greater than the remaining side.";
	}

}
