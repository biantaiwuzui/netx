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

# 设置输入流的编码格式
reload(sys)
sys.setdefaultencoding("utf-8")

conn_es = es_db.get_conn_es()
conn_db = es_db.get_mysql_connector('netx')#上线
#conn_db = es_db.get_mysql_connector('test_netx')#测试
# conn_db = es_db.get_mysql_connector('dev_netx')#开发


create_index_body = {
    'settings': {
        'number_of_shards': 1,
        'number_of_replicas': 5
    },
    'mappings': {
        'article': {
            'properties': {
                'id': {'type': 'text'},
                'userId': {'type': 'text'},
                'title': {'type': u'text'},
                'pic': {'type': 'text'},
                'atta': {'type': 'text'},
                'content': {'type': u'text'},
                'who': {'type': 'integer'},
                'receiver': {'type': 'text'},
                'isAnonymity': {'type': 'integer'},
                'isShowLocation': {'type': 'integer'},
                'isIllegal': {'type': 'integer'},
                'advertorialType': {'type': 'integer'},
                'location': {'type': 'geo_point'},
                'top': {'type': 'integer'},
                'reLocation': {'type': 'text'},
                'topExpiredAt': {'type': 'date'},
                'statusCode': {'type': 'integer'},
                'reason': {'type': 'text'},
                'isLock': {'type': 'integer'},
                'createTime': {'type': 'date'},
                'deleted': {'type': 'integer'},
                'commentNum': {'type': 'integer'},
                'giftNum': {'type': 'integer'},
                'invitationNum': {'type': 'integer'},
                'hits': {'type': 'long'},
                'likeNum': {'type': 'integer'},
                'tagNames': {'type': u'text', u'term_vector': 'with_positions_offsets','index': 'not_analyzed'},
                'tagIds': {'type': 'text', 'term_vector': 'with_positions_offsets'}
            }
        }
    }
}


def get_index_name():
    return const.index_article_name


if conn_es.indices.exists(get_index_name()):
    conn_es.indices.delete(index=get_index_name(), ignore=400)
conn_es.indices.create(index=get_index_name(), body=create_index_body)


def dump_one_article_index(result):
    article_json = {}
    article_json['id'] = result[0]
    article_json['userId'] = result[1]
    article_json['title'] = result[2]
    article_json['pic'] = result[3]
    article_json['atta'] = result[4]
    article_json['content'] = result[5]
    article_json['who'] = result[6]
    article_json['receiver'] = result[7]
    article_json['isAnonymity'] = result[8]
    article_json['isShowLocation'] = result[9]
    article_json['isIllegal'] = result[10]
    article_json['advertorialType'] = result[11]
    # location = str(lat)+","+str(lon)
    article_json['location'] = str(result[13]) + "," + str(result[12])
    article_json['top'] = result[14]
    article_json['reLocation'] = result[15]
    article_json['topExpiredAt'] = result[16]
    article_json['statusCode'] = result[17]
    article_json['reason'] = result[18]
    article_json['isLock'] = result[19]
    article_json['createTime'] = result[20]
    article_json['deleted'] = result[21]
    article_json['commentNum'] = countCheckNone(result[22])
    article_json['giftNum'] = countCheckNone(result[23])
    article_json['invitationNum'] = countCheckNone(result[24])
    article_json['hits'] = countCheckNone(result[25])
    article_json['likeNum'] = countCheckNone(result[26])
    return article_json


def countCheckNone(num):
    if num is None:
        return 0
    else:
        return num


def create_one_article_table(article_json):
    conn_es.create(index=get_index_name(), doc_type=const.type_article, id=article_json['id'], body=article_json)


def dump_article_index(sql):
    cur = conn_db.cursor();
    log.log(u'开始导入资讯表')
    print sql
    count = cur.execute(sql)
    log.log('there has %s rows record' % count)
    results = cur.fetchmany(count)
    index = 0
    errorIndex = 0;
    for result in results:
        articleId = result[0]
        try:
            article_json = dump_one_article_index(result)
            get_tags(article_json, cur)
        except Exception, e:
            log.log('dump article index id %s Error' % articleId)
            print e
            errorIndex += 1
        index += 1
        if index % 50 == 0:
            log.log(u'已完成:' + ' %.2f ' % (index / count * 100) + "%")
            time.sleep(0.5)
    log.log(u'导入完成')
    log.log(u'错误行数：' + '%.2f' % errorIndex)
    cur.close()


