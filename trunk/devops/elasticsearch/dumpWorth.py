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
        'worth': {
            'properties': {
                'id': {'type': 'text'},#网能id
                'worthType': {'type': 'keyword'},#网能类型：【心愿、活动、技能、需求】
                'title': {'type': 'keyword'},#网能标题
                'detail': {'type': 'keyword'},#网能详情
                'hit': {'type': 'integer'},#点击量
                'publishTime': {'type': 'date'},#发布时间
                'endTime': {'type': 'date'},#结束时间
                'amount': {'type': 'double'},#金额
                'dealsCount': {'type': 'integer'},#成交   数
                'dealsTotal': {'type': 'double'},#成交总额
                'imagesUrl': {'type': 'text'},#网能图片
                'unit': {'type': 'text'},#单位
                'isHoldCredit': {'type': 'integer'},#是否支持网信
                'status': {'type': 'integer'},#状态
                'location': {'type': 'geo_point'},#经纬度
                'userId': {'type': 'keyword'},#发布者id
                'headImg': {'type': 'text'},#发布者头像
                'birthday': {'type': 'date'},#发布者生日
                'nickname': {'type': 'keyword'},#发布者昵称
                'lv': {'type': 'integer'},#发布者等级
                'isLogin': {'type': 'integer'},#发布者的登录状态
                'credit': {'type': 'integer'},#发布者信用
                'loginAt': {'type': 'date'},#最后登录时间
                'activeAt': {'type': 'date'},#最后操作时间
                'sex': {'type': 'text'},#性别
                'num': {'type': 'integer'},
                'meetingType': {'type': 'integer'},
                'matchImageUrl':{'type': u'text'},#比赛的图片
                'subTitle':{'type': 'keyword'},#副标题
                'matchKind':{'type': 'integer'},#比赛类型
                'matchRule':{'type': 'text'},#比赛规则
                'grading':{'type': 'text'},#评分标准
                'matchIntroduction':{'type': 'text'},#比赛介绍
                'organizerName':{'type': 'text'},#商户名称
                'initimtorId':{'type': 'text'},#创建者id
                'regCount':{'type':'integer'}
            }
        }
    }
}


def get_index_name():
    return const.index_worth_name

if conn_es.indices.exists(index=get_index_name()):
    conn_es.indices.delete(index=get_index_name(), ignore=400)
conn_es.indices.create(index=get_index_name(), body= create_index_body)


def create_one_worth(result,type):
    worth_json = {}
    worth_json['id']=result[0]
    worth_json['worthType']=type
    worth_json['title']=result[1]
    worth_json['detail']=result[2]
    worth_json['hit']=countCheckNone(result[3])
    worth_json['publishTime']=result[4]
    worth_json['endTime']=result[5]
    worth_json['amount']=countCheckNone(result[6])/100
    worth_json['dealsCount']=countCheckNone(result[7])
    worth_json['dealsTotal']=countCheckNone(result[8])/100
    worth_json['imgesUrl']=result[9]
    worth_json['location']=str(result[10]) +","+str(result[11])
    worth_json['userId']=result[12]
    worth_json['birthday']=result[13]
    worth_json['nickname']=result[14]
    worth_json['lv']=result[15]
    worth_json['isLogin']=result[16]
    worth_json['credit']=result[17]
    worth_json['loginAt']=result[18]
    worth_json['activeAt']=result[19]
    worth_json['sex']=result[20]
    worth_json['isHoldCredit']=countToBoolean(result[21])
    worth_json['status']=result[22]
    resultLen = len(result)
    if resultLen > 23:
        worth_json['unit']=result[23]
    if resultLen > 24:
        worth_json['num']=countCheckNone(result[24])
    return worth_json

