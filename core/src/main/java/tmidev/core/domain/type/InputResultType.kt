package tmidev.core.domain.type

data class InputResultType(
    val successful: Boolean,
    val errorType: InputErrorType?
)