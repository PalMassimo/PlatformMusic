package it.units.musicplatform.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.setFragmentResult
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import it.units.musicplatform.databinding.FragmentBottomSheetBinding
import it.units.musicplatform.enumerations.PostOperation

private const val POST_OPERATION = "post_operation"
private const val POSITION = "position"
private const val OPERATION = "operation"

class BottomSheetFragment : BottomSheetDialogFragment() {

    private var _binding: FragmentBottomSheetBinding? = null
    private val binding get() = _binding!!

    private var _elementPosition: Int? = null
    private val elementPosition get() = _elementPosition!!

    companion object {
        @JvmStatic
        fun newInstance(position: Int) = BottomSheetFragment().apply {
            _elementPosition = position
        }
    }


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentBottomSheetBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.editLayout.setOnClickListener { postOperationListener(PostOperation.EDIT.name, elementPosition) }
        binding.deleteLayout.setOnClickListener { postOperationListener(PostOperation.DELETE.name, elementPosition) }
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