if (redis.call('exists', KEYS[1]) == 1) then
    local stock = tonumber(redis.call('hget', KEYS[1], KEYS[2]));
    if stock <= 0 then
        return -1;
    end
    redis.call('HINCRBY', KEYS[1], KEYS[2], -1);
    return stock - 1;
end
