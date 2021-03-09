package com.jerrypeng31.taipeizoodemoapp.mvvm.view.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import com.jerrypeng31.taipeizoodemoapp.mvvm.view.adapter.ZooItemAdapter
import com.jerrypeng31.mvvmtest.EventViewModelFactory
import com.jerrypeng31.mvvmtest.Repository
import com.jerrypeng31.taipeizoodemoapp.R
import com.jerrypeng31.taipeizoodemoapp.databinding.FragmentZooBinding
import com.jerrypeng31.taipeizoodemoapp.keys.Keys
import com.jerrypeng31.taipeizoodemoapp.mvvm.model.AreaApiModel
import com.jerrypeng31.taipeizoodemoapp.mvvm.view.MainActivity
import com.jerrypeng31.taipeizoodemoapp.mvvm.viewmodel.EventObserver
import com.jerrypeng31.taipeizoodemoapp.mvvm.viewmodel.EventViewModel
import com.jerrypeng31.taipeizoodemoapp.retrofit.RetrofitUtil

class ZooFragment : Fragment() {
    companion object{
        const val TAG = "ZooFragment"
    }

    var dataBinding: FragmentZooBinding? = null

    private val eventViewModel: EventViewModel by activityViewModels{
        EventViewModelFactory(Repository(RetrofitUtil.getApiService()))
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        dataBinding = DataBindingUtil.inflate<FragmentZooBinding>(inflater, R.layout.fragment_zoo, container, false)
        return dataBinding?.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        initEventViewModel()
    }

    override fun onDestroy() {
        eventViewModel.clearAreaDataList()
        super.onDestroy()
    }

    override fun onHiddenChanged(hidden: Boolean) {
        super.onHiddenChanged(hidden)
        if(! hidden){
            initToolBar()
        }
    }

    fun initToolBar(){
        if(activity is MainActivity){
            (activity as MainActivity).toolbar(R.drawable.menu, getString(R.string.zoo))
        }
    }

    fun initEventViewModel(){
        initToolBar()

        val recyclerView = dataBinding?.recyclerViewZoo
        val adapter = ZooItemAdapter(eventViewModel)

        recyclerView?.layoutManager = LinearLayoutManager(context)
        val itemAnimator = DefaultItemAnimator()
        itemAnimator.supportsChangeAnimations = false
        recyclerView?.itemAnimator = itemAnimator

        recyclerView?.adapter = adapter

        eventViewModel.getAreaData()
        eventViewModel.areaData.observe(viewLifecycleOwner, EventObserver {
            adapter.notifyDataSetChanged()
        })

        eventViewModel.areaDataClick.observe(viewLifecycleOwner, EventObserver{
            itemClick(it)
        })
    }

    fun itemClick(areaDataResult: AreaApiModel.Result.AreaDataResult?){
        val transaction = parentFragmentManager.beginTransaction()
        val currentFragment = parentFragmentManager.findFragmentByTag(TAG)
        currentFragment?.let {
            transaction.hide(it)
        }

        val areaFragment = AreaFragment()
        val bundle = Bundle()
        bundle.putSerializable(Keys.FRAGMENT_KEY_PARAM, areaDataResult)
        areaFragment.arguments = bundle

        transaction.add(R.id.frameLayout_container, areaFragment, AreaFragment.TAG)
            .addToBackStack(AreaFragment.TAG).commit()
    }
}