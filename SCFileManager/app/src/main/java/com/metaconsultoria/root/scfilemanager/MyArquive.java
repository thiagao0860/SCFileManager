package com.metaconsultoria.root.scfilemanager;

import java.sql.Array;

public class MyArquive implements Comparable<MyArquive> {
      public long id;
      private  String nome;
      private  String data;
      private  String path;
      private  String lastuse;

      MyArquive(){
            id=0;
            lastuse="0";
      }

      MyArquive(String Nome, String data){
          this.nome=Nome;
          this.data=data;
          id=0;
      }

      MyArquive(String Nome, String data, String path){
          this.nome=Nome;
          this.data=data;
          this.path=path;
          id=0;
      }


      MyArquive(String nome, String data, String path, String lastuse){
        this.nome=nome;
        this.data=data;
        this.path=path;
        this.lastuse=lastuse;
        id=0;
      }

      public void setNome(String nome){
          this.nome=nome;
      }

      public void setData(String data){
        this.data=data;
      }

      public void setPath(String path){
        this.path=path;
    }

      public void setLastuse(String lastuse){
        this.lastuse=lastuse;
    }

      public String getNome(){
          return this.nome;
      }

      public String getData(){
          return this.data;
      }

      public String getPath(){
        return this.path;
    }

      public String getLastUse(){
        return this.lastuse;
    }

      public static int getComparableLastUseLRU(String lastuse) throws IllegalArgumentException{
          char[] array = lastuse.toCharArray();
          for(int i=0;i<array.length-1;i++){
              if(array[i]=='1') { return i;}
              if(array[i]!='0' && array[i]!='1'){throw new IllegalArgumentException("String com valores nao binarios");}
          }
          return 0;
      }

    public static int getComparableLastUseMRU(String lastuse){
        int soma=0;
        char[] array = lastuse.toCharArray();
        for(int i=0;i<array.length-1;i++){
            if(array[i]=='1') {soma++;}
            if(array[i]!='0' && array[i]!='1'){throw new IllegalArgumentException("String com valores nao binarios");}
        }
        return soma;
    }

    @Override
    public int compareTo(MyArquive o) {
          return MyArquive.getComparableLastUseLRU(this.getLastUse())-MyArquive.getComparableLastUseLRU(o.getLastUse());
    }
}

