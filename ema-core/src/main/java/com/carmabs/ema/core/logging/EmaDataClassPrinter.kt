package com.carmabs.ema.core.logging

import java.lang.reflect.Field
import java.lang.reflect.Modifier
import java.text.BreakIterator
import java.util.UUID

/**
 * Created by Carlos Mateo Benito on 5/10/23.
 *
 * <p>
 * Copyright (c) 2023 by Carmabs. All rights reserved.
 * </p>
 *
 * @author <a href=“mailto:apps.carmabs@gmail.com”>Carlos Mateo Benito</a>
 */
/**
 * Pretty print function.
 *
 * Prints any object in a pretty format for easy debugging/reading
 *
 * @param [obj] the object to pretty print
 * @param [indent] optional param that specifies the number of spaces to use to indent. Defaults to 2.
 * @param [writeTo] optional param that specifies the [Appendable] to output the pretty print to. Defaults appending to `System.out`.
 * @param [wrappedLineWidth] optional param that specifies how many characters of a string should be on a line.
 */
@JvmOverloads
fun formatToStringPretty(
    obj: Any?,
    indent: Int = 2,
    wrappedLineWidth: Int = 80
): String = PrettyPrinter(indent, wrappedLineWidth).serializeToStringPretty(obj)

/**
 * Inline helper method for printing withing method chains. Simply delegates to [pp]
 *
 * Example:
 *   val foo = op2(op1(bar).pp())
 *
 * @param [T] the object to pretty print
 * @param [indent] optional param that specifies the number of spaces to use to indent. Defaults to 2.
 * @param [writeTo] optional param that specifies the [Appendable] to output the pretty print to. Defaults appending to `System.out`
 * @param [wrappedLineWidth] optional param that specifies how many characters of a string should be on a line.
 */
@JvmOverloads
fun <T> T.toStringPretty(
    indent: Int = 2,
    wrappedLineWidth: Int = 80
): String = formatToStringPretty(this, indent, wrappedLineWidth)


/**
 * Class for performing pretty print operations on any object with customized indentation, target output, and line wrapping
 * width for long strings.
 *
 * @param [tabSize] How much more to indent each level of nesting.
 * @param [writeTo] Where to write a pretty printed object.
 * @param [wrappedLineWidth] How long a String needs to be before it gets transformed into a multiline String.
 */
