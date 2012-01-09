from jmodelica.fmi import FMUModel

myModel=FMUModel('./bouncingBall.fmu')
myModel.simulate()