# Vehicle Information App

An Android application built using **Kotlin**, **Hilt**, **Retrofit**, and **Jetpack ViewModel** to fetch, display, and sort vehicle data from a REST API. The app follows **Clean Architecture** with a multi-module structure, ensuring a scalable and maintainable codebase.

## Tech Stack

- **Kotlin**: Main language for development.
- **Hilt**: Dependency injection to simplify component management.
- **Retrofit**: HTTP client for API requests.
- **Jetpack ViewModel**: For lifecycle-conscious data handling.
- **StateFlow/MutableStateFlow**: To manage UI state and updates.
- **RecyclerView**: For displaying lists efficiently.
- **Coroutines**: For managing background tasks like API requests.
- **Flow**: To handle asynchronous streams of data.
- **LiveData**: For observing changes in data and reflecting them in the UI.
- **Gson**: JSON converter for serializing and deserializing API responses.
- **OkHttp**: HTTP client used with Retrofit for networking, providing connection management and logging.
- **ConstraintLayout**: Used for designing responsive UI layouts.
- **Navigation Component**: Used to manage app navigation and handle fragment transitions.
- **Material Design**: For UI components like buttons, input fields, and dropdowns, ensuring modern UI/UX consistency.

## API

The app fetches vehicle data from the following API:

- **URL**: `https://random-data-api.com/api/vehicle/random_vehicle`
- Returns random vehicles with attributes such as `vin`, `make_and_model`, `color`, `car_type`, and `kilometrage`.

## Installation

1. Clone the repository:

   ```bash
   https://github.com/waleHa/RidesChallenge.git
   ```

2. Open the project in **Android Studio** and sync the Gradle files.
3. Run the app on an Android device or emulator.

## Usage

- The user enters a number (1-100) and presses the **Fetch Vehicles** button to load vehicle data.
- Vehicles are displayed in a RecyclerView with their `make/model` and `VIN`.
- Sorting is available through a dropdown to sort vehicles by **VIN** or **make/model**.

## Architecture

The app follows **Clean Architecture** principles, separated into multiple modules for better scalability and maintainability:

### Layers

1. **Domain Layer**:
   - **Models**: Business entities such as `VehicleResponse`, representing the vehicle data.
   - **Repository Interfaces**: Define data access contracts, such as fetching vehicles from an API.
   - **Use Cases**: Handle business logic, such as fetching and sorting vehicle data using the `GetVehiclesUseCase`.

2. **Data Layer**:
   - **API Interface**: Manages API requests using **Retrofit**.
   - **DTOs (Data Transfer Objects)**: Represent raw API responses, such as `VehicleResponseDto`, before mapping to domain models.
   - **Repository Implementation**: Implements the data-fetching logic, converts DTOs to domain models, and returns them to the domain layer.

3. **Core Layer**:
   - **BaseUseCase**: Abstract class for executing business logic.
   - **Resource**: Sealed class used to represent the different states of data (Success, Loading, Error).

4. **Presentation Layer**:
   - **VehicleListFragment**: Displays the UI for vehicle input and list.
   - **VehicleViewModel**: Manages fetching data from the API and sorting vehicles based on user-selected criteria. Observes and updates the UI state using `StateFlow`.

   ### UI Overview:
   - **Input Field**: The user enters a number of vehicles (1-100) and clicks the fetch button to trigger the API call.
   - **Dropdown for Sorting**: Allows users to sort the list by **VIN** (ascending/descending) or **make_and_model** (ascending/descending).
   - **RecyclerView**: Displays the fetched list of vehicles. Each item shows `make/model` and `VIN`.
   - **Progress Bar**: Displays loading feedback while data is being fetched.
   - **Error Handling**: Displays error messages when the API call fails.

## Dependencies

- **Hilt** for Dependency Injection.
- **Retrofit** for API requests.
- **RecyclerView** for list display.
- **Coroutines** for handling background tasks.
- **StateFlow** for state management.
- **OkHttp** for managing network requests.

```gradle
implementation("com.google.dagger:hilt-android:2.51.1")
implementation("com.squareup.retrofit2:retrofit:2.10.0")
implementation("com.squareup.retrofit2:converter-gson:2.10.0")
implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.6.4")
implementation("com.squareup.okhttp3:okhttp:4.9.3")
``` 

- ### Vehicle List Screenshot:

<img width="359" alt="image" src="https://github.com/user-attachments/assets/01c0bada-1714-4a7b-b7db-3f200a18fffa">