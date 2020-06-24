-- 订单号
local orderBn = KEYS[1]
local getResult = redis.call('get', orderBn)
if getResult then
    return 'false'
else
    redis.call('set', orderBn, '1')
    return 'true'
end