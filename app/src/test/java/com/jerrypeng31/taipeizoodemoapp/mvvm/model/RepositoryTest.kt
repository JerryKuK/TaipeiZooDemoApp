package com.jerrypeng31.taipeizoodemoapp.mvvm.model

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.jerrypeng31.mvvmtest.Repository
import com.jerrypeng31.taipeizoodemoapp.retrofit_utils.MockAPIResponse
import com.jerrypeng31.taipeizoodemoapp.retrofit_utils.MockInterceptor
import com.jerrypeng31.taipeizoodemoapp.retrofit_utils.MockRetrofit
import com.jerrypeng31.taipeizoodemoapp.retrofit_utils.Utils
import org.junit.Assert
import org.junit.Rule
import org.junit.Test

class RepositoryTest {
    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    @Test
    fun testGetAreaData() {
        val interceptor = MockInterceptor()

        interceptor.setInterceptorListener(object : MockInterceptor.MockInterceptorListener {
            override fun setAPIResponse(url: String): MockAPIResponse? {
                val mockAPIResponse = MockAPIResponse()
                mockAPIResponse.status = 200
                mockAPIResponse.responseString = Utils.loadTestData( "area_data.json")
                return mockAPIResponse
            }
        })

        val mockRetrofit = MockRetrofit(interceptor)
        val areaDataModel = mockRetrofit.apiService?.areaData(Repository.SCOPE)?.blockingFirst()

        Assert.assertEquals("臺灣動物區", areaDataModel?.result?.results?.get(0)?.eName)
        Assert.assertEquals("兒童動物區", areaDataModel?.result?.results?.get(1)?.eName)
    }

    @Test
    fun testGetPlantData() {
        val interceptor = MockInterceptor()

        interceptor.setInterceptorListener(object : MockInterceptor.MockInterceptorListener {
            override fun setAPIResponse(url: String): MockAPIResponse? {
                val mockAPIResponse = MockAPIResponse()
                mockAPIResponse.status = 200
                mockAPIResponse.responseString = Utils.loadTestData( "plant_data.json")
                return mockAPIResponse
            }
        })

        val mockRetrofit = MockRetrofit(interceptor)
        val plantDataModel = mockRetrofit.apiService?.plantData(Repository.SCOPE, "")?.blockingFirst()

        Assert.assertEquals("大花紫薇", plantDataModel?.result?.results?.get(0)?.FNameCh)
        Assert.assertEquals("水黃皮", plantDataModel?.result?.results?.get(1)?.FNameCh)
    }
}