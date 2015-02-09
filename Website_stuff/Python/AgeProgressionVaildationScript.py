#
# Aging App Testing Script and general python toolset
#
# Author: Devin Simpson
#
import subprocess
import csv
import os  
import re
import shutil
import sys
import itertools
import numpy as np
import scipy as sp
import scipy.spatial
import matplotlib as mpl
import matplotlib.pyplot as plt
import matplotlib.ticker as ticker
from matplotlib import cm
from mpl_toolkits.mplot3d import Axes3D
from matplotlib.font_manager import FontProperties

MainPath = "C:\\AgingApp\\Release" #path to AgingApp location, all other path variables are based off this location

ModelsPath = MainPath + "\\Age_Models" #path to the aging models

OutputPath =  MainPath + "\\Output"

AppFilePath = OutputPath + "\\APP_files"

DataPath = MainPath+"\\Data"

StatisticsPath = MainPath + "\\Statistics"

GraphsPath = MainPath + "\\Graphs"



def main():

    #set current directly to the directory containing the agingapp
    os.chdir(MainPath) 

    ##very dangerous function, deletes all generated data so do NOT use after the other functions 
    #reset()

    ##function to automatically construct age models based on image on points files
    #buildAgeModel()

    ##function that creates image directories based off generated data files
    #sortImagesbyExperiment()

    ##function to apply the source image to a synthesized images background
    #reapplySourceImageToSynthesizedImageAsBackground()

    ##function to build a graph of the average ages of a model
    #buildAverageAgeGraph()

    ##function to run openBR on synthesized images and retrieve the face recognition score
    #openBRTests()

    #sort_aged_images()

    ##function to get the first three modes of a age model
    #getfirstThreeModeImages()

    #getEyeCoordinates()

    ##major functions for aging app test script, these functions are responsible for aging images, comparing the reults and printing them
    #______________________# 
    age()
    covarianceMatrices()
    agedImageComparisons()
    printData()
    #______________________# 
    
    
    ##minor functions, not used by major frunctions, but still have limited use
    #______________________# 
    #estimateAges()
    #cleanSHAPEdir()
    #______________________# 

    ##a function to split a directory of images into two directores of only male and only female images 
    #splitMaleFemale()

    print("done")

#function to read all the specified files of a directory to be accessed later
def createFileList(Path):

    fileList = []
    i = 0

    for file in os.listdir(Path):
        if file.endswith(".jpg") | file.endswith(".JPG") | file.endswith(".jpeg") | file.endswith(".JPEG") |  file.endswith(".app")|  file.endswith(".poseapp")|  file.endswith(".pose")|  file.endswith(".texture")|  file.endswith(".shape")|  file.endswith(".txt"):
            
            fileList.insert(i,file)
            i = i + 1
            continue
        else:
            continue

    return fileList

#function that finds the index to the last file of a group of related files
def findLastInd(startInd,fileList):
        currentID = fileList[startInd][0:6]
        for ind in range(startInd,len(fileList)):
            if(currentID != fileList[ind][0:6]):
                return ind
        return len(fileList)

#function to split a directory of male and female images into two directores of male only and female only images
def splitMaleFemale():
    MaterialPath = MainPath + "\\Age_Model_Material\\"

    splitDIR =MaterialPath+ "GEN_252\\Training"



    dirMale = splitDIR + "_Male"
    dirFemale =  splitDIR + "_Female"

    if not os.path.exists(dirMale):
        os.mkdir(dirMale)
    if not os.path.exists(dirFemale):
        os.mkdir(dirFemale)

    i = 0

    for filename in os.listdir(splitDIR):
        
        if(filename[-7] == "M" or filename[-7] == "m"):
            shutil.move(splitDIR+"\\"+filename,dirMale )
        if(filename[-7] == "F" or filename[-7] == "f"):
            shutil.move(splitDIR+"\\"+filename,dirFemale )
 
def invert_and_rescale_results(Path):

     with open (Path, "r") as myfile:
                data = myfile.read().split(" ")

     ind = 0

     max = 0.0

     while( ind+2 < len(data)):
         
         current_score = float(data[ind+2])

         if current_score > max:
             max = current_score

         ind = ind+3

     return max
               
#read the .pts file for the eye coordinates
def getEyeCoordinates():

    coordinates = os.listdir(MainPath + "\\Input\\points")
    
    source_images = createFileList(MainPath + "\\Input\\Test")

    for i in range (0,len(coordinates)):

        file = open( MainPath + "\\Input\\points\\"+coordinates[i])

        coor = file.read()

        startIndex = coor.index('{')+1


        endIndex = coor.index('}')

        

        with file as f:
           f.seek(startIndex+4)
       
           data = f.read(endIndex - startIndex-2).split("\n")

           lEye = data[112]

           rEye = data[180]

           lIndex = lEye.index(' ')

           rIndex = rEye.index(' ')

           lEyeX = lEye[0:lIndex]

           lEyeY = lEye[lIndex+1:len(lEye)]

           rEyeX = rEye[0:rIndex]

           rEyeY = rEye[rIndex+1:len(rEye)]

           imageName = ""

          

           for file in source_images:
               if file.startswith(coordinates[i][:-4]):
                   imageName = file


           imageEyeInfo = [imageName,lEyeX,lEyeY,rEyeX,rEyeY]

           fwrite = open('C:\\PythonFaceEvaluation\\sigsets\\alleyes.csv',  'a', encoding='utf8',newline='')
           wr = csv.writer(fwrite, dialect='excel')

           wr.writerow(imageEyeInfo)
           
#this function will take an age table from a model and construct the average age images from 20 to 80 at 10 year intervals
def buildAverageAgeGraph():

    #first, slected the model whose average ages are desired
    ageModel = "GEN_252_SMALL"
    
    minAge = 18
    
    
	#find the age table
    tableFile =  open( ModelsPath+"\\"+ageModel+".table")

    currentLine = tableFile.readline()

    numRows = 0
    numCols = 0

	#read age data
    while not ("[" in currentLine):

        if "rows" in currentLine:
            numRows =  int(''.join(re.findall('\d+', currentLine)))

        if "cols" in currentLine:
            numCols =  int(''.join(re.findall('\d+', currentLine)))

        
        
        currentLine = tableFile.readline()
        
    tableFile.seek(0)

    file =  tableFile.read()
    startIndex = file.index('[')
	
	#predetermined end index
    endIndex = 131123+2216+32

    tableFile.seek(startIndex)
    startCheck = tableFile.read(1)

    while(startCheck != "["):
        startIndex+=1
        startCheck = tableFile.read(1)
        
    startIndex+=1

    
    with tableFile as tbl:
        tbl.seek(startIndex)
       
        data = tbl.read(endIndex - startIndex).split(",")

    offset = 0

    #create the average age image poseapp files 
    for ind in range(2,9): 

        age = ind * 10
        
        
        #write poseapp file preamble
        f = open("C:\\AgingApp\\Release\\Input\\Modes_and_Ages\\"+ageModel+"_"+"age"+"_"+str(age)+".poseapp", 'a')
        f.write("0.002 ")
        f.write("0.0 ")
        f.write("225.0 ")
        f.write("225.0 ")
        f.close()

        while(offset < numRows):

            currentItem = (age - minAge) + (numCols*offset)

            f = open("C:\\AgingApp\\Release\\Input\\Modes_and_Ages\\"+ageModel+"_"+"age"+"_"+str(age)+".poseapp", 'a')
            f.write(str(data[currentItem])+" ")
            f.close()

            offset+=1


        offset = 0
        #now run aging app to produce the image
        subprocess.call("AgingApp.exe C:\\AgingApp\\Release\\Input\\Modes_and_Ages\\black_background.jpg -g C:\\AgingApp\\Release\\Input\\Modes_and_Ages\\"+ageModel+"_"+"age"+"_"+str(age)+".poseapp " + ModelsPath+"\\"+ageModel+".bin " + ModelsPath+"\\"+ageModel+".table " + ModelsPath+"\\"+ageModel+".svm " + "C:\\AgingApp\\Release\\Input\\Modes_and_Ages\\"+ageModel+"_"+"average"+"_"+str(age)+".jpg " + ":10")

