#!/usr/bin/env python
# -*- coding:utf-8-*-

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
#conn_db = es_db.get_mysql_connector('dev_netx')#开发

create_index_body = {
    'settings': {
        # just one shard, no replicas for testing
        'number_of_shards': 1,
        'number_of_replicas': 5
    },
    'mappings': {
        'userFriend': {
            'properties': {
                'id': {'type': 'text'},
                'userNumber': {'type': 'keyword'},
                'nickName': {'type': 'keyword'},
                'realName': {'type': 'keyword'},
                'sex': {'type': u'text'},
                'birthday': {'type': 'date'},
                'mobile': {'type': 'text'},
                'isAdminUser': {'type': 'integer'},
                'isLock': {'type': 'integer'},
                'lockVersion': {'type': 'integer'},
                'idNumber': {'type': 'text'},
                'video': {'type': u'text'},
                'car': {'type': u'text'},
                'house': {'type': u'text'},
                'degree': {'type': u'text'},
                'education': {'type': u'text'},
                'profession': {'type': u'text'},
                'interest': {'type': u'text'},
                'loginAt': {'type': 'date'},
                'activeAt': {'type': 'date'},
                'loginDays': {'type': 'integer'},
                'location': {'type': 'geo_point'},
                'giftSetting': {'type': 'integer'},
                'invitationSetting': {'type': 'integer'},
                'articleSetting': {'type': 'integer'},
                'nearlySetting': {'type': 'integer'},
                'voiceSetting': {'type': 'integer'},
                'shockSetting': {'type': 'integer'},
                'score': {'type': 'double'},
                'credit': {'type': 'integer'},
                'value': {'type': 'double'},
                'totalIncome': {'type': 'double'},
                'contribution': {'type': 'double'},
                'lv': {'type': 'integer'},
                'profileScore': {'type': 'long'},
                'completePercent': {'type': 'double'},
                'currentLikes': {'type': 'long'},
                'watchTo': {'type': 'long'},
                'watchFrom': {'type': 'long'},
                'approvalTime': {'type': 'date'},
                'isLogin': {'type': 'integer'},
                'isBackend': {'type': 'integer'},
                'headImg': {'type': 'text'},
                'oftenIn': {'type': 'keyword'},
                'homeTown': {'type': 'keyword'},
                'alreadyTo': {'type': 'keyword'},
                'wantTo': {'type': 'keyword'},
                'address': {'type': 'keyword'},
                'introduce': {'type': 'keyword'},
                'disposition': {'type': 'keyword'},
                'appearance': {'type': 'keyword'},
                'income': {'type': 'integer'},
                'maxIncome': {'type': 'integer'},
                'emotion': {'type': 'keyword'},
                'height': {'type': 'integer'},
                'weight': {'type': 'integer'},
                'nation': {'type': u'text'},
                'animalSigns': {'type': u'text'},
                'starSign': {'type': u'text'},
                'bloodType': {'type': u'text'},
                'degreeItem': {'type': u'text', 'term_vector': 'with_positions_offsets'},
                'school': {'type': u'text', 'term_vector': 'with_positions_offsets'},
                'company': {'type': u'text', 'term_vector': 'with_positions_offsets'},
                'interestDetails': {'type': u'text', 'term_vector': 'with_positions_offsets'},
                'topProfession': {'type': u'text', 'term_vector': 'with_positions_offsets'}
            }
        }
    }
}


def get_index_name():
    return const.index_user_name


if conn_es.indices.exists(index=get_index_name()):
    conn_es.indices.delete(index=get_index_name(), ignore=400)
conn_es.indices.create(index=get_index_name(), body=create_index_body)


