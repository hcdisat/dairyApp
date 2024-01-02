package com.hcdisat.dairyapp.abstraction.domain.model

import java.time.Instant

data class DomainImageToDelete(
    val remotePath: String = "",
    val createdAt: Long = Instant.now().toEpochMilli()
)