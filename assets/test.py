from jmodelica.fmi import FMUModel

myModel=FMUModel('/var/tmp/bouncingBall.fmu')
myModel.simulate()