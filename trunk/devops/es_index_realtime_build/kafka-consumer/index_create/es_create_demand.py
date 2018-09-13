#!/usr/bin/env python
#-*- coding:utf-8-*-


from common.const import type_demand, index_demand_name

from utils import log


def create_demand_index(conn_es):

    create_index_body = {
        'settings': {
            # just one shard, no replicas for testing
            'number_of_shards': 1,
            'number_of_replicas': 5
        },
        'mappings': {
            type_demand: {
                'properties': {
                    'id': {'type': 'text'},
                    'userId': {'type': 'text'},
                    'title': {'type': 'keyword'},
                    'demandType': {'type': 'integer'},
                    'meetingLabels': {'type': u'text', u'term_vector': 'with_positions_offsets'},
                    'isOpenEnded': {'type': 'integer'},
                    'startAt': {'type': 'date'},
                    'endAt': {'type': 'date'},
                    'about': {'type': "text"},
                    'amount': {'type': 'integer'},
                    'unit': {'type': 'text'},
                    'obj': {'type': 'integer'},
                    'objList': {'type': 'text', 'term_vector': 'with_positions_offsets'},
                    'address': {'type': 'text'},
                    'location': {'type': 'geo_point'},
                    'description': {'type': 'text'},
                    'demandImagesUrl': {'type': 'text'},
                    'demandDetailsImagesUrl': {'type': 'text'},
                    'orderIds': {'type': 'text'},
                    'orderPrice': {'type': 'float'},
                    'isPickUp': {'type': 'integer'},
                    'wage': {'type': 'float'},
                    'isEachWage': {'type': 'integer'},
                    'bail': {'type': 'float'},
                    'status': {'type': 'integer'},
                    'isPay': {'type': 'integer'},
                    'createTime': {'type': 'date'},
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
    if conn_es.indices.exists(index=index_demand_name):
        conn_es.indices.delete(index=index_demand_name, ignore=400)
    conn_es.indices.create(index=index_demand_name, body=create_index_body)

    log.log('create index %s succeed'% index_demand_name)

