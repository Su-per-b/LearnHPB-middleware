from jmodelica.fmi import FMUModel

myModel=FMUModel('/var/tmp/JModelica/Python/src/jmodelica/examples/files/FMUs/bouncingBall.fmu')
myModel.simulate()

myModel2=FMUModel('/home/ubuntu/scripts/testFMI.fmu')
myModel2.simulate()