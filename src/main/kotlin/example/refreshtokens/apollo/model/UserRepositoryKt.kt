package example.refreshtokens.apollo.model

object UserRepositoryKt {

    private var counter : Int = 0
    private var database: MutableList<UserKt> = mutableListOf()

    init {
        //Bootstraping a few users
        insertUser("miguel", "miguelpassword", true)
        insertUser("joao", "joaopassword", false)
    }

    fun insertUser(username: String, password: String, isAdmin: Boolean): UserKt {
        val user = UserKt(counter++, username, password, isAdmin)
        database.add(user)
        return user
    }

    fun getUserById(id: Int): UserKt {
        return database[id]
    }

    fun getUserByUsername(username: String): UserKt? {
        return database.firstOrNull { it.username == username }
    }
}