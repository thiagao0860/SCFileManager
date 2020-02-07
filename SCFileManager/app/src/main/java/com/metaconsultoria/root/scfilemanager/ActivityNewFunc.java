package com.metaconsultoria.root.scfilemanager;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class ActivityNewFunc extends AppCompatActivity implements View.OnClickListener {
    private boolean isFirstAcess;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_func);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setTitle(R.string.string_cadastrar_usuario);

        Intent intent= getIntent();
        isFirstAcess =intent.getExtras().getBoolean("isFirst");

        ((Button)findViewById(R.id.button_add)).setOnClickListener(this);
        View cancel = ((Button)findViewById(R.id.button_cancel));
        cancel.setOnClickListener(this);

        if(isFirstAcess){
            cancel.setClickable(false);
            cancel.setAlpha(0.3f);
        }else{
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }
    }

    @Override
    public void onClick(View v) {

        if(v==findViewById(R.id.button_add)){
            String nome = ((EditText)findViewById(R.id.editText_nome)).getText().toString();
            String matricula = ((EditText)findViewById(R.id.editText_matricula)).getText().toString();
            String restauracao = ((EditText)findViewById(R.id.editText_restauracao)).getText().toString();
            String senha = ((EditText)findViewById(R.id.editText_senha)).getText().toString();
            String confirmSenha = ((EditText)findViewById(R.id.editText_confirm_senha)).getText().toString();

            boolean check =isThereAnEmptyText  (nome,
                                                matricula,
                                                restauracao,
                                                senha,
                                                confirmSenha);

            if(senha.equals(confirmSenha) && check){
                FuncDB fdb =new FuncDB(this);
                Funcionario fx= new Funcionario();
                fx.setNome(nome);
                fx.setMatricula(matricula);
                fx.setSenha(senha);
                fx.setRestauracao(restauracao);
                fdb.save(fx);

                Intent returnIntent = new Intent();
                returnIntent.putExtra("matricula", matricula);
                setResult(Activity.RESULT_OK,returnIntent);
                finish();
            }else{
                Toast.makeText(this,"falei",Toast.LENGTH_SHORT);
            }

        }
        if(v==findViewById(R.id.button_cancel)){
            if(!isFirstAcess){
                Intent returnIntent = new Intent();
                setResult(Activity.RESULT_CANCELED, returnIntent);
                finish();
            }
        }

    }

    public boolean isThereAnEmptyText(String nome,String matricula,String restauracao, String senha, String comfirmSenha){
        if(nome.equals("")||nome.equals(getString(R.string.string_nome))){
            String buffer= this.addToBuffer(getString(R.string.string_nome));
            Toast.makeText(this,buffer,Toast.LENGTH_SHORT).show();
            return false;
        }
        if(matricula.equals("")||matricula.equals(getString(R.string.string_matricula))){
            String buffer= this.addToBuffer(getString(R.string.string_matricula));
            Toast.makeText(this,buffer,Toast.LENGTH_SHORT).show();
            return false;
        }
        if(restauracao.equals("")||restauracao.equals(getString(R.string.string_email))){
            String buffer= this.addToBuffer(getString(R.string.string_email));
            Toast.makeText(this,buffer,Toast.LENGTH_SHORT).show();
            return false;
        }
        if(senha.equals("")||senha.equals(getString(R.string.string_senha))){
            String buffer= this.addToBuffer(getString(R.string.string_senha));
            Toast.makeText(this,buffer,Toast.LENGTH_SHORT).show();
            return false;
        }
        if(comfirmSenha.equals("")||comfirmSenha.equals(getString(R.string.string_confirm_senha))){
            String buffer= this.addToBuffer(getString(R.string.string_confirm_senha));
            Toast.makeText(this,buffer,Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    private String addToBuffer(String add){
        return getString(R.string.string_cadastrar_usuario_erro_1)+add+getString(R.string.string_cadastrar_usuario_erro_2);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int i=item.getItemId();
        if(i==android.R.id.home){
            this.onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }
}
