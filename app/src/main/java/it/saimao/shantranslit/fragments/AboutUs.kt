package it.saimao.shantranslit.fragments

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import it.saimao.shantranslit.R
import it.saimao.shantranslit.adapters.MaoAdapter
import it.saimao.shantranslit.databinding.FragmentAboutUsBinding

class AboutUs : Fragment(), AdapterView.OnItemClickListener {


    private lateinit var binding: FragmentAboutUsBinding
    private val stringsAsk =
        arrayOf("E-mail:", "Facebook:", "Github:", "Rate this app on Play Store")
    private val stringsValue =
        arrayOf("tmk.muse@gmail.com", "ထုင့်မၢဝ်းၶမ်း", "Get source code", "")
    private val icons = intArrayOf(
        R.drawable.ic_gmail,
        R.drawable.ic_facebook,
        R.drawable.ic_github,
        R.drawable.ic_playstore
    )


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentAboutUsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val simpleListView = binding.layoutMain.simpleListView
        binding.aboutUs.setOnClickListener { it.findNavController().popBackStack() }
        val adapter = MaoAdapter(requireContext(), stringsAsk, stringsValue, icons)
        simpleListView.onItemClickListener = this
        simpleListView.adapter = adapter
    }


    override fun onItemClick(adapterView: AdapterView<*>?, view: View?, i: Int, l: Long) {
        val intent: Intent
        if (i == 1) {
            intent = try {
                Intent(Intent.ACTION_VIEW, Uri.parse("fb://page/100377671433172"))
            } catch (e: Exception) {
                Intent(Intent.ACTION_VIEW, Uri.parse("https://www.facebook.com/100377671433172"))
            }
            startActivity(intent)
        } else if (i == 0) {
            val to = "tmk.muse@gmail.com"
            intent = Intent(Intent.ACTION_SEND)
            intent.putExtra(Intent.EXTRA_EMAIL, arrayOf(to))
            intent.setType("message/rfc822")
            startActivity(Intent.createChooser(intent, "Choose an Email client :"))
        } else if (i == 2) {
            startActivity(
                Intent(
                    Intent.ACTION_VIEW,
                    Uri.parse("https://github.com/SaingHmineTun/ShanTranslitAndroid")
                )
            )
        } else if (i == 3) {
            startActivity(
                Intent(
                    Intent.ACTION_VIEW,
                    Uri.parse("market://details?id=it.saimao.shantranslit")
                )
            )
        }
    }

}