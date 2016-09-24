package todomvcfx.tornadofx

import javafx.beans.binding.When
import javafx.scene.control.*
import javafx.scene.layout.HBox
import javafx.scene.layout.VBox
import javafx.util.Callback
import tornadofx.*

/**
 * View classes for the TornadoFX version of the TodoItem app
 *
 * @author carl
 */
class MainView : View() {

    override val root: VBox by fxml("/MainView.fxml")

    val addInput : TextField by fxid()
    val lvItems : ListView<TodoItem> by fxid()
    val selectAll : CheckBox by fxid()
    val itemsLeftLabel : Label by fxid()
    val showAll : ToggleButton by fxid()
    val showActive : ToggleButton by fxid()
    val showCompleted : ToggleButton by fxid()

    val controller : MainViewController by inject()

    init {

        lvItems.itemsProperty().bind( controller.viewableItemsProperty )

        val stateGroup = showAll.toggleGroup

        controller.filterByProperty.bind(

                When(stateGroup.selectedToggleProperty().isEqualTo(showActive))
                        .then( {tdi: TodoItem -> tdi.completed.not()} )
                        .otherwise(
                                When(stateGroup.selectedToggleProperty().isEqualTo(showCompleted))
                                        .then( {tdi: TodoItem -> tdi.completed })
                                        .otherwise( {tdi: TodoItem -> true })
                        )
        )

        addInput.setOnAction {
            val newItem = TodoItem( addInput.text, false )
            controller.addItem( newItem )
            addInput.clear()
            selectAll.isSelected = false
        }

        lvItems.cellFactory = Callback { TodoItemListCell() }

        // switch this to binding
        selectAll.isVisible = false
        lvItems.itemsProperty().onChange {
            selectAll.isVisible = lvItems.items.isNotEmpty()
            updateItemsLeftLabel()
        }

        selectAll.selectedProperty().onChange { nv ->
            lvItems.items.forEach { itm ->
                itm.completed = nv ?: false  // also sets model b/c of reference
            }
        }

        itemsLeftLabel.text = "0 items left"
    }

    fun updateItemsLeftLabel() {
        itemsLeftLabel.text = "${lvItems.items.count { !it.completed }} items left"
    }
}

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

class ItemFragment : Fragment() {

    override val root: HBox by fxml("/ItemFragment.fxml")

    val completed : CheckBox by fxid()
    val contentLabel : Label by fxid()
    val deleteButton : Button by fxid()
    val contentBox : HBox by fxid()
    val contentInput : TextField by fxid()

    var item : TodoItem? = null

    val controller : MainViewController by inject()
    val mainView: MainView by inject()

    init {
        deleteButton.visibleProperty().bind( root.hoverProperty() )
    }

    fun delete() {
        if( item != null ) {
            mainView.lvItems.items.remove( item )
            controller.removeItem( item!! )
        }
    }


    fun load(item : TodoItem) {

        this.item = item

        completed.bind( item.completedProperty )

        item.completedProperty.onChange { nv ->
            contentLabel.toggleClass("strikethrough", nv ?: false)
            mainView.updateItemsLeftLabel()
        }
        contentLabel.textProperty().bind( item.textProperty )
        contentInput.textProperty().bindBidirectional( item.textProperty )

        contentLabel.setOnMouseClicked { event ->
            if (event.clickCount > 1) {
                toggleEditMode(true)
            }
        }

        contentInput.setOnAction {
            toggleEditMode(false)
        }

        contentInput.focusedProperty().onChange { newValue ->
            if (!(newValue ?: false)) {
                toggleEditMode(false)
            }
        }
    }

    fun toggleEditMode(edit : Boolean) {
        contentInput.isVisible = edit
        if( edit ) {
            contentInput.requestFocus()
        }
        contentBox.isVisible = !edit
        completed.isVisible = !edit
    }
}

