package com.what3words.addressvalidator.javawrapper.addresslookupservice.loqate.internal

class LoqateTreeImpl : LoqateTree {
    private var rootNode = LoqateNode(primaryAddress = "Root", description = "###$$$")

    // addresses represent a collection of all the distinct addresses that have been added into the tree
    private val addresses: HashSet<List<String>> = hashSetOf()

    override val size: Int
        get() = addresses.size


    @Synchronized
    override fun addNodes(wordsList: List<String>, description: String, id: String): Boolean {
        return if (!addresses.contains(wordsList)) {
            addresses.add(wordsList)
            /**
             * Iterate through [wordsList] from the end to the start of the list,
             * adding each word as a child in the word that comes after it (i.e in the list [67 Studio, Alfred Road]. 67 Studio will be added as a child in Alfred Road)
             * **/
            val n = wordsList.size - 1
            var parentNode = rootNode
            for (i in n downTo 0) {
                val word = wordsList[i].trim()
                // if current word starts with a number, remove the number and add the words that occur after the number as a node in the current tree
                // before adding the current word as a child of the newly add node
                if (word.startsWithDigitButEndsWithLetters()) {
                    val wordAfterDigit = getWordsAfterDigit(word = word)
                    parentNode = parentNode.create(
                        word = wordAfterDigit,
                        description = if (parentNode.primaryAddress != rootNode.primaryAddress) "${parentNode.primaryAddress}, ${parentNode.description}" else description
                    )
                    parentNode = parentNode.create(
                        word = word,
                        description = if (parentNode.primaryAddress != rootNode.primaryAddress) parentNode.description else description
                    )
                } else {
                    parentNode = parentNode.create(
                        word = word,
                        description = if (parentNode.primaryAddress != rootNode.primaryAddress) "${parentNode.primaryAddress}, ${parentNode.description}" else description
                    )
                }
                if (i == 0) parentNode.id = id
            }
            true
        } else false
    }

    override fun getChildrenAt(wordsList: List<String>): List<LoqateNode> {
        val n = wordsList.size - 1
        var currentNode: LoqateNode = rootNode
        for (i in n downTo 0) {
            if (currentNode.contains(word = wordsList[i])) {
                currentNode = currentNode.getNode(word = wordsList[i])!!
            } else {
                break
            }
        }
        return currentNode.children()
    }

    override fun getNodeAt(wordsList: List<String>): LoqateNode {
        val n = wordsList.size - 1
        var currentNode: LoqateNode = rootNode
        for (i in n downTo 0) {
            val word = wordsList[i]
            if (currentNode.contains(word = word)) {
                currentNode = currentNode.getNode(word = word)!!
            } else {
                break
            }
        }
        return currentNode
    }

    override fun getRoot(): LoqateNode {
        return rootNode
    }

    override fun prune() {
        // the implementation of this pruning process utilizes a breadth first traversal
        val nodeQueue: ArrayDeque<LoqateNode> = ArrayDeque()
        nodeQueue.addAll(rootNode.children())
        while (nodeQueue.isNotEmpty()) {
            var size = nodeQueue.size
            while (size > 0) {
                val currentNode = nodeQueue.removeFirst()
                if (currentNode.children().size == 1) {
                    val child = currentNode.children().first()
                    val parent = currentNode.parentNode
                    parent?.remove(node = currentNode)
                    parent?.add(node = child)
                    child.parentNode = parent
                    nodeQueue.add(child)
                } else {
                    nodeQueue.addAll(currentNode.children())
                }
                size--
            }
        }
    }

    override fun clear() {
        rootNode = LoqateNode(primaryAddress = "Root", description = "###$$$")
        addresses.clear()
    }

    private fun getWordsAfterDigit(word: String): String {
        var textStartIndex = 0
        while (textStartIndex < word.length && !word[textStartIndex].isLetter()) {
            textStartIndex++
        }
        return word.substring(textStartIndex).trim()
    }

    private fun String.startsWithDigitButEndsWithLetters(): Boolean {
        return this[0].isDigit() && this.length > 3 && this[length - 1].isLetter() && this[length - 2].isLetter()
    }
}