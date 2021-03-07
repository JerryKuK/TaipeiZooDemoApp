package com.jerrypeng31.taipeizoodemoapp.mvvm.view.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.jerrypeng31.taipeizoodemoapp.R
import com.jerrypeng31.taipeizoodemoapp.databinding.FragmentPlantBinding
import com.jerrypeng31.taipeizoodemoapp.keys.Keys
import com.jerrypeng31.taipeizoodemoapp.mvvm.model.PlantApiModel
import com.jerrypeng31.taipeizoodemoapp.mvvm.view.MainActivity

class PlantFragment : Fragment() {
    companion object{
        const val TAG = "PlantFragment"
    }

    var param: Any? = null
    var dataBinding: FragmentPlantBinding? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        param = arguments?.getSerializable(Keys.FRAGMENT_KEY_PARAM)

        dataBinding = FragmentPlantBinding.inflate(inflater, container, false)
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

    fun initToolBar(){
        if(activity is MainActivity && param is PlantApiModel.Result.PlantDataResult){
            (activity as MainActivity).toolbar(R.drawable.back, (param as PlantApiModel.Result.PlantDataResult).FNameCh)
        }
    }

    fun initEventViewModel(){
        initToolBar()

        if(param is PlantApiModel.Result.PlantDataResult){
            val dataResult = param as PlantApiModel.Result.PlantDataResult

            dataBinding?.imageViewIcon?.let {
                Glide.with(it.context)
                    .load(dataResult.fPic01URL)
                    .placeholder(R.drawable.default_icon)
                    .centerCrop()
                    .into(it)
            }

            val stringBuffer = StringBuffer()
            stringBuffer.append(dataResult.FNameCh + "\n")
            stringBuffer.append(dataResult.fNameEn + "\n\n")
            stringBuffer.append(getString(R.string.alsoknown) + "\n")
            stringBuffer.append(dataResult.fAlsoKnown + "\n\n")
            stringBuffer.append(getString(R.string.brief) + "\n")
            stringBuffer.append(dataResult.fBrief + "\n\n")
            stringBuffer.append(getString(R.string.feature) + "\n")
            stringBuffer.append(dataResult.fFeature + "\n\n")
            stringBuffer.append(getString(R.string.function_application) + "\n")
            stringBuffer.append(dataResult.fFunction_Application + "\n\n")
            stringBuffer.append(getString(R.string.last_update) + dataResult.fUpdate)

            dataBinding?.textViewContent?.text = stringBuffer.toString()
        }

    }
}