package com.carmabs.domain.model

/**
 *  *<p>
 * Copyright (c) 2020, Carmabs. All rights reserved.
 * </p>
 *
 *
 * @author <a href=“mailto:apps.carmabs@gmail.com”>Carlos Mateo</a>
 */

data class User(
    val name: String,
    val surname: String,
    val role: Role = Role.USER)