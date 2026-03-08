/*
 * Copyright (C) 2020 HaiYang Li
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 *
 * https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 */

package com.landawn.abacus.util;

/**
 * Parallelization policy used by {@link Matrices} for the current thread.
 *
 * <p>The active value is stored by {@link Matrices#setParallelMode(ParallelMode)} and is consulted by
 * helpers such as {@link Matrices#isParallelizable(AbstractMatrix, long)}.</p>
 *
 * @see Matrices#setParallelMode(ParallelMode)
 * @see Matrices#getParallelMode()
 */
public enum ParallelMode {
    /**
     * Prefer parallel execution whenever the runtime supports it.
     */
    FORCE_ON,

    /**
     * Force sequential execution.
     */
    FORCE_OFF,

    /**
     * Use the built-in heuristics in {@link Matrices#isParallelizable(AbstractMatrix, long)}.
     */
    AUTO
}
