/* vase.c  */

/* devin simpson */

/* uses polygons in line mode */
 
/* simple mouse click interface to rotate */

#include <stdlib.h>
#include <GL/glut.h>
#include <math.h>


void earth()
{

double x, y, z, thet, phi; /*co-ordinates*/
int nlat, nlong; /*latitude and longitude */
double c; /*radians */
int index = 0;
int p[18] = {58,59,62,67,72,67,60,52,40,35,30,35,40,50,60,70,70}; //the phi values to create the vase curve
int i[18] = {1,3,5,5,-5,-7,-8,-12,-5,-5,5,5,10,10,10,0,0};//the difference between the current phi value and the next (keeps the quads together)


c=3.14159/180.0;  /*degree to radian convertor */


x=y=0;
z=1;  
glBegin(GL_TRIANGLE_FAN);
glVertex3d(x,y,z);
z=sin(c*80.0);
for(thet=-180.0; thet<=180.0;thet+=10)
{
	x=sin(c*thet)*cos(c*110.0);
	y=cos(c*thet)*cos(c*110.0);
	glVertex3d(x,y,z);
}
glEnd();

for(phi=-80.0; phi<=80.0; phi += 10)  /* latitude */
{ 
	
	glBegin(GL_QUAD_STRIP);
	for(thet=-180.0; thet<=180.0;thet+= 10)  /* longitude */
	{
		x=sin(c*thet)*cos(c*p[index]);
		y=cos(c*thet)*cos(c*p[index]);
		z=sin(c*phi);
		glVertex3d(x,y,z);
		x=sin(c*thet)*cos(c*(p[index]+ i[index]));
		y=cos(c*thet)*cos(c*(p[index]+ i[index]));
		z=sin(c*(phi+10));
		glVertex3d(x,y,z);
	}
	glEnd();
index++;
}

}

static GLfloat theta[] = {0.0,0.0,0.0};
static GLint axis = 2;

void display(void)
{
/* display callback, clear frame buffer and z buffer,
   rotate sphere and draw, swap buffers */

 glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
	glLoadIdentity();
	glRotatef(theta[0], 1.0, 0.0, 0.0);
	glRotatef(theta[1], 0.0, 1.0, 0.0);
	glRotatef(theta[2], 0.0, 0.0, 1.0);

 earth();

 /*glFlush();*/
	glutSwapBuffers();
}

void spinEarth()
{

/* Idle callback, rotate sphere 2 degrees about selected axis */

	theta[axis] += 10.0;
	if( theta[axis] > 360.0 ) theta[axis] -= 360.0;
	/* display(); */
	glutPostRedisplay();
}

void mouse(int btn, int state, int x, int y)
{

/* mouse callback, selects an axis about which to rotate */

	if(btn==GLUT_LEFT_BUTTON && state == GLUT_DOWN) theta[0]+=10;
	if(btn==GLUT_MIDDLE_BUTTON && state == GLUT_DOWN) theta[1]+= 10;
	if(btn==GLUT_RIGHT_BUTTON && state == GLUT_DOWN) theta[2]+=10;
	glutPostRedisplay();
}

void myReshape(int w, int h)
{
    glViewport(0, 0, w, h);
    glMatrixMode(GL_PROJECTION);
    glLoadIdentity();
    if (w <= h)
        glOrtho(-2.0, 2.0, -2.0 * (GLfloat) h / (GLfloat) w,
            2.0 * (GLfloat) h / (GLfloat) w, -10.0, 10.0);
    else
        glOrtho(-2.0 * (GLfloat) w / (GLfloat) h,
            2.0 * (GLfloat) w / (GLfloat) h, -2.0, 2.0, -10.0, 10.0);
    glMatrixMode(GL_MODELVIEW);
}

void
main(int argc, char **argv)
{
    glutInit(&argc, argv);

/* need both double buffering and z buffer */

    glutInitDisplayMode(GLUT_DOUBLE | GLUT_RGB | GLUT_DEPTH);
    glutInitWindowSize(500, 500);
    glutCreateWindow("vase");
    glutReshapeFunc(myReshape);
    glutDisplayFunc(display);
	   glutIdleFunc(NULL);
	   glutMouseFunc(mouse);
	   glEnable(GL_DEPTH_TEST); /* Enable hidden--surface--removal */
	   glClearColor(1.0, 1.0, 1.0, 1.0);
	   glColor3f(0.0, 0.0, 0.0);
	glPolygonMode(GL_FRONT_AND_BACK,GL_LINE);
    glutMainLoop();
}
