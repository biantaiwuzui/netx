#!/usr/bin/env python
#-*- coding:utf-8-*-

from __future__ import division
import sys
import datetime
import time

from utils import log, es_db, search_config
from utils.util import get_time_long
from common import const


#定义索引存储结构
user_mapping = {
    u'userId': {'boost': 1.0,
                'index': 'analyzed',
                'store': 'yes',
                'type': u'string',
                "term_vector": "with_positions_offsets"},
    u'userName': {'boost': 1.0,
                  'index': 'analyzed',
                  'store': 'yes',
                  'type': u'string',
                  "term_vector": "with_positions_offsets"},
    u'countryCode': {'boost': 1.0,
                     'index': 'not_analyzed',
                     'store': 'yes',
                     'type': u'string'},
    u'phone': {'boost': 1.0,
               'index': 'analyzed',
               'store': 'yes',
               'type': u'string',
               "term_vector": "with_positions_offsets"},
    u'email': {'boost': 1.0,
               'index': 'not_analyzed',
               'store': 'yes',
               'type': u'string'},
    u'nickname': {'boost': 1.0,
                  'index': 'analyzed',
                  'store': 'yes',
                  'type': u'string',
                  "term_vector": "with_positions_offsets"},
    u'headPicUrl': {'boost': 1.0,
                    'index': 'not_analyzed',
                    'store': 'yes',
                    'type': u'string'},
    u'grade': {'boost': 1.0,
               'index': 'analyzed',
               'store': 'yes',
               'type': u'string',
               "term_vector": "with_positions_offsets"},
    u'accountType': {'boost': 1.0,
                     'index': 'analyzed',
                     'store': 'yes',
                     'type': u'string',
                     "term_vector": "with_positions_offsets"},
    u'isActive': {'boost': 1.0,
                  'index': 'not_analyzed',
                  'store': 'yes',
                  'type': u'integer'},
    u'registerDate': {'boost': 1.0,
                      'index': 'not_analyzed',
                      'store': 'yes',
                      'type': u'long'},
    u'registerIp': {'boost': 1.0,
                    'index': 'not_analyzed',
                    'store': 'yes',
                    'type': u'string'},
    u'loginCount': {'boost': 1.0,
                    'index': 'not_analyzed',
                    'store': 'yes',
                    'type': u'integer'},
    u'loginTime': {'boost': 1.0,
                   'index': 'not_analyzed',
                   'store': 'yes',
                   'type': u'long'},
    u'expenseTotal': {'boost': 1.0,
                      'index': 'not_analyzed',
                      'store': 'yes',
                      'type': u'long'},
    u'payPoints': {'boost': 1.0,
                   'index': 'not_analyzed',
                   'store': 'yes',
                   'type': u'long'},
    u'userMoney': {'boost': 1.0,
                   'index': 'not_analyzed',
                   'store': 'yes',
                   'type': u'long'},
    u'productLi': {'boost': 1.0,
                   'index': 'analyzed',
                   'store': 'yes',
                   'type': u'string',
                   "term_vector": "with_positions_offsets"},
    u'brandLi': {'boost': 1.0,
                 'index': 'analyzed',
                 'store': 'yes',
                 'type': u'string',
                 "term_vector": "with_positions_offsets"},
    u'isReturned': {'boost': 1.0,
                    'index': 'not_analyzed',
                    'store': 'yes',
                    'type': u'integer'},
    u'sendSmsTimes': {'boost': 1.0,
                      'index': 'not_analyzed',
                      'store': 'yes',
                      'type': u'integer'},
    u'lastSmsTime': {'boost': 1.0,
                     'index': 'not_analyzed',
                     'store': 'yes',
                     'type': u'long'},
    u'fansNum': {'boost': 1.0,
                 'index': 'not_analyzed',
                 'store': 'yes',
                 'type': u'integer'},
    u'followNum': {'boost': 1.0,
                   'index': 'not_analyzed',
                   'store': 'yes',
                   'type': u'integer'},
    u'lastPayTime': {'boost': 1.0,
                     'index': 'not_analyzed',
                     'store': 'yes',
                     'type': u'long'},
    u'sumOrderFee': {'boost': 1.0,
                     'index': 'not_analyzed',
                     'store': 'yes',
                     'type': u'integer'},
    u'orderCount': {'boost': 1.0,
                    'index': 'not_analyzed',
                    'store': 'yes',
                    'type': u'integer'},
    u'avgOrderFee': {'boost': 1.0,
                     'index': 'not_analyzed',
                     'store': 'yes',
                     'type': u'integer'},
    u'orderSources': {'boost': 1.0,
                      'index': 'analyzed',
                      'store': 'yes',
                      'type': u'string',
                      "term_vector": "with_positions_offsets"},
    ##IOS,ANDROID,B2C,WECHAT
    u'systemType': {'boost': 1.0,
                    'index': 'analyzed',
                    'store': 'yes',
                    'type': u'string',
                    "term_vector": "with_positions_offsets"},
    u'cartProducts': {'boost': 1.0,
                      'index': 'analyzed',
                      'store': 'yes',
                      'type': u'string',
                      "term_vector": "with_positions_offsets"},
    u'unpaidProducts': {'boost': 1.0,
                        'index': 'analyzed',
                        'store': 'yes',
                        'type': u'string',
                        "term_vector": "with_positions_offsets"},
    u'lastCartTime': {'boost': 1.0,
                      'index': 'not_analyzed',
                      'store': 'yes',
                      'type': u'long'},
    u'devices': {'boost': 1.0,
                 'index': 'analyzed',
                 'store': 'yes',
                 'type' : u'string',
                 'term_vector': 'with_positions_offsets'},
    u'PROFBrands': {'boost': 1.0,
                    'index': 'analyzed',
                    'store': 'yes',
                    'type' : u'string',
                    'term_vector': 'with_positions_offsets'},
    u'PROFCategorys': {'boost': 1.0,
                       'index': 'analyzed',
                       'store': 'yes',
                       'type' : u'string',
                       'term_vector': 'with_positions_offsets'},
    u'PROFEfficacys': {'boost': 1.0,
                       'index': 'analyzed',
                       'store': 'yes',
                       'type' : u'string',
                       'term_vector': 'with_positions_offsets'},
    u'deviceToken': {
        'type': 'object',
         'properties': {
             'type': {'boost': 1.0, 'index': 'not_analyzed', 'store': 'yes', 'type': u'string'},
             'tokens': {'boost': 1.0, 'index': 'analyzed', 'store': 'yes', 'type': u'string', 'term_vector': 'with_positions_offsets'}
        }
    }
}


