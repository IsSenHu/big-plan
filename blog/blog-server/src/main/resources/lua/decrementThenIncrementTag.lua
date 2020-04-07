local key = KEYS[1]
local del = ARGV[1]
local add = ARGV[2]
local tagTable = {}

string.gsub(del, '[^,]+', function(w)
    table.insert(tagTable, w)
end)

for _, v in pairs(tagTable) do
    local rank = redis.call("ZRANK", key, v)
    if rank then
        local result = redis.call("ZINCRBY", key, -1, v)
        if result == '0' then
            redis.call("ZREM", key, v)
        end
    end
end

tagTable = {};
string.gsub(add, '[^,]+', function(w)
    table.insert(tagTable, w)
end)

for _, v in pairs(tagTable) do
    local rank = redis.call("ZRANK", key, v)
    if rank then
        redis.call("ZINCRBY", key, 1, v)
    else
        redis.call("ZADD", key, 1, v)
    end
end