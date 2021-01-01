package id.smartech.get_talent.helper

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import id.smartech.get_talent.R
import id.smartech.get_talent.databinding.ItemProjectCompanyBinding
import id.smartech.get_talent.data.ProjectCompanyModel

class ListProjectAdapter : RecyclerView.Adapter<ListProjectAdapter.ProjectHolder>() {
    private var items = mutableListOf<ProjectCompanyModel>()

    fun addList(list : List<ProjectCompanyModel>) {
        items.clear()
        items.addAll(list)
        notifyDataSetChanged()
    }

    class ProjectHolder(val binding : ItemProjectCompanyBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProjectHolder {
        return ProjectHolder(
            DataBindingUtil.inflate(
                (LayoutInflater.from(parent.context)),
                R.layout.item_project_company,
                parent,
                false
            )
        )
    }

    override fun getItemCount(): Int = items.size

    override fun onBindViewHolder(holder: ProjectHolder, position: Int) {
        val item = items[position]
        val image = item.projectImage.split(".")[0]
        val deadlineSplit = item.projectDeadline.split("T")[0]

        holder.binding.tvName.text = item.projectName
        holder.binding.tvDecription.text = item.projectDesc
        holder.binding.tvDeadline.text = deadlineSplit
        holder.binding.tvImageProject.text = image
    }
}