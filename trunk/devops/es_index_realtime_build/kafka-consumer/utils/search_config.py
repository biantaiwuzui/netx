#!/usr/bin/env python
#-*- coding:utf-8-*-

from utils import config

import os

CUR_PATH = os.path.split(os.path.realpath(__file__))[0]

INI_SECTION = 'info'

ES_CONFIG_NAME = 'elasticsearch'

def get_config_file(key_name):
    file_path = CUR_PATH + '/../conf/' + key_name +'.ini'
    print file_path
    return file_path

def get_mysql_config(biz_type):
    config_file = get_config_file(ES_CONFIG_NAME)
    init_section = "mysql_" + biz_type


    host = config.read_config(config_file, init_section, 'host')
    port = config.read_config(config_file, init_section, 'port')
    db = config.read_config(config_file, init_section, 'db')
    user = config.read_config(config_file, init_section, 'user')
    passwd = config.read_config(config_file, init_section, 'passwd')

    return host, port, db, user, passwd
    pass

def get_es_config():
    config_file = get_config_file(ES_CONFIG_NAME)
    init_section = 'es'

    host_port = config.read_config(config_file, init_section, 'host_port')

    return host_port
    pass

def write_dump_time(key_name, dump_time):
    config_file = get_config_file(key_name)
    config.write_config(config_file, INI_SECTION, key_name,
                        dump_time.strftime('%Y-%m-%d %X'))


def read_last_dump_time(key_name):
    config_file = get_config_file(key_name)
    return config.read_config(config_file, INI_SECTION, key_name)

def get_redis_config():
    config_file = get_config_file(ES_CONFIG_NAME)
    init_section = 'redis'
    host = config.read_config(config_file, init_section, 'host')
    port = config.read_config(config_file, init_section, 'port')
    db = config.read_config(config_file, init_section, 'db')
    password = config.read_config(config_file, init_section, 'password')
    return host, password, port, db
    pass

def get_kafka_config():
    config_file = get_config_file(ES_CONFIG_NAME)
    init_section = 'kafka'
    topic = config.read_config(config_file, init_section, 'topic')
    bootstrap_server = config.read_config(config_file, init_section, 'bootstrap_server')
    return topic, bootstrap_server
    pass
