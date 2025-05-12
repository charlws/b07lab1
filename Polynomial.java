public class Polynomial {
	protected double [] coefficients;
	
	public Polynomial() {
		this.coefficients = new double[] {0};
	}
	
	public Polynomial(double [] coefficients) {
		this.coefficients = coefficients;
	}
	
	public Polynomial add(Polynomial that) {
		int thisLen = this.coefficients.length;
		int thatLen = that.coefficients.length;
		
		double [] newCoefficients = new double[thisLen > thatLen ? thisLen : thatLen];
		for(int i=0;i<thisLen;i++) {
			newCoefficients[i] = this.coefficients[i];
		}
		for(int i=0;i<thatLen;i++) {
			newCoefficients[i] += that.coefficients[i];
		}
		
		return new Polynomial(newCoefficients);
	}
	
	public double evaluate(double x) {
		double res = 0;
		for(int i=0;i<this.coefficients.length;i++) {
			res += this.coefficients[i] * Math.pow(x, i);
		}
		return res;
	}
	
	public boolean hasRoot(double x) {
		return this.evaluate(x) == 0;
	}
}
