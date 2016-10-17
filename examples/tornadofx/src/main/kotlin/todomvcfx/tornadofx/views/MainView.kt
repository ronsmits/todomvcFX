package todomvcfx.tornadofx.views

import javafx.beans.binding.When
import javafx.geometry.Pos
import todomvcfx.tornadofx.Styles
import todomvcfx.tornadofx.controllers.MainViewController
import todomvcfx.tornadofx.model.TodoItem
import tornadofx.*

class MainView : View() {
    val controller: MainViewController by inject()
    val todoListView: TodoListView by inject()

    override val root = vbox {
        alignment = Pos.CENTER
        label("TODOS").setId(Styles.title)
        hbox {
            setId(Styles.addItemRoot)
            checkbox() {
                setOnAction {
                    controller.selectAll(this.isSelected)
                }
            }
            textfield {
                promptText = "what do you want to do"
                setOnAction {
                    controller.addItem(TodoItem(this.text, false))
                    this.clear()
                }
            }

        }
        add(todoListView.root)
        hbox {
            togglegroup {
                togglebutton("All") {
                    isSelected=true
                }
                val showActive = togglebutton("Active")
                val showCompleted = togglebutton("Done")
                controller.filterByProperty.bind(

                        When(selectedToggleProperty().isEqualTo(showActive))
                                .then({ item: TodoItem -> item.completed.not() })
                                .otherwise(
                                        When(selectedToggleProperty().isEqualTo(showCompleted))
                                                .then({ item: TodoItem -> item.completed })
                                                .otherwise({ item: TodoItem -> true })
                                )
                )
            }
        }
    }

}