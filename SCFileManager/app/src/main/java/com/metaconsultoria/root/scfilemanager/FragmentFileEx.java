package com.metaconsultoria.root.scfilemanager;

import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;
import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;


public class FragmentFileEx extends Fragment  {
    private RecyclerView m_RootList;
    private Listedfiles listfiles = new Listedfiles();
    private ArrayList m_filesp = new ArrayList<String>();
    private ArrayList m_filesPathp = new ArrayList<String>();
    private String m_root =null;
    private String ultimodir;
    private Uri file;
    private View mView;
    private File m_file;
    private ListAdapter m_listAdapter = null;
    private StorageAccess task;
    private ProgressBar progress;
    private String procura = null;

    @Override
    public void onCreate( Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        this.mView= inflater.inflate(R.layout.fraglayoutex, container,false);
        m_RootList = mView.findViewById(R.id.rl_lvListRoot);
        m_RootList.setLayoutManager(new LinearLayoutManager(getActivity()));
        m_RootList.setItemAnimator(new DefaultItemAnimator());
        progress = (ProgressBar) mView.findViewById(R.id.progress);
        progress.setVisibility(View.VISIBLE);
        Bundle bundle = getArguments();
        m_root = bundle.getString("arqpath");
        if(ultimodir==null) {
            ultimodir = m_root;
        }
        getDirFromRoot(ultimodir);
        return mView;
    }

    public void refresh(Bundle data){
        String mPath;
        mPath = data.getString("arqpath");
        ultimodir = mPath;
        getDirFromRoot(mPath);
    }

    public void NewSearch(String text){
        listfiles = new Listedfiles();
        m_filesp = new ArrayList<String>();
        m_filesPathp = new ArrayList<String>();
        if(text!=null && text.length()!=0) {
            getDirFromRoot(ultimodir);
            procura = text;
        }else{
            procura = null;
            getDirFromRoot(ultimodir);
        }
    }

    //// Obtendo os arquivos da memória
    private void getDirFromRoot(String p_rootPath) {
        listfiles = new Listedfiles();
        m_filesp = new ArrayList<String>();
        m_filesPathp = new ArrayList<String>();
        m_file = new File(p_rootPath);
        m_listAdapter = null;
        m_RootList.setAdapter(m_listAdapter);
        if(task != null){
            task.cancel(true);
        }
        task = new StorageAccess();
        task.execute(p_rootPath);
    }
    private class StorageAccess extends AsyncTask<String,Void,Listedfiles>{
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progress.setVisibility(View.VISIBLE);
        }
        @Override
        protected Listedfiles doInBackground(String... strings) {
            if (procura == null) {
                if (m_file.isDirectory()) {
                    File[] m_filesArray = m_file.listFiles();
                    if (!ultimodir.equals(m_root)) {
                        listfiles.m_itemp.add("../");
                        listfiles.m_pathp.add(m_file.getParent());
                    }
                    Arrays.sort(m_filesArray);
                    for (int i = 0; i < m_filesArray.length; i++) {
                        File file = m_filesArray[i];
                        if (file.isDirectory()) {
                            listfiles.m_itemp.add(file.getName());
                            listfiles.m_pathp.add(file.getPath());
                        } else {
                            m_filesp.add(file.getName());
                            m_filesPathp.add(file.getPath());
                        }
                    }
                    for (Object m_AddFile : m_filesp) {
                        listfiles.m_itemp.add(m_AddFile);
                    }
                    for (Object m_AddPath : m_filesPathp) {
                        listfiles.m_pathp.add(m_AddPath);
                    }
                } else {
                    m_filesp.add(m_file.getName());
                    m_filesPathp.add(m_file.getPath());
                    for (Object m_AddFile : m_filesp) {
                        listfiles.m_itemp.add(m_AddFile);
                    }
                    for (Object m_AddPath : m_filesPathp) {
                        listfiles.m_pathp.add(m_AddPath);
                    }
                }
            }else{
                File[] m_filesArray = m_file.listFiles();
                Arrays.sort(m_filesArray);
                for (int i = 0; i < m_filesArray.length; i++) {
                    File file = m_filesArray[i];
                    final boolean contains = file.getName().toLowerCase().contains(procura.toLowerCase());

                    if (file.isDirectory()) {
                        if(contains){
                            listfiles.m_itemp.add(file.getName());
                            listfiles.m_pathp.add(file.getPath());
                        }
                        getDirFromRootSEC(file.toString(), procura);
                    } else {
                        if(contains) {
                            m_filesp.add(file.getName());
                            m_filesPathp.add(file.getPath());
                        }
                    }
                }
                for (Object m_AddFile : m_filesp) {
                    listfiles.m_itemp.add(m_AddFile);
                }
                for (Object m_AddPath : m_filesPathp) {
                    listfiles.m_pathp.add(m_AddPath);
                }
            }
            return listfiles;
        }

