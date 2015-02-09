import java.util.ArrayList;
import java.util.Random;

public class Matrix {

	private double[][] matrix;
	private int rows, columns;
	private String name, className, classNameDiscriminate;

	/**
	 * general form of the matrix, where there are no classes to be placed in
	 * @param m the matrix rows
	 * @param n the matrix columns
	 * @param mat the 2d array of data for the matrix
	 * @param na the name of the matrix
	 */
	public Matrix(int m, int n, double[][] mat, String na) {
		matrix = mat;
		rows = m;
		columns = n;
		name = na;
	}
	
	/**
	 * special matrix when a class name is required 
	 * @param m the matrix rows
	 * @param n the matrix columns
	 * @param mat the 2d array of data for the matrix
	 * @param na the name of the matrix
	 * @param cn the name of the class of the matrix
	 */
	public Matrix(int m, int n, double[][] mat, String na,String cn) {
		matrix = mat;
		rows = m;
		columns = n;
		name = na;
		className = cn;
	}
	
	/**
	 * runs the distance formula for selected nodes
	 * @param node
	 * @return then result of the operation
	 */
	public double distance(Matrix node){
		return Math.sqrt(Math.pow(node.matrix[0][0] - this.matrix[0][0], 2) + Math.pow(node.matrix[1][0] - this.matrix[1][0], 2) );
	}
	
	/**
	 * runs the power method to find all eigen values for an n X n matrix 
	 * @return the egien values
	 */
	public double[] powerMethod(){
		
		Random rG = new Random();
		
		double convergance = 0.001; //convergence value needs to be this high to do run time
		
		Matrix A = this;
		
		double[] eigenValues = new double [this.rows];
		
		//for every eigen value, run the power method on a deflated matrix(except for the largest)
		
		for(int index = 0; index < this. rows; index++){
		
		
		int i = 0;
		
		double rLength = 0;//collect the value of the error so it may be compared to the convergance standard
		
		double[][] rand = new double[this.rows][1];
		
		for(int ind = 0; ind < this.rows; ind++){
			
			rand[ind][0] = rG.nextDouble()+1; 
		}
		
		Matrix y = new Matrix(this.rows,1,rand,"random matrix", this.className);
		
		Matrix x = A.muiltiply(y);
		
		double u = 0;
		
		do{
			
			y = x.ScalarMultiplication((1/x.vectorLength()));
		
			x = A.muiltiply(y);
			
			 u = (y.transpose().muiltiply(x).getData(0, 0))/(y.transpose().muiltiply(y).getData(0,0));
			
			Matrix r = (y.ScalarMultiplication(u)).subtract(x);
			
			rLength = r.vectorLength();
			
			i +=1;
			
		}while(rLength > convergance && i < 10000); // do this until a convergence standard has been met or a number of iterations has passed
		
		eigenValues[index] = u; //add the new eigen value
		
		Matrix oldX = x.ScalarMultiplication(1/x.vectorLength());
		
		A = A.subtract(oldX.muiltiply(oldX.transpose()).ScalarMultiplication(eigenValues[index]));
		
		}
		
		
		
		return eigenValues;
		
		
	}
	
	
	/**
	 * returns the length of a 2 X 1 vector 		
	 * @return the selected vectors length
	 */
	public double vectorLength(){
		double l = 0; 
		
		for(int ind =0; ind < this.rows; ind++){
			l += Math.pow(this.matrix[ind][0], 2);
		}
		
		return Math.sqrt(l);
	}
	
	/**
	 *returns the 
	 * @return
	 */
	public double[] LeverriersAlgorithm(){
		double[] cP = new double[this.columns];
		
		double trace = this.trace();
		
		cP[0] = trace;
		
		Matrix temp =  new Matrix(this.rows,this.columns,this.matrix, this.name, this.className);
		
		for(int i = 1; i < this.columns; i++){
			
			
			 temp = temp.subtract((this.IdentityMatrix("m").ScalarMultiplication(trace))).muiltiply(this);
			
			trace = temp.trace()/(i+1);
			
			cP[i] = trace;
		}
		
		for(int i = 0; i < cP.length; i++){
			cP[i] = -1*cP[i];
		}
		
		return cP;
	}
	