def create_one_worth_meeting(result,type):
    worth_json = {}
    worth_json['id']=result[0]
    worth_json['worthType']=type
    worth_json['title']=result[1]
    worth_json['detail']=result[2]
    worth_json['hit']=countCheckNone(result[3])
    worth_json['publishTime']=result[4]
    worth_json['endTime']=result[5]
    worth_json['amount']=countCheckNone(result[6])/100
    worth_json['dealsCount']=countCheckNone(result[7])
    worth_json['dealsTotal']=countCheckNone(result[8])/100
    worth_json['imgesUrl']=result[9]
    worth_json['location']=str(result[10]) +","+str(result[11])
    worth_json['userId']=result[12]
    worth_json['birthday']=result[13]
    worth_json['nickname']=result[14]
    worth_json['lv']=result[15]
    worth_json['isLogin']=result[16]
    worth_json['credit']=result[17]
    worth_json['loginAt']=result[18]
    worth_json['activeAt']=result[19]
    worth_json['sex']=result[20]
    worth_json['isHoldCredit']=countToBoolean(result[21])
    worth_json['status']=result[22]
    worth_json['meetingType']=result[23]
    return worth_json

def create_one_worth_match(result,type):
    matchId_json = {}
    matchId_json['id'] = result[0]
    matchId_json['worthType']=type
    matchId_json['matchImageUrl'] = splitImage(result[1])
    matchId_json['title'] = result[2]
    matchId_json['subTitle'] = result[3]
    matchId_json['matchKind'] = result[4]
    matchId_json['matchRule'] = result[5]
    matchId_json['grading'] = result[6]
    matchId_json['matchIntroduction'] = result[7]
    matchId_json['status'] = result[8]
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
    matchId_json['initimtorId'] = result[24]
    matchId_json['hit']= result[25]
    matchId_json['regCount'] = result[26]
    return matchId_json

def splitImage(ids):
    arr = []
    if not ids is None:
        idsArr = ids.split(",")
        for img in idsArr:
            arr.append(img)
    return arr


def countCheckNone(num):
    if num is None:
        return 0
    else:
        return num

def countToBoolean(num):
    if num is None or num ==0:
        return 0
    else:
        return 1

def dump_one_Worth_index(result,type):
    worth_json = create_one_worth(result,type)
    conn_es.create(index=get_index_name(), doc_type=const.type_worth, id=type+'-'+worth_json['id'], body = worth_json)

def dump_one_Worth_Meeting_index(result,type):
    worth_json = create_one_worth_meeting(result,type)
    conn_es.create(index=get_index_name(), doc_type=const.type_worth, id=type+'-'+worth_json['id'], body = worth_json)

def dump_one_Worth_Match_index(result,type):
    worth_json = create_one_worth_match(result,type)
    conn_es.create(index=get_index_name(), doc_type=const.type_worth, id=type+'-'+worth_json['id'], body = worth_json)

def dump_worth_index():
    types = ['Wish','Demand','Skill','Meeting','Match']
    sqls = [get_select_wish_sql(), get_select_demand_sql(), get_select_skill_sql(), get_select_meeting_sql(),get_select_match_sql()]
    i = 0
    cur = conn_db.cursor()
    for type in types:
        if i == 3:
            executeWorthMeetingSql(cur, sqls[i], type)
            i += 1
            continue
        if i == 4:
            executeWorthMatchSql(cur, sqls[i], type)
            i += 1
            continue
        executeWorthSql(cur, sqls[i], type)
        i += 1
    cur.close()


def executeWorthSql(cur,sql,type):

    log.log(u'开始导入网能-'+type+'信息')
    count = cur.execute(sql)
    log.log('there has %s rows user record' % count)
    results = cur.fetchmany(count)
    index = 0
    errorIndex = 0
    for result in results:
        worthId = result[0]
        try:
            dump_one_Worth_index(result,type)
        except Exception, e:
            log.log("dump worth "+type+"Id: %s error" % worthId)
            print e.args
            errorIndex += 1
        index += 1
        if index % 50 == 0:
            log.log(u'已完成:'+ '%.2f' % (index / count * 100) + "%")
            time.sleep(0.5)
    log.log(u'完成'+type+'检索')
    log.log(u'错误行数：' + '%.2f' % errorIndex)

