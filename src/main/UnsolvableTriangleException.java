package main;

@SuppressWarnings("serial")
public class UnsolvableTriangleException extends RuntimeException {
	@Override
	public String getMessage() {
		return "Not enough information was provided to solve the triangle";
	}

}
