import com.carmabs.ema.core.action.EmaAction
import com.carmabs.ema.core.state.EmaDataState
import com.carmabs.ema.core.viewmodel.emux.middleware.common.EmaMiddleware
import com.carmabs.ema.core.viewmodel.emux.middleware.SideEffectScope
import com.carmabs.ema.core.viewmodel.emux.middleware.common.EmaMiddlewareStore
import com.carmabs.ema.core.viewmodel.emux.middleware.common.EmaNext
import com.carmabs.ema.core.viewmodel.emux.store.EmaStore
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

    private val store = EmaStore<TestState>(
        TestState(""),
        scope
    ) {

    }

    private class TestMiddleware : EmaMiddleware<TestState> {
        context(SideEffectScope)
        override fun invoke(
            store: EmaStore<TestState>,
            action: EmaAction
        ): EmaNext {
            return next(action)
        }

    }

    private val middlewareStore = EmaMiddlewareStore(
        store, scope,
        listOf()
    )

    @Test
    fun `test middleware`(){
        
    }
}