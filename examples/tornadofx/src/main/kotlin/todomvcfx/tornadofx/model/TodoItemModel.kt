package todomvcfx.tornadofx.model

import javafx.beans.property.*
import javafx.collections.FXCollections
import javafx.collections.ObservableList
import javafx.collections.transformation.FilteredList
import tornadofx.getValue
import tornadofx.onChange
import tornadofx.setValue
import java.util.function.Predicate

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

    val itemsProperty : ReadOnlyObjectProperty<ObservableList<TodoItem>> = SimpleObjectProperty(FXCollections.observableArrayList())

    val viewableItemsProperty : ReadOnlyObjectProperty<FilteredList<TodoItem>> = SimpleObjectProperty(FilteredList(itemsProperty.get()))

    val filterByProperty: ObjectProperty<Predicate<in TodoItem>>
        get() = viewableItemsProperty.get().predicateProperty()

    /*
        val filterByProperty: ObjectProperty<Predicate<in TodoItem>>?
        get() = viewableItemsProperty.get().predicateProperty()
     */

    fun add(tdi: TodoItem) {
        itemsProperty.get().add(tdi)
    }

    fun remove(tdi: TodoItem) : Boolean {
        return itemsProperty.get().remove(tdi)
    }
}