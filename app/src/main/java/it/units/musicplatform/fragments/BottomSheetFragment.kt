package it.units.musicplatform.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.setFragmentResult
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import it.units.musicplatform.databinding.FragmentBottomSheetBinding

private const val POST_OPERATION = "post_operation"
private const val POSITION = "position"
private const val OPERATION = "operation"

class BottomSheetFragment : BottomSheetDialogFragment() {

    companion object {
        @JvmStatic
        fun newInstance(position: Int) = BottomSheetFragment().apply {
            elementPosition = position
        }
    }

    private var _binding: FragmentBottomSheetBinding? = null
    private val binding get() = _binding!!
    private var elementPosition: Int? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentBottomSheetBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.editLayout.setOnClickListener { postOperationListener("edit", elementPosition!!) }
        binding.deleteLayout.setOnClickListener { postOperationListener("delete", elementPosition!!) }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    private fun postOperationListener(operation: String, elementPosition: Int){
        setFragmentResult(POST_OPERATION, bundleOf(OPERATION to operation, POSITION to elementPosition))
        dismiss()
    }


}