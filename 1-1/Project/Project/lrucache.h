#pragma once

#include <cassert>

#include <algorithm>
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
        : m_capacity(DEFAULT_CAP)
    {}

    explicit lru_cache(size_t capacity)
    {
        if (capacity == 0)
        {
            throw lru_exception();
        }
        // We do no other checks here, because if a user is dumb enough
        // to pass negative or too big value, he will die with OOM
        m_capacity = capacity;
    }

    size_t capacity() const
    {
        assert(m_capacity > 0, "Capacity must be non zero");
        return m_capacity;
    }

    size_t size() const
    {
        assert(m_used_order.size() <= m_capacity);
        assert(m_used_order.size() == m_cache.size());

        return m_used_order.size();
    }

    void insert(Key key, Value val)
    {
        // Check both list and map consistensy and invariant
        assert(size() <= m_capacity);

        auto it = m_cache.find(key);
        if (it != m_cache.end())
        {
            assert(std::count(m_used_order.begin(), m_used_order.end(), key), "Key must exist in list");

            m_used_order.remove(key);
            m_used_order.push_front(key);

            it->second.value = val;
            it->second.pos = m_used_order.begin();
        }
        else
        {
            assert(!std::count(m_used_order.begin(), m_used_order.end(), key), "Key must not exist in list");

            auto [insert_it, flag] = m_cache.emplace(key, val);
            assert(flag, "Key should have been inserted");

            m_used_order.push_front(key);

            insert_it->second.pos = m_used_order.begin();
        }

        if (m_used_order.size() > m_capacity)
        {
            auto to_delete = std::prev(m_used_order.end());
            m_cache.erase(*to_delete);
            m_used_order.pop_back();
            assert(m_used_order.size() == m_capacity);
        }

        assert(size() <= m_capacity);
        assert(m_used_order.front() == key);
    }

    const Value& get(const Key& key)
    {
        // Check both list and map consistensy and invariant
        assert(size() <= m_capacity);

        auto it = m_cache.find(key);
        if (it == m_cache.end())
        {
            throw lru_exception();
        }

        assert(std::count(m_used_order.begin(), m_used_order.end(), key), "Key must exist in list");

        m_used_order.remove(key);
        m_used_order.push_front(key);
        it->second.pos = m_used_order.begin();

        assert(size() <= m_capacity);
        assert(m_used_order.front() == key);
        return it->second.value;
    }

private:
    struct cache_node
    {
        using pos_t = typename std::list<Key>::iterator;
        cache_node(Value val)
            : value(val)
        {}

        Value value;
        pos_t pos;
    };

    size_t m_capacity;
    std::list<Key> m_used_order;
    std::unordered_map<Key, cache_node> m_cache;
};