#from jmodelica.fmi import FMUModel

# cd /var/tmp/JModelica/build/Python
#sudo ./jm_python.sh

#cd /opt/packages/jmodelica/jmodelica-install/bin
#sudo ./jm_ipython.sh
#from jmodelica.fmi import FMUModel
#myModel=FMUModel('/var/tmp/JModelica/Python/src/jmodelica/examples/files/FMUs/bouncingBall.fmu')
#myModel.simulate()

#http://stackoverflow.com/questions/2418583/problem-importing-pylab-in-ubuntu-8-1
#import matplotlib

from jmodelica.examples import *

cstr.run_demo()
cstr_casadi.run_demo()