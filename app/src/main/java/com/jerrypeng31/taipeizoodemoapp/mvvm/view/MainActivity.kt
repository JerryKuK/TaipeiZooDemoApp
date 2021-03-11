package com.jerrypeng31.taipeizoodemoapp.mvvm.view

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.databinding.BindingAdapter
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.jerrypeng31.taipeizoodemoapp.R
import com.jerrypeng31.taipeizoodemoapp.databinding.ActivityMainBinding
import com.jerrypeng31.taipeizoodemoapp.mvvm.view.fragment.ZooFragment

class MainActivity : AppCompatActivity() {
    lateinit var mainBinding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mainBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(mainBinding.root)

        supportFragmentManager.beginTransaction()
            .add(R.id.frameLayout_container, ZooFragment(), ZooFragment.TAG)
            .commit()
    }
    
    fun toolbar(drawableResId: Int, title: String, isMenuShow: Boolean, refreshClick: (MenuItem)->Boolean){
        val toolbar = mainBinding.toolbar
        toolbar.title = title
        toolbar.navigationIcon = ContextCompat.getDrawable(this, drawableResId)
        toolbar.menu.getItem(0).isVisible = isMenuShow
        toolbar.setNavigationOnClickListener{
            val checkZooFragment = checkFragment{
                it is ZooFragment
            }

            if(! checkZooFragment){
                onBackPressed()
            }
        }

        toolbar.setOnMenuItemClickListener {
            refreshClick(it)
        }
    }

    private fun checkFragment(checkFragment: (Fragment)-> Boolean): Boolean{
        for(item in supportFragmentManager.fragments){
            if(item.isVisible && checkFragment(item)){
                return true
            }
        }
        return false
    }
}

@BindingAdapter("imgUrl")
fun setPicture(imageView: ImageView, imgUrl: String) {
    Glide.with(imageView.getContext())
        .load(imgUrl)
        .placeholder(R.drawable.default_icon)
        .diskCacheStrategy(DiskCacheStrategy.ALL)
        .centerCrop()
        .into(imageView)
}