#get the images for the top three models of variation, 7 images for each mode -3, -2, -1, 0, 1 ,2 ,3 standard devations from the mean of the mode 
def getfirstThreeModeImages():

    ageModel = "CAF"

    def getModes():
        subprocess.call("AgingApp.exe [nothingHere] -m " +  ModelsPath+"\\"+ageModel+".bin " + ModelsPath+"\\"+ageModel+".table " + ModelsPath+"\\"+ageModel+".svm " + " " + ageModel + " :10")


    tableFile =  open( ModelsPath+"\\"+ageModel+".table")

    currentLine = tableFile.readline()

   #get the number of rows from the table file so we know how many numbers to put in the .poseapp file

    numRows = 0

    while not ("cols" in currentLine):

        if "rows" in currentLine:
            numRows =  int(''.join(re.findall('\d+', currentLine)))

        currentLine = tableFile.readline()

    

    #now read in the three standard devs we want

    getModes()

    #now create the poseapp file and send it to aging app to generate the image
    data = np.genfromtxt(ModelsPath+"\\model_modes\\"+ageModel+".modes", delimiter = " \n")

    for i in range(0,len(data)):

       for j in range (0,7):

           standardD = float(j)
           s = j

           if j > 3 :
               standardD = (float(j) - 3.0)*-1.0
               s = (j-3)*-1

           poseAppData = [0.0] * numRows

           poseAppData[i] = data[i]*standardD

           f = open(ModelsPath+"\\model_modes\\"+ageModel+"_"+"mode"+"_"+str(i)+"_standard_dev_"+str(s)+".poseapp", 'a')
           f.write("0.002 ")
           f.write("0.0 ")
           f.write("225.0 ")
           f.write("225.0 ")

           for appD in poseAppData:
                f.write("%s " % appD)

           
           f.close()

           subprocess.call("AgingApp.exe C:\\AgingApp\\Release\\Input\\Modes_and_Ages\\black_background.jpg -g "+ ModelsPath+"\\model_modes\\"+ageModel+"_"+"mode"+"_"+str(i)+"_standard_dev_"+str(s)+".poseapp " + ModelsPath+"\\"+ageModel+".bin " + ModelsPath+"\\"+ageModel+".table " + ModelsPath+"\\"+ageModel+".svm " +  ModelsPath+"\\model_modes\\"+ageModel+"_"+"mode"+"_"+str(i) + "_standard_dev_" + str(s) + ".jpg " + ":10")
 
#redraw the synthesized images with the source image as a background
def reapplySourceImageToSynthesizedImageAsBackground():

    #go through this list of Synthesized images and find the ones generated by the original age progression method

    imageList = createFileList(OutputPath+"\\POSEAPP_files")

    sourceImages = createFileList(MainPath + "\\Input\\Test") 
   
    for i in range (0,len(imageList)):
       
        algorthimIndex = 19 #this is where the algorithm part of the image name is 

        algorthimUsed = imageList[i][algorthimIndex:algorthimIndex+8]

        if algorthimUsed == "original":
            
            correctSourceImage = ""

            for sourceImageName in sourceImages :

                if sourceImageName.startswith(imageList[i][0:12]):
                    correctSourceImage = sourceImageName

            modelNameBegin = algorthimIndex + 9

            modelNameEnd = imageList[i].rfind(".")

            modelName = imageList[i][modelNameBegin:modelNameEnd]

            #now run agingapp to get the new image
            subprocess.call("AgingApp.exe C:\\AgingApp\\Release\\Input\\Test\\"+correctSourceImage + " -g C:\\AgingApp\\Release\\Output\\POSEAPP_files\\"+imageList[i] + " " + ModelsPath+"\\"+modelName+".bin " + ModelsPath+"\\"+modelName+".table " + ModelsPath+"\\"+modelName+".svm " + "C:\\AgingApp\\Release\\Output\\Source_Background_Aged_Images(original_only)\\"+ imageList[i][:-8] +".jpg " + ":10")

#some variations in direct texture age progression algorithm produce different images and parameters, move them to separate directories 
def sort_aged_images():

    imageList = createFileList(OutputPath+"\\Aged_Images")

    for filename in imageList:
        if "UPDATE" in filename:
            shutil.copy(OutputPath+"\\Aged_Images\\"+filename,OutputPath+"\\direct_texture_UPDATE")

#run tests using openBR
def openBRTests():
    
    openBRlocation = "C:\\openbr\\build-msvc2012\\install\\bin"

    os.chdir(openBRlocation)  #change the directory we are looking at to the openBR location

    subprocess.call("br.exe")

    def source_experiment():

        sourceExperiments = createFileList(DataPath+"\\\openBR_experiments\\source_image_experiments")

        source_images = createFileList(MainPath + "\\Input\\Test")

        for i in range (0,len(sourceExperiments)):

            with open (DataPath+"\\\openBR_experiments\\source_image_experiments\\"+sourceExperiments[i], "r") as myfile:
                data = myfile.read().split(" ")

            ind = 0

            while( ind+2 < len(data)):

                gender = open(DataPath+"\\\openBR_experiments\\results\\"+sourceExperiments[i][:-4]+"_source_results.txt", 'a')
                general = open(DataPath+"\\\openBR_experiments\\results\\general_source_results.txt", 'a')
                

                youngerImage = "" 

                olderImage = ""

                for file in source_images:

                    if file.startswith(data[ind][:12]):
                        youngerImage = file
                    if file.startswith(data[ind+1][:12]):
                        olderImage = file

                output = subprocess.Popen("br -algorithm FaceRecognition -compare "+ MainPath +"\\Input\\Test\\"+youngerImage +" "+ MainPath +"\\Input\\Test\\"+olderImage,stdout=subprocess.PIPE,stderr=subprocess.PIPE)
                score = re.findall("[-+]?\d*\.\d+|\d+",str(output.communicate()[0]))


                gender.write(youngerImage + " " + olderImage + " " + score[0] + " ")
                general.write(youngerImage + " " + olderImage + " " + score[0]+ " ")

                gender.close()
                general.close()

                ind = ind +3
        
    def age_progression_experiment_using_source_images():
        
        ageProgressExperiments = createFileList(DataPath+"\\\openBR_experiments\\age_progression_experiments")

        source_images = createFileList(MainPath + "\\Input\\Test")

        original_age_progressed_images = createFileList(OutputPath+"\\original_with_background")

        direct_texture_progressed_images = createFileList(OutputPath+"\\direct_texture_UPDATE")

        for i in range (0,len(ageProgressExperiments)):

             with open (DataPath+"\\\openBR_experiments\\age_progression_experiments\\"+ageProgressExperiments[i], "r") as myfile:
                data = myfile.read().split(" ")

             ind = 0 
             
             while( ind+2 < len(data)):
             
                 results = open(DataPath+"\\\openBR_experiments\\results\\OpenBR"+ageProgressExperiments[i][11:], 'a')


                 #find the correct image based on model, older image will be the source image

                 youngerImage = "" 

                 olderImage = ""

                 algorthimIndex = ageProgressExperiments[i].find("_")+1

                 algorthimUsed = ageProgressExperiments[i][algorthimIndex:algorthimIndex+13]

                 currentAlgorthim = ""

                 youngImagePath = ""

                 if algorthimUsed == "directTexture": 
                     
                     youngImagePath = OutputPath+"\\direct_texture_UPDATE\\"
                               
                     for file in direct_texture_progressed_images:
                         if file== data[ind][:-4]+".jpg":
                             youngerImage = file
                 else:

                      youngImagePath = OutputPath+"\\original_with_background\\"

                      for file in original_age_progressed_images:
                         if file == data[ind][:-4]+".jpg":
                             youngerImage = file


                 for file in source_images:
                    if file.startswith(data[ind+1][:12]):
                        olderImage = file


                 output = subprocess.Popen("br -algorithm FaceRecognition -compare "+ youngImagePath+youngerImage +" "+ MainPath +"\\Input\\Test\\"+olderImage,stdout=subprocess.PIPE,stderr=subprocess.PIPE)
                 score = re.findall("[-+]?\d*\.\d+|\d+",str(output.communicate()[0]))

                 results.write(youngerImage + " " + olderImage + " " + score[0] + " ")
                 results.close()

                 ind = ind +3
    #run experiments here

    #source_experiment()

    #age_progression_experiment_using_source_images()


    os.chdir(MainPath) #now that our tests are concluded, change current directroy back to the aging app directory

