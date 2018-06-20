package todoapp

import com.fasterxml.jackson.databind.SerializationFeature

import io.ktor.application.*
import io.ktor.features.*
import io.ktor.jackson.*
import io.ktor.http.*
import io.ktor.request.receive
import io.ktor.response.*
import io.ktor.routing.*

import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.transaction
import org.jetbrains.exposed.sql.SchemaUtils.create

//fun initDatabase(): Database {
//    val db = Database.connect("jdbc:h2:mem:test", driver = "org.h2.Driver")
//    transaction(db) {
//
//        create (ToDoItems)
//
//        ToDoItem.new {
//            subject = "my first automatically created todo item"
//        }
//
//        println("Init: ${ToDoItem.all().joinToString {it.subject}}")
//    }
//    return db
//}

fun Application.main() {
//    val db = initDatabase()

    install(ContentNegotiation) {
        jackson {
            enable(SerializationFeature.INDENT_OUTPUT)
        }
    }
    install(DefaultHeaders)
    install(CallLogging)

    routing {
        route("/todos") {
            get {
                val items = call.let {
                    transaction(DbSettings.db) {
                        ToDoItem.all().map {
                            ToDo(it.subject)
                        }
                    }
                }

                println("Get: ${items.joinToString {it.subject}}")

                call.respond(items)
            }
            post {
                val item = call.receive<NewToDo>()

                call.run {
                    transaction(DbSettings.db) {
                        ToDoItem.new {
                            subject = item.subject
                        }
                    }
                }

                call.respondText("", ContentType.Application.Json)
            }
        }
    }
}
