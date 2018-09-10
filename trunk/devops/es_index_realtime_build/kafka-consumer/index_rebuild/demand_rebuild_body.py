#!/usr/bin/env python
# -*-coding:utf-8-*-
from index_rebuild.add_credit_sum import add_credit_sum, add_credit_sum_for_worth
from index_rebuild.add_hit import add_hit
from index_rebuild.add_to_demand import add_demand_register, add_demand_order_count, add_demand_register_count
from index_rebuild.add_user import add_user, add_user_to_worth
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

def create_one_demand(result):
    one_demand_json = {}
    one_demand_json['id'] = result[0]
    one_demand_json['userId'] = result[1]
    one_demand_json['title'] = result[2]
    one_demand_json['demandType'] = result[3]
    one_demand_json['demandLabels'] = splitLabel(result[4])
    one_demand_json['isOpenEnded'] = result[5]
    one_demand_json['startAt'] = result[6]
    one_demand_json['endAt'] = result[7]
    one_demand_json['about'] = result[8]
    one_demand_json['amount'] = result[9]
    one_demand_json['unit'] = result[10]
    one_demand_json['obj'] = result[11]
    one_demand_json['objList'] = splitLabel(result[12])
    one_demand_json['address'] = result[13]
    one_demand_json['location'] = str(result[14]) + "," + str(result[15])
    one_demand_json['description'] = result[16]
    one_demand_json['imagesUrl'] = result[17]
    one_demand_json['detailsImagesUrl'] = result[18]
    one_demand_json['orderIds'] = result[19]
    one_demand_json['orderPrice'] = result[20]
    one_demand_json['isPickUp'] = result[21]
    one_demand_json['wage'] = result[22]
    one_demand_json['isEachWage'] = result[23]
    one_demand_json['bail'] = result[24]
    one_demand_json['status'] = result[25]
    one_demand_json['isPay'] = result[26]
    one_demand_json['createTime'] = result[27]
    worth_json = {}
    worth_json['id'] = result[0]
    worth_json['worthType'] = 'Demand'
    worth_json['title'] = result[2]
    worth_json['detail'] = result[16]
    worth_json['publishTime'] = result[27]
    worth_json['endTime'] = result[7]
    worth_json['amount'] = countCheckNone(result[24]) / 100
    worth_json['imgesUrl'] = result[17]
    worth_json['location'] = str(result[14]) + "," + str(result[15])
    worth_json['status'] = result[25]
    worth_json['unit'] = result[10]
    worth_json['num'] = countCheckNone(result[9])

    return one_demand_json, worth_json

def get_demand(id, no_cache, conn_db):
    demand_sql = 'select d.id, d.user_id, d.title, d.demand_type, d.demand_label, d.is_open_ended+0 ' \
                 'as is_open_ended,  d.start_at, d.end_at, d.about, d.amount, d.unit, d.obj, d.obj_list, ' \
                 'd.address, d.lat, d.lon, d.description,  d.images_url, d.details_images_url, d.order_ids, ' \
                 'd.order_price, d.is_pick_up+0, d.wage, d.is_each_wage+0,  d.bail, d.status, d.is_pay+0, ' \
                 'd.create_time ' \
                 'from demand as d WHERE d.id = \''+ id + '\''+ ' AND d.deleted = 0'

    cur = conn_db.cursor()
    count = cur.execute(demand_sql)
    results = cur.fetchmany(count)
    if(len(results) > 0):
        result = results[0]
        demand_json, worth_body = create_one_demand(result)
        return demand_json, worth_body
    else:
        return None, None


def demand_rebuild(id, no_cache):
    conn_db = es_db.get_mysql_connector('netx')  # 上线
    json_body, worth_body = get_demand(id, no_cache, conn_db)
    #deleted=1的情况
    if(json_body == None):
        return None, None
 #构造demand_json
    json_body = add_user(json_body, conn_db)
    json_body = add_credit_sum(json_body, conn_db)
    json_body = add_demand_register(json_body, conn_db)

# 构造worth_body
    user_id = json_body['userId']
    demand_id = json_body['id']
    # 注入用户信息
    worth_body = add_user_to_worth(worth_body, conn_db, True, user_id)
    #注入hit
    worth_body =  add_hit('Demand', worth_body, conn_db)
    #注入demand_order
    worth_body = add_demand_order_count(worth_body, demand_id, conn_db)
    #注入demand_register
    worth_body = add_demand_register_count(worth_body, demand_id, conn_db)
    #注入credit_sum
    worth_body = add_credit_sum_for_worth(worth_body, conn_db, user_id)
    conn_db.close()
    return json_body, worth_body