#copy image into new folders that are explained by a previous experiment
def sortImagesbyExperiment():

    experimentPath = DataPath + "\\paper2_stuff"

    experimentList = createFileList(experimentPath)

    for i in range (0, len(experimentList)):
        os.mkdir(DataPath+"\\experiment_image_directores\\"+experimentList[i][:-4])
        os.mkdir(DataPath+"\\experiment_image_directores\\"+experimentList[i][:-4]+"\\young_side_comparison")
        os.mkdir(DataPath+"\\experiment_image_directores\\"+experimentList[i][:-4]+"\\old_side_comparison")

        with open (experimentPath+'\\'+experimentList[i], "r") as myfile:
            string = myfile.read().split(" ")
        
        

        ind = 0

        while( ind+2 < len(string)):

            youngerFileName = ""

            olderFileName = ""
            
            for filename in os.listdir(OutputPath+"\\Aged_Images"):
                if filename.startswith(string[ind][:-4]):
                    youngerFileName =  filename
                if filename.startswith(string[ind+1][:-4]):
                    olderFileName =  filename

            shutil.copy(OutputPath+"\\Aged_Images\\"+youngerFileName,DataPath+"\\experiment_image_directores\\"+experimentList[i][:-4]+"\\young_side_comparison")
            shutil.copy(OutputPath+"\\Aged_Images\\"+olderFileName,DataPath+"\\experiment_image_directores\\"+experimentList[i][:-4]+"\\old_side_comparison")

            ind = ind+3
                              
#build age models based on image and points files, this is done by first making the AAM, then making the model
def buildAgeModel():

    ##create the age model building functions here##
    #_________________________________________________________________________#

    def buildBINfile(MaterialPath):

        #this is the list of directories whoes models currently need to be build, should be emptied at function termination, manual input will be added later
        #note do not include AAM_collection as a part of this list 

        buildModel = "GEN_252"

        #this is for printing the name of the model being built(as well as the name of the BIN file)
        nameList = [buildModel]

        buildModelList = [MaterialPath+buildModel+"\\"]

        #the directory (or directories) that contain the actual points and images needs to be listed, for each element in the build model list, the default for 
        #directory name for both images and points will be "Training", manual input will be added later
        
        imageDirectoryList = []
        pointsDirectoryList = []  

        defaultDIR = "AAM_images"

        #this version of agingapp requires the extensions for the image and points files, the default for images is ".jpg" and for points is ".pts"

        imageExtensionList = []
        pointsExtensionList = []

        defaultIMG = ".jpg"
        defaultPTS = ".pts"

        for i in range(0,len(buildModelList)):

            #add the directory names here
            imageDirectoryList.append(buildModelList[i] + defaultDIR)
            pointsDirectoryList.append(buildModelList[i] + defaultDIR)

            #add the indivdual extensions for each model
            imageExtensionList.append(defaultIMG)
            pointsExtensionList.append(defaultPTS)


        #make list of all bin files to return

        binList = []

        #now run agingapp build() function to create the AAM of all the material directories in the build model list

        for i in range (0,len(buildModelList)):

            print("building " + nameList[i] +".bin\n\n")

            subprocess.call("AgingApp.exe [GIVE_THIS_TO_PATH_VARIABLE_IN_AGING_APP] -b " + imageDirectoryList[i] + " " + pointsDirectoryList[i] + " " + imageExtensionList[i] + " " + pointsExtensionList[i] + " -q")

            for filename in os.listdir("."):
                if filename.startswith("newAAM"):
                    os.rename(filename, nameList[i]+".bin")
                    shutil.move(nameList[i] + ".bin", MaterialPath +"Age_Models")
                    binList.append(MaterialPath +"Age_Models\\"+nameList[i] + ".bin")

           

       

    def buildSVMandTABLEfile(MaterialPath, binList):

        #this is the list of directories whoes models currently need to be build, should be emptied at function termination, manual input will be added later
        #note do not include Aage_Models as a part of this list 

        buildModel = "GEN_252_SMALL"

        #this is for printing the name of the model being built(as well as the name of the SVM and TABLE files)
        nameList = [buildModel]

        buildModelList = [MaterialPath+buildModel+"\\"]

        #the directory (or directories) that contain the actual points and images needs to be listed, for each element in the build model list, the default for 
        #directory name for both images and points will be "Training", manual input will be added later
        
        imageDirectoryList = []
        pointsDirectoryList = []  

        defaultDIR = "Training"

        #this version of aging app requires the extensions for the image and points files, the default for images is ".jpg" and for points is ".pts"

        imageExtensionList = []
        pointsExtensionList = []

        defaultIMG = ".jpg"
        defaultPTS = ".pts"

        for i in range(0,len(buildModelList)):

            #add the directory names here
            imageDirectoryList.append(buildModelList[i] + defaultDIR)
            pointsDirectoryList.append(buildModelList[i] + defaultDIR)

            #add the indivdual extensions for each model
            imageExtensionList.append(defaultIMG)
            pointsExtensionList.append(defaultPTS)

        #get the max age of the age table

        max_age = "83"

        #now run aging app trainAgeModel() function to create the AAM of all the material directories in the build model list

        for i in range (0,len(buildModelList)):

            print("building " + nameList[i])

            subprocess.call("AgingApp.exe [GIVE_THIS_TO_PATH_VARIABLE_IN_AGING_APP] -t " + " " + binList[i] + " " + imageDirectoryList[i] + " " + pointsDirectoryList[i] + " " + imageExtensionList[i] + " " + pointsExtensionList[i] + " " + nameList[i] + ".table "+ nameList[i] + ".svm " + max_age +" -q")
            shutil.move(nameList[i] + ".table", MaterialPath +"Age_Models")
            shutil.move(nameList[i] + ".svm", MaterialPath +"Age_Models")
            
                
                    
    #_________________________________________________________________________#

    MaterialPath = MainPath + "\\Age_Model_Material\\"

    #run the age model building functions here
    #_________________________________________________________________________#

    #buildBINfile(MaterialPath)

    binList = [MaterialPath +"Age_Models\\GEN_252_SMALL.bin"]

    buildSVMandTABLEfile(MaterialPath, binList)
    #_________________________________________________________________________#

