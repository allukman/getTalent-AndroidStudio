package id.smartech.get_talent.activity.skill

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import id.smartech.get_talent.R
import id.smartech.get_talent.activity.home.OnRecyclerViewClickListener
import id.smartech.get_talent.databinding.ItemSkillBinding

class SkillAdapter(private val items: ArrayList<SkillModel>, private val onRecyclerViewClickListener: OnRecyclerViewClickListener): RecyclerView.Adapter<SkillAdapter.SkillHolder>() {

    fun addList(list: List<SkillModel>){
        items.clear()
        items.addAll(list)
        notifyDataSetChanged()
    }

    class SkillHolder(val binding: ItemSkillBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SkillHolder {
        return SkillHolder(
            DataBindingUtil.inflate(
                LayoutInflater.from(parent.context),
                R.layout.item_skill, parent, false))
    }

    override fun getItemCount(): Int = items.size

    override fun onBindViewHolder(holder: SkillHolder, position: Int) {
        val item = items[position]

        holder.binding.skillName.text = item.skillName

        holder.itemView.setOnClickListener {
            onRecyclerViewClickListener.onRecyclerViewItemClicked(position)
        }
    }
}