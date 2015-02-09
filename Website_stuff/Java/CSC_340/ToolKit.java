import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Random;
import java.util.Scanner;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.awt.Color;

public class ToolKit {

	ArrayList<Matrix> matrices = new ArrayList<Matrix>(); //a collection of all matrices
	ArrayList<Matrix> input = new ArrayList<Matrix>(); //a collection of input
	ArrayList<Double> determinants = new ArrayList<Double>(); //a collection of determinate values 
	ArrayList<Matrix> meanMatrices = new ArrayList<Matrix>();//a collection of mean matrices
	ArrayList<Matrix> covarianceMatrices = new ArrayList<Matrix>();//a collection of covariance matrices
	ArrayList<Matrix> discriminateValues = new ArrayList<Matrix>();//a collection of the values created by the discriminate functions on input matrices and mean matrices  
	DecimalFormat df = new DecimalFormat("#.###");
	Scanner sc = new Scanner(System.in);
	
	
	public static void main(String[] args) {

		ToolKit t = new ToolKit();
		


	}
	
	/**
	 * method to type in the text file of the matrix data
	 * and determine how to read in the file (as m X n matrices or 2 x 1 coordinate vectors) 
	 */
	public ToolKit() {

		String filename;

		String vectorChoice;

		
		//do not include the .txt at the end of the file name
		System.out.println(" Enter the file name of the matrix data: ");
		Scanner sc = new Scanner(System.in);

		filename = sc.nextLine();

		System.out.println(" Do you want to read it as coordinate vector (y/n) ");
		//if y is not pressed, the data will be read as a m x n matrices
		
		
		vectorChoice = sc.nextLine();

		readMatrices(filename, vectorChoice); //read data and perform some basic operations on matrices

		menuScreen();
		sc.close();
	}
	
	/**
	 * menu screen for viewing matrices and performing specified operations 
	 * it is brought back after any action is taken
	 */
	public void menuScreen() {

		Scanner sc = new Scanner(System.in);

		System.out.println("\n\n\n\n\n\n\n\nwhat do you want to do?"
				+ "\na) list matrices			 h) get the inverse of a matrix					p) get the trace of a matrix"
				+ "\nb) add two matrices			 i) view matrix determinant					q) get the eigenvalues and eigne vectors of a matrix"
				+ "\nc) subtract two matrices 		 j) list misclassified points from discriminate functions 	r) Leverrier’s algorithm " 
				+ "\nd) multiply two matrices 		 k) get the discriminate functions of a vetex			s) power method "
				+ "\ne) multiply a matrix by a scalar	 l) get the boundary contour of the classes			t) traveling sales man problems"
				+ "\nf) preform Gauss Elimination on a matrx  m) get the condition number of a matrix			u) curve fit and find roots"
				+ "\ng) preform Gauss-Jordan on a matrix	 n) view all covariance matrices and their inverses		w) question 2 for final test"
				+ "\nv) view the data of a matrix		 o) get the product of two matrix determinants			z) read in a new matrix");

		String choice = sc.nextLine();

		if (choice.equals("a")) {
			listMatrices();
		} else if (choice.equals("b")) {
			addMatrices();
		} else if (choice.equals("c")) {
			subtractMatrices();
		} else if (choice.equals("d")) {
			multiplyMatrices();
		} else if (choice.equals("e")) {
			scalarMultiplyMatrix();
		} else if (choice.equals("f")) {
			Gauss_Elimination();
		} else if (choice.equals("g")) {
			Gauss_Jordan();
		} else if (choice.equals("v")) { 
			viewMatrixData();
		} else if (choice.equals("h")) {
			createInverse();
		} else if (choice.equals("i")) {
			viewDeterminant();
		}else if (choice.equals("j")){
			misclassifiedDiscriminantFunction();
		}else if(choice.equals("k")){
			vertexDiscriminate();
		}else if(choice.equals("l")){
			boundaryContour();
		}else if(choice.equals("m")){
			conditionNumber();
		}else if(choice.equals("n")){
			viewCovariance();
		}else if(choice.equals("o")){
			multiplyDeterminants();
		}else if(choice.equals("p")){
			matrixTrace();
		}else if(choice.equals("q")){
			eigen();
		}else if(choice.equals("r")){
			Leverrier();
		}else if(choice.equals("s")){
			power();
		}else if(choice.equals("t")){
			tsp();
		}else if(choice.equals("u")){
			fitCurveFindRoot();
		}else if(choice.equals("w")){
			questionTwoFinaltest();
		}else if(choice.equals("z")){
			System.out.println("name the matrix you wish to add: ");
			
			String filename = sc.next();
			
			readMatrices(filename, "n");
			
			goBacktoMenu();
		}else {
		
			menuScreen();
		}

	}
	
	/**
	 * runs the software to answer the 2nd question of the final CSC 340 test (main menu was running out of room).
	 */
	public void questionTwoFinaltest(){
		
		Matrix ma1 = getFirstMatrix();
		
		System.out.println("a) fs(x) and gs(x) graphs 			h) 2 dimensonal correlation\n" +
						   "b) PSD graph data \n" +
						   "c) sum of and product of sine function\n"+
						   "d) sine26PI(x-c)\n"+
						   "e) filter graph data\n"+
						   "f) correlation\n" +
						   "g) convolution");
		
		String choice = sc.next();
		
		if(choice.equals("a")){
			commonSignalsGraph();
		}else if (choice.equals("b")){	
			PSD(ma1);
		}else if (choice.equals("c")){
			sineSumAndProductPSD();
		}else if (choice.equals("d")){
			sine26PI();
		}else if (choice.equals("e")){
			filterData(ma1);
		}else if (choice.equals("f")){
			correlation(ma1);
		}else if (choice.equals("g")){
			convolution(ma1);
		}else if (choice.equals("h")){
			twoDImagecorrelation();
		}
		
	}
	
	
	/**
	 * generates a signal starting a row 230 and column 206 that is 50 rows and 100 columns 
	 * a pulse that is 25 by 50
	 * the correlation between the two
	 * and renders and image based on the magnitude of the correlation
	 * image is 512 by 512 pixels
	 */
	public void twoDImagecorrelation(){
		
		
		//create singal and pulse
		Complex signal[][] = new Complex[512][512];
		
		Complex pulse[][] = new Complex[512][512];
		
		for(int i = 0; i < 512; i++){
			for(int j = 0; j < 512; j ++){
				
				if(i > 205 && i < 307 && j > 229 && j< 281){
					signal[i][j] = new Complex (255,0);
				}else{
					signal[i][j] = new Complex(0,0);
				}
				
				if(i < 51 && j < 26){
					pulse[i][j] = new Complex(255,0);
				}else{
				pulse[i][j] = new Complex(0,0);
				}
			}
		}
		
		//display the the picture and pulse
		
		Picture s = new Picture(512,512);
		Picture p = new Picture(512,512);
		
		for(int i = 0; i < 512; i++){
			for(int j = 0; j < 512; j ++){
				
				Color cS = new Color((int)signal[i][j].re(),(int)signal[i][j].re(),(int)signal[i][j].re());
				
				Color cP = new Color((int)pulse[i][j].re(),(int)pulse[i][j].re(),(int)pulse[i][j].re());
				
				s.set(i, j, cS);
				
				p.set(i,j,cP);
			}
		}
		
		s.show();
		
		p.show();
		
		//get the FFT of the pulse anf the signal
		
		Complex fSignal[][] = twoDFFT(signal,1);
		
		Complex fPulse[][] = twoDFFT(pulse,1);
		
		Complex product[][] = new Complex[512][512];
		
		for(int i = 0; i < 512; i++)
			for(int j = 0; j < 512; j ++){
				product[i][j] = fSignal[i][j].times(fPulse[i][j].conjugate());
			}
		
		//find their correlation
		
		Complex imageCor[][] = twoDFFT(product,-1);
		
		double pictureData[][] = new double[512][512];
		
		double max =0;
		
		//render the resulting correlation
		
		for(int i = 0; i < 512; i++)
			for(int j = 0; j < 512; j ++){
				
				
				
				if(max < imageCor[i][j].re()){
					max = imageCor[i][j].re();
				}
				
			}
		
		//scale correlation data
		
		for(int i = 0; i < 512; i++)
			for(int j = 0; j < 512; j ++){
				
				if(imageCor[i][j].re() < .00000001){
					pictureData[i][j] = 0;
				}else{
				
				pictureData[i][j] = (imageCor[i][j].re()/max)*255;
				}
				
			}
		
		for(int i = 0; i < 512; i++){				
					pictureData[i][0] = 0;			
			}
			

		try{
			  // Create file 
			  FileWriter fstream = new FileWriter("picdata.txt");
			  BufferedWriter out = new BufferedWriter(fstream);
			  for(int i = 0; i < 512; i++){
				  for(int j = 0; j < 512; j++){
					  out.write(df.format(pictureData[i][j])+ " ");
					  if(j == 511){
						  out.newLine();
					  }
					
					  
				  }
			  }	  
			  
			  //Close the output stream
			  out.close();
			  }catch (Exception e){//Catch exception if any
			  System.err.println("Error: " + e.getMessage());
			  }
		
		
		Picture imageCorrelation = new Picture(512,512);
		
		for(int i = 0; i < 512; i++){
			for(int j = 0; j < 512; j ++){
				
				

					Color c = new Color((int)pictureData[i][j],0,0);
				
				imageCorrelation.set(i, j, c);
			}
		}
		
		imageCorrelation.show();
		
		
		
	}
	
