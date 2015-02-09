
/*
 * robot_arm.c
 * Devin Simpson
 * 10/18/2013
 * CSC 370
 * the key'x' moves the sholder around the origin 360 degrees on the X axis and 'X' does so in reverse 
 * the key'y' moves the sholder around the origin 360 degrees on the Y axis and 'Y' does so in reverse 
 * the key'z' moves the sholder around the origin 360 degrees on the Z axis and 'Z' does so in reverse 
 * the key'e' moves the elbow around the lower arm in 120 degrees back and forth and 'E' does so in reverse 
 * the key'r' rotates the elbow around the lower arm in 180 degrees on the x axis back and forth and 'R' does so in reverse 
 * the key'h' moves the hand back anf forth 180 degrees 'H' does so in reverse 
 * the key'f' closes the fingers and thumb into a fist and back out again 'F' does so in reverse 
 */
#include <stdlib.h>
#include <string.h>
#include <GL/glut.h>


static int shoulderX = 55, shoulderY = 5, shoulderZ = 0, elbow = 30, elbowRotate = 0, hand =30;
static int fingers[4][3]= {{60,60,60},{60,60,60},{60,60,60},{60,60,60}};
static int thumb[2] = {60,60};
int increaseHand = 1;
int increaseElbow = 1;
int increaseFingers = 1;


void init(void) 
{
	

   GLfloat ambient[] = {0.0, 0.0, 0.0, 1.0};
   GLfloat diffuse[] = {1.0, 1.0, 1.0, 1.0};
   GLfloat specular[] = {1.0, 1.0, 1.0, 1.0};
   GLfloat position[] = {0.0, 3.0, 3.0, 0.0};

   GLfloat lmodel_ambient[] = {0.2, 0.2, 0.2, 1.0};
   GLfloat local_view[] = {0.0};

   glLightfv(GL_LIGHT0, GL_AMBIENT, ambient);
   glLightfv(GL_LIGHT0, GL_DIFFUSE, diffuse);
   glLightfv(GL_LIGHT0, GL_POSITION, position);
   glLightModelfv(GL_LIGHT_MODEL_AMBIENT, lmodel_ambient);
   glLightModelfv(GL_LIGHT_MODEL_LOCAL_VIEWER, local_view);

   glFrontFace(GL_CW);
   glEnable(GL_LIGHTING);
   glEnable(GL_LIGHT0);
   glEnable(GL_AUTO_NORMAL);
   glEnable(GL_NORMALIZE);
   glEnable(GL_DEPTH_TEST);
}

//function to create a spcified material for an ojbect 
void material(GLfloat ambr, GLfloat ambg, GLfloat ambb, GLfloat difr, GLfloat difg, GLfloat difb, GLfloat specr, GLfloat specg, GLfloat specb, GLfloat shine){
	  
   GLfloat mat[4];

   mat[0] = ambr; mat[1] = ambg; mat[2] = ambb; mat[3] = 1.0;
   glMaterialfv(GL_FRONT, GL_AMBIENT, mat);
   mat[0] = difr; mat[1] = difg; mat[2] = difb;
   glMaterialfv(GL_FRONT, GL_DIFFUSE, mat);
   mat[0] = specr; mat[1] = specg; mat[2] = specb;
   glMaterialfv(GL_FRONT, GL_SPECULAR, mat);
   glMaterialf(GL_FRONT, GL_SHININESS, shine * 128.0);

}

//generate string for legend 
void doRasterString(float x, float y, char*s)
{
	char c;
	glRasterPos2f(x,y);

	for (; (c = *s) != '\0'; s++)
		glutBitmapCharacter(GLUT_BITMAP_TIMES_ROMAN_24, c);	  
}

