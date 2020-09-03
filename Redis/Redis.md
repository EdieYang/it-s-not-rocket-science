# 常用命令

EXISTS key #判断当前的key是否存在

move key slot #将当前的key移动到指定库中

keys * #查看所有key

ttl key #查看当前key的剩余时间

type key #查看当前key的类型

## String 命令

SET key value

GET key

EXISTS key # 判断某个key是否存在

SETNX : set if not exit  如果key存在，创建失败

SETEX ：set with expire 

SETRANGE ：SETRANGE key offset value 

* 在offset后覆盖value，如果offset 大于key的最大长度，用零字节填充

```bash
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

```bash
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

GETRANGE key start end #截取字符串 [start  , end ] 两头都包括

MSET key value key value key value 

MGET key1 key2 key3 ... 

MSETNX k1 v1 k2 v2 k3 v3 ... #msetnx是一个原子操作，要么一起成功，要么一起失败。

#对象

SET user:1 {name:zhangsan,age:3} #设置一个user:1对象 值为json字符串来保存一个对象

GETSET #先get 再set ， 返回原来的值，并设置新的值。

String类型的使用场景

* 计数器
* 统计多单位的数量
* 粉丝数
* 对象缓存存储



## List 命令

LPUSH #插入到列表头部（左）

LRANGE key start end  # 获取list的值

RPUSH key value1 value2 .. #将一个值或多个值插入列表尾部（右）

---

LPOP key #移除list的第一个元素

RPOP key #移除list的最后一个元素

LINDEX key offset  #通过下标获取list中的某一个值

LLEN key #获取列表长度

LREM key number value #移除列表中指定个数的value，精确匹配

LTRIM key start end #通过下标截取指定的长度。

RPOPLPUSH #移除列表的最后一个元素，将他移动到新的列表中。

LSET key index element #将列表中指定下标的值替换为另一个值，更新操作。如果不存在列表报错，不存在下标，报错。

LINSERT key [before|after]  **pivot**  element #将某个具体的值插入到列表中某个元素值的前面或后面。pivot为插入中心的value

## Set命令

SADD key elem #set集合中添加元素

SMEMBERS key #查看指定set的所有值

SISMEMBER key member #判断某个值是否在集合中

SCARD key #获取set集合中元素的个数

SREM key member #移除set集合中的指定元素

SRANDMEMBER key [count] #随机抽取指定个数个元素

SPOP key [count] #随机删除set集合中指定个数元素

SMOVE source destination member #将source集合中的元素移到des集合中

SDIFF key1 key2 .. #以key1 集合为主，跟别的集合做比较，查看差集元素

SINTER key1 key2 .. #交集

SUNION key1 key2 ... #并集

使用场景：

微博关注的人放一个set ， 粉丝放一个set ， 利用交集 ，并集 差集 查看 共同关注，共同爱好，推荐好友等功能

