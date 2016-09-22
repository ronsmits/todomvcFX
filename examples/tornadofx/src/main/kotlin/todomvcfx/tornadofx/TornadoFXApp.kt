package todomvcfx.tornadofx

import javafx.application.Application
import javafx.beans.binding.When
import javafx.beans.property.SimpleBooleanProperty
import javafx.beans.property.SimpleIntegerProperty
import javafx.beans.property.SimpleStringProperty
import javafx.beans.value.ChangeListener
import javafx.collections.ListChangeListener
import javafx.event.EventHandler
import javafx.scene.Parent
import javafx.scene.control.*
import javafx.scene.control.cell.TextFieldListCell
import javafx.scene.layout.HBox
import javafx.scene.layout.VBox
import javafx.util.Callback
import tornadofx.*

/**
 *
 * @author carl
 */

class TodoItem(text: String, completed: Boolean) {

    val id = SimpleIntegerProperty( nextId() )
    val text = SimpleStringProperty(text)
    val completed = SimpleBooleanProperty(completed)

    fun idProperty() = id

    fun textProperty() = text

    fun completedProperty() = completed

    companion object {
        var idgen = 1 // faux static class member
        fun nextId() = idgen++
    }
}

class MainView : View() {

    override val root: VBox by fxml("/MainView.fxml")

    val addInput : TextField by fxid()
    val items : ListView<TodoItem> by fxid()
    val selectAll : CheckBox by fxid()
    val itemsLeftLabel : Label by fxid()

    init {

        addInput.onAction = EventHandler{
            event ->
                val newItem = TodoItem( addInput.text, false )
                items.items.add( newItem )
                addInput.clear()
                selectAll.setSelected( false )
        }

        items.cellFactory = Callback{
            lv ->
                val cell = TodoItemListCell()
                cell
        }

        // switch this to binding
        selectAll.setVisible(false)
        items.items.addListener( TodoItemListChangeListener() )

        selectAll.selectedProperty().addListener(
                 {
                obs,ov,nv ->
                        items.items.forEach { itm ->
                            itm.completed.set( nv )
                        }
            })

        itemsLeftLabel.text = "0 items left"

    }

    //
    // Will need to introduce a tfx Controller() w. a model to support a divergence between the data (all items) and
    // what may be shown in the ListView (only completeds, only actives)
    //
    fun all() {}  // TODO: implement
    fun active() {}  // TODO: implement
    fun completed() {}  // TODO: implement

    inner class TodoItemListChangeListener : ListChangeListener<TodoItem> {
        override fun onChanged(c: ListChangeListener.Change<out TodoItem>?) {
            selectAll.setVisible( items.items.isEmpty().not() )
            updateItemsLeftLabel()
        }
    }

    public fun updateItemsLeftLabel() {
        val numActives = items.items.filter({ itm -> !itm.completed.get() }).count()
        itemsLeftLabel.text = numActives.toString() + " items left"
    }
}

class TodoItemListCell : ListCell<TodoItem>() {

    override fun updateItem(item: TodoItem?, empty: Boolean) {
        super.updateItem(item, empty)
        if( empty || item == null) {

            setText(null)
            setGraphic(null)

        } else {

            setText( null )
            setGraphic( readCache(item).root )
         }
    }

    companion object {

        // TODO: clean up deletions from cache after removal from UI
        var cellCache : MutableMap<Int, ItemFragment> = mutableMapOf()

        fun readCache(item : TodoItem) : ItemFragment {

            val id = item.id.get()

            if( !cellCache.containsKey(id) ) {

                val itemFragment = find(ItemFragment::class)  // prototype
                itemFragment.load( item )
                cellCache.put( id, itemFragment )
            }

            return cellCache.get(id)!!
        }
    }
}

class ItemFragment : Fragment() {

    override val root: HBox by fxml("/ItemFragment.fxml")

    val completed : CheckBox by fxid()
    val contentLabel : Label by fxid()  // TODO: switch over to edit mode to update text
    val deleteButton : Button by fxid()
    val contentBox : HBox by fxid()
    val contentInput : TextField by fxid()

    var item : TodoItem? = null

    init {
        deleteButton.visibleProperty().bind( root.hoverProperty() );

    }

    fun delete() {
        if( item != null ) {
            val mv : MainView = find(MainView::class)
            mv.items.items.remove( item )
        }
    }


    fun load(item : TodoItem) {

        this.item = item

        completed.bind( item.completedProperty() )

        val mv : MainView = find(MainView::class)

        item.completed.addListener(
                ChangeListener {
                    obs,ov,nv ->
                        if( nv ) {
                            if( !contentLabel.styleClass.contains( "strikethrough") ) {
                                contentLabel.styleClass.add( "strikethrough" )
                            }
                        } else {
                            if( contentLabel.styleClass.contains("strikethrough") ) {
                                contentLabel.styleClass.remove("strikethrough")
                            }
                        }
                        mv.updateItemsLeftLabel()
                })
        contentLabel.textProperty().bind( item.textProperty() )
        contentInput.textProperty().bindBidirectional( item.textProperty() )

        contentLabel.setOnMouseClicked { event ->
            if (event.clickCount > 1) {
                toggleEditMode(true)
            }
        }

        contentInput.setOnAction {
            toggleEditMode(false)
        }

        contentInput.focusedProperty().addListener { observable, oldValue, newValue ->
            if (!newValue) {
                toggleEditMode(false)
            }
        }
    }

    fun toggleEditMode(edit : Boolean) {
        contentInput.setVisible(edit)
        if( edit ) {
            contentInput.requestFocus()
        }
        contentBox.setVisible(!edit)
        completed.setVisible(!edit)
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