package todoapp

import kotlinx.html.*
import kotlinx.html.js.*
import org.w3c.dom.*
import react.*
import react.dom.*
import kotlinx.coroutines.experimental.*

interface TodoProps : RProps

interface TodoState : RState {
    var items: List<String>
    var subject: String
    var error: String?
}

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
                items = toDoList.map { it.subject }
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
                                } else {
                                    onSubmitted()
                                }
                            }) {
                                postToDo(state.subject)
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

                ul {
                    for ((index, item) in state.items.withIndex()) {
                        li {
                            key = index.toString()
                            +item
                        }
                    }
                }
            }
        }
    }

    private fun onSubmitted() {
        setState {
            error = null
            items += subject
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

class App : RComponent<RProps, RState>() {
    override fun RBuilder.render() {
        todo()
    }
}

fun RBuilder.app() = child(App::class) {}

