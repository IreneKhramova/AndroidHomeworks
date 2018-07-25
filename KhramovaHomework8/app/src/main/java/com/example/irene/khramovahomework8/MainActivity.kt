package com.example.irene.khramovahomework8

import android.os.Bundle
import android.support.design.widget.FloatingActionButton
import android.support.design.widget.Snackbar
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.StaggeredGridLayoutManager
import android.support.v7.widget.Toolbar
import android.util.Log
import android.view.View.GONE
import android.view.View.VISIBLE
import android.widget.ProgressBar
import android.widget.TextView
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers


class MainActivity : AppCompatActivity() {
    private lateinit var toolbar: Toolbar
    private lateinit var fab: FloatingActionButton
    private lateinit var recyclerView: RecyclerView
    private lateinit var progressBar: ProgressBar
    private lateinit var textViewNoNotes: TextView
    private lateinit var viewAdapter: NotesAdapter
    private lateinit var viewManager: RecyclerView.LayoutManager
    private var disposable: Disposable? = null
    private var appDatabase: AppDatabase? = null

    companion object {
        private const val SPAN_COUNT = 2
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        toolbar = findViewById(R.id.toolbarMain)
        toolbar.inflateMenu(R.menu.menu_main)

        progressBar = findViewById(R.id.progressBar)
        textViewNoNotes = findViewById(R.id.textViewNoNotes)

        fab = findViewById(R.id.floatingActionButton)
        fab.setOnClickListener {
            startActivity(EditActivity.createStartIntent(this@MainActivity, Note()))
        }

        appDatabase = AppDatabase.getInstance(this)
        disposable = load()

        viewManager = StaggeredGridLayoutManager(SPAN_COUNT, StaggeredGridLayoutManager.VERTICAL)
        recyclerView = findViewById(R.id.recyclerView)
        recyclerView.layoutManager = viewManager

        viewAdapter = NotesAdapter(object : NotesAdapter.OnNoteClick {
            override fun onClick(note: Note) {
                startActivity(EditActivity.createStartIntent(this@MainActivity, note))
            }
        },
                object : NotesAdapter.OnLongNoteClick {
                    override fun onLongClick(note: Note): Boolean {
                        AlertDialog
                                .Builder(this@MainActivity, R.style.AlertDialogLongClickTheme)
                                .setMessage(getString(R.string.note_dialog_message))
                                .setPositiveButton(getString(R.string.note_archive)) { _, _ ->
                                    Observable.fromCallable { appDatabase?.noteDao()?.archive(note.id) }
                                            .subscribeOn(Schedulers.io())
                                            .observeOn(AndroidSchedulers.mainThread())
                                            .subscribe({
                                                Log.d("Archive", "Заметка архивирована")
                                            },
                                                    { e ->
                                                        Log.d("Archive", e.localizedMessage)
                                                    })
                                }
                                .setNegativeButton(getString(R.string.note_delete)) { _, _ ->
                                    Observable.fromCallable { appDatabase?.noteDao()?.delete(note) }
                                            .subscribeOn(Schedulers.io())
                                            .observeOn(AndroidSchedulers.mainThread())
                                            .subscribe({
                                                Log.d("Delete", "Заметка удалена")
                                            },
                                                    { e ->
                                                        Log.d("Delete", e.localizedMessage)
                                                    })
                                }
                                .create().show()

                        return true
                    }

                })
        recyclerView.adapter = viewAdapter
    }

    private fun load(): Disposable? {
        progressBar.visibility = VISIBLE
        textViewNoNotes.visibility = GONE
        val flowable = appDatabase?.noteDao()?.getNotes()

        if (flowable != null) {
            return flowable
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe({ notes ->
                        viewAdapter.notes = ArrayList(notes)
                        Log.d("load success", notes.size.toString())
                        progressBar.visibility = GONE
                        if (notes.isEmpty()) {
                            textViewNoNotes.visibility = VISIBLE
                        } else {
                            textViewNoNotes.visibility = GONE
                        }
                    },
                            { e ->
                                Snackbar
                                        .make(recyclerView, getString(R.string.error_load_data), Snackbar.LENGTH_INDEFINITE)
                                        .setAction(getString(R.string.retry)) { _ ->
                                            disposable = load()
                                        }
                                        .show()
                                Log.d("load err", e.localizedMessage)
                                progressBar.visibility = GONE
                            })
        }
        return null
    }

    override fun onDestroy() {
        disposable?.dispose()
        AppDatabase.destroyInstance()
        super.onDestroy()
    }
}