	private Complex[][] twoDFFT(Complex[][]z,int d){
		
		Complex colAns [][] = new Complex[z.length][z[0].length];
		
		
		//run the 1D FFT on all the columsn and then all the rows
		for(int i = 0; i < z.length; i++){
			
			Complex col[] = new Complex[z[i].length];
			
			for(int j = 0; j < z[i].length; j++){
				col[j] = z[i][j];
			}
			
			Complex fCol[] = FFTCoolyTukey(col,d);
			
			for(int j = 0; j < z[i].length; j++){
				colAns[i][j] = fCol[j];
			}
		}
		
		Complex ans [][] = new Complex[z.length][z[0].length];
		
		for(int i = 0; i < z.length; i++){
			
			Complex row[] = new Complex[z.length];
			
			for(int j = 0; j < z.length; j++){
				row[j] = colAns[j][i];
			}
			
			Complex fRow[] = FFTCoolyTukey(row,d);
			
			for(int j = 0; j < z[i].length; j++){
				ans[i][j] = fRow[j];
			}
		}
		
		
		return ans;
		
	}
	
	/**
	 * applies a specified convolution filter to a signal
	 * @param u
	 */
	public void convolution(Matrix u){
		
		System.out.println("what point filter do you want? ");
		
		double point = sc.nextDouble();
		
		//create filter
		
		Complex[] filter = new Complex[u.getRows()];
		
		Complex[] signal = new Complex[u.getRows()];
		
		for(int i = 0; i < filter.length;i++){
			
			signal[i] = new Complex(u.getData(i,0),0);
			
			if(i < (int)point){
			filter[i] = new Complex(1.0/point,0);
			
			
			}else{
				filter[i] = new Complex(0,0);
			}
			
		}
		
		Complex uM[] = FFTCoolyTukey(signal,1);
		Complex hM[] = FFTCoolyTukey(filter,1);
		
		//run FFT on signal and pulse, then the inverse to create the new graph
		
		Complex product[] = new Complex[uM.length];
		
		for(int i = 0; i < uM.length;i++){
			product[i] = uM[i].times(hM[i]);
		}
		
		Complex yK[] = FFTCoolyTukey(product,-1);
		
		double y[] = new double[yK.length];
		
		for(int i = 0; i < uM.length;i++){
			y[i] = yK[i].re();
		}
		
		try{
			  // Create file 
			  FileWriter fstream = new FileWriter("convolutionofSignal.txt");
			  BufferedWriter out = new BufferedWriter(fstream);
			  for(int i = 0; i < y.length; i++){
					  out.write(String.valueOf(y[i]));
					  out.newLine();
					  
				  }
				  
			  
			  //Close the output stream
			  out.close();
			  }catch (Exception e){//Catch exception if any
			  System.err.println("Error: " + e.getMessage());
			  }
		
		goBacktoMenu();
	}
	
	/**
	 * finds the correlation between two signals, provides the graph data and the peak index
	 * @param x the signal to be correlated against another
	 */
	public void correlation(Matrix x){
		
		Matrix y = matrices.get(1);
		
		Complex[] xData = new Complex[x.getRows()];
				
		Complex[] yData = new Complex[x.getRows()];		
		
		for(int i = 0; i < xData.length ;i++){
			
			xData[i] = new Complex(x.getData(i, 0),0);
			
			if( i < y.getRows()){	
				yData[i] = new Complex(y.getData(i, 0),0);
			}else{
				yData[i] = new Complex(0,0);
			}
			
		}
		
		Complex[] xM = FFTCoolyTukey(xData,1);
		
		Complex[] yM = FFTCoolyTukey(yData,1);
		
		Complex product[] = new Complex[x.getRows()];
		
		for(int i = 0; i < xM.length;i++){
			product[i] = xM[i].times(yM[i].conjugate());
		}
		
		Complex[]r =   FFTCoolyTukey(product,-1);
		
		double rReal[] = new double[xM.length];
		
		for(int i = 0; i < rReal.length; i++){
			rReal[i] = r[i].re();
		}
		
		double max = 0;
		int maxIndex = 0;
		for(int i = 0; i < rReal.length; i++){
			if(max < rReal[i] ){
				max = rReal[i];
				maxIndex = i;
			}
		}
		
		System.out.println("the peak frequency is: " + maxIndex);
		
		try{
			  // Create file 
			  FileWriter fstream = new FileWriter("CorrelationForPulseReturn.txt");
			  BufferedWriter out = new BufferedWriter(fstream);
			  for(int i = 0; i < rReal.length; i++){
					  out.write(String.valueOf(rReal[i]));
					  out.newLine();
					  
				  }
				  
			  
			  //Close the output stream
			  out.close();
			  }catch (Exception e){//Catch exception if any
			  System.err.println("Error: " + e.getMessage());
			  }
		
		goBacktoMenu();
		
	}
	
	/**
	 * runs the ideal low pass, high pass, band pass, and notch filters on a signal and returns the graph data
	 * @param m
	 */
	public void filterData(Matrix m){
		
		//get relevant frequncy information
		
		Complex[] singal = new Complex[m.getRows()];
		
		System.out.println("Low pass filter? ");
		
		double LPfH = sc.nextDouble()-1;
		
		System.out.println("High pass filter? ");
		
		double HPfL = sc.nextDouble()-1;
		
		System.out.println("Band filter low frequency? ");
		
		double BPL = sc.nextDouble()-1;
		
		System.out.println("Band filter high frequency? ");
		
		double BPH = sc.nextDouble()-1;
		
		System.out.println("Notch filter low frequency? ");
		
		double NPL = sc.nextDouble()-1;
		
		System.out.println("Notch filter high frequency? ");
		
		double NPH = sc.nextDouble()-1;
		
		double[] LPfilter = new double[m.getRows()];
		double[] HPfilter = new double[m.getRows()];
		double[] BPfilter = new double[m.getRows()];
		double[] NotchFilter = new double[m.getRows()];
		
		//create filters
		
		for(int i = 0; i < singal.length; i++){
			singal[i] = new Complex(m.getData(i, 0),0);
			
			if(i <= LPfH){
				 LPfilter[i] = 1;
			}else{
				 LPfilter[i] = 0;
			}
			
			if(i >=  HPfL){
				HPfilter[i] = 1;
			}else{
				HPfilter[i] = 0;
			}
			
			if(i >= BPL && i <= BPH ){
				BPfilter[i] = 1;
			}else{
				BPfilter[i] = 0;
			}
			
			if(i <= NPL || i >= NPH){
				NotchFilter[i] = 1;
			}else{
				NotchFilter[i] = 0;
			}
			
		}
		
		//get FFT
		
		Complex[] z = FFTCoolyTukey(singal,1);
		
		for(int i = 0; i < singal.length; i++){
			if(Math.abs(z[i].re())  < .000000000001 && Math.abs(z[i].im()) < .000000000001){
	  			z[i]= z[i].times(0);
	  		}
		}
		
		//apply filter to FFT data
		
		Complex[] LPfilterSingal = new Complex[m.getRows()];
		Complex[] HPfilterSingal = new Complex[m.getRows()];
		Complex[] BPfilterSingal = new Complex[m.getRows()];
		Complex[] NotchFilterSingal = new Complex[m.getRows()];
		
		for(int i = 0; i < singal.length; i++){	
		
			LPfilterSingal[i] = z[i].times(LPfilter[i]);
			HPfilterSingal[i] = z[i].times(HPfilter[i]);		
			BPfilterSingal[i] = z[i].times(BPfilter[i]);
			NotchFilterSingal[i] = z[i].times(NotchFilter[i]);
			
			
		}	
		
		//get the filtered data
		
		double[] LPfilterSingaldata = new double[m.getRows()];
		double[] HPfilterSingaldata = new double[m.getRows()];
		double[] BPfilterSingaldata = new double[m.getRows()];
		double[] NotchFilterSingaldata = new double[m.getRows()];
		
		Complex LP [] = FFTCoolyTukey(LPfilterSingal,-1);
		Complex HP [] = FFTCoolyTukey(HPfilterSingal,-1);	
		Complex BP [] =  FFTCoolyTukey(BPfilterSingal,-1);
		Complex Notch [] = FFTCoolyTukey(NotchFilterSingal,-1);
		
		for(int i = 0; i < singal.length; i++){
			
			LPfilterSingaldata[i] = LP[i].re();
			HPfilterSingaldata[i] = HP[i].re();
			BPfilterSingaldata[i] =  BP[i].re();
			NotchFilterSingaldata[i] = Notch[i].re();
		}
		
		try{
			  // Create file 
			  FileWriter fstream = new FileWriter("LowPassfilter.txt");
			  BufferedWriter out = new BufferedWriter(fstream);
			  for(int i = 0; i < LPfilterSingaldata.length; i++){
					  out.write(String.valueOf(LPfilterSingaldata[i]));
					  out.newLine();
					  
				  }
				  
			  
			  //Close the output stream
			  out.close();
			  }catch (Exception e){//Catch exception if any
			  System.err.println("Error: " + e.getMessage());
			  }
		
		try{
			  // Create file 
			  FileWriter fstream = new FileWriter("HighPassfilter.txt");
			  BufferedWriter out = new BufferedWriter(fstream);
			  for(int i = 0; i < HPfilterSingaldata.length; i++){
					  out.write(String.valueOf(HPfilterSingaldata[i]));
					  out.newLine();
					  
				  }
				  
			  
			  //Close the output stream
			  out.close();
			  }catch (Exception e){//Catch exception if any
			  System.err.println("Error: " + e.getMessage());
			  }
		
		try{
			  // Create file 
			  FileWriter fstream = new FileWriter("BandPassfilter.txt");
			  BufferedWriter out = new BufferedWriter(fstream);
			  for(int i = 0; i < BPfilterSingaldata.length; i++){
					  out.write(String.valueOf(BPfilterSingaldata[i]));
					  out.newLine();
					  
				  }
				  
			  
			  //Close the output stream
			  out.close();
			  }catch (Exception e){//Catch exception if any
			  System.err.println("Error: " + e.getMessage());
			  }
		
		try{
			  // Create file 
			  FileWriter fstream = new FileWriter("Notchfilter.txt");
			  BufferedWriter out = new BufferedWriter(fstream);
			  for(int i = 0; i < NotchFilterSingaldata.length; i++){
					  out.write(String.valueOf(NotchFilterSingaldata[i]));
					  out.newLine();
					  
				  }
				  
			  
			  //Close the output stream
			  out.close();
			  }catch (Exception e){//Catch exception if any
			  System.err.println("Error: " + e.getMessage());
			  }
		
		goBacktoMenu();
	}
	
