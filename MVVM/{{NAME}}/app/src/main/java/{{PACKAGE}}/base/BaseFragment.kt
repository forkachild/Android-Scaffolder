package {{PACKAGE}}.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import {{PACKAGE}}.BR
import {{PACKAGE}}.utils.observeEvent

abstract class BaseFragment<V : ViewDataBinding, VM : BaseViewModel> : Fragment() {

    abstract val layoutRes: Int
    abstract val viewModel: VM

    private lateinit var _binding: V
    protected val binding: V
        get() = _binding

    final override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        if (!::_binding.isInitialized) {
            _binding = DataBindingUtil.inflate(inflater, layoutRes, container, false)
            _binding.lifecycleOwner = this
            _binding.setVariable(BR.vm, viewModel)
            viewModel.onAttach()
            onCreated(savedInstanceState)
        }

        return _binding.root
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
        viewModel.navigateBack.observeEvent(this) {
            findNavController().navigateUp()
        }
    }

}