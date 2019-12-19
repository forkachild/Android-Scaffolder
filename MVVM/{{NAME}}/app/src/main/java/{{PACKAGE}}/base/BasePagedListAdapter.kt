package {{PACKAGE}}.base

import android.content.Context
import android.view.LayoutInflater
import androidx.databinding.ViewDataBinding
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView

abstract class BasePagedListAdapter<Data, VH : BasePagedListAdapter.BaseViewHolder<*>>(
    protected val context: Context,
    differ: DiffUtil.ItemCallback<Data>
) : PagedListAdapter<Data, VH>(differ) {

    protected val inflater: LayoutInflater = LayoutInflater.from(context)

    abstract class BaseViewHolder<V : ViewDataBinding>(
        protected val binding: V
    ) : RecyclerView.ViewHolder(binding.root)

}