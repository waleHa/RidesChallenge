package com.ibm.core

import kotlinx.coroutines.flow.Flow

abstract class BaseUseCase<in Params, T> where T : Any {

    // Abstract function to be implemented by subclasses, returns a Flow of Resource<T>
    abstract suspend fun execute(params: Params): Flow<Resource<T>>

    // This class represents a case where no input parameters are needed for a use case
    object None
}

