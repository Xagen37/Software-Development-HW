#include "pch.h"

TEST(CreationTest, SimpleCreate) {
    lru_cache<int, int> cache1;
    ASSERT_EQ(cache1.get_capacity(), 10);

    lru_cache<int, int> cache2(5);
    ASSERT_EQ(cache2.get_capacity(), 5);
}
