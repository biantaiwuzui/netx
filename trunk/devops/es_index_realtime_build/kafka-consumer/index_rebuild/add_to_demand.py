#!/usr/bin/env python
# -*-coding:utf-8-*-
from utils import es_db

def countCheckNone(num):
    if num is None:
        return 0
    else:
        return num

def add_demand_registe_count_to_json_body(result, demand_json):
    demand_json['count'] = countCheckNone(result[0])
    return demand_json

def add_worth_order_count(result, worth_json):
    worth_json['dealsTotal'] = countCheckNone(result[1]) / 100
    return worth_json

def add_worth_register_count(result, worth_json):
    worth_json['dealsCount'] = countCheckNone(result[1])
    return worth_json

def add_demand_register(demand_body, conn_db):
    demand_id = demand_body['id']
    sql = 'SELECT COUNT(demand_register.id) AS count FROM demand_register   WHERE demand_register.demand_id =\'' + demand_id + '\' GROUP BY demand_register.demand_id;'
    cur = conn_db.cursor()
    count = cur.execute(sql)
    results = cur.fetchmany(count)
    if(len(results) > 0):
        result = results[0]
        demand_body = add_demand_registe_count_to_json_body(result, demand_body)
    else:
        demand_body['count'] = 0

    return demand_body


def add_demand_order_count(demand_body, demand_id, conn_db):
    sql = 'select demand_id, count(*) as DCount, sum(bail) as total from demand_order where demand_id =\' ' + demand_id + '\' and is_pay = 1 and deleted = 0 group by demand_id;'
    cur = conn_db.cursor()
    count = cur.execute(sql)
    results = cur.fetchmany(count)
    if (len(results) > 0):
        result = results[0]
        demand_body = add_worth_order_count(result, demand_body)
    else:
        demand_body['dealsTotal'] = 0
    return demand_body


def add_demand_register_count(demand_body, demand_id, conn_db):
    sql = 'select demand_id,count(*) as dCount from demand_register where deleted=0 and demand_id = \'' + demand_id + '\'group by demand_id;'
    cur = conn_db.cursor()
    count = cur.execute(sql)
    results = cur.fetchmany(count)
    if(len(results) > 0):
        result = results[0]
        demand_body = add_worth_register_count(result, demand_body)
    else:
        demand_body['dealsCount'] = 0

    return demand_body