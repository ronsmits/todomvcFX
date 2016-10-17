package todomvcfx.tornadofx

import javafx.application.Application
import javafx.beans.property.SimpleBooleanProperty
import javafx.beans.property.SimpleIntegerProperty
import javafx.beans.property.SimpleObjectProperty
import javafx.beans.property.SimpleStringProperty
import javafx.collections.FXCollections
import javafx.collections.ObservableList
import javafx.scene.control.*
import javafx.scene.layout.HBox
import javafx.scene.layout.VBox
import javafx.util.Callback
import todomvcfx.tornadofx.controllers.MainViewController
import todomvcfx.tornadofx.model.TodoItem
import todomvcfx.tornadofx.views.ItemFragment
import todomvcfx.tornadofx.views.MainView
import todomvcfx.tornadofx.views.OrginalMainView
import tornadofx.*
import kotlin.properties.ObservableProperty

/**
 *
 * @author carl
 */


class TornadoFXApp : App(MainView::class, Styles::class) {

}
//
//fun main(args: Array<String>) {
//    Application.launch(TornadoFXApp::class.java, *args)
//}