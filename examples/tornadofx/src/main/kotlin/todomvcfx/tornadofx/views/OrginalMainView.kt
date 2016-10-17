package todomvcfx.tornadofx.views

import javafx.scene.control.CheckBox
import javafx.scene.control.Label
import javafx.scene.control.ListView
import javafx.scene.control.TextField
import javafx.scene.layout.VBox
import javafx.util.Callback
import todomvcfx.tornadofx.controllers.MainViewController
import todomvcfx.tornadofx.model.TodoItem
import tornadofx.View
import tornadofx.onChange

/**
 * Created by ronsmits on 24/09/16.
 */
class OrginalMainView : View() {

    override val root: VBox by fxml("/MainView.fxml")

    val addInput : TextField by fxid()
    val lvItems : ListView<TodoItem> by fxid()
    val selectAll : CheckBox by fxid()
    val itemsLeftLabel : Label by fxid()

    val controller : MainViewController by inject()

    init {

//        lvItems.itemsProperty().bind( controller.todoCursor )

        addInput.setOnAction {
            val newItem = TodoItem( addInput.text, false )
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

//    fun all() { controller.noFilter() }
//    fun active() { controller.filterActive()  }
//    fun completed() { controller.filterCompleted() }

    fun updateItemsLeftLabel() {
        itemsLeftLabel.text = "${lvItems.items.count { !it.completed }} items left"
    }
}
