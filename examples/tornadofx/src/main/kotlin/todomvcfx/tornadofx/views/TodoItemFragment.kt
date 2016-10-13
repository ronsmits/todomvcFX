package todomvcfx.tornadofx.views

import javafx.scene.control.Button
import javafx.scene.control.CheckBox
import javafx.scene.control.Label
import javafx.scene.control.TextField
import javafx.scene.layout.HBox
import todomvcfx.tornadofx.Styles
import todomvcfx.tornadofx.controllers.MainViewController
import todomvcfx.tornadofx.model.TodoItem
import tornadofx.ListCellFragment
import tornadofx.bind
import tornadofx.onChange
import tornadofx.toggleClass

/**
 * View component representing a row in the TodoItem ListView
 *
 */
class TodoItemFragment : ListCellFragment<TodoItem>() {
    override val root: HBox by fxml("/ItemFragment.fxml")

    val completed: CheckBox by fxid()
    val contentLabel: Label by fxid()
    val deleteButton: Button by fxid()
    val contentBox: HBox by fxid()
    val contentInput: TextField by fxid()
    val controller: MainViewController by inject()

    init {

        println("creating fragment ${this?.hashCode()}")

        deleteButton.visibleProperty().bind(root.hoverProperty())

        contentLabel.setOnMouseClicked { event ->
            if (event.clickCount > 1) {
                toggleEditMode(true)
            }
        }

        contentInput.setOnAction {
            toggleEditMode(false)
        }

        contentInput.focusedProperty().onChange { focused ->
            if (!focused) {
                toggleEditMode(false)
            }
        }

        contentLabel.toggleClass(Styles.strikethrough, completed.selectedProperty())

        completed.selectedProperty().onChange {
            nv ->
                if( nv ) {
                    controller.completeAnItem()
                }
        }

        itemProperty.addListener {

            obs, ov, nv ->

                println("item property changing for fragment ${this?.item?.hashCode()} ov=${ov?.id} nv=${nv?.id}")

                if (ov != null) {

                    completed.selectedProperty().unbindBidirectional(ov.completedProperty)
                    contentInput.textProperty().unbindBidirectional(ov.textProperty)
                    contentLabel.textProperty().unbind()
                }
                completed.bind(item.completedProperty)
                contentInput.bind(item.textProperty)
                contentLabel.bind(item.textProperty, readonly = true)
        }
    }

    fun delete() {
        if (item != null) {
            controller.removeItem(item)
        }
    }

    fun toggleEditMode(edit: Boolean) {
        contentInput.isVisible = edit
        contentBox.isVisible = !edit
        completed.isVisible = !edit
        if (edit) {
            contentInput.requestFocus()
        }
    }
}
