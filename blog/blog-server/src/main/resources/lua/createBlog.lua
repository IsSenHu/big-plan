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
-- 博客的数据
local blogData = ARGV[1]
-- 博客的内容
local content = ARGV[2]
-- 博客的ID
local blogId = ARGV[3]
-- 检查key是否存在
local checkExists = redis.call('EXISTS', blogKey)
if checkExists == 1 then
    return '-1'
end
local blogJson = cjson.decode(blogData)
-- 累加相应的分类
local category = blogJson['category']
local categoryRank = redis.call("ZRANK", categoriesKey, category)
if categoryRank then
    redis.call("ZINCRBY", categoriesKey, 1, category)
else
    redis.call("ZADD", categoriesKey, 1, category)
end
-- 累加相应的标签
local tags = blogJson['tags']
for _, v in pairs(tags) do
    local tagRank = redis.call("ZRANK", tagsKey, v)
    if tagRank then
        redis.call("ZINCRBY", tagsKey, 1, v)
    else
        redis.call("ZADD", tagsKey, 1, v)
    end
end
-- 初始化阅读数
redis.call('ZADD', viewsKey, 0, blogId)
-- 保存博客的数据
redis.call('SET', blogKey, blogData)
-- 保存博客的内容
redis.call('SET', contentKey, content)
-- 保存博客的ID
redis.call('ZADD', idsKey, 0, blogId)
return '0'
