#!/bin/bash

# echo "Install bin"
# pushd bin
# install.sh
# popd

#canal


#kakfa
echo "Install kafka"
pushd kafka
chmod a+x install.sh
install.sh
popd

#nginx
# echo "Install nginx"
# pushd nginx
# install.sh
# popd
