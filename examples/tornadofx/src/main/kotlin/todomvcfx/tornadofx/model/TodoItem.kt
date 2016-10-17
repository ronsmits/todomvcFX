package todomvcfx.tornadofx.model

import javafx.beans.property.SimpleBooleanProperty
import javafx.beans.property.SimpleIntegerProperty
import javafx.beans.property.SimpleStringProperty
import tornadofx.getValue
import tornadofx.setValue

/**
 * Created by ronsmits on 24/09/16.
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