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
    'meeting': {
      'properties': {
        'id': {'type': 'text'},
        'userId': {'type': 'text'},
        'title': {'type': 'keyword'},
        'meetingType': {'type': 'integer'},
        'meetingLabels': {'type': u'text', u'term_vector': 'with_positions_offsets'},
        'startedAt': {'type': 'date'},
        'endAt': {'type': 'date'},
        'regStopAt': {'type': 'date'},
        'obj': {'type': "integer"},
        'objList': {'type': 'text'},
        'amount': {'type': 'float'},
        'address': {'type': 'text'},
        'orderIds': {'type': 'text'},
        'orderPrice': {'type': 'float'},
        'location': {'type': 'geo_point'},
        'description': {'type': 'text'},
        'meetingImagesUrl': {'type': 'text'},
        'posterImagesUrl': {'type': 'text'},
        'ceil': {'type': 'integer'},
        'floor': {'type': 'integer'},
        'regCount': {'type': 'integer'},
        'regSuccessCount': {'type': 'integer'},
        'status': {'type': 'integer'},
        'feeNotEnough': {'type': 'integer'},
        'isConfirm': {'type': 'integer'},
        'lockVersion': {'type': 'integer'},
        'allRegisterAmount': {'type': 'float'},
        'balance': {'type': 'float'},
        'isBalancePay': {'type': 'integer'},
        'payFrom': {'type': 'text'},
        'meetingFeePayAmount': {'type': 'float'},
        'meetingFeePayFrom': {'type': 'text'},
        'meetingFeePayType': {'type': 'text'},
        'nickname': {'type': u'text'},
        'sex': {'type': u'text'},
        'birthday': {'type': 'date'},
        'mobile': {'type': 'text'},
        'score': {'type': 'long'},
        'credit': {'type': 'integer'},
        'isLogin': {'type': 'integer'},
        'createTime': {'type': 'date'},
        'lv': {'type': 'integer'},
        'creditSum': {'type': 'integer'}
      }
    }
  }
}


def get_index_name():
    return const.index_meeting_name

if conn_es.indices.exists(index=get_index_name()):
    conn_es.indices.delete(index=get_index_name(), ignore=400)
conn_es.indices.create(index=get_index_name(), body= create_index_body)

def countCheckNone(num):
    if num is None:
        return 0
    else:
        return num


def create_one_meeting(result):
    meeting_json = {}
    meeting_json['id'] = result[0]
    meeting_json['userId'] = result[1]
    meeting_json['title'] = result[2]
    meeting_json['meetingType'] = result[3]
    meeting_json['meetingLabels'] = splitLabel(result[4])
    meeting_json['startedAt'] = result[5]
    meeting_json['endAt'] = result[6]
    meeting_json['regStopAt'] = result[7]
    meeting_json['obj'] = result[8]
    meeting_json['objList'] = result[9]
    meeting_json['amount'] = result[10]
    meeting_json['address'] = result[11]
    meeting_json['orderIds'] = result[12]
    meeting_json['orderPrice'] = result[13]
    meeting_json['location'] = str(result[14]) + "," + str(result[15])
    meeting_json['description'] = result[16]
    meeting_json['meetingImagesUrl'] = result[17]
    meeting_json['posterImagesUrl'] = result[18]
    meeting_json['ceil'] = result[19]
    meeting_json['floor'] = result[20]
    meeting_json['regCount'] = result[21]
    meeting_json['regSuccessCount'] = result[22]
    meeting_json['status'] = result[23]
    meeting_json['feeNotEnough'] = result[24]
    meeting_json['isConfirm'] = result[25]
    meeting_json['lockVersion'] = result[26]
    meeting_json['allRegisterAmount'] = result[27]
    meeting_json['balance'] = result[28]
    meeting_json['isBalancePay'] = result[29]
    meeting_json['payFrom'] = result[30]
    meeting_json['meetingFeePayAmount'] = result[31]
    meeting_json['meetingFeePayFrom'] = result[32]
    meeting_json['meetingFeePayType'] = result[33]
    meeting_json['nickname'] = result[34]
    meeting_json['sex'] = result[35]
    meeting_json['birthday'] = result[36]
    meeting_json['mobile'] = result[37]
    meeting_json['score'] = countCheckNone(result[38])
    meeting_json['credit'] = countCheckNone(result[39])
    meeting_json['isLogin'] = result[40]
    meeting_json['createTime'] = result[41]
    meeting_json['lv'] = countCheckNone(result[42])
    meeting_json['creditSum'] = countCheckNone(result[43])
    return meeting_json

def splitLabel(ids):
    arr = []
    if not ids is None:
        idsArr = ids.split(",")
        for tagId in idsArr:
            arr.append(tagId)
    return arr

def dump_one_meeting_index(result):
    meeting_json = create_one_meeting(result)
    conn_es.create(index = get_index_name(), doc_type = const.type_meeting, id = meeting_json['id'], body = meeting_json)

def dump_userFriend_index(sql):
    cur = conn_db.cursor()
    log.log(u'开始导入用户活动表')
    print sql
    count = cur.execute(sql)
    log.log('there has %s rows meeting record' % count)

    results = cur.fetchmany(count)
    index = 0
    errorIndex = 0
    for result in results:
        meetingId = result[0]
        try:
            dump_one_meeting_index(result)
        except Exception, e:
            log.log("dump meeting id: %s error" % meetingId)
            print e
            errorIndex += 1
        index += 1
        if index % 50 == 0:
            log.log(u'已完成:'+ '%.2f' % (index / count * 100) + "%")
            time.sleep(0.5)

    log.log(u'完成用户活动检索')
    log.log(u'错误行数：' + '%.2f' % errorIndex)
    cur.close()


def get_select_all_meeting_sql():
    sql = 'SELECT m.id, m.user_id, m.title, m.meeting_type, m.meeting_label, m.started_at, m.end_at, ' \
          ' m.reg_stop_at, m.obj, m.obj_list, m.amount, m.address, m.order_ids, m.order_price, m.lat, ' \
          ' m.lon, m.description, m.meeting_images_url, m.poster_images_url, m.ceil, m.floor, m.reg_count, m.reg_success_count, ' \
          ' m.status, m.fee_not_enough, m.is_confirm+0, m.lock_version, m.all_register_amount, m.balance, ' \
          ' m.is_balance_pay+0, m.pay_from, m.meeting_fee_pay_amount, m.meeting_fee_pay_from, m.meeting_fee_pay_type, ' \
          ' u.nickname, u.sex, u.birthday, u.mobile, u.score, u.credit, u.is_login+0, m.create_time, u.lv, c.credit_sum ' \
          ' FROM meeting AS m LEFT JOIN `user` AS u ON u.id = m.user_id ' \
          ' LEFT JOIN(SELECT credit.user_id, COUNT(credit.id)+0 AS credit_sum FROM credit GROUP BY credit.user_id) ' \
          ' AS c ON c.user_id = m.user_id AND m.deleted = 0 '
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
    dump_userFriend_index(get_select_all_meeting_sql())

    search_config.write_dump_time(const.type_meeting, dump_time)

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