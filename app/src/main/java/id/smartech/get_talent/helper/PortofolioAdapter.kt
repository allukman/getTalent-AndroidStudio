package id.smartech.get_talent.helper

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import id.smartech.get_talent.R
import id.smartech.get_talent.data.PortofolioModel
import id.smartech.get_talent.databinding.ItemPortofolioBinding

class PortofolioAdapter(private val items: ArrayList<PortofolioModel>, private val portofolioClickListener: PortofolioClickListener): RecyclerView.Adapter<PortofolioAdapter.PortofolioHolder>(){
//    private var items = mutableListOf<PortofolioModel>()

    fun addList(list: List<PortofolioModel>) {
        items.clear()
        items.addAll(list)
        notifyDataSetChanged()
    }

    class PortofolioHolder(val binding: ItemPortofolioBinding): RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PortofolioHolder {
        return PortofolioHolder(
            DataBindingUtil.inflate(
                (LayoutInflater.from(parent.context)),
                R.layout.item_portofolio,
                parent, false
            )
        )
    }

    override fun getItemCount(): Int = items.size

    override fun onBindViewHolder(holder: PortofolioHolder, position: Int) {
        val item = items[position]
        val img = "http://174.129.47.146:4000/image/${item.prPhoto}"

        Glide.with(holder.itemView)
            .load(img)
            .placeholder(R.drawable.portofolio1)
            .error(R.drawable.portofolio1)
            .into(holder.binding.image)

        holder.itemView.setOnClickListener {
            portofolioClickListener.onPortofoliItemClicked(position)
        }
    }
}