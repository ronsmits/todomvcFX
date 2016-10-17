package todomvcfx.tornadofx.views

import javafx.beans.binding.Bindings
import javafx.beans.binding.When
import javafx.beans.property.SimpleObjectProperty
import javafx.beans.value.ChangeListener
import javafx.scene.control.*
import javafx.scene.layout.VBox
import todomvcfx.tornadofx.model.TodoItem
import todomvcfx.tornadofx.model.TodoItemModel
import tornadofx.View
import tornadofx.cellCache
import tornadofx.find
import java.util.function.Predicate

/**
 * View component for the main UI
 *
 * Created by ronsmits on 24/09/16.
 */
class MainView : View() {

    override val root: VBox by fxml("/MainView.fxml")

    val addInput : TextField by fxid()
    val lvItems : ListView<TodoItem> by fxid()
    val selectAll : CheckBox by fxid()
    val itemsLeftLabel : Label by fxid()
    val stateGroup : ToggleGroup by fxid()
    val showActive : ToggleButton by fxid()
    val showCompleted : ToggleButton by fxid()

    val model : TodoItemModel by inject()

    val selectAllListener = ChangeListener<Boolean> {
        obs, ob, nv ->
        lvItems.items.forEach { itm ->
            itm.completed = nv  // also sets model b/c of reference
        }
    }

    init {

        lvItems.itemsProperty().bind( model.viewableItemsProperty )

        model.filterByProperty.bind(

                When(stateGroup.selectedToggleProperty().isEqualTo(showActive))
                        .then( SimpleObjectProperty<Predicate<TodoItem>>( Predicate<TodoItem>( {tdi -> tdi.completed.not()} ) ) )
                        .otherwise(
                                When(stateGroup.selectedToggleProperty().isEqualTo(showCompleted))
                                        .then( SimpleObjectProperty<Predicate<TodoItem>>( Predicate<TodoItem>( {tdi -> tdi.completed} ) ) )
                                        .otherwise( SimpleObjectProperty<Predicate<TodoItem>>( Predicate<TodoItem>( {tdi -> true} ) ))
                        )

        )

        addInput.setOnAction {

            val newItem = TodoItem(addInput.text, false)
            model.add( newItem )
            addInput.clear()

            selectAll.selectedProperty().removeListener( selectAllListener )
            selectAll.isSelected = false
            selectAll.selectedProperty().addListener( selectAllListener )

            if( stateGroup.selectedToggle == showCompleted ) {
                stateGroup.selectToggle(showActive)
            }
        }

        lvItems.cellCache {
            val itemFragment = find(ItemFragment::class)  // prototype
            itemFragment.load( it )
            itemFragment.root
        }

        selectAll.visibleProperty().bind(
                Bindings.size(
                        model.viewableItemsProperty.get()
                ).greaterThan(0)
        )

        selectAll.selectedProperty().addListener( selectAllListener )

        itemsLeftLabel.textProperty().bind(
                Bindings.concat(model.numActiveItemsProperty.asString(), " items left")
        )
    }
}
