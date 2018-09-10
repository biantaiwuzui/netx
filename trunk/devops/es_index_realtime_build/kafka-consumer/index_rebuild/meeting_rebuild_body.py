#!/usr/bin/env python
# -*-coding:utf-8-*-
from index_rebuild.add_credit_sum import add_credit_sum, add_credit_sum_for_worth
from index_rebuild.add_hit import add_hit
from index_rebuild.add_user import add_user, add_user_to_worth
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

def create_one_meeting(result):
    one_meeting_json = {}
    one_meeting_json['id'] = result[0]
    one_meeting_json['userId'] = result[1]
    one_meeting_json['title'] = result[2]
    one_meeting_json['meetingType'] = result[3]
    one_meeting_json['meetingLabels'] = splitLabel(result[4])
    one_meeting_json['startedAt'] = result[5]
    one_meeting_json['endAt'] = result[6]
    one_meeting_json['regStopAt'] = result[7]
    one_meeting_json['obj'] = result[8]
    one_meeting_json['objList'] = result[9]
    one_meeting_json['amount'] = result[10]
    one_meeting_json['address'] = result[11]
    one_meeting_json['orderIds'] = result[12]
    one_meeting_json['orderPrice'] = result[13]
    one_meeting_json['location'] = str(result[14]) + "," + str(result[15])
    one_meeting_json['description'] = result[16]
    one_meeting_json['meetingImagesUrl'] = result[17]
    one_meeting_json['posterImagesUrl'] = result[18]
    one_meeting_json['ceil'] = result[19]
    one_meeting_json['floor'] = result[20]
    one_meeting_json['regCount'] = result[21]
    one_meeting_json['regSuccessCount'] = result[22]
    one_meeting_json['status'] = result[23]
    one_meeting_json['feeNotEnough'] = result[24]
    one_meeting_json['isConfirm'] = result[25]
    one_meeting_json['lockVersion'] = result[26]
    one_meeting_json['allRegisterAmount'] = result[27]
    one_meeting_json['balance'] = result[28]
    one_meeting_json['isBalancePay'] = result[29]
    one_meeting_json['payFrom'] = result[30]
    one_meeting_json['meetingFeePayAmount'] = result[31]
    one_meeting_json['meetingFeePayFrom'] = result[32]
    one_meeting_json['meetingFeePayType'] = result[33]
    one_meeting_json['createTime'] = result[34]
    worth_json = {}
    worth_json['id']=result[0]
    worth_json['worthType']='Meeting'
    worth_json['title']=result[1]
    worth_json['detail']=result[16]
    worth_json['publishTime']=result[34]
    worth_json['endTime']=result[6]
    worth_json['amount']=countCheckNone(result[10])/100
    worth_json['dealsCount']=countCheckNone(result[21])
    worth_json['dealsTotal']=countCheckNone(result[27])/100
    worth_json['imgesUrl']=result[17]
    worth_json['location']=str(result[14]) +","+str(result[15])
    worth_json['status']=result[23]
    worth_json['meetingType']=result[3]
    return one_meeting_json, worth_json


def get_meeting(id, no_cache, conn_db):
    m_sql = 'SELECT m.id, m.user_id, m.title, m.meeting_type, m.meeting_label, m.started_at, m.end_at, ' \
            ' m.reg_stop_at, m.obj, m.obj_list, m.amount, m.address, m.order_ids, m.order_price, m.lat, ' \
            ' m.lon, m.description, m.meeting_images_url, m.poster_images_url, m.ceil, m.floor, m.reg_count, m.reg_success_count, ' \
            ' m.status, m.fee_not_enough, m.is_confirm+0, m.lock_version, m.all_register_amount, m.balance, ' \
            ' m.is_balance_pay+0, m.pay_from, m.meeting_fee_pay_amount, m.meeting_fee_pay_from, m.meeting_fee_pay_type, m.create_time from meeting as m where ' \
            'm.id = \''+ id + '\''+ ' AND m.deleted = 0'

    cur = conn_db.cursor()
    count = cur.execute(m_sql)
    results = cur.fetchmany(count)
    if(len(results) > 0):
        result = results[0]
        meeting_json, worth_json = create_one_meeting(result)
        return meeting_json, worth_json
    else:
        return None, None

def meeting_rebuild(id, no_cache):
    conn_db = es_db.get_mysql_connector('netx')  # 上线
    meeting_json, worth_json = get_meeting(id, no_cache, conn_db)
    if (meeting_json == None):
        return None, None
    user_id = meeting_json['userId']
# 构造meeting
    meeting_user_body = add_user(meeting_json, conn_db)
    meeting_user_credit_body = add_credit_sum(meeting_user_body, conn_db)
# 构造worth_json
    # 第一步，注入用户信息
    worth_json = add_user_to_worth(worth_json, conn_db, True, user_id)
    # 第二步，注入hit
    worth_json = add_hit('Meeting', worth_json, conn_db)
    # 第三步，注入is_holdCredit
    worth_json = add_credit_sum_for_worth(worth_json, conn_db, user_id)
    return meeting_user_credit_body, worth_json

