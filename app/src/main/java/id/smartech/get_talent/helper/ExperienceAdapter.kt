package id.smartech.get_talent.helper

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import id.smartech.get_talent.R
import id.smartech.get_talent.activity.home.OnRecyclerViewClickListener
import id.smartech.get_talent.data.ExperienceModel
import id.smartech.get_talent.databinding.ItemExperienceBinding

class ExperienceAdapter(private val items: ArrayList<ExperienceModel>, private val onRecyclerViewClickListener: OnRecyclerViewClickListener): RecyclerView.Adapter<ExperienceAdapter.ExperienceHolder>(){
//   private var items = mutableListOf<ExperienceModel>()

    fun addList(list: List<ExperienceModel>) {
        items.clear()
        items.addAll(list)
        notifyDataSetChanged()
    }

    class ExperienceHolder(val binding: ItemExperienceBinding): RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ExperienceHolder {
        return ExperienceHolder(
            DataBindingUtil.inflate(
                (LayoutInflater.from(parent.context)),
                R.layout.item_experience,
                parent, false
            )
        )
    }

    override fun getItemCount(): Int = items.size

    override fun onBindViewHolder(holder: ExperienceHolder, position: Int) {
        val item = items[position]
        val img = "http://174.129.47.146:4000/image/${item.exPhoto}"
        val exStart = item.exStart!!.split("T")[0]
        val exEnd = item.exEnd!!.split("T")[0]

        holder.binding.xpCompanyName.text = item.exCompany
        holder.binding.xpJobTitle.text = item.exPosisi
        holder.binding.xpStartDate.text = exStart
        holder.binding.xpEndDate.text = exEnd
        holder.binding.xpDeskripsi.text = item.exDeskripsi

        Glide.with(holder.itemView)
            .load(img)
            .placeholder(R.drawable.profile)
            .error(R.drawable.profile)
            .into(holder.binding.xpLogo)

        holder.itemView.setOnClickListener {
            onRecyclerViewClickListener.onRecyclerViewItemClicked(position)
        }
    }
}