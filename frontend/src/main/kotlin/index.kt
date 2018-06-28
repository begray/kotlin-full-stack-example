package todoapp

import react.dom.render
import kotlin.browser.document

fun main(args: Array<String>) {

    // TODO it's probably a good idea to move index.html to frontend

    render(document.getElementById("root")) {
        todo()
    }
}

