package info.akshaysadarangani.ILoveZappos.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import info.akshaysadarangani.ILoveZappos.R;

import info.akshaysadarangani.ILoveZappos.rest.ApiClient;
import info.akshaysadarangani.ILoveZappos.rest.ApiInterface;

/**
 * Created by Akshay on 2/7/2017.
 */

public class SearchActivity extends AppCompatActivity {

    // private static final String TAG = MainActivity.class.getSimpleName(); //for debugging


    // Zappos API key
    private final static String API_KEY = "b743e26728e16b81da139182bb2094357c31d331";

    // Elements
    private EditText searchTerm;
    private TextInputLayout inputLayoutSearch;
    private Button btnSearch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        if (API_KEY.isEmpty()) {
            Toast.makeText(getApplicationContext(), "Invalid API", Toast.LENGTH_LONG).show();
            return;
        }

        inputLayoutSearch = (TextInputLayout) findViewById(R.id.input_layout_name);
        searchTerm = (EditText) findViewById(R.id.search_term);
        btnSearch = (Button) findViewById(R.id.btn_search);
        searchTerm.addTextChangedListener(new MyTextWatcher(searchTerm));

        searchTerm.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                boolean handled = false;
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                    submitForm();
                    handled = true;
                }
                return handled;
            }
        });

        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                submitForm();
            }
        });
    }

        private void submitForm() {

            if(!validateSearch()) {
                return;
            }
            String term = searchTerm.getText().toString();
            Intent intent = new Intent(getBaseContext(), MainActivity.class);
            intent.putExtra("SEARCH_QUERY", term);
            searchTerm.setText("");
            startActivity(intent);
        }

        private boolean validateSearch() {
            if (searchTerm.getText().toString().trim().isEmpty()) {
                inputLayoutSearch.setError(getString(R.string.err_msg_search));
                requestFocus(searchTerm);
                return false;
            }
            else {
                inputLayoutSearch.setErrorEnabled(false);
            }
            return true;
        }
        private void requestFocus(View view) {
            if (view.requestFocus()) {
                getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
            }
        }



        ApiInterface apiService =
                ApiClient.getClient().create(ApiInterface.class);

        class MyTextWatcher implements TextWatcher {

            private View view;

            private MyTextWatcher(View view) {
                this.view = view;
            }

            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (validateSearch())  {
                    btnSearch.setEnabled(true);
                }
                else {
                    btnSearch.setEnabled(false);
                }
            }

            public void afterTextChanged(Editable editable) {
                switch (view.getId()) {
                    case R.id.search_term:
                        validateSearch();
                        break;
                }
            }
        }

}
