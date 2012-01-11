import sys
import os

print "Hi! From Python: test_fmu.py"
print sys.version_info


from jmodelica.fmi import FMUModel
myModel=FMUModel("bouncingBall.fmu")
myModel.simulate()