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
    json_body['hit'] = countCheckNone(result[0])
    return json_body


def add_hit(type, json_body, conn_db):
    user_id = json_body['id']
    sql = 'select count(*) as hit from worth_click_history where type_name= \''+ type + ' \'and deleted=0 and type_id=\' '+ user_id + '\'group by type_id;'

    cur = conn_db.cursor()
    count = cur.execute(sql)
    results = cur.fetchmany(count)
    if(len(results) > 0):
        result = results[0]
        json_body = add_from_result(result, json_body)
    else:
        json_body['hit'] = 0

    return json_body
