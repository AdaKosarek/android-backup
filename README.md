# FoodDiary

FoodDiary is an Android application that serves as a personal journal for tracking favorite meals and the places where they were consumed.
The app combines external food data, on-device machine learning, location services, and maps to create a richer and more interactive food logging experience.

## Goal
The objective of this project was to practice real-world Android development concepts including API integration, ML-based image recognition, maps, location handling, and automated testing.

## Features
- Search and fetch meals from the Spoonacular API
- Meal descriptions, recipes, calories, and photos
- Food category detection using on-device ML
- Smart suggestions for similar meals
- Save meals to a personal list
- Mark meals as favorites
- Automatically capture and store local GPS coordinates with each meal
- Display saved meals on a map
- Unit and UI testing

## How it works
1. The user selects or captures a meal.
2. Food category is recognized using ML Kit.
3. Similar meals are suggested from the API.
4. The user chooses a meal and saves it.
5. The app retrieves the device’s current location (GPS coordinates).
6. The meal is stored locally together with its metadata (recipe, calories, image, and coordinates).
7. Saved meals can be visualized on a map.

## Tech stack
- Kotlin
- Jetpack Compose
- MVVM architecture
- REST API
- On-device ML kit
- Google Maps SDK
- Location services (GPS)
- Room database
- Hilt DI
- Unit tests (JUnit)
- UI tests (Compose testing)

## Local.properties:
- server="https://api.spoonacular.com/"
- API_KEY="generated-free-key"
