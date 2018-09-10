#!/usr/bin/env python
#-*- coding:utf-8-*-

import sys
import ConfigParser


class Config:
    def __init__(self, path):
        self.path = path
        self.cf = ConfigParser.ConfigParser()
        self.cf.read(self.path)

    def get(self, field, key):
        try:
            result = self.cf.get(field, key)
        except IOError:
            result = ""
        return result

    def set(self, field, key, value):
        try:
            self.cf.set(field, key, value)
            self.write(open(self.path, 'w'))
        except IOError:
            return False

        return True


def read_config(config_file_path, field, key):
    cf = ConfigParser.ConfigParser()
    result = ''
    try:
        cf.read(config_file_path)
        result = cf.get(field, key)
    except Exception, e:
        print "error,ini:%s, field:%s, key:%s" % (config_file_path, field, key)
        print(e)
        pass

    return result


def write_config(config_file_path, field, key, value):
    cf = ConfigParser.ConfigParser()
    try:
        cf.read(config_file_path)
        if not cf.has_section(field):
            cf.add_section(field);
        cf.set(field, key, value)
        cf.write(open(config_file_path, 'w'))
    except IOError as e:
        print "error,ini:%s, field:%s, key:%s, value: %s" % (config_file_path, field, key, value)
        print("I/O error({0}):{1}".format(e.errno, e.strerror))
        raise

    return True

if __name__ == "__main__":
    if len(sys.argv) < 4:
        sys.exit(1)

    config_file_path = sys.argv[1]
    field = sys.argv[2]
    key = sys.argv[3]
    if len(sys.argv) == 4:
        print read_config(config_file_path, field, key)
    else:
        value = sys.argv[4]
        print value
        write_config(config_file_path, field, key, value)