conn_db = es_db.get_conn_db()
conn_es = es_db.get_conn_es()


def get_last_cart_update_time_by_user_id(uid):
    sql = 'select if(ci.update_time is null, 0, UNIX_TIMESTAMP(ci.update_time)), c.user_id from cart_item ci ' \
          'left join cart c on ci.cart_id = c.id where c.user_id = %d and c.status = 0 ' \
          'and ci.status = 0 order by ci.update_time desc limit 1' % uid
    cur = conn_db.cursor()
    cur.execute(sql)
    result = cur.fetchone()
    if result is not None:
        return result[0]
    else:
        return 0
    pass


def process_order_info_by_user_id(uid, user_json):
    sql = 'select last_pay_time, sum_order_fee, order_count, sum_order_fee/order_count avg_order_fee, order_sources ' \
          'from (' \
          'select max(pay_receive_time) last_pay_time, sum(order_total_fee) sum_order_fee,count(*) order_count from( ' \
          'select order_no, pay_receive_time, order_total_fee from order_info where pay_receive_time > "1970-01-01" ' \
          'and user_id=%d and trade_status in ("TRADE_SHIPPED","TRADE_PAYED","TRADE_FINISHED")' \
          ')t3)t1' \
          ',(select GROUP_CONCAT(DISTINCT system_type SEPARATOR " ") order_sources from order_info ' \
          'where pay_receive_time > "1970-01-01" and trade_status in ("TRADE_SHIPPED","TRADE_PAYED","TRADE_FINISHED") '\
          'and user_id=%d)t2;' % (uid, uid)
    cur = conn_db.cursor()
    cur.execute(sql)
    result = cur.fetchone()
    if result is not None:
        user_json["lastPayTime"] = get_time_long(result[0])
        if result[1] is None:
            user_json["sumOrderFee"] = 0
        else:
            user_json["sumOrderFee"] = result[1]
        user_json["orderCount"] = result[2]
        if result[3] is None:
            user_json["avgOrderFee"] = 0
        else:
            user_json["avgOrderFee"] = result[3]
        user_json["orderSources"] = result[4]

    pass


