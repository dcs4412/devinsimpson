
/* Author: Devin Simpson */

/* A room scene constructed from simple and complex shapes */

#include <stdlib.h>
#include <GL/glut.h>
#include <math.h>
#include "RGBpixmap.h"

static GLuint texName;
RGBpixmap pic; //  pixmap
int ImageWidth = 512, ImageHeight = 512; 
static GLubyte Image[512][512][4];
float c = 3.14/180.0;
float yangle = 0 ;
int fogMode;
void right_menu(int);
void fog_menu(int);
void lamp_menu(int);

int lampX,lampY,lampZ;

//<<<<<<<<<<<<<<<<<<<<<<<wall>>>>>>>>>>>>>>>>>>>>>>>>\

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

void wall (double thickness)
{
 //draw thin wall waith top = xz-plane, corner at origin
	glPushMatrix();
    glTranslatef (0.5, 0.5*thickness, 0.5);
	glScalef(1.0, thickness, 1.0);
    glutSolidCube(1.0);
	glPopMatrix();
}

//<<<<<<<<<<<<<<<<<<<<table leg >>>>>>>>>>>>>>>>>>>>>\\

void tableleg(double thick, double len)
{
   	glPushMatrix();
    glTranslatef (0, len/2, 0);
	glScalef(thick, len, thick);
    glutSolidCube(1.0);
    glPopMatrix();
}

//<<<<<<<<<<<<<<<<<<<<lamp>>>>>>>>>>>>>>>>>>>>>>>>\\

void drawLamp()
{
	glPushMatrix();
	glScalef(0.1, 0.02, 0.2);
	material( 0.36, 0.223529, 0.027451,0.780392, 0.568627, 0.113725, 0.992157, 0.941176, 0.807843,0.21794872);
	glutSolidCube(1.0);
	glPopMatrix();

	glPushMatrix();
	glTranslatef (0, 0.02, 0);
	glScalef(0.07, 0.03, 0.14);
	material( 0.36, 0.223529, 0.027451,0.780392, 0.568627, 0.113725, 0.992157, 0.941176, 0.807843,0.21794872);
	glutSolidCube(1.0);
	glPopMatrix();

	glPushMatrix();
	glTranslatef (0, 0.05, 0);
	glScalef(0.008, 0.16, 0.008);
	material(0.0, 0.0, 0.0, 0.01, 0.01, 0.01, 0.50, 0.50, 0.50, .25);
	glutSolidCube(1.0);
	glPopMatrix();

	glPushMatrix();
	glTranslatef (0, 0.13, 0);
	glScalef(0.085, 0.02, 0.17);
	material(1, 1, 1, 0.5, 0.5, 0.5, 0.7, 0.7, 0.7, .078125);
	glutSolidCube(1.0);
	glPopMatrix();

	glPushMatrix();
	glTranslatef (0, 0.16, 0);
	glScalef(0.1, 0.03, 0.2);
	material( 0.36, 0.223529, 0.027451,0.780392, 0.568627, 0.113725, 0.992157, 0.941176, 0.807843,0.21794872);
	glutSolidCube(1.0);
	glPopMatrix();
}

//<<<<<<<<<<<<<<<<<<<<table>>>>>>>>>>>>>>>>>>>>>>>>\\

void table (double topWid, double topThick, double legThick, double legLen)
{
// draw the table top and four legs
	double dist;
    glPushMatrix();
    glTranslatef (0, legLen, 0);

    glScalef(topWid, topThick, topWid);
	glutSolidCube(1.0);

	glPopMatrix();
	dist = 0.95 * topWid/2.0 - legThick/2.0;

    glPushMatrix();    
    glTranslatef (dist, 0, dist);
	tableleg(legThick, legLen);

	glTranslatef (0, 0, -2*dist);
	tableleg(legThick, legLen);

	glTranslatef (-2*dist, 0, 2*dist);
	tableleg(legThick, legLen);

	glTranslatef (0, 0, -2*dist);
	tableleg(legThick, legLen);

    glPopMatrix();
	
}


