import java.io.*;
import java.nio.file.Files;
import java.text.DecimalFormat;

public class Polynomial {
	protected double [] coefficients;
	protected int [] exponents;
	
	public Polynomial() {
		this.coefficients = new double[] {};
		this.exponents = new int[] {};
	}
	
	public Polynomial(double [] coefficients, int [] exponents) {
		this.coefficients = coefficients;
		this.exponents = exponents;
	}
	
	private static double[] parseCoefficients(String polystr) {
		String [] parts = polystr.split("(?=[+-])"); // include sign in splitting
		double [] coefficients = new double[parts.length];
		for(int i=0;i<parts.length;i++) {
			String [] term = parts[i].split("(?=x)"); // include 'x' in splitting
			String pre = term[0];
			if(pre.equals("+") || pre.startsWith("x")) {
				// it would be a +/- sign if it's a term in the middle with 1 coefficient
				// or just x if it's the first term
				coefficients[i] = 1;
			} else if(pre.equals("-")){
				// just minus - negative 1 coefficient
				coefficients[i] = -1;
			} else {
				// otherwise - parse normally
				coefficients[i] = Double.parseDouble(pre);
			}
		}
		return coefficients;
	}
	
	private static int[] parseExponents(String polystr) {
		String [] parts = polystr.split("(?=[+-])");
		int [] exponents = new int[parts.length];
		for(int i=0;i<parts.length;i++) {
			String [] term = parts[i].split("(?=x)");
			String suf = term[term.length - 1];
			if(!suf.startsWith("x")) {
				// does not have a 'x' part - constant number
				exponents[i] = 0;
			} else if(suf.equals("x")) {
				// just 'x' - degree 1
				exponents[i] = 1;
			} else {
				// otherwise - parse the degree
				exponents[i] = Integer.parseInt(suf.substring(1));
			}
		}
		return exponents;
	}
	
	public Polynomial(String polystr) {
		this(parseCoefficients(polystr), parseExponents(polystr));
	}
	
	public Polynomial(File file) throws IOException {
		this(Files.readString(file.toPath()).trim());
	}
	
	public void saveToFile(String fileName) throws IOException {
		FileWriter writer = new FileWriter(fileName);
		writer.write(this.toString());
	    writer.close();
	}
	
	@Override public String toString() {
		String ret = "";
		DecimalFormat format = new DecimalFormat("0.#");
		for(int i=0;i<this.coefficients.length;i++) {
			// we add sign manually
			if(this.coefficients[i] > 0 && i != 0) ret += "+";
			if(this.coefficients[i] < 0) ret += "-";
			
			// local variable to reduce code length
			double coef = Math.abs(this.coefficients[i]);
			int expo = this.exponents[i];

			if(coef != 1 || coef == 1 && expo == 0)
				ret += format.format(coef);
			if(expo >= 1) ret += "x";
			if(expo > 1) ret += expo;
		}
		return ret;
	}
	
	public void beautify() {
		int len = this.coefficients.length;
		for(int i=0;i<len;i++) {
			for(int j=0;j<len;j++) {
				if(this.exponents[i] > this.exponents[j]) {
					// order in decreasing degrees
					int tmpExponent = this.exponents[i];
					double tmpCoefficient = this.coefficients[i];
					this.exponents[i] = this.exponents[j];
					this.coefficients[i] = this.coefficients[j];
					this.exponents[j] = tmpExponent;
					this.coefficients[j] = tmpCoefficient;
				}
				if(this.exponents[i] == this.exponents[j] && i != j) {
					// merge same-degree terms
					this.coefficients[i] += this.coefficients[j];
					this.coefficients[j] = 0;
				}
			}
		}
		
		// remove zero terms
		int nonZeroLen = 0;
		for(int i=0;i<len;i++) {
			if(this.coefficients[i] != 0) {
				nonZeroLen++;
			}
		}
		
		double [] newCoefficients = new double[nonZeroLen];
		int [] newExponents = new int[nonZeroLen];
		int idx = 0;
		for(int i=0;i<len;i++) {
			if(this.coefficients[i] != 0) {
				newCoefficients[idx] = this.coefficients[i];
				newExponents[idx] = this.exponents[i];
				idx++;
			}
		}

		this.coefficients = newCoefficients;
		this.exponents = newExponents;
	}
	
	public Polynomial add(Polynomial that) {
		int thisLen = this.coefficients.length;
		int thatLen = that.coefficients.length;
		
		double [] newCoefficients = new double[thisLen + thatLen];
		int [] newExponents = new int[thisLen + thatLen];
		
		for(int i=0;i<thisLen;i++) {
			newCoefficients[i] = this.coefficients[i];
			newExponents[i] = this.exponents[i];
		}
		for(int i=0;i<thatLen;i++) {
			newCoefficients[thisLen + i] = that.coefficients[i];
			newExponents[thisLen + i] = that.exponents[i];
		}
		
		Polynomial newPolynomial = new Polynomial(newCoefficients, newExponents);
		newPolynomial.beautify(); // this merges terms of same degree
		
		return newPolynomial;
	}
	
	public Polynomial multiply(Polynomial that) {
		int thisLen = this.coefficients.length;
		int thatLen = that.coefficients.length;
		
		Polynomial newPolynomial = new Polynomial();
		
		for(int i=0;i<thatLen;i++) {
			// go through each term in that polynomial
			// multiply the coefficient of the term, and increase degree of all terms in this polynomial
			// then add this product to our result
			double [] tempCoefficients = new double[thisLen];
			int [] tempExponents = new int[thisLen];
			
			for(int j=0;j<thisLen;j++) {
				tempCoefficients[j] = this.coefficients[j] * that.coefficients[i];
				tempExponents[j] = this.exponents[j] + that.exponents[i];
			}
	
			newPolynomial = newPolynomial.add(new Polynomial(tempCoefficients, tempExponents));
		}
		
		newPolynomial.beautify();
		
		return newPolynomial;
	}
	
	public double evaluate(double x) {
		double res = 0;
		for(int i=0;i<this.coefficients.length;i++) {
			res += this.coefficients[i] * Math.pow(x, this.exponents[i]);
		}
		return res;
	}
	
	public boolean hasRoot(double x) {
		return this.evaluate(x) == 0;
	}
}