	/**
	 * calculates the eigen values for a 2x2 matrix
	 * @return eigen values
	 */
	public double[] getEigenValues(){
		
		double[] eV =  quadraticEquation(1, -1*this.trace(), this.determinant()); 
		
		return eV;
		
	}
	
	/**
	 * calculates the unit length eigen vectors for a 2x2 matrix
	 * @return unit length eigen vectors
	 */
	public Matrix[] getEigenVectors( double[] eigenValues){
		
		Matrix[] m = new Matrix[eigenValues.length];
		
		for(int ind =0; ind < this.getEigenValues().length;ind++){
			
			Matrix e = this.subtract(this.IdentityMatrix("m").ScalarMultiplication(eigenValues[ind])); //Perform the manipulations
			
			e = new Matrix(this.rows,this.columns,e.subtractColumnOver(), this.name, this.className);//subtract the X1 values over 
			
			
			for(int i = 0; i < e.rows; i++){			
				for(int j =0; j < e.columns; j++)
				{
					e.matrix[i][j] = e.matrix[i][j]/e.matrix[i][1];
				}
			}
			
			double[][] eVector = new double[1][e.columns];
		
			for(int i = 0; i < e.columns; i++){
				eVector[0][i] = e.matrix[0][i];
			}
		
			e = new Matrix(1,this.columns,eVector, this.name, this.className);//subtract the X1 values over 
		
			double length = e.vectorLength();
		
			for(int i = 0; i < e.columns; i++){
				e.matrix[0][i] = e.matrix[0][i]/length;
			}

			m[ind] = e;
	
			}
		
		return m;
	}
	
	
	 //Performs the quadratic equation for three selected values 
	private double[] quadraticEquation(double a, double b, double c){
		double[] result = new double[2];
		
		double rOne =0, rTwo = 0;
		
		rOne = ((-1*b) + Math.sqrt(Math.pow(b, 2) - (4 * a * c)) )/(2*a);
		rTwo = ((-1*b) - Math.sqrt(Math.pow(b, 2) - (4 * a * c)) )/(2*a);
		
		result[0] = rOne;
		result[1] = rTwo;
		
		return result;
	}
	
	// for a series of equations moving the first value over to the other side results in a negative of that number on the opposite end of the matrix
	private double[][] subtractColumnOver(){
		double[][] newM = new double[this.rows][this.columns];
		
		
		
		for(int i = 0; i < newM.length; i++){
			double temp = this.matrix[i][0]*-1;
			for(int j = 0; j < newM[i].length-1; j++){
			
			
			newM[i][j] = this.matrix[i][j+1];
			
			
			}
			newM[i][newM[i].length-1] = temp;
		}
		
		
		return newM;
	}
	
	/**
	 * generates the trace of the matrix
	 * @return the trace
	 */
	public double trace(){
		double trace = 0;
		
		for(int i = 0; i < this.columns; i++){
			trace += this.matrix[i][i];
		}
		
		return trace;
	}
	
	/**
	 * generates the norm of the matrix
	 * @return the norm of the matrix
	 */
	public double norm(){
		double norm = 0; //the norm value
		double currentRowResult = 0; //the addition of the absolute value of the current row
		
		for(int i = 0; i< this.rows; i++){
			currentRowResult = 0; //clear the row additions
			for(int j =0; j < this.columns; j++){
				currentRowResult += Math.abs(this.getData(i, j));
			}
			
			if(currentRowResult > norm){ //if row additions are greater than current norm, then change the norm to the new number
				norm = currentRowResult;
			}
		}
		
		return norm;
	}
	
	/**
	 * sets the name of a matrix (primarily used to reset the names of mean and covariance matrices)
	 * @param n
	 */
	public void setName(String n){
		name = n;
	}
	
	/**
	 * returns the name of the matrix
	 * @return the name of the matrix
	 */
	public String getName(){
		return name;
	}
	
	/**
	 * the class calculated by the discriminate function is placed here 
	 * @param s , the name of the class
	 */
	public void setDiscriminateClassName(String s){
		classNameDiscriminate = s;
	}
	
	/**
	 * returns the name of the class that the discriminate function calculated
	 * @return
	 */
	public String getDiscriminateClassName(){
		return classNameDiscriminate;
	}
	
