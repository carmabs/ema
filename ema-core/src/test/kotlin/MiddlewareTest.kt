import com.carmabs.ema.core.action.EmaAction
import com.carmabs.ema.core.state.EmaDataState
import com.carmabs.emax.middleware.common.EmaxMiddleware
import com.carmabs.ema.core.viewmodel.emux.middleware.SideEffectScope
import com.carmabs.emax.middleware.common.EmaxMiddlewareStore
import com.carmabs.ema.core.viewmodel.emux.middleware.common.EmaNext
import com.carmabs.emax.store.EmaxStore
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import org.junit.Test

/**
 * Created by Carlos Mateo Benito on 1/10/23.
 *
 * <p>
 * Copyright (c) 2023 by Carmabs. All rights reserved.
 * </p>
 *
 * @author <a href=“mailto:apps.carmabs@gmail.com”>Carlos Mateo Benito</a>
 */
class MiddlewareTest {

    data class TestState(val testValue: String) : EmaDataState

    val scope = CoroutineScope(Dispatchers.Unconfined)

    private val store = EmaxStore<TestState>(
        TestState(""),
        scope
    ) {

    }

    private class TestMiddleware : EmaxMiddleware<TestState> {
        context(SideEffectScope)
        override fun invoke(
            store: EmaxStore<TestState>,
            action: EmaAction
        ): EmaNext {
            return next(action)
        }

    }

    private val middlewareStore = EmaxMiddlewareStore(
        store, scope,
        listOf()
    )

    @Test
    fun `test middleware`(){
        
    }
}