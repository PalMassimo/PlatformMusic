package it.units.musicplatform.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.auth.FirebaseAuth
import it.units.musicplatform.adapters.FollowersPostsAdapter
import it.units.musicplatform.databinding.FragmentHomeBinding
import it.units.musicplatform.entities.Post
import it.units.musicplatform.enumerations.Preference
import it.units.musicplatform.enumerations.PreferenceOperation
import it.units.musicplatform.utilities.PreferenceOperationParser
import it.units.musicplatform.viewmodels.FollowersPostsViewModel
import it.units.musicplatform.viewmodels.UserViewModel
import it.units.musicplatform.viewmodels.factories.FollowersPostsViewModelFactory
import it.units.musicplatform.viewmodels.factories.UserViewModelFactory


class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private val userId = FirebaseAuth.getInstance().currentUser!!.uid

    private lateinit var adapter: FollowersPostsAdapter
    lateinit var userViewModel: UserViewModel
    private lateinit var followersPostsViewModel: FollowersPostsViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        userViewModel = ViewModelProvider(requireActivity(), UserViewModelFactory(userId)).get(UserViewModel::class.java)
        followersPostsViewModel = ViewModelProviders.of(requireActivity(), FollowersPostsViewModelFactory(userId)).get(FollowersPostsViewModel::class.java)

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

        adapter = FollowersPostsAdapter(this, binding.followersPostsRecyclerView, followersPostsViewModel.followersPosts.value!!)
        binding.followersPostsRecyclerView.adapter = adapter
        binding.followersPostsRecyclerView.layoutManager = LinearLayoutManager(context)
        followersPostsViewModel.followersPosts.observe(viewLifecycleOwner, { adapter.setFollowersPosts(followersPostsViewModel.followersPosts.value!!) })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    fun changePreference(position: Int, preference: Preference) {
        val post = followersPostsViewModel.followersPosts.value!![position]
        when (PreferenceOperationParser.changePreference(preference, post.id, userViewModel.user.value!!.likes,userViewModel.user.value!!.dislikes)) {
            PreferenceOperation.ADD_LIKE -> addLike(post, position)
            PreferenceOperation.REMOVE_LIKE -> removeLike(post, position)
            PreferenceOperation.ADD_DISLIKE -> addDislike(post, position)
            PreferenceOperation.REMOVE_DISLIKE -> removeDislike(post, position)
            PreferenceOperation.FROM_LIKE_TO_DISLIKE -> fromLikeToDislike(post, position)
            PreferenceOperation.FROM_DISLIKE_TO_LIKE -> fromDislikeToLike(post, position)
        }
        adapter.notifyItemChanged(position)
    }

    fun updateNumberOfDownloads(position: Int) {
        followersPostsViewModel.incrementNumberOfDownloads(position)
        adapter.notifyItemChanged(position)
    }

    private fun addLike(post: Post, position: Int) {
        userViewModel.addLike(post.id)
        followersPostsViewModel.addLike(position)
        adapter.setLike(true, position)
    }

    private fun addDislike(post: Post, position: Int) {
        userViewModel.addDislike(post.id, post.numberOfDislikes + 1)
        followersPostsViewModel.addDislike(position)
        adapter.setDislike(true, position)
    }

    private fun removeLike(post: Post, position: Int) {
        userViewModel.removeLike(post.id, post.numberOfLikes - 1)
        followersPostsViewModel.removeLike(position)
        adapter.setLike(false, position)
    }

    private fun removeDislike(post: Post, position: Int) {
        userViewModel.removeDislike(post.id, post.numberOfDislikes - 1)
        followersPostsViewModel.removeDislike(position)
        adapter.setDislike(false, position)
    }

    private fun fromLikeToDislike(post: Post, position: Int) {
        removeLike(post, position)
        addDislike(post, position)
    }

    private fun fromDislikeToLike(post: Post, position: Int) {
        removeDislike(post, position)
        addLike(post, position)
    }

}