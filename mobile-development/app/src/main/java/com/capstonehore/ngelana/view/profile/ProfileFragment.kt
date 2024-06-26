package com.capstonehore.ngelana.view.profile

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.capstonehore.ngelana.R
import com.capstonehore.ngelana.adapter.ProfileAdapter
import com.capstonehore.ngelana.data.Profile
import com.capstonehore.ngelana.databinding.FragmentProfileBinding
import com.capstonehore.ngelana.view.home.HomeViewModel
import com.capstonehore.ngelana.view.login.LoginActivity
import com.capstonehore.ngelana.view.profile.aboutus.AboutUsActivity
import com.capstonehore.ngelana.view.profile.favorite.MyFavoriteActivity
import com.capstonehore.ngelana.view.profile.helpcenter.HelpCenterActivity
import com.capstonehore.ngelana.view.profile.language.LanguageActivity
import com.capstonehore.ngelana.view.profile.personalinformation.PersonalInformationActivity
import com.capstonehore.ngelana.view.profile.review.MyReviewActivity

class ProfileFragment : Fragment() {

    private var _binding: FragmentProfileBinding? = null

    private val binding get() = _binding!!
    private val viewModel: HomeViewModel by viewModels()

    private val accountList = ArrayList<Profile>()
    private val settingsList = ArrayList<Profile>()
    private val informationList = ArrayList<Profile>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentProfileBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupRecyclerView(binding.rvAccount)
        setupRecyclerView(binding.rvSettings)
        setupRecyclerView(binding.rvInformation)

        accountList.addAll(
            getListFromResources(
                R.array.data_account_name,
                R.array.data_account_icon
            )
        )
        settingsList.addAll(
            getListFromResources(
                R.array.data_settings_name,
                R.array.data_settings_icon
            )
        )
        informationList.addAll(
            getListFromResources(
                R.array.data_information_name,
                R.array.data_information_icon
            )
        )

        showList(binding.rvAccount, accountList) { item ->
            val intent = when (item.name) {
                "Personal information" -> Intent(requireContext(), PersonalInformationActivity::class.java)
                "My favorite" -> Intent(requireContext(), MyFavoriteActivity::class.java)
                "My review" -> Intent(requireContext(), MyReviewActivity::class.java)
                else -> null
            }
            intent?.let { startActivity(it) }
        }

        showList(binding.rvSettings, settingsList) { item ->
            val intent = when (item.name) {
                "Language" -> Intent(requireContext(), LanguageActivity::class.java)
                else -> null
            }
            intent?.let { startActivity(it) }
        }

        showList(binding.rvInformation, informationList) { item ->
            val intent = when (item.name) {
                "Help center" -> Intent(requireContext(), HelpCenterActivity::class.java)
                "About us" -> Intent(requireContext(), AboutUsActivity::class.java)
                else -> null
            }
            intent?.let { startActivity(it) }
        }

        binding.signOutButton.setOnClickListener {
            val intent = Intent(requireContext(), LoginActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(intent)
            requireActivity().finish()
        }
    }

    private fun setupRecyclerView(recyclerView: RecyclerView) {
        recyclerView.setHasFixedSize(true)
        recyclerView.layoutManager = LinearLayoutManager(requireActivity())
    }

    private fun getListFromResources(namesResId: Int, iconsResId: Int): ArrayList<Profile> {
        val dataName = resources.getStringArray(namesResId)
        val dataIcon = resources.obtainTypedArray(iconsResId)
        val list = ArrayList<Profile>()
        for (i in dataName.indices) {
            val profile = Profile(dataName[i], dataIcon.getResourceId(i, -1))
            list.add(profile)
        }
        dataIcon.recycle()
        return list
    }

    private fun showList(
        recyclerView: RecyclerView,
        list: ArrayList<Profile>,
        onItemClick: (Profile) -> Unit
    ) {
        val profileAdapter = ProfileAdapter(list)
        recyclerView.adapter = profileAdapter

        profileAdapter.setOnItemClickCallback(object : ProfileAdapter.OnItemClickCallback {
            override fun onItemClicked(items: Profile) {
                onItemClick(items)
            }
        })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}