package com.example.irene.khramovahomework8

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import android.util.Log
import android.view.View
import android.widget.EditText
import android.widget.LinearLayout
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers


class EditActivity : AppCompatActivity() {
    private lateinit var toolbar: Toolbar
    private lateinit var editTextTitle: EditText
    private lateinit var editTextContent: EditText
    private lateinit var linearLayout: LinearLayout
    private lateinit var note: Note
    private var appDatabase: AppDatabase? = null

    companion object {
        private const val EXTRA_NOTE = "Note"

        fun createStartIntent(context: Context, note: Note?): Intent {
            val intent = Intent(context, EditActivity::class.java)
            intent.putExtra(EXTRA_NOTE, note)
            return intent
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit)

        appDatabase = AppDatabase.getInstance(this)
        note = intent.extras.getParcelable(EXTRA_NOTE)

        linearLayout = findViewById(R.id.linearLayout)
        linearLayout.setBackgroundColor(ContextCompat.getColor(this, note.color))

        toolbar = findViewById(R.id.toolbarEdit)
        toolbar.navigationIcon = ContextCompat.getDrawable(this, R.drawable.ic_arrow)
        toolbar.setNavigationOnClickListener {
            onBackPressed()
        }

        editTextTitle = findViewById(R.id.editTextTitle)
        editTextContent = findViewById(R.id.editTextContent)

        editTextTitle.setText(note.title)
        editTextContent.setText(note.content)

        toolbar.inflateMenu(R.menu.menu_edit)
        toolbar.setOnMenuItemClickListener { item ->
            when (item?.itemId) {
                R.id.changeColor -> {
                    val dialogView = layoutInflater.inflate(R.layout.dialog_color_picker, null)

                    val alertDialog = AlertDialog
                            .Builder(this@EditActivity, R.style.AlertDialogColorPickTheme)
                            .setView(dialogView)
                            .setNegativeButton(getString(R.string.note_dialog_cancel)) { _, _ ->
                            }
                            .setTitle(getString(R.string.choose_color))
                            .create()

                    val radioGridGroup = dialogView.findViewById<RadioGridGroup>(R.id.radioGridGroup)
                    radioGridGroup.onChildClick = object : RadioGridGroup.OnChildClick {
                        override fun onChildClick(view: View) {
                            val color = Color.getColor(view.id)
                            Observable.fromCallable { appDatabase?.noteDao()?.changeColor(note.id, color) }
                                    .subscribeOn(Schedulers.io())
                                    .observeOn(AndroidSchedulers.mainThread())
                                    .subscribe({
                                        Log.d("Change color", "Заметка окрашена")
                                        note.color = color
                                        linearLayout.setBackgroundColor(ContextCompat.getColor(this@EditActivity, color))
                                    },
                                            { e ->
                                                Log.d("Change color", e.localizedMessage)
                                            })
                            alertDialog.cancel()
                        }
                    }

                    alertDialog.show()
                }
            }
            true
        }
    }

    override fun onBackPressed() {
        saveNote();
        super.onBackPressed()
    }

    private fun saveNote() {
        note.title = editTextTitle.text.toString()
        note.content = editTextContent.text.toString()

        if (note.id == null) {
            //Insert
            if (!note.title.isNullOrEmpty() || !note.content.isNullOrEmpty()) {
                Observable.fromCallable { appDatabase?.noteDao()?.insert(note) }
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe({
                            Log.d("Insert", "Заметка сохранена")
                        },
                                { e ->
                                    Log.d("Insert", e.localizedMessage)
                                })
            }
        } else {
            //Update
            Observable.fromCallable { appDatabase?.noteDao()?.update(note) }
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe({
                        Log.d("Update", "Заметка сохранена")
                    },
                            { e ->
                                Log.d("Update", e.localizedMessage)
                            })
        }
    }
}
