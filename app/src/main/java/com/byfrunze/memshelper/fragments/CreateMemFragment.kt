package com.byfrunze.memshelper.fragments

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Typeface
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.byfrunze.memshelper.R
import com.byfrunze.memshelper.adapters.FontAdapter
import com.byfrunze.memshelper.adapters.TextCreateAdapter
import com.flask.colorpicker.ColorPickerView
import com.flask.colorpicker.builder.ColorPickerDialogBuilder
import com.xw.repo.BubbleSeekBar
import kotlinx.android.synthetic.main.fragment_create_mem.*
import java.io.File
import java.io.FileOutputStream
import java.io.OutputStream
import java.util.*
import kotlin.collections.ArrayList


/**
 * A simple [Fragment] subclass.
 */
@Suppress("DEPRECATION")
class CreateMemFragment : Fragment() {

    private var checkSender = false
    private var URI_IMG: Uri? = null
    private var PICK_IMAGE = 100
    lateinit var mAdapter: FontAdapter
    lateinit var mAdapterTextCreate: TextCreateAdapter

    private val arrayTextView = ArrayList<TextView>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_create_mem, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        btn_change_img.setOnClickListener {
            val photoPiker = Intent(Intent.ACTION_PICK)
            photoPiker.type = "image/*"
            startActivityForResult(photoPiker, PICK_IMAGE)
            checkSender = false
            btn_send_create.isEnabled = checkSender
        }

