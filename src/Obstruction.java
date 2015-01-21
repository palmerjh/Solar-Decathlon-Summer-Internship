
public class Obstruction { // *****************TWO-DIMENSIONAL (i.e. all points lie in same plane)
	
	// Obstruction perimeter formed by line segments p0--p1, p1--p2, p2--p3,...,p(n-2)--p(n-1), p(n-1)--p0}
	public ThreeDimensionalPoint[] point; 	// {p0, p1,..., p(n-1)}
	
	public double[][] linearTransformation; // 3 x 3 matrix that takes vectors from the standard basis
		// and finds their components in the shifted coordinate system where the plane of the obstruction represents the
		// xy-plane; each "ThreeDimensionalPoint" represents a row of the matrix
	public Points[] planePoint;				// T(p0, p1,..., p(n-1)) --> (2-D p0,..., 2-D p(n-1))
	public LineSegment[] lineSegment; // array of line segments within plane; lines include slope, y-intercepts, & domains
	
	public ThreeDimensionalPoint normal; 		// vector normal to plane [a, b, c] --> a*x + b*y + c*z = d
	
	// shift equals value of d in standard equation for plane: a*x + b*y + c*z = d
	public double shift;
	
	public double shgc;
		
	public boolean canSetLength;				// for overhang
	
	
	private int n; 								// ignore; for calculation purposes
	
	public Obstruction() {						// default obstruction = box model overhang; p1 and p2
		
		this.point = new ThreeDimensionalPoint[4];
		
		point[0] = new ThreeDimensionalPoint(0, 10, 0);
		point[1] = new ThreeDimensionalPoint(31, 10, 0);
		point[2] = new ThreeDimensionalPoint(31, 10, 0);
		point[3] = new ThreeDimensionalPoint(0, 10, 0);
		
		this.linearTransformation = getLinearTransformation(this.point); // also finds and sets normal
		this.planePoint = getPlanePoints(this.point, this.linearTransformation);
		this.lineSegment = getLineSegments(this.planePoint);	
		
		shift = dot(normal, point[0]);
				
		shgc = 0.0;
		
		canSetLength = false;
				
	}

	// customized general-type four-sided Obstruction constructor
	public Obstruction(ThreeDimensionalPoint p1, ThreeDimensionalPoint p2, ThreeDimensionalPoint p3,
			ThreeDimensionalPoint p4, double shgc) {
		
		this.point = new ThreeDimensionalPoint[4];
		
		point[0] = p1;
		point[1] = p2;
		point[2] = p3;
		point[3] = p4;
		
		this.linearTransformation = getLinearTransformation(this.point); // also finds and sets normal
		this.planePoint = getPlanePoints(this.point, this.linearTransformation);
		this.lineSegment = getLineSegments(this.planePoint);
		
		shift = dot(normal, point[0]);
		
		this.canSetLength = false;
		
		this.shgc = shgc;
	}
	
	// customized general-type n-sided Obstruction constructor
	public Obstruction(ThreeDimensionalPoint[] point, double shgc) {
		
		this.point = point;
		this.linearTransformation = getLinearTransformation(this.point); // also finds and sets normal
		this.planePoint = getPlanePoints(this.point, this.linearTransformation);
		this.lineSegment = getLineSegments(this.planePoint);
		
		shift = dot(normal, point[0]);
		
		this.canSetLength = false;
		
		this.shgc = shgc;
	}
	
	/*public void setLength(double length) {		// for overhang purposes
		
		if (this.canSetLength) {
			
			double y = this.point[0].y + Math.sin(this.angle) * (this.length - length);
			double z = this.point[5].z - Math.cos(this.angle) * (this.length - length);
			
			this.length = length;
			
			if (p12IsSameHeightAsP34) {
				point[0].y = y;
				point[3].y = y;
			} 
			
			point[4].y = y;
			point[5].y = y;
			
			point[4].z = z;
			point[5].z = z;
		}	
	} */
	