def dump_all_index():
    es_db.es_init(user_mapping, const.index_name, const.type_user)

    from_time = '1970-01-01'
    dump_time = datetime.datetime.now()

    sql = DUMP_USER_SQL + ' where update_time BETWEEN "%s" AND "%s"' % (from_time, dump_time)
    print sql
    dump_user_index(sql)
    search_config.write_dump_time('user', dump_time)
    pass


def dump_increment_index():
    from_time = search_config.read_last_dump_time('user')
    dump_time = datetime.datetime.now()

    sql = DUMP_USER_SQL + ' where status = 0 and update_time BETWEEN "%s" AND "%s"' % (from_time, dump_time)
    print sql
    dump_user_index(sql)

    sql = DUMP_USER_SQL + ' where id in (select DISTINCT user_id from order_info where pay_receive_time ' \
                          'BETWEEN "%s" and "%s")' % (from_time, dump_time)
    print sql
    dump_user_index(sql)

    ## 更新user关联购物车和未支付订单商品ID
    dump_user_cart_unpaid_products(from_time, dump_time)
    delete_users_index()
    search_config.write_dump_time('user', dump_time)
    pass


def delete_one_user(uid):
    conn_es.delete(const.index_name, const.type_user, uid)
    pass


def delete_users_index():
    from_time = search_config.read_last_dump_time('user')
    dump_time = datetime.datetime.now()

    sql = 'select id from user where status=1 and update_time BETWEEN "%s" AND "%s"' % (from_time, dump_time)
    print sql
    cur = conn_db.cursor()
    log.log('开始删除无效用户')
    count = cur.execute(sql)
    log.log('there has %s invalid user' % count)

    results = cur.fetchmany(count)
    index = 0
    for result in results:
        uid = result[0]

        try:
            delete_one_user(uid)
        except Exception, e:
            pass

        index += 1
        if index % 100 == 0:
            log.log('已完成:'+'%.2f' % (index / count * 100) + "%")
    pass


def process_common_user_info(result, user_json):
    user_id = result[0]
    user_json["userId"] = user_id
    user_json["userName"] = result[1]
    user_json["email"] = result[2]
    user_json["nickname"] = result[3]
    phone = result[4]
    if phone == '':
        phone = get_phone_by_order_info(user_id)
    user_json["phone"] = phone
    user_json["registerDate"] = get_time_long(result[5])
    user_json["registerIp"] = result[6]
    user_json["loginTime"] = get_time_long(result[7])
    user_json["loginCount"] = result[8]
    user_json["accountType"] = result[9]
    user_json["headPicUrl"] = result[10]
    user_json["dumpTime"] = time.strftime("%Y-%m-%d %H:%M:%S", time.localtime())
    pass


def process_product_brand_ids_by_user_id(user_id, user_json):
    sql = 'select GROUP_CONCAT(DISTINCT t4.product_id SEPARATOR " ") product_ids, ' \
          'GROUP_CONCAT(DISTINCT brand_id SEPARATOR " ") brand_ids from brand_product t3 join( ' \
          'select product_id from order_info t1 join order_item t2 on t1.order_no=t2.order_no and ' \
          't1.pay_time > "1970-01-01" and t1.user_id=%d )t4 on t3.product_id=t4.product_id and ' \
          't3.status=0;' % user_id
    cur = conn_db.cursor()
    cur.execute(sql)
    result = cur.fetchone()
    if result is not None:
        user_json["productLi"] = result[0]
        user_json["brandLi"] = result[1]
    pass


def get_phone_by_order_info(user_id):
    sql = 'select mobile from order_info where user_id = %d and (mobile != "" || mobile is not null) ' \
          'order by create_time desc limit 1' % user_id
    cur = conn_db.cursor()
    cur.execute(sql)
    result = cur.fetchone()
    if result is not None:
        return result[0]
    else:
        return ''
    pass


