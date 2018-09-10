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
        'number_of_shards': 1,
        'number_of_replicas': 5
    },
    'mappings': {
        'credit': {
            'properties': {
                'id': {'type': 'text'},
                'userId': {'type': 'text'},
                'name': {'type': 'text'},
                'tagIds': {'type': 'text'},
                'frontStyle': {'type': 'text'},
                'backStyle': {'type': 'text'},
                'isFitScopeOne': {'type': 'integer'},
                'isFitScopeTwo': {'type': 'integer'},
                'sellerIds': {'type': 'text'},
                'isFitScopeThree': {'type': 'integer'},
                'importNetNum': {'type': 'text'},
                'importName': {'type': 'text'},
                'importPhone': {'type': 'text'},
                'importIdcard': {'type': 'text'},
                'releaseNum': {'type': 'integer'},
                'releaseTime': {'type': 'date'},
                'successTime': {'type': 'date'},
                'faceValue': {'type': 'integer'},
                'applyPrice': {'type': 'long'},
                'buyFactor': {'type': 'double'},
                'growthRate': {'type': 'integer'},
                'royaltyRatio': {'type': 'integer'},
                'remark': {'type': 'text'},
                'buyAmount': {'type': 'long'},
                'payAmount': {'type': 'long'},
                'status': {'type': 'integer'},
                'createTime': {'type': 'date'},
                'location': {'type': 'geo_point'}
            }
        }
    }
}

def create_one_credit(result):
    credit_json={}
    credit_json['id'] = result[0]
    credit_json['userId'] = result[1]
    credit_json['name'] = result[1]
    credit_json['tagIds'] = result[3]
    credit_json['frontStyle'] = result[4]
    credit_json['backStyle'] = result[5]
    credit_json['isFitScopeOne'] = result[6]
    credit_json['isFitScopeTwo'] = result[7]
    credit_json['sellerIds'] = result[8]
    credit_json['isFitScopeThree'] = result[9]
    credit_json['importNetNum'] = result[10]
    credit_json['importName'] = result[11]
    credit_json['importPhone'] = result[12]
    credit_json['importIdcard'] = result[13]
    credit_json['releaseNum'] = result[14]
    credit_json['releaseTime'] = result[15]
    credit_json['faceValue'] = result[16]
    credit_json['applyPrice'] = result[17]
    credit_json['buyFactor'] = result[18]
    credit_json['growthRate'] = result[19]
    credit_json['royaltyRatio'] = result[20]
    credit_json['remark'] = result[21]
    credit_json['buyAmount'] = result[22]
    credit_json['payAmount'] = result[23]
    credit_json['status'] = result[24]
    credit_json['createTime'] = result[25]
    credit_json['location'] = str(result[27]) +","+str(result[26])
    return credit_json


def SplitLable(ids):
    Arr = []
    if not ids is None:
        idsArr = ids.split(",")
        for tagId in idsArr:
            Arr.append(tagId)
    return Arr


def dump_one_credit_index(result):
    credit_json = create_one_credit(result)
    conn_es.create(index=get_index_name(), doc_type=const.type_credit, id=credit_json['id'], body=credit_json)

def dump_credit_index(sql):
    cur = conn_db.cursor()
    log.log(u'开始导入网信表')
    print sql
    count = cur.execute(sql)
    log.log('there has %s rows demand record' % count)

    results = cur.fetchmany(count)
    index = 0
    error_index = 0
    for result in results:
        creditId = result[0]
        try:
            dump_one_credit_index(result)
        except Exception, e:
            log.log('dump credit id:%s error' % creditId)
            print e
            error_index +=1
        index +=1
        if index % 50 == 0:
            log.log(u'已完成:'+ ' %.2f ' % (index / count * 100) + "%")
            time.sleep(0.5)
    log.log(u'完成网信需求检索')
    log.log(u'错误行数：' + '%.2f' % error_index)
    cur.close()


def get_select_all_credit_sql():
    sql = 'select c.id , c.user_id , c.name , c.tag_ids , c.front_style , c.back_style , c.is_fit_scope_one+0, ' \
          'c.is_fit_scope_two+0 , c.seller_ids , c.is_fit_scope_three+0, c.import_net_num , c.import_name, ' \
          'c.import_phone ,c.import_idcard ,c.release_num ,c.release_time ,c.success_time ,c.face_value, ' \
          'c.apply_price ,c.buy_factor ,c.growth_rate ,c.royalty_ratio ,c.remark ,c.buy_amount ,c.pay_amount, ' \
          'c.status ,c.create_time ,user.lon ,user.lat from credit as c left join user on c.user_id = user.id ' \
          'where c.deleted=0 and user.deleted=0 order by c.create_time'
    return sql


def delete_all_index():
    conn_es.indices.delete(index=get_index_name(), ignore=[400, 404])

#全文索引
def dump_all_index():
    #es_db.es_init(seller_mapping, get_index_name(), const.type_seller)
    #log.log(u"先清理之前遗留的数据")
    #delete_all_index()
    from_date = '1970-01-01'
    dump_time = datetime.datetime.now()
    dump_credit_index(get_select_all_credit_sql())

    search_config.write_dump_time(const.type_credit, dump_time)


def get_index_name():
    return const.index_credit_name


if conn_es.indices.exists(get_index_name()):
    conn_es.indices.delete(index=get_index_name(), ignore=400)
conn_es.indices.create(index=get_index_name(), body=create_index_body)


def params_err_log():
    print u'参数格式不正确:all | deleteAll'


#if len(sys.argv) < 2:
#    params_err_log()
#    conn_db.close()
#    exit(1)

#param = sys.argv[1]
param = 'all'
if param == 'all':
    #导入中文搜索引擎库
    dump_all_index()
elif param == 'deleteAll':
    print 'deleteAll'
    log.log(u'清除次索引所有数据')
    delete_all_index()
    log.log(u'清除完成')
else:
    params_err_log()
conn_db.close()
