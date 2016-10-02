package todomvcfx.tornadofx.views

import javafx.scene.control.ListCell
import todomvcfx.tornadofx.controllers.MainViewController
import todomvcfx.tornadofx.model.TodoItem
import tornadofx.find

/**
 * Custom ListCell for a TodoItem and controls
 *
 */
class TodoItemListCell : ListCell<TodoItem>() {

    val controller : MainViewController = find(MainViewController::class)

    override fun updateItem(item: TodoItem?, empty: Boolean) {
        super.updateItem(item, empty)
        if( empty || item == null) {
            text = null
            graphic = null
        } else {
            text = null
            graphic = controller.readCache(item).root
        }
    }

}