def dump_tags_index(sql, article_json, cur):
    cur = conn_db.cursor();
    log.log(u'开始导入标签')
    count = cur.execute(sql)
    log.log('there has %s rows record' % count)
    results = cur.fetchmany(count)
    index = 0
    errorIndex = 0;
    for result in results:
        articleId = result[0]
        try:
            article_json = dump_one_tags(result, article_json)
            create_one_article_table(article_json)
        except Exception, e:
            log.log('dump article index id %s Error' % articleId)
            print e
            errorIndex += 1
        index += 1
        if index % 50 == 0:
            log.log(u'已完成:' + ' %.2f ' % (index / count * 100) + "%")
            time.sleep(0.5)
    log.log(u'导入完成')
    log.log(u'错误行数：' + '%.2f' % errorIndex)
    cur.close()


def splitLabel(ids):
    arr = []
    if not ids is None:
        idsArr = ids.split(",")
        for tagId in idsArr:
            arr.append(tagId)
    return arr


def dump_one_tags(result, article_json):
    article_json['tagNames'] = splitLabel(result[0])
    article_json['tagIds'] = splitLabel(result[1])
    return article_json


def get_tags(article_json, cur):
    # sql='select GROUP_CONCAT(common_tags.type_cate) as type_cates,GROUP_CONCAT(common_tags.id) as tags_ids from common_tags left join article_tags on common_tags.id = article_tags.tag_id WHERE common_tags.id in(SELECT tag_id FROM article_tags WHERE article_id='+article_json['id']+')'+' GROUP BY common_tags.id; '
    sql = 'SELECT GROUP_CONCAT(common_tags.value) AS type_cates,GROUP_CONCAT(common_tags.id) AS ids FROM common_tags WHERE common_tags.id in ' + '(' + 'SELECT article_tags.tag_id FROM article_tags WHERE article_id ="' + \
          article_json['id'] + '");'
    dump_tags_index(sql, article_json, cur)


def get_count_sql(idName, tabelName, contidion):
    return 'select ' + idName + ' as id,count(*) as num from ' + tabelName + ' where ' + contidion + ' deleted = 0 group by ' + idName


def get_select_all_article_sql():
    # 新模块的sql语句
    sql = 'select a.id, a.user_id, a.title, a.pic, a.atta, a.content, a.who, a.receiver, a.is_anonymity+0, a.is_show_location+0, ' \
          'a.is_illegal+0, a.advertorial_type, a.lon, a.lat, a.top, a.location, a.top_expired_at, ' \
          'a.status_code, a.reason ,a.is_lock, a.create_time, a.deleted , evaluate.num, gift.num, invitation.num, clicks.num, likes.num ' \
          ' from article a left join ( ' + get_count_sql("type_id", "common_evaluate",
                                                         "") + ') as evaluate on evaluate.id = a.id ' \
                                                               ' left join ( ' + get_count_sql("to_user_id", "gift",
                                                                                               "") + ') as gift on gift.id = a.user_id ' \
                                                                                                     ' left join ( ' + get_count_sql(
        "to_user_id", "invitation", "") + ') as invitation on invitation.id = a.user_id ' \
                                          ' left join ( ' + get_count_sql("article_id", "user_article_likes",
                                                                          "is_like = 1 and") + ') as likes on likes.id = a.id ' \
                                                                                               ' left join ( ' + get_count_sql(
        "article_id", "article_click_history", "") + ') as clicks on clicks.id = a.id ' \
                                                     'where a.deleted=0 order by a.create_time desc'
    # 'a.commentNumber, a.giftNumber, a.invitationNumber ' \ 剩下的这些值这ArticleAction. refactorEntityToDto
    return sql


def dump_all_index():
    log.log(u'开始清除遗留的index数据')
    dump_time = datetime.datetime.now()
    dump_article_index(get_select_all_article_sql())
    search_config.write_dump_time(const.type_article, dump_time)

def delete_all_index():
    conn_es.indices.delete(index=get_index_name(), ignore=[400, 404])

def params_err_info():
    print u'参数不匹配:all | deleteAll'

if len(sys.argv) < 2:
    params_err_info()
    conn_db.close()
    exit(1)

param = sys.argv[1]

if param == 'all':
    log.log(u'导入引擎库')
    dump_all_index()
elif param == 'deleteAll':
    log.log(u'清除次索引所有数据')
    delete_all_index()
    log.log(u'清除完成')
else:
    params_err_info()
conn_db.close()
