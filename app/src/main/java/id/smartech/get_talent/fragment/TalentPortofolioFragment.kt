package id.smartech.get_talent.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.ListAdapter
import id.smartech.get_talent.R
import id.smartech.get_talent.data.Image
import id.smartech.get_talent.databinding.FragmentTalentPortofolioBinding
import id.smartech.get_talent.helper.PortofolioRecyclerViewAdapter

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [TalentPortofolioFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class TalentPortofolioFragment : Fragment() {

    private lateinit var binding: FragmentTalentPortofolioBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_talent_portofolio, container, false)

        val images = listOf<Image>(
            Image(R.drawable.portofolio1),
            Image(R.drawable.portofolio2),
            Image(R.drawable.portofolio3)
        )

        binding.imagesRecyclerView.layoutManager = LinearLayoutManager(activity)
        binding.imagesRecyclerView.setHasFixedSize(true)
        binding.imagesRecyclerView.adapter = PortofolioRecyclerViewAdapter(images)

        return binding.root
    }


}