	/**
	 * returns the actual class name of the matrix
	 * @return the class name
	 */
	public String getClassName(){
		return className;
	}
	
	/**
	 * creates an identity matrix for a selected matrix
	 * @param m the selected matrix
	 * @param addmul determines whether identity is an addition identity or a muplication identity
	 * @return the identity matrix 
	 */
	public Matrix IdentityMatrix( String addmul){
		
		double id[][] = new double[this.rows][this.columns];
		Matrix identity = null;
		
	if(addmul.equals("a")){ //if the user wants to make an addition identity matrix
		for(int i = 0; i < id.length; i++)
			for(int j = 0; j < id[i].length; j++){
				id[i][j] = 0;
			}
		
		identity = new Matrix(this.rows,this.columns,id,"addition identity " + this.rows + " " + this.columns + " matrix");
		
	}else{ //else create a multiplication identity 
		
		int counter;  
	    counter = 0;
		
		for(int i = 0; i < id.length; i++)
			for(int j = 0; j < id[i].length; j++){
				if(i == counter && j == counter ){
					id[i][j] = 1; //for the diagonal, set the value to 1 instead of 0
					counter++;
				}else
				id[i][j] = 0;  //all other locations set 0
			}
		
		identity = new Matrix(this.rows,this.columns,id,"multiplication identity " + this.rows + " " + this.columns + " matrix");
	}
	
	
	
	return identity;
	
	}
	
	/**
	 * returns the result of a gauss-jordan operation on a matrix 
	 * @return gauss-jordan result matrix
	 */
	public Matrix GaussJordan(){
		
		double a[][] = new double[this.rows][this.columns]; //make sure that the data in the matrix is the same as the selected matrix,
															//but that the operation does not change the data in the original matrix
		
		for(int i = 0; i < this.rows; i++)
			for(int j = 0; j < this.columns; j++)
				a[i][j] = this.matrix[i][j];
		
		Matrix answer = new Matrix(this.rows,this.columns,a,"row reduced echelon form of " + this.name);
		
		answer.sortArrayRows();
		
		
		int variableIndex = 0;
		//Perform the eliminations
		while (variableIndex < answer.rows){
			double variable = answer.matrix[variableIndex][variableIndex];		
			double temp[] = new double[answer.columns];
			double tempForTemp[] = new double[answer.columns];
			for(int j = 0; j < answer.columns; j++){
				answer.matrix[variableIndex][j] = answer.matrix[variableIndex][j]/variable; //go through pivot row, dividing it by the pivot
				temp[j] = answer.matrix[variableIndex][j]; //place row in temporary array to do operations
				tempForTemp[j] = temp[j]; //place in secondary array to reset row
			}
			
			
			
			for(int i = 0; i < answer.rows; i++){
				if(i != variableIndex){
				double tempVar = answer.matrix[i][variableIndex];
				
				for(int j = 0; j < answer.columns; j++){
				temp[j] = temp[j]*tempVar;
				answer.matrix[i][j] = answer.matrix[i][j] - temp[j];
				
				}
				
				for(int j = 0; j < answer.columns; j++)
					 temp[j] = tempForTemp[j];
			}
				
			}
			
			variableIndex++;
			
		}
		
		return answer;
	}
	
	/**
	 * returns the result of a gauss-elimination operation on a matrix 
	 * @return gauss-elimination result matrix
	 */
	public Matrix GaussElimination(){
		
		double a[][] = new double[this.rows][this.columns];
		
		for(int i = 0; i < this.rows; i++)
			for(int j = 0; j < this.columns; j++)
				a[i][j] = this.matrix[i][j];
		
		Matrix answer = new Matrix(this.rows,this.columns,a,"echelon form of " + this.name);
		answer.sortArrayRows();
		
		int variableIndex = 0;
		ArrayList<Integer> positions = new ArrayList<Integer>(); 
		//Perform the eliminations
		while (variableIndex < answer.rows){
			double variable = answer.matrix[variableIndex][variableIndex];
			double temp[] = new double[answer.columns];
			double tempForTemp[] = new double[answer.columns];
			for(int j = 0; j < answer.columns; j++){
				
				temp[j] = answer.matrix[variableIndex][j]; //place row in temporary array to do operations
				tempForTemp[j] = temp[j]; //place in secondary array to reset row
			}
			
			
			
			for(int i = variableIndex+1; i < answer.rows; i++){
				
				int k = 0;
				
				for(Integer p: positions){
					if( i == p){
						k++;
						
					}
				}
				
				positions.add(i);
				
				
				
				double var =  answer.matrix[k][k];
				
				double tempVar = answer.matrix[i][k];
				
				for(int j = 0; j < answer.columns; j++){
				temp[j] = temp[j]*(tempVar/var);
				answer.matrix[i][j] = answer.matrix[i][j] - temp[j];
				
				}
				
				for(int j = 0; j < answer.columns; j++)
					 temp[j] = tempForTemp[j];
			}
				
			
			
			variableIndex++;
			
		}
		
		return answer;
	}
	
	
	  
