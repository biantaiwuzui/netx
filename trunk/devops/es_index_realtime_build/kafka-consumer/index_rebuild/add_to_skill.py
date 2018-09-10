#!/usr/bin/env python
# -*-coding:utf-8-*-
from utils import es_db


def countCheckNone(num):
    if num is None:
        return 0
    else:
        return num


def add_wish_suport_count_to_json_body(result, worth_json):
    worth_json['endTime']=result[1]
    worth_json['dealsCount']=countCheckNone(result[2])
    worth_json['dealsTotal']=countCheckNone(result[3])/100
    return worth_json


def add_worth_skill(skill_id, worth_json, conn_db):
    sql = 'select sr.skill_id, count(*) as sCount, sum(sorder.fee) as total,max(sorder.end_at) as end_at from skill_register as sr left join skill_order as sorder on sr.id = sorder.skill_register_id where sr.deleted=0 and sorder.deleted = 0 and skill_id =\' '+ skill_id + '\' group by sr.skill_id;'
    cur = conn_db.cursor()
    count = cur.execute(sql)
    results = cur.fetchmany(count)
    if (len(results) > 0):
        result = results[0]
        worth_json = add_wish_suport_count_to_json_body(result, worth_json)
    else:
        worth_json['endTime'] = '2188-01-01'
        worth_json['dealsCount'] = 0
        worth_json['dealsTotal'] = 0

    return worth_json


