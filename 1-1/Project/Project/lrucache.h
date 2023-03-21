#pragma once

#include <cassert>

#include <unordered_map>
#include <list>

template<class Key, class Value>
class lru_cache
{
    const size_t DEFAULT_CAP = 10U;
public:
    struct lru_exception : std::exception
    {
        const char* what()
        { return "LRU Cache error"; }
    };

    lru_cache()
        : capacity(DEFAULT_CAP)
    {}

    explicit lru_cache(size_t cap) :
        capacity(cap)
    {
        if (capacity == 0)
        {
            throw new lru_exception;
        }
        // We do no other checks here, because if a user is dumb enough
        // to pass negative or too big value, he will die with OOM
    }

    size_t get_capacity() const
    {
        assert(capacity > 0, "Capacity must be non zero");
        return capacity;
    }
private:
    size_t capacity;
};