#!/bin/sh

if test "${JAVA_HOME}" = ""; then
  export JAVA_HOME=/usr/lib/jvm/java-6-openjdk/
fi
JMODELICA_HOME=/opt/packages/jmodelica/jmodelica-install \
IPOPT_HOME=/opt/packages/coin-or/ipopt-install \
CPPAD_HOME=/var/tmp/JModelica/ThirdParty/CppAD/cppad-20100325/ \
SUNDIALS_HOME=/opt/packages/sundials/sundials-install \
PYTHONPATH=:/opt/packages/jmodelica/jmodelica-install/Python/:/opt/packages/casadi/build/swig/python \
LD_LIBRARY_PATH=:/opt/packages/coin-or/ipopt-install/lib/:/opt/packages/sundials/sundials-install/lib/:/opt/packages/jmodelica/jmodelica-install/Python/assimulo/lib \
ipython $@
