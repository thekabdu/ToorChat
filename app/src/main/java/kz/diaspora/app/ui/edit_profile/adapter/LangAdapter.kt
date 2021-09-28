package kz.diaspora.app.ui.edit_profile.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.content.ContextWrapper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.TextView
import kz.diaspora.app.R
import kz.diaspora.app.domain.model.Language


import org.jetbrains.anko.layoutInflater


class LangAdapter(var context: Context, private var languageList: ArrayList<Language>) : BaseAdapter() {

    @SuppressLint("ViewHolder")
    override fun getView(position: Int, view: View?, viewGroup: ViewGroup?): View {
        val language = languageList.get(position)
        val row = (context as ContextWrapper).layoutInflater.inflate(R.layout.lang_list_item, viewGroup, false)
        val tvName = row.findViewById(R.id.txt_lang_name) as TextView
        val iconImg = row.findViewById(R.id.ic_lang) as ImageView

        tvName.text = language.name
       /* iconImg.baseline = language.icon!!*/
        return row
    }

    override fun getItem(i: Int): Any {
        return languageList[i]
    }

    override fun getItemId(p0: Int): Long {
        return 0
    }

    override fun getCount(): Int {
        return languageList.size
    }
}