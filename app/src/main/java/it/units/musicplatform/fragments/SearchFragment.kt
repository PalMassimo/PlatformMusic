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


class SearchFragment : Fragment() {

    private var _binding: FragmentSearchBinding? = null
    private val binding get() = _binding!!

    private lateinit var adapter: UsersAdapter
    private lateinit var usersSearchedViewModel: UsersSearchedViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        usersSearchedViewModel = ViewModelProviders.of(this).get(UsersSearchedViewModel::class.java)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentSearchBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setUpRecyclerView()

    }

    private fun isSearchRequired(): Boolean {
        return arguments?.get("query") != null
    }

    private fun setUpRecyclerView() {

        if (isSearchRequired()) performSearch() else showMostPopular()

        binding.usersRecyclerView.layoutManager = LinearLayoutManager(context)
        binding.usersRecyclerView.adapter = adapter

    }

    private fun performSearch() {
        adapter = UsersAdapter(ArrayList(), HashSet())
        GlobalScope.launch(Dispatchers.Main) {
            val resultUsers = usersSearchedViewModel.searchUsers(requireArguments().get("query") as String)
            adapter.users = resultUsers
            adapter.notifyDataSetChanged()
        }
    }

    private fun showMostPopular() {
        adapter = UsersAdapter(if (usersSearchedViewModel.popularUsers.value == null) ArrayList() else usersSearchedViewModel.popularUsers.value!!, HashSet())

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