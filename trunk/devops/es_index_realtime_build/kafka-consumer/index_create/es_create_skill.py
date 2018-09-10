#!/usr/bin/env python
#-*- coding:utf-8-*-

from common.mysql_es_datatype import mysql_datetime, mysql_varchar, mysql_decimal, mysql_bigint, mysql_bit, mysql_int,mysql_text, mysql_tinyint
from common.const import type_skill, index_skill_name
from utils import es_db, log


def create_skill_index(conn_es):

    create_index_body = {
        'settings': {
            # just one shard, no replicas for testing
            'number_of_shards': 1,
            'number_of_replicas': 5
        },
        'mappings': {
            type_skill: {
                'properties': {
                    'id': {'type': 'text'},
                    'userId': {'type': 'text'},
                    'skillLabels': {'type': u'text', u'term_vector': 'with_positions_offsets'},
                    'levels': {'type': u'text', u'term_vector': 'with_positions_offsets'},
                    'description': {'type': 'keyword'},
                    'skillImagesUrl': {'type': 'text'},
                    'skillDetailImagesUrl': {'type': 'text'},
                    'unit': {'type': 'text'},
                    'amount': {'type': "float"},
                    'intr': {'type': 'text'},
                    'obj': {'type': 'integer'},
                    'location': {'type': 'geo_point'},
                    'registerCount': {'type': 'integer'},
                    'successCount': {'type': 'integer'},
                    'status': {'type': 'integer'},
                    'createTime': {'type': 'date'},
                    'nickname': {'type': u'text'},
                    'sex': {'type': u'text'},
                    'birthday': {'type': 'date'},
                    'mobile': {'type': 'text'},
                    'score': {'type': 'long'},
                    'credit': {'type': 'integer'},
                    'isLogin': {'type': 'integer'},
                    'lv': {'type': 'integer'},
                    'creditSum': {'type': 'integer'}
                }
            }
        }
    }
    if conn_es.indices.exists(index=index_skill_name):
        conn_es.indices.delete(index=index_skill_name, ignore=400)
    conn_es.indices.create(index=index_skill_name, body=create_index_body)

    log.log('create index %s succeed'% index_skill_name)


