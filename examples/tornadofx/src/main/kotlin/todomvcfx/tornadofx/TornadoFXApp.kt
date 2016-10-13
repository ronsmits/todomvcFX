package todomvcfx.tornadofx

import javafx.application.Application
import todomvcfx.tornadofx.views.MainView
import tornadofx.App

/**
 * Main entry point for the TornadoFX version of the TodoItem app
 *
 * @author carl
 */
class TornadoFXApp : App(MainView::class)

fun main(args: Array<String>) {
    Application.launch(TornadoFXApp::class.java, *args)
}