package tmidev.core.domain.model

data class Customer(
    val firstName: String,
    val lastName: String,
    val isActive: Boolean,
    val addedAt: Long = System.currentTimeMillis(),
    val id: Int = 0
)