//<<<<<<<<<<<<<<<<<<<<chair>>>>>>>>>>>>>>>>>>>>>>>>\\

void drawChair()
{
	table(0.22, 0.02, 0.02, 0.2);

	glPushMatrix();
	glTranslatef (-0.01, 0.32, 0.168);
	glScalef (0.02, 0.15, 0.02);
	glutSolidCube(1.0);
	glPopMatrix();

	glPushMatrix();
	glTranslatef (0.176, 0.32, 0.168);
	glScalef(0.02, 0.15, 0.02);
	glutSolidCube(1.0);
	glPopMatrix();

	glPushMatrix();
	glTranslatef (0.080, 0.47, 0.168);
	glScalef(0.212, 0.15, 0.02);
	glutSolidCube(1.0);
	glPopMatrix();
}

//vector functions

void VectorFromPoints(float pointA[3], float pointB[3], float vectorAB[3])
{
    vectorAB[0] = pointB[0] - pointA[0];
    vectorAB[1] = pointB[1] - pointA[1];
    vectorAB[2] = pointB[2] - pointA[2];
}

float VectorMagnitude(float vector[3])
{

	float temp;

	temp = vector[0]*vector[0];
	temp += vector[1]*vector[1];
	temp += vector[2]*vector[2];

	return(sqrt(temp));
}

void VectorCrossProduct(float x[3], float y[3], float xCrossy[3])
{

	xCrossy[0] = x[1]*y[2] - x[2]*y[1] ;
	xCrossy[1] = x[2]*y[0] - x[0]*y[2];
	xCrossy[2] = x[0]*y[1] - x[1]*y[0] ;

}

void VectorNormalize(float vector[3], float normalizedVector[3])
{
	float temp;

	temp = VectorMagnitude(vector);

	normalizedVector[0] = vector[0]/temp;
	normalizedVector[1] = vector[1]/temp;
	normalizedVector[2] = vector[2]/temp;
}


//<<<<<<<<<<<<<<<<<<<<vase>>>>>>>>>>>>>>>>>>>>>>>>\\

void drawVase()
{

	float AB[3], AC[3],OrthogonalABC[3], normalizedABC[3];//vectors--Adhar
	float pointA[3], pointB[3], pointC[3];// Adhar

	double x, y, z, thet, phi; /*co-ordinates*/
	int nlat, nlong; /*latitude and longitude */
	double c; /*radians */
	int index = 0, toggle = 0;
	int p[18] = {58,59,62,67,72,67,60,52,40,35,30,35,40,50,60,70,70}; //the phi values to create the vase curve
	int i[18] = {1,3,5,5,-5,-7,-8,-12,-5,-5,5,5,10,10,10,0,0};//the difference between the current phi value and the next (keeps the quads together)


	c=3.14159/180.0;  /*degree to radian convertor */


	x=y=0;
	z=1;  
	glBegin(GL_TRIANGLE_FAN);
	glVertex3d(x,y,z);
	z=sin(c*80.0);

	pointA[0] = x;
	pointA[1] = y;
	pointA[2] = z;

	for(thet=-180.0; thet<=180.0;thet+=10)
	{
		x=sin(c*thet)*cos(c*110.0);
		y=cos(c*thet)*cos(c*110.0);

		if(toggle == 0){
		pointB[0] = x;
		pointB[1] = y;
		pointB[2] = z;
	
		//do vector caluations
		VectorFromPoints(pointA, pointB , AB);
		toggle = 1;
		}else{

		pointC[0] = x;
		pointC[1] = y;
		pointC[2] = z;

		//do vector caluations

		VectorFromPoints(pointA, pointC , AC);
		VectorCrossProduct(AB,AC,OrthogonalABC);
		VectorNormalize(OrthogonalABC, normalizedABC);
		glNormal3fv(normalizedABC);
		toggle = 0;

		pointA[0] = x;
		pointA[1] = y;
		pointA[2] = z;
		}
		
		glVertex3d(x,y,z);
		
	}
	glEnd();

	toggle = 0;

	for(phi=-80.0; phi<=80.0; phi += 10)  /* latitude */
	{ 
	
		glBegin(GL_QUAD_STRIP);
		for(thet=-180.0; thet<=180.0;thet+= 10)  /* longitude */
		{
			x=sin(c*thet)*cos(c*p[index]);
			y=cos(c*thet)*cos(c*p[index]);

			z=sin(c*phi);
		
			if(toggle == 0){
			pointA[0] = x;
			pointA[1] = y;
			pointA[2] = z;
			}else{
				pointC[0] = x;
				pointC[1] = y;
				pointC[2] = z;
			}
			glVertex3d(x,y,z);
			x=sin(c*thet)*cos(c*(p[index]+ i[index]));
			y=cos(c*thet)*cos(c*(p[index]+ i[index]));
			z=sin(c*(phi+10));
			

			if(toggle == 0){

				pointB[0] = x;
				pointB[1] = y;
				pointB[2] = z;

				VectorFromPoints(pointA, pointB , AB);
				toggle = 1;
			}else
			{

				//do vector caluations

				VectorFromPoints(pointA, pointC , AC);
				VectorCrossProduct(AB,AC,OrthogonalABC);
				VectorNormalize(OrthogonalABC, normalizedABC);
				glNormal3fv(normalizedABC);
				toggle = 0;
		
			}


			glVertex3d(x,y,z);
		}
		glEnd();
	index++;
	}

}

