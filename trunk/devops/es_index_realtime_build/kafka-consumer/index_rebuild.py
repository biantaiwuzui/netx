#!/usr/bin/env python
# -*-coding:utf-8-*-
import json
import threading
import time

from multiprocessing import Queue

import re

from kafka import KafkaConsumer

from common.const import action_delete, index_demand_name, type_demand, action_update, action_index, index_skill_name, \
    type_skill, index_meeting_name, type_meeting, type_wish, index_wish_name, key_table_name, key_time, \
    index_worth_name, type_worth
from index_create.es_create_indices import create_indices
from index_rebuild.demand_rebuild_body import demand_rebuild
from index_rebuild.meeting_rebuild_body import meeting_rebuild
from index_rebuild.skill_rebuild_body import skill_rebuild
from index_rebuild.wish_rebuild_body import wish_rebuild
from utils import es_db, log
from utils.search_config import get_kafka_config



#创建index
conn_es = es_db.get_conn_es();
create_indices(conn_es)

def delete_doc(index, type, body):
    #被删除情况
    if(body == None):
        return
    try:
        conn_es.delete(index=index, doc_type=type, id=body['pkId'])
        log.log('delete '+ type + ' ' + body['pkId'] + 'succeed!')
    except Exception as e:
        log.error('delete '+ type + ' ' + body['pkId'] + 'failure....................')
        print(e)


def update_doc(index, type, body):
    #被删除情况
    if (body == None):
        return
    update_body = {}
    update_body['doc']=body
    try:
        if (type == type_worth):
            id = body['worthType']+ '-' + body['id']
            conn_es.update(index=index, doc_type=type, id=id, body=update_body)
        else:
            conn_es.update(index = index, doc_type=type, id=body['id'], body=update_body)
        log.log('update ' + type + ' ' + body['id'] + 'succeed!')
    except Exception as e:
        log.error('update ' + type + ' ' + body['id'] + 'failure....................')
        print (e)


def index_doc(index, type, body):
    #被删除情况
    if (body == None):
        return
    try:
        if (type == type_worth):
            conn_es.index(index=index, doc_type=type, id=body['worthType']+ '-' + body['id'], body=body)
        else:
            conn_es.index(index=index, doc_type=type, id=body['id'], body=body)
        log.log('index ' + type + ' ' + body['id'] + 'succeed!')
    except Exception as e:
        log.error('index ' + type + ' ' + body['id'] + 'failure.....................')
        print(e)


def rebuild_demand_body(json_value):
    id = json_value['pkId']
    # 获取rebuild json
    json_body, worth_body = demand_rebuild(id, False)
    return json_body, worth_body


def rebuild_demand(action, json_value):
    json_body, worth_body = rebuild_demand_body(json_value)
    if(action == action_delete):
        delete_doc(index_demand_name, type_demand, json_value)
        json_value['pkId'] = 'Demand-'+json_value['pkId']
        delete_doc(index_worth_name, type_worth, json_value)
    elif(action == action_update):
        update_doc(index_demand_name, type_demand, json_body)
        update_doc(index_worth_name, type_worth, worth_body)
    elif(action == action_index):
        index_doc(index_demand_name, type_demand, json_body)
        index_doc(index_worth_name, type_worth, worth_body)
    else:
        pass


def rebuild_skill_body(json_value):
    id = json_value['pkId']
    # 获取rebuild json
    json_body, worth_body = skill_rebuild(id, False)
    return json_body, worth_body


def rebuild_skill(action, json_value):
    json_body, worth_body = rebuild_skill_body(json_value)
    if (action == action_delete):
        delete_doc(index_skill_name, type_skill, json_value)
        json_value['pkId'] = 'Skill-' + json_value['pkId']
        delete_doc(index_worth_name, type_worth, json_value)
    elif (action == action_update):
        update_doc(index_skill_name, type_skill, json_body)
        update_doc(index_worth_name, type_worth, worth_body)
    elif (action == action_index):
        index_doc(index_skill_name, type_skill, json_body)
        index_doc(index_worth_name, type_worth, worth_body)
    else:
        pass


def rebuild_meeting_body(json_value):
    id = json_value['pkId']
    # 获取rebuild json
    json_body, worth_json = meeting_rebuild(id, False)
    return json_body, worth_json


def rebuild_meeting(action, json_value):
    json_body, worth_json = rebuild_meeting_body(json_value)
    if (action == action_delete):
        delete_doc(index_meeting_name, type_meeting, json_value)
        json_value['pkId'] = 'Meeting-' + json_value['pkId']
        delete_doc(index_worth_name, type_worth, json_value)
    elif (action == action_update):
        update_doc(index_meeting_name, type_meeting, json_body)
        update_doc(index_worth_name, type_worth, worth_json)
    elif (action == action_index):
        index_doc(index_meeting_name, type_meeting, json_body)
        index_doc(index_worth_name, type_worth, worth_json)
    else:
        pass


def rebuild_wish_body(json_value):
    id = json_value['pkId']
    json_body, worth_body = wish_rebuild(id, True)
    return json_body, worth_body


def rebuild_wish(action, json_value):
    json_body, worth_body = rebuild_wish_body(json_value)
    if (action == action_delete):
        delete_doc(index_wish_name, type_wish, json_value)
        json_value['pkId'] = 'Wish-' + json_value['pkId']
        delete_doc(index_worth_name, type_worth, json_value)
    elif (action == action_update):
        update_doc(index_wish_name, type_wish, json_body)
        update_doc(index_worth_name, type_worth, worth_body)
    elif (action == action_index):
        index_doc(index_wish_name, type_wish, json_body)
        index_doc(index_worth_name, type_worth, worth_body)
    else:
        pass


