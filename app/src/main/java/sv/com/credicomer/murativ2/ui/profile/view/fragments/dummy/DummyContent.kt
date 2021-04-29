package sv.com.credicomer.murativ2.ui.profile.view.fragments.dummy

import sv.com.credicomer.murativ2.ui.profile.model.UserCarnet
import java.util.ArrayList
import java.util.HashMap

/**
 * Helper class for providing sample content for user interfaces created by
 * Android template wizards.
 *
 * TODO: Replace all uses of this class before publishing your app.
 */
object DummyContent {


    val ITEMS: MutableList<UserCarnet> = ArrayList()

    val ITEM_MAP: MutableMap<String, UserCarnet> = HashMap()

    private val COUNT = 25

    init {

        for (i in 1..COUNT) {
            addItem(createDummyItem(i))
        }
    }

    private fun addItem(item: UserCarnet) {
        ITEMS.add(item)
        ITEM_MAP.put(item.name!!, item)
    }

    private fun createDummyItem(position: Int): UserCarnet {
        return UserCarnet(name = "nombre colaborador", email = "nombre_colaborador@credicomer.com.sv")
    }

    private fun makeDetails(position: Int): String {
        val builder = StringBuilder()
        builder.append("Details about Item: ").append(position)
        for (i in 0..position - 1) {
            builder.append("\nMore details information here.")
        }
        return builder.toString()
    }

    data class DummyItem(val id: String, val content: String, val details: String) {
        override fun toString(): String = content
    }
}