	public double[][] getLinearTransformation(ThreeDimensionalPoint[] p) {
		n = 1;
		ThreeDimensionalPoint v1 = new ThreeDimensionalPoint(p[n].x - p[0].x, p[n].y - p[0].y, p[n].z - p[0].z);
		while (v1.magnitude() < 0.00001) {
			n++;
			v1.x = p[n].x - p[0].x; v1.y = p[n].y - p[0].y; v1.z = p[n].z - p[0].z;
		}
		
		n++;
				
		ThreeDimensionalPoint v2 = new ThreeDimensionalPoint(p[n].x - p[n-1].x, p[n].y - p[n-1].y, p[n].z - p[n-1].z);
		
		double dotProduct = dot(v1, v2);
						
		while ((v2.magnitude() < 0.00001) || Math.abs(Math.abs(dotProduct) - v1.magnitude()*v2.magnitude()) < 0.00001) {
			n++;
			v2.x = p[n].x - p[n-1].x; v2.y = p[n].y - p[n-1].y; v2.z = p[n].z - p[n-1].z; 
			dotProduct = dot(v1, v2);
		}
				
		ThreeDimensionalPoint v3 = getCrossProduct(v1, v2);
		
		ThreeDimensionalPoint v4 = getCrossProduct(v3, v1);
		
		normal = v3;
		
		/*System.out.printf("(%.2f, %.2f, %.2f)\n", v1.x, v1.y, v1.z);
		System.out.printf("(%.2f, %.2f, %.2f)\n", v2.x, v2.y, v2.z);
		System.out.printf("(%.2f, %.2f, %.2f)\n", v3.x, v3.y, v3.z);
		System.out.printf("(%.2f, %.2f, %.2f)\n", v4.x, v4.y, v4.z);*/
		
		// cob = change of basis; converts plane coords to standard coords
		double[][] cob = 	{{v1.x, v4.x, v3.x},
							 {v1.y, v4.y, v3.y},
							 {v1.z, v4.z, v3.z}};
		

		double[][] linTrans = inverse(cob);
		for (int i = 0; i < 3; i++) {
			linTrans[0][i] *= v1.magnitude();
			linTrans[1][i] *= v4.magnitude();
			linTrans[2][i] *= v3.magnitude();
		}
		
		/*double[][] vector1 = {{v1.x},
							  {v1.y},
							  {v1.z}};
		
		double[][] vector4 = {{v4.x/v4.magnitude()},
				  {v4.y/v4.magnitude()},
				  {v4.z/v4.magnitude()}};
		
		double[][] vector3 = {{v3.x/v3.magnitude()},
				  {v3.y/v3.magnitude()},
				  {v3.z/v3.magnitude()}};
		
		double[][] result = matrixMult(linTrans, vector3);
		System.out.printf("(%.2f, %.2f, %.2f)\n", result[0][0], result[1][0], result[2][0]);*/
		
		return linTrans;
		
	}
	
