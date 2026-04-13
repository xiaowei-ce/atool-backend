local lock = KEYS[1];
local thread_sign = ARGV[1];

if redis.call('get',lock) == thread_sign then
    return redis.call('del',lock);
end

redis.call('expire','lock',10);
return 0;