def dump_one_index(result):
    user_id = result[0]
    try:
        user_json = conn_es.get(const.index_name, const.type_user, user_id)
    except Exception, e:
        user_json = {}
    process_common_user_info(result, user_json)
    process_order_info_by_user_id(user_id, user_json)
    #process_product_brand_ids_by_user_id(user_id, user_json)
    #dump_user_device(user_id, user_json) #用户设备ID add by ZHUWEIPING 2015-12-01
    #dump_user_device_token(user_id, user_json) #用户设备Token add by ZHUWEIPING  2015-12-07
    conn_es.index(user_json, const.index_name, const.type_user, user_id, bulk=True)
    pass


DUMP_USER_SQL = 'select id,user_name,email,nickname,phone,register_date, register_ip,' \
                'login_time,login_count,account_type,head_pic_url,create_time,update_time from user '


def dump_user_index(sql):
    cur = conn_db.cursor()
    log.log('开始导入用户表')
    count = cur.execute(sql)
    log.log('there has %s rows user record' % count)

    results = cur.fetchmany(count)
    index = 0
    for result in results:
        user_id = result[0]
        #create_time = result[20]
        try:
            dump_one_index(result)
            index += 1
            if index % conn_es.bulk_size == 0:
                log.log('已完成:'+'%.2f' % (index / count * 100) + "%")
                #search_config.write_dump_time('user', create_time)
            #    time.sleep(1)
        except Exception, e:
            log.log("dump user id: %d error" % user_id)
            print e
    conn_es.force_bulk()
    pass


def dump_user_cart_unpaid_products(from_time, dump_time):
    dump_user_cart_products(from_time, dump_time)
    dump_user_unpaid_products(from_time, dump_time)


def dump_user_cart_products(from_time, dump_time):
    sql = 'select distinct c.user_id from cart c inner join user u on c.user_id = u.id where c.user_id != 0 ' \
          'and c.status = 0 and u.status = 0 and c.update_time BETWEEN "%s" AND "%s"' % (from_time, dump_time)
    cur = conn_db.cursor()
    log.log('开始更新用户购物车商品')
    count = cur.execute(sql)
    log.log('there has %s rows user record' % count)
    results = cur.fetchmany(count)
    index = 0
    for result in results:
        user_id = result[0]
        cart_products = get_user_cart_products(user_id)
        try:
            user_json = conn_es.get(const.index_name, const.type_user, user_id)
            user_json["cartProducts"] = cart_products
            user_json["lastCartTime"] = get_last_cart_update_time_by_user_id(user_id)
            conn_es.update_by_function(user_json, const.index_name, const.type_user, user_id)
            index += 1
            if index % 200 == 0:
                log.log('已完成:'+'%.2f' % (index / count * 100) + "%")
                time.sleep(0.5)
        except Exception, e:
            log.log('error at dump_user_cart_products, from_time:%s end_time:%s, user id is %d' % (from_time, dump_time, user_id))
            print e


def dump_user_unpaid_products(from_time, dump_time):
    sql = 'select distinct user_id from order_info o inner join user u on o.user_id = u.id ' \
          'where o.pay_status = "PS_UNPAID" and o.order_status = "OS_CONFIRMED" and o.status = 0 and u.status = 0 ' \
          'and o.update_time between "%s" AND "%s"' % (from_time, dump_time)
    cur = conn_db.cursor()
    log.log('开始更新用户未付款商品')
    count = cur.execute(sql)
    log.log('there has %s rows user record' % count)
    results = cur.fetchmany(count)
    index = 0
    for result in results:
        user_id = result[0]
        unpaid_products = get_user_unpaid_products(user_id)
        try:
            user_json = conn_es.get(const.index_name, const.type_user, user_id)
            user_json["unpaidProducts"] = unpaid_products
            conn_es.update_by_function(user_json, const.index_name, const.type_user, user_id)
            index += 1
            if index % 200 == 0:
                log.log('已完成:'+'%.2f' % (index / count * 100) + "%")
                time.sleep(0.5)
        except Exception, e:
            log.log('error at dump_user_unpaid_products, from_time:%s end_time:%s, user id is %d'
                    % (from_time, dump_time, user_id))
            print e
    pass


