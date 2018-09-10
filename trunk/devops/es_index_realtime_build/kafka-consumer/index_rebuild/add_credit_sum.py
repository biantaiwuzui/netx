#!/usr/bin/env python
# -*-coding:utf-8-*-
from utils import es_db

def splitLabel(ids):
    arr = []
    if not ids is None:
        idsArr = ids.split(",")
        for tagId in idsArr:
            arr.append(tagId)
    return arr

def countCheckNone(num):
    if num is None:
        return 0
    else:
        return num


def add_from_result(result, json_body):
    json_body['creditSum'] = countCheckNone(result[0])
    return json_body


def add_to_worth_result(result, json_body):
    json_body['isHoldCredit'] = countCheckNone(result[0])
    return json_body



def add_credit_sum(json_body, conn_db):
    user_id = json_body['userId']
    sql = 'select COUNT(c.id)+0 AS credit_sum FROM credit as c where c.user_id = \''+ user_id + '\' GROUP BY c.user_id ;'

    cur = conn_db.cursor()
    count = cur.execute(sql)
    results = cur.fetchmany(count)
    if(len(results) > 0):
        result = results[0]
        json_body = add_from_result(result, json_body)
    else:
        json_body['creditSum'] = 0

    return json_body

def add_credit_sum_for_worth(json_body, conn_db, user_id):
    sql = 'select COUNT(c.id)+0 AS credit_sum FROM credit as c where c.user_id = \''+ user_id + '\' GROUP BY c.user_id ;'

    cur = conn_db.cursor()
    count = cur.execute(sql)
    results = cur.fetchmany(count)
    if(len(results) > 0):
        result = results[0]
        json_body = add_to_worth_result(result, json_body)
    else:
        json_body['isHoldCredit'] = 0

    return json_body