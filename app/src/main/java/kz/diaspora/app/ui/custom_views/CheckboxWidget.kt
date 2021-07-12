package kz.diaspora.app.ui.custom_views

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.LinearLayout
import kz.diaspora.app.R
import kz.diaspora.app.databinding.WidgetCheckboxBinding

class CheckboxWidget @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : LinearLayout(context, attrs, defStyleAttr) {

    private var _binding: WidgetCheckboxBinding? = null
    private val binding get() = _binding!!

    private var checked: Boolean = false
        set(value) {
            field = value
            if (value) {
                binding.ivIcon.setImageDrawable(context.resources.getDrawable(R.drawable.checkbox_checked))
                binding.tvTitle.text = context.getString(R.string.on)
            } else {
                binding.ivIcon.setImageDrawable(context.resources.getDrawable(R.drawable.checkbox_unchecked))
                binding.tvTitle.text = context.getString(R.string.off)
            }
        }

    init {
        _binding = WidgetCheckboxBinding.inflate(LayoutInflater.from(context), this, true)

        val a = context.obtainStyledAttributes(attrs, R.styleable.CheckboxWidget)

        try {
            checked = a.getBoolean(R.styleable.CheckboxWidget_checked, false)
        } finally {
            a.recycle()
        }

        binding.llRoot.setOnClickListener {
            checked = !checked
        }
    }
}