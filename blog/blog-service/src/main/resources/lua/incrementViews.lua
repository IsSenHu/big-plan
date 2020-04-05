local key = KEYS[1]
local id = ARGV[1]

local rank = redis.call("ZRANK", key, id)
if rank then
    redis.call("ZINCRBY", key, 1, id)
else
    redis.call("ZADD", key, 1, id)
end