def get_user_cart_products(user_id):
    cart_products = '';
    sql = 'select GROUP_CONCAT(DISTINCT ci.product_id SEPARATOR " "), c.user_id from cart_item ci left join cart c ' \
          'on ci.cart_id = c.id where c.user_id = %d and c.status = 0 and ci.status = 0 ' \
          'group by user_id limit 1' % user_id
    cur = conn_db.cursor()
    count = cur.execute(sql)
    results = cur.fetchmany(count)
    for result in results:
        cart_products = result[0]
    return cart_products
    pass


def get_user_unpaid_products(user_id):
    unpaid_products = '';
    sql = 'select GROUP_CONCAT(DISTINCT t.product_id SEPARATOR " ") from ' \
          '(select distinct i.product_id , h.user_id from order_info h left join order_item i ' \
          'on h.order_no = i.order_no where h.pay_status = "PS_UNPAID" and '\
          'h.order_status = "OS_CONFIRMED" and h.user_id = %d and h.status = 0 and i.status = 0) t ' \
          'group by t.user_id' % user_id
    cur = conn_db.cursor()
    count = cur.execute(sql)
    results = cur.fetchmany(count)
    for result in results:
        unpaid_products = result[0]
    return unpaid_products
    pass


def dump_user_device(user_id=None, user=None):
    cur = conn_db.cursor()
    sql = 'SELECT t.user_id, ' \
          'GROUP_CONCAT(t.device_id ORDER BY t.acc DESC, t.update_time DESC SEPARATOR " ") AS devices ' \
          'FROM user_device t ' \
          'WHERE t.auth = 1 ' \
          'AND t.status = 0 ' \
          '%s ' \
          'GROUP BY t.user_id'
    sql = user_id and sql % 'AND t.user_id = %s' % user_id or sql % ''
    count = cur.execute(sql)
    if user_id:
        result = cur.fetchone()
        if result:
            user['devices'] = result[1]
        return

    log.log('开始导入用户设备ID')
    log.log(sql)
    log.log('there has %s rows user record' % count)
    index = 0
    user_id = -1
    for result in cur.fetchall():
        index += 1
        try:
            user_id = result[0]
            user = conn_es.get(const.index_name, const.type_user, user_id)
            user['devices'] = result[1]
            conn_es.update(const.index_name, const.type_user, user_id, document=user, bulk=True)
        except Exception, e:
            log.log('error at dump_user_device, user id is %d\n%s' %(user_id, e))
        if index % conn_es.bulk_size == 0 or index % count == 0:
            log.log('已完成:'+'%.2f' % (index / count * 100) + "%")
    conn_es.force_bulk()
    cur.close()


def dump_test(uid):
    #es_db.es_init(user_mapping, const.index_name, const.type_user)

    #from_time = '1970-01-01'
    #dump_time = datetime.datetime.now()

    sql = DUMP_USER_SQL + 'where id = %d limit 10' % uid
    dump_user_index(sql)

    #search_config.write_dump_time('user', dump_time)
    pass

    
def dump_user_device_token(user_id=None, user=None):
    cur = conn_db.cursor(es_db.MySQLdb.cursors.DictCursor)
    sql = 'SELECT t.user_id, t.device_type as type, GROUP_CONCAT(t.device_token ORDER BY t.update_time ' \
          'DESC SEPARATOR " ") AS tokens FROM user_device_token t WHERE t.user_id > 0 '\
          'AND t.status = 0 %s GROUP BY t.user_id, t.device_type '
    sql = user_id and sql % 'AND t.user_id = %s' % user_id or sql % ''
    count = cur.execute(sql)
    if user_id:
        result = cur.fetchone()

    if result:
        result.pop('user_id')
        user['deviceToken'] = result

    return

    # dump All
    log.log('开始导入用户设备TOKEN')
    log.log(sql)
    log.log('there has %s rows user record' % count)
    index = 0
    user_id = -1
    for result in cur.fetchall():
        index += 1
    try:
        user_id = result.pop('user_id')
        user = conn_es.get(const.index_name, const.type_user, user_id)
        user['deviceToken'] = result
        conn_es.update(const.index_name, const.type_user, user_id, document=user, bulk=True)
    except Exception, e:
        log.log('error at dump_user_deviceToken, user id is %d\n%s' % (user_id, e))
    if index % conn_es.bulk_size == 0 or index % count == 0:
        log.log('已完成:'+'%.2f' % (index / count * 100) + "%")
    conn_es.force_bulk()
    cur.close()
    pass


