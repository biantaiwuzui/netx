#!/usr/bin/env python
#-*- coding:utf-8-*-

from __future__ import division
import urllib2
import sys
import datetime
import time
import json
import math

from common import const
from utils import es_db, search_config, log, redisUtil

#设置输入流的编码格式
reload(sys)
sys.setdefaultencoding( "utf-8" )

conn_es = es_db.get_conn_es()
conn_db = es_db.get_mysql_connector('netx')#上线
#conn_db = es_db.get_mysql_connector('test_netx')#测试
#conn_db = es_db.get_mysql_connector('dev_netx')#开发
#conn_redis = es_db.get_redis_connector()

create_index_body = {
  'settings': {
    # just one shard, no replicas for testing
    'number_of_shards': 1,
    'number_of_replicas': 5
  },
  'mappings': {
    'wish': {
      'properties': {
        'id': {'type': 'text'},
        'userId': {'type': 'text'},
        'title': {'type': 'keyword'},
        'wishLabels': {'type': u'text', u'term_vector': 'with_positions_offsets'},
        'amount': {'type': 'float'},
        'currentAmount': {'type': 'float'},
        'currentApplyAmount': {'type': 'float'},
        'expiredAt': {'type': "date"},
        'refereeIds': {'type': 'text', 'term_vector': 'with_positions_offsets'},
        'refereeCount': {'type': 'integer'},
        'refereeAcceptCount': {'type': 'integer'},
        'refereeRefuseCount': {'type': 'integer'},
        'isLock': {'type': 'integer'},
        'description': {'type': 'text'},
        'imagesUrl': {'type': 'text'},
        'imagesTwoUrl': {'type': 'text'},
        'status': {'type': 'integer'},
        'createTime': {'type': 'date'},
        'location': {'type': 'geo_point'},
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
    return const.index_wish_name

if conn_es.indices.exists(index=get_index_name()):
    conn_es.indices.delete(index=get_index_name(), ignore=400)
conn_es.indices.create(index=get_index_name(), body= create_index_body)

def countCheckNone(num):
    if num is None:
        return 0
    else:
        return num

def create_one_wish(result):
    wish_json = {}
    wish_json['id'] = result[0]
    wish_json['userId'] = result[1]
    wish_json['title'] = result[2]
    wish_json['wishLabels'] = splitLabel(result[3])
    wish_json['amount'] = result[4]
    wish_json['currentAmount'] = result[5]
    wish_json['currentApplyAmount'] = result[6]
    wish_json['expiredAt'] = result[7]
    wish_json['refereeIds'] = splitLabel(result[8])
    wish_json['refereeCount'] = result[9]
    wish_json['refereeAcceptCount'] = result[10]
    wish_json['refereeRefuseCount'] = result[11]
    wish_json['description'] = result[12]
    wish_json['imagesUrl'] = result[13]
    wish_json['imagesTwoUrl'] = result[14]
    wish_json['status'] = result[15]
    wish_json['createTime'] =  result[16]
    wish_json['location'] = str(result[18]) +","+str(result[17])
    wish_json['nickname'] = result[19]
    wish_json['sex'] = result[20]
    wish_json['birthday'] = result[21]
    wish_json['mobile'] = result[22]
    wish_json['score'] = countCheckNone(result[23])
    wish_json['credit'] = countCheckNone(result[24])
    wish_json['isLogin'] = result[25]
    wish_json['lv'] = countCheckNone(result[26])
    wish_json['creditSum'] = countCheckNone(result[27])
    wish_json['count'] = countCheckNone(result[28])
    wish_json['isLock'] = countCheckNone(result[29])
    return wish_json

def splitLabel(ids):
    arr = []
    if not ids is None:
        idsArr = ids.split(",")
        for tagId in idsArr:
            arr.append(tagId)
    return arr

def dump_one_wish_index(result):
    wish_json = create_one_wish(result)
#    add_redisUserGeo(wish_json['userId'], wish_json)
    conn_es.create(index = get_index_name(), doc_type = const.type_wish, id = wish_json['id'], body = wish_json)

#def add_redisUserGeo(userId, wish_json):
#    lat,lon = redisUtil.get_user_LatAndLon(userId,conn_redis)
#    if not lat is None:
#        wish_json['location'] = str(lat) +","+str(lon)
#    else:
#        log.log(userId + ":location is none")
#    return wish_json

def dump_wish_index(sql):
    cur = conn_db.cursor()
    log.log(u'开始导入用户心愿表')
    print sql
    count = cur.execute(sql)
    log.log('there has %s rows wish record' % count)

    results = cur.fetchmany(count)
    index = 0
    errorIndex = 0
    for result in results:
        wishId = result[0]
        try:
            dump_one_wish_index(result)
        except Exception, e:
            log.log("dump wishId id: %s error" % wishId)
            print e
            errorIndex += 1

        index += 1
        if index % 50 == 0:
            log.log(u'已完成:'+ '%.2f' % (index / count * 100) + "%")
            time.sleep(0.5)

    log.log(u'完成用户心愿检索')
    log.log(u'错误行数：' + '%.2f' % errorIndex)
    cur.close()


def get_select_all_wish_sql():
    sql = 'SELECT w.id, w.user_id, w.title, w.wish_label, w.amount, w.current_amount, w.current_apply_amount, ' \
          ' w.expired_at, w.referee_ids, w.referee_count, w.referee_accept_count, w.referee_refuse_count, ' \
          ' w.description, w.wish_images_url, w.wish_images_two_url, w.status, w.create_time, u.lon, u.lat, ' \
          ' u.nickname, u.sex, u.birthday, u.mobile, u.score, u.credit, u.is_login+0, u.lv, c.credit_sum, ws.count, w.is_lock+0 ' \
          ' FROM wish AS w LEFT JOIN `user` AS u ON u.id = w.user_id ' \
          ' LEFT JOIN(SELECT credit.user_id, COUNT(credit.id) AS credit_sum FROM credit GROUP BY credit.user_id) ' \
          ' AS c ON c.user_id = w.user_id ' \
          ' LEFT JOIN(SELECT wish_support.wish_id, COUNT(wish_support.id) AS count FROM wish_support ' \
          ' GROUP BY wish_support.wish_id)AS ws ON ws.wish_id = w.id AND w.deleted = 0 '
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
    dump_wish_index(get_select_all_wish_sql())

    search_config.write_dump_time(const.type_wish, dump_time)

#获取


def params_err_log():
    print u"参数不对,请输入'all | deleteAll'"


if len(sys.argv) < 2:
    params_err_log()
    conn_db.close()
    exit(1)

param = sys.argv[1]
#param = 'all'
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