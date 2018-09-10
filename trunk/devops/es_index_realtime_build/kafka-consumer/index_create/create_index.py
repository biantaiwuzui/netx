#!/usr/bin/env python
#-*- coding:utf-8-*-


#设置输入流的编码格式
import sys
from common.mysql_es_datatype import *

from common.const import index_test_name
from utils import es_db

reload(sys)
sys.setdefaultencoding( "utf-8" )

conn_es = es_db.get_conn_es();

create_index_body = {
  'settings': {
    # just one shard, no replicas for testing
    'number_of_shards': 1,
    'number_of_replicas': 5
  },
    'mappings': {
        'country': {
          'properties': {
              'id':mysql_varchar,
              'countryname' : mysql_varchar,
              'countrycode' : mysql_varchar
          }
        }
    }
}
if conn_es.indices.exists(index=index_test_name):
    conn_es.indices.delete(index=index_test_name, ignore=400)
conn_es.indices.create(index=index_test_name, body= create_index_body)
