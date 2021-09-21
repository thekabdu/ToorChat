package kz.diaspora.app.ui.edit_profile.languages

import android.content.Context
import android.graphics.Typeface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import kotlinx.android.synthetic.main.view_lang_bottom_sheet.*
import kz.diaspora.app.R
import kz.diaspora.app.data.db.PrefsImpl
import kz.diaspora.app.utils.updateResources


class LanguagesBottomSheetDialogFragment constructor(
    private val prefsImpl: PrefsImpl,
    private val mContext: Context?,
    private val callback: LanguagesBottomSheetCallback
) : BottomSheetDialogFragment(), View.OnClickListener {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.view_lang_bottom_sheet, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        ll_russian.setOnClickListener(this)
        ll_kazakh.setOnClickListener(this)
        ll_english.setOnClickListener(this)
        setLang(prefsImpl.getLanguage())
        super.onViewCreated(view, savedInstanceState)
    }

    override fun onClick(v: View?) {
        deselectAll()
        when (v?.id) {
            R.id.ll_russian -> {
                russianSelected()
            }
            R.id.ll_kazakh -> {
                kazakhSelected()
            }
            R.id.ll_english -> {
                englishSelected()
            }
        }
        callback.languageClicked()
        dismiss()
    }

    private fun setLang(lang: String?) {
        deselectAll()

        when (lang) {
            "ru" -> {
                russianSelected()
            }
            "kk" -> {
                kazakhSelected()
            }
            "en" -> {
                englishSelected()
            }
        }
    }

    private fun deselectAll() {
        ll_russian.background = null
        ll_kazakh.background = null
        ll_english.background = null

        tv_russian.setTextColor(resources.getColor(R.color.color_363636))
        tv_kazakh.setTextColor(resources.getColor(R.color.color_363636))
        tv_english.setTextColor(resources.getColor(R.color.color_363636))

        tv_russian.setTypeface(tv_russian.typeface, Typeface.NORMAL)
        tv_kazakh.setTypeface(tv_kazakh.typeface, Typeface.NORMAL)
        tv_english.setTypeface(tv_english.typeface, Typeface.NORMAL)

        iv_russian.visibility = View.GONE
        iv_kazakh.visibility = View.GONE
        iv_english.visibility = View.GONE
    }

    private fun russianSelected() {
        ll_russian.background = resources.getDrawable(R.drawable.bottom_sheet_rounded_item)
        tv_russian.setTextColor(resources.getColor(R.color.colorAccent))
        tv_russian.setTypeface(tv_russian.typeface, Typeface.BOLD)
        iv_russian.visibility = View.VISIBLE

        changeLang("ru")
    }

    private fun kazakhSelected() {
        ll_kazakh.background = resources.getDrawable(R.drawable.bottom_sheet_rounded_item)
        tv_kazakh.setTextColor(resources.getColor(R.color.colorAccent))
        tv_kazakh.setTypeface(tv_russian.typeface, Typeface.BOLD)
        iv_kazakh.visibility = View.VISIBLE

        changeLang("kk")
    }

    private fun englishSelected() {
        ll_english.background = resources.getDrawable(R.drawable.bottom_sheet_rounded_item)
        tv_english.setTextColor(resources.getColor(R.color.colorAccent))
        tv_english.setTypeface(tv_russian.typeface, Typeface.BOLD)
        iv_english.visibility = View.VISIBLE

        changeLang("en")
    }

    private fun changeLang(lang: String) {
        prefsImpl.setLanguage(lang)
        updateResources(mContext, lang)
//        updateResources(mContext?.applicationContext, lang)
    }
}
