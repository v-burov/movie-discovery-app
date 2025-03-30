# Benedict Cumberbatch Movie App

## Overview
The Benedict Cumberbatch Movie App is a mobile application that allows users to explore movies starring 
Benedict Cumberbatch. Using the [TMDB](https://developer.themoviedb.org/docs/getting-started) API, the app
fetches and displays a list of movies featuring Benedict Cumberbatch. Users can view movie details such
as the title, poster, and synopsis. Additionally, the app provides recommendations for similar movies.

### Technologies Used
The app is built using the following technologies:

- **Kotlin** – A modern, expressive, and concise programming language designed for Android development.
- **Jetpack Compose** – A UI toolkit for building native Android interfaces with a declarative approach.
- **Koin** – A lightweight dependency injection framework for managing dependencies efficiently.
- **Clean Architecture** – A design pattern that promotes separation of concerns and testability.
- **Retrofit** – A powerful networking library for making API calls and handling responses.
- **Room Database** – A persistence library that provides an abstraction layer over SQLite for efficient local data storage.
- **Compose Navigation** – A library for handling navigation within a single-activity Compose-based application.
- **Single Activity Architecture** – Ensures a seamless navigation experience and avoids unnecessary activity recreation.

## Video Demo
Check out the video demo of the app: [Watch on YouTube](https://youtube.com/shorts/TAUEwjeGnrg?feature=share)

## TODO List
- Implement dynamic search for movies or people (avoid hardcoding the person ID for Benedict Cumberbatch).
- Integrate local and remote data sources (Retrofit & Room):
  - Use Retrofit for API calls and Room for local database storage.
  - Wrap both sources to allow flexibility if data sources change.
- Implement mappers to convert data between the API, domain, and database layers.
- Organize the app into separate modules for Domain, Data, and Presentation layers.
- Implement pagination using Paging3 to load more movies as the user scrolls.
- Write unit tests for key components and UI tests for app functionality.
- Handle API, network, and data errors gracefully with appropriate messages or retry mechanisms.
- Detect offline status and handle retries when the connection is restored.
- Use KDoc to document key classes and methods clearly.
- Clear database when needed.
- Inject `SchedulersProvider` into the repository to enable mocking in unit tests.
- Add a spinner while loading to enhance user experience.
- Optimize data insertions in the database by updating instead of replacing existing records.
