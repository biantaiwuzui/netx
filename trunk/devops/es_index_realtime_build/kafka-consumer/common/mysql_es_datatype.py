#-*- coding:utf-8-*-
__author__ = 'Yawn'

# 一位
mysql_bit={'type': 'byte'}

### 1字节

# 小整数
mysql_tinyint = {'type': 'byte'}

### 2字节
# 大整数
mysql_smallint = {'type': 'short'}

### 3字节
# 大整数值
mysql_mediumint = {'type': 'integer'}

### 4字节
# 大整数值
mysql_int = {'type': 'integer'}
# 单精度浮点
mysql_float = {'type': 'float'}


### 8字节
# 极大整数值
mysql_bigint = {'type': 'long'}
# 双精度浮点
mysql_double = {'type': 'double'}
# 小数值
mysql_decimal = {'type': 'double'}



### 日期类型
mysql_datetime = {'type': 'date',"format": "yyyy-MM-dd HH:mm:ss||yyyy-MM-dd||epoch_millis","ignore_malformed" : True, "null_value": "NULL"}
mysql_time = {'type': 'date'}
mysql_year = {'type': 'date'}
mysql_time_stamp = {'type': 'date'}



### 字符串类型
# 变长字符串
mysql_varchar = {'type': 'text'}
# 定长字符串
mysql_char = {'type': 'text'}
# 不超过255字节的二进制字符串
mysql_tinyblob = {'type': 'text'}
# 短文本
mysql_tinytest = {'type': 'text'}
# 二进制形式长文本
mysql_blob = {'type': 'text'}
# 长文本
mysql_text = {'type': 'text'}
# 二进制形式的中等长度文本数据
mysql_mediumblob = {'type': 'text'}
# 中等长度文本数据
mysql_mediumtest = {'type': 'text'}
# 二进制形式的极大文本数据
mysql_longblob = {'type': 'text'}
# 极大文本数据
mysql_longtext = {'type': 'text'}

mysql_lon_lan = {'type': 'geo_point'}