package todomvcfx.tornadofx

import javafx.scene.paint.Color
import tornadofx.*

/**¡
 * Created by ronsmits on 24/09/16.
 */
class Styles : Stylesheet() {
    companion object {
        val title by cssid()
        val addItemRoot by cssid()
        val closeIcon by cssclass()
        val contentBox by cssclass()
        val itemRoot by cssclass()
        val completed by cssclass()
        val text by cssclass()
    }

    init {
        title {
            fontSize = 3.em
            textFill = c(175, 47, 47, 0.8)
        }

        checkBox {
            padding = box(0.1.em, 1.em, 0.1.em, 0.2.em)
        }
        addItemRoot {
            padding = box(1.em)
            checkBox {
                padding = box(0.em, 1.em, 0.em, 0.em)
            }
        }
        contentBox {
            button {
                backgroundColor += Color.TRANSPARENT
            }
            itemRoot {
                padding = box(1.em)
            }
            checkBox {
                padding = box(0.em, 1.em, 0.em, 0.em)
            }
            label {
                fontSize = 1.2.em
            }
            completed {
                text {
                    strikethrough = true
                }

            }
            closeIcon {
                fill = c("#cc9a9a")
                add(hover){
                    fill = c("#af5b5e")
                }
            }
        }
    }
}