private class PrettyPrinter(
    private val tabSize: Int,
    private val wrappedLineWidth: Int
) {
    private val lineInstance = BreakIterator.getLineInstance()

    //    private val logger = KotlinLogging.logger {}
    private val visited = mutableSetOf<Int>()
    private val revisited = mutableSetOf<Int>()

    /**
     * Pretty prints the given object with this printer.
     *
     * @param [obj] The object to pretty print.
     */
    fun serializeToStringPretty(obj: Any?): String {
        val resultString = ppAny(obj)
        return resultString + writeLine()
    }

    /**
     * The core pretty print method. Delegates to the appropriate pretty print method based on the object's type. Handles
     * cyclic references. `collectionElementPad` and `objectFieldPad` are generally the same. A specific case in which they
     * differ is to handle the difference in alignment of different types of fields in an object, as seen in `ppPlainObject(...)`.
     *
     * @param [obj] The object to pretty print.
     * @param [collectionElementPad] How much to indent the elements of a collection.
     * @param [objectFieldPad] How much to indent the field of an object.
     */
    private fun ppAny(
        obj: Any?,
        collectionElementPad: String = "",
        objectFieldPad: String = collectionElementPad,
        staticMatchesParent: Boolean = false
    ): String {
        var resultString = ""

        val id = System.identityHashCode(obj)

        if (obj != null && staticMatchesParent) {
            val className = obj.javaClass.simpleName
            return write("$className.<static cyclic class reference>")
        }

        if (!obj.isAtomic() && visited[id]) {
            resultString = write("cyclic reference detected for $id")
            revisited.add(id)
            return resultString
        }

        visited.add(id)
        resultString += when {
            obj is Iterable<*> -> ppIterable(obj, collectionElementPad)
            obj is Map<*, *> -> ppMap(obj, collectionElementPad)
            obj is String -> ppString(obj, collectionElementPad)
            obj is Enum<*> -> ppEnum(obj)
            obj.isAtomic() -> ppAtomic(obj)
            obj is Any -> ppPlainObject(obj, objectFieldPad)
            else -> {
                ""
            }
        }
        visited.remove(id)

        if (revisited[id]) {
            resultString += write("[\$id=$id]")
            revisited -= id
        }

        return resultString
    }

    /**
     * Pretty prints the contents of the Iterable receiver. The given function is applied to each element. The result
     * of an application to each element is on its own line, separated by a separator. `currentDepth` specifies the
     * indentation level of any closing bracket.
     */
    private fun <T> Iterable<T>.ppContents(
        currentDepth: String,
        separator: String = "",
        f: (T) -> String
    ): String {
        val list = this.toList()
        var resultString = ""
        if (!list.isEmpty()) {
            resultString += f(list.first())
            list.drop(1).forEach {
                resultString += writeLine(separator)
                resultString += f(it)
            }
            resultString += writeLine()
        }

        resultString += write(currentDepth)

        return resultString
    }

    private fun ppPlainObject(obj: Any, currentDepth: String): String {
        val increasedDepth = deepen(currentDepth)
        val className = obj.javaClass.simpleName

        var resultString = ""
        resultString += writeLine("$className(")
        resultString += obj.javaClass.declaredFields
            .filterNot { it.isSynthetic || it.isStatic() }
            .ppContents(currentDepth) {
                val staticMatchesParent =
                    Modifier.isStatic(it.modifiers) && it.type == obj.javaClass

                it.isAccessible = true
                var fString = write("$increasedDepth${it.name} = ")
                val extraIncreasedDepth =
                    deepen(increasedDepth, it.name.length + 3) // 3 is " = ".length in prev line
                val fieldValue = it.get(obj)
                fString += ppAny(
                    fieldValue,
                    extraIncreasedDepth,
                    increasedDepth,
                    staticMatchesParent
                )
                fString
            }
        resultString += write(')')

        return resultString
    }

    private fun ppIterable(obj: Iterable<*>, currentDepth: String): String {
        val increasedDepth = deepen(currentDepth)

        var resultString = writeLine('[')
        resultString += obj.ppContents(currentDepth, ",") {
            var fString = write(increasedDepth)
            fString += ppAny(it, increasedDepth)
            fString
        }
        resultString = write(']')
        return resultString
    }

    private fun ppMap(obj: Map<*, *>, currentDepth: String): String {
        val increasedDepth = deepen(currentDepth)

        var resultString = writeLine('{')
        resultString += obj.entries.ppContents(currentDepth, ",") {
            var fString = write(increasedDepth)
            fString += ppAny(it.key, increasedDepth)
            fString += write(" -> ")
            fString += ppAny(it.value, increasedDepth)
            fString
        }
        resultString += write('}')

        return resultString
    }

    private fun ppString(s: String, currentDepth: String): String {
        var resultString = ""
        if (s.length > wrappedLineWidth) {
            val tripleDoubleQuotes = "\"\"\""
            resultString += writeLine(tripleDoubleQuotes)
            resultString += writeLine(wordWrap(s, currentDepth))
            resultString += write("$currentDepth$tripleDoubleQuotes")
        } else {
            resultString += write("\"$s\"")
        }

        return resultString
    }

    private fun ppEnum(enum: Enum<*>): String {
        return write("${enum.javaClass.simpleName}.$enum")
    }

    private fun ppAtomic(obj: Any?): String {
        return write(obj.toString())
    }

    /**
     * Writes to the writeTo with a new line and adds logging
     */
    private fun writeLine(str: Any? = ""): String {
        return str.toString() + "\n"
    }

    /**
     * Writes to the writeTo and adds logging
     */
    private fun write(str: Any?): String {
        return str.toString()
    }

    private fun wordWrap(text: String, padding: String): String {
        lineInstance.setText(text)
        var start = lineInstance.first()
        var end = lineInstance.next()
        val breakableLocations = mutableListOf<String>()
        while (end != BreakIterator.DONE) {
            val substring = text.substring(start, end)
            breakableLocations.add(substring)
            start = end
            end = lineInstance.next()
        }
        val arr = mutableListOf(mutableListOf<String>())
        var index = 0
        arr[index].add(breakableLocations[0])
        breakableLocations.drop(1).forEach {
            val currentSize = arr[index].joinToString(separator = "").length
            if (currentSize + it.length <= wrappedLineWidth) {
                arr[index].add(it)
            } else {
                arr.add(mutableListOf(it))
                index += 1
            }
        }
        return arr.flatMap { listOf("$padding${it.joinToString(separator = "")}") }
            .joinToString("\n")
    }

    private fun deepen(currentDepth: String, size: Int = tabSize): String =
        " ".repeat(size) + currentDepth
}

/**
 * Determines if this object should not be broken down further for pretty printing.
 */
private fun Any?.isAtomic(): Boolean =
    this == null
            || this is Char || this is Number || this is Boolean || this is UUID

private fun Any?.isClassInside(): Boolean =
    this?.javaClass?.isMemberClass == true

private fun Field.isStatic(): Boolean =
    Modifier.isStatic(modifiers)


// For syntactic sugar
operator fun <T> Set<T>.get(x: T): Boolean = this.contains(x)