local bloomName = KEYS[1];
local value = KEYS[2];
local result_1 = redis.call('BF.EXISTS', bloomName, value);
return result_1;