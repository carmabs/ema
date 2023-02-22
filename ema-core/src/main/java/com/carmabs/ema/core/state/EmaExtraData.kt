package com.carmabs.ema.core.state

/**
 * Class to handle extra data used for single events, loading states.
 *
 * @author <a href=“mailto:apps.carmabs@gmail.com”>Carlos Mateo</a>
 */

data class EmaExtraData(val id: String = DEFAULT_ID, val data: Any? = null) : EmaDataState
{
    companion object{
       const val DEFAULT_ID = "EXTRA_DEFAULT_ID"
    }
}