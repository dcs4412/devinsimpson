/* editor.c   */
/*
 * Devin Simspson
 *
 * logic gate editor
 *
 * 11/15/2013
 *
 */


/* This program illustrates a very basic editor */


#define NULL 0
#define LINE 1
#define ANDGATE 2
#define ORGATE  3
#define NOTGATE 4
#define TEXT 5

#include <stdlib.h>
#include <GL/glut.h>
#include <string.h>
#include <math.h>
#include <GL\glui.h>


void mouse(int, int, int, int);
void display(void);
void drawSquare(int, int);
void myReshape(GLsizei, GLsizei);
void drawArcOrCircle(int,int,int,char);
void drawBezierCurve(int,int,float,float);
void drawANDgate(int,int);
void drawORgate(int,int);
void drawNOTgate(int,int);
void myMouse(int,int);

void fillInOutUnconnected(int,int,char);
void myinit(void);

int checkXandY(int,int,char,char);

void screen_box(int, int, int);
void right_menu(int);
void color_menu(int);
void create_menu(int);

int savedANDgates[50][2];
int savedORgates[50][2];
int savedNOTgates[50][2];

int unConnectedOutput[150][2];
int connectedOutput[150][2];
int unConnectedInput[150][2];
int connectedInput[150][2];


/* globals */

GLsizei wh = 500, ww = 500; /* initial window size */
GLfloat size = 3.0,topY,bottomY;   /* half side length of square */
int draw_mode = 0; /* drawing mode */
int rx, ry; /*raster position*/
int mouseX, mouseY,selectedX,selectedY,oldX,oldY,viewedX,viewedY; //mouse coordinates


GLfloat r = 0.0, g = 0.0, b = 0.0; /* drawing color */
int fill = 0; /* fill flag */

/* glui menu elements */
int   main_window;
GLUI *glui, *colorGlui;


void drawSquare(int x, int y)
{

        y=wh-y;
        glColor3ub( (char) rand()%256, (char) rand()%256, (char) rand()%256); 
        glBegin(GL_POLYGON);
                glVertex2f(x+size, y+size);
                glVertex2f(x-size, y+size);
                glVertex2f(x-size, y-size);
                glVertex2f(x+size, y-size);
        glEnd();
}

/* rehaping routine called whenever window is resized
or moved */

void myReshape(GLsizei w, GLsizei h)
{

	int i;

/* adjust clipping box */

        glMatrixMode(GL_PROJECTION);
        glLoadIdentity(); 
        glOrtho(0.0, (GLdouble)w, 0.0, (GLdouble)h, -1.0, 1.0);
        glMatrixMode(GL_MODELVIEW);
        glLoadIdentity(); 

		
	

/* adjust viewport and  clear */

        glViewport(0,0,w,h);
    glClearColor (1.0, 1.0, 1.0, 1.0);
        glClear(GL_COLOR_BUFFER_BIT);

		for(i = 0; i < 150; i++){

			if((connectedOutput[i][0] != 0 && connectedOutput[i][1] != 0) && (connectedInput[i][0] != 0 && connectedInput[i][1] != 0)){
			
				connectedOutput[i][1] = connectedOutput[i][1]+(h - wh);
				connectedInput[i][1] = connectedInput[i][1]+(h - wh);
			}

			if((unConnectedOutput[i][0] != 0 && unConnectedOutput[i][1] != 0)){  
				unConnectedOutput[i][1] = unConnectedOutput[i][1]+(h - wh);	
			}

			if(unConnectedInput[i][0] != 0 && unConnectedInput[i][1] != 0){
				unConnectedInput[i][1] = unConnectedInput[i][1]+(h - wh);
			}

		}

        display();
        //glutSwapBuffers();

/* set global size for use by drawing routine */

        ww = w;
        wh = h; 

		//glui 

		//GLUI_Master.auto_set_viewport(); 

}