//load the image for texture

void LoadImage()
{
    int i,j;
	mRGB  pixel;
	pic.readBMPFile("ibex.bmp"); //make a pixmap from a bmp file

	for (i = 0 ; i <ImageHeight; i++) {
		for (j = 0; j < ImageWidth; j++) {

			  pixel = pic.getPixel(i,j);
			  Image[i][j][0] =  (GLubyte) pixel.r ;
			  
			  Image[i][j][1] =  (GLubyte) pixel.g ;
			  
			  Image[i][j][2] =  (GLubyte) pixel.b ;
			 
			  Image[i][j][3] =  (GLubyte)  255;
 			}
		}
}
void display(void)
{

   double winHt = 1.0; 
   
   GLfloat mat_ambient[] = { 0.7, 0.7, 0.7, 1.0 };
   GLfloat mat_diffuse[] = { 0.8, 0.8, 0.8, 1.0 };
   GLfloat mat_specular[] = { 1.0, 1.0, 1.0, 1.0 };
   
   GLfloat mat_shininess[] = { 50.0 };

   GLfloat lmodel_ambient[] = { 0.4, 0.4, 0.4, 1.0 };
   GLfloat local_view[] = { 0.0 };
   
  
   GLfloat diffuse[] = { 0.7, 0.7, 0.7, 1.0 };
   GLfloat position[] = { 2.0, 6.0, 3.0, 0.0 };


   glMaterialfv(GL_FRONT, GL_AMBIENT, mat_ambient);
   glMaterialfv(GL_FRONT, GL_DIFFUSE, mat_diffuse);
   glMaterialfv(GL_FRONT, GL_SPECULAR, mat_specular);
   glMaterialfv(GL_FRONT, GL_SHININESS, mat_shininess);

     

   glClearColor(0.1,0.1,0.1, 0.0);

   glLightfv(GL_LIGHT0, GL_DIFFUSE, diffuse);
   glLightfv(GL_LIGHT0, GL_POSITION, position);

   glMatrixMode(GL_PROJECTION);
   glLoadIdentity();
   glOrtho(-winHt*64/48, winHt*64/48, -winHt, winHt, 0.1, 100.0);

   glMatrixMode(GL_MODELVIEW);
   glLoadIdentity();
   gluLookAt(2.3, 1.3, 2.0, 0, 0.25,0, 0.0, 1.0, 0.0);

   glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);

   glPushMatrix();
   glTranslatef (0.4, 0, 0.4);
   material(0.7, 0.7, 0.2, 0.5, 0.5, 0.4,0.7, 0.7, 0.04, .078125);
   table(0.6, 0.02, 0.02, 0.3);
   glPopMatrix();

   glPushMatrix();
   material(0.7, 0.0, 0.0, 0.5, 0.5, 0.4,0.7, 0.7, 0.04, .078125);
   wall(0.02);    // wall -1 in xz plane
	glPopMatrix();
   glPushMatrix();
   glRotatef(90.0, 0.0, 0.0, 1.0);
   material(0, 1, 1, 0.5, 0.5, 0.4,0.7, 0.7, 0.04, .078125);
   wall(0.02);   // wall -2 in yz plane
   glPopMatrix();
   glPushMatrix();
   material(0, 1, 1, 0.5, 0.5, 0.4,0.7, 0.7, 0.04, .078125);
   glRotatef(-90.0, 1.0, 0.0, 0.0);
   wall(0.02);    // wall - 3 in xz plane
   glPopMatrix();

   
   //lets add some items on the table

   //teapot
   
	glPushMatrix();
	glTranslatef (0.34, 0.38 ,0.66);
	material(0.1745, 0.01175, 0.01175, 0.61424, 0.04136, 0.04136, 0.727811, 0.626959, 0.626959, 0.6);
	glutSolidTeapot(0.06);
	glPopMatrix();
	
	//lamp
	glPushMatrix();
	glTranslatef (0.25, 0.32 ,0.25);
	glRotatef(-45.0, 0.0, 1.0, 0.0);
	drawLamp();
	glPopMatrix();

	 //lamp light
   GLfloat diffuse1[] = { 0.7, 0.7, 0.7, 1.0 };
   GLfloat position1[] = { 0.25, 0.52, 0.25, 1.0 };
   glLightfv(GL_LIGHT1, GL_DIFFUSE, diffuse1);
   glLightfv(GL_LIGHT1, GL_POSITION, position1);

	//vase
	glPushMatrix();
	glTranslatef (0.61, 0.42 ,0.23);
	glScalef(0.1, 0.1, 0.1);
	glRotatef(90.0, 1.0, 0.0, 0.0);
	//material(0.0, 0.0, 0.0, 0.61424, 0.04136, 0.04136, 0.727811, 0.626959, 0.626959, 0.6);
	drawVase();
	glPopMatrix();


	//now put some items on the floor

	//sphere
	glPushMatrix();
	glTranslatef (0.67, 0.1 ,0.36);
	material( 0.25, 0.25, 0.65, 0.4, 0.4, 0.4, 0.774597, 0.774597, 0.774597, 0.6);
	glutSolidSphere(0.1,25,25);
	glPopMatrix();
	
	
	//chair
	glPushMatrix();
	glTranslatef (0.38, 0 ,0.80);
	material(0.7, 0.7, 0.2, 0.5, 0.5, 0.4,0.7, 0.7, 0.04, .078125);
	drawChair();
	glPopMatrix();


	//apply the texture rectange to the 

	glEnable(GL_TEXTURE_2D);
	glTexEnvf(GL_TEXTURE_ENV, GL_TEXTURE_ENV_MODE, GL_REPLACE);
    glBindTexture(GL_TEXTURE_2D, texName);
	glTranslatef (0.7, 0.38 ,0.011);
	glRotatef(90.0, 0.0, 0.0, 1.0);
	glScalef(0.512, 0.512, 0.0);

	glBegin(GL_QUADS);
    glTexCoord2f(0.0, 0.0); glVertex3f(0.0, 0.0, 0.0);
	glTexCoord2f(0.0, 1.0); glVertex3f(0.0, 1.0, 0.0);
    glTexCoord2f(1.0, 1.0); glVertex3f(1.0, 1.0, 0.0);
	glTexCoord2f(1.0, 0.0); glVertex3f(1.0, 0.0, 0.0);
    
    
	glEnd();
	glDisable(GL_TEXTURE_2D);


    glFlush();
	glutSwapBuffers();
}

