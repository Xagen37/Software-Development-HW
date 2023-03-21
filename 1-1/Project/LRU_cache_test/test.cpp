#include "pch.h"

TEST(CreationTest, SimpleCreate)
{
    using test_t = lru_cache<int, int>;

    test_t cache1;
    EXPECT_EQ(cache1.capacity(), 10);

    test_t cache2(5);
    EXPECT_EQ(cache2.capacity(), 5);

    EXPECT_ANY_THROW(test_t cache3(0));
}

TEST(InsertTest, SimpleInsert)
{
    using test_t = lru_cache<int, char>;

    test_t cache(2);
    ASSERT_EQ(cache.capacity(), 2);
    ASSERT_EQ(cache.size(), 0);

    cache.insert(1, 'a');
    ASSERT_EQ(cache.capacity(), 2);
    ASSERT_EQ(cache.size(), 1);

    cache.insert(1, 'c');
    ASSERT_EQ(cache.capacity(), 2);
    ASSERT_EQ(cache.size(), 1);

    cache.insert(2, 'b');
    ASSERT_EQ(cache.capacity(), 2);
    ASSERT_EQ(cache.size(), 2);

    cache.insert(3, 'c');
    ASSERT_EQ(cache.capacity(), 2);
    ASSERT_EQ(cache.size(), 2);
}

TEST(LRUTest, FullTest)
{
    using test_t = lru_cache<int, char>;
    test_t cache(2);
    ASSERT_EQ(cache.capacity(), 2);
    ASSERT_EQ(cache.size(), 0);
    EXPECT_ANY_THROW(cache.get(1));

    cache.insert(1, 'a');
    ASSERT_EQ(cache.capacity(), 2);
    ASSERT_EQ(cache.size(), 1);
    EXPECT_EQ(cache.get(1), 'a');
    EXPECT_ANY_THROW(cache.get(0));

    cache.insert(1, 'c');
    ASSERT_EQ(cache.capacity(), 2);
    ASSERT_EQ(cache.size(), 1);
    EXPECT_EQ(cache.get(1), 'c');

    cache.insert(2, 'b');
    ASSERT_EQ(cache.capacity(), 2);
    ASSERT_EQ(cache.size(), 2);
    EXPECT_EQ(cache.get(1), 'c');
    EXPECT_EQ(cache.get(2), 'b');

    cache.insert(3, 'c');
    ASSERT_EQ(cache.capacity(), 2);
    ASSERT_EQ(cache.size(), 2);
    EXPECT_EQ(cache.get(2), 'b');
    EXPECT_EQ(cache.get(3), 'c');
    EXPECT_ANY_THROW(cache.get(1));

    cache.get(2);
    cache.insert(4, 'd');
    ASSERT_EQ(cache.capacity(), 2);
    ASSERT_EQ(cache.size(), 2);
    EXPECT_EQ(cache.get(2), 'b');
    EXPECT_EQ(cache.get(4), 'd');
    EXPECT_ANY_THROW(cache.get(3));
}