void myinit(void)
{
	int i;
	int j;
	glViewport(0,0,ww,wh);

/* Pick 2D clipping window to match size of X window 
This choice avoids having to scale object coordinates
each time window is resized */

        glMatrixMode(GL_PROJECTION);
        glLoadIdentity(); 
        glOrtho(0.0, (GLdouble) ww , 0.0, (GLdouble) wh , -1.0, 1.0);

/* set clear color to black and clear window */

        glClearColor (1.0, 1.0, 1.0, 1.0);
        glClear(GL_COLOR_BUFFER_BIT);



		for(i = 0; i <50 ; i++)
			for(j = 0; j < 2; j++){
				savedANDgates[i][j] = 0;
				savedORgates[i][j] = 0;
				savedNOTgates[i][j] = 0;
			}

		for(i = 0; i <150 ; i++)
			for(j = 0; j < 2; j++){
				unConnectedOutput[i][j] = 0;
				connectedOutput[i][j] = 0;
				unConnectedInput[i][j]= 0;
				connectedInput[i][j]= 0;
			}
		selectedX = 0;
		selectedY = 0;
		oldX = 0;
		oldY = 0;
		viewedX = 0;
		viewedY = 0;
}

void mouse(int btn, int state, int x, int y)
{
	int i=0,dx;
	


    if(btn==GLUT_LEFT_BUTTON && state==GLUT_DOWN) 
    {
       glPushAttrib(GL_ALL_ATTRIB_BITS);

	   
       if(checkXandY(x,wh-y,'o','y') == 1){
		   draw_mode = LINE;
		   glColor3f(1, 0,0);
		   glBegin(GL_LINES); 
				glVertex2i(selectedX,selectedY);
				glVertex2i(x,wh -y);
			glEnd();

			oldX = selectedX;
			oldY = selectedY;

	   }
	  
      
       else switch(draw_mode)
       {
        
          
             
             
	   case(LINE):

		   if(checkXandY(x,wh-y,'i','y') == 1){
		   
			   draw_mode = 0;
			   glColor3f(1, 0,0);
			   glBegin(GL_LINE_STRIP); 
					glVertex2i(oldX,oldY);

					dx = (selectedX-oldX)/2;

					glVertex2i(oldX+dx,oldY);
					glVertex2i(oldX+dx,selectedY);
					glVertex2i(selectedX,selectedY);
				glEnd();

			

				for(i = 0; i < 150; i++){

					if(unConnectedInput[i][0] == selectedX && unConnectedInput[i][1] == selectedY){

						unConnectedInput[i][0] = 0;
						unConnectedInput[i][1] = 0;

					}

					if(unConnectedOutput[i][0] == oldX && unConnectedOutput[i][1] == oldY){

						unConnectedOutput[i][0] = 0;
						unConnectedOutput[i][1] = 0;

					}

				}

				i = 0;

				while((connectedOutput[i][0] != 0 && connectedOutput[i][1] != 0) && (connectedInput[i][0] != 0 && connectedInput[i][1] != 0)){
					i++;
					
				}

				connectedOutput[i][0] = oldX;
				connectedOutput[i][1] = oldY;

				connectedInput[i][0] = selectedX;
				connectedInput[i][1] = selectedY;

				oldX = 0;
				oldY = 0;
		   }else{

			   draw_mode = 0;

			   oldX = 0;
			   oldY = 0;

		   }

		   break;
         
        case(ANDGATE):
			 glColor3f(r, g, b);
			 drawANDgate(x,y);

			 i= 0;

			  while(savedANDgates[i][0] != 0 && savedANDgates[i][1] != 0){
				i++;
			}

			 savedANDgates[i][0] = x;
			 savedANDgates[i][1] = y;

			 fillInOutUnconnected(x,y,'a');

			 glutSetCursor(GLUT_CURSOR_RIGHT_ARROW);
             draw_mode=0;
            
			  
			 break;
		  
        case (ORGATE):
			glColor3f(r, g, b);
			drawORgate(x,y);

			i = 0;

			while(savedORgates[i][0] != 0 && savedORgates[i][1] != 0){
				i++;
			}

			 savedORgates[i][0] = x;
			 savedORgates[i][1] = y;

			 fillInOutUnconnected(x,y,'o');

			glutSetCursor(GLUT_CURSOR_RIGHT_ARROW);
              draw_mode=0;
             
          
          break;
        case(NOTGATE):
          {
			  glColor3f(r, g, b);
             drawNOTgate(x,y);

			 i = 0;

		while(savedNOTgates[i][0] != 0 && savedNOTgates[i][1] != 0){
				i++;
			}

			 savedNOTgates[i][0] = x;
			 savedNOTgates[i][1] = y;

			 fillInOutUnconnected(x,y,'n');

			glutSetCursor(GLUT_CURSOR_RIGHT_ARROW);
              draw_mode=0;
              
          }
		  break;
       }
	   glutSwapBuffers();
       glPopAttrib();
      
     }
}