def executeWorthMeetingSql(cur,sql,type):
    log.log(u'开始导入网能-活动表'+type+'信息')
    count = cur.execute(sql)
    log.log('there has %s rows user record' % count)
    results = cur.fetchmany(count)
    index = 0
    errorIndex = 0
    for result in results:
        worthId = result[0]
        try:
            dump_one_Worth_Meeting_index(result,type)
        except Exception, e:
            log.log("dump worth "+type+"Id: %s error" % worthId)
            print e.args
            errorIndex += 1
        index += 1
        if index % 50 == 0:
            log.log(u'已完成:'+ '%.2f' % (index / count * 100) + "%")
            time.sleep(0.5)
    log.log(u'完成'+type+'检索')
    log.log(u'错误行数：' + '%.2f' % errorIndex)

def executeWorthMatchSql(cur,sql,type):
    log.log(u'开始导入赛事表'+type+'信息')
    count = cur.execute(sql)
    log.log('there has %s rows user record' % count)
    results = cur.fetchmany(count)
    index = 0
    errorIndex = 0
    for result in results:
        worthId = result[0]
        try:
            dump_one_Worth_Match_index(result,type)
        except Exception, e:
            log.log("dump worth "+type+"Id: %s error" % worthId)
            print e.args
            errorIndex += 1
        index += 1
        if index % 50 == 0:
            log.log(u'已完成:'+ '%.2f' % (index / count * 100) + "%")
            time.sleep(0.5)
    log.log(u'完成'+type+'检索')
    log.log(u'错误行数：' + '%.2f' % errorIndex)

def get_hit_sql(type):
    return "(select type_id,count(*) as hit from worth_click_history where type_name= '"+type+"' and deleted=0 group by type_id )"

# def get_demand_sql(type):
#     return "(select id as demandId from demand where user_id= '"+type+"' and deleted=0)"

def get_publishCreditCount_sql():
    return "(select user_id,count(*) as creditCount from credit where deleted=0 group by user_id )"

def get_select_wish_sql():
    sql = 'select w.id, w.title, w.description, h.hit, w.create_time, w.expired_at, ' \
          ' w.amount, ws.wCount, ws.total, w.wish_images_url, u.lat, u.lon, u.id, u.birthday, ' \
          ' u.nickname, u.lv, u.is_login+0, u.credit, u.last_login_at, u.last_active_at, u.sex, cr.creditCount ,w.status' \
          ' from wish as w inner join user as u on w.user_id = u.id  ' \
          ' left join '+get_hit_sql("Wish")+' as h on h.type_id=w.id ' \
                                            ' left join (select wish_id,count(*) as wCount,sum(amount) as total ' \
                                            ' from wish_support where is_pay=1 and deleted=0 group by wish_id ) as ws ' \
                                            ' on ws.wish_id = w.id left join '+get_publishCreditCount_sql()+' as cr ' \
                                                                                                            ' on cr.user_id = w.user_id where w.deleted = 0 and w.is_lock = 0 '
    return sql

def get_select_skill_sql():
    sql = 'select s.id, s.skill, s.description, h.hit, s.create_time, sregister.end_at, s.amount, ' \
          ' sregister.sCount, sregister.total, s.skill_images_url, s.lat, s.lon, u.id, u.birthday, ' \
          ' u.nickname, u.lv, u.is_login+0, u.credit, u.last_login_at, u.last_active_at, u.sex, cr.creditCount, s.status,s.unit ' \
          ' from skill as s inner join user as u on s.user_id = u.id  ' \
          ' left join '+get_hit_sql("Skill")+' as h on h.type_id=s.id ' \
                                             ' left join ( select sr.skill_id, count(*) as sCount, sum(sorder.fee) as total, ' \
                                             ' max(sorder.end_at) as end_at from skill_register as sr left join skill_order as sorder ' \
                                             ' on sr.id = sorder.skill_register_id where sr.deleted=0 and sorder.deleted = 0 group by sr.skill_id ) ' \
                                             ' as sregister on sregister.skill_id = s.id ' \
                                             ' left join '+get_publishCreditCount_sql()+' as cr on cr.user_id = s.user_id ' \
                                                                                        ' where s.deleted = 0 '
    return sql

