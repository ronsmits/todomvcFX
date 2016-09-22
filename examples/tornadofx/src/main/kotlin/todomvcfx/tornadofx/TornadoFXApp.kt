package todomvcfx.tornadofx

import javafx.application.Application
import javafx.beans.property.SimpleBooleanProperty
import javafx.beans.property.SimpleIntegerProperty
import javafx.beans.property.SimpleObjectProperty
import javafx.beans.property.SimpleStringProperty
import javafx.collections.FXCollections
import javafx.collections.ObservableList
import javafx.scene.control.*
import javafx.scene.layout.HBox
import javafx.scene.layout.VBox
import javafx.util.Callback
import tornadofx.*
import kotlin.properties.ObservableProperty

/**
 *
 * @author carl
 */

class TodoItem(text: String, completed: Boolean) {

    val idProperty = SimpleIntegerProperty( nextId() )
    var id by idProperty
    val textProperty = SimpleStringProperty(text)
    val completedProperty = SimpleBooleanProperty(completed)
    var completed by completedProperty

    companion object {
        private var idgen = 1 // faux static class member
        fun nextId() = idgen++
    }
}

class MainViewController : Controller() {

    private val todoItemModel : MutableList<TodoItem> = mutableListOf()

    //
    // View binds to a model that's always available via inject()
    //
    var todoCursor = SimpleObjectProperty(FXCollections.observableArrayList(todoItemModel))

    enum class FilterType { NO_FILTER, COMPLETED, ACTIVE }

    private var filterApplied = FilterType.NO_FILTER

    fun addItem( item : TodoItem ) {
        todoItemModel.add( item )
        if( filterApplied != FilterType.COMPLETED ) {
            todoCursor.get().add(item)
        }
    }

    fun removeItem( item : TodoItem ) {
        todoItemModel.remove( item )
        todoCursor.get().add( item )
    }

    fun noFilter() {
        filterApplied = FilterType.NO_FILTER
        todoCursor.set( FXCollections.observableArrayList(todoItemModel) )
    }

    fun filterCompleted() {
        filterApplied = FilterType.COMPLETED
        todoCursor.set( FXCollections.observableArrayList(todoItemModel.filter( {itm -> itm.completed } ) ) )
    }

    fun filterActive() {
        filterApplied = FilterType.ACTIVE
        todoCursor.set( FXCollections.observableArrayList(todoItemModel.filter( {itm -> itm.completed.not()} ) ) )
    }
}

class MainView : View() {

    override val root: VBox by fxml("/MainView.fxml")

    val addInput : TextField by fxid()
    val lvItems : ListView<TodoItem> by fxid()
    val selectAll : CheckBox by fxid()
    val itemsLeftLabel : Label by fxid()

    val controller : MainViewController by inject()

    init {

        lvItems.itemsProperty().bind( controller.todoCursor )

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

    fun all() { controller.noFilter() }
    fun active() { controller.filterActive()  }
    fun completed() { controller.filterCompleted() }

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

class TornadoFXApp : App(MainView::class) {
    companion object {
        @JvmStatic
        fun main(args: Array<String>) {
            Application.launch(TornadoFXApp::class.java)
        }
    }
}