void fillInOutUnconnected(int x, int y,char AON){

	int i;

	i = 0;
	if(AON == 'n'){ //indicates the creation of a NOT gate 
		//look for open output table slot
		while(unConnectedOutput[i][0] != 0 && unConnectedOutput[i][1] != 0){
					i++;
				}

		unConnectedOutput[i][0] = x+78;
		unConnectedOutput[i][1] = wh-(y-24);

		//look for open input table slot
		i = 0;
		while(unConnectedInput[i][0] != 0 && unConnectedInput[i][1] != 0){
					i++;
				}

		unConnectedInput[i][0] = x-20;
		unConnectedInput[i][1] = wh-(y-24);
	}else if(AON == 'a'){//indicates the creation of an AND gate 
		i = 0;
		//look for open output table slot
		while(unConnectedOutput[i][0] != 0 && unConnectedOutput[i][1] != 0){
					i++;
				}

		unConnectedOutput[i][0] = x+76;
		unConnectedOutput[i][1] = wh-(y-24);

		//look for open input table slot
		i = 0;
		while(unConnectedInput[i][0] != 0 && unConnectedInput[i][1] != 0){
					i++;
				}

		unConnectedInput[i][0] = x-20;
		unConnectedInput[i][1] = wh-y+10;

		//look for a second open input table slot
		i = 0;
		while(unConnectedInput[i][0] != 0 && unConnectedInput[i][1] != 0){
					i++;
				}

		unConnectedInput[i][0] = x-20;
		unConnectedInput[i][1] = wh-y+37;

	}else{

		i = 0;
		//look for open output table slot
		while(unConnectedOutput[i][0] != 0 && unConnectedOutput[i][1] != 0){
					i++;
				}

		unConnectedOutput[i][0] = x+55;
		unConnectedOutput[i][1] = wh-y-24;

		//look for open input table slot
		i = 0;
		while(unConnectedInput[i][0] != 0 && unConnectedInput[i][1] != 0){
					i++;
				}

		unConnectedInput[i][0] = x-41;
		unConnectedInput[i][1] = wh-y-12;

		//look for a second open input table slot
		i = 0;
		while(unConnectedInput[i][0] != 0 && unConnectedInput[i][1] != 0){
					i++;
				}

		unConnectedInput[i][0] = x-41;
		unConnectedInput[i][1] = wh-y-37;

	}
	
	
}

void drawNOTgate(int x, int y){

	glBegin(GL_LINES);
		glVertex2f(x,wh-y);
        glVertex2f(x,wh-(y-47));
	glEnd();
	glBegin(GL_LINES);
		glVertex2f(x,wh-(y-47));
        glVertex2f(x+47,wh-(y-24));
	glEnd();
	glBegin(GL_LINES);
		glVertex2f(x+47,wh-(y-24));
		glVertex2f(x,wh-y);
	glEnd();

	drawArcOrCircle(x+21,y,6,'c');

	//draw input line
	glBegin(GL_LINES);
		glVertex2f(x,wh-(y-24));
        glVertex2f(x-20,wh-(y-24));
	glEnd();

	//draw output line
	glBegin(GL_LINES);
		glVertex2f(x+58,wh-(y-24));
		glVertex2f(x+78,wh-(y-24));
	glEnd();
	
}

void drawORgate(int x, int y){


	//front curve
	drawBezierCurve(x,y,20,90);
	//lengthen the front curve
	glBegin(GL_LINES);
		glVertex2f(x-20,wh-y+1);
		glVertex2f(x-30,wh-y+1);
	glEnd();

	glBegin(GL_LINES);
		glVertex2f(x-20,bottomY);
		glVertex2f(x-30,bottomY);
	glEnd();
	//back curve
	drawBezierCurve(x,y,30,-4);


	//draw inputs
	glBegin(GL_LINES);
		glVertex2f(x-21,wh-y-12);
		glVertex2f(x-41,wh-y-12);
	glEnd();

	glBegin(GL_LINES);
		glVertex2f(x-21,wh-y-37);
		glVertex2f(x-41,wh-y-37);
	glEnd();
	//draw output 
	glBegin(GL_LINES);
		glVertex2f(x+35,wh-y-24);
		glVertex2f(x+55,wh-y-24);
	glEnd();
	
}

