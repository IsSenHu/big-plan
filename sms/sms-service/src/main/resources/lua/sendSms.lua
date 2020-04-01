local phoneNumber = ARGV[1]
local templateCode = ARGV[2]
local signName = ARGV[3]
local effectiveTime = ARGV[4]
local intervals = ARGV[5]
local type = ARGV[6]
local verifyCode = ARGV[7]

local colon = ":"
local keyForInIntervals = "inIntervals:"
local keyForSaveVerifyCodes = "verifyCode:"

-- 先检查能否给这个手机号发这个签名和模版的短信
local thisPhoneKeyForInIntervals = keyForInIntervals .. signName .. colon .. templateCode .. colon .. phoneNumber
local inside = redis.call("GET", thisPhoneKeyForInIntervals)
if (inside) then
    -- 给该手机号发送短信过于频繁
    return "1"
end

-- 记录给该手机号发这个签名和模版的短信并设置请求间隔
redis.call("SETEX", thisPhoneKeyForInIntervals, intervals, "1")

-- 如果是验证码则要保存验证码
if (type == "0") then
    local thisPhoneKeyForSaveVerifyCodes = keyForSaveVerifyCodes .. signName .. colon .. templateCode .. colon .. phoneNumber
    redis.call("SETEX", thisPhoneKeyForSaveVerifyCodes, effectiveTime, verifyCode)
end

return "0"
