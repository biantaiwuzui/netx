#!/usr/bin/env python
#-*- coding:utf-8-*-

from __future__ import division
import urllib2
import sys
import datetime
import time
import json

from common import const
from utils import es_db, search_config, log, redisUtil


#设置输入流的编码格式
reload(sys)
sys.setdefaultencoding( "utf-8" )

conn_es = es_db.get_conn_es()
conn_db = es_db.get_mysql_connector('netx')#上线
#conn_db = es_db.get_mysql_connector('test_netx')#测试
#conn_db = es_db.get_mysql_connector('dev_netx')#开发


create_index_body = {
  'settings': {
    # just one shard, no replicas for testing
    'number_of_shards': 1,
    'number_of_replicas': 5
  },
  'mappings': {
    'demand': {
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


def get_index_name():
    return const.index_demand_name

if conn_es.indices.exists(index=get_index_name()):
    conn_es.indices.delete(index=get_index_name(), ignore=400)
conn_es.indices.create(index=get_index_name(), body= create_index_body)

def countCheckNone(num):
    if num is None:
        return 0
    else:
        return num

def create_one_demand(result):
    demand_json = {}
    demand_json['id'] = result[0]
    demand_json['userId'] = result[1]
    demand_json['title'] = result[2]
    demand_json['demandType'] = result[3]
    demand_json['demandLabels'] = splitLabel(result[4])
    demand_json['isOpenEnded'] = result[5]
    demand_json['startAt'] = result[6]
    demand_json['endAt'] = result[7]
    demand_json['about'] = result[8]
    demand_json['amount'] = result[9]
    demand_json['unit'] = result[10]
    demand_json['obj'] = result[11]
    demand_json['objList'] = splitLabel(result[12])
    demand_json['address'] = result[13]
    demand_json['location'] = str(result[14]) + "," + str(result[15])
    demand_json['description'] = result[16]
    demand_json['imagesUrl'] = result[17]
    demand_json['detailsImagesUrl'] = result[18]
    demand_json['orderIds'] = result[19]
    demand_json['orderPrice'] = result[20]
    demand_json['isPickUp'] = result[21]
    demand_json['wage'] = result[22]
    demand_json['isEachWage'] = result[23]
    demand_json['bail'] = result[24]
    demand_json['status'] = result[25]
    demand_json['isPay'] = result[26]
    demand_json['createTime'] = result[27]
    demand_json['nickname'] = result[28]
    demand_json['sex'] = result[29]
    demand_json['birthday'] = result[30]
    demand_json['mobile'] = result[31]
    demand_json['score'] = countCheckNone(result[32])
    demand_json['credit'] = countCheckNone(result[33])
    demand_json['isLogin'] = result[34]
    demand_json['lv'] = countCheckNone(result[35])
    demand_json['creditSum'] = countCheckNone(result[36])
    demand_json['count'] = countCheckNone(result[37])

    return demand_json

def splitLabel(ids):
    arr = []
    if not ids is None:
        idsArr = ids.split(",")
        for tagId in idsArr:
            arr.append(tagId)
    return arr

def dump_one_demand_index(result):
    demand_json = create_one_demand(result)
    conn_es.create(index = get_index_name(), doc_type = const.type_demand, id = demand_json['id'], body = demand_json)

def dump_demand_index(sql):
    cur = conn_db.cursor()
    log.log(u'开始导入用户需求表')
    print sql
    count = cur.execute(sql)
    log.log('there has %s rows demand record' % count)

    results = cur.fetchmany(count)
    index = 0
    errorIndex = 0
    for result in results:
        demandId = result[0]
        try:
            dump_one_demand_index(result)
        except Exception, e:
            log.log("dump demand id: %s error" % demandId)
            print e
            errorIndex += 1
        index += 1
        if index % 50 == 0:
            log.log(u'已完成:'+ '%.2f' % (index / count * 100) + "%")
            time.sleep(0.5)

    log.log(u'完成用户需求检索')
    log.log(u'错误行数：' + '%.2f' % errorIndex)
    cur.close()


def get_select_all_demand_sql():
    sql = 'select d.id, d.user_id, d.title, d.demand_type, d.demand_label, d.is_open_ended+0 as is_open_ended, ' \
          ' d.start_at, d.end_at, d.about, d.amount, d.unit, d.obj, d.obj_list, d.address, d.lat, d.lon, d.description, ' \
          ' d.images_url, d.details_images_url, d.order_ids, d.order_price, d.is_pick_up+0, d.wage, d.is_each_wage+0, ' \
          ' d.bail, d.status, d.is_pay+0, d.create_time, ' \
          ' u.nickname, u.sex, u.birthday, u.mobile, u.score, u.credit, u.is_login+0, u.lv, c.credit_sum, dr.count ' \
          ' from demand as d LEFT JOIN `user` AS u ON u.id = d.user_id ' \
          ' LEFT JOIN(SELECT credit.user_id, COUNT(credit.id)+0 AS credit_sum FROM credit GROUP BY credit.user_id) ' \
          ' AS c ON c.user_id = d.user_id ' \
          ' LEFT JOIN(SELECT demand_register.demand_id, COUNT(demand_register.id) AS count FROM demand_register ' \
          ' GROUP BY demand_register.demand_id)AS dr ON dr.demand_id = d.id AND d.deleted = 0 '
    return sql


def delete_all_index():
    conn_es.indices.delete(index=get_index_name(), ignore=[400, 404])

#全量索引
def dump_all_index():
    #es_db.es_init(seller_mapping, get_index_name(), const.type_seller)
    #log.log(u"先清理之前遗留的数据")
    #delete_all_index()
    from_time = '1970-01-01'
    dump_time = datetime.datetime.now()
    dump_demand_index(get_select_all_demand_sql())

    search_config.write_dump_time(const.type_demand, dump_time)

#获取


def params_err_log():
    print u"参数不对,请输入'all | deleteAll'"


#if len(sys.argv) < 2:
#    params_err_log()
#    conn_db.close()
#    exit(1)

#param = sys.argv[1]
param = 'all'
if param == 'all':
    #导入中文简体搜索库
    dump_all_index()
elif param == 'deleteAll':
    print 'deleteAll'
    log.log(u"清理此索引所有数据")
    delete_all_index()
    log.log(u"清理成功")
else:
    params_err_log()

conn_db.close()