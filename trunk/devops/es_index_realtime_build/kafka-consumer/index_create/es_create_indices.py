#!/usr/bin/env python
#-*- coding:utf-8-*-

from es_create_demand import create_demand_index
from es_create_meeting import create_meeting_index
from es_create_skill import create_skill_index
from es_create_wish import create_wish_index
from index_create.es_create_worth import create_worth_index


def create_indices(conn_es):
    create_demand_index(conn_es)
    create_meeting_index(conn_es)
    create_skill_index(conn_es)
    create_wish_index(conn_es)
    create_worth_index(conn_es)

# create_indices()


