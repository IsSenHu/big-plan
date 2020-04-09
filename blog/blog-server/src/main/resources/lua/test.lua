local key = KEYS[1]
local data = ARGV[1]
redis.call("set", key, data)