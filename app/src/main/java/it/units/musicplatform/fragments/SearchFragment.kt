package it.units.musicplatform.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import androidx.lifecycle.viewModelScope
import androidx.recyclerview.widget.LinearLayoutManager
import it.units.musicplatform.adapters.UsersAdapter
import it.units.musicplatform.databinding.FragmentSearchBinding
import it.units.musicplatform.viewmodels.FollowersPostsViewModel
import it.units.musicplatform.viewmodels.UserViewModel
import it.units.musicplatform.viewmodels.UsersSearchedViewModel
import it.units.musicplatform.viewmodels.factories.UsersSearchedViewModelFactory
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


class SearchFragment : Fragment() {

    private var _binding: FragmentSearchBinding? = null
    private val binding get() = _binding!!

    private lateinit var adapter: UsersAdapter
    private lateinit var userViewModel: UserViewModel
    private lateinit var usersSearchedViewModel: UsersSearchedViewModel
    private lateinit var followersPostsViewModel: FollowersPostsViewModel

    private lateinit var userId: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        userViewModel = ViewModelProviders.of(requireActivity()).get(UserViewModel::class.java)
        userId = userViewModel.userId

        followersPostsViewModel = ViewModelProviders.of(requireActivity()).get(FollowersPostsViewModel::class.java)
        usersSearchedViewModel = ViewModelProviders.of(requireActivity(), UsersSearchedViewModelFactory(userId)).get(UsersSearchedViewModel::class.java)

    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentSearchBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setUpRecyclerView()

    }

    private fun isSearchRequired() = arguments?.get("query") != null


    private fun setUpRecyclerView() {

        if (isSearchRequired()) performSearch() else showMostPopular()

        binding.usersRecyclerView.layoutManager = LinearLayoutManager(context)
        binding.usersRecyclerView.adapter = adapter

        userViewModel.user.observe(viewLifecycleOwner, { adapter.following = userViewModel.user.value!!.following.keys })
    }

    private fun performSearch() {
        adapter = UsersAdapter(ArrayList(), userViewModel.user.value!!.following.keys, this)
        usersSearchedViewModel.viewModelScope.launch(Dispatchers.Main) {
            adapter.users = usersSearchedViewModel.searchUsers(requireArguments().get("query") as String)
            adapter.notifyDataSetChanged()
        }
    }

    private fun showMostPopular() {
        adapter = UsersAdapter(usersSearchedViewModel.popularUsers.value!!, userViewModel.user.value!!.following.keys, this)
        usersSearchedViewModel.popularUsers.observe(viewLifecycleOwner, {
            adapter.users = usersSearchedViewModel.popularUsers.value!!
            adapter.notifyDataSetChanged()
        })
    }

    fun addFollowing(followingId: String) {
        userViewModel.addFollowing(followingId)
        followersPostsViewModel.addFollowing(followingId)
    }

    fun removeFollowing(followingId: String) {
        userViewModel.removeFollowing(followingId)
        followersPostsViewModel.removeFollowing(followingId)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}