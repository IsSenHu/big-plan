-- 订单号
local orderBn = KEYS[1]
return redis.call('SETNX', orderBn, '0');