#!/usr/bin/env python
# -*-coding:utf-8-*-
from index_rebuild.add_credit_sum import add_credit_sum, add_credit_sum_for_worth
from index_rebuild.add_hit import add_hit
from index_rebuild.add_to_wish import add_wish_support, add_worth_wish_suport
from index_rebuild.add_user import add_user_location_message, add_user_to_worth
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


def create_one_wish(result, type):
    # 第一步，重构wish_json
    one_wish_json = {}
    one_wish_json['id'] = result[0]
    one_wish_json['userId'] = result[1]
    one_wish_json['title'] = result[2]
    one_wish_json['wishLabels'] = splitLabel(result[3])
    one_wish_json['amount'] = result[4]
    one_wish_json['currentAmount'] = result[5]
    one_wish_json['currentApplyAmount'] = result[6]
    one_wish_json['expiredAt'] = result[7]
    one_wish_json['refereeIds'] = splitLabel(result[8])
    one_wish_json['refereeCount'] = result[9]
    one_wish_json['refereeAcceptCount'] = result[10]
    one_wish_json['refereeRefuseCount'] = result[11]
    one_wish_json['description'] = result[12]
    one_wish_json['imagesUrl'] = result[13]
    one_wish_json['imagesTwoUrl'] = result[14]
    one_wish_json['status'] = result[15]
    one_wish_json['createTime'] = result[16]
    one_wish_json['isLock'] = countCheckNone(result[17])
    # 第二步，重构worth_json
    worth_json = {}
    worth_json['id'] = result[0]
    worth_json['worthType'] = type
    worth_json['title'] = result[2]
    worth_json['amount'] = countCheckNone(result[4]) / 100
    worth_json['endTime'] = result[7]
    worth_json['detail'] = result[12]
    worth_json['imgesUrl'] = result[13]
    worth_json['status'] = result[15]
    worth_json['publishTime'] = result[16]
    return one_wish_json, worth_json




def get_wish(id, is_worth, conn_db):

    wish_sql = 'SELECT w.id, w.user_id, w.title, w.wish_label, w.amount, w.current_amount, w.current_apply_amount, ' \
               'w.expired_at, w.referee_ids, w.referee_count, w.referee_accept_count, w.referee_refuse_count, ' \
               'w.description, w.wish_images_url, w.wish_images_two_url, w.status, w.create_time ,w.is_lock+0 ' \
               'from wish as w where w.id = \''+ id + '\''+ ' AND w.deleted = 0'

    cur = conn_db.cursor()
    count = cur.execute(wish_sql)
    # 返回一个元祖
    results = cur.fetchmany(count)
    if(len(results) > 0):
        result = results[0]
        if(is_worth):
            skill_json, worth_json = create_one_wish(result, "Wish")
            return skill_json, worth_json
    else:
        return None, None


def wish_rebuild(id, is_worth):
    conn_db = es_db.get_mysql_connector('netx')  # 上线
    wish_body, worth_body = get_wish(id, True, conn_db)
    # deleted=1的情况
    if (wish_body == None):
        return None, None
# 处理wish
    # 第一步，注入用户信息
    wish_user_body = add_user_location_message(wish_body, conn_db)
    # 第二步，注入信用信息
    wish_user_sum_body = add_credit_sum(wish_user_body, conn_db)
    #第三步，注入wish_suport
    json_body = add_wish_support(wish_user_sum_body, conn_db)
# 处理worth
    # 第一步，注入hit
    worth_body = add_hit('Wish', worth_body, conn_db)
    # 第二步，注入wish_suport
    worth_body = add_worth_wish_suport(worth_body, conn_db)
    # 第三步，注入用户信息
    user_id = json_body['userId']
    worth_body = add_user_to_worth(worth_body, conn_db, False, user_id)
    # 第四步，注入信用
    worth_body = add_credit_sum_for_worth(worth_body, conn_db, user_id)
    conn_db.close()
    return json_body, worth_body
