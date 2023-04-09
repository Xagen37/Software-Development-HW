package db

import com.mongodb.client.model.Filters.eq
import com.mongodb.rx.client.MongoClients
import com.mongodb.rx.client.Success
import model.Product
import model.User
import rx.Observable

val database = MongoClients.create("mongodb://localhost:27017").getDatabase("React")

val users = database.getCollection("Users")

fun User.save(): Observable<Success> {
    return users.insertOne(this.toTable())
}

fun findUser(login: String): Observable<User> {
    return users.find(eq("login", login))
                .toObservable()
                .first()
                .map { User(it) }
}

val products = database.getCollection("Products")

fun Product.save(): Observable<Success> {
    return products.insertOne(this.toTable())
}

fun getProducts(): Observable<Product> {
    return products.find()
                   .toObservable()
                   .map { Product(it) }
}