#perform the aging/deaging 
def age():

    ##create the aging functions here##
    #_________________________________________________________________________#

    #functuion that ages the images of a subject to all the other ages of the subject images
    def agePictures(ModelName, PicPath):

        fileList = createFileList(PicPath)
    
        GeneralModelPath = ModelsPath+"\\GeneralModels"

        maleModels = ["CAM", "CAM_BIG", "CAM_Devin"]

        femaleModels = ["CAF", "CAF_BIG", "CAF_Devin"]

        generalModels = ["CAU", "GEN_252", "GEN_252_SMALL"]

        #each subjects images are next to each other so we create an index at the beginning and end of the 
        #current subject's list of images so that each images can be aged to all the other ages in the subjects
        #list of images

        startInd = 0

        endInd = findLastInd(startInd,fileList)

        #function that runs the aging app on the current image (aging it as many times as there are images for its subject)
        def runAgingApp(ind,startInd,endInd,PicPath,file,fileList,model,table,svm,modelName):
        
            #age the current image to all the ages of the subject (including the age of the current image)
            for i in range(startInd,endInd):
 
                age = fileList[i][10:12]

                print("age: "  + age)

                #age image using the original aging algorithim 
                subprocess.call("AgingApp.exe " + PicPath + "\\" + file + " -a " + model + " " + table + " " + svm + " "+ modelName +" :"+ file[10:12] +" :"+ age +" -q") #read in file, remove print statements with -q

                #age image using the dirct texture method
                subprocess.call("AgingApp.exe " + PicPath + "\\" + file + " -d "+" " + file + " " + model + " " + table + " " + svm + " "+ modelName +" :"+ file[10:12] +" :"+ age +" -q") #read in file, remove print statements with -q
                

        for ind in range(0,len(fileList)):

            if(ind == endInd):
                    startInd = endInd
                    endInd = findLastInd(startInd,fileList)

            #run through each male model and age the image if the image is male
            if(fileList[ind][9:10] == "m"):
                for mIndex in range(0, len(maleModels)):
                    if(os.path.isfile(ModelsPath + "\\" + maleModels[mIndex] +".bin")):
                       model = ModelsPath + "\\" + maleModels[mIndex] +".bin"
                       table = ModelsPath + "\\" + maleModels[mIndex] +".table"
                       svm = ModelsPath + "\\" + maleModels[mIndex] +".svm"
                       modelName = maleModels[mIndex] 
                       #preform the aging
                       runAgingApp(ind,startInd,endInd,PicPath,fileList[ind],fileList,model,table,svm,modelName)
            
            #run through each female model and age the image if the image is female
            if(fileList[ind][9:10] == "f"):
                for fIndex in range(0, len(femaleModels)):
                    if(os.path.isfile(ModelsPath + "\\" + femaleModels[fIndex] +".bin")):
                       model = ModelsPath + "\\" + femaleModels[fIndex] +".bin"
                       table = ModelsPath + "\\" + femaleModels[fIndex] +".table"
                       svm = ModelsPath + "\\" + femaleModels[fIndex] +".svm"
                       modelName = femaleModels[fIndex] 
                       #preform the aging
                       runAgingApp(ind,startInd,endInd,PicPath,fileList[ind],fileList,model,table,svm,modelName)

            #run through each general model and age the images
            for gIndex in range(0, len(generalModels)):
                if(os.path.isfile(ModelsPath + "\\" + generalModels[gIndex] +".bin")):
                   model = ModelsPath + "\\" + generalModels[gIndex] +".bin"
                   table = ModelsPath + "\\" + generalModels[gIndex] +".table"
                   svm = ModelsPath + "\\" + generalModels[gIndex] +".svm"
                   modelName = generalModels[gIndex] 
                   #preform the aging
                   runAgingApp(ind,startInd,endInd,PicPath,fileList[ind],fileList,model,table,svm,modelName)

           
            #print a progess statement detailing the number of images currently aged over the total number of images 
            print('processed images: ' + str(ind+1) + '/' + str(len(fileList)), end ='\r')
    
    #_________________________________________________________________________#


    ##generate all higher level varables needed for aging functions here##
    #_________________________________________________________________________#

    #path to face pictures and points files, divided up by race

    AfricanPath = MainPath + "\Input\African" 
    AsianPath = MainPath + "\Input\Asian" 
    CaucasianPath = MainPath + "\Input\Caucasian" 
    HispanicPath = MainPath + "\Input\Hispanic" 
    MiddleEasternPath = MainPath + "\Input\MiddleEastern"
    TestPath = MainPath + "\Input\Test" 
    #_________________________________________________________________________#


    #run the agingapp functions here
    #_________________________________________________________________________#
    #check each race directory for images, if images are found, age them
    if os.listdir(AfricanPath):
        ModelName = "\AF"
        PicPath = AfricanPath
        print('processing afican images...');

        agePictures(ModelName,PicPath)

        print('\nall afican images processed');

    if os.listdir(AsianPath):
        ModelName = "\AS"
        PicPath = AsianPath
        print('processing asian images...');

        agePictures(ModelName,PicPath)

        print('\nall asian images processed');
    
    if os.listdir(CaucasianPath):
        ModelName = "\CA"
        PicPath = CaucasianPath
    
        print('processing caucasian images...');

        agePictures(ModelName,PicPath)

        print('\nall caucasian images processed');

    if os.listdir(HispanicPath):
         ModelName = "\HI"
         PicPath = HispanicPath
         print('processing hispanic images...');

         agePictures(ModelName,PicPath)

         print('\nall hispanic images processed');

    if os.listdir(MiddleEasternPath):
        ModelName = "\ME"
        PicPath = MiddleEasternPath
        print('processing middle eastern images...');

        agePictures(ModelName,PicPath)

        print('\nall middle eastern images processed');

    if os.listdir(TestPath):
        ModelName = "\CA"
        PicPath = TestPath
        print('processing test images...');

        agePictures(ModelName,PicPath)

        print('\nall test images processed');
    #_________________________________________________________________________#

def cleanSHAPEdir():
    #for some reason all the output directories are copied empty into the SHAPE file directory, delete them here
        shutil.rmtree(OutputPath+"\\SHAPE_files\\Aged_Images")
        shutil.rmtree(OutputPath+"\\SHAPE_files\\APP_files")
        shutil.rmtree(OutputPath+"\\SHAPE_files\\Fitted_Image_Files")
        shutil.rmtree(OutputPath+"\\SHAPE_files\\POSE_files")
        shutil.rmtree(OutputPath+"\\SHAPE_files\\POSEAPP_files")
        shutil.rmtree(OutputPath+"\\SHAPE_files\\Processed_Images")
        shutil.rmtree(OutputPath+"\\SHAPE_files\\SHAPE_files")
        shutil.rmtree(OutputPath+"\\SHAPE_files\\TEXTURE_files")

#run a separate command in agingapp to produce a test file of just age estimations from an age model 
def estimateAges():

    imageFileList = createFileList(OutputPath+"\Processed_Images")

    for i in range(len(imageFileList)):
         #estimate ages with gender models
        if imageFileList[i][9:10] == "f":
            model = ModelsPath + "\CAF.bin"
            table = ModelsPath + "\CAF.table"
            svm = ModelsPath + "\CAF.svm"
            modelName = "CAF"
        else:
            model = ModelsPath + "\CAM.bin"
            table = ModelsPath + "\CAM.table"
            svm = ModelsPath + "\CAM.svm"
            modelName = "CAM"
        #read in file, remove print statements with -q
        subprocess.call("AgingApp.exe" + " " + OutputPath+"\Processed_Images" + "\\" + imageFileList[i] + " -s "+" " + imageFileList[i] + " " + model + " " + table + " " + svm + " "+ modelName +" :"+ imageFileList[i][10:12] +" :"+ imageFileList[i][10:12] +" -q")
        
        #estimate ages with general model
        model = ModelsPath + "\CAU.bin"
        table = ModelsPath + "\CAU.table"
        svm = ModelsPath + "\CAU.svm"
        modelName = "CAU"

        #read in file, remove print statements with -q
        subprocess.call("AgingApp.exe" + " " + OutputPath+"\Processed_Images" + "\\" + imageFileList[i] + " -s "+" " +  imageFileList[i] + " " + model + " " + table + " " + svm + " "+ modelName +" :"+ imageFileList[i][10:12] +" :"+ imageFileList[i][10:12] +" -q")

