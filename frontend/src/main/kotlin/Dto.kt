package todoapp

// DTOs for our REST API
// TODO Copy pasted from the backend, should consider using multiplatform project here with dtos being in common part

data class ToDo(val subject: String)
data class NewToDo(val subject: String)

