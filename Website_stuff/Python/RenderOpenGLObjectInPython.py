#some functions used to render a mesh in OpenGL with vertex buffer objects in Python

#sending model data to gpu 
    def bind(self):

        #get the obj file lists
        vertexCoords = OBJObject.getVerticesOrdered()
        textureCoordsCoords = OBJObject.getTextureCoordsOrdered()
        normalCoords = OBJObject.getNormalsOrdered()

        #the vertex/texture coordinates/normal list needs to be converted to numpy to be sent to the buffer
        self.v_id = GL.glGenBuffers(1)
        self.n_id = GL.glGenBuffers(1)
        self.t_id = GL.glGenBuffers(1)

        self.vertices = numpy.array(vertexCoords, dtype='float32')
        self.textureCoordinates = numpy.array(textureCoordsCoords, dtype='float32')
        self.normals = numpy.array(normalCoords, dtype='float32')

        #buffer the data
        if self.hasTextureCoords is True:
            GL.glBindBuffer(GL.GL_ARRAY_BUFFER, self.t_id)
            GL.glBufferData(GL.GL_ARRAY_BUFFER, self.textureCoordinates, GL.GL_DYNAMIC_DRAW)
            GL.glTexCoordPointer(2,GL.GL_FLOAT, 0,None)
            GL.glBindBuffer(GL.GL_ARRAY_BUFFER, 0)

        if self.hasNormals is True:
            GL.glBindBuffer(GL.GL_ARRAY_BUFFER, self.n_id)
            GL.glBufferData(GL.GL_ARRAY_BUFFER, self.normals, GL.GL_DYNAMIC_DRAW)
            GL.glNormalPointer(GL.GL_FLOAT, 0,None)
            GL.glBindBuffer(GL.GL_ARRAY_BUFFER, 0)

        GL.glBindBuffer(GL.GL_ARRAY_BUFFER, self.v_id)
        GL.glBufferData(GL.GL_ARRAY_BUFFER, self.vertices, GL.GL_DYNAMIC_DRAW)
        GL.glVertexPointer(3, GL.GL_FLOAT, 0, None)
        GL.glBindBuffer(GL.GL_ARRAY_BUFFER, 0)
		
		
	#draw the 3d object here
    def renderMesh(self):

        generateList  = GL.glGenLists(1)
        GL.glNewList(generateList, GL.GL_COMPILE)

        #get ready to draw the models
        GL.glEnableClientState(GL.GL_VERTEX_ARRAY)
        if self.hasTextureCoords is True:

            GL.glEnableClientState(GL.GL_TEXTURE_COORD_ARRAY)
        if self.hasNormals is True:
            GL.glEnableClientState(GL.GL_NORMAL_ARRAY)

        shape = [ GL.GL_TRIANGLES, GL.GL_QUADS, GL.GL_POLYGON][self.facePolygon - 3]

        if self.meshVisible is True:

            GL.glPolygonMode(GL.GL_FRONT_AND_BACK, GL.GL_FILL)

            GL.glColor3f(self.meshColor.redF(), self.meshColor.greenF(), self.meshColor.blueF())

            if self.lightingOn is True:
                GL.glEnable(GL.GL_LIGHTING)
                GL.glEnable(GL.GL_LIGHT0)

            #if there is a texture, bind it to the mesh
            if self.isTextured is True:
                GL. glBindTexture(GL.GL_TEXTURE_2D, self.texture_id)


            GL.glDrawArrays(shape, 0, self.numberOfVertices)

            #once that texture has been bounded to the mesh, remove the bind so nothing else gets textured (wireframe, vertices, ect...)
            if self.isTextured is True:
                GL. glBindTexture(GL.GL_TEXTURE_2D, 0)

            if self.lightingOn is True:
                GL.glDisable(GL.GL_LIGHTING)
                GL.glDisable(GL.GL_LIGHT0)

        if self.wireFrameVisible is True:

            GL.glPolygonMode(GL.GL_FRONT_AND_BACK, GL.GL_LINE)
            GL.glEnable(GL.GL_POLYGON_OFFSET_LINE)
            GL.glPolygonOffset(-1.0, -1.0)
            GL.glLineWidth(self.wireFrameThickness)
            GL.glColor3f(self.wireFrameColor.redF(), self.wireFrameColor.greenF(), self.wireFrameColor.blueF())

            GL.glDrawArrays(shape, 0, self.numberOfVertices)

            GL.glDisable(GL.GL_POLYGON_OFFSET_LINE)

        if self.verticesVisible is True:

            GL.glPolygonMode(GL.GL_FRONT_AND_BACK, GL.GL_POINT)
            GL.glPointSize(self.vertexSize)
            GL.glEnable(GL.GL_POLYGON_OFFSET_POINT)
            GL.glPolygonOffset(-2.0, -2.0)

            GL.glColor3f(self.vertexColor.redF(), self.vertexColor.greenF(), self.vertexColor.blueF())
            GL.glDrawArrays(shape, 0, self.numberOfVertices)
            GL.glDisable(GL.GL_POLYGON_OFFSET_POINT)

        GL.glDisableClientState(GL.GL_VERTEX_ARRAY)
        if self.hasTextureCoords is True:
            GL.glDisableClientState(GL.GL_TEXTURE_COORD_ARRAY)
        if self.hasNormals is True:
            GL.glDisableClientState(GL.GL_NORMAL_ARRAY)

        GL.glEndList()
        return generateList
		
		
	 #apply a texture to the current obj
    def loadTexture(self, imageFile):

        #enable textures for the model
        GL.glEnable(GL.GL_TEXTURE_2D)

        #set the texture ID
        self.texture_id = GL.glGenTextures(1)

        #set the raw pixel data and put it into a numpy array
        int_img = imageFile.convert("RGBA")
        ix, iy, image = int_img.size[0], int_img.size[1], int_img.tostring("raw", "RGBA", 0, -1)

        #bind the image and set the id to the current texture ID
        GL.glBindTexture(GL.GL_TEXTURE_2D,  self.texture_id)
        GL.glPixelStorei(GL.GL_UNPACK_ALIGNMENT,1)


        GL.glTexImage2D(GL.GL_TEXTURE_2D, 0, 3 , ix,
                        iy,  0, GL.GL_RGBA, GL.GL_UNSIGNED_BYTE, image)

        GL.glTexParameterf(GL.GL_TEXTURE_2D, GL.GL_TEXTURE_WRAP_S, GL.GL_REPEAT)
        GL.glTexParameterf(GL.GL_TEXTURE_2D, GL.GL_TEXTURE_WRAP_T, GL.GL_REPEAT)
        GL.glTexParameterf(GL.GL_TEXTURE_2D, GL.GL_TEXTURE_MAG_FILTER,GL.GL_LINEAR)
        GL.glTexParameterf(GL.GL_TEXTURE_2D, GL.GL_TEXTURE_MIN_FILTER, GL.GL_LINEAR)

        GL.glTexEnvf(GL.GL_TEXTURE_ENV, GL.GL_TEXTURE_ENV_MODE, GL.GL_DECAL)

        #set off the bind function
        GL.glBindTexture(GL.GL_TEXTURE_2D,  0)

        #update textured status
        self.isTextured = True

        #render the scene
        self.object = self.renderMesh()
        self.updateGL()
		
	#handle window resize events
    def resizeGL(self, width, height):

        #to keep the ratio from dividing by 0, never allow width to be 0
        if width == 0:
            width = 50

        if height == 0:
            height = 50

        #set the view port and model mode
        GL.glViewport(0, 0, width, height)
        GL.glMatrixMode(GL.GL_PROJECTION)
        GL.glLoadIdentity()

        #establish the z boundaries
        self.zNear = 1.0
        self.zFar = 250000.0

        fov = (math.atan(float(height)*0.5)/self.zNear)*0.5

        aspect = float(width)/float(height)

        top = math.tan(fov*0.5) * self.zNear
        bottom = - top
        left = aspect * bottom
        right = aspect * top

        #keep the scene ratio constant during resizing using this ratio on glFrustum
        GL.glFrustum(left,right,bottom,top,self.zNear,self.zFar)
        GL.glMatrixMode(GL.GL_MODELVIEW)
		
	#apply transformations to mesh
    def paintGL(self):

        #set background color
        GL.glClearColor(0.2,0.2,0.2,0)
        GL.glClear(GL.GL_COLOR_BUFFER_BIT | GL.GL_DEPTH_BUFFER_BIT)
        GL.glLoadIdentity()

        #apply current transformation
        GL.glTranslatef(self.xTranslate,self.yTranslate,  self.zTranslate)
        #apply current zoom
        GL.glScalef(self.zoom, self.zoom, 1)

        #apply current rotation
        GL.glRotated(self.yRotate / 16.0, 1.0, 0.0, 0.0)
        GL.glRotated(self.xRotate / 16.0, 0.0, 1.0, 0.0)   

        #render scene
        if self.object is not 0:
            GL.glCallList(self.object)

		
	