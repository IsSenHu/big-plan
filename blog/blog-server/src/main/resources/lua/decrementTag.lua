local key = KEYS[1]
local tags = ARGV[1]
local tagTable = {}
string.gsub(tags, '[^,]+', function(w)
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