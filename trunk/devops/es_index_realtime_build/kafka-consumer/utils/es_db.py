#!/usr/bin/env python
#-*- coding:utf-8-*-

__author__ = 'spark'

import MySQLdb
import redis
import search_config
from elasticsearch import Elasticsearch

es_host_port = search_config.get_es_config()
conn_es = Elasticsearch(es_host_port)

def get_mysql_connector(biz_type):
    host, port , db, user, passwd = search_config.get_mysql_config(biz_type)
    conn_db = MySQLdb.connect(host=host, user=user, passwd=passwd, db=db, port=int(port), charset='utf8')
    print conn_db
    return conn_db

def get_conn_es():
    return conn_es

def get_conn_db_cursors(conn_db):
    return conn_db.cursor(cursorclass = MySQLdb.cursors.DictCursor)

def es_refresh():
    conn_es.refresh()

def es_init(mapping, index_name, type_name):
    conn_es.indices.create(index=index_name, ignore = 400)

def get_redis_connector():
    host, password, port, db = search_config.get_redis_config()
    conn_redis = redis.Redis(host=host, password=password, port=port, db=db)
    return conn_redis

