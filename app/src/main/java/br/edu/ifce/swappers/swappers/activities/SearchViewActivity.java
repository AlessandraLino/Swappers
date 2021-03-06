package br.edu.ifce.swappers.swappers.activities;

import android.app.SearchManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import br.edu.ifce.swappers.swappers.R;
import br.edu.ifce.swappers.swappers.miscellaneous.adapters.BookRecyclerViewAdapter;
import br.edu.ifce.swappers.swappers.model.Book;
import br.edu.ifce.swappers.swappers.miscellaneous.utils.AndroidUtils;
import br.edu.ifce.swappers.swappers.miscellaneous.tasks.BookTask;
import br.edu.ifce.swappers.swappers.miscellaneous.interfaces.RecycleViewOnClickListenerHack;
import br.edu.ifce.swappers.swappers.miscellaneous.interfaces.SearchInterface;
import br.edu.ifce.swappers.swappers.miscellaneous.SwappersToast;


public class SearchViewActivity extends AppCompatActivity implements SearchInterface, RecycleViewOnClickListenerHack {
    private RecyclerView recyclerView;
    private ArrayList<Book> mBookList;
    private ArrayList<Book> mBookListAux;
    private BookRecyclerViewAdapter adapter;
    private LinearLayoutManager lm;
    private TextView tv;
    private FrameLayout frameLayout;
    private BookTask bookTask;
    private SearchView searchView;
    private Context context;
    private Book book;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_search_view);

        this.initToolbar();
        context = getApplicationContext();
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);


        mBookListAux = new ArrayList<>();
        Book book = new Book("A Tormenta de Espadas", "George R. R. Martin", "Leya", 5.0f, 2.5f);
        mBookListAux.add(book);

        adapter = new BookRecyclerViewAdapter(this,mBookListAux);
        adapter.setRecycleViewOnClickListenerHack(this);

        frameLayout = (FrameLayout) findViewById(R.id.fl_container);

        recyclerView.setHasFixedSize(false);
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        lm = new LinearLayoutManager(getApplicationContext());

        recyclerView.setLayoutManager(lm);
        recyclerView.setAdapter(adapter);

        hendleSearch(getIntent());
    }

    private void initToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);

        if (toolbar != null){
            this.setSupportActionBar(toolbar);

            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }
    }

    @Override
    protected void onNewIntent(Intent intent) {
        setIntent(intent);
        hendleSearch(intent);
    }

    public void hendleSearch(Intent intent) {
        if (Intent.ACTION_SEARCH.equalsIgnoreCase(intent.getAction())) {
            String query = intent.getStringExtra(SearchManager.QUERY);
            searchBook(query);
        }
    }

    //Busca a ser feita com AsyncTask no google-books
    public void searchBook(String query) {
        mBookListAux.clear();
            if(AndroidUtils.isNetworkAvailable(getApplicationContext())){
                initSearchWS(query);
            }else {
                Toast toast = SwappersToast.makeText(this, "Verifique sua conexão!", Toast.LENGTH_LONG);
                toast.setGravity(Gravity.CENTER, 0, 0);
                toast.show();
            }
    }

    public void initSearchWS(String query){
        if(bookTask==null || bookTask.getStatus()!= AsyncTask.Status.RUNNING) {
            bookTask = new BookTask(this,this);
            bookTask.execute(query);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_searchview_activity, menu);

        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        MenuItem searchItem = menu.findItem(R.id.action_search);

        if( Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB ){
            searchView = (SearchView) searchItem.getActionView();
        }
        else{
            searchView = (SearchView) MenuItemCompat.getActionView(searchItem);
        }

        searchView.setQueryHint(getString(R.string.search_view_hint));

        ComponentName cn = new ComponentName(this, SearchViewActivity.class);
        searchView.setSearchableInfo(searchManager.getSearchableInfo(cn));
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void updateRecycleView(List<Book> bookList) {
        mBookListAux.clear();
        mBookListAux.addAll(bookList);
        recyclerView.setVisibility(mBookListAux.isEmpty() ? View.GONE : View.VISIBLE);
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onClickListener(View view, int position) {
        adapter = new BookRecyclerViewAdapter (context,mBookListAux);
        Intent intentDetailBookActivity = new Intent (getApplicationContext(), DetailBookActivity.class);
        Book book = adapter.getItem(position);

        intentDetailBookActivity.putExtra(AndroidUtils.SELECTED_BOOK_ID, book);
        startActivity(intentDetailBookActivity);
    }


}