def get_select_demand_sql():
    sql = 'select d.id, d.title, d.description, h.hit, d.create_time, d.end_at, d.bail, dregister.dCount, ' \
          ' dorder.total, d.images_url, d.lat, d.lon, u.id, u.birthday, u.nickname, u.lv, u.is_login+0, u.credit, ' \
          ' u.last_login_at, u.last_active_at, u.sex, cr.creditCount,d.status, d.unit ,d.amount' \
          ' from demand as d inner join user as u on d.user_id = u.id  ' \
          ' left join '+get_hit_sql("Demand")+' as h on h.type_id=d.id ' \
                                              ' left join (select demand_id,count(*) as DCount ,sum(bail) as total from demand_order where is_pay=1 and deleted=0 group by demand_id ) as dorder on dorder.demand_id = d.id' \
                                              ' left join (select demand_id,count(*) as dCount from demand_register where deleted=0 group by demand_id ) as dregister on dregister.demand_id = d.id left join '+get_publishCreditCount_sql()+' as cr ' \
                                                                                                                                                                                                                                             ' on cr.user_id = d.user_id where d.deleted = 0 '
    return sql

def get_select_meeting_sql():
    sql = 'select m.id, m.title, m.description, h.hit, m.create_time, m.end_at, ' \
          ' m.amount, m.reg_count, m.all_register_amount, m.meeting_images_url, m.lat, m.lon, u.id, u.birthday, ' \
          ' u.nickname, u.lv, u.is_login+0, u.credit, u.last_login_at, u.last_active_at, u.sex, cr.creditCount, m.status, m.meeting_type' \
          ' from meeting as m inner join user as u on m.user_id = u.id  ' \
          ' left join '+get_hit_sql("Meeting")+' as h on h.type_id=m.id ' \
                                               ' left join '+get_publishCreditCount_sql()+' as cr on cr.user_id = m.user_id ' \
                                                                                          ' where m.deleted = 0 '
    return sql

def get_select_match_sql():
    sql = 'SELECT m.id, m.match_image_url, m.title, m.sub_title, m.match_kind, ' \
          'm.match_rule, m.grading, m.match_introduction, m.match_status status, m.pass_time,' \
          ' u.nickname, u.sex, u.birthday, u.mobile, u.score, u.credit, u.is_login + 0, u.lv, ' \
          'c.credit_sum, d.picture_url, d.merchant_id, d.organizer_name,u.lon,u.lat, m.initiator_id,h.hit,' \
          ' ( SELECT COUNT(*) FROM match_participant p INNER JOIN match_zone z ON p.zone_id = z.id WHERE ' \
          'p.is_pay = 1 AND z.match_id = m.id ) regCount FROM match_event m INNER JOIN user u' \
          ' ON m.initiator_id = u.id LEFT JOIN ( SELECT credit.user_id, COUNT(credit.id) + 0 AS credit_sum ' \
          'FROM credit GROUP BY credit.user_id ) c ON m.id = c.user_id LEFT JOIN ( SELECT m.merchant_id, ' \
          'm.organizer_name, u.picture_url, m.match_id FROM match_review m INNER JOIN ( SELECT picture_url,' \
          ' merchant_id FROM merchant_picture WHERE merchant_picture_type = \'logo\' ) u ON m.merchant_id = u.merchant_id' \
          ' AND m.organizer_kind = 0 ) d ON m.id = d.match_id LEFT JOIN '+get_hit_sql("Match")+ \
          ' as h ON h.type_id = m.id WHERE m.is_approved = 1'
    print(sql)
    return sql


def delete_all_index():
    conn_es.indices.delete(index=get_index_name(), ignore=[400, 404])

#全量索引
def dump_all_index():
    #es_db.es_init(seller_mapping, get_index_name(), const.type_seller)
    #log.log(u"先清理之前遗留的数据")
    #delete_all_index()
    dump_time = datetime.datetime.now()
    dump_worth_index()
    #log.log(dump_time.strftime('%Y-%m-%d %X')+u'完成用户信息更新')
    search_config.write_dump_time(const.type_worth, dump_time)

#获取


def params_err_log():
    print u"参数不对,请输入'all | deleteAll'"


if len(sys.argv) < 2:
    params_err_log()
    conn_db.close()
    exit(1)
#param = 'all'
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