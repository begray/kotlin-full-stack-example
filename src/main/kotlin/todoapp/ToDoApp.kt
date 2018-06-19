package todoapp

import com.fasterxml.jackson.databind.SerializationFeature

import io.ktor.application.*
import io.ktor.features.*
import io.ktor.jackson.*
import io.ktor.http.*
import io.ktor.request.receive
import io.ktor.response.*
import io.ktor.routing.*
import java.util.*

// One To-Do list item
data class ToDoItem(val subject: String)

// TODO implement database storage
val todos = Collections.synchronizedList(mutableListOf(
        ToDoItem("todo1"),
        ToDoItem("todo2")
))

fun Application.main() {
    install(ContentNegotiation) {
        jackson {
            enable(SerializationFeature.INDENT_OUTPUT)
        }
    }
    install(DefaultHeaders)
    install(CallLogging)
    install(Routing) {
        route("/todos") {
            get {
                call.respond(
                        synchronized(todos) { todos.toList() }
                )
            }
            post {
                val item = call.receive<ToDoItem>()
                todos += item
                call.respondText("", ContentType.Application.Json)
            }
        }
    }
}
