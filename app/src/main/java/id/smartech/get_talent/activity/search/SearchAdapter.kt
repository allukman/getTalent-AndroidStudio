package id.smartech.get_talent.activity.search

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import id.smartech.get_talent.R
import id.smartech.get_talent.activity.home.OnRecyclerViewClickListener
import id.smartech.get_talent.data.EngineerModel
import id.smartech.get_talent.databinding.ItemHomeBinding

class SearchAdapter(private val items: ArrayList<EngineerModel>,private val onRecyclerViewClickListener: OnRecyclerViewClickListener)
    : RecyclerView.Adapter<SearchAdapter.SearchHolder>(){

    fun addList(list: List<EngineerModel>) {
        items.clear()
        items.addAll(list)
        notifyDataSetChanged()
    }

    class SearchHolder(val binding: ItemHomeBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchHolder {
        return SearchHolder(
            DataBindingUtil.inflate(
                (LayoutInflater.from(parent.context)),
                R.layout.item_home,
                parent, false
            )
        )
    }

    override fun getItemCount(): Int = items.size

    override fun onBindViewHolder(holder: SearchHolder, position: Int) {

        val item = items[position]
        val img = "http://174.129.47.146:4000/image/${item.engineerPhoto}"

        holder.binding.engNameTitle1.text = item.accountName
        holder.binding.engTitle1.text = item.engineerJobTitle

        Glide.with(holder.itemView)
            .load(img)
            .placeholder(R.drawable.profile)
            .error(R.drawable.profile)
            .into(holder.binding.engPhotoTitle1)

        holder.itemView.setOnClickListener {
            onRecyclerViewClickListener.onRecyclerViewItemClicked(position)
        }
    }
}