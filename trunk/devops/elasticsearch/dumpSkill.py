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
    'skill': {
      'properties': {
        'id': {'type': 'text'},
        'userId': {'type': 'text'},
        'skillLabels': {'type': u'text', u'term_vector': 'with_positions_offsets'},
        'levels': {'type': u'text', u'term_vector': 'with_positions_offsets'},
        'description': {'type': 'keyword'},
        'skillImagesUrl': {'type': 'text'},
        'skillDetailImagesUrl': {'type': 'text'},
        'unit': {'type': 'text'},
        'amount': {'type': "float"},
        'intr': {'type': 'text'},
        'obj': {'type': 'integer'},
        'location': {'type': 'geo_point'},
        'registerCount': {'type': 'integer'},
        'successCount': {'type': 'integer'},
        'status': {'type': 'integer'},
        'createTime': {'type': 'date'},
        'nickname': {'type': u'text'},
        'sex': {'type': u'text'},
        'birthday': {'type': 'date'},
        'mobile': {'type': 'text'},
        'score': {'type': 'long'},
        'credit': {'type': 'integer'},
        'isLogin': {'type': 'integer'},
        'lv': {'type': 'integer'},
        'creditSum': {'type': 'integer'}
      }
    }
  }
}


def get_index_name():
    return const.index_skill_name

if conn_es.indices.exists(index=get_index_name()):
    conn_es.indices.delete(index=get_index_name(), ignore=400)
conn_es.indices.create(index=get_index_name(), body= create_index_body)

def countCheckNone(num):
    if num is None:
        return 0
    else:
        return num


def create_one_skill(result):
    print result
    skill_json = {}
    skill_json['id'] = result[0]
    skill_json['userId'] = result[1]
    skill_json['skillLabels'] = splitLabel(result[2])
    skill_json['levels'] = splitLabel(result[3])
    skill_json['description'] = result[4]
    skill_json['skillImagesUrl'] = result[5]
    skill_json['skillDetailImagesUrl'] = result[6]
    skill_json['unit'] = result[7]
    skill_json['amount'] = result[8]
    skill_json['intr'] = result[9]
    skill_json['obj'] = result[10]
    skill_json['location'] = str(result[12]) + "," + str(result[11])
    skill_json['registerCount'] = result[13]
    skill_json['successCount'] = result[14]
    skill_json['status'] = result[15]
    skill_json['createTime'] = result[16]
    skill_json['nickname'] = result[17]
    skill_json['sex'] = result[18]
    skill_json['birthday'] = result[19]
    skill_json['mobile'] = result[20]
    skill_json['score'] = countCheckNone(result[21])
    skill_json['credit'] = countCheckNone(result[22])
    skill_json['isLogin'] = result[23]
    skill_json['lv'] = countCheckNone(result[24])
    skill_json['creditSum'] = countCheckNone(result[25])
    return skill_json

def splitLabel(ids):
    arr = []
    if not ids is None:
        idsArr = ids.split(",")
        for tagId in idsArr:
            arr.append(tagId)
    return arr

def dump_one_skill_index(result):
    skill_json = create_one_skill(result)
    conn_es.create(index = get_index_name(), doc_type = const.type_skill, id = result[0], body = skill_json)

def dump_userFriend_index(sql):
    cur = conn_db.cursor()
    log.log(u'开始导入用户技能表')
    print sql
    count = cur.execute(sql)
    log.log('there has %s rows skill record' % count)

    results = cur.fetchmany(count)
    index = 0
    errorIndex = 0
    for result in results:
        skillId = result[0]
        try:
            dump_one_skill_index(result)
        except Exception, e:
            log.log("dump skill id: %s error" % skillId)
            print e
            errorIndex += 1
        index += 1
        if index % 50 == 0:
            log.log(u'已完成:'+ '%.2f' % (index / count * 100) + "%")
            time.sleep(0.5)

    log.log(u'完成用户技能检索')
    log.log(u'错误行数：' + '%.2f' % errorIndex)
    cur.close()


def get_select_all_skill_sql():
    sql = 'SELECT s.id, s.user_id, s.skill, s.level, s.description, s.skill_images_url, s.skill_detail_images_url, s.unit, ' \
          ' s.amount, s.intr, s.obj, s.lon, s.lat, s.register_count, s.success_count, s.status, s.create_time, ' \
          ' u.nickname, u.sex, u.birthday, u.mobile, u.score, u.credit, u.is_login+0, u.lv, c.credit_sum ' \
          ' FROM skill AS s LEFT JOIN `user` AS u ON u.id = s.user_id ' \
          ' LEFT JOIN(SELECT credit.user_id, COUNT(credit.id) AS credit_sum FROM credit GROUP BY credit.user_id) ' \
          ' AS c ON c.user_id = s.user_id AND s.deleted = 0 '
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
    dump_userFriend_index(get_select_all_skill_sql())

    search_config.write_dump_time(const.type_skill, dump_time)

#获取


def params_err_log():
    print u"参数不对,请输入'all | deleteAll'"


if len(sys.argv) < 2:
    params_err_log()
    conn_db.close()
    exit(1)

param = sys.argv[1]
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