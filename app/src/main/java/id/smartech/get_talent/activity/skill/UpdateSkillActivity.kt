package id.smartech.get_talent.activity.skill

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import id.smartech.get_talent.R
import id.smartech.get_talent.activity.main.EngineerMainActivity
import id.smartech.get_talent.activity.response.HelperResponse
import id.smartech.get_talent.databinding.ActivityUpdateSkillBinding
import id.smartech.get_talent.remote.ApiClient
import id.smartech.get_talent.service.SkillApiService
import id.smartech.get_talent.util.Constant
import id.smartech.get_talent.util.PrefHelper
import kotlinx.coroutines.*

class UpdateSkillActivity : AppCompatActivity() {
    private lateinit var binding: ActivityUpdateSkillBinding
    private lateinit var coroutineScope: CoroutineScope
    private lateinit var service: SkillApiService
    private lateinit var prefHelper: PrefHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_update_skill)
        coroutineScope = CoroutineScope(Job() +Dispatchers.Main)
        prefHelper = PrefHelper(this)
        service = ApiClient.getApiClient(this)!!.create(SkillApiService::class.java)
        val skillId = prefHelper.getInteger(Constant.SK_ID_CLICK)

        getSkillById(skillId)

        binding.btnUpdateSkill.setOnClickListener {
            val skillName = binding.etUpdateSkill.text.toString()

            if (skillName.isEmpty()) {
                Toast.makeText(this, "All field must be filled", Toast.LENGTH_SHORT).show()
            } else {
                updateSkill(skillId, skillName)
                moveIntent()
            }
        }

        binding.btnSkillDelete.setOnClickListener {
            deleteSkill(skillId)
            moveIntent()
        }

    }

    private fun getSkillById(skillId: Int) {
        coroutineScope.launch {
            val result = withContext(Dispatchers.IO) {
                try {
                    service.getSkillById(skillId)
                } catch (e:Throwable) {
                    e.printStackTrace()
                }
            }

            if (result is GetSkillByIdResponse) {
                binding.model = result.data
            }
        }
    }

    private fun updateSkill(skillId: Int, skillName: String) {
        coroutineScope.launch {
            val result = withContext(Dispatchers.IO) {
                try {
                    service.updateSkill(skillId, skillName)
                } catch (e:Throwable) {
                    e.printStackTrace()
                }
            }

            if (result is HelperResponse) {
                Toast.makeText(this@UpdateSkillActivity, "Success add skill", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this@UpdateSkillActivity, "failed to update skill", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun deleteSkill(skillId: Int){
        coroutineScope.launch {
            val result = withContext(Dispatchers.IO) {
                try {
                    service.deleteSkill(skillId)
                } catch (e:Throwable) {
                    e.printStackTrace()
                }
            }

            if (result is HelperResponse) {
                Toast.makeText(this@UpdateSkillActivity, "Success delete skill", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this@UpdateSkillActivity, "failed to delete skill", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun moveIntent(){
        startActivity(Intent(Intent(this, EngineerMainActivity::class.java)))
    }


}