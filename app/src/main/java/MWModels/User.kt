package MWModels

data class User(
    val id: Int, val fullName: String?, val username: String?, val email: String?,
    val password: String?, val status: String?, val role: String?)