void drawBezierCurve(int x, int y, float offest1, float offset2){

	float i;

	float pX,pY,qX,qY,sX,sY,cX,cY,u;

	cX = x;
			cY = wh -y;

			pX = cX-offest1;
			pY = cY;
			qX = cX +offset2;
			qY =  cY -25;
			sX = cX-offest1;
			sY = cY - 49;

			topY = cY;
			bottomY= sY;
			glBegin(GL_LINE_STRIP); 
			for(i = 0; i < 41; i ++){

				u = i/40;

				cX = ((1 - u)*(1 - u))*pX + 2*u*(1-u)*qX + (u*u)*sX;

				cY = ((1 - u)*(1 - u))*pY + 2*u*(1-u)*qY + (u*u)*sY;

				glVertex2f(cX, cY);
			}
           glEnd();

}

void drawANDgate(int x, int y){
	//draw the line that make up the body of the AND gate
	int i;
    

	//draw the arc at the end of the AND gate body
	drawArcOrCircle(x, y,24,'a');

	glBegin(GL_LINES);
		glVertex2i(x,wh-y);
        glVertex2i(x,topY);
	glEnd();
	glBegin(GL_LINES);
		glVertex2i(x,(int)topY);
		glVertex2i(x+32,(int)topY);
	glEnd();
	glBegin(GL_LINES);
		glVertex2i(x,wh - y);
		glVertex2i(x+32, wh - y);
    glEnd();

		
			
			//draw out put line
			glBegin(GL_LINES);
			     glVertex2f(x+56,wh-y+24);
                 glVertex2f(x+76,wh-y+24);
            glEnd();

			 //draw the input lines
			 glBegin(GL_LINES);
			      glVertex2f(x,wh-y+10);
                  glVertex2f(x-20,wh-y+10);
             glEnd();
			 glBegin(GL_LINES);
			      glVertex2f(x,wh-y+37);
                  glVertex2f(x-20,wh-y+37);
             glEnd();
}
void drawArcOrCircle(int x, int y, int r, char arcOrCircle){

	float cx, cy, sides, ax, ay,tx,ty,final;

	int i;

	float theta, tangetial_factor,radial_factor;

	sides = 40;

	cx = x+32;
	cy = wh - y+24;

	if(arcOrCircle == 'a'){

		ax = r* cosf(1.57079633);

		ay = r * sinf(1.57079633)+1; 

		theta =  -3.3415926 / sides; 
	
	
	}else{
		ax = r;

		ay = 0; 
		theta =  -3.3415926 *2 / sides; 
	}
	
	tangetial_factor = tanf(theta);//calculate the tangential factor 

	radial_factor = cosf(theta);//calculate the radial factor 
	
	
		

	topY = ay + cy;
    
	glBegin(GL_LINE_STRIP); 
	for(i = 0; i < sides; i++) 
	{ 
		glVertex2f(ax + cx, ay + cy);//output vertex 
        
		//calculate the tangential vector 
		//remember, the radial vector is (x, y) 
		//to get the tangential vector we flip those coordinates and negate one of them 

		tx = -ay; 
		ty = ax; 
        
		//add the tangential vector 

		ax += tx * tangetial_factor; 
		ay += ty * tangetial_factor; 
       
		//correct using the radial factor 

		ax *= radial_factor; 
		ay *= radial_factor; 
	} 
	glEnd();

	bottomY = ay+cy;
}