void init(void)
{    
   glClearColor (0.0, 0.0, 0.0, 0.0);
   glShadeModel(GL_FLAT);
   glEnable(GL_DEPTH_TEST);
   
   LoadImage();
   
   glPixelStorei(GL_UNPACK_ALIGNMENT, 1);


   glGenTextures(1, &texName);
   glBindTexture(GL_TEXTURE_2D, texName);


   glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL_REPEAT);
   glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL_REPEAT);
   glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_NEAREST);
   glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_NEAREST);

   glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA, ImageWidth, ImageHeight, 
                0, GL_RGBA, GL_UNSIGNED_BYTE, Image);


   glEnable(GL_FOG);
   {
      GLfloat fogColor[4] = {0.5, 0.5, 0.5, 1.0};

      fogMode = 0;
      glFogi (GL_FOG_MODE, fogMode);
      glFogfv (GL_FOG_COLOR, fogColor);
      glFogf (GL_FOG_DENSITY, 0.0);
      glHint (GL_FOG_HINT, GL_DONT_CARE);
      glFogf (GL_FOG_START, 1.0);
      glFogf (GL_FOG_END, 5.0);
   }
   glClearColor(0.5, 0.5, 0.5, 1.0);  /* fog color */
}

void right_menu(int id)
{
	if(id == 1) exit(0);
}
void fog_menu(int id)
{
	
	if(id == 1){
		glDisable(GL_FOG);
	}else if(id == 2) {
		glEnable(GL_FOG);
		fogMode = GL_EXP;
		glFogf (GL_FOG_DENSITY, 0.35);
	}else if(id == 3){
		glEnable(GL_FOG);
		fogMode = GL_EXP2;
		glFogf (GL_FOG_DENSITY, 0.35);
	}else{
		glEnable(GL_FOG);
		fogMode = GL_LINEAR;
		glFogf (GL_FOG_DENSITY, 0.35);
	}

	 glFogi (GL_FOG_MODE, fogMode);
	 glutPostRedisplay();

}
void lamp_menu(int id)
{
	if(id == 1){
	
	glEnable(GL_LIGHT1);

	}else{

	glDisable(GL_LIGHT1);

	}

	 glutPostRedisplay();


}

