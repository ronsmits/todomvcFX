package todomvcfx.tornadofx.views

import javafx.beans.binding.When
import javafx.scene.control.*
import javafx.scene.layout.VBox
import javafx.util.Callback
import todomvcfx.tornadofx.controllers.MainViewController
import todomvcfx.tornadofx.model.TodoItem
import tornadofx.View
import tornadofx.onChange

/**
 * View component for the main UI
 *
 * Created by ronsmits on 24/09/16.
 */
class MainView : View() {

    override val root: VBox by fxml("/MainView.fxml")

    val addInput : TextField by fxid()
    val lvItems : ListView<TodoItem> by fxid()
    val selectAll : CheckBox by fxid()
    val itemsLeftLabel : Label by fxid()
    val showAll : ToggleButton by fxid()
    val showActive : ToggleButton by fxid()
    val showCompleted : ToggleButton by fxid()

    val controller : MainViewController by inject()

    init {

        lvItems.itemsProperty().bind( controller.viewableItemsProperty )

        val stateGroup = showAll.toggleGroup

        controller.filterByProperty.bind(

                When(stateGroup.selectedToggleProperty().isEqualTo(showActive))
                        .then( {tdi: TodoItem -> tdi.completed.not()} )
                        .otherwise(
                                When(stateGroup.selectedToggleProperty().isEqualTo(showCompleted))
                                        .then( {tdi: TodoItem -> tdi.completed })
                                        .otherwise( {tdi: TodoItem -> true })
                        )
        )

        addInput.setOnAction {
            val newItem = TodoItem(addInput.text, false)
            controller.addItem( newItem )
            addInput.clear()
            selectAll.isSelected = false
        }

        lvItems.cellFactory = Callback { TodoItemListCell() }

        // switch this to binding
        selectAll.isVisible = false
        lvItems.itemsProperty().onChange {
            selectAll.isVisible = lvItems.items.isNotEmpty()
            updateItemsLeftLabel()
        }

        selectAll.selectedProperty().onChange { nv ->
            lvItems.items.forEach { itm ->
                itm.completed = nv ?: false  // also sets model b/c of reference
            }
        }

        itemsLeftLabel.text = "0 items left"
    }

    fun updateItemsLeftLabel() {
        itemsLeftLabel.text = "${lvItems.items.count { !it.completed }} items left"
    }
}
