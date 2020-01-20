package com.example.proyecto2

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class RegistrarActivity : AppCompatActivity() {

    private lateinit var txtNombre:EditText
    private lateinit var txtApellido:EditText
    private lateinit var txtEmail:EditText
    private lateinit var txtPassword:EditText
    private lateinit var progressBar:ProgressBar
    private lateinit var dbReference:DatabaseReference
    private lateinit var database:FirebaseDatabase
    private lateinit var auth:FirebaseAuth



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_registrar)
        txtNombre=findViewById(R.id.txtNombre)
        txtApellido=findViewById(R.id.txtApellido)
        txtEmail=findViewById(R.id.txtEmail)
        txtPassword=findViewById(R.id.txtPassword)

        progressBar=findViewById(R.id.progressBar)
        database= FirebaseDatabase.getInstance()
        auth=FirebaseAuth.getInstance()
        dbReference=database.reference.child("user")//crear user en firebase

    }
    //crear metodo del onclick del button
    fun register(view:View){
        crearNuevaCuenta()
    }
    private fun crearNuevaCuenta(){
        //obteniendo valores de la caja de texto
        val nombre:String=txtNombre.text.toString()
        val apellido:String=txtApellido.text.toString()
        val email:String=txtEmail.text.toString()
        val password:String=txtPassword.text.toString()

        //verifico que no sean vacios
        if(!TextUtils.isEmpty(nombre) &&!TextUtils.isEmpty(apellido) && !TextUtils.isEmpty(email) &&!TextUtils.isEmpty(password)){
                progressBar.visibility=View.VISIBLE
            auth.createUserWithEmailAndPassword(email,password)
                .addOnCompleteListener(this){
                    task ->
                    if(task.isComplete) {
                        val user: FirebaseUser? = auth.currentUser
                        verificarEmail(user)

                        val userBD=dbReference.child(user?.uid)
                        userBD.child("Nombre").setValue(nombre)
                        userBD.child("Apellido").setValue(apellido)
                        //mando los datos a la BD
                        cambiarPantalla()
                    }
                }


        }

    }
    private fun cambiarPantalla(){
        startActivity(Intent(this,LoginActivity::class.java))
    }
    //? para que sea de manera segura, es decir q no se caiga con un null
    private fun verificarEmail(user: FirebaseUser?){
        user?.sendEmailVerification()
            ?.addOnCompleteListener(this){
                task ->
                if(task.isComplete)
                    Toast.makeText(this,"Email enviado",Toast.LENGTH_LONG).show()
                else
                    Toast.makeText(this,"Error al enviar el Email",Toast.LENGTH_LONG).show()
            }
    }
}
