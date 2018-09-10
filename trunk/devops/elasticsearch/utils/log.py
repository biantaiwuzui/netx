#!/usr/bin/env python
#-*- coding:utf-8-*-

__author__ = 'spark'

import time


def log(log_str):
    time_format = '%Y-%m-%d %X'
    print '[%s] %s' % (time.strftime(time_format, time.localtime()), log_str)