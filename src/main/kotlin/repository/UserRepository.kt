package repository

import api.GitHubService
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import model.User
import model.Repository
import java.io.File
import java.util.Date

class UserRepository(
    private val gitHubService: GitHubService,
    private val cacheFile: File = File("github_users_cache.json")
) {
    private val gson = Gson()
    private val userCache = mutableMapOf<String, User>()

    init {
        loadCache()
    }

    private fun loadCache() {
        if (cacheFile.exists()) {
            val type = object : TypeToken<Map<String, User>>() {}.type
            val cache = gson.fromJson<Map<String, User>>(cacheFile.readText(), type)
            userCache.putAll(cache)
        }
    }

    private fun saveCache() {
        cacheFile.writeText(gson.toJson(userCache))
    }

    suspend fun getUser(username: String): User {
        val normalizedUsername = username.lowercase()
        return userCache[normalizedUsername] ?: fetchAndCacheUser(normalizedUsername)
    }

    private suspend fun fetchAndCacheUser(username: String): User {
        val user = gitHubService.getUser(username)
        val repos = gitHubService.getUserRepositories(username)
        val userWithRepos = user.copy(repositories = repos)
        userCache[username] = userWithRepos
        saveCache()
        return userWithRepos
    }

    fun searchUser(username: String): User? {
        return userCache[username.lowercase()]
    }

    fun searchRepositories(query: String): List<Repository> {
        return userCache.values
            .flatMap { it.repositories }
            .filter { it.name.contains(query, ignoreCase = true) }
    }

    fun getAllCachedUsers(): List<User> = userCache.values.toList()
} 