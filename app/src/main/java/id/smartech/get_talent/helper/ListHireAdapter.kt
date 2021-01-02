package id.smartech.get_talent.helper

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import id.smartech.get_talent.R
import id.smartech.get_talent.data.HireEngineerModel
import id.smartech.get_talent.databinding.ItemHireBinding

class ListHireAdapter : RecyclerView.Adapter<ListHireAdapter.hireHolder>() {
    private var items = mutableListOf<HireEngineerModel>()

    fun addList(list : List<HireEngineerModel>) {
        items.clear()
        items.addAll(list)
        notifyDataSetChanged()
    }

    class hireHolder(val binding: ItemHireBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): hireHolder {
        return hireHolder(
            DataBindingUtil.inflate(
                (LayoutInflater.from(parent.context)),
                R.layout.item_hire, parent, false
            )
        )
    }

    override fun getItemCount(): Int = items.size

    override fun onBindViewHolder(holder: hireHolder, position: Int) {
        val item = items[position]

        val price = "Budget : Rp. ${item.hirePrice}"
        val deadlineSplit = item.projectDeadline!!.split("T")[0]
        val deadline = "Deadline: $deadlineSplit"


        holder.binding.hirePjName.text = item.projectName
        holder.binding.hirePjMessage.text = item.hireMessage
        holder.binding.hirePrice.text = price
        holder.binding.hirePjDeadline.text = deadline
        holder.binding.hireStatus.text = item.hireStatus

    }
}