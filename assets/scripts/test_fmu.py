from jmodelica.fmi import FMUModel

myModel=FMUModel('/home/ubuntu/bouncingBall.fmu')
myModel.simulate()