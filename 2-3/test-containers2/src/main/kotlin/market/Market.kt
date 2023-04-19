package market

interface Market {
    fun getCompany(id: Int): Company?
    fun getUser(id: Int): User?
    fun getCompaniesIdByUser(userId: Int): Set<Int>
    fun getHoldersIdByCompany(companyId: Int): Set<Int>
    fun getUserStocks(companyId: Int, userId: Int): Long


    fun addCompany(company: Company): Int
    fun addStocks(companyId: Int, count: Long)

    fun addUser(user: User): Int
    fun addMoney(userId: Int, value: Long) : Boolean

    fun purchase(userId: Int, companyId: Int, count: Long, price: Long): Boolean
    fun sell(userId: Int, companyId: Int, count: Long, price: Long): Boolean
    fun adjustPrice(companyId: Int, newPrice: Long): Boolean
}