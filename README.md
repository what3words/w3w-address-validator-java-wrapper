# <img valign='top' src="https://what3words.com/assets/images/w3w_square_red.png" width="32" height="32" alt="what3words">&nbsp;w3w-address-validator-java-wrapper

Overview
--------

This JVM library contains code for accessing various address validation services. The services currently supported are:

* Swift Complete - [https://www.swiftcomplete.com](https://www.loqate.com/)
* Data 8 - [https://www.data-8.co.uk](https://www.loqate.com/)
* Loqate - [https://www.loqate.com/](https://www.loqate.com/)

The library provides a unified protocol for these services via an interface called `W3WAddressValidatorInterface`.
We provide a default implementation of this interface called `What3WordsAddressValidatorV1`, that makes it easy to
switch between any of the supported address validation services.

#### Example

A sample app that utilizes this library can be
found [here](https://github.com/what3words/w3w-android-address-validator-components/tree/main/mobile-demo-app).

If you don't want to do all the UI work, we recommend that you use our Android Address Validator component which is
available for Wear OS and
Mobile: [https://github.com/what3words/w3w-android-address-validator-components](https://github.com/what3words/w3w-android-address-validator-components)

### What3WordsAddressValidatorV1

`search(near: String)` returns address nodes near a
three word address

`list(node: W3WAddressValidatorParentNode)`  given a
node returned from a previous call, get any child nodes

`info(node: W3WAddressValidatorLeafNode)` get detailed info for a
particular leaf node returned from a previous call

#### Address Tree

The three functions `search`, `list`, and `info` provide access to a hierarchical tree of address data.

The tree is made of up nodes derived from `W3WAddressValidatorNode`. These are: `W3WAddressValidatorLeafNode`
, `W3WValidatorParentNode`, and `W3WAddressValidatorRootNode`.

Typically, you start with by calling `search` with a three word address, and it returns a root
node (`W3WAddressValidatorRootNode`) with a list of nodes near to that
address. You then use that list to present options to your user. Once a user chooses one of those nodes, you take that
node and call `list` to get sub nodes in the tree. Present those to your user and so on.

The leaf node is `W3WAddressValidatorLeafNode`, but these can be used to
call `info(node: W3WAddressValidatorLeafNode): Either<W3WAddressValidatorError, W3WStreetAddress>`
which will return to you a `W3WStreetAddress`.

**Important**: This `info()` call is the call that most services count towards a quota.

#### Retrieving a street address

`W3WStreetAddress` is a sealed class that has three implementations (`W3WStreetAddressData8`, `W3WStreetAddressLoqate`,
and `W3WStreetAddressSwiftComplete`) that corresponds to the final address info returned by each of the support address
validation service.

Services
--------

### In Kotlin

#### Data 8

```Kotlin
val data8 =
    What3WordsAddressValidatorV1(addressLookUpService = AddressLookUpService.Data8(apiKey = "YOUR_DATA_8_API_KEY"))
```

#### Loqate

```Kotlin
val loqate =
    What3WordsAddressValidatorV1(addressLookUpService = AddressLookUpService.Loqate(apiKey = "YOUR_LOQATE_API_KEY"))
```

#### Swift Complete

```Kotlin
val swiftComplete =
    What3WordsAddressValidatorV1(addressLookUpService = AddressLookUpService.SwiftComplete(apiKey = "YOUR_SWIFT_COMPLETE_API_KEY"))
```

### In Java

The `What3WordsAddressValidatorV1` class cannot be used directly in a Java code because it's functions are annotated
with the Kotlin coroutines suspend keyword. Therefore, we created a
utility class called `What3WordsAddressValidatorJavaWrapper` that transforms the suspend functions in
the `What3WordsAddressValidator` to plain functions that returns CompletableFutures.

#### Data 8

```Java
class MyViewModel() {
    What3WordsAddressValidatorJavaWrapper wrapper = new What3WordsAddressValidatorJavaWrapper(new AddressLookUpService.Data8("YOUR_DATA_8_API_KEY"));
}
```

#### Loqate

```Java
class MyViewModel() {
    What3WordsAddressValidatorJavaWrapper wrapper = new What3WordsAddressValidatorJavaWrapper(new AddressLookUpService.Loqate("YOUR_LOQATE_KEY"));
}
```

#### SwiftComplete

```Java
class MyViewModel() {
    What3WordsAddressValidatorJavaWrapper wrapper = new What3WordsAddressValidatorJavaWrapper(new AddressLookUpService.SwiftComplete("YOUR_SWIFT_COMPLETE_KEY"));
}
```

#### In Android Project 
Add the following ProGuard rules
```
-keep class com.what3words.addressvalidator.javawrapper.addresslookupservice.loqate.network.dto.** {*; }
-keep class com.what3words.addressvalidator.javawrapper.addresslookupservice.swiftcomplete.network.dto.** {*;}
-keep class com.what3words.addressvalidator.javawrapper.addresslookupservice.data8.network.dto.** {*;}
```

Interface definition
-------------------

All the services above conform to `W3WAddressValidatorInterface` which is defined as follows:

#### W3WAddressValidatorInterface

```Kotlin
interface What3WordsAddressValidator {
    /**
     * searches near a three word address
     * @param near: the three word address to search near
     * @return Either [W3WAddressValidatorRootNode] if the call was successful or [W3WAddressValidatorError] in the event of a failure
     **/
    suspend fun search(near: String): Either<W3WAddressValidatorError, W3WAddressValidatorRootNode>


    /**
     * returns children (or sub addresses) under [node]
     * @param node to fetch its children
     * @return Either [List<[W3WAddressValidatorNode]>] if call was successful or [W3WAddressValidatorError] when list operation fails
     * **/
    suspend fun list(node: W3WAddressValidatorParentNode): Either<W3WAddressValidatorError, List<W3WAddressValidatorNode>>

    /**
     * returns the [W3WStreetAddress] for a leaf node
     * @param node the node to make the detail/info call on
     **/
    suspend fun info(
        node: W3WAddressValidatorLeafNode
    ): Either<W3WAddressValidatorError, W3WStreetAddress>
}
```