def rebuild(json_key, json_value):
    if(re.match('^demand.*', json_key[key_table_name], 0)):
        rebuild_demand(json_key['eventType'], json_value)
    elif(re.match('^skill.*', json_key[key_table_name], 0)):
        rebuild_skill(json_key['eventType'], json_value)
    elif (re.match('^meeting.*', json_key[key_table_name], 0)):
        rebuild_meeting(json_key['eventType'], json_value)
    elif (re.match('^wish.*', json_key[key_table_name], 0)):
        rebuild_wish(json_key['eventType'], json_value)
    else:
        #增加其他表格
        pass


def index_all_skill(conn_db):
    m_sql = 'SELECT s.id from skill as s ' \
            'WHERE s.deleted = 0'

    cur = conn_db.cursor()
    count = cur.execute(m_sql)
    results = cur.fetchmany(count)

    for result in results:
        skillId = result[0]
        json_body, worth_body = skill_rebuild(skillId, False)
        index_doc(index_skill_name, type_skill, json_body)
        index_doc(index_worth_name, type_worth, worth_body)



def index_all_demand(conn_db):
    demand_sql = 'select d.id from demand as d ' \
                 'WHERE d.deleted = 0'
    cur = conn_db.cursor()
    count = cur.execute(demand_sql)
    results = cur.fetchmany(count)
    for result in results:
        demandId = result[0]
        json_body, worth_body = demand_rebuild(demandId, False)
        index_doc(index_demand_name, type_demand, json_body)
        index_doc(index_worth_name, type_worth, worth_body)


def index_all_meeting(conn_db):
    m_sql = 'SELECT m.id from meeting as m ' \
            'WHERE m.deleted = 0'

    cur = conn_db.cursor()
    count = cur.execute(m_sql)
    results = cur.fetchmany(count)

    for result in results:
        meetingId = result[0]
        json_body, worth_body = meeting_rebuild(meetingId, False)
        index_doc(index_meeting_name, type_meeting, json_body)
        index_doc(index_worth_name, type_worth, worth_body)

def index_all_wish(conn_db):
    m_sql = 'SELECT m.id from wish as m ' \
            'WHERE m.deleted = 0'

    cur = conn_db.cursor()
    count = cur.execute(m_sql)
    results = cur.fetchmany(count)

    for result in results:
        wishId = result[0]
        json_body, worth_body = wish_rebuild(wishId, False)
        index_doc(index_wish_name, type_wish, json_body)
        index_doc(index_worth_name, type_worth, worth_body)

conn_db = es_db.get_mysql_connector('netx')  # 上线
index_all_demand(conn_db)
index_all_meeting(conn_db)
index_all_skill(conn_db)
index_all_wish(conn_db)
conn_db.close()
log.log('index all indies succeed!!!!!!')


class KafkaMessage:
    def __init__(self,queue):
        self.queue = queue
    def add(self,message):
        self.queue.put(message)
    def sub(self):
        if (not self.queue.empty()):
            return self.queue.get()
    def empty(self):
        return self.queue.empty()

class MessageProducer(threading.Thread):
    def __init__(self,kafka_consumer,condition,goods,sleeptime = 0.5):#sleeptime=0.5
        threading.Thread.__init__(self)
        self.cond = condition
        self.goods = goods
        self.sleeptime = sleeptime
        self.kafka_consumer = kafka_consumer

    def run(self):
        cond = self.cond
        goods = self.goods
        consumer = self.kafka_consumer
        i = 0
        before = time.time()
        for message in consumer:
            cond.acquire()
            json_value = json.loads(message.value)
            print( 'add one')
            goods.add(json_value)
            i += 1
            now = time.time()
            real_time = now-before
            if(i == 5 or real_time > 5):
                i = 0
                before = now
                # 唤醒其他线程
                cond.notifyAll()
                cond.wait()
                time.sleep(self.sleeptime)
            # 释放信号
            cond.release()


class MessageConsumer(threading.Thread):
    def __init__(self,condition,goods,sleeptime = 1):
        threading.Thread.__init__(self)
        self.cond = condition
        self.goods = goods
        self.sleeptime = sleeptime

    def run(self):
        cond = self.cond
        goods = self.goods
        i = 0l
        sum_time = 0l
        while True:
            time.sleep(self.sleeptime)
            cond.acquire()
            while goods.empty():
                cond.notifyAll()
                cond.wait()
            send_message = []
            while not goods.empty():
                kafka_message = goods.sub()
                print ('pop one')
                if(not kafka_message['pkId'] in send_message):
                    before = 1
                    try:
                        timeArray = time.strptime(kafka_message[key_time], "%Y-%m-%d %H:%M:%S")
                        before = float(time.mktime(timeArray))
                    except Exception as e:
                        log.log(e.message)
                    send_message.append(kafka_message['pkId'])
                    rebuild(kafka_message, kafka_message)
                    now = time.time()
                    real_time = (now - before) / 2
                    # 过滤较大的时间
                    if (real_time > 5):
                        continue
                    i += 1
                    sum_time += real_time
                    avg_real_time = sum_time / i
                    log.log("the %d time operate avg_time is %f" % (i, avg_real_time))
            cond.release()


topic, bootstrap_server = get_kafka_config()
consumer = KafkaConsumer(topic,
                         bootstrap_servers=bootstrap_server)
q = Queue()
g = KafkaMessage(queue=q)
c = threading.Condition()

pro = MessageProducer(consumer,c,g)
pro.start()

con = MessageConsumer(c,g)
con.start()

