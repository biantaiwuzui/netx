NETX_BASE=/opt/netx
NETX_BIN=$NETX_BASE/bin 

if [ ! -d $NETX_BIN ]; then
	mkdir -p $NETX_BIN
fi

cp -r nginx $NETX_BIN
cp -r kafka $NETX_BIN
