package com.metaconsultoria.root.scfilemanager;


import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;


public class NewComentFragment extends Fragment implements View.OnClickListener{
    private int fragHeight;
    private RecentFilesDB db;
    private MyArquive arq;
    public NewComentFragment() {
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        setRetainInstance(true);
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View inf=inflater.inflate(R.layout.fragment_new_coment, container, false);
        fragHeight=inf.getMeasuredHeight();

        Log.wtf("tamanho",String.valueOf(fragHeight));
        return inf;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        final Activity activity = getActivity();
        if(getArguments()==null) {
            activity.findViewById(R.id.imageButtonEdit).setOnClickListener(this);
        }else{
            ((TextView)activity.findViewById(R.id.coment_title)).setText("Editar Comentario:");
            db = new RecentFilesDB(getContext());
            MyComent lastcoment=db.findByComentId(arq,getArguments().getLong("coment_id"));
            ((EditText) activity.findViewById(R.id.editText_name_new_coment)).setText(lastcoment.getName());
            ((EditText) activity.findViewById(R.id.editText_coment_new_coment)).setText(lastcoment.getComent());
            activity.findViewById(R.id.imageButtonEdit).setOnClickListener(this);
        }
        final EditText myEditText1=(EditText)activity.findViewById(R.id.editText_name_new_coment);
        myEditText1.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    ((InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE)).hideSoftInputFromWindow(
                            myEditText1.getWindowToken(), 0);
                }
            }
        });

        final EditText myEditText2=(EditText)activity.findViewById(R.id.editText_coment_new_coment);
        myEditText2.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    ((InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE)).hideSoftInputFromWindow(
                            myEditText2.getWindowToken(), 0);
                }
            }
        });
        super.onViewCreated(view, savedInstanceState);
    }

    public int getFragHeight() {
        return fragHeight;
    }

    public void setFragHeight(int fragHeight) {
        this.fragHeight = fragHeight;
    }

    public MyArquive getArq() {
        return arq;
    }

    public void setArq(MyArquive arq) {
        this.arq = arq;
    }

    public static String utilGetDate(){
        SimpleDateFormat formater = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        Calendar cal =Calendar.getInstance();
        Date data = cal.getTime();
        return formater.format(data);
    }

    public static void utilHideKeyboard(Context context, View editText) {
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(editText.getWindowToken(), 0);
    }


    @Override
    public void onClick(View v) {
        if(v.getId()==R.id.imageButtonEdit){
            if(getArguments()==null){
                EditText editText1 = (EditText) getActivity().findViewById(R.id.editText_name_new_coment);
                EditText editText2 = (EditText) getActivity().findViewById(R.id.editText_coment_new_coment);
                NewComentFragment.utilHideKeyboard(getActivity(), editText2);
                String name = editText1.getText().toString();
                String text = editText2.getText().toString();
                if (!name.equals("") && !name.equals(getString(R.string.new_coment_name_hint))) {
                    if (!text.equals("") && !text.equals(getString(R.string.new_coment_coment_hint))) {
                        MyComent coment = new MyComent(name, text, NewComentFragment.utilGetDate());
                        db = new RecentFilesDB(getContext());
                        db.saveComent(coment, arq);
                        getActivity().onBackPressed();
                        Toast.makeText(getActivity(), R.string.coment_acepted, Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(getActivity(), R.string.erro_coment_2, Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(getActivity(), R.string.erro_coment_1, Toast.LENGTH_SHORT).show();
                }
            }
        }else{
            EditText editText1 = (EditText)getActivity().findViewById(R.id.editText_name_new_coment);
            EditText editText2 = (EditText)getActivity().findViewById(R.id.editText_coment_new_coment);
            NewComentFragment.utilHideKeyboard(getActivity(), editText2);
            String name = editText1.getText().toString();
            String text = editText2.getText().toString();
            if (!name.equals("") && !name.equals(getString(R.string.new_coment_name_hint))) {
                if (!text.equals("") && !text.equals(getString(R.string.new_coment_coment_hint))) {
                    db = new RecentFilesDB(getContext());
                    MyComent coment=db.findByComentId(arq,getArguments().getLong("coment_id"));
                    coment.setName(name);
                    coment.setComent(text);
                    coment.setData_hr(utilGetDate());
                    db.updateByComentId(arq,coment);
                    getActivity().onBackPressed();
                    Toast.makeText(getActivity(), R.string.coment_acepted, Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getActivity(), R.string.erro_coment_2, Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(getActivity(), R.string.erro_coment_1, Toast.LENGTH_SHORT).show();
            }
        }
    }
}
