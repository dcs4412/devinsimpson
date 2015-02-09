#reverse lookup function and warp mesh function used to warp a template face mesh into a scan subject face mesh

def runReverseLookup(self):
        #get the texture face coordinates from the face obj
        OBJTextureCoords = self.faceOBJ_storage_Handler.getUnorderedTextCoords()
        OBJFaces = self.faceOBJ_storage_Handler.getFaces()
        OBJVertices = self.faceOBJ_storage_Handler.get_unorder_vertices()

        #get the landmarks on the current let/right panel

        landmarks = self.pointsHandler.getLandMarkList("LR")
        total_points = len(landmarks)
        image_h = float(self.leftRightLandMarkWindow.imageSize.height())
        image_w = float(self.leftRightLandMarkWindow.imageSize.width())
        image_path = self.leftRightLandMarkWindow.imageName
        print"number of texture coordinates: ", len(OBJTextureCoords)
        print "number of faces ", len(OBJFaces)
        print "number of vertices ", len(OBJVertices)
        print "number of landmarks: ", total_points
        print "image size: ", image_w, " and ", image_h, "\n"
        print "Image path: " + image_path

        landmark_matched_texture_coordinates = []
        vertex_indices_from_OBJ_faces_matched_by_texture_coordinates = []
        vertex_coordinates_to_warp_quad_mesh = []

        landmark_quad_mesh_vertex_indices = []
        landmark_coordinates = []

        writeUV_coordinate = []

        #get the indices for the mesh vertices in landmark order
        for i in range(0, total_points):
            if (landmarks[i].trueY > 0.0):
                landmark_quad_mesh_vertex_indices.append(int(landmarks[i].id)+1)
                landmark_coordinates.append([landmarks[i].trueX, landmarks[i].trueY])


        total_points = len(landmark_quad_mesh_vertex_indices)

        #the first step in reverse look up to find the texture coordinates that are closest to the landmarks
        if os.path.exists("face_landmark_coord_matchup.txt"):
            print "saved landmark texture lookup file found! reading..."
            textureCoordSaved = open("face_landmark_coord_matchup.txt",'r')
            for line in textureCoordSaved:
                landmark_matched_texture_coordinates.append(int(line))

            savedUVs = open("save_found_UVs.txt",'r')

            for line in savedUVs:
                values = line.split(" ")
                writeUV_coordinate.append([float(values[0]),float(values[1])])

            savedUVs.close()
            print"finished reading!"
        else:
            print "no saved landmark texture lookup file was found, building new file..."

            for i in range(0, len(landmark_quad_mesh_vertex_indices)):
                distTest = 0.1
                closestCoordIndex = -1
                getCord = None
                for cIndex in range (0,len(OBJTextureCoords)):

                    xd = (landmark_coordinates[i][0]/image_w) - OBJTextureCoords[cIndex][0]
                    yd = ((image_h - landmark_coordinates[i][1])/image_h) - OBJTextureCoords[cIndex][1]
                    dist = math.sqrt((xd*xd) + (yd*yd))
                    if dist < distTest:
                        distTest = dist
                        closestCoordIndex = cIndex
                        getCord = OBJTextureCoords[cIndex]

                if closestCoordIndex > -1:
                    landmark_matched_texture_coordinates.append(closestCoordIndex+1)
                    writeUV_coordinate.append([getCord[0], getCord[1]])

                else:
                    print"Error ! ",  landmark_quad_mesh_vertex_indices[i], " height ", ((image_h - landmark_coordinates[i][1])/image_h) , " width  ",(landmark_coordinates[i][0]/image_w)
                print i ,'/' , total_points,'\r',

            writePoints = open("face_landmark_coord_matchup.txt", "w")
            for coord in landmark_matched_texture_coordinates:
                writePoints.write(str(coord)+"\n")

            writePoints.close()

            savedUVs = open("save_found_UVs.txt", "w");
            for uv in writeUV_coordinate:
                savedUVs.write(str(uv[0])+" "+ str(uv[1]) +"\n")

            savedUVs.close()

            print"finished building!"

        print "length of texture coords ", len(landmark_matched_texture_coordinates),"\n"

        print len(OBJFaces), "  ==  ", len(landmark_matched_texture_coordinates)

        #next step is to use the list of texture coordinate indices to find their associated faces, and use the faces to
        #find the vertex coordinate
        if os.path.exists("face_landmark_vertex_matchup.txt"):
            print "saved landmark vertex lookup file found! reading..."
            vertexCoordSaved = open("face_landmark_vertex_matchup.txt",'r')
            for line in vertexCoordSaved:
                vertex_indices_from_OBJ_faces_matched_by_texture_coordinates.append(int(line))
            print"finished reading!"
        else:
            print("no saved landmark vertex lookup file found, finding vertex coords...")
            for i in range (0,len(landmark_matched_texture_coordinates)):

                found = False
                faceIndex = 0
                while found is False:

                    if (landmark_matched_texture_coordinates[i] == OBJFaces[faceIndex][2][0]):
                        vertex_indices_from_OBJ_faces_matched_by_texture_coordinates.append(OBJFaces[faceIndex][0][0]-1)
                        found = True
                    elif (landmark_matched_texture_coordinates[i]  == OBJFaces[faceIndex][2][1]):
                        vertex_indices_from_OBJ_faces_matched_by_texture_coordinates.append(OBJFaces[faceIndex][0][1]-1)
                        found = True
                    elif (landmark_matched_texture_coordinates[i]  == OBJFaces[faceIndex][2][2]):
                        vertex_indices_from_OBJ_faces_matched_by_texture_coordinates.append(OBJFaces[faceIndex][0][2]-1)
                        found = True

                    faceIndex+=1

                print i," / ", len(landmark_matched_texture_coordinates), '\r',
            writePoints = open("face_landmark_vertex_matchup.txt", "w")
            for coord in vertex_indices_from_OBJ_faces_matched_by_texture_coordinates:
                writePoints.write(str(coord)+"\n")

            writePoints.close()
            print"found all vertices!"


        print "number of vertex coords " , len(vertex_indices_from_OBJ_faces_matched_by_texture_coordinates), "\n"

        #now use the vertex coordinates to build a list of the vertex coordinates at the given index, we will use this
        #3d coordinate to warp our quad mesh
        print "building list of selected face vertices "

        finalUV = []
        final_vertex_indices = []

        middleIDs =[]
        middleRightUVs =[]
        middleLeftUVs =[]

        middleVertex = []

        for i in range(0, total_points):

            if(i < total_points / 2):

                if(landmark_quad_mesh_vertex_indices[i] == landmark_quad_mesh_vertex_indices[(total_points/2)+i]):
                    middleIDs.append(landmark_quad_mesh_vertex_indices[i])
                    middleLeftUVs.append([writeUV_coordinate[i][0] + 0.006, writeUV_coordinate[i][1]])

                    #get the average of the two found middle vertex coordinates(one from left side, one from right)
                    middleVertex.append([(OBJVertices[vertex_indices_from_OBJ_faces_matched_by_texture_coordinates[i]][0] +
                                        OBJVertices[vertex_indices_from_OBJ_faces_matched_by_texture_coordinates[i+(total_points/2)]][0])/2,
                                         (OBJVertices[vertex_indices_from_OBJ_faces_matched_by_texture_coordinates[i]][1] +
                                        OBJVertices[vertex_indices_from_OBJ_faces_matched_by_texture_coordinates[i+(total_points/2)]][1])/2,
                                         (OBJVertices[vertex_indices_from_OBJ_faces_matched_by_texture_coordinates[i]][2] +
                                        OBJVertices[vertex_indices_from_OBJ_faces_matched_by_texture_coordinates[i+(total_points/2)]][2])/2
                                         ])
                else:
                    final_vertex_indices.append(landmark_quad_mesh_vertex_indices[i])
                    finalUV.append(writeUV_coordinate[i])

                    vertex_coordinates_to_warp_quad_mesh.append(OBJVertices[vertex_indices_from_OBJ_faces_matched_by_texture_coordinates[i]])

            else:

                if(landmark_quad_mesh_vertex_indices[i] == landmark_quad_mesh_vertex_indices[i - (total_points/2)]):
                     middleRightUVs.append([writeUV_coordinate[i][0] - 0.0063, writeUV_coordinate[i][1]])
                else:
                    final_vertex_indices.append(landmark_quad_mesh_vertex_indices[i])
                    finalUV.append(writeUV_coordinate[i])

                    vertex_coordinates_to_warp_quad_mesh.append(OBJVertices[vertex_indices_from_OBJ_faces_matched_by_texture_coordinates[i]])


        print "number of new vertex coordinates ", len(vertex_coordinates_to_warp_quad_mesh)

        #now warp mesh with vertex coordinates and indices
        print"warping mesh..."
        self.warpMesh(vertex_coordinates_to_warp_quad_mesh, middleVertex,final_vertex_indices, finalUV, middleIDs, middleRightUVs, middleLeftUVs, image_path, image_h, image_w)
		
		  #warp the base mesh using scan vertices found from the current subject's found UVs
    def warpMesh(self, newCoords, newCoordsMiddle ,quadMeshIndices, newTextureCoords, middleIDs, middleUVsRight, middleUVsLeft, imagePath, image_height, image_width):

        #step through the list of vertex indices (as they are drawn in openGL) note, every 4 indices constitutes a face
        for i in range(0, len(self.vertex_indices)/4):

            #make temporary storage for the current face we are on
            currentFace = []

            #establish the start of the current face we are on
            currentStartIndex = i * 4

            #add the indices of the face to the temp storage
            currentFace.append(self.vertex_indices[currentStartIndex])
            currentFace.append(self.vertex_indices[currentStartIndex+1])
            currentFace.append(self.vertex_indices[currentStartIndex+2])
            currentFace.append(self.vertex_indices[currentStartIndex+3])

            #set up a boolean to test if we are on the left side of the face or the right side of the face
            #(relevant for middle line UVs)
            isRight = None

            #set up another boolean tracking if a middle index exists in the face
            hasMiddleIndex = False

            #step through the current face and check which side of the face the (planar face) is on
            for vI in currentFace:

                #the left side of the face will have an index value smaller than the first middle index
                if(vI < middleIDs[0]):

                    #set isRight if it has not been set
                    if(isRight == None):
                        isRight = False

                #the middle vertices are too scatted to use the same test for the right side face check if
                #index is not in the middle ids, that means the index is on the right side
                else:
                    if(vI not in middleIDs):
                        #set isRight if it has not been set
                        if(isRight == None):
                            isRight = True

                    #this indicate that a middle index is in the current face
                    else:
                        hasMiddleIndex = True

            #is there is not middle index, do a simple swapping of current vertices and UVs
            if(hasMiddleIndex is False):

                #step through the current face and update the vertices and UVs
                for vI in range(0, len(currentFace)):

                    #find the index of the vertex and UV in the list of updated IDs and update the lists of mesh vertices and UVs
                    newItemIndex = quadMeshIndices.index(currentFace[vI])
                    self.setNewVertexAndUV( newItemIndex, vI, currentStartIndex, newCoords, newTextureCoords)

            #if a middle index is present, go through and handle the UV selection based on left or right face
            else:

                #step through the current face and update the vertices and UVs
                for vI in range(0, len(currentFace)):

                    #if the current index is the middle index
                    if(currentFace[vI] in middleIDs):

                        #use the left side UVs if the this is a left side face
                        if(isRight is False):

                            #find the index of the vertex and UV in the list of updated IDs and update the lists of mesh vertices and UVs
                            newItemIndex = middleIDs.index(currentFace[vI])
                            self.setNewVertexAndUV( newItemIndex, vI, currentStartIndex, newCoordsMiddle, middleUVsLeft)

                         #use the right side UVs if the this is a right side face
                        else:

                            #find the index of the vertex and UV in the list of updated IDs and update the lists of mesh vertices and UVs
                            newItemIndex = middleIDs.index(currentFace[vI])
                            self.setNewVertexAndUV( newItemIndex, vI, currentStartIndex, newCoordsMiddle, middleUVsRight)

                    #if this is not a middle index, apply the new vertex/UV as normal
                    else:

                        #find the index of the vertex and UV in the list of updated IDs and update the lists of mesh vertices and UVs
                        newItemIndex = quadMeshIndices.index(currentFace[vI])
                        self.setNewVertexAndUV( newItemIndex, vI, currentStartIndex, newCoords, newTextureCoords)


        #bind the changes to the vertex list
        GL.glBindBuffer(GL.GL_ARRAY_BUFFER, self.v_id)
        GL.glBufferSubData(GL.GL_ARRAY_BUFFER, 0,  self.vertices.size*4, self.vertices)
        GL.glBindBuffer(GL.GL_ARRAY_BUFFER, 0)

        #bind the changes to the texture coordinate (UV) list
        GL.glBindBuffer(GL.GL_ARRAY_BUFFER, self.t_id)
        GL.glBufferSubData(GL.GL_ARRAY_BUFFER,0, self.textureCoordinates.size*4,  self.textureCoordinates)
        GL.glBindBuffer(GL.GL_ARRAY_BUFFER, 0)

        #build the object and update the scene
        self.object = self.makeObject()
        self.updateGL()
											 
						