	/**
	 * build a graph from the sin(26PIX - c), original and phase shifted FFT data and PSDs
	 */
	public void sine26PI(){
		
		System.out.println("number of x samples ? ");
		
		int x = sc.nextInt();
		
		double xSamples[]= new double[x];
		
		//generate x samples
		
		for(int i = 0; i < x; i++){
			xSamples[i] = ((double)i+1)/(double)x;
		}
		
		System.out.println("phase shift ? ");
		
		double c = sc.nextDouble();
		
		Complex[] hX = new Complex[x];
		
		Complex[] hXPhase = new Complex[x];
		
		for(int i = 0; i< x; i++){
			
			hX[i] = new Complex(Math.sin(26*Math.PI*xSamples[i]),0);
			
			hXPhase[i] =new Complex(Math.sin(26*Math.PI*(xSamples[i]-c)),0);
			
		}
		
		Complex hXcomplex[] = FFTCoolyTukey(hX,1);
		
		Complex hXPhasecomplex[] = FFTCoolyTukey(hXPhase,1);
		
		//get data for the FFT results from h(x) and h(x) phase
		
		try{
			  // Create file 
			  FileWriter fstream = new FileWriter("h(x)FFT " + x + "x's "  + " .txt");
			  BufferedWriter out = new BufferedWriter(fstream);
			  for(int i = 0; i < x; i++){
				 
					  out.write(String.valueOf(hXcomplex[i]));
					  out.newLine();
					  
				  }
				  
			  
			  //Close the output stream
			  out.close();
			  }catch (Exception e){//Catch exception if any
			  System.err.println("Error: " + e.getMessage());
			  }
		
		try{
			  // Create file 
			  FileWriter fstream = new FileWriter("h(x)phaseFFT " + x + "x's " + " .txt");
			  BufferedWriter out = new BufferedWriter(fstream);
			  for(int i = 0; i < x; i++){
				 
					  out.write(String.valueOf(hXPhasecomplex[i]));
					  out.newLine();
					  
				  }
				  
			  
			  //Close the output stream
			  out.close();
			  }catch (Exception e){//Catch exception if any
			  System.err.println("Error: " + e.getMessage());
			  }
		
		//get the PSD of h(x) and h(x) phase
		
		double[] hxTpoints = new double[x];
		
		double[] hxPhaseTpoints = new double[x];
		
		for(int i = 0; i < x; i++){
			hxTpoints[i] = (hXcomplex[i].times(hXcomplex[i].conjugate())).re();
			
			hxPhaseTpoints[i] = (hXPhasecomplex[i].times(hXPhasecomplex[i].conjugate())).re();
		}
		
		try{
			  // Create file 
			  FileWriter fstream = new FileWriter("h(x) PSD" + x + "x's "  + " .txt");
			  BufferedWriter out = new BufferedWriter(fstream);
			  for(int i = 0; i < x; i++){
				 
				  if(Math.abs(hxTpoints[i]) < .000000000001){
			  			hxTpoints[i]= 0;
			  		}
				  
					  out.write(String.valueOf(hxTpoints[i]));
					  out.newLine();
					  
				  }
				  
			  
			  //Close the output stream
			  out.close();
			  }catch (Exception e){//Catch exception if any
			  System.err.println("Error: " + e.getMessage());
			  }
		
		try{
			  // Create file 
			  FileWriter fstream = new FileWriter("h(x)phase PSD" + x + "x's " + " .txt");
			  BufferedWriter out = new BufferedWriter(fstream);
			  for(int i = 0; i < x; i++){
				 
				  if(Math.abs(hxPhaseTpoints[i]) < .000000000001){
					  hxPhaseTpoints[i]= 0;
			  		}
				  
					  out.write(String.valueOf(hxPhaseTpoints[i]));
					  out.newLine();
					  
				  }
				  
			  
			  //Close the output stream
			  out.close();
			  }catch (Exception e){//Catch exception if any
			  System.err.println("Error: " + e.getMessage());
			  }
		 
		
		goBacktoMenu();
		
	}
	
	/**
	 * creates the graphs form the sum  and product of the sin(2PI(2k - 1)x)/(2k -1) and  sin(2PI(2k)x)/(2k)function and their PSDs
	 */
	public void sineSumAndProductPSD(){
		
		System.out.println("number of x samples ? ");
		
		int x = sc.nextInt();
		
		double xSamples[]= new double[x];
		
		//generate x samples
		
		for(int i = 0; i < x; i++){
			xSamples[i] = ((double)i+1)/(double)x;
		}
		
		//get the amplitude, phase shift and the frequencies of the two functions.
		
		System.out.println("amplitude ? ");
		
		double a = sc.nextDouble();
		
		System.out.println("phase shift ? ");
		
		double c = sc.nextDouble();
		
		System.out.println("frequency for f1? ");
		
		double fOne = sc.nextDouble();
		
		System.out.println("frequency for f2? ");
		
		double fTwo = sc.nextDouble();
		
		Complex [] xOft = new Complex[x];
		
		Complex [] yOft = new Complex[x];
		
		//create the samples for the y coordinate of both graphs
		
		for(int i = 0; i< x; i++){
			
			double sineFOne = a*(Math.sin(2*Math.PI*fOne*(xSamples[i] - c)));
			
			double sineFTwo = a*(Math.sin(2*Math.PI*fTwo*(xSamples[i] - c)));
			
			xOft[i] = new Complex(sineFOne + sineFTwo,0);
					
			yOft[i] = new Complex(sineFOne * sineFTwo,0);
			
		}
		
		// run the FFt on the data
		
		Complex yTcomplex [] = FFTCoolyTukey(yOft,1);
		
		Complex xTcomplex [] = FFTCoolyTukey(xOft,1);
		
		//get the PSD of the data
		
		double[] xTpoints = new double[x];
		
		double[] yTpoints = new double[x];
		
		for(int i = 0; i < x; i++){
			xTpoints[i] = (xTcomplex[i].times(xTcomplex[i].conjugate())).re();
			
			yTpoints[i] = (yTcomplex[i].times(yTcomplex[i].conjugate())).re();
		}
		
		try{
			  // Create file 
			  FileWriter fstream = new FileWriter("PSD of x(t) " + x + "x's "+ " .txt");
			  BufferedWriter out = new BufferedWriter(fstream);
			  for(int i = 0; i < x; i++){
				  		if(Math.abs(xTpoints[i]) < .000000000001){
				  			xTpoints[i]= 0;
				  		}
					  out.write(String.valueOf(xTpoints[i]));
					  out.newLine();
					  
				  }
				  
			  
			  //Close the output stream
			  out.close();
			  }catch (Exception e){//Catch exception if any
			  System.err.println("Error: " + e.getMessage());
			  }
		
		try{
			  // Create file 
			  FileWriter fstream = new FileWriter("PSD of y(t) " + x + "x's " + " .txt");
			  BufferedWriter out = new BufferedWriter(fstream);
			  for(int i = 0; i < x; i++){
				  if(Math.abs(yTpoints[i]) < .00000000001){
			  			yTpoints[i]= 0;
			  		}
					  out.write(String.valueOf(yTpoints[i]));
					  out.newLine();
					  
				  }
				  
			  
			  //Close the output stream
			  out.close();
			  }catch (Exception e){//Catch exception if any
			  System.err.println("Error: " + e.getMessage());
			  }
		 
		
		goBacktoMenu();
		
	}
	
	/**
	 * finds the PSDof a signal and prints the graph data to a text file
	 * @param ma1
	 */
	public void PSD(Matrix ma1){
		
		
		
		System.out.println("Name of Data ? ");
		
		String name = sc.next();
		
		Complex [] com = new Complex[ma1.getRows()];
		
		for(int i = 0; i < com.length; i++){
			com[i] = new Complex(ma1.getData(i, 0), 0);
		}
		
		Complex z[] = FFTCoolyTukey(com,1);
		
		double[] points = new double[z.length];
		
		for(int i = 0; i < points.length; i++){
			points[i] = (z[i].times(z[i].conjugate())).re();
		}
		
		try{
			  // Create file 
			  FileWriter fstream = new FileWriter(name + " .txt");
			  BufferedWriter out = new BufferedWriter(fstream);
			  for(int i = 0; i < points.length; i++){
				 
				  if(Math.abs(points[i]) < .00000000001){
			  			points[i]= 0;
			  		}
					  out.write(String.valueOf(points[i]));
					  out.newLine();
					  
				  }
				  
			  
			  //Close the output stream
			  out.close();
			  }catch (Exception e){//Catch exception if any
			  System.err.println("Error: " + e.getMessage());
			  }
		 
		
		goBacktoMenu();
		
	}
	
	
	private Complex[] FFTCoolyTukey(Complex[] z , int d){
		
		int N = z.length;
			
		double theta = -(2.0*Math.PI*(double)d)/(double)N;
			
		int r = N/2;
			
		for(int i = 1; i < N; i = 2*i){
			Complex w = new Complex(Math.cos(i*theta), Math.sin(i*theta));
			
			
			
			for(int k = 0; k < N; k = k + (2*r)){
					
				Complex u = new Complex(1,0);
					
				for(int m = 0; m < r; m++){
						
					Complex t = z[k+m].minus(z[k+m+r]);
					
					z[k+m] = z[k+m].plus(z[k+m+r]);
					
					z[k+m+r] = t.times(u);
					
					u = w.times(u);
						
					}
				}
			r = r/2;
			
			}
		for(int i = 0; i < N; i++){
			r = i;
			int k = 0;
			for(int m = 1; m < N; m = 2*m){
				k = 2*k + (r % 2);
				
				r = r/2;
				
				
			}
			
			if(k > i){ //swap z[i] and z[k]
				Complex t = z[i];
				
				z[i] = z[k];
				
				z[k] = t;
			}
					
		}
	if( d < 0){ //FFT inverse
		
		for(int i = 0; i < N; i++){
			z[i] = z[i].times(1.0/N);
		}
	}
			
	return z;	
	
	}
	
