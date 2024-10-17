

package com.ibm.rides


import app.cash.turbine.test
import com.ibm.core.Resource
import com.ibm.domain.model.VehicleResponse
import com.ibm.domain.usecase.GetVehiclesUseCase
import com.ibm.rides.presentation.vehicle.VehicleViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.MockitoAnnotations
import org.mockito.kotlin.whenever
import kotlin.test.assertIs


@ExperimentalCoroutinesApi
class VehicleViewModelTest {

    private val testDispatcher = StandardTestDispatcher()

    @Mock
    lateinit var getVehiclesUseCase: GetVehiclesUseCase

    private lateinit var vehicleViewModel: VehicleViewModel

    @Before
    fun setUp() {
        MockitoAnnotations.openMocks(this)
        Dispatchers.setMain(testDispatcher)
        vehicleViewModel = VehicleViewModel(getVehiclesUseCase, testDispatcher)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `fetchVehicles with valid input size emits Loading and Success`() = runTest {
        val mockVehicles = listOf(
            VehicleResponse(
                vin = "VIN2",
                makeAndModel = "Audi A4",
                color = "Red",
                carType = "Sedan",
                transmission = "Automatic",
                driveType = "RWD",
                fuelType = "Gasoline",
                doors = 4,
                mileage = 50000,
                kilometrage = 48000,
                licensePlate = "ABC123"
            ),
            VehicleResponse(
                vin = "VIN1",
                makeAndModel = "BMW X3",
                color = "Black",
                carType = "SUV",
                transmission = "Automatic",
                driveType = "AWD",
                fuelType = "Diesel",
                doors = 5,
                mileage = 60000,
                kilometrage = 58000,
                licensePlate = "XYZ987"
            )
        )

        whenever(
            getVehiclesUseCase.execute(
                GetVehiclesUseCase.Params(2, GetVehiclesUseCase.SortOption.VinAsc)
            )
        ).thenReturn(
            flow {
                emit(Resource.Loading())
                emit(Resource.Success(mockVehicles))
            }
        )

        vehicleViewModel.vehiclesState.test {
            // Initial state
            val initialState = awaitItem()
            assertIs<Resource.Loading<*>>(initialState)

            // Trigger action
            vehicleViewModel.fetchVehicles(2)
            advanceUntilIdle()

            // Expect Loading state emitted when use case emits Loading
            val loadingState = awaitItem()
            assertIs<Resource.Loading<*>>(loadingState)

            // Expect Success state
            val successState = awaitItem()
            assertIs<Resource.Success<List<VehicleResponse>>>(successState)
            assertEquals("VIN1", successState.data?.first()?.vin)

            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `fetchVehicles with invalid input size emits Error immediately`() = runTest {
        vehicleViewModel.vehiclesState.test {
            // Initial state
            val initialState = awaitItem()
            assertIs<Resource.Loading<*>>(initialState)

            // Trigger action with invalid size
            vehicleViewModel.fetchVehicles(101)

            // Since input is invalid, expect Error state immediately
            val errorState = awaitItem()
            assertIs<Resource.Error<*>>(errorState)
            assertEquals("Input must be between 1 and 100", errorState.message)

            // No further states should be emitted
            expectNoEvents()

            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `setSortOption without fetching new data emits sorted Success state`() = runTest {
        val mockVehicles = listOf(
            VehicleResponse(
                vin = "VIN1",
                makeAndModel = "BMW X3",
                color = "Black",
                carType = "SUV",
                transmission = "Automatic",
                driveType = "AWD",
                fuelType = "Diesel",
                doors = 5,
                mileage = 60000,
                kilometrage = 58000,
                licensePlate = "XYZ987"
            ),
            VehicleResponse(
                vin = "VIN2",
                makeAndModel = "Audi A4",
                color = "Red",
                carType = "Sedan",
                transmission = "Automatic",
                driveType = "RWD",
                fuelType = "Gasoline",
                doors = 4,
                mileage = 50000,
                kilometrage = 48000,
                licensePlate = "ABC123"
            )
        )

        whenever(
            getVehiclesUseCase.execute(
                GetVehiclesUseCase.Params(2, GetVehiclesUseCase.SortOption.VinAsc)
            )
        ).thenReturn(
            flow {
                emit(Resource.Loading())
                emit(Resource.Success(mockVehicles))
            }
        )

        vehicleViewModel.vehiclesState.test {
            // Initial state
            val initialState = awaitItem()
            assertIs<Resource.Loading<*>>(initialState)

            // Fetch vehicles
            vehicleViewModel.fetchVehicles(2)
            advanceUntilIdle()

            // Expect Loading state emitted when use case emits Loading
            val loadingState = awaitItem()
            assertIs<Resource.Loading<*>>(loadingState)

            // Expect Success state after fetching vehicles
            val successState = awaitItem()
            assertIs<Resource.Success<List<VehicleResponse>>>(successState)

            // Set sort option
            vehicleViewModel.setSortOption(GetVehiclesUseCase.SortOption.MakeAndModelAsc)
            advanceUntilIdle()

            // Expect Success state after sorting
            val sortedState = awaitItem()
            assertIs<Resource.Success<List<VehicleResponse>>>(sortedState)
            assertEquals("Audi A4", sortedState.data?.first()?.makeAndModel)

            cancelAndIgnoreRemainingEvents()
        }
    }
}
