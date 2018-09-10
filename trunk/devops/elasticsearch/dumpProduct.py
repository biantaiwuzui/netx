#!/usr/bin/env python
#-*- coding:utf-8-*-

from __future__ import division
import urllib2
import sys
import datetime
import time
import json

from common import const
from utils import es_db, search_config, log

#设置输入流的编码格式
reload(sys)
sys.setdefaultencoding( "utf-8" )

conn_es = es_db.get_conn_es()
conn_db = es_db.get_mysql_connector('netx')#上线
#conn_db = es_db.get_mysql_connector('test_netx')#测试
#conn_db = es_db.get_mysql_connector('dev_netx')#开发

def convert_to_int(bstring):
    return 0 if bstring == b'\x00' else 1

create_index_body = {
  'settings': {
    # just one shard, no replicas for testing
    'number_of_shards': 1,
    'number_of_replicas': 5,
  },
  'mappings': {
    'product': {
      'properties': {
        'id': {'type': 'text'},
        'merchantId': {'type': 'text'},
        'name': {'type': "keyword"},
        'characteristic': {'type': u'text'},
        'isDelivery': {'type': 'integer'},
        'isReturn': {'type': 'integer'},
        'onlineStatus': {'type': 'integer'},
        'visitCount': {'type': 'long'},
        'merchantName': {'type': 'keyword'},
        'userId': {'type': "text"},
        'provinceCode': {'type': 'keyword'},
        'cityCode': {'type': 'keyword'},
        'areaCode': {'type': 'keyword'},
        'addrCountry': {'type': 'text'},
        'location': {'type': 'geo_point'},
        'payStatus': {'type': 'integer'}, #0:已缴费 1.待缴费 2.待续费
        'isHoldCredit': {'type': 'integer'},
        'categoryIds': {'type': 'text', 'term_vector': 'with_positions_offsets'},
        'categoryNames': {'type': u'text', u'term_vector': 'with_positions_offsets'},
        'categoryParentIds': {'type': 'text', 'term_vector': 'with_positions_offsets'},
        'productImagesUrl': {'type': u'text', u'term_vector': 'with_positions_offsets'},
        'firstRate': {'type': 'double'},
        'volume': {'type': 'long'},
        'nickname': {'type': 'keyword'},
        'credit': {'type': 'integer'},
        'isLogin': {'type': 'integer'},
        'marketPrice': {'type': 'long'},
        'price': {'type': 'long'}
      }
    }
  }
}


def get_index_name():
    return const.index_product_name

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

def changePrice(price):
    if price is None:
        price = 0
    elif price == '':
        price = 0
    return price

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

def dump_one_product(result):
    product_json = {}
    product_json['id'] = result[0]
    product_json['merchantId'] = result[1]
    product_json['name'] = result[2]
    product_json['characteristic'] = result[3]
    product_json['isDelivery'] = result[4]
    product_json['isReturn'] = result[5]
    product_json['onlineStatus'] = result[6]
    product_json['visitCount'] = countCheckNone(result[7])
    return product_json

def dump_one_merchant(result,product_json):
    product_json['merchantName'] = result[0]
    product_json['userId'] = result[1]
    product_json['provinceCode'] = result[2]
    product_json['cityCode'] = result[3]
    product_json['areaCode'] = result[4]
    product_json['addrCountry'] = result[5]
    product_json['location'] = str(locationCheckNone(result[6]))+","+str(locationCheckNone(result[7]))
    product_json['payStatus'] = result[8]
    product_json['isHoldCredit'] = countCheckNone(result[9])
    product_json['pacSetId'] = result[10]
    return product_json

def dump_one_category(result,product_json):
    product_json['productId'] = result[0]
    product_json['categoryIds'] = splitLabel(result[1])
    product_json['categoryNames'] = splitLabel(result[2])
    product_json['categoryParentIds'] = splitLabel(result[3])
    return product_json

def dump_one_picture(result,product_json):
    product_json['productId'] = result[0]
    product_json['productImagesUrl'] = splitLabel(result[1])
    return product_json