	/**
	 * computes the graph data for fs(x) and gs(x) for a user specified s value and number of x's
	 */
	public void commonSignalsGraph(){
		
		System.out.println(" number of S terms? ");
		
		int s = sc.nextInt();
		
		System.out.println(" number of X terms? ");
		
		int x = sc.nextInt();
		
		double [] xSamples = new double[x];
		
		for(int i = 0; i < x; i++){
			xSamples[i] = ((double)i+1)/(double)x;
		}
		
		//fs(x) graph
		
		double[] fsGraph = new double[x];
		
		double[] fsNum = new double[s];
		
		for(int i = 0; i < s; i++ ){
			fsNum[i] = (2*(double)(i+1.0) - 1.0);
			
		}
		
		for(int i = 0; i < x; i++){
			
			double dataPoint = 0;
			
			for(int j = 0; j <s; j++ ){
				dataPoint += Math.sin(2.0*Math.PI*fsNum[j]*xSamples[i])/fsNum[j];
			}
			
			fsGraph[i] =  dataPoint;
			
		}
		
		//gs(x)graph
		
		double[] gsGraph = new double[x];
		
		double[] gsNum = new double[s];
		
		for(int i = 0; i < s; i++ ){
			gsNum[i] =  fsNum[i] +1;
		}
		
		for(int i = 0; i < x ; i++){
			
			 
			
			double dataPoint = 0;
			
			for(int j = 0; j < s; j++ ){
				dataPoint += Math.sin(2*Math.PI*gsNum[j]*xSamples[i])/gsNum[j];
			}
			
			gsGraph[i] =  dataPoint;
			
		}
		
		
		
		 //write the fs(x) to text file
		 try{
			  // Create file 
			  FileWriter fstream = new FileWriter("fs(x) " + s  + " for " + x + " x's " + ".txt");
			  BufferedWriter out = new BufferedWriter(fstream);
			  for(int i = 0; i < (int)x; i++){
				 
					  out.write(String.valueOf(fsGraph[i]));
					  out.newLine();
					  
				  }
				  
			  
			  //Close the output stream
			  out.close();
			  }catch (Exception e){//Catch exception if any
			  System.err.println("Error: " + e.getMessage());
			  }
		 
		 //write the gs(x) to text file
		 try{
			  // Create file 
			  FileWriter fstream = new FileWriter("gs(x) " + s  + " for " + x + " x's " + ".txt");
			  BufferedWriter out = new BufferedWriter(fstream);
			  for(int i = 0; i < (int)x; i++){
				 
					  out.write(String.valueOf(gsGraph[i]));
					  out.newLine();
					  
				  }
				  
			  
			  //Close the output stream
			  out.close();
			  }catch (Exception e){//Catch exception if any
			  System.err.println("Error: " + e.getMessage());
			  }
		
		
		 
		 //write the x samples to separate file
		 try{
			  // Create file 
			  FileWriter fstream = new FileWriter("the " + (int)x + " x values " + ".txt");
			  BufferedWriter out = new BufferedWriter(fstream);
			  for(int i = 0; i < x; i++){
				 
					  out.write(String.valueOf(xSamples[i]));
					  out.newLine();
					  
				  }
				  
			  
			  //Close the output stream
			  out.close();
			  }catch (Exception e){//Catch exception if any
			  System.err.println("Error: " + e.getMessage());
			  }
		 
		
		goBacktoMenu();
		
	}
	
	/**
	 * method that fits a curve to input data, the produces the polynomial and finds all its roots
	 */
	public void fitCurveFindRoot(){
		
		Matrix polynomial = curveFitting(input);
			
		System.out.print("The Polynomial of the coordinates is: ");
		
		for(int i = 0; i < input.size();i++){
			System.out.print(df.format(polynomial.getData(i, 0)) + " ");
		}
		
		
		
		System.out.println("");
		System.out.println("");
		System.out.println("");
		
		//now compute the root estimate
		int roots = 0;
		
		
		double root = -10;
		while(roots < 3){
			
			double xOne = root;
		
			double xZero = 10;
		
		int k = 1;
		double fZero = computePolynomial(polynomial,xZero);
		double fOne = computePolynomial(polynomial,xOne);
		
		//using the secant method
		do{
			double xTwo = xOne - fOne*(xOne - xZero)/(fOne - fZero);
			
			xZero = xOne;
			
			fZero = fOne;
			
			xOne = xTwo;
			
			fOne = computePolynomial(polynomial,xTwo);
			
			k++;
			
			 
			
		}while(k < 1000 && Math.abs(fZero)> .0000001);
		
		System.out.println("root "+ (roots+1) +" " + df.format(xZero)  + " iterations: " + k );
		
		root = xZero + 1;
		
		roots++;
		
		}
		
		goBacktoMenu();
	}
	
	private double computePolynomial(Matrix polynomial, double value){
		double answer = 0;
		
		int length =  polynomial.getRows();
		
		for(int i = 0; i < length;i++){
			answer += Math.pow(value, length - i-1)*polynomial.getData(i, 0);
		}
		
		return answer;
	}
	
	private Matrix curveFitting(ArrayList<Matrix> data){
		
		double[][] Ydata = new double[data.size()][1];
		
		for(int i = 0; i < data.size();i++){
			Ydata[i][0] = data.get(i).getData(1, 0);
		}
		
		double[][] Xdata = new double[data.size()][data.size()];
		
		for(int i = 0; i < data.size();i++){
			for(int j = 0; j < data.size();j++){
				
				Xdata[i][j] = Math.pow( data.get(i).getData(0, 0),data.size() - (j+1));
				
			}
		}
		
		Matrix Y = new Matrix(data.size(),1,Ydata,"Y");
		
		Matrix X = new Matrix(data.size(),data.size(),Xdata,"X");
		
		System.out.println("the the augmented coefficient matrix: ");
		
		printMatrix(X);
		System.out.println("");
		System.out.println("");
		System.out.println("");
		
		Matrix polynomial = (X.inverseMatrix()).muiltiply(Y);
	
		
		return polynomial;
	}
	
