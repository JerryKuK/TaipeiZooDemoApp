package com.jerrypeng31.taipeizoodemoapp.mvvm.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.jerrypeng31.mvvmtest.Repository
import com.jerrypeng31.taipeizoodemoapp.idling.Idling
import com.jerrypeng31.taipeizoodemoapp.mvvm.model.AreaApiModel
import com.jerrypeng31.taipeizoodemoapp.mvvm.model.PlantApiModel
import com.jerrypeng31.taipeizoodemoapp.retrofit_utils.RxSchedulersOverrideRule
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.impl.annotations.MockK
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

        coEvery { stubRepository.getAreaData() }.answers{
            areaApiModel
        }

        val viewModel = EventViewModel(stubRepository)
        viewModel.getAreaData()

        Assert.assertEquals(areaApiModel.result.count, viewModel.areaData.value?.result?.count)
        Assert.assertEquals(areaApiModel.result.limit, viewModel.areaData.value?.result?.limit)
        Assert.assertEquals(areaApiModel.result.offset, viewModel.areaData.value?.result?.offset)
        Assert.assertEquals(areaApiModel.result.sort, viewModel.areaData.value?.result?.sort)
    }

    @Test
    fun testGetAreaData_fail() {
        coEvery { stubRepository.getAreaData() }.answers{
            throw Exception("NoAreaData")
        }

        val viewModel = EventViewModel(stubRepository)
        viewModel.getAreaData()

        Assert.assertEquals("NoAreaData", viewModel.areaError.value?.message)
    }

    @Test
    fun testGetPlantData_success() {
        val plantApiModel = PlantApiModel(PlantApiModel.Result(2000, 20, 23, mutableListOf(), "Test"))

        coEvery { stubRepository.getPlantData(any()) }.answers{
            plantApiModel
        }

        val viewModel = EventViewModel(stubRepository)
        viewModel.getPlantaData("")

        Assert.assertEquals(plantApiModel.result.count, viewModel.plantData.value?.result?.count)
        Assert.assertEquals(plantApiModel.result.limit, viewModel.plantData.value?.result?.limit)
        Assert.assertEquals(plantApiModel.result.offset, viewModel.plantData.value?.result?.offset)
        Assert.assertEquals(plantApiModel.result.sort, viewModel.plantData.value?.result?.sort)
    }

    @Test
    fun testGetPlantData_fail() {
        coEvery { stubRepository.getPlantData(any()) }.answers{
            throw Exception("NoPlantData")
        }

        val viewModel = EventViewModel(stubRepository)
        viewModel.getPlantaData("")

        Assert.assertEquals("NoPlantData", viewModel.plantError.value?.message)
    }
}