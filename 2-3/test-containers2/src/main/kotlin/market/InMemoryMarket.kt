package market

class InMemoryMarket : Market {
    private val companies = mutableListOf<Company>()
    private val users = mutableListOf<User>()
    private val userToCompanies = mutableMapOf<Int, MutableSet<Int>>()
    private val companyToUsers = mutableMapOf<Int, MutableSet<Int>>()
    private val userStocks = mutableMapOf<Pair<Int, Int>, Long>()

    override fun getCompany(id: Int): Company? {
        return companies.getOrNull(id)
    }

    override fun getUser(id: Int): User? {
        return users.getOrNull(id)
    }

    override fun getCompaniesIdByUser(userId: Int): Set<Int> {
        return userToCompanies[userId] ?: setOf()
    }

    override fun getHoldersIdByCompany(companyId: Int): Set<Int> {
        return companyToUsers[companyId] ?: setOf()
    }

    override fun getUserStocks(companyId: Int, userId: Int): Long {
        return userStocks[companyId to userId] ?: 0L
    }

    override fun addCompany(company: Company): Int {
        companies.add(company)
        return companies.size - 1
    }

    override fun addStocks(companyId: Int, count: Long) {
        if (count <= 0) {
            return
        }

        val company = companies.getOrNull(companyId) ?: return
        val stocks = company.stocks
        stocks.number += count
    }

    override fun addUser(user: User): Int {
        users.add(user)
        return users.size - 1
    }

    override fun addMoney(userId: Int, value: Long) : Boolean {
        if (value < 0) {
            return false
        }
        val user = users.getOrNull(userId)
        return if (user == null) {
            false
        } else {
            users[userId].money += value
            true
        }
    }

    override fun purchase(userId: Int, companyId: Int, count: Long, price: Long): Boolean {
        if (count < 0) {
            return false
        }
        val user = getUser(userId)?: return false
        val company = getCompany(companyId)?: return false
        val total = count * price
        if (company.stocks.price != price || company.stocks.number < count) {
            return false
        }
        if (user.money < total) {
            return false
        }

        user.money -= total
        userToCompanies.getOrPut(userId) { mutableSetOf()}.add(companyId)
        companyToUsers.getOrPut(companyId) { mutableSetOf()}.add(userId)
        userStocks[companyId to userId] = getUserStocks(companyId, userId) + count
        company.stocks.number -= count
        return true
    }

    override fun sell(userId: Int, companyId: Int, count: Long, price: Long): Boolean {
        if (count < 0) {
            return false
        }
        val user = getUser(userId)?: return false
        val company = getCompany(companyId)?: return false
        val total = count * price
        val currUserStocks = getUserStocks(companyId, userId)
        if (company.stocks.price != price) {
            return false
        }
        if (currUserStocks < count) {
            return false
        }

        user.money += total
        company.stocks.number += count
        userStocks[companyId to userId] = currUserStocks - count
        if (userStocks[companyId to userId] == 0L) {
            userStocks.remove(companyId to userId)
            userToCompanies[userId]!!.remove(companyId)
            companyToUsers[companyId]!!.remove(userId)
        }
        return true
    }

    override fun adjustPrice(companyId: Int, newPrice: Long): Boolean {
        if (newPrice < 0) {
            return false
        }
        val company = getCompany(companyId) ?: return false
        company.stocks.price = newPrice
        return true
    }
}