	public void tsp(){
		
		Scanner s = new Scanner(System.in);
		
		System.out.println("Exhaustive search (e), Random search (r), Genetic algorithm (g)");
		
		String choice = s.next();
		
		double[][] distance = new double[matrices.size()][matrices.size()];
		
		for(int i = 0; i < matrices.size(); i++)
			for(int j = 0; j < distance[i].length; j++){
				distance[i][j] = matrices.get(i).distance(matrices.get(j));
			}
		
		
		if(choice.equals("e")){
			
			 double start = System.currentTimeMillis();
			
			PermutationTester pT = new PermutationTester( distance, matrices.size());  //run the path generation and analysis
			
			 double end = System.currentTimeMillis();
			 
			 //print data
			System.out.println(" mean of distance: " + pT.meanData() + " standard devation is: " + pT.standardDevation() + " min distance " + df.format(pT.minDis()) + " max distance: " + df.format(pT.maxDis()) );
			 
			if(pT.count() == pT.binTest()){  //check to make sure all path lengths were recorded in the bin data
				System.out.println("all bins ok");
			}else{
				System.out.println("bin error");
			}
			
			int[] maxP = pT.maxPath();
					
			int[] minP = pT.minPath();
			
			System.out.print("longest route:  ");  //print shortest and longest route
			
			for(int i = 0; i < matrices.size(); i++){
				System.out.print(maxP[i] + " ");
			}
			
			System.out.print("\nshortest route: ");
			
			for(int i = 0; i < matrices.size(); i++){
				System.out.print(minP[i]+ " ");
			}
			System.out.print("\n");
			System.out.print("\n");
			
			
			 System.out.println("time took was " + (end - start)/1000 + " seconds" );  //print out elapsed time
			 
			 double[][] b = pT.Bin();
			 
			 printToFile(b,"e");  //print bin data to text file
				  
			
			 
			
			goBacktoMenu();
			
		}else if(choice.equals("r")){
			
			double[][] bin = new double[100][2]; //set up data bins for histogram
			
			double binIndex = 0;
			
			for(int i = 0; i < 100; i++){
				bin[i][0] = binIndex;
				binIndex = binIndex+.2;
			}
			
			double min = 0;
			double max = 0;
			  
			int[] minP = new int[matrices.size()];
		    int[] maxP = new int[matrices.size()];
			  
			double sum = 0;
			double sumSquare = 0;
			  
			double mean = 0;
			double standardD = 0;
			
			Random rG = new Random();
			
			int number = 0;
			
			 double start = System.currentTimeMillis();
			 int paths = 0;
			
			while(paths < 1000000){ //generate 1000000 paths and analyze them as they are processed
				
				int[] path = new int[matrices.size()];  
				
				int[] pool = new int[matrices.size()]; //generate a list of possible cities to select from
			
				int size = matrices.size();
				
				for(int j = 0; j < matrices.size(); j++){ // fill city list
					pool[j] = j;
				}
				
				for(int i = 0; i < matrices.size(); i++){	//add a new city randomly, and remove that city from the list
					
					int index = rG.nextInt(size--);
					int node = pool[index];
					pool[index] = pool[size];
					
					path[i] = node;
					
				}
				
				number++;  // new path has been made
				
				double currentDis = 0;
				
				if(number == 1){
				  	for (int i=0; i < matrices.size(); i++){ //get max and min paths
					  
					  	minP[i] = path[i];
					  	maxP[i] = path[i];
				  	}
					  
					for (int i=0; i < matrices.size()-1; i++){
						currentDis += distance[path[i]][path[i+1]];  //gt max and min distance
						max = currentDis;
						min = currentDis;	
					}
					currentDis += distance[path[path.length-1]][path[0]];
					
					
			  	}else{ //check each path for being the max or min path
			  		 
			  		for (int i=0; i< matrices.size()-1; i++){
			  			currentDis += distance[path[i]][path[i+1]];
			  		}
			  		
			  		currentDis += distance[path[path.length-1]][path[0]];
			  		
			  		
			  		
			  		if(min > currentDis){
			  			min = currentDis;
			  			for (int i=0; i <  matrices.size(); i++){
			  				minP[i] = path[i];
			  			}
			  		}
			  		
			  		if(max < currentDis){
			  			max = currentDis;
			  		for (int i = 0 ; i <  matrices.size(); i++){
			  				maxP[i] = path[i];
			  				
			  			}
			  		
			  		}	  	
			  	}	
			  	
			  	sum+= currentDis;  //get the sum and sum or squares then use them to find the running mean and standard deviation 
			  	sumSquare+=(currentDis*currentDis);
			  	
			  	mean = (sum)/number;
			  	
			  	standardD = Math.sqrt((sumSquare - number*mean*mean)/(number-1));
			  	
			  	bin[(int)(currentDis/.2)][1] =  bin[(int)(currentDis/.2)][1] + 1;
			  	paths++;
			}
			
			 double end = System.currentTimeMillis();
			 
			 //when done, print out data
			
			System.out.println(" mean of distance: " + mean + " standard devation is: " + standardD + " min distance " + df.format(min) + " max distance: " + df.format(max) );
			System.out.print("longest route:  ");
			
			for(int i = 0; i < matrices.size(); i++){
				System.out.print(maxP[i] + " ");
			}
			
			System.out.print("\nshortest route: ");
			
			for(int i = 0; i < matrices.size(); i++){
				System.out.print(minP[i]+ " ");
			}
			System.out.print("\n");
			System.out.print("\n");

			 System.out.println("time took was " + (end - start)/1000 + " seconds" );
			 
			 printToFile(bin, "r"); //write bin data to a text file
			
			goBacktoMenu();
			
		}else if(choice.equals("g")){

			int totalP = 1000; //set size of population 
			
			int mutationCounter = 0; //count the number of mutations that occur
			
			double min = 0; //lengths for max and min paths
			double max = 0;
			  
			int[] minP = new int[matrices.size()]; //storage for max and min paths
		    int[] maxP = new int[matrices.size()];
			  
			double sum = 0; //varaible for sum and sum of squares
			double sumSquare = 0;
			  
			double mean = 0;
			double standardD = 0;
			
			Random rG = new Random(); 
			
			int number = 0;
			
			double[][] bin = new double[100][2]; // create the bins
			
			double binIndex = 0;
			
			for(int i = 0; i < 100; i++){  //the first column is the bin labels
				bin[i][0] = binIndex;
				binIndex = binIndex+.2;
			}
			
			
			double[][] pop1 = new double[totalP][matrices.size()];  //two populations to be used in the algorithm  
			
			double[] lengths1 = new double[totalP]; //store the path lengths of pop1
			
			
			
			 double start = System.currentTimeMillis();
				
			while(number < totalP){ //generate a random population of size 1000 to start with
					
				int[] path = new int[matrices.size()];
					
				int[] pool = new int[matrices.size()];
				
				int size = matrices.size();
					
				for(int j = 0; j < matrices.size(); j++){  //generate pool of numbers
						pool[j] = j;
				}
					
				for(int i = 0; i < matrices.size(); i++){ //assign the path unique random numbers	
						
					int index = rG.nextInt(size--);
					int node = pool[index];
					pool[index] = pool[size];
						
					path[i] = node;
						
				}
				
				for(int i = 0; i < matrices.size(); i++){ //put the path into the population
				  	pop1[number][i] = path[i];
				  }
				
				number++;
				double currentDis = 0; //get the min of the random population
				if(number == 1){
				  	for (int i=0; i<matrices.size(); i++){
				  		minP[i] = path[i];
				  	}
					  
					for (int i=1; i<matrices.size()-1; i++){
						currentDis += distance[path[i]][path[i+1]];
						min = currentDis;	
					}
					currentDis += distance[path[path.length-1]][path[0]];
					
					
			  	}else
				
				
						  
					for (int i=0; i < matrices.size()-1; i++){
						currentDis += distance[path[i]][path[i+1]];
					}
					currentDis += distance[path[path.length-1]][path[0]]; //calculate the path length for the path
					
					lengths1[number -1] = currentDis; //add the path length to the list
			}
			
			double[][] newGen = new double[totalP][matrices.size()+1]; //keep track of current generation and the next generation
			
			double[][] currentGen = new double[totalP][matrices.size()+1];
			
			
			//consolidate the paths and their lengths
			
			for(int i = 0; i < currentGen.length; i++){
				for(int j  =0; j < currentGen[i].length-1; j++){
					currentGen[i][j] = pop1[i][j];
				}
				
				currentGen[i][currentGen[i].length-1] = lengths1[i];
			}
			
			
			
			
			int iterations = 0;
			do{
			
				//now run the genetic algorithm on pop1 until either a path length of 3.6 is found,
				//or a the standard deviation of the population has gone past .0001, or 
				//the number of iterations has reached 20
			
				
			
			int nextGen = 0;
			
			double mutationRate =0.05; // what percent of the time should a mutation in the population occur
			
			//construct the next generation by using the tournament method, where four potential parents are randomly chosen 
			//and the two groups of two compete for better fitness and the two winners become the two parents
			
			while( nextGen < totalP){
				
				
				int[] cChoice = new int[4]; //generate a selection of four potential parents
				int[] pool = new int[totalP];
				
				int size = totalP;
					
				for(int j = 0; j < totalP; j++){  //generate pool of numbers
						pool[j] = j;
				}
					
				for(int i = 0; i < 4; i++){ //assign the path unique random numbers	
						
					int index = rG.nextInt(size--);
					int node = pool[index];
					pool[index] = pool[size];
					cChoice[i] = node;
						
				}
				
				double[][] candidates = new double[4][matrices.size()+1]; //complete list of candidate parents
				
				for(int i = 0; i < matrices.size()+1; i++){
					candidates[0][i] = currentGen[cChoice[0]][i]; 
					candidates[1][i] = currentGen[cChoice[1]][i];
					candidates[2][i] = currentGen[cChoice[2]][i];
					candidates[3][i] = 	currentGen[cChoice[3]][i];
				}
				
				double[][] parents = new double[2][matrices.size()+1];
				
				if(candidates[0][matrices.size()] < candidates[1][matrices.size()]){
					for(int i = 0; i < matrices.size()+1; i++){
						parents[0][i] = candidates[0][i];
					}
				}else{
					for(int i = 0; i < matrices.size()+1; i++){
						parents[0][i] = candidates[1][i];
					}
				}
				
				
				if(candidates[2][matrices.size()] < candidates[3][matrices.size()]){
					for(int i = 0; i < matrices.size()+1; i++){
						parents[1][i] = candidates[2][i];
					}
				}else{
					for(int i = 0; i < matrices.size()+1; i++){
						parents[1][i] = candidates[3][i];
					}
				}
				
				//create the two children of the parents  
				
				double[][] children = generateChildren( parents);
				
				for (int i=0; i < matrices.size()-1; i++){
					children[0][matrices.size()] += distance[(int)children[0][i]][(int)children[0][i+1]];;
					children[1][matrices.size()] += distance[(int)children[1][i]][(int)children[1][i+1]];;
				}
				
				double mutateC1 = rG.nextDouble(); //generate random number to see if child 1 will mutate 
				
				double mutateC2 = rG.nextDouble(); //generate random number to see if child 2 will mutate 
				 
				if(mutateC1 < mutationRate){  //if number is less than mutation rate, mutate child 1
					mutationCounter++;
					int rIndex1 = rG.nextInt(matrices.size());
					int rIndex2 = rG.nextInt(matrices.size());
					while(rIndex1 == rIndex2){
						rIndex2 = rG.nextInt(matrices.size());
					}
					
					double temp = children[0][rIndex1];
					
					children[0][rIndex1] = children[0][rIndex2];
					children[0][rIndex2] = temp;
					
					
				}
				
				if(mutateC2 < mutationRate){					 //if number is less than mutation rate, mutate child 2
					mutationCounter++;
					int rIndex1 = rG.nextInt(matrices.size());
					int rIndex2 = rG.nextInt(matrices.size());
					while(rIndex1 == rIndex2){
						rIndex2 = rG.nextInt(matrices.size());
					}
					
					double temp = children[1][rIndex1];
					
					children[1][rIndex1] = children[1][rIndex2];
					children[1][rIndex2] = temp;
				}
				
				//place the children and the parents in one array, determine the two most fit, then place them in the next generation
				double[][] fit = new double[4][children[0].length];
				
				for(int i = 0; i < parents[0].length; i++){
					fit[0][i] = children[0][i];
				    fit[1][i] = children[1][i];
		     		fit[2][i] = parents[0][i];
				    fit[3][i] = parents[1][i];
				}
				
				//sort the array based on shortest distance
				
				Arrays.sort(fit, new Comparator<double[]>() {
			        @Override
			        public int compare(double[] double1, double[] double2)
			        {
			        	Double number1 = double1[double1.length-1];
			        	Double number2 = double2[double1.length-1];
			            return number1.compareTo(number2);
			        }
			    });
				
				for(int i = 0; i < newGen[nextGen].length; i++){
					newGen[nextGen][i] = fit[0][i];
					newGen[nextGen+1][i] = fit[1][i];
				}
				
						nextGen = nextGen +2; //Because two paths are added to the next generation, increment by 2
						
			}
			
			//now run an analysis on the new generation, find the min path, max path, the mean and the standard deviation of the new population
			
			int count = 0;	
				for(int i =0;i< newGen.length;i++){ //go through population
				
				double currentDis=0;  //get distances 
				for(int j  =0; j < matrices.size()-1; j++){
					currentDis += distance[(int)newGen[i][j]][(int)newGen[i][j+1]];
				}
				
				currentDis = currentDis + distance[(int)newGen[i][0]][(int)newGen[i][matrices.size()-1]]; //distance from last city back to first
				 count += 1;
				
				 newGen[i][matrices.size()] =  currentDis;
				 
				sum += currentDis;
			  	sumSquare+=(currentDis*currentDis);
			  	
			  	if(min > currentDis){  //find min path
		  			min = currentDis;
		  			for (int k=0; k <  matrices.size(); k++){
		  				minP[k] = (int) newGen[i][k];
		  			}
		  		}
		  		
		  		
			  	
			  	mean = (sum)/count;
			  	standardD = Math.sqrt((sumSquare - count*mean*mean)/(count-1));
			  	
			  	
			  	
		}
				
				
			
			for(int i =0;i< newGen.length;i++)		  //set the new generation as the current generation	
				for(int j =0;j< newGen[i].length;j++){
					currentGen[i][j] = newGen[i][j];
				}
						
						
					sum = 0;
					sumSquare=0;
					
						iterations++;

			
			}while(min > 3.6  && standardD> .336 && iterations < 20);
				
			
			 double end = System.currentTimeMillis();
			 
			 max = newGen[0][matrices.size()];
			
			for(int i =0;i< newGen.length;i++){ //find the max path in the final population and fill the bins
				
					bin[(int)(newGen[i][matrices.size()]/.2)][1] =  bin[(int)(newGen[i][matrices.size()]/.2)][1] + 1;
					
					if(newGen[i][matrices.size()] > max){
						max = newGen[i][matrices.size()];
						
						for(int j = 0;j < matrices.size();j++){
							maxP[j] = (int)newGen[i][j];
						}
					}
					
				}
			
			printToFile(bin, "g"); //write bin data to text file
			
			
			//print data
			System.out.println(" mean of distance: " + mean + " standard devation is: " + standardD + " min distance " + df.format(min) + " max distance: " + df.format(max) );
			
			System.out.print("longest route:  ");
			
			for(int i = 0; i < matrices.size(); i++){
				System.out.print(maxP[i] + " ");
			}
			
			System.out.print("\nshortest route: ");
			
			for(int i = 0; i < matrices.size(); i++){
				System.out.print(minP[i]+ " ");
			}
			System.out.print("\n");
			System.out.print("\n");
			
			
			System.out.println("time took was " + (end - start)/1000 + " seconds" );
			
			System.out.print("\n");
			System.out.print(" number of mutations " + mutationCounter);
			System.out.print("\n");
		
			goBacktoMenu();

			
			
		}else
			
			goBacktoMenu(); // go back to main menu
		
	}
	