def create_one_user(result):
    user_json = {}
    user_json['id'] = result[0]
    user_json['userNumber'] = result[1]
    user_json['nickName'] = result[2]
    user_json['realName'] = result[3]
    user_json['sex'] = result[4]
    user_json['birthday'] = result[5]
    user_json['mobile'] = result[6]
    user_json['isAdminUser'] = result[7]
    user_json['isLock'] = result[8]
    user_json['lockVersion'] = result[9]
    user_json['idNumber'] = result[10]
    user_json['video'] = result[11]
    user_json['car'] = result[12]
    user_json['house'] = result[13]
    user_json['degree'] = result[14]
    user_json['education'] = result[15]
    user_json['profession'] = result[16]
    user_json['interest'] = result[17]
    user_json['loginAt'] = result[18]
    user_json['activeAt'] = result[19]
    user_json['loginDays'] = result[20]
    user_json['location'] = str(result[22]) + "," + str(result[21])
    user_json['giftSetting'] = result[23]
    user_json['invitationSetting'] = result[24]
    user_json['articleSetting'] = result[25]
    user_json['nearlySetting'] = result[26]
    user_json['voiceSetting'] = result[27]
    user_json['shockSetting'] = result[28]
    user_json['score'] = result[29]
    user_json['credit'] = result[30]
    user_json['value'] = result[31]
    user_json['totalIncome'] = result[32]
    user_json['contribution'] = result[33]
    user_json['lv'] = result[34]
    user_json['profileScore'] = result[35]
    user_json['completePercent'] = result[36]
    user_json['currentLikes'] = result[37]
    user_json['watchTo'] = result[38]
    user_json['watchFrom'] = result[39]
    user_json['approvalTime'] = result[40]
    user_json['isLogin'] = result[41]
    user_json['isBackend'] = result[42]
    user_json['headImg'] = result[43]
    user_json['oftenIn'] = result[44]
    user_json['homeTown'] = result[45]
    user_json['alreadyTo'] = result[46]
    user_json['wantTo'] = result[47]
    user_json['address'] = result[48]
    user_json['introduce'] = result[49]
    user_json['disposition'] = result[50]
    user_json['appearance'] = result[51]
    user_json['income'] = result[52]
    user_json['maxIncome'] = result[53]
    user_json['emotion'] = result[54]
    user_json['height'] = result[55]
    user_json['weight'] = result[56]
    user_json['nation'] = result[57]
    user_json['animalSigns'] = result[58]
    user_json['starSign'] = result[59]
    user_json['bloodType'] = result[60]
    user_json['degreeItem'] = splitLabel(result[61])
    user_json['school'] = splitLabel(result[62])
    user_json['company'] = splitLabel(result[63])
    user_json['interestDetails'] = splitLabel(result[64])
    user_json['topProfession'] = splitLabel(result[65])
    return user_json


def splitLabel(ids):
    arr = []
    if not ids is None:
        idsArr = ids.split(",")
        for tagId in idsArr:
            arr.append(tagId)
    return arr


# def dump_one_friendUser_index(result):
#    user_json = create_one_user(result)
#    conn_es.create(index = get_index_name(), doc_type = const.type_user, id = user_json['id'], body = user_json)

# def dump_userFriend_index(sql):
#   cur = conn_db.cursor()
#   log.log(u'开始导入用户信息')
#   count = cur.execute(sql)
#   log.log('there has %s rows user record' % count)
#   results = cur.fetchmany(count)
#   index = 0
#   errorIndex = 0
#   for result in results:
#       userId = result[0]
#       try:
#           dump_one_friendUser_index(result)
#       except Exception, e:
#           log.log("dump user id: %s error" % userId)
#           print e.args
#           errorIndex += 1
#       index += 1
#       if index % 50 == 0:
#           log.log(u'已完成:'+ '%.2f' % (index / count * 100) + "%")
#           time.sleep(0.5)
#   log.log(u'完成用户信息检索')
#   log.log(u'错误行数：' + '%.2f' % errorIndex)
#   cur.close()


def dump_userFriend_index(sql):
    cur = conn_db.cursor()
    log.log(u'开始导入用户信息')
    count = cur.execute(sql)
    log.log('there has %s rows user record' % count)
    results = cur.fetchmany(count)
    index = 0
    errorIndex = 0
    for result in results:
        userId = result[0]
        try:
            dump_one_user_index(result, cur)
        except Exception, e:
            log.log("dump user id: %s error" % userId)
            print e.args
            errorIndex += 1
        index += 1
        if index % 50 == 0:
            log.log(u'已完成:' + '%.2f' % (index / count * 100) + "%")
            time.sleep(0.5)
    log.log(u'完成用户信息检索')
    log.log(u'错误行数：' + '%.2f' % errorIndex)
    cur.close()


