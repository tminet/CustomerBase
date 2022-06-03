package tmidev.core.domain.usecase

import tmidev.core.domain.type.InputErrorType
import tmidev.core.domain.type.InputResultType
import javax.inject.Inject

interface ValidateSimpleFieldUseCase {
    operator fun invoke(string: String): InputResultType
}

class ValidateSimpleFieldUseCaseImpl @Inject constructor() : ValidateSimpleFieldUseCase {
    override fun invoke(string: String): InputResultType {
        if (string.isBlank()) return InputResultType(
            successful = false,
            errorType = InputErrorType.FIELD_EMPTY
        )

        return InputResultType(
            successful = true,
            errorType = null
        )
    }
}