//function to create and rotate indivdual fingers
void fingerPiece(GLfloat tx, GLfloat ty, GLfloat tz, int angle, GLfloat spr, int numDiv, GLfloat sx, GLfloat syz){

	GLfloat t2x = sx/2;
	GLfloat t3x = t2x*-1;
	GLfloat space = 0.2;
	
	glTranslatef (tx, ty, tz);
		glRotatef ((GLfloat) angle, 0.0, 0.0, 1.0);
		material( 0.25, 0.25, 0.25, 0.4, 0.4, 0.4, 0.774597, 0.774597, 0.774597, 0.6); //sphere color
		glutSolidSphere(spr,numDiv,numDiv);
		glTranslatef (t2x, 0.0, 0.0);
    glPushMatrix();
		glScalef (sx, syz, syz);
		material( 0.25, 0.25, 0.65, 0.4, 0.4, 0.4, 0.774597, 0.774597, 0.774597, 0.6); //cube color
		glutSolidCube (1.0);// finger 1 piece
	glPopMatrix();
		glTranslatef (t3x, 0.0, space);
		material( 0.25, 0.25, 0.25, 0.4, 0.4, 0.4, 0.774597, 0.774597, 0.774597, 0.6); //sphere color
		glutSolidSphere(spr,numDiv,numDiv);
		glTranslatef (t2x, 0.0, 0.0);
    glPushMatrix();
		glScalef (sx, syz, syz);
		material( 0.25, 0.25, 0.65, 0.4, 0.4, 0.4, 0.774597, 0.774597, 0.774597, 0.6); //cube color
		glutSolidCube (1.0);// finger 2 piece
	glPopMatrix();
		glTranslatef (t3x, 0.0, space);
		material( 0.25, 0.25, 0.25, 0.4, 0.4, 0.4, 0.774597, 0.774597, 0.774597, 0.6); //sphere color
		glutSolidSphere(spr,numDiv,numDiv);
		glTranslatef (t2x, 0.0, 0.0);
    glPushMatrix();
		glScalef (sx, syz, syz);
		material( 0.25, 0.25, 0.65, 0.4, 0.4, 0.4, 0.774597, 0.774597, 0.774597, 0.6); //cube color
		glutSolidCube (1.0);// finger 3 piece
	glPopMatrix();
		glTranslatef (t3x, 0.0, space);
		material( 0.25, 0.25, 0.25, 0.4, 0.4, 0.4, 0.774597, 0.774597, 0.774597, 0.6); //sphere color
		glutSolidSphere(spr,numDiv,numDiv);
		glTranslatef (t2x, 0.0, 0.0);
    glPushMatrix();
		glScalef (sx, syz, syz);
		material( 0.25, 0.25, 0.65, 0.4, 0.4, 0.4, 0.774597, 0.774597, 0.774597, 0.6); //cube color
		glutSolidCube (1.0);// finger 4 piece
	glPopMatrix();

}


//write a legend to the screen
void write_text(){

   material( 12, 12,12, 0, 0, 0, 0, 0, 0, 0);

   doRasterString(-5, -2.5, "LEGEND:");

    doRasterString(-7, -3.2,"- press 'x' to rotate shoulder around x axis");
  
   doRasterString(-7, -3.7,"- press 'y' to rotate shoulder around y axis");

   doRasterString(-7, -4.2,"- press 'z' to rotate shoulder around z axis");
  
   doRasterString(-7, -4.7,"- press 'e' to move elbow on z axis");
 
   doRasterString(-7, -5.2,  "- press 'r' to rotate elbow on x axis");

   doRasterString(-7, -5.7,   "- press 'h' to move hand on z axis");

   doRasterString(-7, -6.2, "- press 'f' to open and close fingers");
	
   doRasterString(-7, -6.7, "- the uppercase version of these keys will do");

   doRasterString(-7, -7.2,"   the reverse of the lowercase");

}

