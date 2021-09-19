package com.example.wishlist

import android.app.Activity
import android.app.Dialog
import android.content.DialogInterface
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.ListView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.constraintlayout.motion.widget.OnSwipe
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton

class MainActivity : AppCompatActivity() {
    private lateinit var rvItems: RecyclerView
    private lateinit var fabAdd: FloatingActionButton
    private lateinit var lista: MutableList<Desejo>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        this.lista = mutableListOf()

        this.rvItems = findViewById(R.id.rvItems)
        this.fabAdd = findViewById(R.id.fabAdd)

        this.rvItems.adapter = DesejoAdapter(this.lista)

        val resultForm = registerForActivityResult(ActivityResultContracts.StartActivityForResult()){
            if (it.resultCode == RESULT_OK){
                val desejo = it.data?.getSerializableExtra("DESEJO") as Desejo
                this.lista.add(desejo)
                (this.rvItems.adapter as DesejoAdapter).notifyDataSetChanged()
            }
        }

        ItemTouchHelper(OnSwipe()).attachToRecyclerView(this.rvItems)

        this.fabAdd.setOnClickListener {
            val intent = Intent(this, FormActivity::class.java)
            resultForm.launch(intent)
        }
        (this.rvItems.adapter as DesejoAdapter).listener = OnItemClick()

    }

        inner class OnItemClick: OnItemClickRecycleView{
            override fun onItemClick(position: Int) {
                val desejo = this@MainActivity.lista[position]
                Toast.makeText(this@MainActivity, desejo.descricao, Toast.LENGTH_SHORT).show()
            }

            override fun onItemClickLong(position: Int) {
                dialogDelete(position)
            }
        }

    //aqui
        fun dialogDelete( position: Int){
        var alertDialog = AlertDialog.Builder(this)
        var desejo = this@MainActivity.lista[position]

        alertDialog.setTitle("Alerta!")
        alertDialog.setMessage("Deseja remover o '${desejo.descricao}'? ")
        alertDialog.setPositiveButton("Sim") { dialogInterface: DialogInterface, i: Int ->
            this@MainActivity.lista.removeAt(position)
            (this@MainActivity.rvItems.adapter as DesejoAdapter).notifyDataSetChanged()
        }
        alertDialog.setNegativeButton("Não", null)
        alertDialog.create().show()
    }

    inner class OnSwipe: ItemTouchHelper.SimpleCallback(
        ItemTouchHelper.UP or ItemTouchHelper.DOWN,
        ItemTouchHelper.START or ItemTouchHelper.END){
        override fun onMove(
            recyclerView: RecyclerView,
            viewHolder: RecyclerView.ViewHolder,
            target: RecyclerView.ViewHolder
        ): Boolean {
            (this@MainActivity.rvItems.adapter as DesejoAdapter).swap(viewHolder.adapterPosition, target.adapterPosition)
            return true
        }

        override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
            (this@MainActivity.rvItems.adapter as DesejoAdapter).del(viewHolder.adapterPosition)
        }
    }

}