import sys
import os

os.chdir('fmus')

#print "Hi! From Python: test_fmu.py"
print sys.version_info

#for arg in sys.argv:
 #   print arg
 
if (len(sys.argv) > 1) :
	fileName = sys.argv[1]
	print "running simulation for file: " + fileName
else :
	print "no filename defined"
	exit()
	
	
from jmodelica.fmi import FMUModel
myModel=FMUModel(fileName, enable_logging=True)


res = myModel.simulate()
print res._result_file_name

fin = open(res._result_file_name, "r")
fileContents = fin.read()
fin.close()
print fileContents