        createRecycleViewCreate()
        createRecycleView()
        optionBottomMenu()



    }


    fun transitionTxt(txt: TextView) {
        var xDelta = 0
        var yDelta = 0

        txt.setOnTouchListener(View.OnTouchListener { view, event ->
            event?.let {

                val params =
                    view.layoutParams as FrameLayout.LayoutParams
                val x = event.rawX
                val y = event.rawY

                when (it.action) {
                    MotionEvent.ACTION_DOWN -> {
                        xDelta = (x - params.leftMargin).toInt()
                        yDelta = (y - params.topMargin).toInt()
                    }
                    MotionEvent.ACTION_MOVE -> {
                        params.topMargin = (y - yDelta).toInt()
                        params.leftMargin = (x - xDelta).toInt()
                        view.layoutParams = params
                    }
                    MotionEvent.ACTION_UP -> {

                    }
                    else -> Log.i("MOVE", "MOVE_ELSE")
                }
            }
            return@OnTouchListener true
        })
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        URI_IMG = data?.data
        img_create.setImageURI(URI_IMG)

    }

    private fun optionBottomMenu() {
        bottomBar.setOnTabSelectListener {
            when (it) {
                R.id.tab_text_color -> {
                    ll_color_create.visibility = View.VISIBLE
                    fl_text_size_create.visibility = View.GONE
                    fl_text_font_create.visibility = View.GONE
                    constr_layout_share.visibility = View.GONE
                }
                R.id.tab_text_font -> {
                    ll_color_create.visibility = View.GONE
                    fl_text_size_create.visibility = View.GONE
                    fl_text_font_create.visibility = View.VISIBLE
                    constr_layout_share.visibility = View.GONE
                }
                R.id.tab_text_size -> {
                    ll_color_create.visibility = View.GONE
                    fl_text_size_create.visibility = View.VISIBLE
                    fl_text_font_create.visibility = View.GONE
                    constr_layout_share.visibility = View.GONE
                }
                R.id.tab_share -> {
                    ll_color_create.visibility = View.GONE
                    fl_text_size_create.visibility = View.GONE
                    fl_text_font_create.visibility = View.GONE
                    constr_layout_share.visibility = View.VISIBLE

                    var filePath: File? = null
                    btn_download_create.setOnClickListener {
                        val bitmap = getScreenShot(coord_content)
                        val folderToSave = requireContext().externalMediaDirs[0]
                        filePath =
                            savePicture(bitmap = bitmap, folderToSave = folderToSave.toString())
                        checkSender = true
                        btn_send_create.isEnabled = checkSender
                    }
                    btn_send_create.setOnClickListener {
                        if (filePath != null) {
                            Log.i("FILE", filePath.toString())
                            val intent = Intent(Intent.ACTION_SEND)
                            intent.setDataAndType(Uri.fromFile(filePath), "image/*")
                            intent.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(filePath))
                            startActivity(Intent.createChooser(intent, "Share with"))
                        }
                    }

                }
            }

        }
    }

    private fun savePicture(bitmap: Bitmap, folderToSave: String): File? {

        val fOut: OutputStream
        val time = Date()

        try {
            val file = File(folderToSave, "$time.jpg")
            fOut = FileOutputStream(file)
            bitmap.compress(Bitmap.CompressFormat.JPEG, 85, fOut)
            fOut.flush()
            fOut.close()
            MediaStore.Images.Media.insertImage(
                activity?.contentResolver, file.absolutePath,
                file.name, file.name
            )
            Toast.makeText(requireContext(), "Мем сохранен", Toast.LENGTH_SHORT).show()
            return file
        } catch (e: Exception) {
            Toast.makeText(requireContext(), e.localizedMessage, Toast.LENGTH_SHORT).show()
        }
        return null
    }

    private fun getScreenShot(view: View): Bitmap {
        val returnedBitmap =
            Bitmap.createBitmap(view.width, view.height, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(returnedBitmap)
        val bgDrawable = view.background
        if (bgDrawable != null) bgDrawable.draw(canvas)
        else canvas.drawColor(Color.WHITE)
        view.draw(canvas)
        return returnedBitmap
    }

    private fun createRecycleView() {
        val listFont = ArrayList<String>()
        listFont.add("sans-serif")
        listFont.add("sans-serif-light")
        listFont.add("sans-serif-condensed")
        listFont.add("sans-serif-black")
        listFont.add("sans-serif-thin")
        listFont.add("sans-serif-medium")
        mAdapter = FontAdapter()
        mAdapter.setupFonts(listFont)
        recycler_view_font_create.adapter = mAdapter
        recycler_view_font_create.layoutManager =
            LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        recycler_view_font_create.setHasFixedSize(true)


    }

    private fun createRecycleViewCreate() {
        mAdapterTextCreate = TextCreateAdapter()


        btn_new_text_create.setOnClickListener {
            mAdapterTextCreate.setupText("Example")
            val newTextView = TextView(requireContext())
            newTextView.text = "Example"
            coord_content.addView(newTextView)
            arrayTextView.add(newTextView)
        }
        mAdapterTextCreate.setOnCloseListener(object : TextCreateAdapter.OnCloseClickListener {
            override fun onClose(position: Int) {
                coord_content.removeView(arrayTextView[position])
                arrayTextView.removeAt(position)
            }
        })

        mAdapterTextCreate.setOnChooseItemListener1(object :
            TextCreateAdapter.OnChooseItemListener {
            override fun onChooseItem(position: Int) {
                changeSizeText(arrayTextView[position])
                transitionTxt(arrayTextView[position])
                mAdapter.mySetOnChangeTextFontListener(object :
                    FontAdapter.OnChangeTextFontListener {
                    override fun onChangeFontText(create: Typeface) {
                        if (position < arrayTextView.count())

                            arrayTextView[position].typeface = create
                    }
                })
                chooseColorText(arrayTextView[position])

            }
        })
        mAdapterTextCreate.mySetOnChangeTextListener(object :
            TextCreateAdapter.OnChangeTextListener {
            override fun onChangeText(text: String, position: Int) {
                if (position < arrayTextView.count())
                    arrayTextView[position].text = text
            }
        })

        recycler_view_texts_create.adapter = mAdapterTextCreate
        recycler_view_texts_create.layoutManager =
            LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        recycler_view_texts_create.setHasFixedSize(true)
    }

    private fun changeSizeText(txt: TextView) {
        seek_bar_size_create.onProgressChangedListener =
            object : BubbleSeekBar.OnProgressChangedListener {
                override fun onProgressChanged(
                    bubbleSeekBar: BubbleSeekBar?,
                    progress: Int,
                    progressFloat: Float,
                    fromUser: Boolean
                ) {
                    txt.textSize = progressFloat
                }

                override fun getProgressOnActionUp(
                    bubbleSeekBar: BubbleSeekBar?,
                    progress: Int,
                    progressFloat: Float
                ) {
                    txt.textSize = progressFloat
                }

                override fun getProgressOnFinally(
                    bubbleSeekBar: BubbleSeekBar?,
                    progress: Int,
                    progressFloat: Float,
                    fromUser: Boolean
                ) {
                    txt.textSize = progressFloat
                }

            }

    }

    private fun chooseColorText(txt: TextView?) {
        btn_choose_color_create.setOnClickListener {
            ColorPickerDialogBuilder
                .with(context)
                .setTitle("Выберите цвет")
                .initialColor(Color.RED)
                .wheelType(ColorPickerView.WHEEL_TYPE.FLOWER)
                .density(12)
                .setOnColorSelectedListener {
                    txt?.setTextColor(it)
                }
                .setPositiveButton(
                    "Выбрать"
                ) { d, lastSelectedColor, _ ->
                    txt?.setTextColor(lastSelectedColor)
                }
                .setNegativeButton(
                    "Отмена"
                ) { dialog, _ ->
                    dialog.dismiss()
                }
                .build()
                .show()
        }

    }

}

