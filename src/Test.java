
public class Test {

	public static void main(String[] args) {
		
		ThreeDimensionalPoint start = new ThreeDimensionalPoint(-1, -1, 0);
		ThreeDimensionalPoint solarVectorDirect = new ThreeDimensionalPoint(0, 2, 10);
		
		ThreeDimensionalPoint p1 = new ThreeDimensionalPoint(1, 0, 10);
		ThreeDimensionalPoint p2 = new ThreeDimensionalPoint(1, 4, 10);
		ThreeDimensionalPoint p3 = new ThreeDimensionalPoint(3, 4, 8);
		ThreeDimensionalPoint p4 = new ThreeDimensionalPoint(3, 0, 8);
		
		/*ThreeDimensionalPoint[] point = {p1, p2, p3, p4};
		double[][] linearTransformation = getLinearTransformation(point); // also finds and sets normal
		Points[] planePoint = getPlanePoints(point, linearTransformation);
		
		System.out.printf("%.2f\t%.2f\t%.2f\n", linearTransformation[0][0], linearTransformation[0][1], linearTransformation[0][2]);
		System.out.printf("%.2f\t%.2f\t%.2f\n", linearTransformation[1][0], linearTransformation[1][1], linearTransformation[1][2]);
		System.out.printf("%.2f\t%.2f\t%.2f\n\n", linearTransformation[2][0], linearTransformation[2][1], linearTransformation[2][2]);
		
		for (int i = 0; i < planePoint.length; i++) {
			System.out.println(planePoint[i].x);
			System.out.println(planePoint[i].y);
			
			System.out.println();
			
		}*/
		Obstruction[] o = new Obstruction[1];
		o[0] = new Obstruction(p1, p2, p3, p4, 0);
		
		/*//ThreeDimensionalPoint test = new ThreeDimensionalPoint(p3.x-p2.x, p3.y-p2.y, p3.z-p2.z);
		//System.out.println(test.magnitude());
		
		//System.out.printf("%.2f\t%.2f\t%.2f\n", o[0].linearTransformation[0][0], o[0].linearTransformation[0][1], o[0].linearTransformation[0][2]);
		//System.out.printf("%.2f\t%.2f\t%.2f\n", o[0].linearTransformation[1][0], o[0].linearTransformation[1][1], o[0].linearTransformation[1][2]);
		//System.out.printf("%.2f\t%.2f\t%.2f\n\n", o[0].linearTransformation[2][0], o[0].linearTransformation[2][1], o[0].linearTransformation[2][2]);
		
		for (int i = 0; i < o[0].lineSegment.length; i++) {
			System.out.println(o[0].lineSegment[i].lineStart.x);
			System.out.println(o[0].lineSegment[i].lineStart.y);
			
			System.out.println();
			
			System.out.println(o[0].lineSegment[i].lineVector.x);
			System.out.println(o[0].lineSegment[i].lineVector.y);
			
			System.out.println("\n");
		}
		
		for (int i = 0; i < o[0].lineSegment.length; i++) {
			System.out.println(o[0].lineSegment[i].normalStart.x);
			System.out.println(o[0].lineSegment[i].normalStart.y);
			
			System.out.println();
			
			System.out.println(o[0].lineSegment[i].normalVector.x);
			System.out.println(o[0].lineSegment[i].normalVector.y);
			
			System.out.println("\n");
		}
		
		for (int i = 0; i < o[0].planePoint.length; i++) {
			System.out.println(o[0].planePoint[i].x);
			System.out.println(o[0].planePoint[i].y);
			
			System.out.println();
			
		}
		
		System.out.printf("(%.2f, %.2f, %.2f)\n%.2f\n", o[0].normal.x, o[0].normal.y, o[0].normal.z, o[0].normal.magnitude());
			
		*/boolean[] thereIsDirectObstruction = thereIsObs(start, solarVectorDirect, o, -1.0);
		
		System.out.print(thereIsDirectObstruction[0]);
		
		
		/*//double[][] mat = {{2.0, 1.0}, {1.0, 2.0}};
		double[][] mat = {{1, 7, 2}, {8, 4, 2}, {3, 7, 9}};
		
		for (int i = 0; i < mat.length; i++) {
			for (int j = 0; j < mat[0].length; j++) {
				System.out.print(mat[i][j]);
			}
			System.out.println();
		}
		
		double[][] inverseMat = inverse(mat);
		
		double[][] answer = matrixMult(mat, inverseMat);
		
		//double constant = 1.0/determinant(mat);
		
		for (int i = 0; i < answer.length; i++) {
			for (int j = 0; j < answer[0].length; j++) {
				System.out.printf("%.2f\t", answer[i][j]);
			}
			System.out.println();
		}*/
	
	}

