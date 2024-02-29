package com.jerrypeng31.taipeizoodemoapp.mvvm.model

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.jerrypeng31.mvvmtest.Repository
import com.jerrypeng31.taipeizoodemoapp.retrofit_utils.FakeRetrofit
import com.jerrypeng31.taipeizoodemoapp.retrofit_utils.Utils
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.runBlocking
import okhttp3.mockwebserver.MockResponse
import org.hamcrest.Matchers.equalTo
import org.junit.After
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class RepositoryTest {
    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    @Before
    fun setUp() {
        FakeRetrofit.init()
    }

    @Test
    fun testGetAreaData_success() {
        val mockResponse = MockResponse()
        mockResponse.setResponseCode(200)
        mockResponse.setBody(Utils.loadTestData( "area_data.json"))
        FakeRetrofit.mockWebServer.enqueue(mockResponse)

        var areaDataModel : AreaApiModel? = null
        runBlocking {
            flow { emit(FakeRetrofit.apiService?.areaData(Repository.SCOPE))}
                .collect {
                    areaDataModel = it
                }
        }

        Assert.assertEquals("臺灣動物區", areaDataModel?.result?.results?.get(0)?.eName)
        Assert.assertEquals("兒童動物區", areaDataModel?.result?.results?.get(1)?.eName)
    }

    @Test
    fun testGetAreaData_fail() {
        val mockResponse = MockResponse()
        mockResponse.setResponseCode(400)
        mockResponse.setBody(Utils.loadTestData( "area_data.json"))
        FakeRetrofit.mockWebServer.enqueue(mockResponse)

        var areaDataModel : AreaApiModel? = null
        runBlocking {
            flow { emit(FakeRetrofit.apiService?.areaData(Repository.SCOPE))}
                .catch {
                    areaDataModel = AreaApiModel(AreaApiModel.Result(0,0,0, listOf(),"Test"))
                }
                .collect {
                    areaDataModel = it
                }
        }

        Assert.assertThat("Test", equalTo(areaDataModel?.result?.sort))
    }

    @Test
    fun testGetPlantData_success() {
        val mockResponse = MockResponse()
        mockResponse.setResponseCode(200)
        mockResponse.setBody(Utils.loadTestData( "plant_data.json"))
        FakeRetrofit.mockWebServer.enqueue(mockResponse)

        var plantDataModel : PlantApiModel? = null
        runBlocking {
            flow { emit(FakeRetrofit.apiService?.plantData(Repository.SCOPE, ""))}
                .collect {
                    plantDataModel = it
                }
        }

        Assert.assertEquals("大花紫薇", plantDataModel?.result?.results?.get(0)?.FNameCh)
        Assert.assertEquals("水黃皮", plantDataModel?.result?.results?.get(1)?.FNameCh)
    }

    @Test
    fun testGetPlantData_error() {
        val mockResponse = MockResponse()
        mockResponse.setResponseCode(400)
        mockResponse.setBody(Utils.loadTestData( "plant_data.json"))
        FakeRetrofit.mockWebServer.enqueue(mockResponse)

        var plantDataModel : PlantApiModel? = null
        runBlocking {
            flow { emit(FakeRetrofit.apiService?.plantData(Repository.SCOPE, ""))}
                .catch {
                    plantDataModel = PlantApiModel(PlantApiModel.Result(0,0,0, listOf(),"Test"))
                }
                .collect {
                    plantDataModel = it
                }
        }

        Assert.assertEquals("Test", plantDataModel?.result?.sort)
    }

    @After
    fun tearDown() {
        FakeRetrofit.serverShutdown()
    }
}