package com.carmabs.ema.presentation.ui.user

/**
 *
 *
 * @author <a href=“mailto:apps.carmabs@gmail.com”>Carlos Mateo</a>
 *
 * Date: 2019-09-25
 */

interface EmaUserItemModel {

    companion object{
        const val LEFT_ID = 0
        const val RIGHT_ID = 1

        fun getFromId(id:Int):Type{
            return when(id){
                LEFT_ID -> Type.LEFT
                RIGHT_ID -> Type.RIGHT
                else -> Type.LEFT
            }
        }
    }
    enum class Type(val id:Int) {
        LEFT(LEFT_ID),
        RIGHT(RIGHT_ID)
    }

    val type: Type

}

data class EmaUserRightModel(val number: Int) : EmaUserItemModel {
    override val type: EmaUserItemModel.Type = EmaUserItemModel.Type.RIGHT


}

class EmaUserLeftModel(val name: String) : EmaUserItemModel {
    override val type: EmaUserItemModel.Type = EmaUserItemModel.Type.LEFT

}