def dump_user_profile(dumpall=None):
    nullValue = '\\N'
    def _put_value(jsonMap, key, value):
        if value == nullValue:
            return
        jsonMap[key] = value

    if dumpall:
        start_timestamp = 0
        end_timestamp = int(time.time())
    else:
        start_date = datetime.datetime.strptime(search_config.read_last_dump_time('user_profile'), "%Y-%m-%d %X")
        end_date = start_date + datetime.timedelta(days = 1)
        search_config.write_dump_time('user_profile', end_date)
        start_timestamp = int(time.mktime(start_date.timetuple()))
        end_timestamp = int(time.mktime(end_date.timetuple()))

    log.log('开始导入用户画像(属性)：品牌、类目、功效')
    sql = 'select user_id,' \
          'brand_id_order,' \
          'brand_id_cart,' \
          'brand_id_collect,' \
          'category_id_order,' \
          'category_id_cart,' \
          'category_id_collect,' \
          'efficacy_id_order,' \
          'efficacy_id_cart,' \
          'efficacy_id_collect ' \
          'from actionlog.user_profile ' \
          'where create_update_time >= %(start_timestamp)s ' \
          'and create_update_time < %(end_timestamp)s'
    cur = es_db.conn_db2.cursor()
    params = {
        'start_timestamp': start_timestamp,
        'end_timestamp': end_timestamp
    }
    log.log(sql)
    log.log('statement parameters:%s' % str(params))
    count = cur.execute(sql, params)
    log.log('there has %s rows user record' % count)
    index = 0
    for result in cur.fetchall():
        index += 1
        brands = {}
        categorys = {}
        efficacys = {}
        user_id = result[0]
        try:
            user = conn_es.get(const.index_name, const.type_user, user_id)
            _put_value(brands, 'byOrder', result[1])    # 订单
            _put_value(brands, 'byCart', result[2])     # 购物车
            _put_value(brands, 'byCollect', result[3])  # 收藏
            _put_value(categorys, 'byOrder', result[4])	# 订单
            _put_value(categorys, 'byCart', result[5])	# 购物车
            _put_value(categorys, 'byCollect', result[6])# 收藏
            _put_value(efficacys, 'byOrder', result[7])	# 订单
            _put_value(efficacys, 'byCart', result[8])	# 购物车
            _put_value(efficacys, 'byCollect', result[9])# 收藏
            user["PROFBrands"] = brands
            user["PROFCategorys"] = categorys
            user["PROFEfficacys"] = efficacys
            conn_es.update(const.index_name, const.type_user, user_id, document=user, bulk=True)
        except Exception, e:
            log.log('error at dump_user_profile, user id is %d\n%s' %(user_id, e))
        if index % conn_es.bulk_size == 0 or index % count == 0:
            log.log('已完成:'+'%.2f' % (index / count * 100) + "%")
    conn_es.force_bulk()
    cur.close()
    
    
# Main
if len(sys.argv) < 2:
    print "参数不对,请输入'all | increase | profile | device | deviceToken | test'"
    exit(1)

param = sys.argv[1]
if param == 'all':
    dump_all_index()
elif param == 'increase':
    dump_increment_index()
elif param == 'profile':
    arg2 = len(sys.argv) > 2 and sys.argv[2] or None
    #用户画像
    dump_user_profile(arg2)
elif param == 'device':
    #用户设备ID信息
    #dump_user_device()
    pass
elif param == 'deviceToken':
    #用户设备TOKEN
    #dump_user_device_token()
    pass
elif param == 'test':
    #es_db.es_init(user_mapping, const.index_name, const.type_user)
    dump_test(1)
    #dump_user_cart_unpaid_products('1970-01-01','2015-09-07')
    #delete_one_user(668340)
    #process_order_info_by_user_id(18, {})
else:
    print "参数不对,请输入'all | increase | profile | device | deviceToken | test'"