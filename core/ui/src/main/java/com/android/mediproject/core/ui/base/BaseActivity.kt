package com.android.mediproject.core.ui.base

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil.setContentView
import androidx.databinding.ViewDataBinding

abstract class BaseActivity<T : ViewDataBinding>(
    val bindingFactory: (LayoutInflater) -> T
) : AppCompatActivity() {

    protected lateinit var binding: T
        private set

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = bindingFactory(layoutInflater)
        setContentView(binding.root)
        binding.lifecycleOwner = this
        afterBinding()
    }

    abstract fun afterBinding()

    fun log(str: String) = Log.e("wap", str) //for test

    fun toast(str: String) = Toast.makeText(this, str, Toast.LENGTH_LONG).show()
}