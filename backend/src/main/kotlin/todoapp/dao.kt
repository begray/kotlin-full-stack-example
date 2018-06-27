package todoapp

import io.ktor.application.Application
import io.ktor.application.ApplicationEnvironment
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

        System.out.println("Environment: ")
        val env = System.getenv()
        env.entries.forEach {
            System.out.println("${it.key}: ${it.value}")
        }

        // if POSTGRES related environment variables are set, then try to use PostgreSQL
        // use local H2 otherwise (for local testing)
        val dbAddress = System.getenv("TODOAPP_DB_POSTGRES")
        val dbPassword = System.getenv("TODOAPP_DB_PASSWORD")

        val db = if (null != dbAddress && null != dbPassword) {
            System.out.println("Using PostgreSQL database at $dbAddress")

            Database.connect("jdbc:postgresql://$dbAddress/todoapp?user=todoapp&password=$dbPassword", driver = "org.postgresql.Driver")
        } else {
            System.out.println("Using H2 database")

            Database.connect("jdbc:h2:file:./todoapp.db", driver = "org.h2.Driver")
        }

        // TODO this is not a good way to initialize a database
        transaction(db) {
            create (ToDoItems)
        }
        db
    }
}