def dump_one_firstRate(result,product_json):
    product_json['firstRate'] = result[0]
    return product_json

def dump_one_volume(result,product_json):
    product_json['volume'] = countCheckNone(result[0])
    return product_json

def dump_one_user(result,product_json):
    product_json['nickname'] = result[0]
    product_json['credit'] = countCheckNone(result[1])
    product_json['isLogin'] = countCheckNone(result[2])
    return product_json

def dump_one_price(result,product_json):
    product_json['marketPrice'] = changePrice(result[0])
    product_json['price'] = changePrice(result[1])
    conn_es.create(index=get_index_name(), doc_type=const.type_product, id= product_json['id'], body= product_json)

def dump_product_index(sql):
    cur = conn_db.cursor()
    log.log(u'开始导入商品表')
    print sql
    count = cur.execute(sql)
    log.log('there has %s rows product record' % count)

    results = cur.fetchmany(count)
    index = 0
    errorIndex = 0
    for result in results:
        sid = result[0]
        try:
            product_json = dump_one_product(result)
            get_merchant(product_json,cur)
        except Exception, e:
            log.log("dump product id: %s error" % sid)
            print e
            errorIndex += 1

        index += 1
        if index % 50 == 0:
            log.log(u'已完成:'+ '%.2f' % (index / count * 100) + "%")
            time.sleep(0.5)

    log.log(u'完成商品检索')
    log.log(u'错误行数：' + '%.2f' % errorIndex)
    cur.close()

def dump_merchant_index(sql, product_json, cur):
    cur = conn_db.cursor()
    log.log(u'开始导入商家表')
    count = cur.execute(sql)
    log.log('there has %s rows merchant record' % count)

    results = cur.fetchmany(count)
    index = 0
    errorIndex = 0
    for result in results:
        sid = result[0]
        try:
            product_json = dump_one_merchant(result,product_json)
            get_category(product_json,cur)
        except Exception, e:
            log.log("dump merchant id: %s error" % sid)
            print e
            errorIndex += 1

        index += 1
        if index % 50 == 0:
            log.log(u'已完成:'+ '%.2f' % (index / count * 100) + "%")
            time.sleep(0.5)

    log.log(u'完成商家检索')
    log.log(u'错误行数：' + '%.2f' % errorIndex)

def dump_category_index(sql,product_json,cur):
    log.log(u'开始导入类目表')
    count = cur.execute(sql)
    log.log('there has %s rows  record' % count)
    results = cur.fetchmany(count)
    index = 0
    errorIndex = 0
    for result in results:
        sid = result[0]
        try:
            product_json = dump_one_category(result,product_json)
            get_picture(product_json, cur)
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

def dump_picture_index(sql,product_json,cur):
    log.log(u'开始导入商品图片表')
    count = cur.execute(sql)
    log.log('there has %s rows record' % count)
    results = cur.fetchmany(count)
    index = 0
    errorIndex = 0
    for result in results:
        sid = result[0]
        try:
            product_json = dump_one_picture(result,product_json)
            get_firstRate(product_json,cur)
        except Exception, e:
            log.log("dump picture id: %s error" % sid)
            print e
            errorIndex += 1
        index += 1
        if index % 50 == 0:
            log.log(u'已完成:' + '%.2f' % (index / count * 100) + "%")
            time.sleep(0.5)

    log.log(u'完成商品图片检索')
    log.log(u'错误行数：' + '%.2f' % errorIndex)

def dump_firstRate_index(sql,product_json,cur):
    log.log(u'开始导入红包设置表')
    count = cur.execute(sql)
    log.log('there has %s rows record' % count)
    results = cur.fetchmany(count)
    index = 0
    errorIndex = 0
    for result in results:
        sid = result[0]
        try:
            product_json = dump_one_firstRate(result,product_json)
            get_volume(product_json,cur)
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