void display(void)
{

   
	
   glClear (GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
   
    write_text();

   glPushMatrix();
		glTranslatef (-1.0, 0.0, 0.0);
		glRotatef ((GLfloat) shoulderX, 1.0, 0.0, 0.0);
		glRotatef ((GLfloat) shoulderY, 0.0, 1.0, 0.0);
		glRotatef ((GLfloat) shoulderZ, 0.0, 0.0, 1.0);
		glTranslatef (1.0, 0.0, 0.0);
	 glPushMatrix();
		 glScalef (2.0, 0.4, 0.8);
		 material( 0.25, 0.25, 0.65, 0.4, 0.4, 0.4, 0.774597, 0.774597, 0.774597, 0.6); //cube color
		glutSolidCube (1.0);// lower arm
	 glPopMatrix();
	 //elbow
		glTranslatef (1.32, 0.0, 0.0);
		glRotatef ((GLfloat) elbow, 0.0, 0.0, 1.0);
		glRotatef ((GLfloat) elbowRotate, 1.0, 0.0, 0.0);
		material( 0.25, 0.25, 0.25, 0.4, 0.4, 0.4, 0.774597, 0.774597, 0.774597, 0.6); //sphere color
		glutSolidSphere(0.4,10,10);
		glTranslatef (1.32, 0.0, 0.0);
	glPushMatrix();
		glScalef (2.0, 0.4, 0.8);
		material( 0.25, 0.25, 0.65, 0.4, 0.4, 0.4, 0.774597, 0.774597, 0.774597, 0.6); //cube color
		glutSolidCube (1.0);// draw elbow
	glPopMatrix();
	//hand 
		glTranslatef (1.32, 0.0, 0.0);
		glRotatef ((GLfloat) hand, 0.0, 0.0, 1.0);
		material( 0.25, 0.25, 0.25, 0.4, 0.4, 0.4, 0.774597, 0.774597, 0.774597, 0.6); //sphere color
		glutSolidSphere(0.4,10,10);
		glTranslatef (0.78, 0.0, 0.0);
	glPushMatrix();
		glScalef (0.8, 0.4, 0.8);
		material( 0.25, 0.25, 0.65, 0.4, 0.4, 0.4, 0.774597, 0.774597, 0.774597, 0.6); //cube color
		glutSolidCube (1.0);// draw hand
	glPopMatrix();
	glPushMatrix();
	//fingers (lower third)
		fingerPiece(0.4, 0.0, -0.3, fingers[0][1], 0.12 , 10 , 0.5 , 0.12);
	//fingers (middle third)	
	 fingerPiece(0.25, 0.0, -0.6, fingers[0][1], 0.12 , 10 , 0.4 , 0.12);
	//fingers (upper third)
		fingerPiece(0.2, 0.0, -0.6, fingers[0][2], 0.12 , 10 , 0.3 , 0.12);
    //thumb
    glPopMatrix();
		glTranslatef (0.20, -0.06, 0.52);
		glRotatef ((GLfloat) thumb[0], -1.0, 0.0, 0.0);
		material( 0.25, 0.25, 0.25, 0.4, 0.4, 0.4, 0.774597, 0.774597, 0.774597, 0.6); //sphere color
		glutSolidSphere(0.12,10,10);
		glTranslatef (0.0, 0.0, 0.22);
	glPushMatrix();
		glScalef (0.12, 0.12, 0.2);
		material( 0.25, 0.25, 0.65, 0.4, 0.4, 0.4, 0.774597, 0.774597, 0.774597, 0.6); //cube color
		glutSolidCube (1.0);// draw thumb part 1
   glPopMatrix();
        glTranslatef (0.0, 0.0, 0.20);
		glRotatef ((GLfloat) thumb[1], -1.0, 0.0, 0.0);
		material( 0.25, 0.25, 0.25, 0.4, 0.4, 0.4, 0.774597, 0.774597, 0.774597, 0.6); //sphere color
		glutSolidSphere(0.12,10,10);
		glTranslatef (0.0, 0.0, 0.22);
	glPushMatrix();
		glScalef (0.12, 0.12, 0.2);
		material( 0.25, 0.25, 0.65, 0.4, 0.4, 0.4, 0.774597, 0.774597, 0.774597, 0.6); //cube color
		glutSolidCube (1.0);// draw thumb part 2
   glPopMatrix();
   glPopMatrix();

   glutSwapBuffers();  

   material( 12, 12,12, 0, 0, 0, 0, 0, 0, 0);
}



void reshape (int w, int h)
{
   glViewport (0, 0, (GLsizei) w, (GLsizei) h); 
   glMatrixMode (GL_PROJECTION);
   glLoadIdentity ();
   gluPerspective(75.0, (GLfloat) w/(GLfloat) h, 1.0, 20.0);
   glMatrixMode(GL_MODELVIEW);
   glLoadIdentity();
   glTranslatef (0.0, 0.0, -10.0);

   write_text();

}

void keyboard (unsigned char key, int x, int y)
{
   
	switch (key) {
		//rotate the shoulder around x,y and z axis 
      case 'x':
         shoulderX = (shoulderX + 5) % 360;
         glutPostRedisplay();
         break;
      case 'X':
         shoulderX = (shoulderX - 5) % 360;
         glutPostRedisplay();
         break;
	  case 'y':
         shoulderY = (shoulderY + 5) % 360;
         glutPostRedisplay();
         break;
      case 'Y':
         shoulderY = (shoulderY - 5) % 360;
         glutPostRedisplay();
         break;
      case 'z':
         shoulderZ = (shoulderZ + 5) % 360;
         glutPostRedisplay();
         break;
      case 'Z':
         shoulderZ = (shoulderZ - 5) % 360;
         glutPostRedisplay();
         break;
      //move and rotate elbow
      case 'e':
		  if(increaseElbow == 1){
				 elbow = elbow +5;
				 if(elbow == 120){
					increaseElbow = 0;
				 }
			 }else{
				 elbow = elbow - 5;
				 if(elbow == 0){
					 increaseElbow = 1;
				 }
			 }
         glutPostRedisplay();
         break;
      case 'E':
         if(increaseElbow == 1){
				 elbow = elbow -5;
				 if(elbow == 0){
					increaseElbow = 0;
				 }
			 }else{
				 elbow = elbow + 5;
				 if(elbow == 120){
					 increaseElbow = 1;
				 }
			 }
         glutPostRedisplay();
         break;
      case 'r':
		  if(increaseElbow == 1){
				 elbowRotate = elbowRotate + 5;
				 if(elbowRotate == 180){
					increaseElbow = 0;
				 }
			 }else{
				elbowRotate = elbowRotate - 5;
				 if(elbowRotate == 0){
					 increaseElbow = 1;
				 }
			 }
         glutPostRedisplay();
         break;
      case 'R':
         if(increaseElbow == 1){
				 elbowRotate = elbowRotate - 5;
				 if(elbowRotate == 0){
					increaseElbow = 0;
				 }
			 }else{
				 elbowRotate = elbowRotate + 5;
				 if(elbowRotate == 180){
					 increaseElbow = 1;
				 }
			 }
         glutPostRedisplay();
         break;
	  //move hand
      case 'h':
			 
			 if(increaseHand == 1){
				 hand = hand +5;
				 if(hand == 90){
					increaseHand = 0;
				 }
			 }else{
				 hand = hand - 5;
				 if(hand == -90){
					 increaseHand = 1;
				 }
			 }

         glutPostRedisplay();
         break;
      case 'H':
        if(increaseHand == 1){
				 hand = hand -5;
				 if(hand == -90){
					increaseHand = 0;
				 }
			 }else{
				 hand = hand + 5;
				 if(hand == 90){
					 increaseHand = 1;
				 }
			 }
         glutPostRedisplay();
         break;
	  //close and open fingers
	  case 'f':
			 
			 if(increaseFingers == 1){
				 fingers[0][0] = fingers[0][0] +5;
				 fingers[0][1] = fingers[0][0];
				 fingers[0][2] = fingers[0][0];
				 fingers[0][3] = fingers[0][0];
				 thumb[0] = fingers[0][0];
				 thumb[1] = fingers[0][0];
				 if(fingers[0][0] == 90){
					increaseFingers = 0;
				 }
			 }else{
				 fingers[0][0] = fingers[0][0] -5;
				 fingers[0][1] = fingers[0][0];
				 fingers[0][2] = fingers[0][0];
				 fingers[0][3] = fingers[0][0];
				  thumb[0] = fingers[0][0];
				  thumb[1] = fingers[0][0];
				 if(fingers[0][0] == 0){
					 increaseFingers = 1;
				 }
			 }

         glutPostRedisplay();
         break;
	  case 'F':
		  if(increaseFingers == 1){
				 fingers[0][0] = fingers[0][0] -5;
				 fingers[0][1] = fingers[0][0];
				 fingers[0][2] = fingers[0][0];
				 fingers[0][3] = fingers[0][0];
				 thumb[0] = fingers[0][0];
				 thumb[1] = fingers[0][0];
				 if(fingers[0][0] == 0){
					increaseFingers = 0;
				 }
			 }else{
				 fingers[0][0] = fingers[0][0] +5;
				 fingers[0][1] = fingers[0][0];
				 fingers[0][2] = fingers[0][0];
				 fingers[0][3] = fingers[0][0];
				  thumb[0] = fingers[0][0];
				  thumb[1] = fingers[0][0];
				 if(fingers[0][0] == 90){
					 increaseFingers = 1;
				 }
			 }
      case 27:
         exit(0);
         break;
      default:
         break;
   }
}

int main(int argc, char** argv)
{
   glutInit(&argc, argv);
   glutInitDisplayMode (GLUT_DOUBLE | GLUT_RGB);
   glutInitWindowSize (600, 600); 
   glutInitWindowPosition (100, 100);
   glutCreateWindow (argv[0]);
   init ();
   glutDisplayFunc(display); 
   glutReshapeFunc(reshape);
   glutKeyboardFunc(keyboard);
   glutMainLoop();
   return 0;
}
