local key = KEYS[1]
local del = ARGV[1]
local add = ARGV[2]

local rank = redis.call("ZRANK", key, del)
if rank then
    local result = redis.call("ZINCRBY", key, -1, del)
    if result == '0' then
        redis.call("ZREM", key, del)
    end
end

rank = redis.call("ZRANK", key, add)
if rank then
    redis.call("ZINCRBY", key, 1, add)
else
    redis.call("ZADD", key, 1, add)
end