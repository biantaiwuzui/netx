#!/usr/bin/env python
#-*- coding:utf-8-*-
from common.const import index_worth_name
from utils import log


def create_worth_index(conn_es):

    create_index_body = {
        'settings': {
            # just one shard, no replicas for testing
            'number_of_shards': 1,
            'number_of_replicas': 5
        },
        'mappings': {
            'worth': {
                'properties': {
                    'id': {'type': 'text'},  # 网能id
                    'worthType': {'type': 'keyword'},  # 网能类型：【心愿、活动、技能、需求】
                    'title': {'type': 'keyword'},  # 网能标题
                    'detail': {'type': 'keyword'},  # 网能详情
                    'hit': {'type': 'integer'},  # 点击量
                    'publishTime': {'type': 'date'},  # 发布时间
                    'endTime': {'type': 'date'},  # 结束时间
                    'amount': {'type': 'double'},  # 金额
                    'dealsCount': {'type': 'integer'},  # 成交   数
                    'dealsTotal': {'type': 'double'},  # 成交总额
                    'imagesUrl': {'type': 'text'},  # 网能图片
                    'unit': {'type': 'text'},  # 单位
                    'isHoldCredit': {'type': 'integer'},  # 是否支持网信
                    'status': {'type': 'integer'},  # 状态
                    'location': {'type': 'geo_point'},  # 经纬度
                    'userId': {'type': 'keyword'},  # 发布者id
                    'headImg': {'type': 'text'},  # 发布者头像
                    'birthday': {'type': 'date'},  # 发布者生日
                    'nickname': {'type': 'keyword'},  # 发布者昵称
                    'lv': {'type': 'integer'},  # 发布者等级
                    'isLogin': {'type': 'integer'},  # 发布者的登录状态
                    'credit': {'type': 'integer'},  # 发布者信用
                    'loginAt': {'type': 'date'},  # 最后登录时间
                    'activeAt': {'type': 'date'},  # 最后操作时间
                    'sex': {'type': 'text'},  # 性别
                    'num': {'type': 'integer'},
                    'meetingType': {'type': 'integer'}
                }
            }
        }
    }

    if conn_es.indices.exists(index=index_worth_name):
        conn_es.indices.delete(index=index_worth_name, ignore=400)
    conn_es.indices.create(index=index_worth_name, body=create_index_body)
    log.log('create index %s succeed'% index_worth_name)