def create_user(result):
    user_json = {}
    user_json['id'] = result[0]
    user_json['userNumber'] = result[1]
    user_json['nickName'] = result[2]
    user_json['realName'] = result[3]
    user_json['sex'] = result[4]
    user_json['birthday'] = result[5]
    user_json['mobile'] = result[6]
    user_json['isAdminUser'] = result[7]
    user_json['isLock'] = result[8]
    user_json['lockVersion'] = result[9]
    user_json['idNumber'] = result[10]
    user_json['video'] = result[11]
    user_json['car'] = result[12]
    user_json['house'] = result[13]
    user_json['degree'] = result[14]
    user_json['education'] = result[15]
    user_json['profession'] = result[16]
    user_json['interest'] = result[17]
    user_json['loginAt'] = result[18]
    user_json['activeAt'] = result[19]
    user_json['loginDays'] = result[20]
    user_json['location'] = str(result[22]) + "," + str(result[21])
    user_json['giftSetting'] = result[23]
    user_json['invitationSetting'] = result[24]
    user_json['articleSetting'] = result[25]
    user_json['nearlySetting'] = result[26]
    user_json['voiceSetting'] = result[27]
    user_json['shockSetting'] = result[28]
    user_json['score'] = result[29]
    user_json['credit'] = result[30]
    user_json['value'] = result[31]
    user_json['totalIncome'] = result[32]
    user_json['contribution'] = result[33]
    user_json['lv'] = result[34]
    user_json['profileScore'] = result[35]
    user_json['completePercent'] = result[36]
    user_json['currentLikes'] = result[37]
    user_json['watchTo'] = result[38]
    user_json['watchFrom'] = result[39]
    user_json['approvalTime'] = result[40]
    user_json['isLogin'] = result[41]
    user_json['isBackend'] = result[42]
    user_json['headImg'] = result[43]
    user_json['oftenIn'] = result[44]
    user_json['homeTown'] = result[45]
    user_json['alreadyTo'] = result[46]
    user_json['wantTo'] = result[47]
    user_json['address'] = result[48]
    user_json['introduce'] = result[49]
    user_json['disposition'] = result[50]
    user_json['appearance'] = result[51]
    user_json['income'] = result[52]
    user_json['maxIncome'] = result[53]
    user_json['emotion'] = result[54]
    user_json['height'] = result[55]
    user_json['weight'] = result[56]
    user_json['nation'] = result[57]
    user_json['animalSigns'] = result[58]
    user_json['starSign'] = result[59]
    user_json['bloodType'] = result[60]
    return user_json


def dump_one_user_index(result, cur):
    user_json = create_user(result)
    dump_userBean_education_index(user_json, cur)  # 获取教育信息
    dump_userBean_interest_index(user_json, cur)  # 获取兴趣爱好
    dump_userBean_profession_index(user_json, cur)  # 获取工作经历
    conn_es.create(index=get_index_name(), doc_type=const.type_user, id=user_json['id'], body=user_json)


def userBean_sql():
    sql = 'select u.id, u.user_number, u.nickname, u.real_name, u.sex, u.birthday, u.mobile, u.is_admin_user+0, u.is_lock+0, ' \
          'u.lock_version, u.id_number, u.video, u.car, u.house, u.degree, u.education_label, u.profession_label, ' \
          'u.interest_label, u.last_login_at, u.last_active_at, u.login_days, u.lon, u.lat, u.gift_setting+0, u.invitation_setting+0, ' \
          'u.article_setting+0, u.nearly_setting+0, u.voice_setting+0, u.shock_setting+0, u.score, u.credit, u.value, u.income, ' \
          'u.contribution, u.lv, u.user_profile_score, u.last_complete_percent, u.current_likes, u.current_watch_to, ' \
          'u.current_watch_from, u.approval_time, u.is_login+0, u.is_login_backend+0, photo.headImg, profile.often_in, ' \
          'profile.home_town, profile.already_to, profile.want_to, profile.address, profile.introduce, profile.disposition, ' \
          'profile.appearance, profile.income, profile.max_income, profile.emotion, profile.height, profile.weight, ' \
          'profile.nation, profile.animal_signs, profile.star_sign, profile.blood_type from user u ' \
          ' left join ( select user_id, url as headImg from user_photo as p ' \
          ' where p.position=1 and p.deleted=0 ) as photo on u.id=photo.user_id ' \
          ' left join user_profile as profile on u.id=profile.user_id ' \
          ' where u.deleted = 0 and profile.deleted = 0 '
    return sql


def dump_userBean_education_index(user_json, cur):
    sql = 'select degree,school from user_education where user_education.deleted=0 and user_id="' + user_json[
        'id'] + '"'
    count = cur.execute(sql)
    log.log(user_json['id'] + 'there has %s rows userEducation record' % count)
    results = cur.fetchall()
    user_json['degreeItem'] = []
    user_json['school'] = []
    for result in results:
        if result[0] is not None:
            user_json['degreeItem'].append(result[0])
        if result[1] is not None:
            user_json['school'].append(result[1])


