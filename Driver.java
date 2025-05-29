import java.io.*;

// To the instructor:
// Extra work was done on the Polynomial class to support:
// 1. Beautifying the polynomial - i.e. ordering the terms in decreasing degrees
// 2. Construct polynomial from a String - this is a helper function for constructing from File
// Note that we do not beautify the constructed polynomial, but the user may manually beautify it.
// However, we beautify sum / product automatically to remove redundant terms.

public class Driver {
	public static void main(String [] args) throws IOException {
		Polynomial p1 = new Polynomial("-x-10x10+3x2-x+1-3");

		p1.saveToFile("nice-polynomial.txt");
		System.out.println("Saved p1 to file!");
		
		Polynomial p2 = new Polynomial(new File("nice-polynomial.txt"));
		System.out.println("Read p2 from file: " + p2);
		p2.beautify();
		System.out.println("Beautified p2: " + p2);
		
		Polynomial p3 = new Polynomial(new double [] {2, -1, -1, 2}, new int [] {3, 5, 1, 0});
		System.out.println("Constructed p3: " + p3);
		p3.beautify();
		System.out.println("Beautified p3: " + p3);
		
		Polynomial sum = p2.add(p3);
		System.out.println("Sum of p2 and p3: " + sum);
		
		Polynomial prod = p2.multiply(p3);
		System.out.println("Product of p2 and p3: " + prod);
		// I did verify this against WolframAlpha, and it is indeed correct!
		
		System.out.println("Evaluating product at x = 3: " + prod.evaluate(3));
		System.out.println("Checking if x = 1 is a root: " + prod.hasRoot(1));
	}
}