package com.what3words.addressvalidator.javawrapper.addresslookupservice.loqate.internal

data class LoqateNode(
    val primaryAddress: String,
    val description: String,
    var parentNode: LoqateNode? = null
) {
    // keeps collection of children matched to their primary address
    private val children: HashMap<String, LoqateNode> = hashMapOf()

    // id used to query loqate for more information about address. this value is only required for leaf nodes on our loqate tree
    var id: String = "##"

    val isParent: Boolean
        get() = children.isNotEmpty()

    /** creates a new node, adds it as a child to the current node and returns the newly created node or
    previously created node if node already exists
     **/
    @Synchronized
    fun create(word: String, description: String): LoqateNode {
        if (!children.contains(key = word)) {
            val node =
                LoqateNode(primaryAddress = word, description = description, parentNode = this)
            add(node = node)
        }
        return children[word]!!
    }

    fun add(node: LoqateNode) {
        children[node.primaryAddress] = node
    }

    fun remove(node: LoqateNode) {
        children.remove(node.primaryAddress)
    }

    // check if this node has any child with a primary address that matches word
    fun contains(word: String): Boolean {
        return children.contains(key = word)
    }

    // returns child with primary address that matches [word] or null if such child doesn't exit
    fun getNode(word: String): LoqateNode? {
        return children.get(key = word)
    }

    // returns list of children under this node
    fun children(): List<LoqateNode> {
        return children.values.toList()
    }
}