	public Points[] getPlanePoints(ThreeDimensionalPoint[] point, double[][] lT) { // lT = linTrans
		
		double[][][] p = new double[point.length][3][1];
		for (int i = 0; i < point.length; i++) {
			p[i][0][0] = point[i].x - point[0].x;
			p[i][1][0] = point[i].y - point[0].y;
			p[i][2][0] = point[i].z - point[0].z;
		}
		double[][] p0 = {{0}, 
						 {0},
						 {0}};
				
		double[][] p1 = matrixMult(lT, p[n-1]);
		
		int[] differenceBetweenAdditionalPoints = new int[point.length - 2];
		int index = 0;
		int previousInt = n - 1;
		int nextInt;
		ThreeDimensionalPoint v1 = new ThreeDimensionalPoint(p[n][0][0] - p[n-1][0][0], p[n][1][0] - p[n-1][1][0], 
				p[n][2][0] - p[n-1][2][0]);
		
		for (int i = n + 1; i <= p.length; i++) {
			if (i < p.length) {
				
				ThreeDimensionalPoint v2 = new ThreeDimensionalPoint(p[i][0][0] - p[i-1][0][0], p[i][1][0] - p[i-1][1][0], 
						p[i][2][0] - p[i-1][2][0]);
							
				double dotProduct = dot(v1, v2);
								
				while ((i < p.length) && ((v2.magnitude() < 0.00001) || 
						Math.abs(Math.abs(dotProduct) - v1.magnitude()*v2.magnitude()) < 0.00001)) {
					i++;
					v2.x = p[i][0][0] - p[i-1][0][0]; v2.y = p[i][1][0] - p[i-1][1][0]; v2.z = p[i][2][0] - p[i-1][2][0];
					dotProduct = dot(v1, v2);
				}
				v1 = v2;
			}
			nextInt = i - 1;
			differenceBetweenAdditionalPoints[index] = nextInt - previousInt;
			previousInt = nextInt;
			index++;
		}
		
		double[][][] adjustedP = new double[index][3][1];
		int windex = n - 1;
		
		double [][] normalMat = {{normal.x},
								 {normal.y},
								 {normal.z}};
		
		for (int i = 0; i < adjustedP.length; i++) {
			windex += differenceBetweenAdditionalPoints[i];
			double t = (-matrixMult(lT, p[windex])[2][0]) / matrixMult(lT, normalMat)[2][0];
			
			for (int coord = 0; coord < 3; coord++) {
				adjustedP[i][coord][0] = p[windex][coord][0] + t * normalMat[coord][0];
			}

		}
		Points[] planePoints = new Points[2 + index];
		for (int i = 0; i < planePoints.length; i++) {
			planePoints[i] = new Points();
		}
		planePoints[0].x = p0[0][0]; planePoints[0].y = p0[1][0];
		planePoints[1].x = p1[0][0]; planePoints[0].y = p1[1][0];
		
		for (int i = 2; i < planePoints.length; i++) {
			
			double[][] temp = matrixMult(lT, adjustedP[i-2]);
			planePoints[i] = new Points(temp[0][0], temp[1][0]);
		}
		
		return planePoints;
	}
	
	public LineSegment[] getLineSegments(Points[] p) {
		
		LineSegment[] lS = new LineSegment[p.length];
		
		for (int i = 0; i < lS.length - 1; i++) {
			lS[i] = new LineSegment(p[i], p[i + 1]);
		}
		
		lS[lS.length - 1] = new LineSegment(p[p.length - 1], p[0]);
		
		boolean normalOrientationIsCorrect = false;
		int i = 0;
		
		while(!normalOrientationIsCorrect && i < lS.length) {

			boolean thereIsALegitHit = false;			// thereIsALegitHit = whether or not normal ray strikes a side
			int j = 0;									// of the plane
				
			if (i == 0) {
				j++;
			}
			while (!thereIsALegitHit && j < lS.length) {
				
				thereIsALegitHit = thereIsALegitHit(lS[i].normalVector, lS[i].normalStart, lS[j].lineVector, 
						lS[j].lineStart);
				j++;
				
				if (i == j) {
					j++;
				}
			}
			
			normalOrientationIsCorrect = !thereIsALegitHit;
			i++;			
		}
		
		if (!normalOrientationIsCorrect) {
			for (int index = 0; index < lS.length; index++) {
				lS[index].flipNormal();
			}
		}
		
		return lS;
	}
	
	// method sees whether ray w hits lineSegment v	
	public boolean thereIsALegitHit(Points w, Points wStart, Points v, Points vStart ) {
		
		if (Math.abs(Math.abs(dot(w, v)) - w.magnitude() * v.magnitude()) < 0.00001) {
			return false;
		}
		
		double lineSegParam;
		
		if (Math.abs(w.y) > 0.00001) {
			lineSegParam = (wStart.x - vStart.x + w.x / w.y * (vStart.y - wStart.y)) / (v.x - w.x*v.y / w.y);
		} else {
			lineSegParam = (wStart.y - vStart.y) / v.y;
		}
		
		if (lineSegParam < 0 || lineSegParam > 1) {
			return false;
		}
		
		Points connection = new Points();
		connection.x = lineSegParam*v.x + vStart.x - wStart.x;
		connection.y = lineSegParam*v.y + vStart.y - wStart.y;
		
		if (dot(connection, w) < 0) {
			return false;
		}
		
		return true;
	}