        @Override
        protected void onCancelled() {
            task = null;
            super.onCancelled();
        }

        @Override
        protected void onPostExecute(Listedfiles filesarray) {
            super.onPostExecute(filesarray);
            task = null;
            changelistview(filesarray);
        }
    }
    public void getDirFromRootSEC (String R2Path, String procura){
        File m_file = new File(R2Path);
        File[] m_filesArray = m_file.listFiles();
        Arrays.sort(m_filesArray);
        for (int i = 0; i < m_filesArray.length; i++) {
            File file = m_filesArray[i];
            final boolean contains = file.getName().toLowerCase().contains(procura.toLowerCase());
            if (file.isDirectory()) {
                if(contains){
                    listfiles.m_itemp.add(file.getName());
                    listfiles.m_pathp.add(file.getPath());
                }
                getDirFromRootSEC(file.toString(), procura);
            } else {
                if(contains) {
                    m_filesp.add(file.getName());
                    m_filesPathp.add(file.getPath());
                }
            }
        }
    }

    private void changelistview(Listedfiles list){
        if(list.m_itemp.toArray().length>0) {
            m_listAdapter = new ListAdapter(getActivity(), list,onClickitem());
        }else{
            Toast.makeText(getContext().getApplicationContext(),"Arquivo não encontrado",Toast.LENGTH_LONG).show();
        }
        m_RootList.setAdapter(m_listAdapter);
        progress.setVisibility(View.INVISIBLE);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        progress.setVisibility(View.INVISIBLE);
    }

    private ListAdapter.ListOnClickListener onClickitem(){
        return new ListAdapter.ListOnClickListener(){

            @Override
            public void onClickitem(View view, int idx) {
                File m_isFile = new File(listfiles.m_pathp.get(idx).toString());
                int m_ultimoponto = m_isFile.getAbsolutePath().lastIndexOf(".");
                String m_caminhofile = m_isFile.getAbsolutePath();
                file = Uri.fromFile(m_isFile);
                if (m_isFile.isDirectory()) {
                    ultimodir= m_isFile.toString();
                    getDirFromRoot(m_isFile.toString());
                } else {
                    if (m_caminhofile.substring(m_ultimoponto).equalsIgnoreCase(".pdf")) {
                        FragmentListener mListener = (FragmentListener) getActivity();
                        MyArquive arq = new MyArquive(
                                file.getPath().substring(file.getPath().lastIndexOf('/')+1,file.getPath().lastIndexOf('.')),
                                file.toString()
                        );
                        mListener.setPdfActivity(arq);
                    }
                }

            }
        };
    }
    public boolean upDir(){
        if(ultimodir==null || ultimodir.equals(m_root)){return true;}
        else{
            String path = ultimodir.substring(0,ultimodir.lastIndexOf('/'));
            Log.i("teste de path",path);
            Bundle arguments = new Bundle();
            arguments.putString("arqpath", path);
            arguments.putString("text",null);
            this.refresh(arguments);
            return false;
        }
    }

    public interface FragmentListener {
        void setPdfActivity(MyArquive arq);
        void Scanner(String qR);
    }
}
