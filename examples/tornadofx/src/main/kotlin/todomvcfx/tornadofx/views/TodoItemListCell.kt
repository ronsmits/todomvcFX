package todomvcfx.tornadofx.views

import javafx.scene.control.ListCell
import todomvcfx.tornadofx.model.TodoItem
import todomvcfx.tornadofx.model.TodoItemModel
import tornadofx.find

/**
 * Custom ListCell for a TodoItem and controls
 *
 */
@Deprecated("using cellCache instead of cellFactory")
class TodoItemListCell : ListCell<TodoItem>() {

    val model : TodoItemModel = find(TodoItemModel::class)

    override fun updateItem(item: TodoItem?, empty: Boolean) {
        super.updateItem(item, empty)
        if( empty || item == null) {
            text = null
            graphic = null
        } else {
            text = null
            graphic = model.readCache(item).root
        }
    }

}