int checkXandY(int x, int y, char oOrI,char click){
	int i;
	if(oOrI == 'o'){
		for(i = 0; i < 150; i ++){
			if(unConnectedOutput[i][0] != 0 && unConnectedOutput[i][1] != 0){
				if((unConnectedOutput[i][0]+6 >= x && unConnectedOutput[i][0]-6 <= x) && (unConnectedOutput[i][1]+6 >= y && unConnectedOutput[i][1]-6 <= y)){
					
					if(click == 'y'){
						selectedX = unConnectedOutput[i][0];
						selectedY = unConnectedOutput[i][1];
					}else{
						viewedX = unConnectedOutput[i][0];
						viewedY = unConnectedOutput[i][1];
					}
					return 1;
				}
			}
		}

	}else{
		for(i = 0; i < 150; i ++){
					if(unConnectedInput[i][0] != 0 && unConnectedInput[i][1] != 0){
						if((unConnectedInput[i][0]+6 >= x && unConnectedInput[i][0]-6 <= x) && (unConnectedInput[i][1]+6 >= y && unConnectedInput[i][1]-6 <= y)){
							if(click == 'y'){
								selectedX = unConnectedInput[i][0];
								selectedY = unConnectedInput[i][1];
							}else{
								viewedX = unConnectedInput[i][0];
								viewedY = unConnectedInput[i][1];
							}
							return 1;
						}
					}
				}
	}

	return 0;
}

void screen_box(int x, int y, int s )
{
    glBegin(GL_QUADS);
      glVertex2i(x, y);
      glVertex2i(x+s, y);
      glVertex2i(x+s, y+s);
      glVertex2i(x, y+s);
    glEnd();
}

void right_menu(int id)
{
   int i,j;

   if(id == 1) exit(0);
   else if(id == 2){
	   
	   for(i = 0; i <50; i++){
		   for(j = 0; j < 2; j++){
			savedANDgates[i][j] = 0;
			savedORgates[i][j] = 0;
			savedNOTgates[i][j] = 0;
		   }
	   }

	   for(i = 0; i < 150; i++){
		   for(j = 0; j < 2; j++){
		   unConnectedOutput[i][j] = 0;
			connectedOutput[i][j] = 0;
			unConnectedInput[i][j]= 0;
			connectedInput[i][j]= 0;
		   }
	   }

   display();
   }
}

void create_menu(int id){
	if(id == 1){

		draw_mode = ANDGATE;

	}else if(id == 2){

		draw_mode = ORGATE;

	}else{
		draw_mode = NOTGATE;
	}

	glutSetCursor(GLUT_CURSOR_NONE);
}

void color_menu(int id)
{
   if(id == 1) {r = 1.0; g = 0.0; b = 0.0;}
   else if(id == 2) {r = 0.0; g = 1.0; b = 0.0;}
   else if(id == 3) {r = 0.0; g = 0.0; b = 1.0;}
   else if(id == 4) {r = 0.0; g = 1.0; b = 1.0;}
   else if(id == 5) {r = 1.0; g = 0.0; b = 1.0;}
   else if(id == 6) {r = 1.0; g = 1.0; b = 0.0;}
   else if(id == 7) {r = 0.0; g = 0.0; b = 0.0;}
}

void myMouse(int x, int y){
	int i = 0,mY;
	mY = wh - y;
	display();

	if(draw_mode == ANDGATE){

		
		glPushAttrib(GL_ALL_ATTRIB_BITS);
		glutSwapBuffers();
		
		glColor3f(r, g, b);
		drawANDgate(x,y);
		
		glutSwapBuffers();

		glPopAttrib();
	}else if(draw_mode == ORGATE ){
		
		glPushAttrib(GL_ALL_ATTRIB_BITS);
		glutSwapBuffers();
		
		glColor3f(r, g, b);
		drawORgate(x,y);
		
		glutSwapBuffers();

		glPopAttrib();

	}else if(draw_mode == NOTGATE ){
		
		glPushAttrib(GL_ALL_ATTRIB_BITS);
		glutSwapBuffers();
		
		glColor3f(r, g, b);
		drawNOTgate(x,y);
		
		glutSwapBuffers();

		glPopAttrib();
	}else if(draw_mode == LINE){

		if( checkXandY(x,mY,'i','n') == 1){

			display();
				glColor3f(0.6, 0.6, 0);
					glBegin(GL_LINE_LOOP); 

						glVertex2i(viewedX -6, viewedY-6);

						glVertex2i(viewedX -6, viewedY+6);

						glVertex2i(viewedX +6, viewedY+6);

						glVertex2i(viewedX +6, viewedY-6);

					glEnd();

					glColor3f(1, 0, 0);
					glBegin(GL_LINES); 
						glVertex2i(oldX,oldY);
						glVertex2i(viewedX,viewedY);
					glEnd();
			

					glutSwapBuffers();
		}else{

		glPushAttrib(GL_ALL_ATTRIB_BITS);
		glutSwapBuffers();
		glColor3f(1, 0, 0);

		glBegin(GL_LINES); 
                 glVertex2i(selectedX,selectedY);
                 glVertex2i(x,wh-y);
              glEnd();
		glutSwapBuffers();

		glPopAttrib();
		}
	}
	

	mouseX = x;
	mouseY = y;

	
	
	if(draw_mode != LINE){
		if( checkXandY(x,mY,'o','n') == 1){
			glColor3f(0.8, 0.8, 0);
			glBegin(GL_LINE_LOOP); 

				glVertex2i(viewedX -6, viewedY-6);

				glVertex2i(viewedX -6,viewedY+6);

				glVertex2i(viewedX +6, viewedY+6);

				glVertex2i(viewedX +6, viewedY-6);

			glEnd();

			glutSwapBuffers();
		}
	}
}