def dump_userBean_interest_index(user_json, cur):
    sql = 'select interest_detail from user_interest where user_id = "' + user_json[
        'id'] + '" and user_interest.deleted=0'
    count = cur.execute(sql)
    log.log(user_json['id'] + 'there has %s rows userInterest record' % count)
    results = cur.fetchall()
    user_json['interestDetails'] = []
    for result in results:
        user_json['interestDetails'].append(result[0])


def dump_userBean_profession_index(user_json, cur):
    sql = 'select company, top_profession from user_profession where user_profession.deleted=0 and user_id = "' + \
          user_json['id'] + '"'
    count = cur.execute(sql)
    log.log(user_json['id'] + 'there has %s rows userProfession record' % count)
    results = cur.fetchall()
    user_json['company'] = []
    user_json['topProfession'] = []
    for result in results:
        if result[0] is not None:
            user_json['company'].append(result[0])
        if result[1] is not None:
            user_json['topProfession'].append(result[1])


# def get_select_all_user_sql():
#    sql = 'select u.id, u.user_number, u.nickname, u.real_name, u.sex, u.birthday, u.mobile, u.is_admin_user+0, u.is_lock+0, ' \
#          'u.lock_version, u.id_number, u.video, u.car, u.house, u.degree, u.education_label, u.profession_label, ' \
#          'u.interest_label, u.last_login_at, u.last_active_at, u.login_days, u.lon, u.lat, u.gift_setting+0, u.invitation_setting+0, ' \
#          'u.article_setting+0, u.nearly_setting+0, u.voice_setting+0, u.shock_setting+0, u.score, u.credit, u.value, u.income, ' \
#          'u.contribution, u.lv, u.user_profile_score, u.last_complete_percent, u.current_likes, u.current_watch_to, ' \
#          'u.current_watch_from, u.approval_time, u.is_login+0, u.is_login_backend+0, photo.headImg, profile.often_in, ' \
#          'profile.home_town, profile.already_to, profile.want_to, profile.address, profile.introduce, profile.disposition, ' \
#          'profile.appearance, profile.income, profile.max_income, profile.emotion, profile.height, profile.weight, ' \
#          'profile.nation, profile.animal_signs, profile.star_sign, profile.blood_type, ueducation.degree, ueducation.school, ' \
#          'profession.company,interest.detail,profession.top_profession from user u ' \
#          ' left join ( select user_id, url as headImg from user_photo as p ' \
#          ' where p.position=1 and p.deleted=0 ) as photo on u.id=photo.user_id ' \
#          ' left join user_profile as profile on u.id=profile.user_id ' \
#          ' left join ( select user_id, GROUP_CONCAT(degree) as degree, GROUP_CONCAT(school) as school from user_education ' \
#          ' where user_education.deleted=0 group by user_id ) as ueducation on u.id=ueducation.user_id ' \
#          ' left join ( select user_id, GROUP_CONCAT(company) as company, GROUP_CONCAT(top_profession) as top_profession from user_profession ' \
#          ' where user_profession.deleted=0 group by user_id ) as profession on u.id=profession.user_id ' \
#          ' left join ( select user_id, GROUP_CONCAT(interest_detail) as detail from user_interest ' \
#          ' where user_interest.deleted=0 group by user_id ) as interest on interest.user_id=u.id ' \
#          ' where u.deleted = 0 and profile.deleted = 0 '
#    return sql

def delete_all_index():
    conn_es.indices.delete(index=get_index_name(), ignore=[400, 404])


# 全量索引
def dump_all_index():
    # es_db.es_init(seller_mapping, get_index_name(), const.type_seller)
    # log.log(u"先清理之前遗留的数据")
    # delete_all_index()
    dump_time = datetime.datetime.now()
    dump_userFriend_index(userBean_sql())
    # log.log(dump_time.strftime('%Y-%m-%d %X')+u'完成用户信息更新')
    search_config.write_dump_time(const.type_user, dump_time)


def params_err_log():
    print u"参数不对,请输入'all | deleteAll'"


if len(sys.argv) < 2:
    params_err_log()
    conn_db.close()
    exit(1)
# param = 'all'
param = sys.argv[1]
if param == 'all':
    # 导入中文简体搜索库
    dump_all_index()
elif param == 'deleteAll':
    print 'deleteAll'
    log.log(u"清理此索引所有数据")
    delete_all_index()
    log.log(u"清理成功")
else:
    params_err_log()
conn_db.close()
