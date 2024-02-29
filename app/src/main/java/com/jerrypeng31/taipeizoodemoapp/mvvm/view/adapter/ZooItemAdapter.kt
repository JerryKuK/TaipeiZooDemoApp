package com.jerrypeng31.taipeizoodemoapp.mvvm.view.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.jerrypeng31.taipeizoodemoapp.BR
import com.jerrypeng31.taipeizoodemoapp.databinding.AdapterItemZooBinding
import com.jerrypeng31.taipeizoodemoapp.mvvm.model.AreaApiModel
import com.jerrypeng31.taipeizoodemoapp.mvvm.viewmodel.EventViewModel


class ZooItemAdapter(private val viewModel: EventViewModel) :
    RecyclerView.Adapter<ZooItemAdapter.VH>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        val binding = AdapterItemZooBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return VH(binding)
    }

    override fun getItemCount(): Int = getItemList().size

    override fun onBindViewHolder(holder: VH, position: Int) {
        val data = getItemList()[position] ?: return
        holder.viewBinding.setVariable(BR.dataResult, data)
        holder.viewBinding.setVariable(BR.eventViewModel, viewModel)
        holder.viewBinding.executePendingBindings()
    }

    class VH(val viewBinding: AdapterItemZooBinding) : RecyclerView.ViewHolder(viewBinding.root)

    fun getItemList(): List<AreaApiModel.Result.AreaDataResult?>{
        return viewModel.areaData.value?.result?.results ?: listOf()
    }
}