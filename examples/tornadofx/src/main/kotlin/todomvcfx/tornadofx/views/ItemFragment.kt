package todomvcfx.tornadofx.views

import javafx.scene.control.Button
import javafx.scene.control.CheckBox
import javafx.scene.control.Label
import javafx.scene.control.TextField
import javafx.scene.layout.HBox
import todomvcfx.tornadofx.Styles
import todomvcfx.tornadofx.model.TodoItem
import todomvcfx.tornadofx.model.TodoItemModel
import tornadofx.Fragment
import tornadofx.bind
import tornadofx.onChange
import tornadofx.toggleClass

/**
 * View component representing a row in the TodoItem ListView
 *
 */
class ItemFragment : Fragment() {

    override val root: HBox by fxml("/ItemFragment.fxml")

    val completed : CheckBox by fxid()
    val contentLabel : Label by fxid()
    val deleteButton : Button by fxid()
    val contentBox : HBox by fxid()
    val contentInput : TextField by fxid()

    var item : TodoItem? = null

    val model : TodoItemModel by inject()

    init {
        deleteButton.visibleProperty().bind( root.hoverProperty() )
    }

    fun delete() {  // in use; called from .fxml
        if( item != null ) {
            model.remove( item!! )
        }
    }

    fun load(item : TodoItem) {

        this.item = item

        // init checkbox
        completed.bind( item.completedProperty )

        completed.selectedProperty().onChange {
            nv ->
            if( nv ) {
                model.completeAnItem()
            } else {
                model.reactivateItem()
            }
        }

        // init read-only view label
        contentLabel.toggleClass(Styles.strikethrough, completed.selectedProperty())

        contentLabel.textProperty().bind( item.textProperty )

        contentLabel.setOnMouseClicked { event ->
            if (event.clickCount > 1) {
                toggleEditMode(true)
            }
        }

        // init edit textfield
        contentInput.textProperty().bindBidirectional( item.textProperty )

        contentInput.setOnAction {
            toggleEditMode(false)
        }

        contentInput.focusedProperty().onChange {
            newValue ->
                if (!(newValue)) {
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