	//this method generates children by randomly selecting a start and end index to collect cites from parent 1
	//then fill in the rest of the cities in the order that they occur in parent 2 (used in genetic algorithm)
	private double[][] generateChildren( double[][] parents){
		double[][] children = new double[2][matrices.size()+1];
		for(int index = 0; index < parents.length; index++){
		
	    // Get start and end sub tour positions for parent1's path
	        int startPos = (int) (Math.random() * parents[0].length-1);
	        int endPos = (int) (Math.random() * parents[0].length-1);

	    // Loop and add the sub tour from parent1 child1
	        for (int i = 0; i < children[index].length-1; i++) {
	            // If  start position is less than the end position
	            if (startPos < endPos && i > startPos && i < endPos) {
	            	children[index][i] = parents[0][i];
	            } // If start position is larger
	            else if (startPos > endPos) {
	                if (!(i < startPos && i > endPos)) {
	                	children[index][i] = parents[0][i];
	                }
	            }
	        }

	        
	        for (int i = 0; i < children[index].length-1; i++) {
	            // If child2 doesn't have the city add it
	            if (isInside(children[index],parents[1][i]) == false) {
	                // Loop to find a spare position in the child's tour
	                for (int j = 0; j < children[0].length-1; j++) {
	                    // is an city spot is empty, add city
	                    if (children[index][j] == 0) {
	                    	children[index][j] = parents[1][i];
	                       break;
	                    }
	                }
	            }
	        }
		}
	        return children;
	}
	
	//checks if a city is currently inside the path (used when generating children)
	private boolean isInside(double[] dM, double d){
		
		boolean inInside = false;
		
		for(int i = 0; i < dM.length; i++){
			if(d == dM[i]){
				inInside = true;
			}
		}
		
		return inInside;
		
	}
	
	/**
	 * prints a 2d array (data bins from the traveling salesman problem) to a text file so is can easily be copied into excel
	 * @param b the array of bin data
	 * @param c an identifier for the bin(is the data for an exhaustive, random or genetic search)
	 */
	public void printToFile(double[][] b , String c){
		
		 DecimalFormat d = new DecimalFormat("0.0");
		 
		 try{
			  // Create file 
			  FileWriter fstream = new FileWriter("bins" + c +".txt");
			  BufferedWriter out = new BufferedWriter(fstream);
			  for(int i = 0; i < 100; i++)
				  for(int j = 0; j < 2; j++){
					  out.write(d.format(b[i][j]) + "	");
					  if(j == 1){
						  out.newLine();
					  }
				  }
				  
			  
			  //Close the output stream
			  out.close();
			  }catch (Exception e){//Catch exception if any
			  System.err.println("Error: " + e.getMessage());
			  }
		
	}
	
	/**
	 * method runs the power method on a selected matrix and returns its estimated eigen values
	 */
	public void power(){
		Matrix ma1 = getFirstMatrix();
	
		System.out.print("the approximation for the eigen values are: " );
		
		double p[] = ma1.powerMethod();
		
		for(int i = 0; i < ma1.getRows(); i++){ //display all eigen values
		
			System.out.print(df.format(p[i])+ " ");
		}
		
		goBacktoMenu(); // go back to main menu
	}
	
	public void Leverrier(){
		Matrix ma1 = getFirstMatrix();
		
		double[] cP = ma1.LeverriersAlgorithm();
		
		System.out.print(" characteristic ploynomial: x^" + cP.length + " ");
		
		for(int i = 0; i < ma1.getColumns(); i++){
			
			System.out.print(cP[i]+ "x^" + (cP.length - i-1) + " ");
		}
		
		goBacktoMenu(); // go back to main menu
	}
	
	public void eigen(){
		
		Matrix ma1 = getFirstMatrix();
		
		System.out.println("the eigenvalues of " + ma1.getName() + " are:" + df.format(ma1.getEigenValues()[0]) + " and " + df.format(ma1.getEigenValues()[1]) +"\n");
		
		for(int i =0; i<ma1.getEigenValues().length;i++){
			System.out.println("the unit length eigen vector at " +  df.format(ma1.getEigenValues()[i]) + " is " );
			printMatrix(ma1.getEigenVectors(ma1.getEigenValues())[i]);
			System.out.println("");
		}
		
		goBacktoMenu(); // go back to main menu
	}
	
	/**
	 * asks the user for a matrix to calculate the trace of, then displays it.
	 */
	public void matrixTrace(){
		Matrix ma1 = getFirstMatrix();
		
		System.out.println("the trace of" + ma1.getName() +  " is: "+ df.format(ma1.trace()));
		
		goBacktoMenu(); // go back to main menu
		
	}
	
	/**
	 * asks user to matrices, so that their determinates can be calculated, then multiplied together
	 */
	public void multiplyDeterminants(){
		Matrix ma1 = getFirstMatrix();
		Matrix ma2 = getSecondMatrix();
		
		System.out.println("the product of the determinates of " + ma1.getName() + " and " + ma2.getName() + " is: "+ df.format(ma1.determinant() * ma2.determinant()));
		
		goBacktoMenu(); // go back to main menu
	}
	
	/**
	 * displays all covariance matrices
	 */
	public void viewCovariance(){
		for(Matrix c: covarianceMatrices){
			System.out.println("");
			System.out.println("covariance for " + c.getClassName() + ":");
			printMatrix(c);
			System.out.println("");
			System.out.println("\ninverse covariance for " + c.getClassName() + ":");
			printMatrix(c.inverseMatrix());
			System.out.println("");
		}
		
		goBacktoMenu();
	}
	
	/**
	 * generates the condition number of a selected matrix
	 */
	public void conditionNumber(){
		Matrix ma1 = getFirstMatrix(); 
		
		System.out.println("the condition number of " + ma1.getName() + " is: " +  df.format(ma1.norm()*(ma1.inverseMatrix()).norm())); //compute condition number
	}
	
