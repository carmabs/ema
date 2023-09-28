package com.carmabs.ema.testing.android.viewmodel

import androidx.test.ext.junit.runners.AndroidJUnit4
import com.carmabs.ema.core.initializer.EmaInitializer
import com.carmabs.ema.core.navigator.EmaEmptyNavigationEvent
import com.carmabs.ema.core.state.EmaEmptyState
import com.carmabs.ema.core.viewmodel.EmaViewModelBasic
import com.carmabs.ema.testing.core.EmaTest
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import org.junit.Test
import org.junit.runner.RunWith
import java.util.logging.Level
import java.util.logging.Logger

/**
 * Created by Carlos Mateo Benito on 16/8/22.
 *
 * <p>
 * Copyright (c) 2022 by Carmabs. All rights reserved.
 * </p>
 *
 * @author <a href=“mailto:apps.carmabs@gmail.com”>Carlos Mateo Benito</a>
 */
@RunWith(AndroidJUnit4::class)
class ViewModelTest : EmaTest() {

    private val TAG = "TEST_VIEWMODEL"
    private val logger: Logger = Logger.getLogger(TAG)

    fun log(message: String) {
        logger.log(Level.INFO, message)
    }

    @Test
    fun testExecuteViewModelResult() {
        runBlocking {
            val testVm = TestViewModel()
            testVm.onActionTestExecuteUseCase()
        }
    }

    private inner class TestViewModel : EmaViewModelBasic<EmaEmptyState, EmaEmptyNavigationEvent>() {


        override suspend fun onCreateState(initializer: EmaInitializer?): EmaEmptyState {
            return EmaEmptyState
        }

        suspend fun onActionTestExecuteUseCase() {
            var isFinished = false
            val result = executeUseCase {
                log("Start execution")
                delay(1000)
                isFinished = true
                log("Finish execution")
                5
            }.onSuccess {
                log("onSuccess1 $it")
                assert(isFinished)
            }

            delay(2000)

            result.onFinish {
                log("onFinish")
            }
            result.onError {
                log("onError $it")
            }
        }
    }
}