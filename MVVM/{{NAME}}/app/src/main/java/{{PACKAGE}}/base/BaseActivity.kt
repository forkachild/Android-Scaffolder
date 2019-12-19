package {{PACKAGE}}.base

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import {{PACKAGE}}.BR

abstract class BaseActivity<V : ViewDataBinding, VM : BaseViewModel> : AppCompatActivity() {

    abstract val layoutRes: Int
    abstract val viewModel: VM

    private lateinit var _binding: V
    protected val binding: V
        get() = _binding

    final override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = DataBindingUtil.setContentView(this, layoutRes)
        _binding.lifecycleOwner = this
        _binding.setVariable(BR.vm, viewModel)
        viewModel.onAttach()
        onCreated(savedInstanceState)
    }

    override fun onStart() {
        super.onStart()
        viewModel.onStart()
    }

    override fun onResume() {
        super.onResume()
        viewModel.onForeground()
    }

    override fun onPause() {
        super.onPause()
        viewModel.onBackground()
    }

    override fun onStop() {
        super.onStop()
        viewModel.onStop()
    }

    override fun onDestroy() {
        super.onDestroy()
        viewModel.onDetach()
    }

    open fun onCreated(savedInstanceState: Bundle?) {

    }

}