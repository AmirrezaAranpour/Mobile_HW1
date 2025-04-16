package org.example

import api.GitHubService
import model.User
import model.Repository
import repository.UserRepository
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import viewmodel.GitHubViewModel
import java.text.SimpleDateFormat
import java.util.*
import kotlin.system.exitProcess
import kotlinx.coroutines.runBlocking

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
class GitHubView(private val viewModel: GitHubViewModel) {
    private val dateFormat = SimpleDateFormat("yyyy-MM-dd")

    fun displayMenu() {
        println("\nGitHub User Data Fetcher")
        println("1. Fetch GitHub user data")
        println("2. Display cached users")
        println("3. Search user in cache")
        println("4. Search repositories in cache")
        println("5. Exit")
        print("\nEnter your choice (1-5): ")
    }

    suspend fun handleUserChoice(choice: String) {
        when (choice) {
            "1" -> fetchUserData()
            "2" -> displayCachedUsers()
            "3" -> searchUser()
            "4" -> searchRepositories()
            "5" -> exitProcess(0)
            else -> println("Invalid choice. Please try again.")
        }
    }

    private suspend fun fetchUserData() {
        print("Enter GitHub username: ")
        val username = readLine() ?: return
        
        println("\nFetching user data...")
        viewModel.fetchUser(username).fold(
            onSuccess = { displayUser(it) },
            onFailure = { println("Error: ${it.message}") }
        )
    }

    private fun displayCachedUsers() {
        val users = viewModel.getAllCachedUsers()
        if (users.isEmpty()) {
            println("\nNo cached users found.")
            return
        }

        println("\nCached Users:")
        users.forEach { user ->
            println("\nUsername: ${user.login}")
            println("Followers: ${user.followers}")
            println("Following: ${user.following}")
            println("Created: ${dateFormat.format(user.createdAt)}")
        }
    }

    private fun searchUser() {
        print("Enter username to search: ")
        val username = readLine() ?: return
        
        val user = viewModel.searchUser(username)
        if (user != null) {
            displayUser(user)
        } else {
            println("\nUser not found in cache.")
        }
    }

    private fun searchRepositories() {
        print("Enter repository name to search: ")
        val query = readLine() ?: return
        
        val repositories = viewModel.searchRepositories(query)
        if (repositories.isEmpty()) {
            println("\nNo repositories found matching '$query'")
            return
        }

        println("\nMatching Repositories:")
        repositories.forEach { repo ->
            println("\nName: ${repo.name}")
            println("Description: ${repo.description ?: "No description"}")
            println("URL: ${repo.url}")
            println("Stars: ${repo.stars}")
            println("Forks: ${repo.forks}")
        }
    }

    private fun displayUser(user: User) {
        println("\nUser Information:")
        println("Username: ${user.login}")
        println("Followers: ${user.followers}")
        println("Following: ${user.following}")
        println("Created: ${dateFormat.format(user.createdAt)}")
        
        if (user.repositories.isNotEmpty()) {
            println("\nPublic Repositories:")
            user.repositories.forEach { repo ->
                println("\n- ${repo.name}")
                println("  Description: ${repo.description ?: "No description"}")
                println("  Stars: ${repo.stars}")
                println("  Forks: ${repo.forks}")
            }
        }
    }
}

fun main() = runBlocking {
    val retrofit = Retrofit.Builder()
        .baseUrl("https://api.github.com/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    val gitHubService = retrofit.create(GitHubService::class.java)
    val userRepository = UserRepository(gitHubService)
    val viewModel = GitHubViewModel(userRepository)
    val view = GitHubView(viewModel)

    println("Welcome to GitHub User Data Fetcher!")
    
    while (true) {
        view.displayMenu()
        val choice = readLine() ?: continue
        view.handleUserChoice(choice)
    }
}