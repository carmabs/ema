package com.carmabs.emax.middleware.log

import com.carmabs.ema.core.action.EmaAction
import com.carmabs.ema.core.constants.INT_ZERO
import com.carmabs.ema.core.logging.toStringPretty
import com.carmabs.ema.core.state.EmaDataState
import com.carmabs.emax.middleware.common.EmaNextMiddleware
import com.carmabs.emax.middleware.common.EmaNextMiddlewareResult
import com.carmabs.emax.middleware.common.EmaxMiddleware
import com.carmabs.emax.middleware.common.MiddlewareScope
import java.util.logging.Logger
import kotlin.math.absoluteValue

/**
 * Created by Carlos Mateo Benito on 2/10/23.
 *
 * <p>
 * Copyright (c) 2023 by Carmabs. All rights reserved.
 * </p>
 *
 * @author <a href=“mailto:apps.carmabs@gmail.com”>Carlos Mateo Benito</a>
 */
class LoggerEmaxMiddleware<S : EmaDataState>(
    private val lineLength: Int = 80
) : EmaxMiddleware<S> {
    context(MiddlewareScope<S>)
    override fun invoke(
        action: EmaAction,
        next: EmaNextMiddleware
    ): EmaNextMiddlewareResult {
        val padding = "    "
        val nextFunction = Logger.getLogger("EMA").run {
            info("")
            info(printLine('*', "STARTING EMA STATE LOGGING", textPadding = padding))
            info(printLine('*', "", sideCharacterLimit = 2))
            info(printLine('|', "Dispatched action", textPadding = padding))
            info(printLine('|', "", sideCharacterLimit = 2))
            action.toStringPretty().lines().forEach {
                info(
                    printLine(
                        '|',
                        it,
                        sideCharacterLimit = 2,
                        alignment = Alignment.Start,
                        textPadding = padding
                    )
                )
            }
            info(printLine('|', "State before apply action", textPadding = padding))
            info(printLine('|', "", sideCharacterLimit = 2))
            state.toStringPretty().lines().forEach {
                info(
                    printLine(
                        '|',
                        it,
                        sideCharacterLimit = 2,
                        alignment = Alignment.Start,
                        textPadding = padding
                    )
                )
            }
            val nextFunction = next.invoke(action)
            info(printLine('|', "State after apply action", textPadding = padding))
            info(printLine('|', "", sideCharacterLimit = 2))
            state.toStringPretty().lines().forEach {
                info(
                    printLine(
                        '|',
                        it,
                        sideCharacterLimit = 2,
                        alignment = Alignment.Start,
                        textPadding = padding
                    )
                )
            }
            info(printLine('|', ""))
            info(printLine('*', "", sideCharacterLimit = 2))
            info(printLine('*', "FINISHED EMA STATE LOGGING", textPadding = padding))
            info("")
            nextFunction
        }
        return nextFunction
    }


    private fun printLine(
        character: Char,
        text: String,
        length: Int = lineLength,
        sideCharacterLimit: Int? = null,
        alignment: Alignment = Alignment.Center,
        textPadding: String = ""
    ): String {
        when (alignment) {
            Alignment.Start -> {
                val sideStart = sideCharacterLimit?.let {
                    String(charArrayOf(character)).repeat(it)
                } ?: ""
                val endLength =
                    (length - sideStart.length - text.length - textPadding.length * 2).coerceAtLeast(
                        0
                    )
                val sideEnd = (sideCharacterLimit?.let {
                    String(charArrayOf(character)).repeat(it) + String(charArrayOf(' ')).repeat(
                        (endLength - it).coerceAtLeast(0)
                    )
                } ?: String(charArrayOf(character))
                    .repeat(endLength)

                        ).reversed()
                return sideStart + textPadding + text + textPadding + sideEnd
            }

            Alignment.Center -> {
                val sideLength = (length - textPadding.length * 2 - text.length) / 2
                val sideString = (sideCharacterLimit?.let {
                    String(charArrayOf(character)).repeat(it) + String(charArrayOf(' ')).repeat(
                        (sideLength - it).coerceAtLeast(0)
                    )
                } ?: String(charArrayOf(character)).repeat(sideLength))
                return (sideString + textPadding + text + textPadding + sideString.reversed()).let {
                    val remainingCharacters = (lineLength-it.length)
                    when{
                        remainingCharacters>0->{
                            it + String(charArrayOf(character)).repeat(remainingCharacters)
                        }
                        remainingCharacters<0->{
                            it.dropLast(remainingCharacters.absoluteValue)
                        }
                        else->{
                            it
                        }

                    }
                }
            }

            Alignment.End -> {
                val sideEnd = (sideCharacterLimit?.let {
                    String(charArrayOf(character)).repeat(it)
                } ?: "")
                val startLength =
                    (length - sideEnd.length - text.length - textPadding.length * 2).coerceAtLeast(
                        0
                    )
                val sideStart = (sideCharacterLimit?.let {
                    String(charArrayOf(character)).repeat(it) + String(charArrayOf(' ')).repeat(
                        startLength - it
                    )
                } ?: String(charArrayOf(character)).repeat(startLength)
                        )
                return sideStart + textPadding + text + textPadding + sideEnd
            }
        }

    }


    private enum class Alignment {
        Start,
        Center,
        End
    }
}