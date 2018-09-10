#!/usr/bin/env python
#-*- coding:utf-8-*-



from common.mysql_es_datatype import mysql_datetime, mysql_varchar, mysql_decimal, mysql_bigint, mysql_bit, mysql_int, mysql_tinyint, mysql_char
from common.const import index_meeting_name, type_meeting
from utils import es_db, log


def create_meeting_index(conn_es):

    create_index_body = {
        'settings': {
            # just one shard, no replicas for testing
            'number_of_shards': 1,
            'number_of_replicas': 5
        },
        'mappings': {
            type_meeting: {
                'properties': {
                    'id': {'type': 'text'},
                    'userId': {'type': 'text'},
                    'title': {'type': 'keyword'},
                    'meetingType': {'type': 'integer'},
                    'meetingLabels': {'type': u'text', u'term_vector': 'with_positions_offsets'},
                    'startedAt': {'type': 'date'},
                    'endAt': {'type': 'date'},
                    'regStopAt': {'type': 'date'},
                    'obj': {'type': "integer"},
                    'objList': {'type': 'text'},
                    'amount': {'type': 'float'},
                    'address': {'type': 'text'},
                    'orderIds': {'type': 'text'},
                    'orderPrice': {'type': 'float'},
                    'location': {'type': 'geo_point'},
                    'description': {'type': 'text'},
                    'meetingImagesUrl': {'type': 'text'},
                    'posterImagesUrl': {'type': 'text'},
                    'ceil': {'type': 'integer'},
                    'floor': {'type': 'integer'},
                    'regCount': {'type': 'integer'},
                    'regSuccessCount': {'type': 'integer'},
                    'status': {'type': 'integer'},
                    'feeNotEnough': {'type': 'integer'},
                    'isConfirm': {'type': 'integer'},
                    'lockVersion': {'type': 'integer'},
                    'allRegisterAmount': {'type': 'float'},
                    'balance': {'type': 'float'},
                    'isBalancePay': {'type': 'integer'},
                    'payFrom': {'type': 'text'},
                    'meetingFeePayAmount': {'type': 'float'},
                    'meetingFeePayFrom': {'type': 'text'},
                    'meetingFeePayType': {'type': 'text'},
                    'nickname': {'type': u'text'},
                    'sex': {'type': u'text'},
                    'birthday': {'type': 'date'},
                    'mobile': {'type': 'text'},
                    'score': {'type': 'long'},
                    'credit': {'type': 'integer'},
                    'isLogin': {'type': 'integer'},
                    'createTime': {'type': 'date'},
                    'lv': {'type': 'integer'},
                    'creditSum': {'type': 'integer'}
                }
            }
        }
    }
    if conn_es.indices.exists(index=index_meeting_name):
        conn_es.indices.delete(index=index_meeting_name, ignore=400)
    conn_es.indices.create(index=index_meeting_name, body=create_index_body)

    log.log('create index %s succeed'% index_meeting_name)


