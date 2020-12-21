package id.smartech.get_talent.helper

import android.provider.ContactsContract
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import id.smartech.get_talent.R
import id.smartech.get_talent.data.Experience
import id.smartech.get_talent.data.Image
import id.smartech.get_talent.data.Project
import java.nio.file.Files.size

class ProjectRecyclerViewAdapter (
    private val project: List<Project>
): RecyclerView.Adapter<ProjectRecyclerViewAdapter.DataViewHolder>(){

    class DataViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val image = itemView.findViewById<ImageView>(R.id.pj_photo)
        val deskripsi = itemView.findViewById<TextView>(R.id.pj_deskripsi)
        val companyName = itemView.findViewById<TextView>(R.id.pj_com)
        val deadline = itemView.findViewById<TextView>(R.id.pj_deadline)

        fun bindView(data: Project) {
            image.setImageResource(data.imageSrc)
            deskripsi.text = data.deskripsi
            companyName.text = data.companyName
            deadline.text = data.deadline
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DataViewHolder =
        ProjectRecyclerViewAdapter.DataViewHolder(LayoutInflater.from(parent.context)
            .inflate(R.layout.item_project, parent, false))

    override fun getItemCount(): Int = project.size

    override fun onBindViewHolder(holder: DataViewHolder, position: Int) {
        holder.bindView(project[position])
    }
}