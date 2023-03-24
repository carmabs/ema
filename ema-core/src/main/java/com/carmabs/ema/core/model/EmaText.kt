package com.carmabs.ema.core.model

import com.carmabs.ema.core.constants.STRING_EMPTY
import java.io.Serializable

/**
 * Created by Carlos Mateo Benito on 25/12/21.
 *
 * <p>
 * Copyright (c) 2021 by Carmabs. All rights reserved.
 * </p>
 *
 * @author <a href=“mailto:apps.carmabs@gmail.com”>Carlos Mateo Benito</a>
 */
sealed class EmaText(open val data: Array<out Any>? = null) : Serializable {

    companion object {
        fun text(text: String, vararg data: Any) = Text(text, data)
        fun empty() = Text(STRING_EMPTY)
        fun id(id: Int, vararg data: Any) = Id(id, data)
        fun plural(id: Int, quantity: Int, vararg data: Any) = Plural(id, quantity, data)
        fun composable(vararg texts: EmaText) = Composition(listOf(*texts))
    }

    data class Composition(val texts:List<EmaText>) : EmaText(null) {
        override fun equals(other: Any?): Boolean {
            if (this === other) return true
            if (javaClass != other?.javaClass) return false

            other as Composition

            if (texts != other.texts) return false
            if (data != null) {
                if (other.data == null) return false
                if (!data.contentEquals(other.data)) return false
            } else if (other.data != null) return false

            return true
        }

        override fun hashCode(): Int {
            var result = texts.hashCode()
            result = 31 * result + (data?.contentHashCode() ?: 0)
            return result
        }

    }

    data class Text(val text: String, override val data: Array<out Any>?=null) : EmaText(data) {
        override fun equals(other: Any?): Boolean {
            if (this === other) return true
            if (javaClass != other?.javaClass) return false

            other as Text

            if (text != other.text) return false
            if (!data.contentEquals(other.data)) return false

            return true
        }

        override fun hashCode(): Int {
            var result = text.hashCode()
            result = 31 * result + data.contentHashCode()
            return result
        }
    }

    data class Id(val id: Int, override val data: Array<out Any>?=null) : EmaText(data) {
        override fun equals(other: Any?): Boolean {
            if (this === other) return true
            if (javaClass != other?.javaClass) return false
            if (!super.equals(other)) return false

            other as Id

            if (id != other.id) return false
            if (!data.contentEquals(other.data)) return false

            return true
        }

        override fun hashCode(): Int {
            var result = super.hashCode()
            result = 31 * result + id
            result = 31 * result + data.contentHashCode()
            return result
        }

    }

    data class Plural(val id: Int, val quantity: Int, override val data: Array<out Any>?) :
        EmaText(data) {
        override fun equals(other: Any?): Boolean {
            if (this === other) return true
            if (javaClass != other?.javaClass) return false

            other as Plural

            if (id != other.id) return false
            if (quantity != other.quantity) return false
            if (!data.contentEquals(other.data)) return false

            return true
        }

        override fun hashCode(): Int {
            var result = id
            result = 31 * result + quantity
            result = 31 * result + data.contentHashCode()
            return result
        }
    }

    fun isEmpty():Boolean{
       return (this is Text) && this.text.isEmpty()
    }
}
