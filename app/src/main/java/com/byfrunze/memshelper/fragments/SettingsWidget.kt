package com.byfrunze.memshelper.fragments

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CompoundButton
import android.widget.RadioGroup
import android.widget.Toast
import com.byfrunze.memshelper.R
import com.byfrunze.memshelper.data.ModelSaveQuotes
import com.byfrunze.memshelper.data.RealmDB
import io.realm.Realm
import io.realm.kotlin.createObject
import io.realm.kotlin.where
import kotlinx.android.synthetic.main.fragment_settings_widget.*

/**
 * A simple [Fragment] subclass.
 */
class SettingsWidget : Fragment() {

    val APP_PREFERENCES = "mysettings"
    val APP_PREFERENCES_QUOTE = "quote"
    lateinit var pref: SharedPreferences

    val RB_ADVICE = "rb_advice"
    val RB_PROG = "rb_prog"
    val RB_CHACK = "rb_chack"
    val RB_MOT = "rb_motivation"

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_settings_widget, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        var getPref = requireContext().getSharedPreferences(APP_PREFERENCES, Context.MODE_PRIVATE)
            .getString(APP_PREFERENCES_QUOTE, "rb_adivce")
        when (getPref) {
            RB_ADVICE -> rb_advice_settings.isChecked = true
            RB_CHACK -> rb_chack_settings.isChecked = true
            RB_MOT -> rb_motivation_settings.isChecked = true
            RB_PROG -> rb_prog_settings.isChecked = true
        }
        activity?.let {
            pref = it.getSharedPreferences(APP_PREFERENCES, Context.MODE_PRIVATE)

            rg_settings.setOnCheckedChangeListener { _, checkedId ->
                when (checkedId) {
                    R.id.rb_motivation_settings -> getPref = RB_MOT
                    R.id.rb_advice_settings -> getPref = RB_ADVICE
                    R.id.rb_chack_settings -> getPref = RB_CHACK
                    R.id.rb_prog_settings -> getPref = RB_PROG

                }
            }

            btn_save_widget_settings.setOnClickListener {
                try {
                    val editor = pref.edit()
                    editor.putString(APP_PREFERENCES_QUOTE, getPref)
                    editor.apply()
                    Toast.makeText(requireContext(), "Сохранено", Toast.LENGTH_SHORT).show()
                } catch (e: Exception) {
                    Toast.makeText(requireContext(), "Ошибка", Toast.LENGTH_SHORT).show()
                }
            }

        }
    }


}