void display(void)
{
	int shift=0,i,dx;
    glPushAttrib(GL_ALL_ATTRIB_BITS);
    glClearColor (1.0, 1.0, 1.0, 1.0);
    glClear(GL_COLOR_BUFFER_BIT);
	glColor3f(r, g, b);
	for(i = 0; i <50; i++){
		
		if(savedANDgates[i][0] != 0 && savedANDgates[i][1] != 0){
				
				drawANDgate(savedANDgates[i][0],savedANDgates[i][1]);
		}

		if(savedORgates[i][0] != 0 && savedORgates[i][1] != 0){
				
				drawORgate(savedORgates[i][0],savedORgates[i][1]);
		}

		if(savedNOTgates[i][0] != 0 && savedNOTgates[i][1] != 0){
				
				drawNOTgate(savedNOTgates[i][0],savedNOTgates[i][1]);
		}

	}
	glColor3f(1, 0, 0);
	for(i = 0; i < 150; i++){
		if((connectedOutput[i][0] != 0 && connectedOutput[i][1] != 0) && (connectedInput[i][0] != 0 && connectedInput[i][1] != 0)){
		
			glBegin(GL_LINE_STRIP);
				glVertex2i(connectedOutput[i][0],connectedOutput[i][1]);

				dx = (connectedInput[i][0]-connectedOutput[i][0])/2;

				glVertex2i(connectedOutput[i][0]+dx,connectedOutput[i][1]);
				glVertex2i(connectedOutput[i][0]+dx,connectedInput[i][1]);
				glVertex2i(connectedInput[i][0],connectedInput[i][1]);
			glEnd();
		}
	}
	glutSwapBuffers();
	
    glPopAttrib();
}

void control_cb( int control )
{
	
	int i,j;

	if(control == 1)
	{
		draw_mode = ANDGATE;
		glutSetCursor(GLUT_CURSOR_NONE);
	}
	else if(control == 2)
	{
		draw_mode = ORGATE;
		glutSetCursor(GLUT_CURSOR_NONE);
	}
	else if(control == 3 )
	{
		draw_mode = NOTGATE;
		glutSetCursor(GLUT_CURSOR_NONE);
	}
	else if(control == 4){
	
		colorGlui->show();
		

	}
	else if(control == 5){
	
		 for(i = 0; i <50; i++){
		   for(j = 0; j < 2; j++){
			savedANDgates[i][j] = 0;
			savedORgates[i][j] = 0;
			savedNOTgates[i][j] = 0;
		   }
	   }

	   for(i = 0; i < 150; i++){
		   for(j = 0; j < 2; j++){
		   unConnectedOutput[i][j] = 0;
			connectedOutput[i][j] = 0;
			unConnectedInput[i][j]= 0;
			connectedInput[i][j]= 0;
		   }
	   }

	   display();

	}else if(control == 6)
	{
		r = 1.0; g = 0.0; b = 0.0;
		colorGlui->hide();
	}
	else if(control == 7)
	{
		r = 0.0; g = 1.0; b = 0.0;
		colorGlui->hide();
	}
	else if(control == 8)
	{
		r = 0.0; g = 0.0; b = 1.0;
		colorGlui->hide();
	}
	else if(control == 9)
	{
		r = 0.0; g = 1.0; b = 1.0;
		colorGlui->hide();
	}
	else if(control == 10)
	{
		r = 1.0; g = 0.0; b = 1.0;
		colorGlui->hide();
	}
	else if(control == 11)
	{
		r = 1.0; g = 1.0; b = 0.0;
		colorGlui->hide();
	}
	else if(control == 12)
	{
		r = 0.0; g = 0.0; b = 0.0;
		colorGlui->hide();
	}
	else if(control == 13)
	{
		colorGlui->hide();
	}
}

