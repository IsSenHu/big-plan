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
-- 博客的数据
local blogData = ARGV[1]
-- 博客的内容
local content = ARGV[2]
-- 博客的ID
local blogId = ARGV[3]
-- 检查key是否存在
local checkExists = redis.call('EXISTS', blogKey)
if checkExists == 1 then
    local oldBlogData = redis.call('GET', blogKey)
    local oldBlogJson = cjson.decode(oldBlogData)
    local blogJson = cjson.decode(blogData)

    -- 分类计数处理
    local oldCategory = oldBlogJson['category']
    local category = blogJson['category']
    if oldCategory ~= category then
        local oldCategoryRank = redis.call("ZRANK", categoriesKey, oldCategory)
        if oldCategoryRank then
            local result = redis.call("ZINCRBY", categoriesKey, -1, oldCategory)
            if result == '0' then
                redis.call("ZREM", categoriesKey, oldCategory)
            end
        end

        local categoryRank = redis.call("ZRANK", categoriesKey, category)
        if categoryRank then
            redis.call("ZINCRBY", categoriesKey, 1, category)
        else
            redis.call("ZADD", categoriesKey, 1, category)
        end
    end

    -- 标签计数处理
    local oldTags = oldBlogJson['tags']
    for _, v in pairs(oldTags) do
        local tagRank = redis.call("ZRANK", tagsKey, v)
        if tagRank then
            local result = redis.call("ZINCRBY", tagsKey, -1, v)
            if result == '0' then
                redis.call("ZREM", tagsKey, v)
            end
        end
    end

    local tags = blogJson['tags']
    for _, v in pairs(tags) do
        local tagRank = redis.call("ZRANK", tagsKey, v)
        if tagRank then
            redis.call("ZINCRBY", tagsKey, 1, v)
        else
            redis.call("ZADD", tagsKey, 1, v)
        end
    end

    -- 保存博客的数据
    redis.call('SET', blogKey, blogData)
    -- 保存博客的内容
    redis.call('SET', contentKey, content)
    -- 保存博客的ID
    redis.call('ZADD', idsKey, 0, blogId)
    return '0'
end
return '-1'
