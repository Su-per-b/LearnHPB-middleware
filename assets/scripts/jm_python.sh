#!/bin/sh

if test "${JAVA_HOME}" = ""; then
  export JAVA_HOME=/usr/lib/jvm/java-6-openjdk/
fi
JMODELICA_HOME=/usr/local/jmodelica \
IPOPT_HOME=/var/tmp/CoinIpopt \
CPPAD_HOME=/var/tmp/JModelica/ThirdParty/CppAD/cppad-20100325/ \
SUNDIALS_HOME=/usr/local \
PYTHONPATH=:/usr/local/jmodelica/Python/: \
LD_LIBRARY_PATH=:/var/tmp/CoinIpopt/lib/:/usr/local/lib \
python $@
