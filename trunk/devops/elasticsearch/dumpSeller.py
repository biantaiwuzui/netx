#!/usr/bin/env python
#-*- coding:utf-8-*-

from __future__ import division

import datetime
import sys
import time

from common import const
from utils import es_db, search_config, log

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
        'number_of_replicas': 5,
    },
    'mappings': {
        'seller': {
            'properties': {
                'id': {'type': 'text'},
                'name': {'type': 'keyword'},
                'userId': {'type': "text"},
                'provinceCode': {'type': 'keyword'},
                'cityCode': {'type': 'keyword'},
                'areaCode': {'type': 'keyword'},
                'addrCountry': {'type': 'text'},
                'location': {'type': 'geo_point'},
                'desc': {'type': 'text'},
                'status': {'type': 'integer'},  # 1: 正常, 2: 拉黑
                'visitNum': {'type': 'long'},
                'payStatus': {'type': 'integer'}, #0:已缴费 1.待缴费 2.待续费
                'isHoldCredit': {'type': 'integer'},
                'createTime': {'type': 'date'},
                'categoryIds': {'type': 'text', 'term_vector': 'with_positions_offsets'},
                'categoryNames': {'type': u'text', u'term_vector': 'with_positions_offsets'},
                'categoryParentIds': {'type': 'text', 'term_vector': 'with_positions_offsets'},
                'logoImagesUrl': {'type': u'text', u'term_vector': 'with_positions_offsets'},
                'firstRate': {'type': 'double'},
                'volume': {'type': 'long'},
                'nickname': {'type': 'keyword'},
                'credit': {'type': 'integer'},
                'isLogin': {'type': 'integer'}
            }
        }
    }
}


def get_index_name():
    return const.index_seller_name
if conn_es.indices.exists(index=get_index_name()):
    conn_es.indices.delete(index=get_index_name(), ignore=400)
conn_es.indices.create(index=get_index_name(), body= create_index_body)

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

def locationCheckNone(num):
    if num is None:
        return 0.000000
    else:
        return num


def dump_one_seller(result):
    seller_json = {}
    seller_json['id'] = result[0]
    seller_json['name'] = result[1]
    seller_json['userId'] = result[2]
    seller_json['provinceCode'] = result[3]
    seller_json['cityCode'] = result[4]
    seller_json['areaCode'] = result[5]
    seller_json['addrCountry'] = result[6]
    seller_json['location'] = str(locationCheckNone(result[7]))+","+str(locationCheckNone(result[8]))
    seller_json['desc'] = result[9]
    seller_json['status'] = result[10]
    seller_json['visitNum'] = countCheckNone(result[11])
    seller_json['payStatus'] = result[12]
    seller_json['isHoldCredit'] = countCheckNone(result[13])
    seller_json['createTime'] = result[14]
    seller_json['pacSetId'] = result[15]
    return seller_json

def dump_one_category(categoryResult,seller_json):
    seller_json['merchantId'] = categoryResult[0]
    seller_json['categoryIds'] = splitLabel(categoryResult[1])
    seller_json['categoryNames'] = splitLabel(categoryResult[2])
    seller_json['categoryParentIds'] = splitLabel(categoryResult[3])
    return seller_json

def dump_one_picture(result,seller_json):
    seller_json['merchantId'] = result[0]
    seller_json['logoImagesUrl'] = splitLabel(result[1])
    return seller_json

def dump_one_firstRate(result,seller_json):
    seller_json['firstRate'] = result[0]
    return seller_json

def dump_one_volume(result,seller_json):
    seller_json['volume'] = countCheckNone(result[0])
    return seller_json

def dump_one_user(result,seller_json):
    seller_json['nickname'] = result[0]
    seller_json['credit'] = countCheckNone(result[1])
    seller_json['isLogin'] = countCheckNone(result[2])
    conn_es.create(index=get_index_name(), doc_type=const.type_seller, id= seller_json['id'], body= seller_json)


def dump_seller_index(sql):
    cur = conn_db.cursor()
    log.log(u'开始导入商家表')
    #print sql
    count = cur.execute(sql)
    log.log('there has %s rows product record' % count)

    results = cur.fetchmany(count)
    index = 0
    errorIndex = 0
    for result in results:
        sid = result[0]
        try:
            seller_json = dump_one_seller(result)
            get_category(seller_json,cur)
        except Exception, e:
            log.log("dump seller id: %s error" % sid)
            print e
            errorIndex += 1
        index += 1
        if index % 50 == 0:
            log.log(u'已完成:' + '%.2f' % (index / count * 100) + "%")
            time.sleep(0.5)

    log.log(u'完成商家检索')
    log.log(u'错误行数：' + '%.2f' % errorIndex)
    cur.close()

def dump_category_index(sql,seller_json,cur):
    log.log(u'开始导入类目表')
    count = cur.execute(sql)
    log.log('there has %s rows  record' % count)
    results = cur.fetchmany(count)
    index = 0
    errorIndex = 0
    for result in results:
        sid = result[0]
        try:
            category_json = dump_one_category(result,seller_json)
            get_picture(category_json,cur)
        except Exception, e:
            log.log("dump category id: %s error" % sid)
            print e
            errorIndex += 1
        index += 1
        if index % 50 == 0:
            log.log(u'已完成:' + '%.2f' % (index / count * 100) + "%")
            time.sleep(0.5)

    log.log(u'完成类目检索')
    log.log(u'错误行数：' + '%.2f' % errorIndex)

