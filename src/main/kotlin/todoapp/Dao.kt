package todoapp

import org.jetbrains.exposed.dao.EntityID
import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.IntIdTable

import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.transaction
import org.jetbrains.exposed.sql.SchemaUtils.create

// Database model

object ToDoItems : IntIdTable() {
    val subject = varchar("subject", 150).index()
}

class ToDoItem(id: EntityID<Int>): IntEntity(id) {
    companion object : IntEntityClass<ToDoItem>(ToDoItems)

    var subject by ToDoItems.subject
}

// TODO global db connection for the sake of simplicity, should use connection pooling instead
object DbSettings {
    val db by lazy {
        val db = Database.connect("jdbc:h2:file:./todoapp.db", driver = "org.h2.Driver")
        transaction(db) {
            create (ToDoItems)
        }
        db
    }
}
