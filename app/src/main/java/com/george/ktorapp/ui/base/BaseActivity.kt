package com.george.ktorapp.ui.base

import android.os.Bundle
import android.view.LayoutInflater
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.viewbinding.ViewBinding
import java.lang.reflect.ParameterizedType

abstract class BaseActivity<B : ViewBinding,VM : ViewModel>(
    val bindingFactory: (LayoutInflater) -> B
) : AppCompatActivity() {

    abstract val TAG : String
    val binding: B by lazy { bindingFactory(layoutInflater) }
    val viewModel: VM by lazy { ViewModelProvider(this).get(getViewModelClass()) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        setListener()
    }

    abstract fun setListener()

    private fun getViewModelClass(): Class<VM> {
        val type = (javaClass.genericSuperclass as ParameterizedType).actualTypeArguments[0]
        return type as Class<VM>
    }

}