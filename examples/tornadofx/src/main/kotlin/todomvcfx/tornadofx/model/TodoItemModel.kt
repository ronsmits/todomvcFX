package todomvcfx.tornadofx.model

import javafx.beans.property.ObjectProperty
import javafx.beans.property.ReadOnlyObjectProperty
import javafx.beans.property.SimpleObjectProperty
import javafx.collections.FXCollections
import javafx.collections.ObservableList
import javafx.collections.transformation.FilteredList
import todomvcfx.tornadofx.views.ItemFragment
import tornadofx.Component
import tornadofx.Injectable
import tornadofx.find
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
class TodoItemModel : Component(), Injectable {

    val itemsProperty : ReadOnlyObjectProperty<ObservableList<TodoItem>> = SimpleObjectProperty(FXCollections.observableArrayList())

    val viewableItemsProperty : ReadOnlyObjectProperty<FilteredList<TodoItem>> = SimpleObjectProperty(FilteredList(itemsProperty.get()))

    val filterByProperty: ObjectProperty<Predicate<in TodoItem>>
        get() = viewableItemsProperty.get().predicateProperty()

    fun add(tdi: TodoItem) {
        itemsProperty.get().add(tdi)
    }

    fun remove(tdi: TodoItem) : Boolean {

        val removed = itemsProperty.get().remove( tdi )
        if( removed != null ) {
            removeItemFromCache(tdi)
        }

        return true
    }

    private val cellCache : MutableMap<Int, ItemFragment> = mutableMapOf()

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