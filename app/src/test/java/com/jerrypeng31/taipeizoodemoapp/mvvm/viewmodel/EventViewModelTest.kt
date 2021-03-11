package com.jerrypeng31.taipeizoodemoapp.mvvm.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.jerrypeng31.mvvmtest.Repository
import com.jerrypeng31.taipeizoodemoapp.idling.Idling
import com.jerrypeng31.taipeizoodemoapp.mvvm.model.AreaApiModel
import com.jerrypeng31.taipeizoodemoapp.mvvm.model.PlantApiModel
import com.jerrypeng31.taipeizoodemoapp.retrofit_utils.RxSchedulersOverrideRule
import io.mockk.MockKAnnotations
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.reactivex.Observable
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class EventViewModelTest {
    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()
    @get:Rule
    val rule = RxSchedulersOverrideRule()

    @MockK
    lateinit var stubRepository: Repository

    @MockK
    lateinit var idling: Idling

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
    }

    @Test
    fun testGetAreaData_success() {
        val areaApiModel = AreaApiModel(AreaApiModel.Result(1000, 0, 2, mutableListOf(), "Test"))

        every { stubRepository.getAreaData() }.answers{
            Observable.just(areaApiModel)
        }

        val viewModel = EventViewModel(stubRepository)
        viewModel.getAreaData()

        Assert.assertEquals(areaApiModel.result.count, viewModel.areaData.value?.content?.result?.count)
        Assert.assertEquals(areaApiModel.result.limit, viewModel.areaData.value?.content?.result?.limit)
        Assert.assertEquals(areaApiModel.result.offset, viewModel.areaData.value?.content?.result?.offset)
        Assert.assertEquals(areaApiModel.result.sort, viewModel.areaData.value?.content?.result?.sort)
    }

    @Test
    fun testGetAreaData_fail() {
        every { stubRepository.getAreaData() }.answers{
            Observable.fromCallable {
                throw Exception("NoAreaData")
            }
        }

        val viewModel = EventViewModel(stubRepository)
        viewModel.getAreaData()

        Assert.assertEquals("NoAreaData", viewModel.areaError.value?.content?.message)
    }

    @Test
    fun testGetPlantData_success() {
        val plantApiModel = PlantApiModel(PlantApiModel.Result(2000, 20, 23, mutableListOf(), "Test"))

        every { stubRepository.getPlantData(any()) }.answers{
            Observable.just(plantApiModel)
        }

        val viewModel = EventViewModel(stubRepository)
        viewModel.getPlantaData("")

        Assert.assertEquals(plantApiModel.result.count, viewModel.plantData.value?.content?.result?.count)
        Assert.assertEquals(plantApiModel.result.limit, viewModel.plantData.value?.content?.result?.limit)
        Assert.assertEquals(plantApiModel.result.offset, viewModel.plantData.value?.content?.result?.offset)
        Assert.assertEquals(plantApiModel.result.sort, viewModel.plantData.value?.content?.result?.sort)
    }

    @Test
    fun testGetPlantData_fail() {
        every { stubRepository.getPlantData(any()) }.answers{
            Observable.fromCallable {
                throw Exception("NoPlantData")
            }
        }

        val viewModel = EventViewModel(stubRepository)
        viewModel.getPlantaData("")

        Assert.assertEquals("NoPlantData", viewModel.plantError.value?.content?.message)
    }
}