def dump_volume_index(sql,product_json,cur):
    log.log(u'开始导入库存表')
    count = cur.execute(sql)
    log.log('there has %s rows record' % count)
    results = cur.fetchmany(count)
    index = 0
    errorIndex = 0
    for result in results:
        sid = result[0]
        try:
            product_json = dump_one_volume(result,product_json)
            get_user(product_json,cur)
        except Exception, e:
            log.log("dump volume id: %s error" % sid)
            print e
            errorIndex += 1
        index += 1
        if index % 50 == 0:
            log.log(u'已完成:' + '%.2f' % (index / count * 100) + "%")
            time.sleep(0.5)

    log.log(u'完成红包设置检索')
    log.log(u'错误行数：' + '%.2f' % errorIndex)

def dump_user_index(sql,product_json,cur):
    log.log(u'开始导入用户表')
    count = cur.execute(sql)
    log.log('there has %s rows record' % count)
    results = cur.fetchmany(count)
    index = 0
    errorIndex = 0
    for result in results:
        sid = result[0]
        try:
            product_json = dump_one_user(result,product_json)
            get_price(product_json,cur)
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

def dump_price_index(sql,product_json,cur):
    log.log(u'开始导入价格表')
    count = cur.execute(sql)
    log.log('there has %s rows record' % count)
    results = cur.fetchmany(count)
    index = 0
    errorIndex = 0
    for result in results:
        sid = result[0]
        try:
            dump_one_price(result,product_json)
        except Exception, e:
            log.log("dump user id: %s error" % sid)
            print e
            errorIndex += 1
        index += 1
        if index % 50 == 0:
            log.log(u'已完成:' + '%.2f' % (index / count * 100) + "%")
            time.sleep(0.5)

    log.log(u'完成价格检索')
    log.log(u'错误行数：' + '%.2f' % errorIndex)



#全量索引
def dump_all_index():
    #es_db.es_init(seller_mapping, get_index_name(), const.type_seller)
    from_time = '1970-01-01'
    dump_time = datetime.datetime.now()

    sql = 'SELECT id, merchant_id, name, characteristic, is_delivery+0, is_return+0, ' \
          ' online_status, visit_count FROM product WHERE online_status != 3; '

    dump_product_index(sql)
    search_config.write_dump_time(const.type_product, dump_time)

def get_merchant(product_json, cur):
    sql = 'SELECT `name`, user_id, province_code, city_code, area_code, addr_country, lat, lon, ' \
          ' pay_status, is_support_credit+0, pac_set_id FROM merchant WHERE id = "'+product_json['merchantId']+'";'
    dump_merchant_index(sql,product_json,cur)

def get_category(product_json,cur):
    sql = 'SELECT cp.product_id, GROUP_CONCAT(category.id) category_ids, GROUP_CONCAT(category.`name`) category_names, ' \
          ' GROUP_CONCAT(category.parent_id) category_parent_ids FROM category_product AS cp, category ' \
          ' WHERE category.id = cp.category_id AND cp.product_id = "'+product_json['id']+'"' \
          ' GROUP BY cp.product_id; '
    dump_category_index(sql,product_json,cur)

def get_picture(product_json,cur):
    sql = 'SELECT product_id, GROUP_CONCAT(picture_url) AS picture_urls FROM product_picture ' \
          ' WHERE product_picture_type = "none" AND product_id = "'+product_json['id']+'"' \
          ' GROUP BY product_id; '
    dump_picture_index(sql,product_json,cur)

def get_firstRate(product_json,cur):
    sql = 'SELECT first_rate FROM merchant_packet_set WHERE id = "'+product_json['pacSetId']+'";'
    dump_firstRate_index(sql,product_json,cur)

def get_volume(product_json,cur):
    sql = 'SELECT SUM(sell_nums) AS volume FROM sku where product_id = "'+product_json['id']+'";'
    dump_volume_index(sql,product_json,cur)

def get_user(product_json,cur):
    sql = 'SELECT nickname, credit, is_login+0 FROM `user` WHERE id = "'+product_json['userId']+'";'
    dump_user_index(sql,product_json,cur)

def get_price(product_json, cur):
    sql = 'SELECT market_price,price FROM sku WHERE product_id ="'+product_json['id']+'"' \
            ' ORDER BY market_price limit 0,1;'
    dump_price_index(sql,product_json,cur)

#获取


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