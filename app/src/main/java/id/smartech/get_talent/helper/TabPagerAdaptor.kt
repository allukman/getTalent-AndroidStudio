package id.smartech.get_talent.helper

import android.os.Parcelable
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import id.smartech.get_talent.activity.experience.TalentExperienceFragment
import id.smartech.get_talent.fragment.TalentPortofolioFragment


class TabPagerAdaptor(fragment: FragmentManager) : FragmentStatePagerAdapter(fragment, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {

    val fragment = arrayOf(TalentPortofolioFragment(),
        TalentExperienceFragment()
    )

    override fun getCount(): Int = fragment.size

    override fun getItem(position: Int): Fragment {
        return fragment[position]
    }

    override fun getPageTitle(position: Int): CharSequence? {
        return when (position) {
            0 -> "Portofolio"
            1 -> "Experience"
            else -> ""
        }
    }

    override fun saveState(): Parcelable? {
        return null
    }
}

