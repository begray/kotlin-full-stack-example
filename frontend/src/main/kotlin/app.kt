package todoapp

import kotlinx.coroutines.experimental.async
import org.w3c.dom.HTMLInputElement
import react.*
import react.dom.*
import kotlinext.js.*
import kotlinx.html.*
import kotlinx.html.js.*

interface TodoProps : RProps

interface TodoState : RState {
    var items: List<ToDo>
    var subject: String
    var error: String?
}

/**
 * React component to display a list of ToDos and add new ones
 *
 * TODO plain html atm, needs some styling
 */
class Todo(props: TodoProps) : RComponent<TodoProps, TodoState>(props) {
    override fun TodoState.init(props: TodoProps) {
        items = listOf()
        subject = ""
    }

    override fun componentDidMount() {
        refresh()
    }

    private fun refresh() {
        async(onCompletion = {
            if (it != null) {
                onFailed(it)
            }
        }) {
            val toDoList = getToDoList()

            setState {
                items = toDoList
            }
        }
    }

    override fun RBuilder.render() {
        div {
            input(type = InputType.text, name = "itemText") {
                key = "itemText"

                attrs {
                    value = state.subject
                    placeholder = "Add a To Do item"
                    onChangeFunction = {
                        val target = it.target as HTMLInputElement
                        setState {
                            subject = target.value
                        }
                    }
                }
            }

            button {
                +"Add"
                attrs {
                    onClickFunction = {
                        if (state.subject.isNotEmpty()) {

                            async(onCompletion = {
                                if (it != null) {
                                    onFailed(it)
                                }
                            }) {
                                val item = postToDo(state.subject)
                                onSubmitted(item)
                            }
                        }
                    }
                }
            }

            h3 {
                if (null != state.error) {
                    b {
                        +"Error occured: ${state.error}"
                    }
                }

                div {
                    for (item in state.items) {
                        div {
                            attrs.jsStyle = js {
                                display = "flex"
                            }

                            div {
                                attrs.jsStyle = js {
                                    width = "200px"
                                    textAlign = "center"
                                }

                                +item.subject
                            }

                            button {
                                +"×"
                                attrs {
                                    onClickFunction = {
                                        async(onCompletion = {
                                            if (it != null) {
                                                onFailed(it)
                                            } else {
                                                setState {
                                                    items = items.filter { it.id != item.id }
                                                }
                                            }
                                        }) {
                                            deleteToDo(item.id)
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    private fun onSubmitted(item: ToDo) {
        setState {
            error = null
            items += item
            subject = ""
        }
    }

    private fun onFailed(err: Throwable) {
        console.log("failed: " + err.message)

        setState {
            error = err.message
        }
    }
}

fun RBuilder.todo() = child(Todo::class) {}
