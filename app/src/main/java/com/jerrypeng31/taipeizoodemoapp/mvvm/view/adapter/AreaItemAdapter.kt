package com.jerrypeng31.taipeizoodemoapp.mvvm.view.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding
import com.bumptech.glide.Glide
import com.jerrypeng31.taipeizoodemoapp.BR
import com.jerrypeng31.taipeizoodemoapp.databinding.*
import com.jerrypeng31.taipeizoodemoapp.mvvm.model.AreaApiModel
import com.jerrypeng31.taipeizoodemoapp.mvvm.model.PlantApiModel
import com.jerrypeng31.taipeizoodemoapp.mvvm.viewmodel.EventViewModel


class AreaItemAdapter(private val viewModel: EventViewModel, private val areaDataResult: AreaApiModel.Result.AreaDataResult?) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    companion object{
        val TYPE_INTRODUCTION = 1
        val TYPE_TITLE = 2
        val TYPE_ITEM = 3
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        var binding: ViewBinding?
        return when(viewType){
            TYPE_INTRODUCTION -> {
                binding = AdapterItemAreaIntroductionBinding.inflate(LayoutInflater.from(parent.context), parent, false)
                VH_Introduction(areaDataResult, binding)
            }
            TYPE_TITLE -> {
                binding = AdapterItemAreaTitleBinding.inflate(LayoutInflater.from(parent.context), parent, false)
                VH_Title(binding)
            }
            else ->{
                binding = AdapterItemAreaBinding.inflate(LayoutInflater.from(parent.context), parent, false)
                VH_Item(binding)
            }
        }
    }

    override fun getItemCount(): Int {
        val size = getItemList().size
        return when(size){
            0 -> 1
            else -> 2 + size
        }
    }

    override fun getItemViewType(position: Int): Int {
        return when(position){
            0 -> TYPE_INTRODUCTION
            1 -> TYPE_TITLE
            else -> TYPE_ITEM
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when(holder.itemViewType){
            TYPE_INTRODUCTION ->{
                val vh_introduction = holder as? VH_Introduction
                vh_introduction?.init()
                vh_introduction?.viewBinding?.setVariable(BR.dataResult, areaDataResult)
                vh_introduction?.viewBinding?.setVariable(BR.eventViewModel, viewModel)
                vh_introduction?.viewBinding?.executePendingBindings()
            }
            TYPE_ITEM ->{
                val vh_item = holder as? VH_Item
                val data = getItemList().get(position - 2)
                vh_item?.viewBinding?.setVariable(BR.dataResult, data)
                vh_item?.viewBinding?.setVariable(BR.eventViewModel, viewModel)
                vh_item?.viewBinding?.executePendingBindings()
            }
        }

    }

    class VH_Introduction(val areaDataResult: AreaApiModel.Result.AreaDataResult?,
                          val viewBinding: AdapterItemAreaIntroductionBinding): RecyclerView.ViewHolder(viewBinding.root){
        fun init(){
            areaDataResult?.let {
                Glide.with(viewBinding.root.context).load(it.ePicURL).centerCrop().into(viewBinding.imageViewIcon)
                viewBinding.textViewIntroduction.text = it.eInfo
                viewBinding.textViewTime.text = it.eMemo
                viewBinding.textViewArea.text = it.eCategory
            }
        }
    }

    class VH_Title(val viewBinding: AdapterItemAreaTitleBinding) : RecyclerView.ViewHolder(viewBinding.root)
    class VH_Item(val viewBinding: AdapterItemAreaBinding) : RecyclerView.ViewHolder(viewBinding.root)

    fun getItemList(): List<PlantApiModel.Result.PlantDataResult>{
        val checkNoNameSameMap = mutableMapOf<String, PlantApiModel.Result.PlantDataResult>()
        val realList = viewModel.plantData.value?.content?.result?.results
        realList?.let {
            for (item in it){
                if(! checkNoNameSameMap.containsKey(item.FNameCh)){
                    checkNoNameSameMap.put(item.FNameCh, item)
                }
            }
        }

        return checkNoNameSameMap.values.toList()
    }
}