package id.smartech.get_talent.activity.portofolio

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import id.smartech.get_talent.R
import id.smartech.get_talent.activity.portofolio.createPortofolio.CreatePortofolioActivity
import id.smartech.get_talent.data.PortofolioModel
import id.smartech.get_talent.databinding.FragmentTalentPortofolioBinding
import id.smartech.get_talent.helper.PortofolioAdapter
import id.smartech.get_talent.remote.ApiClient
import id.smartech.get_talent.service.PortofolioApiService
import id.smartech.get_talent.util.Constant
import id.smartech.get_talent.util.PrefHelper
import kotlinx.coroutines.*

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [TalentPortofolioFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class TalentPortofolioFragment : Fragment(), PortofolioClickListener {

    private lateinit var binding: FragmentTalentPortofolioBinding
    private lateinit var coroutineScope: CoroutineScope
    private lateinit var prefHelper: PrefHelper
    private lateinit var service: PortofolioApiService
    var listPortofolio = ArrayList<PortofolioModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_talent_portofolio, container, false)
        coroutineScope = CoroutineScope(Job() + Dispatchers.Main)
        prefHelper = PrefHelper(context = context)
        service = ApiClient.getApiClient(requireContext())!!.create(PortofolioApiService::class.java)

        binding.imagesRecyclerView.layoutManager = LinearLayoutManager(activity, RecyclerView.VERTICAL, false)
        binding.imagesRecyclerView.adapter = PortofolioAdapter(listPortofolio, this)

        binding.btnAddPortofolio.setOnClickListener {
            val intent = Intent (activity, CreatePortofolioActivity::class.java)
            activity!!.startActivity(intent)
        }

        getPortofolioByEngId()
        return binding.root
    }

    private fun getPortofolioByEngId(){
        coroutineScope.launch {
            val response = withContext(Dispatchers.IO) {
                try {
                    service.getPortofolioByEngId(prefHelper.getString(Constant.EN_ID))
                } catch (e:Throwable) {
                    e.printStackTrace()
                }
            }

            if (response is GetPortofolioByEngIdResponse) {
                val list = response.data.map {
                    PortofolioModel(
                        it.portofolioId,
                        it.engineerId,
                        it.appName,
                        it.appDescription,
                        it.prLinkPub,
                        it.prLinkRepo,
                        it.workPlace,
                        it.appType,
                        it.portofolioPhoto
                    )
                }
                (binding.imagesRecyclerView.adapter as PortofolioAdapter).addList(list)
            } else {
                Log.d("Portofolio" , "failed to get portofolio")
            }
        }
    }

    override fun onDestroy() {
        coroutineScope.cancel()
        super.onDestroy()
    }

    override fun onPortofoliItemClicked(position: Int) {
        prefHelper.put(Constant.PR_ID_CLICK, listPortofolio[position].prId)
        Toast.makeText(requireContext(), prefHelper.getString(Constant.PR_ID_CLICK), Toast.LENGTH_SHORT).show()
    }

}