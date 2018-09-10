#!/usr/bin/env python
# -*-coding:utf-8-*-

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
#conn_db = es_db.get_mysql_connector('netx')#上线
#conn_db = es_db.get_mysql_connector('test_netx')#测试
conn_db = es_db.get_mysql_connector('dev_netx')#开发



create_index_body = {
    'settings': {
        # just one shard, no replicas for testing
        'number_of_shards': 1,
        'number_of_replicas': 5
    },
    'mappings': {
        'match': {
            'properties': {
                'matchId': {'type': 'text'},
                'matchImageUrl': {'type': 'text'},
                'title': {'type': 'keyword'},
                'subTitle': {'type': 'keyword'},
                'matchKind': {'type': 'integer'},
                'matchRule': {'type': 'text'},
                'grading': {'type': 'text'},
                'matchIntroduction': {'type': "text"},
                'matchStatus': {'type': 'integer'},
                'publishTime': {'type': 'date'},
                'nickname': {'type': u'text'},
                'sex': {'type': u'text'},
                'birthday': {'type': 'date'},
                'mobile': {'type': 'text'},
                'score': {'type': 'long'},
                'credit': {'type': 'integer'},
                'isLogin': {'type': 'integer'},
                'lv': {'type': 'integer'},
                'creditSum': {'type': 'integer'},
                'pictureUrl': {'type': 'text'},
                'merchantId': {'type': 'text'},
                'organizerName': {'type': 'text'},
                'location': {'type': 'geo_point'},
                'initiatorId':{'type':'text'},
                'regCount':{'type':'integer'}
            }
        }
    }
}

def get_index_name():
    return const.index_match_name

if conn_es.indices.exists(index=get_index_name()):
    conn_es.indices.delete(index=get_index_name(), ignore=400)
conn_es.indices.create(index=get_index_name(), body= create_index_body)

def countCheckNone(num):
    if num is None:
        return 0
    else:
        return num

def create_one_match(result):
    matchId_json = {}
    matchId_json['id'] = result[0]
    matchId_json['matchImageUrl'] = splitLabel(result[1])
    matchId_json['title'] = result[2]
    matchId_json['subTitle'] = result[3]
    matchId_json['matchKind'] = result[4]
    matchId_json['matchRule'] = result[5]
    matchId_json['grading'] = result[6]
    matchId_json['matchIntroduction'] = result[7]
    matchId_json['matchStatus'] = result[8]
    matchId_json['publishTime'] = result[9]
    matchId_json['nickname'] = result[10]
    matchId_json['sex'] = result[11]
    matchId_json['birthday'] = result[12]
    matchId_json['mobile'] = result[13]
    matchId_json['score'] = countCheckNone(result[14])
    matchId_json['credit'] = countCheckNone(result[15])
    matchId_json['isLogin'] = result[16]
    matchId_json['lv'] = countCheckNone(result[17])
    matchId_json['creditSum'] = countCheckNone(result[18])
    matchId_json['pictureUrl'] = result[19]
    matchId_json['merchantId'] = result[20]
    matchId_json['organizerName'] = result[21]
    matchId_json['location'] = str(result[23]) + "," + str(result[22])
    # print (matchId_json['location'])
    matchId_json['initiatorId'] = result[24]
    matchId_json['regCount'] = result[25]
    return matchId_json


def splitLabel(ids):
    arr = []
    if not ids is None:
        idsArr = ids.split(",")
        for tagId in idsArr:
            arr.append(tagId)
    return arr

def dump_one_match_index(result):
    match_json = create_one_match(result)
    conn_es.create(index = get_index_name(), doc_type = const.type_match, id = match_json['id'], body = match_json)


def dump_userFriend_index(sql):
    cur = conn_db.cursor()
    log.log(u'开始导入用户赛事活动表')
    # print sql
    count = cur.execute(sql)
    log.log('there has %s rows match record' % count)

    results = cur.fetchmany(count)
    index = 0
    errorIndex = 0
    for result in results:
        matchId = result[0]
        try:
            dump_one_match_index(result)
        except Exception, e:
            log.log("dump match id: %s error" % matchId)
            print e
            errorIndex += 1
        index += 1
        if index % 50 == 0:
            log.log(u'已完成:'+ '%.2f' % (index / count * 100) + "%")
            time.sleep(0.5)

    log.log(u'完成用户活动检索')
    log.log(u'错误行数：' + '%.2f' % errorIndex)
    cur.close()


def get_select_all_matchId_sql():
    sql = 'SELECT a.id, a.match_image_url, a.title, a.sub_title, a.match_kind, ' \
          'a.match_rule, a.grading, a.match_introduction, a.match_status, a.pass_time,' \
          ' b.nickname, b.sex, b.birthday, b.mobile, b.score, b.credit, b.is_login + 0, b.lv, ' \
          'c.credit_sum, d.picture_url, d.merchant_id, d.organizer_name,b.lon,b.lat, a.initiator_id ,' \
          '( SELECT COUNT(*) FROM match_participant p INNER JOIN match_zone z ON p.zone_id = z.id WHERE ' \
          'p.is_pay = 1 AND z.match_id = a.id ) regCount FROM match_event a INNER JOIN user b' \
          ' ON a.initiator_id = b.id LEFT JOIN ( SELECT credit.user_id, COUNT(credit.id) + 0 AS credit_sum ' \
          'FROM credit GROUP BY credit.user_id ) c ON a.id = c.user_id LEFT JOIN ( SELECT a.merchant_id, ' \
          'a.organizer_name, b.picture_url, a.match_id FROM match_review a INNER JOIN ( SELECT picture_url,' \
          ' merchant_id FROM merchant_picture WHERE merchant_picture_type = \'logo\' ) b ON a.merchant_id = b.merchant_id' \
          ' AND a.organizer_kind = 0 ) d ON a.id = d.match_id WHERE a.is_approved = 1'
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
    dump_userFriend_index(get_select_all_matchId_sql())

    search_config.write_dump_time(const.type_match, dump_time)

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