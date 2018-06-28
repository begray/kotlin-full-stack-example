package todoapp

/**
 * DTOs for our REST API
 */

data class ToDo(val id: Int, val subject: String)
data class NewToDo(val subject: String)
data class RemoveToDo(val id: Int)