	//sorts a matrix so that the row with the largest first value is the top row 
	private void sortArrayRows(){
		
		//sort the 2d array by rows
				for(int i = 0; i < this.rows; i++)
				{
				    //Set initial value as the current largest row
				int index_of_max = i;
				double max_value = Math.abs(this.matrix[i][0]);
				     
				    for(int j = i + 1; j < this.rows; j++)
				    {
				        if( Math.abs(this.matrix[j][0]) > max_value)
				        {
				            max_value = Math.abs(this.matrix[j][0]);
				            index_of_max = j;
				        }
				    }
				     
				    //Swap first row and max value
				    if( index_of_max != i)
				    {
				    	
				        for(int j =0; j< this.columns;j++){
				        	double tmp = this.matrix[i][j];
				        	this.matrix[i][j] = this.matrix[index_of_max][j];
				        	this.matrix[index_of_max][j] = tmp;
				        }
				    }
				}
		
		
	}
	
	/**
	 * creates an inverse of a selected matrix
	 * @return the inverse matrix
	 */
	public Matrix inverseMatrix(){
		Matrix in = IdentityMatrix("m"); //create a multiplication identity matrix
		
		double[][] matrixAndId = new double[this.rows][this.columns*2]; //get ready to attach the identity to the original matrix 
		
		
		//stick the two matrices together
		for(int i = 0; i < matrixAndId.length; i++)
			for(int j = 0; j < matrixAndId[i].length; j++){
				if(j >= this.columns){
					matrixAndId[i][j] = in.matrix[i][j-this.columns];
				}else
				matrixAndId[i][j] = this.matrix[i][j];
				
			}
		//put the result in the matrix
		Matrix inverse = new Matrix(this.rows,this.columns*2, matrixAndId, "identity for inverse");
		
		
		//Perform a gauss-jordan on the new matrix
		Matrix newInverse = inverse.GaussJordan();
		
		//get ready to remove the new identity matrix
		double answerData[][] = new double[this.rows][this.columns];
		
		
		//remove the identity matrix 
		for(int i = 0; i < answerData.length; i++)
			for(int j = 0; j < answerData[i].length;j++)
				answerData[i][j] = newInverse.matrix[i][j+this.columns];
		
		//inverse matrix
		Matrix answer = new Matrix(this.rows,this.columns,answerData, "The inverse of " + this.name);
		
		return answer;
	}
	
	/**
	 * multiples two matrices together 
	 * @param m the matrix to be multiplied to
	 * @return the product of the two matrices
	 */
	public Matrix muiltiply(Matrix m){
		if(this.columns == m.rows){
			double product[][] = new double[this.rows][m.columns];
			for (int i = 0; i < product.length; i++)
				for (int j = 0; j < product[i].length; j++) {
					for(int k = 0; k < this.columns; k++){
					product[i][j] += this.matrix[i][k] * m.matrix[k][j]; //Perform the matrix multiplication 
					}
				}
			
			Matrix mProduct = new Matrix(this.rows,m.columns,product, "product of " + this.name + " and " + m.name);
			
			return mProduct;
			
		}
		
		return null;
		
	}
	/**
	 * finds the determinate value of a selected matrix
	 * @return the determinate of the matrix
	 */
	public double determinant(){
		double determinant = 1;
		
		if(this.rows == this.columns && this .rows == 2){
			determinant = (this.matrix[0][0] * this.matrix[1][1] - this.matrix[0][1] * this.matrix[1][0]);
		}else{
		
		Matrix GEmatrix = this.GaussElimination(); //do not tamper with data
		
		int index = 0; //make sure that the values multiplied are on the diagonal of the matrix 
		
		while(index < GEmatrix.rows){
		
		for(int i = 0; i < GEmatrix.rows; i++)
			for(int j = 0; j < GEmatrix.columns; j++)
				if(i == index && j == index){
					determinant = determinant * Math.abs(GEmatrix.matrix[i][j]); //if on the diagonal then multiply 
					index++; //set for next diagonal position in matrix
				}
		}
		}
		return determinant;
	}
	
