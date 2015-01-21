public class Matrix {
	
	public int nRows; 
	public int nCols; 
	public double[][] data; 
	
	public Matrix(double[][] dat) { 
		this.data = dat; 
		this.nRows = dat.length; 
		this.nCols = dat[0].length; 
		
	} 
	
	public Matrix(int nrow, int ncol) { 
		this.nRows = nrow; 
		this.nCols = ncol; 
		data = new double[nrow][ncol]; 
		
	}
}

