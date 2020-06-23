-- 订单号
local orderBn = KEYS[1]
local getResult = redis.call('get', orderBn)
return getResult;