#generate a covariance for each age algorithm/model combination 
def covarianceMatrices():

    print("creating and writing covariance matrices ")

    appFiles = createFileList(AppFilePath)

    #create the covariance matrices used by the mahalanobis distance by reading in all the app files of a particular model and aging algorithm  
    def makeCovarianceMatrix(appFiles):

        dataList = [[]] #data list of lists for app data
        dataNames = [] # the age algorithm/model name list 

        for ind in range(len(appFiles)):

            #the name we are looking for is the model and algorithm name
            name = appFiles[ind][19:-4]

            #this is its index in the data list of lists
            nameInd = -1

            #if this name has appeared before, find the index
            for nameIndex in range(len(dataNames)):
                if name == dataNames[nameIndex]:
                    nameInd = nameIndex
        
            #if not then add a new list to data list and set the index accordingly
            if nameInd == -1:
                nameInd = len(dataNames)
                dataList.append([])
                dataNames.append(name)

            d = np.genfromtxt(AppFilePath+"\\"+appFiles[ind], delimiter=' ')
            data = np.array(d)
            
            dataList[nameInd].append(data)
            print('processed app files: ' + str(ind+1) + '/' + str(len(appFiles)), end='\r')
           
       
        for ind in range(len(dataNames)):    
            o = np.array(dataList[ind])
            cMO = np.cov(o.T)

            outfileO = ".\Data\\"+"covarianceMatrices\\"+dataNames[ind]
            np.save(outfileO , cMO)

    makeCovarianceMatrix(appFiles)  

# all comparisons will be processed here
def agedImageComparisons():

    appFiles = createFileList(AppFilePath)

    def writeOutToFile(i,ind,result,type,additonal):

        gender = ""

        for mInd in range (0,len(generalModels)):

            modelIndex = (len(generalModels[mInd])+4)*-1

            #if the current app file we are looking at is from a general model, figure out which gender the subject is
            if appFiles[ind][modelIndex:-4] == generalModels[mInd]:
                gender = appFiles[ind][9:10]
        
        if(appFiles[ind][13:15] > appFiles[ind][16:18] ): #if either app file compaired is an age regressed comparison, save it to an age regress data file

            f = open(".\Data\\"+type+"AgeRegress"+appFiles[ind][18:-4]+gender+additonal+".txt", 'a')
            f.write( str(appFiles[ind])+" "+str(appFiles[i])+" "+str(result)+" ")
                      
        else: #if none of app files compaired are an age regressed comparison, save it to an age progress data file
            f = open(".\Data\\"+type+"AgeProgress"+appFiles[ind][18:-4]+gender+additonal+".txt", 'a')
            f.write( str(appFiles[ind])+" "+str(appFiles[i])+" "+str(result)+" ")
               

    #function to calculate the euclidean distance between the .app files from the aged images, and write them to a text file based on the age model, gender, aging algorithim,and age progression/regression 
    def euclideanDistance(ind, startInd, endInd, euclideanStartIndexList, euclideanEndIndexList):
        
        #the current vector 
        data1 = np.genfromtxt(AppFilePath+"\\"+appFiles[ind], delimiter=' ')

        for i in range(startInd,endInd):
        
             #check if the comparison about o be made has already been calculated
             sameComparison = 0

             #extract the age model

             for j in range(len(euclideanStartIndexList)):
                 if(i == euclideanStartIndexList[j] and ind == euclideanEndIndexList[j]):
                     sameComparison = 1
                     

                #if the starting ages are different, the end ages are the same, the aging algoritim is the same, the aging model is the same, at least one of the app files is a reconstruction ,and the comparison has not already happened, run the comparison
             if(appFiles[i][10:12] != appFiles[ind][10:12] and appFiles[i][16:18] == appFiles[ind][16:18] and appFiles[i][19:] == appFiles[ind][19:] and ( appFiles[ind][10:12] == appFiles[ind][16:18] or appFiles[i][10:12] == appFiles[i][16:18] ) and sameComparison == 0):
                
                #add the indxes to their corresponding lists (to check later so that this comaprison is not computed more than once)
                euclideanStartIndexList.append(ind)
                euclideanEndIndexList.append(i)

                #the second vector to be subtracted
                data2 = np.genfromtxt(AppFilePath+"\\"+appFiles[i], delimiter=' ')

                #put the vectors into arrays
                u = np.array(data1) 
                v = np.array(data2)

                #caluculate the distance
                tmp = u - v
                result = np.sqrt(np.dot( tmp.T , tmp)) 
                
                type = "Euclidean"

                additonal = ""

                #write the result to the proper data file
                writeOutToFile(i,ind,result,type,additonal)



    def euclideanDistanceRECONSTRUCTION_ONLY(ind, startInd, endInd, euclideanStartIndexList, euclideanEndIndexList):
        
        #the current vector 
        data1 = np.genfromtxt(AppFilePath+"\\"+appFiles[ind], delimiter=' ')

        for i in range(startInd,endInd):
        
             #check if the comparison about to be made has already been calculated
             sameComparison = 0

            

             for j in range(len(euclideanStartIndexList)):
                 if(i == euclideanStartIndexList[j] and ind == euclideanEndIndexList[j]):
                     sameComparison = 1

                 #if both appfiles are reconstructions, the aging algoritim is the same, the aging model is the same ,and the comparison has not already happened, and the two files are not the same,run the comparison
             if(appFiles[i][10:12] == appFiles[i][16:18] and appFiles[ind][10:12] == appFiles[ind][16:18] and appFiles[i][19:] == appFiles[ind][19:] and appFiles[i][10:12] != appFiles[ind][10:12] and sameComparison == 0):
             
                #add the indxes to their corresponding lists (to check later so that this comaprison is not computed more than once)
                euclideanStartIndexList.append(ind)
                euclideanEndIndexList.append(i)

                #the second vector to be subtracted
                data2 = np.genfromtxt(AppFilePath+"\\"+appFiles[i], delimiter=' ')

                #put the vectors into arrays
                u = np.array(data1) 
                v = np.array(data2)

                #caluculate the distance
                tmp = u - v
                result = np.sqrt(np.dot( tmp.T , tmp)) 
                
                type = "Euclidean"

                additonal = "(reconstructed_images_only)"

                #write the result to the proper data file
                writeOutToFile(i,ind,result,type,additonal)



    def mahalanobisDistance(appFiles,ind, startInd, endInd, mahalanobisStartIndexList, mahalanobisEndIndexList):

        #the current vector 
        data1 = np.genfromtxt(AppFilePath+"\\"+appFiles[ind], delimiter=' ') 

        for i in range(startInd,endInd):

            #check if the comparison about o be made has already been calculated
             sameComparison = 0

             alogrthimEnd = appFiles[i].find("_",19)

             for j in range(len(mahalanobisStartIndexList)):
                 if(i == mahalanobisStartIndexList[j] and ind == mahalanobisEndIndexList[j]):
                     sameComparison = 1 #the comparison has already been made
             
            #if the starting ages are different, the end ages are the same, the aging algoritim is the same, the aging model is the same, at least one of the app files is a reconstruction ,and the comparison has not already happened, run the comparison
             if(appFiles[i][10:12] != appFiles[ind][10:12] and appFiles[i][16:18] == appFiles[ind][16:18] and appFiles[i][19:] == appFiles[ind][19:] and  appFiles[i][10:12] == appFiles[i][16:18]   and sameComparison == 0):
                
                #add the indxes to their corresponding lists (to check later so that this comaprison is not computed more than once)
                mahalanobisStartIndexList.append(ind)
                mahalanobisEndIndexList.append(i)

                #the second vector to be subtracted
                data2 = np.genfromtxt(AppFilePath+"\\"+appFiles[i], delimiter=' ')

                #put the vectors into arrays
                u = np.array(data1) 
                v = np.array(data2)

                #find the correct covariance matrix
                for j in range(len(covarianceNames)):
                  
                    if appFiles[ind][19:-4] == covarianceNames[j]:

                        vI = np.linalg.inv(covarianceMatrices[j])
                        
                        #now compute the mahalanobis distance
                        result = scipy.spatial.distance.mahalanobis(u,v,vI)

                        type = "Mahalanobis"

                        additonal = ""

                        #write the result to the proper data file
                        writeOutToFile(i,ind,result,type,additonal)  


    def mahalanobisDistanceRECONSTRUCTION_ONLY(ind, startInd, endInd, mahalanobisStartIndexList, mahalanobisEndIndexList):

        #the current vector 
        data1 = np.genfromtxt(AppFilePath+"\\"+appFiles[ind], delimiter=' ')

        for i in range(startInd,endInd):

            #check if the comparison about o be made has already been calculated
             sameComparison = 0

             alogrthimEnd = appFiles[i].find("_",19)

             for j in range(len(mahalanobisStartIndexList)):
                 if(i == mahalanobisStartIndexList[j] and ind == mahalanobisEndIndexList[j]):
                     sameComparison = 1 #the comparison has already been made
            
            #if both appfiles are reconstructions, the aging algoritim is the same, the aging model is the same ,and the comparison has not already happened, and the two files are not the same,run the comparison
             if(appFiles[i][10:12] == appFiles[i][16:18] and appFiles[ind][10:12] == appFiles[ind][16:18] and appFiles[i][alogrthimEnd:] == appFiles[ind][alogrthimEnd:] and appFiles[i][19:alogrthimEnd] == "original" and appFiles[i][10:12] != appFiles[ind][10:12] and sameComparison == 0):
                
                #add the indxes to their corresponding lists (to check later so that this comaprison is not computed more than once)
                mahalanobisStartIndexList.append(ind)
                mahalanobisEndIndexList.append(i)

                #the second vector to be subtracted
                data2 = np.genfromtxt(AppFilePath+"\\"+appFiles[i], delimiter=' ')

                #put the vectors into arrays
                u = np.array(data1) 
                v = np.array(data2)

                #find the correct covariance matrix
                for j in range(len(covarianceNames)):
                  
                    if appFiles[i][18:-4] == covarianceNames[j]:

                        vI = np.linalg.inv(covarianceMatrices[j])
                        
                        #now compute the mahalanobis distance
                        result = scipy.spatial.distance.mahalanobis(u,v,vI)

                        type = "Mahalanobis"

                        additonal = "(reconstructed_images_only)"

                        #write the result to the proper data file
                        writeOutToFile(i,ind,result,type,additonal)

    #load all the model names into a list so that comparisons can be seperated by model
   
    covarianceMatrices = []
    covarianceNames = []

    generalModels = ["CAU","GEN_252","GEN_252_SMALL"] #general models must be designated before hand so the male and female app files can be properly broken up

    CovarianceMatrixPath = MainPath+"\Data\\"+"covarianceMatrices\\"

    i = 0

    #read in covariance matrices
    for file in os.listdir(CovarianceMatrixPath):

        if file.endswith(".npy"):

            f = np.load(CovarianceMatrixPath+file)
            covarianceMatrices.insert(i, f)
            covarianceNames.insert(i,file[:-4])

    #preform the comparisons on all the images
    startInd = 0

    endInd = findLastInd(startInd,appFiles)

	#make separate lists for euclidean and mahalanobis comparisons
    euclideanStartIndexList = []
    euclideanEndIndexList = []
    mahalanobisStartIndexList = []
    mahalanobisEndIndexList = []

	#step through all parameter files of each age progressed image
    for ind in range(0,len(appFiles)):

        if(ind == endInd):
            startInd = endInd
            endInd = findLastInd(startInd,appFiles)
            euclideanStartIndexList = []
            euclideanEndIndexList = []
            mahalanobisStartIndexList = []
            mahalanobisEndIndexList = []

        #euclideanDistance(ind,startInd,endInd,euclideanStartIndexList,euclideanEndIndexList)
        mahalanobisDistance(appFiles,ind,startInd,endInd,mahalanobisStartIndexList,mahalanobisEndIndexList)

        #euclideanDistanceRECONSTRUCTION_ONLY(ind,startInd,endInd,euclideanStartIndexList,euclideanEndIndexList)

        #mahalanobisDistanceRECONSTRUCTION_ONLY(ind,startInd,endInd,mahalanobisStartIndexList,mahalanobisEndIndexList)

        print('processed app files: ' + str(ind+1) + '/' + str(len(appFiles)), end='\r')


    print("\ndone processing app files")
         
