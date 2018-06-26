package todoapp

import kotlinx.coroutines.experimental.*
import org.w3c.fetch.*
import kotlin.browser.*
import kotlin.js.*
import kotlin.js.JSON.stringify


suspend fun postToDo(subject: String): Unit =
        postAndParseResult(
                "/todos",
                stringify(json("subject" to subject)),
                ::parseEmptyResponse
        )

suspend fun getToDoList(): List<ToDo> =
        getAndParseResult(
                "/todos",
                null,
                ::parseToDoList
        )

private fun parseToDoList(json: dynamic): List<ToDo> {
    val toDoList = json as Array<dynamic>

    return toDoList.map(::parseToDoItem)
}

private fun parseEmptyResponse(json: dynamic) {}

private fun parseToDoItem(json: dynamic): ToDo {
    return ToDo(json.subject as String)
}

private suspend fun <T> postAndParseResult(url: String, body: dynamic, parse: (dynamic) -> T): T =
        requestAndParseResult("POST", url, body, parse)

private suspend fun <T> getAndParseResult(url: String, body: dynamic, parse: (dynamic) -> T): T =
        requestAndParseResult("GET", url, body, parse)

private suspend fun <T> requestAndParseResult(method: String, url: String, body: dynamic, parse: (dynamic) -> T): T {
    val response = window.fetch(url, object : RequestInit {
        override var method: String? = method
        override var body: dynamic = body
        override var credentials: RequestCredentials? = "same-origin".asDynamic()
        override var headers: dynamic = json(
                "Accept" to "application/json",
                "Content-Type" to "application/json"
        )
    }).await()
    return parse(response.json().await())
}

