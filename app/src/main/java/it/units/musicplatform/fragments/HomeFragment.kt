package it.units.musicplatform.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import it.units.musicplatform.adapters.FollowersPostsAdapter
import it.units.musicplatform.databinding.FragmentHomeBinding
import it.units.musicplatform.viewmodels.FollowersPostsViewModel
import it.units.musicplatform.viewmodels.UserViewModel


class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
//    private var userId = FirebaseAuth.getInstance().currentUser!!.uid

    private lateinit var adapter: FollowersPostsAdapter
    lateinit var userViewModel: UserViewModel
    private lateinit var followersPostsViewModel: FollowersPostsViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        userViewModel = ViewModelProviders.of(requireActivity()).get(UserViewModel::class.java)
        followersPostsViewModel = ViewModelProviders.of(this).get(FollowersPostsViewModel::class.java)

    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setUpRecyclerView()

    }

    private fun setUpRecyclerView() {

        adapter = FollowersPostsAdapter(this, binding.followersPostsRecyclerView, if (followersPostsViewModel.followersPosts.value == null) ArrayList() else followersPostsViewModel.followersPosts.value!!)
        adapter.notifyDataSetChanged()
        binding.followersPostsRecyclerView.adapter = adapter
        binding.followersPostsRecyclerView.layoutManager = LinearLayoutManager(context)
        followersPostsViewModel.followersPosts.observe(viewLifecycleOwner, { adapter.setFollowersPosts(followersPostsViewModel.followersPosts.value!!) })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}