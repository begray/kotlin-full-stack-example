package todoapp

import kotlinx.coroutines.experimental.await
import org.w3c.fetch.RequestCredentials
import org.w3c.fetch.RequestInit
import kotlin.browser.window
import kotlin.js.JSON.stringify
import kotlin.js.json

/**
 * Dumb REST API client
 *
 * TODO consider using an existing library to do REST API communication to avoid hand written to/from JSON serialization
 * TODO does multiplatform project provide REST API client generated from backend interfaces?
 */

suspend fun postToDo(subject: String): ToDo =
        postAndParseResult(
                "/todos",
                stringify(json("subject" to subject)),
                ::parseToDoItem
        )

suspend fun getToDoList(): List<ToDo> =
        getAndParseResult(
                "/todos",
                null,
                ::parseToDoList
        )

suspend fun deleteToDo(id: Int): Unit =
        deleteAndParseResult(
                "/todos",
                stringify(json("id" to id)),
                ::parseEmptyResponse
        )

private fun parseToDoList(json: dynamic): List<ToDo> {
    val toDoList = json as Array<dynamic>

    return toDoList.map(::parseToDoItem)
}

private fun parseEmptyResponse(json: dynamic) {}

private fun parseToDoItem(json: dynamic): ToDo {
    return ToDo(json.id as Int, json.subject as String)
}

private suspend fun <T> postAndParseResult(url: String, body: dynamic, parse: (dynamic) -> T): T =
        requestAndParseResult("POST", url, body, parse)

private suspend fun <T> getAndParseResult(url: String, body: dynamic, parse: (dynamic) -> T): T =
        requestAndParseResult("GET", url, body, parse)

private suspend fun <T> deleteAndParseResult(url: String, body: dynamic, parse: (dynamic) -> T): T =
        requestAndParseResult("DELETE", url, body, parse)

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

