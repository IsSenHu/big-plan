local key = KEYS[1]
local category = ARGV[1]

local rank = redis.call("ZRANK", key, category)
if rank then
    redis.call("ZINCRBY", key, 1, category)
else
    redis.call("ZADD", key, 1, category)
end

