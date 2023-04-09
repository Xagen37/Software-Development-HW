package response

import kotlinx.html.*
import kotlinx.html.stream.createHTML
import model.Currency
import model.Product

fun index(products: List<Product>, currency: Currency): String {
    return createHTML().html {
        body {
            table {
                tr {
                    th {
                        text("Name")
                    }
                    th {
                        text("Description")
                    }
                    th {
                        text("Price")
                    }
                }
                products.forEach {
                    tr {
                        td {
                            text(it.name)
                        }
                        td {
                            text(it.desc)
                        }
                        td {
                            text(currency.fromRubles(it.priceRub))
                        }
                    }
                }
            }
        }
    }
}

fun register(): String {
    return createHTML().html {
        body {
            form(
                action = "/register",
                method = FormMethod.post
            ) {
                p {
                    +"Login"
                    textInput(name = "login")
                }
                select {
                    name = "wallets"
                    for (v in Currency.values()) {
                        option {
                            value = v.name
                            text(v.name)
                        }
                    }
                }
                submitInput(classes = "pure-button pure-button-primary") {
                    value = "Register"
                }
            }
        }
    }
}

fun login(): String {
    return createHTML().html {
        body {
            form(
                action = "/login",
                method = FormMethod.post
            ) {
                p {
                    +"Login"
                    textInput(name = "login")
                }
                submitInput(classes = "pure-button pure-button-primary") {
                    value = "Login"
                }
            }
        }
    }
}

fun addProduct(): String {
    return createHTML().html {
        body {
            form(
                action = "/addProduct",
                method = FormMethod.post
            ) {
                p {
                    +"Name"
                    textInput(name = "name")
                }
                p {
                    +"Rubles"
                    textInput(name = "rubles")
                }
                submitInput(classes = "pure-button pure-button-primary") {
                    value = "Add"
                }
            }
        }
    }
}