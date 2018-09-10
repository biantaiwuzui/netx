package com.netx.session;

import java.util.HashMap;
import java.util.Map;


public class SessionStoreFactory {

    private Map<StoreType, Class<? extends SessionStore>> storeTypeClassMap = new HashMap<StoreType, Class<? extends SessionStore>>();

    public SessionStoreFactory(HashMap<StoreType, Class<? extends SessionStore>> storeTypeClassMap) {
        this.storeTypeClassMap = storeTypeClassMap;
    }

    public Map<StoreType, SessionStore> createSessionStores(NetxSessionServletRequest ecommerceRequest, NetxSessionServletResponse ecommerceResponse) {

        Map<StoreType, SessionStore> sessionStores = new HashMap<StoreType, SessionStore>();
        for (Map.Entry<StoreType, Class<? extends SessionStore>> entry : storeTypeClassMap.entrySet()) {
            try {
                sessionStores.put(entry.getKey(), entry.getKey().newSessionStore(ecommerceRequest, ecommerceResponse));
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
        return sessionStores;

    }
}
