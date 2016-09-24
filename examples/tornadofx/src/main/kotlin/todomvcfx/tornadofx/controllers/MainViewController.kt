package todomvcfx.tornadofx.controllers

import todomvcfx.tornadofx.model.TodoItem
import todomvcfx.tornadofx.model.TodoItemModel
import tornadofx.Controller

/**
 * Controller class for the TornadoFX version of the TodoItem app
 *
 * A TornadoFX Controller object (not a JavaFX Controller)
 *
 * Mediates between the UI, which binds to the viewableItemsProperty and the filterByProperty, and the model which
 * is updated by a pair of functions.
 *
 * Updates coming from the UI are handled automatically since the domain object, TodoItem, is JavaFX property-based
 *
 * @author carl
 */
class MainViewController : Controller() {

    private val todoItemModel = TodoItemModel()

    fun addItem( item : TodoItem) {
        todoItemModel.add( item )
    }

    fun removeItem( item : TodoItem) {
        todoItemModel.remove( item )
    }

    val filterByProperty = todoItemModel.filterByProperty

    val viewableItemsProperty = todoItemModel.viewableItemsProperty
}

