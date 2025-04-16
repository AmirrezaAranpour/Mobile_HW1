# GitHub User Data Fetcher

A Kotlin command-line application that allows users to fetch and manage GitHub user data and repositories using the GitHub API.

## Features

- Fetch GitHub user data and their public repositories
- Cache user data locally
- Search through cached users
- Search through cached repositories
- Display detailed user information including:
  - Username
  - Followers count
  - Following count
  - Account creation date
  - Public repositories with details

## Prerequisites

- Java 17 or higher
- Gradle 7.0 or higher
- Kotlin 1.9.0 or higher

## Dependencies

- Retrofit 2.9.0 - For making HTTP requests to GitHub API
- Gson 2.10.1 - For JSON parsing
- Kotlin Coroutines 1.7.3 - For asynchronous programming
- OkHttp 4.11.0 - For HTTP client functionality

## Installation

1. Clone the repository:
```bash
git clone https://github.com/AmirrezaAranpour/Mobile_HW1.git
```

2. Navigate to the project directory:
```bash
cd Mobile_HW1
```

3. Build the project:
```bash
./gradlew build
```

## Usage

Run the application:
```bash
./gradlew run
```

The application provides a menu-driven interface with the following options:

1. Fetch GitHub user data - Enter a GitHub username to fetch their data
2. Display cached users - Show all users stored in the cache
3. Search user in cache - Search for a specific user in the cache
4. Search repositories in cache - Search for repositories in the cache
5. Exit - Close the application


## Architecture

The project follows a clean architecture pattern with the following components:

- **API Layer**: Handles communication with GitHub API
- **Repository Layer**: Manages data operations and caching
- **ViewModel Layer**: Contains business logic and data transformation
- **View Layer**: Handles user interface and input/output
