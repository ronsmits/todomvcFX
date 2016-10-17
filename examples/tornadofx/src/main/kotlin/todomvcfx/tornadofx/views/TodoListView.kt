package todomvcfx.tornadofx.views

import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon.CLOSE
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView
import javafx.geometry.Pos
import javafx.scene.layout.Priority.ALWAYS
import todomvcfx.tornadofx.Styles
import todomvcfx.tornadofx.controllers.MainViewController
import todomvcfx.tornadofx.model.TodoItem
import tornadofx.*


class TodoListView : View() {
    val controller: MainViewController by inject()
    override val root = listview(controller.list) {
        cellCache { currentItem ->
            hbox {
                addClass(Styles.itemRoot)
                checkbox(property = currentItem.completedProperty)
                stackpane {
                    alignment = Pos.CENTER_LEFT
                    hboxConstraints { hGrow = ALWAYS }
                    hbox {
                        addClass(Styles.contentBox)
                        label(currentItem.textProperty) {
                            maxWidth = Double.MAX_VALUE
                            hboxConstraints { hGrow = ALWAYS }
                            currentItem.completedProperty.onChange { checked ->
                                toggleClass(Styles.completed, checked ?: false)
                            }
                        }

                        button {
                            alignment = Pos.CENTER_RIGHT
                            graphic = FontAwesomeIconView(CLOSE).apply { size = "1.5em" }.addClass(Styles.closeIcon)
                            visibleProperty().bind(this@hbox.hoverProperty())
                            setOnAction {
                                controller.removeItem(currentItem)
                            }
                        }
                    }
                }
            }
        }
    }

}
