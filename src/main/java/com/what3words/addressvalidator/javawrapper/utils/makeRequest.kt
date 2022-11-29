package com.what3words.addressvalidator.javawrapper.utils

import com.what3words.addressvalidator.javawrapper.model.error.W3WAddressValidatorError


suspend fun <T> makeRequest(request: suspend () -> T): Either<W3WAddressValidatorError, T> {
    return try {
        Either.Right(request())
    } catch (exception: Exception) {
        Either.Left(W3WAddressValidatorError(exception.message ?: ""))
    }
}