	private static ThreeDimensionalPoint normal;
	private static int n;
	
	public static double[][] getLinearTransformation(ThreeDimensionalPoint[] p) {
		n = 1;
		ThreeDimensionalPoint v1 = new ThreeDimensionalPoint(p[n].x - p[0].x, p[n].y - p[0].y, p[n].z - p[0].z);
		while (v1.magnitude() < 0.00001) {
			n++;
			v1.x = p[n].x - p[0].x; v1.y = p[n].y - p[0].y; v1.z = p[n].z - p[0].z;
		}
		
		//v1.x /= v1.magnitude(); v1.y /= v1.magnitude(); v1.z /= v1.magnitude();
		n++;
				
		ThreeDimensionalPoint v2 = new ThreeDimensionalPoint(p[n].x - p[n-1].x, p[n].y - p[n-1].y, p[n].z - p[n-1].z);
		//v2.x /= v2.magnitude(); v2.y /= v2.magnitude(); v2.z /= v2.magnitude();
		
		double dotProduct = dot(v1, v2);
						
		while ((v2.magnitude() < 0.00001) || Math.abs(Math.abs(dotProduct) - v1.magnitude()*v2.magnitude()) < 0.00001) {
			n++;
			v2.x = p[n].x - p[n-1].x; v2.y = p[n].y - p[n-1].y; v2.z = p[n].z - p[n-1].z; 
			//v2.x /= v2.magnitude(); v2.y /= v2.magnitude(); v2.z /= v2.magnitude();
			dotProduct = dot(v1, v2);
		}
				
		ThreeDimensionalPoint v3 = getCrossProduct(v1, v2);
		//v3.x /= v3.magnitude(); v3.y /= v3.magnitude(); v3.z /= v3.magnitude();
		
		ThreeDimensionalPoint v4 = getCrossProduct(v3, v1);
		//v4.x /= v4.magnitude(); v4.y /= v4.magnitude(); v4.z /= v4.magnitude();
		
		normal = v3;
		
		System.out.printf("(%.2f, %.2f, %.2f)\n", v1.x, v1.y, v1.z);
		System.out.printf("(%.2f, %.2f, %.2f)\n", v2.x, v2.y, v2.z);
		System.out.printf("(%.2f, %.2f, %.2f)\n", v3.x, v3.y, v3.z);
		System.out.printf("(%.2f, %.2f, %.2f)\n", v4.x, v4.y, v4.z);
		
		//v1.x /= v1.magnitude(); v1.y /= v1.magnitude(); v1.z /= v1.magnitude();
		//v4.x /= v4.magnitude(); v4.y /= v4.magnitude(); v4.z /= v4.magnitude();
		//v3.x /= v3.magnitude(); v3.y /= v3.magnitude(); v3.z /= v3.magnitude();
		
		// cob = change of basis; converts plane coords to standard coords
		double[][] cob = 	{{v1.x, v4.x, v3.x},
							 {v1.y, v4.y, v3.y},
							 {v1.z, v4.z, v3.z}};
		
		/*// planeTrans = linear transformation in plane coord system
		double[][] planeTrans = 	{{1.0, 0.0, 0.0},
									 {0.0, 1.0, 0.0},
									 {0.0, 0.0, 0.0}};
				
		//double[][] linTrans = matrixMult(matrixMult(cob, planeTrans), inverse(cob));*/
		double[][] linTrans = inverse(cob);
		for (int i = 0; i < 3; i++) {
			linTrans[0][i] *= v1.magnitude();
			linTrans[1][i] *= v4.magnitude();
			linTrans[2][i] *= v3.magnitude();
		}
		
		double[][] vector1 = {{v1.x},
							  {v1.y},
							  {v1.z}};
		
		double[][] vector4 = {{v4.x/v4.magnitude()},
				  {v4.y/v4.magnitude()},
				  {v4.z/v4.magnitude()}};
		
		double[][] vector3 = {{v3.x/v3.magnitude()},
				  {v3.y/v3.magnitude()},
				  {v3.z/v3.magnitude()}};
		
		double[][] result = matrixMult(linTrans, vector3);
		
		
		System.out.printf("(%.2f, %.2f, %.2f)\n", result[0][0], result[1][0], result[2][0]);
		
		return linTrans;
		
	}
	
