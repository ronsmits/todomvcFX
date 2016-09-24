package todomvcfx.tornadofx.model

import javafx.beans.property.*
import javafx.collections.FXCollections
import javafx.collections.ObservableList
import tornadofx.getValue
import tornadofx.onChange
import tornadofx.setValue

/**
 * Model component for the TornadoFX version of the TodoItem app
 *
 * TodoItem model is a RAM-based store of the model objects and a cursor into the viewable objects.  Callers add
 * and remove items using the items/itemsProperty members.  Since the domain object is property-based, updates are
 * handled automatically.
 *
 * TodoItem model allows filtering through binding a predicate to the filterBy/filterByProperty members.  If no
 * filtering is set, all items are shown.
 *
 * @author carl
 */
class TodoItemModel {

    val itemsProperty : ObjectProperty<ObservableList<TodoItem>> = SimpleObjectProperty(FXCollections.observableArrayList())
    var items by itemsProperty

    val viewableItemsProperty : ObjectProperty<ObservableList<TodoItem>> = SimpleObjectProperty(FXCollections.observableArrayList())
    var viewableItems by viewableItemsProperty

    val filterByProperty = SimpleObjectProperty({ p: TodoItem -> true })  // show all
    var filterBy by filterByProperty

    fun add(tdi: TodoItem) {
        if( filterBy(tdi) ) {
            items.add(tdi)
            viewableItems.add(tdi)
        }
    }

    fun remove(tdi: TodoItem) {
        if( filterBy(tdi) ) {
            items.remove(tdi)
            viewableItems.remove(tdi)
        }
    }

    init {

        filterByProperty.onChange( {
            viewableItems.setAll( items.filtered { tdi -> filterBy(tdi) })
        })
    }
}