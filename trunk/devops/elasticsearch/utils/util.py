__author__ = 'CloudZou'

import datetime
import time


def get_time_long(date):
    if date is not None and date > datetime.datetime(1970, 1, 1):
        timestamp = time.mktime(date.timetuple())
        return timestamp
    else:
        return 0
