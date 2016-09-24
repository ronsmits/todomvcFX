package todomvcfx.tornadofx.views

import javafx.scene.control.ListCell
import todomvcfx.tornadofx.model.TodoItem
import tornadofx.find

/**
 * Custom ListCell for a TodoItem and controls
 *
 */
class TodoItemListCell : ListCell<TodoItem>() {

    override fun updateItem(item: TodoItem?, empty: Boolean) {
        super.updateItem(item, empty)
        if( empty || item == null) {
            text = null
            graphic = null
        } else {
            text = null
            graphic = readCache(item).root
        }
    }

    companion object {

        // TODO: move to controller so that cache can be cleaned up with business logic (add, remove)
        var cellCache : MutableMap<Int, ItemFragment> = mutableMapOf()

        fun readCache(item : TodoItem) : ItemFragment {

            val id = item.id

            if( !cellCache.containsKey(id) ) {

                val itemFragment = find(ItemFragment::class)  // prototype
                itemFragment.load( item )
                cellCache.put( id, itemFragment )
            }

            return cellCache[id]!!
        }
    }
}