	/**
	 * generate a list of coordinate vectors that were misclassified by the discriminate functions
	 * also generate the the number of correctly classified vectors and how many vectors were classified 
	 * in 	 proper class 
	 */
	public void misclassifiedDiscriminantFunction(){
			
		int correctCounter = 0;
		
		int class1counter = 0;
		
		int class2counter = 0;
		
		
		//print out all the data of the misclassified vectors
		for(int i = 0; i < matrices.size(); i++){
			if(!((matrices.get(i)).getClassName().equals((matrices.get(i).getDiscriminateClassName())))){
				System.out.print((matrices.get(i)).getName() + " " );
				 printMatrix(discriminateValues.get(i).transpose());
				 System.out.print((matrices.get(i)).getDiscriminateClassName() + " " + (matrices.get(i)).getClassName());
				 System.out.print("\n");
			}else{
				correctCounter++;
				
				if((matrices.get(i).getClassName()).equals("class 1")){
					class1counter++;
				}else
					class2counter++;
				
			}
		}
		
		// now display the information on the correctly classified vectors and classes
		
		System.out.println("");
		
		System.out.println("the number of correctly classified vectors (including the mean vectors): " + correctCounter);
		
		System.out.println("");
		
		System.out.println("the number of correctly classified vectors in class 1(including the mean vectors): " + class1counter);
		
		System.out.println("");
		
		System.out.println("the number of correctly classified vectors in class 2(including the mean vectors): " + class2counter);
		
		goBacktoMenu();
			
	}
	
	/**
	 * Retrieves the discriminate function values for a selected matrix and prints them along with its class 
	 * that is determined from the discriminate function value comparison
	 */
	public void vertexDiscriminate(){
		Matrix ma1 = getFirstMatrix();
		
		for(int i = 0; i < matrices.size(); i++){
			if(matrices.get(i).getName().equals(ma1.getName())){
				System.out.print((matrices.get(i)).getName() + " " );
				 printMatrix(discriminateValues.get(i).transpose());
				 System.out.print((matrices.get(i)).getDiscriminateClassName() + " " );
				 System.out.print("\n");
				 
			}
		}
		
		goBacktoMenu(); // go back to main menu
	}
	
	/**
	 * perform a discriminate function on a matrix,  its class mean and class covariance matrix and return the result
	 * @param x the matrix 
	 * @param i the index value for the mean and covariance arraylists
	 * @return
	 */
	public double discriminateFunction(Matrix x, int i){
		return -.5*(((((x.transpose())).subtract((meanMatrices.get(i)).transpose())).muiltiply((covarianceMatrices.get(i)).inverseMatrix())).muiltiply(x.subtract((meanMatrices.get(i))))).getData(0,0)  - .5*Math.log(determinants.get(i));
	}
	
	/**
	 * method creates all the discriminate function values for any vertex passed in and sets the class 
	 * who's function result is closest to zero as the class the vertex should belong in
	 * @param x
	 */
	public void descriminate( Matrix x){

		double max = -Double.MAX_VALUE; //create largest negative number to compare function values
		
		double descriminateValues[][] = new double[2][1]; //create a place for all the discriminate function values that a vertex has
		
		for(int i = 0; i < covarianceMatrices.size(); i++){
			//fill matrix with all discriminate function values
			descriminateValues[i][0] = discriminateFunction(x, i); 
					
					
			//find the value closest to zero
			if(max <= discriminateFunction(x, i)){
				max = discriminateFunction(x, i);
				x.setDiscriminateClassName((meanMatrices.get(i).getClassName())); //the function value closest to zero is assumed to be from the correct class
				}
		}
		
		Matrix dValues = new Matrix(2,1,descriminateValues, "descriminate values for " + x.getName()); //add the matrix of discriminate function values to its own arraylist
		
		discriminateValues.add(dValues);
		
	}
	
	/**
	 * generates the boundary contour line for the classes and prints the correct coordinates
	 */
	public void boundaryContour(){
		
		//get the maximum x and y values (magnitude) to help set the size of the graph
		
		double maxX=0;
		
		for(Matrix m: matrices){
			if(maxX < Math.abs(m.getData(0, 0)))
				maxX =m.getData(0, 0);
				
		}
		
		
		double maxY=0;
		for(Matrix m: matrices){
			if(maxY < Math.abs(m.getData(1, 0)))
				maxY = m.getData(1, 0);
				
		}
		
		//set the boundaries of the graph region
		
		double x = Math.round(maxX) + 1; 
		double y = Math.round(maxY) + 1;
		
		//number to determine what coordinates are near the boundary
		double E = 0.1;
		
		for(double i = (-1*x); i < x; i +=.1) //split graph into a grid of 0.1 x 0.1 squares
			for(double j = (-1*y); j < y; j +=.1){
				double m[][] = new double[2][1];
				m[0][0] = i;  //put the coordinates in the matrix
				m[1][0] = j;
				Matrix cB = new Matrix(2,1,m,"boundaryContour");
				
				
				
				double result = 0;
				
				for(int k = 0; k < covarianceMatrices.size(); k++){
					//subtract g1 from g2 by first setting result to be g1, then subtracting it from g2
					result = Math.abs(result - Math.abs(discriminateFunction(cB, k)));
					
				}
				
				//if result is below the test number, then it is near or on the boundary
				if(E >= result){
					printMatrix(cB.transpose());
					for(int k = 0; k < meanMatrices.size(); k++){
						System.out.print(" " + df.format(discriminateFunction(cB, k)));
					}
					System.out.println("");
				}
			}
		
		goBacktoMenu();
	}
	
	
	/**
	 * this method generates from a set of vector classes their mean, covariance matrix, determinants, 
	 * for each class 
	 * @param numOfClasses
	 */
	public void meanVectorCovarianceMatrixDeterminants(int numOfClasses){
		
		int currentClass = 1; //the class that is being operated on - 1
		double scalar = 0; //the total number of matrices under a certain class
		double meanData[][] = new double[2][1];
	
		String className = "class 1"; //start with class 1
		String meanName = "m"+(currentClass); //name of mean of the current class
		
		Matrix mean = new Matrix(2,1,meanData,meanName,className);
		
		while(currentClass<=numOfClasses){
	
		for(Matrix m: matrices){
			if(m.getClassName().equals(mean.getClassName())){
				mean = mean.add(m); //add all matrices with the same class
				scalar++; //every time a new match is found increase the total
				
			}
		}
		
		mean.ScalarMultiplication((1/scalar)); //divide the matrix data by the total amount of class matrices
		
		//the additions cause a name change so this command resets it to its proper name
		mean.setName(meanName);
		
		meanMatrices.add(mean); //add the mean to the general collection of means
		
		
		
		
		String covarianceName = "E"+(currentClass);
		double covarianceSummation[][] = new double[2][2];
		Matrix covariance = new Matrix(2,2,covarianceSummation,covarianceName,className); //build a covariance matrix
		
		for(Matrix m: matrices){
			if(m.getClassName().equals(mean.getClassName()))
				covariance = covariance.add((m.subtract(mean).muiltiply(m.subtract(mean).transpose())));	//create the covariance matrix data 
			
		}		
		
		covariance.ScalarMultiplication(1/scalar);//then divide by total
		
		covariance.setName(covarianceName);
		
		//add covariance matrix
		removeNegativeZeros(covariance);
		
		covarianceMatrices.add(covariance);
		
		
		//get determinants
		determinants.add(covariance.determinant());
		
		
		currentClass++;
		
		//go into the next class
		
		className = "class " +(currentClass);
		meanName = "m"+(currentClass);
		meanData = new double[2][1];
		covarianceName = "E"+(currentClass);
		covarianceSummation = new double[2][2];
		mean = new Matrix(2,1,meanData,meanName,className);
		covariance = new Matrix(2,1,covarianceSummation,covarianceName,className);
		scalar = 0;
		}
	}
	/**
	 * get the determinate of a specified matrix
	 */
	public void viewDeterminant(){
		
		Matrix ma1 = getFirstMatrix();
		
		System.out.println("the determinate of " + ma1.getName() + " is: " +  ma1.determinant()) ;
		
		goBacktoMenu();
	}
	
	/**
	 * creates an inverse of a selected matrix, displays it, and adds it to the general collection of matrices for further use
	 */
	public void createInverse() {
		Matrix ma1 = getFirstMatrix(); //enter the name of the matrix
		Matrix solution = ma1.inverseMatrix(); //Perform operation
		
		removeNegativeZeros(solution); //negative numbers less than .001 create negative zeros and are fixed here
		
		printMatrix(solution); //display resulting matrix
		matrices.add(solution); //add to collection
		goBacktoMenu();
	}
	
	/**
	 * creates a matrix from the gauss-jordan operation done on a selected matrix, displays it, and adds it to the general collection of matrices for further use
	 */
	public void Gauss_Jordan() {
		Matrix ma1 = getFirstMatrix(); //enter the name of the matrix
		Matrix solution = ma1.GaussJordan(); //Perform operation
		
		removeNegativeZeros(solution); //negative numbers less than .001 create negative zeros and are fixed here
	
		
		printMatrix(solution); //display resulting matrix
		matrices.add(solution); //add to collection
		goBacktoMenu();//go back to main menu
	}
	
	/**
	 * creates a matrix from the gauss-elimination operation done on a selected matrix, displays it, and adds it to the general collection of matrices for further use
	 */
	public void Gauss_Elimination() {
		Matrix ma1 = getFirstMatrix(); 
		Matrix solution = ma1.GaussElimination();
		removeNegativeZeros(solution);
		
		printMatrix(solution);
		matrices.add(solution);
		goBacktoMenu();
	}
	
