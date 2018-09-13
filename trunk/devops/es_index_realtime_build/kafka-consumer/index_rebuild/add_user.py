#!/usr/bin/env python
# -*-coding:utf-8-*-
from utils import es_db


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


def countToBoolean(num):
    if num is None or num ==0:
        return 0
    else:
        return 1


def add_user_to_json_body(result, json_body):
    json_body['nickname'] = result[0]
    json_body['sex'] = result[1]
    json_body['birthday'] = result[2]
    json_body['mobile'] = result[3]
    json_body['score'] = countCheckNone(result[4])
    json_body['credit'] = countCheckNone(result[5])
    json_body['isLogin'] = result[6]
    json_body['lv'] = countCheckNone(result[7])
    return json_body


def add_user(json_body, conn_db):
    #deleted = 1的情况
    if(json_body == None):
        return None
    user_id = json_body['userId']
    sql = 'select u.nickname, u.sex, u.birthday, u.mobile, u.score, u.credit, u.is_login+0,u.lv from user as u where u.id  =  \'' + user_id + '\''
    cur = conn_db.cursor()
    count = cur.execute(sql)
    results = cur.fetchmany(count)
    if (len(results) > 0):
        result = results[0]
        json_body = add_user_to_json_body(result, json_body)
    return json_body

def add_user_location_to_json_body(result, json_body):
    json_body['nickname'] = result[0]
    json_body['sex'] = result[1]
    json_body['birthday'] = result[2]
    json_body['mobile'] = result[3]
    json_body['score'] = countCheckNone(result[4])
    json_body['credit'] = countCheckNone(result[5])
    json_body['isLogin'] = result[6]
    json_body['lv'] = countCheckNone(result[7])
    json_body['location'] = str(result[9]) + "," + str(result[8])

    return json_body


def add_user_location_message(json_body, conn_db, user_id):
    # user_id = json_body['userId']
    sql = 'select u.nickname, u.sex, u.birthday, u.mobile, u.score, u.credit, u.is_login+0,u.lv ,u.lon, u.lat from user as u where u.id  =  \'' + user_id + '\''
    cur = conn_db.cursor()
    count = cur.execute(sql)
    results = cur.fetchmany(count)
    if(len(results) > 0):
        result = results[0]
        json_body = add_user_location_to_json_body(result, json_body)

    return json_body




def create_one_worth(result, worth_json):
    worth_json['location']=str(result[0]) +","+str(result[1])
    worth_json['userId']=result[2]
    worth_json['birthday']=result[3]
    worth_json['nickname']=result[4]
    worth_json['lv']=result[5]
    worth_json['isLogin']=result[6]
    worth_json['credit']=result[7]
    worth_json['loginAt']=result[8]
    worth_json['activeAt']=result[9]
    worth_json['sex']=result[10]
    return worth_json

def create_one_worth_meeting(result, worth_json):
    worth_json['userId'] = result[2]
    worth_json['birthday'] = result[3]
    worth_json['nickname'] = result[4]
    worth_json['lv'] = result[5]
    worth_json['isLogin'] = result[6]
    worth_json['credit'] = result[7]
    worth_json['loginAt'] = result[8]
    worth_json['activeAt'] = result[9]
    worth_json['sex'] = result[10]
    return worth_json

def add_user_to_worth(json_body, conn_db, is_meeting, user_id):
    sql = 'select ' \
          'u.lat, u.lon,u.id, u.birthday,  u.nickname, u.lv, u.is_login+0, u.credit, u.last_login_at, u.last_active_at, u.sex ' \
          'from user as u where u.id  =  \'' + user_id + '\''
    cur = conn_db.cursor()
    count = cur.execute(sql)
    results = cur.fetchmany(count)
    if (len(results) > 0):
        result = results[0]
        #是否需要添加用户
        if(is_meeting):
            json_body = create_one_worth_meeting(result, json_body)
        else:
            json_body = create_one_worth(result, json_body)
    return json_body

