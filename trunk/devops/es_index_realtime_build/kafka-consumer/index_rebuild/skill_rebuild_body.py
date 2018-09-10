#!/usr/bin/env python
# -*-coding:utf-8-*-
from index_rebuild.add_credit_sum import add_credit_sum, add_credit_sum_for_worth
from index_rebuild.add_hit import add_hit
from index_rebuild.add_to_skill import add_worth_skill
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


def create_one_skill(result):
    one_skill_json = {}
    one_skill_json['id'] = result[0]
    one_skill_json['userId'] = result[1]
    one_skill_json['skillLabels'] = splitLabel(result[2])
    one_skill_json['levels'] = splitLabel(result[3])
    one_skill_json['description'] = result[4]
    one_skill_json['skillImagesUrl'] = result[5]
    one_skill_json['skillDetailImagesUrl'] = result[6]
    one_skill_json['unit'] = result[7]
    one_skill_json['amount'] = result[8]
    one_skill_json['intr'] = result[9]
    one_skill_json['obj'] = result[10]
    one_skill_json['location'] = str(result[12]) + "," + str(result[11])
    one_skill_json['registerCount'] = result[13]
    one_skill_json['successCount'] = result[14]
    one_skill_json['status'] = result[15]
    one_skill_json['createTime'] = result[16]

    worth_json = {}
    worth_json['id'] = result[0]
    worth_json['worthType'] = 'Skill'
    worth_json['title'] = result[2]
    worth_json['detail'] = result[4]
    worth_json['publishTime'] = result[16]
    worth_json['amount'] = countCheckNone(result[8]) / 100
    worth_json['imgesUrl'] = result[5]
    worth_json['location'] = str(result[12]) + "," + str(result[11])
    worth_json['status'] = result[15]
    worth_json['unit'] = result[7]

    return one_skill_json, worth_json


def get_skill(id, no_cache, conn_db):
    skill_sql = 'SELECT s.id, s.user_id, s.skill, s.level, s.description, s.skill_images_url, s.skill_detail_images_url, s.unit, ' \
                ' s.amount, s.intr, s.obj, s.lon, s.lat, s.register_count, s.success_count, s.status, s.create_time ' \
                'from skill AS s WHERE s.id =\''+ id + '\''+ ' AND s.deleted = 0'

    cur = conn_db.cursor()
    count = cur.execute(skill_sql)
    results = cur.fetchmany(count)
    if(len(results) > 0):
        result = results[0]
        skill_json, worth_json = create_one_skill(result)
        return skill_json, worth_json
    else:
        return None, None


def skill_rebuild(id, no_cache):
    conn_db = es_db.get_mysql_connector('netx')  # 上线
    skill_body, worth_json = get_skill(id, no_cache, conn_db)
    # deleted=1的情况
    if (skill_body == None):
        return None, None
#构造skill_json
    skill_user_body = add_user(skill_body, conn_db)
    json_body = add_credit_sum(skill_user_body, conn_db)
#构造worth_json
    #第一步，注入用户资料
    user_id = json_body['userId']
    worth_json = add_user_to_worth(worth_json, conn_db, True, user_id)
    #第二步，注入skill关联信息
    skill_id = json_body['id']
    worth_json = add_worth_skill(skill_id, worth_json, conn_db)
    #第三步，注入is_holdCredit
    worth_json = add_credit_sum_for_worth(worth_json, conn_db, user_id)
    #第四步，注入hit
    worth_json = add_hit('Skill', worth_json, conn_db)
    conn_db.close()
    return json_body, worth_json

