-- 博客的key
local blogKey = KEYS[1]
-- 博客内容的key
local contentKey = KEYS[2]
-- ID集合的key
local idsKey = KEYS[3]
-- 分类ZSET的key
local categoriesKey = KEYS[4]
-- 标签ZSET的key
local tagsKey = KEYS[5]
-- 阅读数的key
local viewsKey = KEYS[6]
-- 博客的ID
local blogId = ARGV[1]
local checkExists = redis.call('EXISTS', blogKey)
if (checkExists == 1) then
    local blogData = redis.call('GET', blogKey)
    local blogJson = cjson.decode(blogData)
    -- 累减相应的分类
    local category = blogJson['category']
    local categoryRank = redis.call("ZRANK", categoriesKey, category)
    if categoryRank then
        local result = redis.call("ZINCRBY", categoriesKey, -1, category)
        if result == '0' then
            redis.call("ZREM", categoriesKey, category)
        end
    end
    -- 累减相应的标签
    local tags = blogJson['tags']
    for _, v in pairs(tags) do
        local tagRank = redis.call("ZRANK", tagsKey, v)
        if tagRank then
            local result = redis.call("ZINCRBY", tagsKey, -1, v)
            if result == '0' then
                redis.call("ZREM", tagsKey, v)
            end
        end
    end
    -- 删除阅读数
    redis.call('ZREM', viewsKey, blogId)
    -- 删除对应数据
    redis.call('DEL', blogKey)
    redis.call('DEL', contentKey)
    redis.call('ZREM', idsKey, blogId)
    return "0"
end
return "-1"