	public double dot(ThreeDimensionalPoint v1, ThreeDimensionalPoint v2) {	// returns dot product of v1 and v2
		
		return v1.x*v2.x + v1.y*v2.y + v1.z*v2.z;
	}
	
	public double dot(Points v1, Points v2) {		// returns dot product of v1 and v2
		
		return v1.x*v2.x + v1.y*v2.y;
	}
	
	public ThreeDimensionalPoint getCrossProduct(ThreeDimensionalPoint v1, ThreeDimensionalPoint v2) {
		ThreeDimensionalPoint v3 = new ThreeDimensionalPoint(v1.y*v2.z - v1.z*v2.y, v1.z*v2.x - v1.x*v2.z, 
				v1.x*v2.y - v1.y*v2.x);
		return v3;
	}
	
	public static double[][] transpose(double[][] matrix) { 
		double[][] transposedMatrix = new double[matrix[0].length][matrix.length];
		for (int i=0;i<matrix.length;i++) { 
			for (int j=0;j<matrix[0].length;j++) { 
				transposedMatrix[j][i] = matrix[i][j];
				
			} 
			
		} 
		return transposedMatrix; 
		
	}

	public static double determinant(double[][] matrix) { 
	
		double sum = 0.0;
		if (matrix.length==1) {
			return matrix[0][0];
		}
					
		if (matrix.length!=matrix[0].length) {
			throw new IllegalArgumentException("matrix is not square");
		}
		
		if (matrix.length==2) { 
			return (matrix[0][0] * matrix[1][1]) - (matrix[0][1] * matrix[1][0]);
			
		} 
		
		for (int i=0; i<matrix[0].length; i++) { 
			double det = determinant(createSubMatrix(matrix, 0, i));

			sum += changeSign(i) * matrix[0][i] * det; 
			
		} 
		
		
		return sum; 
		
	}
	
	public static double[][] createSubMatrix(double[][] matrix, int excluding_row, int excluding_col) {
		double[][]  mat = new double[matrix.length - 1][matrix[0].length - 1];
		int r = -1; 
		for (int i=0;i<matrix.length;i++) { 
			if (i==excluding_row) {
				continue; 
			}
			
			r++; 
			int c = -1; 
			for (int j=0;j<matrix[0].length;j++) { 
				if (j==excluding_col) {
					continue; 
				}
				
				mat[r][++c] = matrix[i][j];
				
			} 
			
		} 
		
		return mat; 
		
	}

	public static int changeSign(int i) {
		if (i%2==0) {
			return 1;
		} else {
			return -1;
		}
	}
	
	public static double[][] cofactor(double[][] matrix) {
		double[][] mat = new double[matrix.length][matrix[0].length]; 
		for (int i=0;i<matrix.length;i++) { 
			for (int j=0; j<matrix[0].length;j++) { 
				mat[i][j] = changeSign(i) * changeSign(j) * determinant(createSubMatrix(matrix, i, j)); 
				
			} 
			
		} 
		
		return mat; 
		
	}
	
	public static double[][] inverse(double[][] matrix) {
		
		double[][] mat = transpose(cofactor(matrix));
		
		double constant = 1.0/determinant(matrix);
		
		for (int i = 0; i < mat.length; i++) {
			for (int j = 0; j < mat[0].length; j++) {
				mat[i][j] *= constant;
			}
		}
		
		return mat; 
		
	}
	
	public static double[][] matrixMult (double[][] m1, double[][] m2) {
		
		int nRows = m1.length; int nCols = m2[0].length;
		double[][] answer = new double[nRows][nCols];
		m2 = transpose(m2);
		
		for (int r = 0; r < nRows; r++) {
			for (int c = 0; c < nCols; c++) {
				
				answer[r][c] = 0.0;
				for (int entry = 0; entry < m1[0].length; entry++) {
					answer[r][c] += m1[r][entry] * m2[c][entry];
				}
			}
		}
		
		return answer;
	}
}
