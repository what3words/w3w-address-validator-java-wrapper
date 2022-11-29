package com.what3words.addressvalidator.javawrapper.addresslookupservice.loqate.internal

interface LoqateTree {
    // returns the number of leaf nodes in the tree
    val size: Int

    /**
     * add a list of words as nodes in loqate tree
     * @param wordsList the list of words to be added to the tree
     * @param description description of address from loqate
     * @param id address id, used to query loqate for detailed information about a specific address.
     *           the address id is required to be attached on every leaf node in the tree
     *
     * @return true if nodes were successfully created for each word in wordsList else returns false
     * ***/
    fun addNodes(wordsList: List<String>, description: String, id: String): Boolean

    /**
     * returns the children at the node with tree path that matches wordsList
     * e.g if we have two address list [Flat 8, Hartford House, 35 Tavistock Crescent] and [Flat 9, Hartford House, 35 Tavistock Crescent] in our tree
     *     and the [wordsList] to this function is [Hartford House, 35 Tavistock Crescent],
     *     the expected result should be a list of the children of Hartford House in the tree. The children are (Flat 8, Flat 9)
     * **/
    fun getChildrenAt(wordsList: List<String>): List<LoqateNode>

    /**
     * returns the node with path that matches [wordsList]
     * **/
    fun getNodeAt(wordsList: List<String>): LoqateNode

    /**
     * returns root of the tree
     * @return [LoqateNode] root of the tree
     * **/
    fun getRoot(): LoqateNode

    /**
     * process the tree by squashing nodes with one child with that child to reduce the hierarchy of the tree
     * */
    fun prune()

    /**
     * Removes all of the node from this tree.
     * The tree will be empty after this call returns.
     */
    fun clear()

}


