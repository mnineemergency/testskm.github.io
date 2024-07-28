package it.saimao.shantranslit.fragments

import android.content.ClipData
import android.content.ClipboardManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CompoundButton
import android.widget.EditText
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import it.saimao.shantranslit.R
import it.saimao.shantranslit.databinding.FragmentHomeBinding
import it.saimao.shantranslit.utils.ShanTranslit
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

class Home : Fragment(), CompoundButton.OnCheckedChangeListener {

    private lateinit var binding: FragmentHomeBinding


    private lateinit var input: EditText
    private lateinit var output: EditText


    private lateinit var rg1: RadioGroup
    private lateinit var rg2: RadioGroup

    private lateinit var rb1: RadioButton
    private lateinit var rb2: RadioButton
    private lateinit var rb3: RadioButton
    private lateinit var rb4: RadioButton
    private lateinit var rb5: RadioButton
    private lateinit var rb6: RadioButton
    private lateinit var executorService: ExecutorService
    private lateinit var rg1Listener: (RadioGroup, Int) -> Unit
    private lateinit var rg2Listener: (RadioGroup, Int) -> Unit
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        input = binding.layoutMain.edInput
        output = binding.layoutMain.edOutput
        val btConvert = binding.layoutMain.btConvert
        val btClear = binding.layoutMain.btClear
        val btCopy = binding.layoutMain.btCopy
        btConvert.setOnClickListener(this::convert)
        btClear.setOnClickListener(this::clear)
        btCopy.setOnClickListener(this::copy)

        binding.aboutUs.setOnClickListener(this::goToAboutUs)

        binding.layoutMain.rbShnToEng.setOnCheckedChangeListener(this)
        binding.layoutMain.rbEngToShn.setOnCheckedChangeListener(this)

        rg1 = binding.layoutMain.rg1
        rg2 = binding.layoutMain.rg2

        rg1Listener = { _, id ->
            if (id != -1 && rg2.checkedRadioButtonId != -1) {
                rg2.setOnCheckedChangeListener(null)
                rg2.clearCheck()
                rg2.setOnCheckedChangeListener(rg2Listener)
            }
        }

        rg2Listener = { _, id ->
            if (id != -1 && rg1.checkedRadioButtonId != -1) {
                rg1.setOnCheckedChangeListener(null)
                rg1.clearCheck()
                rg1.setOnCheckedChangeListener(rg1Listener)
            }
        }

        rg1.setOnCheckedChangeListener(rg1Listener)

        rg2.setOnCheckedChangeListener(rg2Listener)

        rb1 = binding.layoutMain.rb1
        rb2 = binding.layoutMain.rb2
        rb3 = binding.layoutMain.rb3
        rb4 = binding.layoutMain.rb4
        rb5 = binding.layoutMain.rb5
        rb6 = binding.layoutMain.rb6
        rb1.isChecked = true
        enableRadioButtons()
        enableService()
    }


    private fun goToAboutUs(view: View) {
//        startActivity(Intent(this, AboutUsActivity::class.java))
        view.findNavController().navigate(R.id.action_homeFragment_to_aboutUsFragment)
    }

    private fun enableService() {
        executorService = Executors.newSingleThreadExecutor()
    }

    private fun enableRadioButtons() {
        if (binding.layoutMain.rbShnToEng.isChecked) {
            rb1.text = "tai"
            rb2.text = "tái"
            rb3.text = "တႆး(tai)"
            rb4.text = "တႆး(tái)"
            rb5.text = "တႆး\ntai"
            rb6.text = "တႆး\ntái"
        }

    }

    private fun copy(view: View?) {
        val text: String = output.text.toString()
        if (text.isEmpty()) {
            Toast.makeText(requireContext(), "No Output Text to be Copied!!", Toast.LENGTH_SHORT)
                .show()
            return
        }
        val clipboard =
            requireActivity().getSystemService(AppCompatActivity.CLIPBOARD_SERVICE) as ClipboardManager
        val clip = ClipData.newPlainText("Copied Text", text)
        clipboard.setPrimaryClip(clip)
        Toast.makeText(requireContext(), "Output Text Copied Successfully!", Toast.LENGTH_SHORT)
            .show()

    }

    private fun setShnToEngTextAsync(rbSelected: Int, inputText: String) {

        binding.layoutMain.loading.visibility = View.VISIBLE
        binding.layoutMain.edOutput.text?.clear()
        executorService.execute {
            val result: String = when (rbSelected) {
                1 ->
                    ShanTranslit.taiToEngWithoutTone(inputText)

                2 ->
                    ShanTranslit.taiToEngWithTone(inputText)

                3 ->
                    ShanTranslit.taiToEngWithoutToneSideBySide(inputText)

                4 ->
                    ShanTranslit.taiToEngWithToneSideBySide(inputText)

                5 -> {
                    val s = ShanTranslit.taiToEngWithoutToneUpsideDown(inputText)
                    "${s.shan}\n${s.eng}"
                }

                6 -> {
                    val s = ShanTranslit.taiToEngWithToneUpsideDown(inputText)
                    "${s.shan}\n${s.eng}"
                }

                else -> ShanTranslit.engToShn(input.text.toString())
            }
            requireActivity().runOnUiThread {
                output.setText(result)
                binding.layoutMain.loading.visibility = View.GONE
            }
        }
    }

    private fun convert(view: View?) {

        val inputText = input.text.toString()

        if (inputText.isNotEmpty()) {


            if (binding.layoutMain.rbShnToEng.isChecked) {
                when {
                    rb1.isChecked -> setShnToEngTextAsync(1, inputText)

                    rb2.isChecked -> setShnToEngTextAsync(2, inputText)

                    rb3.isChecked -> setShnToEngTextAsync(3, inputText)

                    rb4.isChecked -> setShnToEngTextAsync(4, inputText)

                    rb5.isChecked -> setShnToEngTextAsync(5, inputText)

                    rb6.isChecked -> setShnToEngTextAsync(6, inputText)
                }
            } else {
                setShnToEngTextAsync(7, inputText)
            }

        }

    }

    private fun clear(view: View?) {
        if (input.text.isNotEmpty())
            input.text.clear()
        if (output.text.isNotEmpty()) output.text.clear()
    }

    override fun onCheckedChanged(buttonView: CompoundButton?, isChecked: Boolean) {
        binding.layoutMain.rg.visibility =
            if (binding.layoutMain.rbShnToEng.isChecked) View.VISIBLE else View.GONE
    }

}