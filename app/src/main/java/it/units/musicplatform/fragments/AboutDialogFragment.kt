package it.units.musicplatform.fragments

import android.app.AlertDialog
import android.app.Dialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import androidx.fragment.app.DialogFragment
import it.units.musicplatform.R
import it.units.musicplatform.databinding.FragmentAboutDialogBinding

class AboutDialogFragment : DialogFragment() {

    private var _binding: FragmentAboutDialogBinding? = null
    private val binding get() = _binding!!

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {

        _binding = FragmentAboutDialogBinding.inflate(LayoutInflater.from(context))

        val builder = AlertDialog.Builder(requireContext()).apply {
            setView(binding.root)
            setTitle("About Window")
            setIcon(R.drawable.ic_github)
            setMessage("The application is completely developed and maintained by Massimo Palmisano. You can find the open source code at github")
            setNeutralButton("Back") { _, _ -> }
            setPositiveButton("Go on github") { _, _ ->
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse("https://github.com/PalMassimo/PlatformMusic"))
                startActivity(intent)
            }

        }

        return builder.create()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


}