#include "pch.h"

TEST(CreationTest, SimpleCreate) {
    using test_t = lru_cache<int, int>;

    test_t cache1;
    EXPECT_EQ(cache1.get_capacity(), 10);

    test_t cache2(5);
    EXPECT_EQ(cache2.get_capacity(), 5);

    EXPECT_ANY_THROW(test_t cache3(0));
}
