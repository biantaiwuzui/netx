#!/usr/bin/env python
#-*- coding:utf-8-*-
import es_db

def get_user_geo(userId,conn_redis = None):
    if conn_redis is None:
        conn_redis = es_db.get_redis_connector()
    geo = conn_redis.geopos('UserGeo',userId)
    if geo[0] is None:
        geo = conn_redis.geopos('UserGeo','"'+userId+'"')
    if not geo is None:
        geo = geo[0]
    return geo

def get_user_LatAndLon(userId,conn_redis = None):
    geo = get_user_geo(userId,conn_redis)
    if geo is None:
        return None,None
    else:
        return geo[1],geo[0]
    pass