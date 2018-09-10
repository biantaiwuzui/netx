#!/usr/bin/env python
#-*- coding:utf-8-*-
from __future__ import print_function
from __future__ import print_function
import time

__author__ = 'spark'


def log(log_str):
    time_format = '%Y-%m-%d %X'
    print('[%s] %s' % (time.strftime(time_format, time.localtime()), log_str))


def error(error_str):
    time_format = '%Y-%m-%d %X'
    print('[%s] %s' % (time.strftime(time_format, time.localtime()), error_str))