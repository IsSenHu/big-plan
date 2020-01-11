-- 检查手机号是否可用/已被注册
local keyForPhones = "phones"
local keyPrefixForTempPhone = "tempPhone:"
local keyPrefixForSmsCode = "smsCode:"
local newPhone = ARGV[1]
local lastPhone = ARGV[2]
local smsCode = ARGV[3]

if newPhone then
    local existed = redis.call("GET", keyPrefixForTempPhone .. newPhone)
    if (existed == newPhone) then
        -- 每60秒只能获取一次短信验证码
        return 1
    end
    local isMember = redis.call("SISMEMBER", keyForPhones, newPhone)
    if (isMember == 1) then
        -- 该手机号已被注册
        return 2
    end
    -- 可以使用这个手机号 如果之前存在 lastPhone 则先删除
    if lastPhone then
        redis.call("DEL", keyPrefixForTempPhone .. lastPhone)
        redis.call("DEL", keyPrefixForSmsCode .. lastPhone)
    end
    -- 设置临时手机号 过期时间61秒 意思就是重新获取短信验证码的间隔为60秒
    redis.call("SETEX", keyPrefixForTempPhone .. newPhone, 60, newPhone)
    -- 保存短信验证码 验证码有效时间5分钟
    redis.call("SETEX", keyPrefixForSmsCode .. newPhone, 300, smsCode)
    -- 成功
    return 0;
end