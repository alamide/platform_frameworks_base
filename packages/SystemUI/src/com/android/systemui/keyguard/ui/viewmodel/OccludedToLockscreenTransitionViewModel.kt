/*
 * Copyright (C) 2022 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.android.systemui.keyguard.ui.viewmodel

import com.android.systemui.animation.Interpolators.EMPHASIZED_DECELERATE
import com.android.systemui.dagger.SysUISingleton
import com.android.systemui.keyguard.domain.interactor.FromOccludedTransitionInteractor.Companion.TO_LOCKSCREEN_DURATION
import com.android.systemui.keyguard.domain.interactor.KeyguardTransitionInteractor
import com.android.systemui.keyguard.ui.KeyguardTransitionAnimationFlow
import javax.inject.Inject
import kotlin.time.Duration.Companion.milliseconds
import kotlinx.coroutines.flow.Flow

/**
 * Breaks down OCCLUDED->LOCKSCREEN transition into discrete steps for corresponding views to
 * consume.
 */
@SysUISingleton
class OccludedToLockscreenTransitionViewModel
@Inject
constructor(
    private val interactor: KeyguardTransitionInteractor,
) {
    private val transitionAnimation =
        KeyguardTransitionAnimationFlow(
            transitionDuration = TO_LOCKSCREEN_DURATION,
            transitionFlow = interactor.occludedToLockscreenTransition,
        )

    /** Lockscreen views y-translation */
    fun lockscreenTranslationY(translatePx: Int): Flow<Float> {
        return transitionAnimation.createFlow(
            duration = TO_LOCKSCREEN_DURATION,
            onStep = { value -> -translatePx + value * translatePx },
            interpolator = EMPHASIZED_DECELERATE,
        )
    }

    /** Lockscreen views alpha */
    val lockscreenAlpha: Flow<Float> =
        transitionAnimation.createFlow(
            startTime = 233.milliseconds,
            duration = 250.milliseconds,
            onStep = { it },
        )
}
