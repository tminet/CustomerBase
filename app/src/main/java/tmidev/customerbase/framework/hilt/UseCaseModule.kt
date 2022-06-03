package tmidev.customerbase.framework.hilt

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import tmidev.core.domain.usecase.ValidateSimpleFieldUseCase
import tmidev.core.domain.usecase.ValidateSimpleFieldUseCaseImpl

@Module
@InstallIn(ViewModelComponent::class)
interface UseCaseModule {
    @Binds
    fun bindValidateSimpleField(
        useCase: ValidateSimpleFieldUseCaseImpl
    ): ValidateSimpleFieldUseCase
}