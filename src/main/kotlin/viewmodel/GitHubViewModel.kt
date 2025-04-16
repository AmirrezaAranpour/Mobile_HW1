package viewmodel

import model.User
import model.Repository
import repository.UserRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class GitHubViewModel(private val userRepository: UserRepository) {
    suspend fun fetchUser(username: String): Result<User> = withContext(Dispatchers.IO) {
        try {
            Result.success(userRepository.getUser(username))
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    fun searchUser(username: String): User? {
        return userRepository.searchUser(username)
    }

    fun searchRepositories(query: String): List<Repository> {
        return userRepository.searchRepositories(query)
    }

    fun getAllCachedUsers(): List<User> {
        return userRepository.getAllCachedUsers()
    }
} 