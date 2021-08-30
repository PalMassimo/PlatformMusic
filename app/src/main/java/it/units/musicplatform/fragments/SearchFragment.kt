package it.units.musicplatform.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import it.units.musicplatform.adapters.UsersAdapter
import it.units.musicplatform.databinding.FragmentSearchBinding
import it.units.musicplatform.entities.User
import it.units.musicplatform.viewmodels.UsersSearchedViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

//private const val ARG_USER_ID = "user_id"

class SearchFragment : Fragment() {

    private var _binding: FragmentSearchBinding? = null
    private val binding get() = _binding!!

    //    private var _userId: String? = null
//    private val userId get() = _userId!!
    private lateinit var adapter: UsersAdapter
    private lateinit var usersSearchedViewModel: UsersSearchedViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        arguments?.let { _userId = it.getString(ARG_USER_ID) }

        usersSearchedViewModel = ViewModelProviders.of(this).get(UsersSearchedViewModel::class.java)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentSearchBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (isSearchRequired()) {
            adapter = UsersAdapter(ArrayList(), HashSet())
            binding.usersRecyclerView.adapter = adapter
            binding.usersRecyclerView.layoutManager = LinearLayoutManager(context)
            GlobalScope.launch (Dispatchers.Main){
                val resultUsers = usersSearchedViewModel.searchUsers(requireArguments().get("query") as String)

                adapter.users = resultUsers
                adapter.notifyDataSetChanged()
//            setUpRecyclerView2()
            }
        } else {
            setUpRecyclerView()
        }


    }

    fun isSearchRequired(): Boolean {
        val bundle = arguments
        return bundle != null && bundle.get("query") != null
    }

//    private fun setUpRecyclerView2(users: ArrayList<User>) {
//
//        adapter = UsersAdapter(users, HashSet())
//
//        usersSearchedViewModel.popularUsers.observe(viewLifecycleOwner, {
//            adapter.users = usersSearchedViewModel.popularUsers.value!!
//            adapter.notifyDataSetChanged()
//        })

//    }

    private fun setUpRecyclerView() {

        adapter = UsersAdapter(if (usersSearchedViewModel.popularUsers.value == null) ArrayList() else usersSearchedViewModel.popularUsers.value!!, HashSet())
        binding.usersRecyclerView.layoutManager = LinearLayoutManager(context)
        binding.usersRecyclerView.adapter = adapter

        usersSearchedViewModel.popularUsers.observe(viewLifecycleOwner, {
            adapter.users = usersSearchedViewModel.popularUsers.value!!
            adapter.notifyDataSetChanged()
        })

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}