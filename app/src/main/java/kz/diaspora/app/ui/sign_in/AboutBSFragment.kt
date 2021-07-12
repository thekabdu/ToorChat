package kz.diaspora.app.ui.sign_in

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import kz.diaspora.app.R
import kz.diaspora.app.databinding.WidgetBsAboutBinding

class AboutBSFragment : BottomSheetDialogFragment() {

    private var _binding: WidgetBsAboutBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = DataBindingUtil.inflate(
            inflater, R.layout.widget_bs_about, container, false
        )
        binding.lifecycleOwner = this
        return binding.root
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}
