#!/usr/bin/env python
# -*-coding:utf-8-*-
from utils import es_db


def countCheckNone(num):
    if num is None:
        return 0
    else:
        return num


def add_wish_suport_count_to_json_body(result, json_body):
    json_body['count'] = countCheckNone(result[0])
    return json_body



def add_wish_support(json_body, conn_db):
    wish_id = json_body['id']

    sql = 'SELECT COUNT(ws.id) AS count FROM wish_support AS ws WHERE ws.wish_id = \'' + wish_id + '\' GROUP BY ws.wish_id;'
    cur = conn_db.cursor()
    count = cur.execute(sql)
    results = cur.fetchmany(count)

    if(len(results) > 0):
        result = results[0]
        json_body = add_wish_suport_count_to_json_body(result, json_body)
    else:
        json_body['count'] = 0

    return json_body



def worth_add_wish_suport(result, worth_json):
    worth_json['dealsCount']=countCheckNone(result[0])
    worth_json['dealsTotal']=countCheckNone(result[1])/100


def add_worth_wish_suport(worth_json, conn_db):
    wish_id = worth_json['id']
    sql = ' select wish_id,count(*) as wCount,sum(amount) as total from wish_support where is_pay=1 and deleted=0 and wish_id =\'' + wish_id + '\' GROUP BY wish_id;'
    cur = conn_db.cursor()
    count = cur.execute(sql)
    results = cur.fetchmany(count)

    if (len(results) > 0):
        result = results[0]
        worth_json = add_wish_suport_count_to_json_body(result, worth_json)
    else:
        worth_json['dealsCount'] = 0
        worth_json['dealsTotal'] = 0

    return worth_json


