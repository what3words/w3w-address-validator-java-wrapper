package loqate.internal


import com.what3words.addressvalidator.javawrapper.addresslookupservice.loqate.internal.LoqateNode
import com.what3words.addressvalidator.javawrapper.addresslookupservice.loqate.internal.LoqateTree
import com.what3words.addressvalidator.javawrapper.addresslookupservice.loqate.internal.LoqateTreeImpl
import org.junit.Before
import org.junit.Test

import org.junit.Assert.*


private data class LoqateItem(
    val wordList: List<String>,
    val description: String,
    val id: String = ""
)

class LoqateTreeImplTest {
    lateinit var loqateTree: LoqateTree

    @Before
    fun setUp() {
        loqateTree = LoqateTreeImpl()
        val items: List<LoqateItem> = listOf(
            LoqateItem(
                wordList = listOf("Stephen Fletcher Architects", " 65 Alfred Road"),
                description = "London, W2 5EU"
            ),
            LoqateItem(
                wordList = listOf("Great Western Studios", "65 Alfred Road"),
                description = "London, W2 5EU"
            ),
            LoqateItem(
                wordList = listOf("The Westbourne Drink Co Ltd", "65 Alfred Road"),
                description = "London, W2 5EU"
            ),
            LoqateItem(
                wordList = listOf("Flat 29", "Oversley House", "Alfred Road"),
                description = "London, W2 5HE"
            )
        )

        items.forEach { loqateItem: LoqateItem ->
            loqateTree.addNodes(
                wordsList = loqateItem.wordList,
                description = loqateItem.description,
                id = loqateItem.id
            )
        }
    }

    @Test
    fun addNodes() {
        val previousChildrenCount = loqateTree.size
        val newLoqateItem = LoqateItem(
            wordList = listOf("Flat 61", "Oversley House", "Alfred Road"),
            description = "London, W2 5HF",
            id = "GB|RM|A|25778791"
        )
        val addResult = loqateTree.addNodes(
            wordsList = newLoqateItem.wordList,
            description = newLoqateItem.description,
            id = newLoqateItem.id
        )
        assertEquals(true, addResult)
        assertEquals(previousChildrenCount + 1, loqateTree.size)
        val addResult2 = loqateTree.addNodes(
            wordsList = newLoqateItem.wordList,
            description = newLoqateItem.description,
            id = newLoqateItem.id
        )
        assertEquals(false, addResult2)
        assertEquals(previousChildrenCount + 1, loqateTree.size)
    }

    @Test
    fun getChildrenAt() {
        val newLoqateItem = LoqateItem(
            wordList = listOf("Flat 61", "Oversley House", "Alfred Road"),
            description = "London, W2 5HF",
            id = "GB|RM|A|25778791"
        )
        loqateTree.addNodes(
            wordsList = newLoqateItem.wordList,
            description = newLoqateItem.description,
            id = newLoqateItem.id
        )
        val children: List<LoqateNode> =
            loqateTree.getChildrenAt(listOf("Oversley House", "Alfred Road"))
        assertEquals(2, children.size)
    }

    @Test
    fun getRoot() {
        assertEquals(1, loqateTree.getRoot().children().size)
    }

    @Test
    fun clear() {
        loqateTree.clear()
        assertEquals(0, loqateTree.size)
    }
}