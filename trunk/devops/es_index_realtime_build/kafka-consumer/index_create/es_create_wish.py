#!/usr/bin/env python
#-*- coding:utf-8-*-

from common.const import index_wish_name, type_wish
from utils import es_db, log
from common.mysql_es_datatype import mysql_datetime, mysql_varchar, mysql_decimal, mysql_bigint, mysql_bit, mysql_int, mysql_text, mysql_tinyint


def create_wish_index(conn_es):
    create_index_body = {
        'settings': {
            # just one shard, no replicas for testing
            'number_of_shards': 1,
            'number_of_replicas': 5
        },
        'mappings': {
            type_wish: {
                'properties': {
                    'id': {'type': 'text'},
                    'userId': {'type': 'text'},
                    'title': {'type': 'keyword'},
                    'wishLabels': {'type': u'text', u'term_vector': 'with_positions_offsets'},
                    'amount': {'type': 'float'},
                    'currentAmount': {'type': 'float'},
                    'currentApplyAmount': {'type': 'float'},
                    'expiredAt': {'type': "date"},
                    'refereeIds': {'type': 'text', 'term_vector': 'with_positions_offsets'},
                    'refereeCount': {'type': 'integer'},
                    'refereeAcceptCount': {'type': 'integer'},
                    'refereeRefuseCount': {'type': 'integer'},
                    'isLock': {'type': 'integer'},
                    'description': {'type': 'text'},
                    'imagesUrl': {'type': 'text'},
                    'imagesTwoUrl': {'type': 'text'},
                    'status': {'type': 'integer'},
                    'createTime': {'type': 'date'},
                    'location': {'type': 'geo_point'},
                    'nickname': {'type': u'text'},
                    'sex': {'type': u'text'},
                    'birthday': {'type': 'date'},
                    'mobile': {'type': 'text'},
                    'score': {'type': 'long'},
                    'credit': {'type': 'integer'},
                    'isLogin': {'type': 'integer'},
                    'lv': {'type': 'integer'},
                    'creditSum': {'type': 'integer'},
                    'count': {'type': 'integer'}
                }
            }
        }
    }
    if conn_es.indices.exists(index=index_wish_name):
        conn_es.indices.delete(index=index_wish_name, ignore=400)
    conn_es.indices.create(index=index_wish_name, body=create_index_body)
    log.log('create index %s succeed'% index_wish_name)

