package br.edu.ifce.swappers.swappers.miscellaneous.tasks;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.view.Gravity;
import android.widget.Toast;

import java.util.List;

import br.edu.ifce.swappers.swappers.R;
import br.edu.ifce.swappers.swappers.model.Book;
import br.edu.ifce.swappers.swappers.miscellaneous.SwappersToast;
import br.edu.ifce.swappers.swappers.miscellaneous.interfaces.SearchInterface;
import br.edu.ifce.swappers.swappers.webservice.BookService;

/**
 * Created by gracyane on 06/07/2015.
 */
public class BookTask extends AsyncTask<String,Void,List<Book>> {
    private ProgressDialog progressDialog;
    private Context context;
    private SearchInterface searchInterface;

    public BookTask(Context context,SearchInterface searchInterface){
        this.context = context;
        this.searchInterface = searchInterface;
    }

    @Override
    protected void onPreExecute() {
        progressDialog = new ProgressDialog(context);
        progressDialog.setMessage(context.getString(R.string.progress_dialog_searching_message));
        progressDialog.show();
    }

    @Override
    protected List<Book> doInBackground(String... params) {

        return BookService.getBooksByTitleWS(params[0]);
    }

    @Override
    protected void onProgressUpdate(Void... params) {
    }

    @Override
    protected void onPostExecute(List<Book> listBook) {
        Toast toast;
        progressDialog.dismiss();

        try {
            if (!listBook.isEmpty()) {
                searchInterface.updateRecycleView(listBook);
            } else {
                toast = SwappersToast.makeText(context, context.getString(R.string.error_performing_search_message), Toast.LENGTH_LONG);
                toast.setGravity(Gravity.CENTER, 0, 0);
                toast.show();
            }
        }
        catch (NullPointerException e){
            SwappersToast.makeText(context, context.getString(R.string.no_results_for_serch_message), Toast.LENGTH_LONG).show();
        }
    }
}
