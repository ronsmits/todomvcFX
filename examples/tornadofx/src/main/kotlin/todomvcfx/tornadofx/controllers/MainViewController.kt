package todomvcfx.tornadofx.controllers

import javafx.beans.property.SimpleObjectProperty
import javafx.collections.FXCollections
import todomvcfx.tornadofx.model.TodoItem
import tornadofx.Controller

/**
 * Created by ronsmits on 24/09/16.
 */
class MainViewController : Controller() {

    private val todoItemModel : MutableList<TodoItem> = mutableListOf()

    //
    // View binds to a model that's always available via inject()
    //
    var todoCursor = SimpleObjectProperty(FXCollections.observableArrayList(todoItemModel))

    enum class FilterType { NO_FILTER, COMPLETED, ACTIVE }

    private var filterApplied = FilterType.NO_FILTER

    fun addItem( item : TodoItem) {
        todoItemModel.add( item )
        if( filterApplied != FilterType.COMPLETED ) {
            todoCursor.get().add(item)
        }
    }

    fun removeItem( item : TodoItem) {
        todoItemModel.remove( item )
        todoCursor.get().add( item )
    }

    fun noFilter() {
        filterApplied = FilterType.NO_FILTER
        todoCursor.set( FXCollections.observableArrayList(todoItemModel) )
    }

    fun filterCompleted() {
        filterApplied = FilterType.COMPLETED
        todoCursor.set( FXCollections.observableArrayList(todoItemModel.filter( { itm -> itm.completed } ) ) )
    }

    fun filterActive() {
        filterApplied = FilterType.ACTIVE
        todoCursor.set( FXCollections.observableArrayList(todoItemModel.filter( { itm -> itm.completed.not()} ) ) )
    }
}