	/**
	 * adds two matrices together
	 * @param m matrix to be added to
	 * @return the sum of the matrices
	 */
	public Matrix add(Matrix m){
		if(this.rows == m.rows && this.columns == m.columns){ //check if addition is possible if not no value is produced
			
			double sumData[][] = new double[this.rows][this.columns];
			
			//create new matrix to make sure old data is not modified
			Matrix sum = new Matrix(this.rows,this.columns,sumData,"sum of " + this.name + " and " + m.name, this.className); 
			
			for (int i = 0; i < this.matrix.length; i++)
				for (int j = 0; j < this.matrix[i].length; j++) {
					sum.matrix[i][j] = this.matrix[i][j] + m.matrix[i][j]; //add the matrix data 
				}
			
			
			return sum;
		}
		
		return null;
	}
	
	/**
	 * creates a transpose of a selected matrix
	 * @return the transpose of a matrix
	 */
	public Matrix transpose(){
		
		double transposeData[][] = new double[this.columns][this.rows]; //create new matrix so old data is not modified 
		
		for(int i = 0; i < this.matrix.length; i++)
			for(int j = 0; j < this.matrix[i].length; j++)
				transposeData[j][i] = this.matrix[i][j]; //swap the row and column locations to create a transpose
				
		Matrix transpose = new Matrix(this.columns,this.rows, transposeData, "transpose of " + this.name, this.className);
		
		return transpose;
		
		
	}
	
	/**
	 * subtracts two matrices
	 * @param m the matrix to be subtracted from
	 * @return the difference of the two matrices
	 */
	public Matrix subtract( Matrix m){
		if(this.rows == m.rows && this.columns == m.columns){
			double result[][] = new double[this.rows][m.columns];
			for (int i = 0; i < result.length; i++)
				for (int j = 0; j < result[i].length; j++) {
						result[i][j] = this.matrix[i][j] - m.matrix[i][j]; //Subtract each value from the corresponding value on the other matrix
				}
			Matrix mResult = new Matrix(this.rows,m.columns,result,"subtraction of " + this.name + " and " + m.name);
			
			return mResult;
		}
		
		return null;
	}
	
	/**
	 * multiply a matrix by a scalar value
	 * @param s the scalar value to be multiplied 
	 * @return the resulting matrix of the scalar multiplication 
	 */
	public Matrix ScalarMultiplication(double s) {
		Matrix mScalar = new Matrix(this.rows,this.columns,this.matrix,"scalar multiplication of" + this.name + "  " + s);
		for (int i = 0; i < mScalar.rows; i++)
			for (int j = 0; j < mScalar.columns; j++) {
				mScalar.matrix[i][j] = s * this.matrix[i][j]; //multiply each value by a scalar value
			}
		
		return mScalar;

	}
	
	/**
	 * returns the number of rows in a matrix
	 * @return the row number
	 */
	public int getRows(){
		return rows;
	}
	
	/**
	 * returns the number of columns in a matrix
	 * @return the columns number
	 */
	public int getColumns(){
		return columns;
	}
	
	/**
	 * Retrieves the data of a matrix at a specifically selected spot within it 
	 * @param m the row value
	 * @param n the column value
	 * @return the data at that location 
	 */
	public double getData(int m, int n){
		return matrix[m][n];
	}
	
	/**
	 * Modifies data in a matrix if needed, this will permanently change the data in a specific place in a matrix so be careful when using 
	 * @param m the row value
	 * @param n the column value
	 * @param d the new data to be placed in matrix
	 */
	public void setData(int m, int n, double d){
		this.matrix[m][n] = d;
	}

}