#results from the comparisons will be summed up in statistics and graphs, computed and printed here
def printData():


    data_to_plot = [] #list of all box plots

    boxTics = [] #labels for box plot 

    max = 0 #global max distance 
            
                     
    colors = ['red','blue','green','yellow','teal','purple', 'black', 'fuchsia', 'gray',  'lime', 'maroon', 'navy', 'olive', 'orange', 'aqua', 'silver', 'teal', 'white']

    models = ["CAM", "CAM_BIG", "CAM_Devin","CAF", "CAF_BIG", "CAF_Devin","CAU", "GEN_252", "GEN_252_SMALL"] #use these model names to build up labels for graphs

    dataList = createFileList(DataPath)

    binValues = [10,15,20,25,30,35,40,45,50,200] #these are the age groups, comparisons less than 10 apart go in the 10 bin, comparisons that are at least 10, but less than 15 go in the 15 bin, ect, 200 was chosen as the final age limit as there are no age comparsions any where near 200 years apart

    binLists = [[] for _ in range(10)] #this is a list of lists that will contain the indivdual comparisons that are placed in the bins (the comparisons in the 10 bin will go in the first list, the 15 bin will go in the second list, ect...) 

    barOffsetList = []
    barFigureNames = []
    
    #to correctly generate the space needed between bars in the bar graph, we need to figure out how
    #many age algorithms/models we are graphing. once that number is found, the space we need is the 
    #total number of files over the number of age types (progression/regression) times the number of
    #genders (male/female) times the number of app file comparisons (euclidean/mahalanobis) plus 1

    ageTypes = 2
    genders = 2
    comparisons =2

    barDistance = len(dataList)/(ageTypes*genders*comparisons)+1

    #if bar is on male of female images only, then this will not change
    combinedOffset = 0

    ##create the printing functions here##
    #_________________________________________________________________________#

    #create a directory for the specific age algorthim/model combination
    def buildBestWorstDir(ind):
        if not os.path.exists(StatisticsPath+"\\" + dataList[ind][:-4]):
                os.makedirs(StatisticsPath+"\\" + dataList[ind][:-4])

        #now create directories for the 5 best and 5 worst comparisons  

        if not os.path.exists(StatisticsPath+"\\" + dataList[ind][:-4]+"\\Best"):
            os.makedirs(StatisticsPath+"\\"  + dataList[ind][:-4]+"\\Best")

        if not os.path.exists(StatisticsPath+"\\"  + dataList[ind][:-4]+"\\Worst"):
            os.makedirs(StatisticsPath+"\\"  + dataList[ind][:-4]+"\\Worst")

    #find the best and worst 5 comparisons
    def findBestWorst(sortedIndex,textIndex,sortedData,bestWorstCompare,dataList,ind):

        #make the directory for the best/worst images
            
        buildBestWorstDir(ind) 

        while sortedIndex < len(sortedData): #this breaks up the data in the file into groups of three
            if(sortedIndex == 5):
                sortedIndex = len(sortedData)-5
            
            if(text[ textIndex] == sortedData[sortedIndex]):
                bestWorstCompare.append( "%s and %s = %s" %(string[textIndex-2] ,string[textIndex-1],string[textIndex]))
                
                dst = ""
                if sortedIndex < 6:
                    dst = StatisticsPath + "\\" + dataList[ind][:-4]+"\\Best"
                else:
                    dst = StatisticsPath + "\\" + dataList[ind][:-4]+"\\Worst"

                #copy first image

                src = ""

                for fname in os.listdir(OutputPath + "\\Aged_Images"):
                    
                    if fname.startswith(string[textIndex-2][:-4]):
                        src = OutputPath + "\\Aged_Images\\"+fname
                        break

                shutil.copy(src,dst)
                
                #copy second image
                src = ""

                for fname in os.listdir(OutputPath + "\\Aged_Images"):
                    if fname.startswith(string[textIndex-1][:-4]):
                        src =  OutputPath + "\\Aged_Images\\"+ fname
                        break

                shutil.copy(src,dst)
                
                sortedIndex = sortedIndex + 1
                textIndex = 0    
                  
            textIndex =  textIndex +1

    #build the line plot
    def buildLinePlot(name,ProgressonRegression,gender,ind,currentLabel):

        plt.xlabel('Aged Image Comparison')
        
        plt.grid(True)

        plt.figure(name+ProgressonRegression+gender+"line")
        plt.ylabel(name+' Distance')
        plt.title(name+' '+ProgressonRegression+' Distances of '+gender+' Aged Images')
        plt.plot(data,label = currentLabel)
        fontP = FontProperties()
        fontP.set_size('small')
        plt.legend(bbox_to_anchor=(1.05, 1), loc=2, borderaxespad=0.,prop = fontP)
        plt.savefig(GraphsPath+"\\"+name+"_"+ProgressonRegression[0:3]+"_"+ProgressonRegression[4:]+"_Distances_of_"+gender+"_Aged_Images.pdf", bbox_inches= 'tight')
        
        #finished writing to the plot    
        plt.close

    #build the bar graph
    def buildBarGraph(name,ProgressonRegression,gender,bins,ind,setMax,currentLabel,binDataCount,combinedOffset):

        t = 1

        if t == 1:

            bins = swap_bin(ind)

            currentLabel = currentAlgorthim + " average Cognitec Face VACS Recognition score"

            if  recon == "(reconstructed_images_only)":
                currentLabel = gender + " Source Images"

        plt.figure(name+ProgressonRegression+gender+"bar")

        if name+ProgressonRegression+gender+"bar" not in barFigureNames:
            barFigureNames.append(name+ProgressonRegression+gender+"bar")
        
            barOffsetList.append(0)
            
        else:
            barIndex = barFigureNames.index(name+ProgressonRegression+gender+"bar")
            barOffsetList[barIndex] += 1
        
        bIndex = barFigureNames.index(name+ProgressonRegression+gender+"bar")  
        offset = barOffsetList[bIndex] 

        

        col = colors[barOffsetList[bIndex]]

        yMax = setMax + 0.5

        ax = plt.gca()

        extraOffset = 0

        if bIndex != 0:
            extraOffset = 2

        xCoor = np.arange(barDistance,(barDistance*10)+1,barDistance)
        xName = np.arange(10,51,5)

        xLabels = ["5 - 9", "10 - 14", "15 - 19","20 - 24","25 - 29","30 - 34","35 - 39","40 - 44","45 - 49","50+",]

        if not(os.path.exists(GraphsPath+"\\"+name+"_"+ProgressonRegression[0:3]+"_"+ProgressonRegression[4:]+"_Average_Distances_of_"+gender+"_Aged_Images.txt")):
            for writeInd in range(0,len(binDataCount)):
                f = open(GraphsPath+"\\"+name+"_"+ProgressonRegression[0:3]+"_"+ProgressonRegression[4:]+"_Average_Distances_of_"+gender+"_Aged_Images.txt", 'a')
                f.write( str(xLabels[writeInd ])+": "+str(binDataCount[writeInd ])+" comparisons \n")

            f.close()

        distanceOrScore = 'Scores'

        plt.ylabel(distanceOrScore)
        plt.xlabel("Age Differences")
        #plt.title(name+' '+ProgressonRegression+' Average '+ distanceOrScore +'s of \n'+gender+' Images per Five Year Increments')
        plt.ylim([0,1])
        plt.xticks(xCoor+offset+bIndex-combinedOffset-1,  xLabels,fontsize = 11)
        plt.grid(False)
        plt.bar(xCoor+offset+bIndex,bins,width = 1,color = col,align='center',label = currentLabel)
        fontP = FontProperties()
        fontP.set_size('small')
        plt.legend( loc=2, borderaxespad=0.,prop = fontP)
        plt.savefig(GraphsPath+"\\"+name+"_"+ProgressonRegression[0:3]+"_"+ProgressonRegression[4:]+"_Average_"+distanceOrScore+"s_of_"+gender+"_Aged_Images.pdf", bbox_inches='tight')

        #finished writing to the graph
        plt.close

    def buildBoxPlot(data_to_plot,boxTics):
        fig =  plt.figure("box")

        ax = fig.add_subplot(111)

        bp = ax.boxplot(data_to_plot, patch_artist=True)

        ## change outline color, fill color and linewidth of the boxes
        for box in bp['boxes']:
            # change outline color
            box.set( color='#7570b3', linewidth=2)
            # change fill color
            box.set( facecolor = '#1b9e77' )

        ## change color and linewidth of the whiskers
        for whisker in bp['whiskers']:
            whisker.set(color='#7570b3', linewidth=2)

        ## change color and linewidth of the caps
        for cap in bp['caps']:
            cap.set(color='#7570b3', linewidth=2)

        ## change color and linewidth of the medians
        for median in bp['medians']:
            median.set(color='#b2df8a', linewidth=2)

        ## change the style of fliers and their fill
        for flier in bp['fliers']:
            flier.set(marker='o', color='#e7298a', alpha=0.5)

        ax.set_xticklabels(boxTics)
        #plt.title(currentAlgorthim + " used on " + gender +" images")
        

        
        fig.savefig(GraphsPath+"\\"+"box.pdf", bbox_inches='tight')


    #generate a statistics report for each age algorithm/model combination
    def generateStatistics(data,bestWorstCompare,ind):
        mean = np.mean(data)

        median = np.median(data)

        standardDeviation = np.std(data)

        statistics = open(StatisticsPath+"\\"+dataList[ind][:-4]+"\\statistics.txt", 'w')
        statistics.write(dataList[ind][:-4] + "\n\n")

        statistics.write("mean: " + str(mean) + "\n\n")

        statistics.write("median: " + str(median) + "\n\n")
    
        statistics.write("standard deviation: " + str(standardDeviation) + "\n\n")

        statistics.write("best five: " + str(bestWorstCompare[:5]) + "\n\n")

        statistics.write("worst five: " + str(bestWorstCompare[-5:]) + "\n\n")

        statistics.write("_________________________________\n") 

    #_________________________________________________________________________#

    #print the values in the age difference bins
    def print_bins(currentAlgorthim,currentModel,gender,bins):

        xLabels = ["5 - 9", "10 - 14", "15 - 19","20 - 24","25 - 29","30 - 34","35 - 39","40 - 44","45 - 49","50+",]

        if currentModel == "":
            currentModel = gender

        if not(os.path.exists(GraphsPath+"\\"+currentModel+"_"+currentAlgorthim+".txt")):
            for writeInd in range(0,len(bins)):
                f = open(GraphsPath+"\\"+currentModel+"_"+currentAlgorthim+".txt", 'a')
                f.write( str(xLabels[writeInd])+", "+str(bins[writeInd ])+", ")

            f.close()
	
	
    #for each age algrothim/model/gender combination, generate statistics and graphs based on the .app file comparsions
    for ind in range(0, len(dataList)):

    
        text = np.genfromtxt(DataPath+ "\\" + dataList[ind], delimiter=' ')

        with open ('.\\Data\\'+dataList[ind], "r") as myfile:
            string = myfile.read().split(" ")
            
        #get the numerical data from the data files
        data = text[2::3]

        sortedData = np.array(data)

        sortedData.sort()

        #get the best/worst comparsions now so that they may be used later on

        bestWorstCompare = []

        sortedIndex = 0

        textIndex = 0;

        #create the base name for all the graphs printed by this function

        nameIndex = dataList[ind].find("Age")

        name = dataList[ind][: nameIndex]

        #figure out if this is an age progresson comparsion or regression comparsion

        ProgressonRegression = ""

        if dataList[ind][nameIndex: nameIndex+10] == "AgeRegress":
            ProgressonRegression = "Age Regression"
        else:
            ProgressonRegression = "Age Progression"

        #figure out what alogrthim is being used

        algorthimIndex = dataList[ind].find("_")+1

       

        if  dataList[ind][:3] == "CSU":
            
            algorthimIndex = dataList[ind].find("_",4)+1
            

        algorthimUsed = dataList[ind][algorthimIndex:algorthimIndex+13]

        currentAlgorthim = ""

        currentModel = ""

        recon = ""

        if dataList[ind].endswith("(reconstructed_images_only).txt"):
            recon = "(reconstructed_images_only)"

        for modelIndex in range (0, len(models)):

            if models[modelIndex] in dataList[ind]:

               currentModel = models[modelIndex]

       
        
        if algorthimUsed == "s_directTextu" or algorthimUsed == "directTexture":
            
            currentAlgorthim = "AAMDT"
        elif algorthimUsed[:8] == "original" and recon == "":

            currentAlgorthim = "AAMAP"
        else:
             currentAlgorthim = "Source"
        

        
        #figure out which age model is being used

        #figure put the gender of the subject compared

        gender = ""

        #this is a bad way of differentiating gender, there should be a text file with a proper list that handles this!! Devin 2014
        if((dataList[ind].endswith("M"+recon+".txt") or dataList[ind].endswith("m"+recon+".txt")) or (dataList[ind].endswith("M_Devin"+recon+".txt") or dataList[ind].endswith("m_Devin"+recon+".txt")) or (dataList[ind].endswith("M_BIG"+recon+".txt") or dataList[ind].endswith("m_BIG"+recon+".txt")) or  (dataList[ind].endswith("M_SMALL"+recon+".txt") or dataList[ind].endswith("m_SMALL"+recon+".txt")) or dataList[ind].endswith("Male_source_results.txt")):
            gender = "Male"
        else:
            gender = "Female"

        if((dataList[ind].endswith("CAU"+recon+".txt")) or (dataList[ind].endswith("GEN_252"+recon+".txt")) or (dataList[ind].endswith("GEN_252_SMALL"+recon+".txt")) or dataList[ind].endswith("GEN_252_BIG"+recon+".txt") or dataList[ind].endswith("All_source_results.txt")):

            gender = "All"
            #combinedOffset = 1
        
        
         #now create this data set's label

        if currentModel == "GEN_252":  #this is because of my stupid naming convention, I made GEN_252 way before I made GEN_252_SMALL, it makes no sense for all the other models to have a BIG version and this one to have a SMALL version

            currentModel = "GEN_252_BIG"

        if currentModel == "GEN_252_SMALL":

            currentModel = "GEN_252"

        if recon == "":
            currentLabel = currentAlgorthim + " using " + currentModel 
        else: 
            currentLabel =  "Reconstructed " + currentModel + " Parameters" 

        if currentModel == "":
            currentLabel = gender + " Source Images"
          
        #for the bar and 3d bar graph,we need to set up some bin lists 

        #initalize the data lists
        binData = [0] * len(binValues)
        binDataCount = [0] * len(binValues)
        bins = [0] * len(binValues)

        disData = []

        boxTics.append(currentModel+"\n"+currentAlgorthim)

        binLists = [[] for _ in range(10)] #this is a list of lists that will contain the indivdual comparisons that are placed in the bins (the comparisons in the 10 bin will go in the first list, the 15 bin will go in the second list, ect...) 

        i = 0
        total = 0

        while i+2 < len(string):

            ageDifference = int(string[i][10:12]) - int(string[i+1][10:12])
            
            if(ageDifference < 0):
                ageDifference = ageDifference*-1
             
            distance = float(string[i+2])
            disData.append(distance)

            j = 0

            while(ageDifference >= binValues[j]):    
            
                j = j +1
                   
            binData[j] = binData[j] + distance
            
            binDataCount[j] += 1
            total += 1
            #for 3d graph
            binLists[j].append(distance)       

            i = i +3     
        
        #we need to find the max bin lengt to determine the full demensions of binLists

        maxBin = 0
        
        data_to_plot.append(disData)

        #we need the average distance of each bin to be used in the bar graph
        for k in range(len(binData)):

            

            if binDataCount[k] > maxBin:
                maxBin = binDataCount[k]

            if binDataCount[k] == 0:
                bins[k] = 0.0
            else:
                bins[k] = binData[k]/float(binDataCount[k])
        #now we have finnish with the averages, we need to fill the binLists empty sections with 0's so it can be graphed
        for l in range(len(binData)):
            while len(binLists[l]) < maxBin:
                binLists[l].append(0.0)      


        #need to switch from error to score?
        t = 0

        if t == 1:
            if dataList[ind][:11] == "Mahalanobis":

                mx = invert_and_rescale_results(DataPath+ "\\" + dataList[ind])

                for bin_index in range(0,len(bins)):
                 
                    if bins[bin_index] > 0.0: 

                        bins[bin_index] = (mx - bins[bin_index])/mx


        #now figure out how big the bar graph will be by setting the y max

        localMax = max(int(bins))

        setMax = 0

        if gender == "Male":
            if localMax > max:
                max = localMax
            setMax = max

    #_________________________________________________________________________#


    #run the statistics and graph functions here
    #_________________________________________________________________________#
        findBestWorst(sortedIndex,textIndex,sortedData,bestWorstCompare,dataList,ind)

        buildLinePlot(name,ProgressonRegression,gender,ind,currentLabel)

        buildBarGraph(name,ProgressonRegression,gender,bins,ind,setMax,currentLabel,binDataCount,combinedOffset)

        generateStatistics(data,bestWorstCompare,ind)
        if ind == len(dataList)-1:
            buildBoxPlot(data_to_plot,boxTics)
     #________________________________________________________________________#