	/**
	 * adds two matrices and places the result in the general matrix collection
	 */
	public void addMatrices() {
		
		
		Matrix ma1 = getFirstMatrix();
		Matrix ma2 = getSecondMatrix();
		Matrix solution = null;

		solution = ma1.add(ma2);

		System.out.println("");
		System.out.println("");

		removeNegativeZeros(solution);
		
		printMatrix(solution);
		matrices.add(solution);

		goBacktoMenu();

	}

	/**
	 * subtracts two matrices and places the result in the general matrix collection
	 */
	public void subtractMatrices() {
		

		Matrix ma1 = getFirstMatrix();
		Matrix ma2 = getSecondMatrix();
		Matrix solution = null;

		solution = ma1.subtract(ma2);

		System.out.println("");
		System.out.println("");

		removeNegativeZeros(solution);
		
		printMatrix(solution);
		matrices.add(solution);

		goBacktoMenu();

	}

	/**
	 * multiplies two matrices and places the result in the general matrix collection
	 */
	public void multiplyMatrices() {
		

		Matrix ma1 = getFirstMatrix();
		Matrix ma2 = getSecondMatrix();
		Matrix solution = null;

		solution = ma1.muiltiply(ma2);

		System.out.println("");
		System.out.println("");

		removeNegativeZeros(solution);
		
		printMatrix(solution);
		matrices.add(solution);

		goBacktoMenu();

	}
	
	/**
	 * multiplies a matrix and a scalar value and places the result in the general matrix collection
	 */
	public void scalarMultiplyMatrix() {
		
		Matrix ma1 = null;
		Matrix solution = null;

		System.out.println("enter the name of the matrix to be multipled: ");
		String mName = sc.nextLine();

		for (Matrix m : matrices) {
			if (mName.equals(m.getName()))
				ma1 = m;
		}

		System.out
				.println("enter the scalar number you wish to multply it by: ");
		double scalar = sc.nextDouble();
		solution = ma1.ScalarMultiplication(scalar);
		removeNegativeZeros(solution);
		printMatrix(solution);
		matrices.add(solution);

		goBacktoMenu();

	}
	
	/**
	 * method to ask the user for the first or only matrix for an operation to be performed
	 * @return the matrix that matches the user's input 
	 */
	public Matrix getFirstMatrix() {
		
		Matrix ma1 = null;

		System.out.println("enter the name of the matrix");

		String m1 = sc.nextLine();

		for (Matrix m : matrices) {
			if (m1.equals(m.getName()))
				ma1 = m; //make sure that the matrix exists

		}
		for(Matrix c:  covarianceMatrices){
			if (m1.equals(c.getName()))
				ma1 = c; //make sure that the matrix exists
		}
		
		for(Matrix me:  meanMatrices){
			if (m1.equals(me.getName()))
				ma1 = me; //make sure that the matrix exists
		}
		
		

		return ma1;
	}

	/**
	 * method to ask the user for the second matrix for an operation to be performed
	 * @return the matrix that matches the user's input 
	 */
	public Matrix getSecondMatrix() {
		
		Matrix ma2 = null;

		System.out.println("enter the name of the second matrix");

		String m2 = sc.nextLine();

		for (Matrix m : matrices) {
			if (m2.equals(m.getName()))
				ma2 = m; //make sure that the matrix exists
		}

		for(Matrix c:  covarianceMatrices){
			if (m2.equals(c.getName()))
				ma2 = c; //make sure that the matrix exists
		}
		
		for(Matrix me:  meanMatrices){
			if (m2.equals(me.getName()))
				ma2 = me; //make sure that the matrix exists
		}
		
	
		
		return ma2;
	}

	/**
	 * lists the names of all the matrices in the general matrix collection
	 */
	public void listMatrices() {
		for (Matrix m : matrices) {
			System.out.print(m.getName() + " ");
		}
		
		for( Matrix c : covarianceMatrices){
			System.out.print(c.getName() + " ");
		}
		
		for(Matrix me:  meanMatrices){
			System.out.print(me.getName() + " ");
		}
		
		
		
		// allows user to copy the names of long named matrices so you can copy paste to perform the operation instead of typing out the name
		
		goBacktoMenu();
	}

	/**
	 * allows the user to type a matrix name then displays the matrix data
	 */
	public void viewMatrixData() {
		Scanner sc = new Scanner(System.in);
		System.out.println("enter the name of the matrix you wish to view: ");
		String matrixSelect = sc.nextLine();

		for (Matrix m : matrices) {
			if (matrixSelect.equals(m.getName()))
				printMatrix(m);
		}
		
		for(Matrix c: covarianceMatrices){
			if (matrixSelect.equals(c.getName()))
				printMatrix(c);
		}
		
		for(Matrix me:  meanMatrices){
			if (matrixSelect.equals(me.getName()))
				printMatrix(me);
		}
		


		goBacktoMenu();

	}
	
	/**
	 * due to the decimal format of the program being #.### negative numbers smaller than .001 will appear as negative zeros
	 * @param m the selected matrix
	 */
	public void removeNegativeZeros(Matrix m){
		for(int i = 0; i< m.getRows(); i++)
			for(int j = 0; j < m.getColumns(); j++){
				if(m.getData(i, j) < 0 && Math.abs(m.getData(i, j)) < .001){
					m.setData(i, j, (m.getData(i, j) * -1));
				}
			}
	}
	
	/**
	 * method to return the user to the main method after an operation is preformed
	 */
	public void goBacktoMenu() {
		Scanner sc = new Scanner(System.in);
		System.out.println("");
		System.out.println("hit the enter key to get back to the menu");
		String mChoice = sc.nextLine();

		if (mChoice.equals("")) {
			menuScreen();
		} else
			menuScreen();
	}

	/**
	 * prints the matrix data in it's m x n form 
	 * @param m
	 */
	public void printMatrix(Matrix m) {
		
		for (int i = 0; i < m.getRows(); i++)
			for (int j = 0; j < m.getColumns(); j++) {
				System.out.print(df.format(m.getData(i, j)) + " ");
				if (j == m.getColumns() - 1 && i != m.getRows()-1) {
					System.out.print("\n");
				}

			}
	}

	/**
	 * this method reads in the data file to build the matrices on it
	 * there are two ways to read a data file, read as an m x n matrix or
	 * read it in as a collection of 2 x 1 coordinate vectors
	 * 
	 * if the coordinate vector is selected a few operations are performed to 
	 * generate the mean and covariance matrices and the the determinates of the 
	 * vectors and the means
	 *  
	 * @param fileName
	 * @param vectorChoice
	 */
	public void readMatrices(String fileName, String vectorChoice) {
		// TODO Auto-generated method stub
		ArrayList<Double> rawData = new ArrayList<Double>();
		ArrayList<String> matrixNames = new ArrayList<String>();

		File inFile = new File(fileName + ".txt");
		FileInputStream fis = null;
		try {
			fis = new FileInputStream(inFile);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		int lineCount = 0;
		int col = 0;
		Double matrixData;

		
		Scanner s = new Scanner(fis);

		while (!s.hasNextDouble()) {
			String name = s.next();
			matrixNames.add(name);
			col++; // figure out how many columns are in the file (for coordinate vector each column is labeled)
		}

		if (vectorChoice.equals("y")) { //read in the text file as 2 x1 coordinate vectors
			
			
			double vector[][] = new double[2][1]; //build vector
			int totalClasses = col/2; //each class has an x and y value, to the number of x's + y's divided by 2 is the number of classes
			int count = 0; //keeps the correct values in the correct vectors
			
			int classCount = 1; //start with class 1
			
			String  className = "class " + classCount; // build class name
			int vectorNameNumber = 0; // count the vectors to build their names
			String nameVector = null;
			while (s.hasNextDouble()) {
						
				if( count == 0){	//make new vector when previous is filled
					vector = new double[2][1];
				}
				 
				vector[count][0] = s.nextDouble(); //put the next value in the right spot
			
				nameVector = "v"+ vectorNameNumber;		
			
				count++; 
			
				if(count == 2){ //when the vector is filled, reset counters, increment the class number as the next values will be in a different class
					Matrix v = new Matrix(2,1,vector,nameVector,className); //build vector
					matrices.add(v); //add vector to general matrix collection
					input.add(v);
					vectorNameNumber++;
					classCount++;
					className = "class " + classCount;
					count = 0;
					
					if(classCount == totalClasses+1){ //when the last class is filled, the next set of data is to go in the first class
						classCount = 1;
						className = "class " + classCount;
					}
					
					
				}
			
				
				
			}
			
			 meanVectorCovarianceMatrixDeterminants(col/2); //create the mean and covariance matrices 
			 
			 for(Matrix m: matrices) //create the discriminate values for all the vectors and means
				 descriminate(m);
			 
			

		} else { // if not reading in data as coordinate vectors, reading them in as m x n matrix
			
			while (s.hasNextDouble()) {
				
				matrixData = s.nextDouble();
				rawData.add(matrixData);
				lineCount++; // get rough line count

			}

			int row = lineCount / col; // get true line count

			
			
				String currentMatrixName = "m"+matrices.size(); //create the name

				

				double matrixDataCollection[][] = new double[row][col];

				for (int i = 0, k = 0; i < row; i++) {  //transfer the data to a 2d array
					for (int j = 0; j < col; j++) {
						
						matrixDataCollection[i][j] = rawData.get(k++);

					}
				}

				Matrix newMatrix = new Matrix(row, col, matrixDataCollection,
						currentMatrixName);  //build matrix

				matrices.add(newMatrix); //add matrix

				

		}
		s.close();
	}

	

}
