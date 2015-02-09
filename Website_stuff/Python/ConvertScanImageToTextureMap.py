#function to use a scan mesh uv to convert a scan subject image to a standard texture map format 

		newTexture = Image.new('RGB',(4096 , 4096 ),'white')
        scanUVimage = Image.new('RGB',(4096 , 4096 ),'white')
        templateUVimage = Image.new('RGB',(4096 , 4096 ),'white')

        scanTexture = Image.open(imagePath)

        ##BUILD TEXTURE MAP 

        self.generateTextureMap(newTexture, scanTexture, image_height, image_width)
		
	def generateTextureMap(self, newTexture, scannedTexture, image_height, image_width):
		
		#get the scan subject uv coordinates in a list and set up the new and old picture for pixel transfer
        scanUVs = scanUVtextureCoordinates.tolist()
        scannedPixels = scannedTexture.load()
        newPixels = newTexture.load()

        lengthConstant = str(len(self.templateTextureCoordinates)/4)

		#step through each uv quad for the pixel transfer
        for i in range(0, len(self.templateTextureCoordinates)/4):

            #build the template uv quad

            print("quad " + str(i) + " of " + lengthConstant)

            templateUVOneX = int((self.templateTextureCoordinates[i * 4][0] * 4096))
            templateUVTwoX = int((self.templateTextureCoordinates[i * 4 + 1][0] * 4096))
            templateUVThreeX = int((self.templateTextureCoordinates[i * 4 + 2][0] * 4096))
            templateUVFourX = int((self.templateTextureCoordinates[i * 4 + 3][0] * 4096))

            templateUVOneY = int(((1 - self.templateTextureCoordinates[i * 4][1]) * 4096))
            templateUVTwoY = int(((1 - self.templateTextureCoordinates[i * 4 + 1][1]) * 4096))
            templateUVThreeY = int(((1 - self.templateTextureCoordinates[i * 4 + 2][1]) * 4096))
            templateUVFourY = int(((1 - self.templateTextureCoordinates[i * 4 + 3][1]) * 4096))


            #build the scan uv quad
            scanUVOneX = int((scanUVs[i * 4][0] * image_width))
            scanUVTwoX = int((scanUVs[i * 4 + 1][0] * image_width))
            scanUVThreeX = int((scanUVs[i * 4 + 2][0] * image_width))
            scanUVFourX = int((scanUVs[i * 4 + 3][0] * image_width))

            scanUVOneY = int(((1 - scanUVs[i * 4][1]) * image_height))
            scanUVTwoY = int(((1 - scanUVs[i * 4 + 1][1]) * image_height))
            scanUVThreeY = int(((1 - scanUVs[i * 4 + 2][1]) * image_height))
            scanUVFourY = int(((1 - scanUVs[i * 4 + 3][1]) * image_height))

            #make the scan UV bounding box
            scanMinX = min(scanUVOneX, scanUVTwoX, scanUVThreeX, scanUVFourX)
            scanMinY = min(scanUVOneY, scanUVTwoY, scanUVThreeY, scanUVFourY)

            scanMaxX = max(scanUVOneX, scanUVTwoX, scanUVThreeX, scanUVFourX)
            scanMaxY = max(scanUVOneY, scanUVTwoY, scanUVThreeY, scanUVFourY)

            bitMapPixelCoordinates = []
            bitMapPixelsUVs = []

            #gather the pixels from the bitmap and store them in a list

            #make UV list
            scanUVQuad = path.Path([ [scanUVOneX, scanUVOneY],
                          [scanUVTwoX, scanUVTwoY],
                          [scanUVThreeX, scanUVThreeY],
                          [scanUVFourX, scanUVFourY] ])

            scanQuad = [ [scanUVOneX, scanUVOneY],
                          [scanUVTwoX, scanUVTwoY],
                          [scanUVThreeX, scanUVThreeY],
                          [scanUVFourX, scanUVFourY] ]

			#for each pixel in the bounding box, check if the pixel is within the current quad, and if so, add the x,y location
			#and find the u,v ratio coordinates for pixel matching	
            for i in range(scanMinX, scanMaxX):

                for j in range(scanMinY, scanMaxY):

                    inUVTestOne = scanUVQuad.contains_point([i,j], radius = -1.0)

                    if(inUVTestOne == 1):

                        bitMapPixelCoordinates.append(scannedPixels[i,j])
                        bitMapPixelsUVs.append(self.bilinear_interpolate([i,j], scanQuad, "SCAN"))

            if len(bitMapPixelCoordinates) == 0 :

                if((scanUVOneX == scanUVTwoX and scanUVTwoX == scanUVThreeX and scanUVThreeX == scanUVFourX) and
                    (scanUVOneY == scanUVTwoY and scanUVTwoY == scanUVThreeY == scanUVFourY)):

                        bitMapPixelCoordinates.append(scannedPixels[scanUVOneX,scanUVOneY])
                        bitMapPixelsUVs.append([1.0,1.0])


            #now find out how may pixels we need to fill the template UV
            #keep track of pixel coordinates that land within the UV quad

            templateUVQuadPixelCoords = []
            templateUVs = []

            #make the bounding box for the template UV
            templateMinX = min(templateUVOneX, templateUVTwoX, templateUVThreeX, templateUVFourX)
            templateMinY = min(templateUVOneY, templateUVTwoY, templateUVThreeY, templateUVFourY)

            templateMaxX = max(templateUVOneX, templateUVTwoX, templateUVThreeX, templateUVFourX)
            templateMaxY = max(templateUVOneY, templateUVTwoY, templateUVThreeY, templateUVFourY)

			#create the quad as a matplotlib path to use the contains function to see if a pixel resides within a quad 
            templateUV = path.Path([ [templateUVOneX, templateUVOneY],
                           [templateUVTwoX, templateUVTwoY],
                           [templateUVThreeX, templateUVThreeY],
                           [templateUVFourX, templateUVFourY] ])

            templateQuad = [ [templateUVOneX, templateUVOneY],
                           [templateUVTwoX, templateUVTwoY],
                           [templateUVThreeX, templateUVThreeY],
                           [templateUVFourX, templateUVFourY] ]
			
			#for each pixel in the bounding box, check if the pixel is within the current quad, and if so, add the x,y location
			#and find the u,v ratio coordinates for pixel matching			
            for i in range(templateMinX, templateMaxX):

                for j in range(templateMinY, templateMaxY):
                    inUV =  templateUV.contains_point([i,j], radius = -1.0)

                    if(inUV == 1):

                        templateUVQuadPixelCoords.append([i,j])
                        templateUVs.append(self.bilinear_interpolate([i,j],templateQuad, "TEMPLATE"))

            closestScanUV = 0


			#for every pixel in the quad we need to fill, find the closet pixel in the scan quad using the uv coordinates
            for j in range (0, len(templateUVs)):

               distanceTest = 100.1428571428571428571428571429
               for k in range (0, len(bitMapPixelsUVs)):
                   xd = math.fabs(templateUVs[j][0] - bitMapPixelsUVs[k][0])
                   yd = math.fabs(templateUVs[j][1] - bitMapPixelsUVs[k][1])

                   dist = (xd*xd) + (yd*yd)

                   if dist < distanceTest:
                       distanceTest = dist
                       closestScanUV = k

               newPixels[templateUVQuadPixelCoords[j][0],templateUVQuadPixelCoords[j][1]] = bitMapPixelCoordinates[closestScanUV]

		#set aside space for image then save new texture as a .jpg image
        ImageFile.MAXBLOCK = 4096 * 4096
        newTexture.save("texture_map_attempt_five.jpg", quality = 100, optimaize = True, progressive = True)

   def bilinear_interpolate(self, point, quad, type):

		#convert the points to float 
        pX = float(point[0])
        pY = float(point[1])

        aX = float(quad[0][0])
        bX = float(quad[1][0])
        cX = float(quad[2][0])
        dX = float(quad[3][0])

        aY = float(quad[0][1]
        bY = float(quad[1][1])
        cY = float(quad[2][1])
        dY = float(quad[3][1])

		
		#transform unit square onto quad
        C = (aY - pY) * (dX - pX) - (aX - pX) * (dY - pY)
        B = (aY - pY) * (cX - dX) + (bY - aY) * (dX - pX) - (aX - pX) * (cY - dY) - (bX - aX) * (dY - pY)
        A = (bY - aY) * (cX - dX) - (bX - aX) * (cY - dY)

        D = math.fabs(B * B - 4 * A * C)

		#now find the the uv ratio coordinates of the pixel within the unit square
       
        u = (-B + math.sqrt(D)) / (2 * A)

        if u < 0.0:
            u = 0.0
        elif u > 1.0:
            u = 1.0

        p1x = aX + (bX - aX) * u
        p2x = dX + (cX - dX) * u
        px = pX

        v = (px - p1x) / (p2x - p1x)

        if v < 0.0:
            v = 0.0
        elif v > 1.0:
            v = 1.0

        return [u,v]