int main(int argc, char** argv)
{
    int c_menu, cr_menu;


    glutInit(&argc,argv);
    glutInitDisplayMode (GLUT_DOUBLE | GLUT_RGB);
	glutInitWindowSize(750, 500);
    main_window = glutCreateWindow("Basic Editor");
    glutDisplayFunc(display);


	myinit ();
    glutReshapeFunc (myReshape); 
	glutPassiveMotionFunc(myMouse);
    glutMouseFunc(mouse);

    c_menu = glutCreateMenu(color_menu);
    glutAddMenuEntry("Red",1);
    glutAddMenuEntry("Green",2);
    glutAddMenuEntry("Blue",3);
    glutAddMenuEntry("Cyan",4);
    glutAddMenuEntry("Magenta",5);
    glutAddMenuEntry("Yellow",6);
    glutAddMenuEntry("Black",7);
	cr_menu = glutCreateMenu( create_menu);
	glutAddMenuEntry("AND gate",1);
    glutAddMenuEntry("OR gate",2);
    glutAddMenuEntry("NOT gate",3);
	glutCreateMenu(right_menu);
    glutAddMenuEntry("Quit",1);
    glutAddMenuEntry("Clear",2);
	glutAddSubMenu("Create",cr_menu);
	glutAddSubMenu("Colors",c_menu);
    glutAttachMenu(GLUT_RIGHT_BUTTON);
    

	//glui code here
	glui = GLUI_Master.create_glui_subwindow( main_window, GLUI_SUBWINDOW_TOP );

	GLUI_Button *ANDgate = new GLUI_Button( glui, "AND gate",1, control_cb);
	new GLUI_Column( glui, false );

	GLUI_Button *ORgate = new GLUI_Button( glui, "OR gate",2, control_cb);
	new GLUI_Column( glui, false );

	GLUI_Button *NOTgate = new GLUI_Button( glui,"NOT gate",3, control_cb );
	new GLUI_Column( glui, false );

	GLUI_Button *colors = new GLUI_Button( glui,"Colors",4, control_cb );
	new GLUI_Column( glui, false );

	GLUI_Button  *clear = new GLUI_Button (glui, "Clear",5 , control_cb );
	new GLUI_Column( glui, false );

	GLUI_Button  *quit = new GLUI_Button ( glui, "Quit",0,(GLUI_Update_CB)exit  );

	glui->set_main_gfx_window( main_window );


	// color menu glui warning if the X button is clicked, the entire program closes

	colorGlui = GLUI_Master.create_glui( "Select a Color", 1, 300, 400 );

	GLUI_Button *red = new GLUI_Button(colorGlui, "Red",6, control_cb);
		

	GLUI_Button *green = new GLUI_Button( colorGlui, "Green",7, control_cb);
		

	GLUI_Button *blue = new GLUI_Button( colorGlui,"Blue",8, control_cb );
	new GLUI_Column( colorGlui, false );

	GLUI_Button *cyan = new GLUI_Button( colorGlui,"Cyan",9, control_cb );


	GLUI_Button  *magenta = new GLUI_Button (colorGlui, "Magenta",10 , control_cb );


	GLUI_Button  *yellow = new GLUI_Button ( colorGlui, "Yellow",11, control_cb );
	new GLUI_Column( colorGlui, false );

	GLUI_Button  *black = new GLUI_Button ( colorGlui, "Black",12, control_cb );

	GLUI_Button  *cancel = new GLUI_Button ( colorGlui, "Cancel",12, control_cb );

	colorGlui->set_main_gfx_window( main_window );
	colorGlui->hide();


    glutMainLoop();
	return 0;
}
