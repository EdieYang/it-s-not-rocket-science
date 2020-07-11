##  String 命令

SET key value

GET key

SETNX : set if not exit 

SETEX ：set with expire

SETRANGE ：SETRANGE key offset value 

* 在offset后覆盖value，如果offset 大于key的最大长度，用零字节填充

```
Basic usage:

redis> SET key1 "Hello World"
"OK"
redis> SETRANGE key1 6 "Redis"
(integer) 11
redis> GET key1
"Hello Redis"
redis> 
Example of zero padding:

redis> SETRANGE key2 6 "Redis"
(integer) 11
redis> GET key2
"\u0000\u0000\u0000\u0000\u0000\u0000Redis"
redis> 
```

STRLEN key : return length of the string value stored at key.

APPEND : 插入key的末尾，如果不存在此key ，set一个key

GETSET：SET key并返回key的原值

INCR : key的值加一 并返回总和

INCRBY : key的值加步长 并返回总和 key不存在先设为0

INCRBYFLOAT：key的值加浮点数步长 并返回总和 key不存在先设为0

```
redis> SET mykey 10.50
"OK"
redis> INCRBYFLOAT mykey 0.1
"10.6"
redis> INCRBYFLOAT mykey -5
"5.6"
redis> SET mykey 5.0e3
"OK"
redis> INCRBYFLOAT mykey 2.0e2
"5200"
redis> 
```

