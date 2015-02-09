class TwoD_FacialPoint:

    #the twoD facial point is used during face registration by
    def __init__(self, x, y, displayX, displayY, size, id):
        self.trueX = x
        self.trueY = y
        self.size = float(size)
        self.displayX = displayX
        self.displayY = displayY
        self.mouseHighlight = False
        self.rubberBandHighlight = False
        self.isGrabbed = False
        self.set = True

        #layout editor creates points before it sets them, give a special status for this
        self.layoutNotYetSet = True

        self.id = id


    #define getter methods

    #true X and Y are the image locations that are written to a points file when registration is complete
    def getTrueX(self):
        return self.trueX

    def getTrueY(self):
        return self.trueY

    #disyplay X and Y are the current location of the point after transformations on the current image view
    def getDisplayX(self):
        return self.displayX

    def getDisplayY(self):
        return self.displayY

    #current size of rendered point
    def getSize(self):
        return self.size

    #boolean if mouse is over point
    def getMouseHighlighted(self):
        return self.mouseHighlight

    #boolean if point is part of a rubberband selection
    def getRubberBandHighlighted(self):
        return self.rubberBandHighlight

    #boolean if point has been grabbed by mouse button hold
    def getIsGrabbed(self):
        return self.isGrabbed

    #id of point
    def getID(self):
        return self.id

    #boolean to determine if point is part of a point layout that user has not set
    def getLayoutNotYetSet(self):
        return self.layoutNotYetSet

    #boolean if current point has not been set
    def getIsSet(self):
        return self.set

    #define setter methods

    #true x and y are only set after they have been moved (not the image being moved)
    #and require being set BEFORE setting display X and y
    def setTrueX(self, x):
        difference = x - self.displayX
        self.trueX = self.trueX + difference

    def setTrueY(self, y):
        difference = y - self.displayY
        self.trueY = self.trueY + difference

    #display x and y are set after ANY movement (image or point)
    def setDisplayX(self, x):
        self.displayX = x

    def setDisplayY(self, y):
        self.displayY = y

    def setMouseHighlighted(self, isHighlighted):
        self.mouseHighlight = isHighlighted

    def setRubberBandHighlighted(self, isHighlighted):
        self.rubberBandHighlight = isHighlighted

    def setIsGrabbed(self,isGrabbed):
        self.isGrabbed = isGrabbed

    def setSize(self, size):
        self.size = float(size)

    def setLayoutNotYetSet(self, setting):
        self.layoutNotYetSet = setting

    def setID(self, id):
        self.id = id

    def setLandmarkUnsetOrSet(self, set):
        self.set = set

    #check if mouse cursor is hovering over point
    def isMouseOverPoint(self, mouseX, mouseY):
        if (float(mouseX) < float(self.displayX) + self.size/2.0 and float(mouseX) > float(self.displayX) - self.size/2.0)  and (float(mouseY) < float(self.displayY) + self.size/2.0 and float(mouseY) > float(self.displayY) - self.size/2.0) :
            return True

        return False