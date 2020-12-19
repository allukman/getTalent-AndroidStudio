package id.smartech.get_talent.helper

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import id.smartech.get_talent.R
import id.smartech.get_talent.data.Experience

class ExperienceRecyclerViewAdapter(
    private val experience: List <Experience>
): RecyclerView.Adapter<ExperienceRecyclerViewAdapter.ImageViewHolder>() {


    class ImageViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val img = itemView.findViewById<ImageView>(R.id.xp_logo)
        val title = itemView.findViewById<TextView>(R.id.xp_job_title)
        val companyName = itemView.findViewById<TextView>(R.id.xp_company_name)
        val date = itemView.findViewById<TextView>(R.id.xp_start_end)
        val deskripsi = itemView.findViewById<TextView>(R.id.xp_deskripsi)

        fun bindView(data: Experience) {
            img.setImageResource(data.imageSrc)
            title.text = data.jobTitle
            companyName.text = data.CompanyName
            date.text = data.date
            deskripsi.text = data.deskripsi.toString()
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImageViewHolder =
        ExperienceRecyclerViewAdapter.ImageViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.item_experience, parent, false)
        )

    override fun getItemCount(): Int = experience.size

    override fun onBindViewHolder(holder: ExperienceRecyclerViewAdapter.ImageViewHolder, position: Int) {
        holder.bindView(experience[position])
    }

}