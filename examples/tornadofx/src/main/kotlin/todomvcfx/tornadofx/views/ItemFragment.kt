package todomvcfx.tornadofx.views

import javafx.scene.control.Button
import javafx.scene.control.CheckBox
import javafx.scene.control.Label
import javafx.scene.control.TextField
import javafx.scene.layout.HBox
import todomvcfx.tornadofx.controllers.MainViewController
import todomvcfx.tornadofx.model.TodoItem
import tornadofx.Fragment
import tornadofx.bind
import tornadofx.onChange
import tornadofx.toggleClass

class ItemFragment : Fragment() {

    override val root: HBox by fxml("/ItemFragment.fxml")

    val completed : CheckBox by fxid()
    val contentLabel : Label by fxid()
    val deleteButton : Button by fxid()
    val contentBox : HBox by fxid()
    val contentInput : TextField by fxid()

    var item : TodoItem? = null

    val controller : MainViewController by inject()
    val mainView: OrginalMainView by inject()

    init {
        deleteButton.visibleProperty().bind( root.hoverProperty() )
    }

    fun delete() {
        if( item != null ) {
            mainView.lvItems.items.remove( item )
//            controller.removeItem( item!! )
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
