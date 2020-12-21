package id.smartech.get_talent.fragment

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import id.smartech.get_talent.R
import id.smartech.get_talent.activity.ProfileEngineerActivity
import id.smartech.get_talent.databinding.FragmentHomeBinding
import id.smartech.get_talent.helper.WebviewActivity

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [HomeFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class HomeFragment : Fragment() {

    private lateinit var binding: FragmentHomeBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_home, container, false)
        binding.ivWebdev1.setOnClickListener {
            val intent = Intent (activity, ProfileEngineerActivity::class.java)
            activity!!.startActivity(intent)
        }

        binding.ivAndroid1.setOnClickListener {
            val intent = Intent (activity, ProfileEngineerActivity::class.java)
            activity!!.startActivity(intent)
        }

        return binding.root
    }


}