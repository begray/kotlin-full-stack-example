package todoapp

import io.ktor.application.*
import io.ktor.features.*
import io.ktor.http.*
import io.ktor.response.*
import io.ktor.routing.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*

data class ToDoItem(val text: String)

fun Application.main() {
    install(DefaultHeaders)
    install(CallLogging)
    install(Routing) {
        get("/") {
            call.respondText("", ContentType.Text.Plain)
        }
        get("/demo") {
            call.respond(
                    ToDoItem("hello world!")
            )
        }
    }
}
