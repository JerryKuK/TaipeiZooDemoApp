package com.jerrypeng31.taipeizoodemoapp.mvvm.view.fragment

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.jerrypeng31.mvvmtest.EventViewModelFactory
import com.jerrypeng31.mvvmtest.Repository
import com.jerrypeng31.taipeizoodemoapp.R
import com.jerrypeng31.taipeizoodemoapp.databinding.FragmentAreaBinding
import com.jerrypeng31.taipeizoodemoapp.keys.Keys
import com.jerrypeng31.taipeizoodemoapp.mvvm.model.AreaApiModel
import com.jerrypeng31.taipeizoodemoapp.mvvm.model.PlantApiModel
import com.jerrypeng31.taipeizoodemoapp.mvvm.view.MainActivity
import com.jerrypeng31.taipeizoodemoapp.mvvm.view.adapter.AreaItemAdapter
import com.jerrypeng31.taipeizoodemoapp.mvvm.viewmodel.Event
import com.jerrypeng31.taipeizoodemoapp.mvvm.viewmodel.EventObserver
import com.jerrypeng31.taipeizoodemoapp.mvvm.viewmodel.EventViewModel
import com.jerrypeng31.taipeizoodemoapp.retrofit.RetrofitUtil

class AreaFragment : Fragment() {
    companion object{
        const val TAG = "AreaFragment"
    }
    var param: Any? = null
    var dataBinding: FragmentAreaBinding? = null

    private val eventViewModel: EventViewModel by activityViewModels{
        EventViewModelFactory(Repository(RetrofitUtil.getApiService()))
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        param = arguments?.getSerializable(Keys.FRAGMENT_KEY_PARAM)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        dataBinding = DataBindingUtil.inflate<FragmentAreaBinding>(inflater, R.layout.fragment_area, container, false)
        return dataBinding?.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        initEventViewModel()
    }

    override fun onHiddenChanged(hidden: Boolean) {
        super.onHiddenChanged(hidden)
        if(! hidden){
            initToolBar()
        }
    }

    override fun onDestroy() {
        context?.let { Glide.get(it).clearMemory() }
        eventViewModel.clearPlantDataList()
        super.onDestroy()
    }

    fun initToolBar(){
        if(activity is MainActivity && param is AreaApiModel.Result.AreaDataResult){
            val areaDataResult = param as? AreaApiModel.Result.AreaDataResult
            val name = areaDataResult?.eName ?: ""

            (activity as MainActivity).toolbar(R.drawable.back, name, true){
                areaDataResult?.let {
                    eventViewModel.getPlantaData(name)
                }
                true
            }
        }
    }

    fun initEventViewModel(){
        initToolBar()

        val areaDataResult = param as? AreaApiModel.Result.AreaDataResult

        val recyclerView = dataBinding?.recyclerViewArea
        val adapter = AreaItemAdapter(eventViewModel, areaDataResult)

        recyclerView?.layoutManager = LinearLayoutManager(context)
        val itemAnimator = DefaultItemAnimator()
        itemAnimator.supportsChangeAnimations = false
        recyclerView?.itemAnimator = itemAnimator

        recyclerView?.adapter = adapter

        areaDataResult?.let {
            eventViewModel.getPlantaData(it.eName)
        }

        eventViewModel.plantData.observe(viewLifecycleOwner, EventObserver {
            adapter.notifyDataSetChanged()
        })

        eventViewModel.itemUrlClick.observe(viewLifecycleOwner, EventObserver{
            itemUrlClick(it)
        })

        eventViewModel.plantDataClick.observe(viewLifecycleOwner, EventObserver {
            itemClick(it)
        })

        eventViewModel.plantError.observe(viewLifecycleOwner, EventObserver{
            connectError(it)
        })
    }

    fun itemUrlClick(areaDataResult: AreaApiModel.Result.AreaDataResult?){
        val uri = Uri.parse(areaDataResult?.eURL)
        startActivity(Intent(Intent.ACTION_VIEW, uri))
    }

    fun itemClick(plantDataResult: PlantApiModel.Result.PlantDataResult?){
        val transaction = parentFragmentManager.beginTransaction()
        val currentFragment = parentFragmentManager.findFragmentByTag(AreaFragment.TAG)
        currentFragment?.let {
            transaction.hide(it)
        }

        val plantFragment = PlantFragment()
        val bundle = Bundle()
        bundle.putSerializable(Keys.FRAGMENT_KEY_PARAM, plantDataResult)
        plantFragment.arguments = bundle

        transaction.add(R.id.frameLayout_container, plantFragment, PlantFragment.TAG)
            .addToBackStack(AreaFragment.TAG).commit()
    }

    fun connectError(throwable: Throwable?){
        Toast.makeText(context, getString(R.string.connect_error_no_plant_data), Toast.LENGTH_LONG).show()
    }
}