package todomvcfx.tornadofx.views

import javafx.scene.control.Button
import javafx.scene.control.CheckBox
import javafx.scene.control.Label
import javafx.scene.control.TextField
import javafx.scene.layout.HBox
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
    val mainView: MainView by inject()

    init {
        deleteButton.visibleProperty().bind( root.hoverProperty() )
    }

    fun delete() {
        if( item != null ) {
            model.remove( item!! )
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
