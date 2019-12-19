package {{PACKAGE}}.model

data class SocialModel(

    val id: String,
    val name: String,
    val email: String,
    val provider: Provider

) {

    enum class Provider {

        Facebook,
        Google

    }

}