#returns all source images and points files to input directory, deletes aged data and printed graphs and statistics
def reset():

    ##create the reset functions here##
    #_________________________________________________________________________#

    #move the source images and their points files back to input, for now all images and points files will
    #be moved to the Causcasian directory as all subjects currently being tested are caucasian
    #a more generic approach should be implemented in the future
    def moveSource():
        #list all files in directory

        fileList = os.listdir(OutputPath+"\\Processed_Images\\")

        #get the images
        sourceImages = createFileList(OutputPath+"\\Processed_Images\\")
    
        #get the correct points files

        sourcePoints = [] 

        index = 0

        for file in fileList:
           if(index < len(sourceImages)):
                if(file == sourceImages[index][:-3]+"pts" or file == sourceImages[index][:-4]+"pts" ):
                    sourcePoints.append(file)
                    index +=1

        #now move these files into the input directory
        for i in range (0,len(sourceImages)):
            shutil.move(OutputPath+"\\Processed_Images\\"+sourceImages[i], MainPath + "\\Input\\Caucasian" )
            shutil.move(OutputPath+"\\Processed_Images\\"+sourcePoints[i], MainPath + "\\Input\\Caucasian" )
    
    #remove the generated output from aging app
    def emptyOutputDir():

        for dirName in os.listdir(OutputPath):

            for fileName in os.listdir(OutputPath+"\\"+dirName):

                os.remove(OutputPath+"\\"+dirName+"\\"+fileName)

            
    #remove all the text file in the Data folder containing the comparison information
    def emptyData():


        for fileName in os.listdir(DataPath):

            if fileName.endswith(".txt"):
                os.remove(DataPath+"\\"+fileName)
        #now remove all the covaranece matrices 
        for fileName in os.listdir(DataPath+"\\covarianceMatrices"):
            os.remove(DataPath+"\\covarianceMatrices\\"+fileName)
   
    #remove all of the generated graphs
    def emptyGraphs():
         for fileName in os.listdir(GraphsPath):
             os.remove(GraphsPath+"\\"+fileName)
    
    #remove all the generated statistics data 
    def emptyStatistics():

        for dirName in os.listdir(StatisticsPath):
            shutil.rmtree(StatisticsPath+"\\"+dirName)

    #_________________________________________________________________________#

    ##run the reset functions here##
    #_________________________________________________________________________#
    moveSource()
    emptyOutputDir()
    emptyData()
    emptyGraphs()
    emptyStatistics()
    #_________________________________________________________________________#

if __name__ == "__main__":
    main()