	public static Points[] getPlanePoints(ThreeDimensionalPoint[] point, double[][] lT) { // lT = linTrans
		
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
		
				//v1.x /= v1.magnitude(); v1.y /= v1.magnitude(); v1.z /= v1.magnitude();
				
				ThreeDimensionalPoint v2 = new ThreeDimensionalPoint(p[i][0][0] - p[i-1][0][0], p[i][1][0] - p[i-1][1][0], 
						p[i][2][0] - p[i-1][2][0]);
				
				//v2.x /= v2.magnitude(); v2.y /= v2.magnitude(); v2.z /= v2.magnitude();
				
				double dotProduct = dot(v1, v2);
								
				while ((i < p.length) && ((v2.magnitude() < 0.00001) || 
						Math.abs(Math.abs(dotProduct) - v1.magnitude()*v2.magnitude()) < 0.00001)) {
					i++;
					v2.x = p[i][0][0] - p[i-1][0][0]; v2.y = p[i][1][0] - p[i-1][1][0]; v2.z = p[i][2][0] - p[i-1][2][0];
					//v2.x /= v2.magnitude(); v2.y /= v2.magnitude(); v2.z /= v2.magnitude();
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
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	// returns whether or not v passes through each obstruction of the array; if maxT = -1, v = ray; otherwise, v = 
		// line segment with maximum parameter value of maxT
		public static boolean[] thereIsObs(ThreeDimensionalPoint vStart, ThreeDimensionalPoint v,
				Obstruction[] obstruction, double maxT) {
			
			boolean[] thereIsObstruction = new boolean[obstruction.length];
			
			for (int oIndex = 0; oIndex < obstruction.length; oIndex++) {
				Obstruction o = obstruction[oIndex];
				double vectorParam = (o.shift - dot(o.normal, vStart)) / 
						dot(v, o.normal);
				
				//System.out.println(vectorParam);
				//System.out.println();
				
				if (vectorParam >= 0 && (maxT == -1.0 || vectorParam <= maxT)) {
					double[][] threeDPlanePoint = {{vStart.x + vectorParam * v.x - obstruction[oIndex].point[0].x},
												   {vStart.y + vectorParam * v.y - obstruction[oIndex].point[0].y},
												   {vStart.z + vectorParam * v.z - obstruction[oIndex].point[0].z}};
					
					
					/*System.out.println(threeDPlanePoint[0][0]);
					System.out.println(threeDPlanePoint[1][0]);
					System.out.println(threeDPlanePoint[2][0]);*/
					
					//System.out.println();
					
					double[][] temp = matrixMult(o.linearTransformation, threeDPlanePoint);
					Points planePoint = new Points(temp[0][0], temp[1][0]);
					
					//System.out.println(planePoint.x);
					//System.out.println(planePoint.y);
					
					//System.out.println();
					
					thereIsObstruction[oIndex] = pointIsInside(planePoint, o.lineSegment);
				} else {
					thereIsObstruction[oIndex] = false;
				}
							
			}
			return thereIsObstruction;
		}

		// method returns whether or not a point is within a plane bounded by the given array of LineSegments
		public static boolean pointIsInside(Points point, LineSegment[] lS) {
			
			Points analysisRay = new Points(0, 1);	// arbitrary direction for analysis; ray starts at point
			double minDistance = legitHitDistance(analysisRay, point, lS[0].lineVector, lS[0].lineStart);
			LineSegment closestLS = lS[0];
			
			double currentDistance;
			
			for (int i = 1; i < lS.length; i++) {
				
				currentDistance = legitHitDistance(analysisRay, point, lS[i].lineVector, lS[i].lineStart);
				
				System.out.println(currentDistance);
				
				if (currentDistance != -1.0) {
					if (minDistance == -1.0) {
						minDistance = currentDistance;
						closestLS = lS[i];
					} else if (currentDistance < minDistance) {
						minDistance = currentDistance;
						closestLS = lS[i];
					}
				}
				
				System.out.printf("currentDistance:\t%.2f\nminDistance:\t%.2f\nclosestLS:\t(%.2f, %.2f)\n\n",
						currentDistance, minDistance, closestLS.lineStart.x, closestLS.lineStart.y);
			}
			
			if (minDistance == -1.0) {
				return false;
			}
			
			double dotProduct = dot(closestLS.normalVector, analysisRay);
			System.out.println(dotProduct);
			
			if (dotProduct < 0) {
				return false;
			}
			
			return true;
		}
		
		// method returns distance between legit ray w hits of lineSegment v and wStart; returns -1 for illegit hits
		public static double legitHitDistance(Points w, Points wStart, Points v, Points vStart ) {
				
			/*System.out.println(Math.abs(dot(w, v)) - w.magnitude() * v.magnitude());
			System.out.println(dot(w, v));
			System.out.println(w.x);
			System.out.println(v.x);*/
			
			if (Math.abs(Math.abs(dot(w, v)) - w.magnitude() * v.magnitude()) < 0.00001) {
				return -1.0;
			}			
			double lineSegParam;
			
			if (Math.abs(w.y) > 0.00001) {
				lineSegParam = (wStart.x - vStart.x + w.x / w.y * (vStart.y - wStart.y)) / (v.x - w.x*v.y / w.y);
				
			} else {
				lineSegParam = (wStart.y - vStart.y) / v.y;	
			}	
			
			if (lineSegParam < 0 || lineSegParam > 1) {
				return -1.0;
		
			}
				
			Points connection = new Points();
			connection.x = lineSegParam*v.x + vStart.x - wStart.x;
			connection.y = lineSegParam*v.y + vStart.y - wStart.y;
			
			//System.out.printf("connection:\t(%.2f, %.2f)\nmagnitude: %.2f\n\n",
			//		connection.x, connection.y, connection.magnitude());
			
			if (dot(connection, w) < 0) {
				return -1.0;
			}
						
			return connection.magnitude();
		}

		public static double dot(ThreeDimensionalPoint v1, ThreeDimensionalPoint v2) {	// returns dot product of v1 and v2
				
			return v1.x*v2.x + v1.y*v2.y + v1.z*v2.z;
			
		}
			
		public static double dot(Points v1, Points v2) {		// returns dot product of v1 and v2
				
			return v1.x*v2.x + v1.y*v2.y;
			
		}
		
		public static ThreeDimensionalPoint getCrossProduct(ThreeDimensionalPoint v1, ThreeDimensionalPoint v2) {
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
