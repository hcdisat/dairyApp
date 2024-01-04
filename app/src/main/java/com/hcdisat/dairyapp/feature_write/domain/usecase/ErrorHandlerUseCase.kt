package com.hcdisat.dairyapp.feature_write.domain.usecase

import android.content.res.Resources
import com.hcdisat.common.RealmGenericException
import com.hcdisat.common.UserNotAuthenticatedException
import com.hcdisat.core.ui.R
import javax.inject.Inject

interface ErrorHandlerUseCase {
    operator fun invoke(throwable: Throwable): String
}

class ErrorHandlerUseCaseImpl @Inject constructor(
    private val resources: Resources
) : ErrorHandlerUseCase {
    override fun invoke(throwable: Throwable): String {
        return when (throwable) {
            is UserNotAuthenticatedException -> resources.getString(R.string.user_not_found_error)

            is RealmGenericException ->
                throwable.throwable.message ?: resources.getString(R.string.user_not_found_error)

            else -> throwable.message ?: throwable.javaClass.simpleName
        }
    }
}