def dump_picture_index(sql,seller_json,cur):
    log.log(u'开始导入商家图片表')
    count = cur.execute(sql)
    log.log('there has %s rows record' % count)
    results = cur.fetchmany(count)
    index = 0
    errorIndex = 0
    for result in results:
        sid = result[0]
        try:
            seller_json = dump_one_picture(result,seller_json)
            get_firstRate(seller_json,cur)
        except Exception, e:
            log.log("dump picture id: %s error" % sid)
            print e
            errorIndex += 1
        index += 1
        if index % 50 == 0:
            log.log(u'已完成:' + '%.2f' % (index / count * 100) + "%")
            time.sleep(0.5)

    log.log(u'完成商家图片检索')
    log.log(u'错误行数：' + '%.2f' % errorIndex)

def dump_firstRate_index(sql,seller_json,cur):
    log.log(u'开始导入红包设置表')
    count = cur.execute(sql)
    log.log('there has %s rows record' % count)
    results = cur.fetchmany(count)
    index = 0
    errorIndex = 0
    for result in results:
        sid = result[0]
        try:
            seller_json = dump_one_firstRate(result,seller_json)
            get_volume(seller_json,cur)
        except Exception, e:
            log.log("dump firstRate id: %s error" % sid)
            print e
            errorIndex += 1
        index += 1
        if index % 50 == 0:
            log.log(u'已完成:' + '%.2f' % (index / count * 100) + "%")
            time.sleep(0.5)

    log.log(u'完成红包设置检索')
    log.log(u'错误行数：' + '%.2f' % errorIndex)

def dump_volume_index(sql,seller_json,cur):
    log.log(u'开始导入库存表')
    count = cur.execute(sql)
    log.log('there has %s rows record' % count)
    results = cur.fetchmany(count)
    index = 0
    errorIndex = 0
    for result in results:
        sid = result[0]
        try:
            seller_json = dump_one_volume(result,seller_json)
            get_user(seller_json,cur)
        except Exception, e:
            log.log("dump volume id: %s error" % sid)
            print e
            errorIndex += 1
        index += 1
        if index % 50 == 0:
            log.log(u'已完成:' + '%.2f' % (index / count * 100) + "%")
            time.sleep(0.5)

    log.log(u'完成库存检索')
    log.log(u'错误行数：' + '%.2f' % errorIndex)

def dump_user_index(sql,seller_json,cur):
    log.log(u'开始导入用户表')
    count = cur.execute(sql)
    log.log('there has %s rows record' % count)
    results = cur.fetchmany(count)
    index = 0
    errorIndex = 0
    for result in results:
        sid = result[0]
        try:
            dump_one_user(result,seller_json)
        except Exception, e:
            log.log("dump user id: %s error" % sid)
            print e
            errorIndex += 1
        index += 1
        if index % 50 == 0:
            log.log(u'已完成:' + '%.2f' % (index / count * 100) + "%")
            time.sleep(0.5)

    log.log(u'完成用户检索')
    log.log(u'错误行数：' + '%.2f' % errorIndex)


#全量索引
def dump_all_index():
    #es_db.es_init(seller_mapping, get_index_name(), const.type_seller)
    from_time = '1970-01-01'
    dump_time = datetime.datetime.now()

    sql = 'SELECT id, `name`, user_id, province_code, city_code, area_code, addr_country, lat, lon, ' \
            ' `desc`, `status`, visit_count, pay_status, is_support_credit+0, create_time, pac_set_id FROM merchant; '

    dump_seller_index(sql)
    search_config.write_dump_time(const.type_seller, dump_time)

#获取
#获取类目
def get_category(seller_json,cur):
    sql = 'SELECT mc.merchant_id, GROUP_CONCAT(category.id) category_ids, GROUP_CONCAT(category.`name`) category_names, ' \
            ' GROUP_CONCAT(category.parent_id) category_parent_ids FROM merchant_category AS mc, category ' \
            ' WHERE category.id = mc.category_id AND mc.merchant_id = "'+seller_json['id']+'"' \
            ' GROUP BY mc.merchant_id; '
    dump_category_index(sql,seller_json,cur)

def get_picture(seller_json,cur):
    sql = 'SELECT merchant_id, GROUP_CONCAT(picture_url) AS picture_urls FROM merchant_picture ' \
            ' WHERE merchant_picture_type = "logo" AND merchant_id = "'+seller_json['id']+'"' \
            ' GROUP BY merchant_id; '
    dump_picture_index(sql,seller_json,cur)

def get_firstRate(seller_json,cur):
    sql = 'SELECT first_rate FROM merchant_packet_set WHERE id = "'+seller_json['pacSetId']+'";'
    dump_firstRate_index(sql,seller_json,cur)

def get_volume(seller_json,cur):
    sql = 'SELECT SUM(sell_nums) AS volume FROM sku where product_id in(SELECT id FROM product WHERE merchant_id = "'+seller_json['id']+'");'
    dump_volume_index(sql,seller_json,cur)

def get_user(seller_json,cur):
    sql = 'SELECT nickname, credit, is_login+0 FROM `user` WHERE id = "'+seller_json['userId']+'";'
    dump_user_index(sql,seller_json,cur)

def params_err_log():
    print u"参数不对,请输入'all | exchange | increase | test'"

def delete_all_index():
    conn_es.indices.delete(index=get_index_name(), ignore=[400, 404])


if len(sys.argv) < 2:
    params_err_log()
    es_db.close_conn_db()
    exit(1)

param = sys.argv[1]
#param = 'all'
if param == 'all':
    #导入中文简体搜索库
    #delete_all_index()
    dump_all_index()
elif param == 'increase':
    print 'test'
    #dump_increment_index()
else:
    params_err_log()

conn_db.close()