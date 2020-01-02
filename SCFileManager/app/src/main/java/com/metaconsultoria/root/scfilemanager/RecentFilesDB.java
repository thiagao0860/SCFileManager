package com.metaconsultoria.root.scfilemanager;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Build;
import android.support.annotation.RequiresApi;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


public class RecentFilesDB extends SQLiteOpenHelper {
    public static final String NOME_DO_BANCO = "recentfiles.sqlite";
    private static final int VERSAO_DO_BANCO = 1;
    private Context cont;

    RecentFilesDB(Context contx) {
        super(contx, NOME_DO_BANCO, null, VERSAO_DO_BANCO);
        this.cont = contx;
    }

    public void onCreate(SQLiteDatabase db) {
        db.execSQL(
                "create table if not exists recent_arquivos (id integer primary key autoincrement,nome text,data text,path text,lastuse text,lastuseLRU integer default 0,lastuseMRU integer default 0);"
        );
    }

    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public long save(MyArquive myArquive) {
        long id = myArquive.id;
        SQLiteDatabase db = getWritableDatabase();
        try {

            MyArquive outra_coisa = findByPath(myArquive.getPath(), db);
            if (myArquive.getPath().equals(outra_coisa.getPath())) {
                return 0;
            }

            ContentValues values = new ContentValues();
            values.put("nome", myArquive.getNome());
            values.put("data", myArquive.getData());
            values.put("path", myArquive.getPath());
            values.put("lastuse", myArquive.getLastUse());
            if (id == 0) {
                return db.insert("recent_arquivos", "", values);
            } else {
                String _id = String.valueOf(myArquive.id);
                String[] whereArgs = new String[]{_id};
                return db.update("recent_arquivos", values, "_id", whereArgs);
            }
        } finally {
            db.close();
        }
    }


    public List<MyArquive> findAll() {
        SQLiteDatabase db = getWritableDatabase();
        try {
            Cursor c = db.query("recent_arquivos", null, null, null, null, null, null);
            if (c.getCount() > 0) {
                List<MyArquive> fx_vetor = new ArrayList<MyArquive>();
                if (c.moveToFirst()) {
                    do {
                        MyArquive fx = new MyArquive();
                        fx.setNome(c.getString(c.getColumnIndex("nome")));
                        fx.setData(c.getString(c.getColumnIndex("data")));
                        fx.setPath(c.getString(c.getColumnIndex("path")));
                        fx.setLastuse(c.getString(c.getColumnIndex("lastuse")));
                        fx_vetor.add(fx);
                    } while (c.moveToNext());
                }
                return fx_vetor;
            } else {
                return null;
            }
        } finally {
            db.close();
        }
    }


    public MyArquive findByPath(String path) {
        SQLiteDatabase db = getWritableDatabase();
        try {
            Cursor c = db.query("recent_arquivos", null, "matricula='" + path + "'", null, null, null, null);
            if (c.getCount() > 0) {
                MyArquive fx = new MyArquive();
                c.moveToFirst();
                fx.setNome(c.getString(c.getColumnIndex("nome")));
                fx.setData(c.getString(c.getColumnIndex("data")));
                fx.setPath(c.getString(c.getColumnIndex("path")));
                fx.setLastuse(c.getString(c.getColumnIndex("lastuse")));
                return fx;
            } else {
                return null;
            }
        } finally {
            db.close();
        }
    }

    public MyArquive findByPath(String path, SQLiteDatabase db) {

        try {
            Cursor c = db.query("recent_arquivos", null, "path='" + path + "'", null, null, null, null);
            if (c.getCount() > 0) {
                MyArquive fx = new MyArquive();
                c.moveToFirst();
                fx.setNome(c.getString(c.getColumnIndex("nome")));
                fx.setData(c.getString(c.getColumnIndex("data")));
                fx.setPath(c.getString(c.getColumnIndex("path")));
                fx.setLastuse(c.getString(c.getColumnIndex("lastuse")));
                return fx;
            } else {
                return null;
            }
        } finally {
            db.close();
        }
    }


    public int deleteByPath(String path) {
        SQLiteDatabase db = getWritableDatabase();
        try {
            int count = db.delete("recent_arquivos", "path=?", new String[]{path});
            return count;
        } finally {
            db.close();
        }
    }

    public int deleteAll() {
        SQLiteDatabase db = getWritableDatabase();
        try {
            int count = db.delete("recent_arquivos", "", null);
            return count;
        } finally {
            db.close();
        }
    }

    public void execSQL(String sql) {
        SQLiteDatabase db = getWritableDatabase();
        try {
            db.execSQL(sql);
        } finally {
            db.close();
        }
    }

    private int updateRowsByPath(String colunm,String  new_value, String path, SQLiteDatabase db) {

        ContentValues values = new ContentValues();
        values.put(colunm, new_value);
        String selection = "path" + " LIKE ?";
        String[] selectionArgs = {path};

        return db.update(
                "recent_arquivos",
                values,
                selection,
                selectionArgs);
    }

    private int updateRowsByPath(String colunm,int new_value, String path, SQLiteDatabase db) {

        ContentValues values = new ContentValues();
        values.put(colunm, new_value);
        String selection = "path" + " LIKE ?";
        String[] selectionArgs = {path};

        return db.update(
                "recent_arquivos",
                values,
                selection,
                selectionArgs);
    }


    private void refreshDataBase(String path){
        SQLiteDatabase db= getWritableDatabase();
        List<MyArquive> array= findAll();
        String buffer;
        for(MyArquive iterador: array){
            if(iterador.getPath().equals(path)){buffer = "0" + iterador.getLastUse();}
            else{buffer = "0" + iterador.getLastUse();}
            if(buffer.length()>64){
                buffer=buffer.substring(0,63);
            }
            updateRowsByPath("lastuse",buffer,iterador.getPath(),db);
            updateRowsByPath("lastuseLRU",MyArquive.getComparableLastUseLRU(buffer),iterador.getPath(),db);
            updateRowsByPath("lastuseMRU",MyArquive.getComparableLastUseMRU(buffer),iterador.getPath(),db);
        }
    }

    public void myCommit(MyArquive myArquive){
        if(findByPath(myArquive.getPath())==null){
            save(myArquive);
        }
        refreshDataBase(myArquive.getPath());
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public List<MyArquive> mySelect(int number){
        SQLiteDatabase db = getWritableDatabase();
        try {
            Cursor c = db.query("recent_arquivos", null, null, null, null, null, "lastuseMRU", String.valueOf(number));
            if (c.getCount() > 0) {
                List<MyArquive> fx_vetor = new ArrayList<MyArquive>();
                if (c.moveToFirst()) {
                    do {
                        MyArquive fx = new MyArquive();
                        fx.setNome(c.getString(c.getColumnIndex("nome")));
                        fx.setData(c.getString(c.getColumnIndex("data")));
                        fx.setPath(c.getString(c.getColumnIndex("path")));
                        fx.setLastuse(c.getString(c.getColumnIndex("lastuse")));
                        fx_vetor.add(fx);
                    } while (c.moveToNext());
                }
                Collections.sort(fx_vetor);
                return fx_vetor;
            } else {
                return null;
            }
        } finally {
            db.close();
        }
    }
}
