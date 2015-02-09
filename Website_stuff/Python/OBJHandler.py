#####################################################################
# keep track of all OBJ commands                                    #
# # -> comment line                                                 #
# v: x y z -> vertex                                                #
# vt: u v [w] -> vertex texture                                     #
# vn: x y z -> vertex normal                                        #
# f: v1/vt1/vn1 v2/vt2/vn2 v3/vt3/vn3 ... -> face                   #
# g: name -> group name                                             #
# usemtl: name -> material                                          #
# s: number -> group number                                         #
#####################################################################
import os
import math
class OBJHandler():

    def __init__(self):
        self.models = []
        self.faces = []
        self.vertex_indices = []

        #multiple models can reference the same vertex, texture coordinates, and normals, so create a general pool for all of them
        self.vertices = []
        self.textureCoordinates = []
        self.normals = []
        self.textureCoordinates_indices = []
        self.normals_indices = []

        self.vbo_indices = []

        self.runningXTotal = 0.0

        self.OBJName = None

        self.hasNormals = True
        self.hasTextureCoords = True

    def getOBJName(self):
        return self.OBJName

    def getUnorderedTextCoords(self):
        return self.textureCoordinates

    def getModels(self):
        return self.models

    def getVertex_Coords(self):
        return self.vbo_v

    def getNormal_Coords(self):
        return self.vbo_n

    def getTextureCoord_Coords(self):
        return self.vbo_t

    def getVertexID(self):
        return self.vertexIDs

    def getFaces(self):
        return self.faces

    def getVBO_indices(self):
        return self.vbo_indices

    def getVertexIndices(self):
        return self.vertex_indices

    def getshape_type(self):
        return self.shapeSelect

    def get_unorder_vertices(self):
        return self.vertices

    def getXmean(self):
        return (self.runningXTotal/float(len(self.vertices)))

    #inital build will ignore group names and group numbers
    def read(self, fileName, scale, angleX,translate):

        self.OBJName = os.path.basename(fileName)[:-4]

        for line in open(fileName,'rb' ,buffering=(2<<16) + 8):

            l = len(self.vertices)

            #ignore comments
            if line.startswith('#'): continue

            #get line data
            currentCommand = line.split()

            #ignore emtpy lines
            if not currentCommand: continue

            #current line has a vertex command
            if currentCommand[0] == 'v':
                item = map(float, currentCommand[1:4])
                scaledItem = [item[0] * scale, item[1] * scale, item[2] * scale]
                rotatedItemByX = [scaledItem[0], scaledItem[1] * math.cos(angleX) + scaledItem[2] * math.sin(angleX), scaledItem[1] * math.sin(angleX)*-1 + scaledItem[2] * math.cos(angleX)]
                translatedItem = [rotatedItemByX[0] + translate[0], rotatedItemByX[1] + translate[1], rotatedItemByX[2]+translate[2]]
                self.runningXTotal = self.runningXTotal +item[0]
                self.addVertex(translatedItem)

            #current line has a texture coordinate command
            elif currentCommand[0] == 'vt':
                item = map(float, currentCommand[1:3])
                self.addTextCoordinate(item)

            #current line has a normal command
            elif currentCommand[0] == 'vn':
                item = map(float, currentCommand[1:4])
                self.addNormal(item)

            #current line has a face command
            elif currentCommand[0] == 'f':
                vertices = []
                texCoor = []
                normals = []

                #each part of the face must be added correctly
                for c in currentCommand[1:]:
                    s = c.split('/')
                    vertices.append(int(s[0]))
                    if len(s) >= 2 and len(s[1]) > 0:
                        texCoor.append(int(s[1]))
                    else:
                        texCoor.append(0)
                    if len(s) >= 3 and len(s[2]) > 0:
                        normals.append(int(s[2]))
                    else:
                        normals.append(0)

                self.addFace((vertices, normals, texCoor))

        #add the final model to the list and return the model list

        self.processLists_forVBO()

    #process the obj data for use by a vertex buffer object
    def processLists_forVBO(self):

        self.vbo_v = None
        self.vbo_t = None
        self.vbo_n = None

        self.shapeSelect = None

        self.indices = []
        optimizedVertices = []
        optimizedTextureCoords = []
        optimizedNormals = []
        self.vertexIDs = []

        current_v_indices = []

        for face in self.faces:

            #get the face component indices from the face tuple
            vertices, normals, textureCoordinates = face

            vertex_l = len(optimizedVertices)
            tc_l = len(optimizedTextureCoords)
            normal_l = len(optimizedNormals)

            #figure out if the face polygon is a triangle or quad
            if len(vertices) == 3:
                self.shapeSelect = 0
            elif len(vertices) == 4:
                self.shapeSelect = 1
            else:
                self.shapeSelect = 2

            #rebuild the list of vertices, normals and textureCoordinates in the order for OpenGL to read it as a
            #vertex buffer object
            for v, vtc, vn in zip(vertices, textureCoordinates, normals):

                oVertex = tuple(self.vertices[v -1])
                self.vertex_indices.append(v)
                oTexCoord = None
                oNormal = None
                if textureCoordinates[0]:
                    oTexCoord = tuple(self.textureCoordinates[vtc - 1])
                else:
                    self.hasTextureCoords = False
                    oTexCoord = (0., 0.)
                if normals[0] :
                    oNormal = tuple(self.normals[vn - 1])
                else:
                    self.hasNormals = False
                    oNormal = (0.,0.,0.)

                optimizedVertices.append(oVertex)
                optimizedTextureCoords.append(oTexCoord)
                optimizedNormals.append(oNormal)

            current_v_indices.append((vertex_l, len(vertices)))
        self.setVBO_indices(current_v_indices)

        self.vbo_v = optimizedVertices
        self.vbo_t = optimizedTextureCoords
        self.vbo_n = optimizedNormals


        #currently no use for ordered list of normals so delete it
        del self.normals



    def clear(self):
        self.models = []
        self.vertices = []
        self.textureCoordinates = []
        self.normals = []
        self.vbo_indices = []
        self.faces = []
        self.vertex_indices = []
        self.textureCoordinates_indices = []
        self.normals_indices = []
        self.runningXTotal = 0.0
        self.OBJName = None

    def addVertex(self, v):
        self.vertices.append(v)

    def addTextCoordinate(self, tc):
        self.textureCoordinates.append(tc)

    def addNormal(self, n):
        self.normals.append(n)

    def addFace(self, f):
        self.faces.append(f)

    def hasNormals(self):
        return self.hasNormals

    def setVBO_indices(self, vbo_i):
        self.vbo_indices = vbo_i

    def hasTextureCoords(self):
        return self.hasTextureCoords


    #write a new obj file
    def write(self, name, threeDVertices, uvTextures, normals, faces):

        newOBJ = open(name+".obj", 'w')

        for vertex in threeDVertices:
            newOBJ.write("v " + str(vertex[0]) + " " + str(vertex[1]) + " " + str(vertex[2]) + "\n")

        for uv in uvTextures:
            newOBJ.write("vt " + str(uv[0]) + " " + str(uv[1]) + "\n")

        for normal in normals:
            newOBJ.write("vn " + str(normal[0]) + " " + str(normal[1]) + " " +  str(normal[2]) + "\n")

        for face in faces:
            newOBJ.write("f " + str(face[0][0]) + "/" + str(face[2][0]) + "/" +  str(face[1][0]) + " "+
            str(face[0][1]) + "/" + str(face[2][1]) + "/" +  str(face[1][1]) + " " +
            str(face[0][2]) + "/" + str(face[2][2]) + "/" +  str(face[1][2]) + " " +
            str(face[0][3]) + "/" + str(face[2][3]) + "/" +  str(face[1][3]) + "\n")


        newOBJ.close()






