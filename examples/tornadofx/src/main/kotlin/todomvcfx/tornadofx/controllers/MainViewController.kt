package todomvcfx.tornadofx.controllers

import javafx.beans.property.SimpleObjectProperty
import javafx.collections.transformation.FilteredList
import todomvcfx.tornadofx.model.TodoItem
import tornadofx.*

/**
 * Created by ronsmits on 24/09/16.
 */
class MainViewController : Controller() {

    private val todoItemModel = mutableListOf<TodoItem>().observable()
    val list = FilteredList(todoItemModel, { p -> true })

    val filterByProperty = SimpleObjectProperty({ p: TodoItem -> true })  // show all
    var filterBy: ((TodoItem) -> Boolean)? by filterByProperty


    init {
        filterByProperty.onChange {
            list.setPredicate(filterBy)
        }
    }
    fun selectAll(toggle: Boolean) {
        todoItemModel.forEach { it -> it.completed = toggle }
    }

    fun addItem(item: TodoItem) {
        todoItemModel.add(item)

    }

    fun removeItem(item: TodoItem) {
        todoItemModel.remove(item)
        list.setPredicate(filterBy)
    }

}
