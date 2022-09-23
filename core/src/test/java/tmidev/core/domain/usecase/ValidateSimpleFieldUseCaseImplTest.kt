package tmidev.core.domain.usecase

import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.BlockJUnit4ClassRunner
import tmidev.core.domain.type.InputErrorType
import tmidev.core.domain.type.InputResultType

@RunWith(BlockJUnit4ClassRunner::class)
class ValidateSimpleFieldUseCaseImplTest {
    private lateinit var validateSimpleFieldUseCase: ValidateSimpleFieldUseCase

    @Before
    fun setUp() {
        validateSimpleFieldUseCase = ValidateSimpleFieldUseCaseImpl()
    }

    @Test
    fun `success for string`() {
        val expectedInputResult = InputResultType(
            successful = true,
            errorType = null
        )

        val stringSize = 5
        val char = "a"
        val string = List(size = stringSize) { char }.joinToString(separator = "")

        val actualInputResult = validateSimpleFieldUseCase(string = string)

        assertEquals(expectedInputResult, actualInputResult)
    }

    @Test
    fun `error for empty string`() {
        val expectedInputResult = InputResultType(
            successful = false,
            errorType = InputErrorType.FIELD_EMPTY
        )

        val string = ""
        val actualInputResult = validateSimpleFieldUseCase(string = string)

        assertEquals(expectedInputResult, actualInputResult)
    }

    @Test
    fun `error for blank string`() {
        val expectedInputResult = InputResultType(
            successful = false,
            errorType = InputErrorType.FIELD_EMPTY
        )

        val string = " "
        val actualInputResult = validateSimpleFieldUseCase(string = string)

        assertEquals(expectedInputResult, actualInputResult)
    }
}