int main(int argc, char** argv)
{

   int f_menu, l_menu;
   
   

   glutInit(&argc, argv);
   glutInitDisplayMode (GLUT_SINGLE | GLUT_RGB | GLUT_DEPTH);
   glutInitWindowSize (640, 480);
   glutCreateWindow("simple room");
   glutDisplayFunc(display);
   
   init();
   
   
   glEnable(GL_LIGHTING);
   glEnable(GL_LIGHT0);

   glShadeModel (GL_SMOOTH);
   glEnable(GL_DEPTH_TEST);
   glEnable(GL_NORMALIZE);
   glClearColor(0.1, 0.1, 0.1, 0.0);

   f_menu = glutCreateMenu(fog_menu);
   glutAddMenuEntry("off",1);
   glutAddMenuEntry("exp",2);
   glutAddMenuEntry("exp2",3);
   glutAddMenuEntry("linear",4);

   l_menu = glutCreateMenu(lamp_menu);
   glutAddMenuEntry("lamp on",1);
   glutAddMenuEntry("lamp off",2);

    glutCreateMenu(right_menu);
    glutAddMenuEntry("quit",1);
	glutAddSubMenu("fog",f_menu);
	glutAddSubMenu("lamp",l_menu);
   glutAttachMenu(GLUT_RIGHT_BUTTON);
   display();
   glutMainLoop();
   return 0; 
}
