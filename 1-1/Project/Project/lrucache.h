#pragma once

#include <cassert>

#include <unordered_map>
#include <list>

template<class Key, class Value>
class lru_cache
{
    const size_t DEFAULT_CAP = 10U;
public:
    lru_cache()
        : capacity(DEFAULT_CAP)
    {}

    explicit lru_cache(size_t cap) :
        capacity(cap)
    {
        // We do no checks here, because if a user is dumb enough
        // to pass negative or too big value, he will die with OOM
    }

    size_t get_capacity() const
    {
        return capacity;
    }
private:
    size_t capacity;
};