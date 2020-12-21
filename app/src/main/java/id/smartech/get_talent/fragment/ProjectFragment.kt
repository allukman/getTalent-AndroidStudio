package id.smartech.get_talent.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import id.smartech.get_talent.R
import id.smartech.get_talent.data.Project
import id.smartech.get_talent.databinding.FragmentProjectBinding
import id.smartech.get_talent.helper.ProjectRecyclerViewAdapter

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [ProjectFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class ProjectFragment : Fragment() {

    private lateinit var binding: FragmentProjectBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_project, container, false)

        val data = listOf<Project>(
            Project(R.drawable.portofolio1, "Membuat aplikasi kalkulator","PT. SUKA MAJU", "3 bulan" ),
            Project(R.drawable.portofolio2, "Membuat aplikasi dice roller","PT. CIPTA KUSUMA", "4 bulan" ),
            Project(R.drawable.portofolio3, "Membuat aplikasi Alarm","PT. PERMATA MERAH", "2 bulan" ),
            Project(R.drawable.portofolio1, "Membuat aplikasi dengan laravel","PT. BALOON", "6 bulan" ),
            Project(R.drawable.portofolio2, "Membuat aplikasi adnroid","PT. SURYA CITRA", "9 bulan" ),
            Project(R.drawable.portofolio3, "Membuat aplikasi kalkulator","PT. NET", "1 bulan" )
        )

        binding.projectRecyclerView.layoutManager = LinearLayoutManager(activity)
        binding.projectRecyclerView.setHasFixedSize(true)
        binding.projectRecyclerView.adapter = ProjectRecyclerViewAdapter(data)

        return binding.root
    }


}