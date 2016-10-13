package todomvcfx.tornadofx.controllers

import todomvcfx.tornadofx.model.TodoItem
import todomvcfx.tornadofx.model.TodoItemModel
import todomvcfx.tornadofx.views.ItemFragment
import tornadofx.Controller
import tornadofx.find

/**
 * Controller class for the TornadoFX version of the TodoItem app
 *
 * A TornadoFX Controller object (not a JavaFX Controller)
 *
 * Mediates between the UI, which binds to the viewableItemsProperty and the filterByProperty, and the model which
 * is updated by a pair of functions.  The itemsProperty can also be used to get at the unfiltered model.
 *
 * Updates coming from the UI are handled automatically since the domain object, TodoItem, is JavaFX property-based
 *
 * @author carl
 */
class MainViewController : Controller() {

    private val todoItemModel = TodoItemModel()
    private val cellCache : MutableMap<Int, ItemFragment> = mutableMapOf()

    fun addItem( item : TodoItem) {
        todoItemModel.add( item )
    }

    fun removeItem( item : TodoItem) {
        val removed = todoItemModel.remove( item )
        if( removed ) {
            removeItemFromCache(item)
        }
    }

    val filterByProperty = todoItemModel.filterByProperty

    val viewableItemsProperty = todoItemModel.viewableItemsProperty

    val itemsProperty = todoItemModel.itemsProperty

    fun readCache(item : TodoItem) : ItemFragment {

        val id = item.id

        if( !cellCache.containsKey(id) ) {

            val itemFragment = find(ItemFragment::class)  // prototype
            itemFragment.load( item )
            cellCache.put( id, itemFragment )
        }

        return cellCache[id]!!
    }

    private fun removeItemFromCache(item : TodoItem) {
        cellCache.remove( item.id )
    }
}

