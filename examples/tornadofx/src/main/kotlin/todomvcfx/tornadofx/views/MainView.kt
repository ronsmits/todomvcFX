package todomvcfx.tornadofx.views

import javafx.beans.binding.Bindings
import javafx.beans.binding.When
import javafx.beans.property.SimpleObjectProperty
import javafx.beans.value.ChangeListener
import javafx.scene.control.*
import javafx.scene.layout.VBox
import todomvcfx.tornadofx.controllers.MainViewController
import todomvcfx.tornadofx.model.TodoItem
import tornadofx.View
import tornadofx.cellFragment
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

    val controller : MainViewController by inject()

    val selectAllListener = ChangeListener<Boolean> {
        obs, ob, nv ->
        lvItems.items.forEach { itm ->
            itm.completed = nv  // also sets model b/c of reference
        }
    }

    init {

        lvItems.itemsProperty().bind( controller.viewableItemsProperty )

        controller.filterByProperty.bind(

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
            controller.addItem( newItem )
            addInput.clear()

            selectAll.selectedProperty().removeListener( selectAllListener )
            selectAll.isSelected = false
            selectAll.selectedProperty().addListener( selectAllListener )

            if( stateGroup.selectedToggle.equals(showCompleted ) ) {
                stateGroup.selectToggle(showActive)
            }
        }

        lvItems.cellFragment(TodoItemFragment::class)

        selectAll.visibleProperty().bind(
                Bindings.size(
                        controller.viewableItemsProperty.get()
                ).greaterThan(0)
        )

        selectAll.selectedProperty().addListener( selectAllListener )

        itemsLeftLabel.textProperty().bind(
                Bindings.concat(controller.numActiveItemsProperty.asString(), " items left")
        )
    }
}
