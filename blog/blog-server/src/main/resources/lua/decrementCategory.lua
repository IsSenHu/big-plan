local key = KEYS[1]
local category = ARGV[1]

local rank = redis.call("ZRANK", key, category)
if rank then
    local result = redis.call("ZINCRBY", key, -1, category)
    if result == '0' then
        redis.call("ZREM", key, category)
    end
end