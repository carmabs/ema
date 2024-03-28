package com.carmabs.ema.core.usecase

/**
 * Base class to handle every use case.
 *
 * All the logic associated to data retrieving must be done inside an use case.
 *
 * @author <a href=“mailto:apps.carmabs@gmail.com”>Carlos Mateo</a>
 */

/**
 * @param I Input. Must be the model object that the use case can use to make the request
 * @param O Output.Must be the model object that the use case must return
 */

interface UseCase<I, O> {

    /**
     * Executes a function inside a background thread by async way
     * @return the object with the return value
     */
    suspend operator fun invoke(input: I): O

    /**
     * Executes a function inside blocking the thread until
     * the result is delivered
     * @return the object with the return value
     */
    fun executeBlocking(input: I): O

    /**
     * Executes a function inside the current thread by dispatcher blocking the thread until
     * the result is delivered
     * @return the object with the return value
     */
    fun executeBlockingInCurrentThread(input: I): O
}