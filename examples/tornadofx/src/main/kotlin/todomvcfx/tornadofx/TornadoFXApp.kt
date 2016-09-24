package todomvcfx.tornadofx

import javafx.application.Application
import tornadofx.App

/**
 * Main entry point for the TornadoFX version of the TodoItem app
 *
 * @author carl
 */
class TornadoFXApp : App(MainView::class) {
    companion object {
        @JvmStatic
        fun main(args: Array<String>) {
            Application.launch(TornadoFXApp::class.java)
        }
    }
}