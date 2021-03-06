package todoapp

import com.fasterxml.jackson.databind.SerializationFeature
import io.ktor.application.Application
import io.ktor.application.call
import io.ktor.application.install
import io.ktor.content.defaultResource
import io.ktor.content.files
import io.ktor.content.static
import io.ktor.features.CallLogging
import io.ktor.features.ContentNegotiation
import io.ktor.features.DefaultHeaders
import io.ktor.http.ContentType
import io.ktor.jackson.jackson
import io.ktor.request.receive
import io.ktor.response.respond
import io.ktor.response.respondText
import io.ktor.routing.*
import org.jetbrains.exposed.sql.transactions.transaction

fun Application.main() {

    install(ContentNegotiation) {
        jackson {
            enable(SerializationFeature.INDENT_OUTPUT)
        }
    }
    install(DefaultHeaders)
    install(CallLogging)

    routing {
        static("/") {
            defaultResource("index.html")
        }
        static("frontend") {
            files("frontend")
        }
        route("/todos") {
            get {
                val items = call.let {
                    transaction(DbSettings.db) {
                        ToDoItem.all().map {
                            ToDo(it.id.value, it.subject)
                        }
                    }
                }

                call.respond(items)
            }
            post {
                val item = call.receive<NewToDo>()

                val todo = call.let {
                    transaction(DbSettings.db) {
                        ToDoItem.new {
                            subject = item.subject
                        }
                    }
                }

                call.respond(ToDo(todo.id.value, todo.subject))
            }
            delete {
                val item = call.receive<RemoveToDo>()

                call.run {
                    transaction(DbSettings.db) {
                        ToDoItem[item.id].delete()
                    }
                }

                call.respondText("{}", ContentType.Application.Json)
            }
        }
    }
}
