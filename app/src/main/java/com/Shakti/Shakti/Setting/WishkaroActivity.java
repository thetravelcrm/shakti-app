package com.Shakti.Shakti.Setting;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.Shakti.Shakti.Dashbord.ui.VerifySiteActivity;
import com.Shakti.Shakti.R;
import com.Shakti.Shakti.Utils.Loader;
import com.Shakti.Shakti.Utils.UtilMethods;

public class WishkaroActivity extends Fragment implements View.OnClickListener {

    Button btsubmit;
    Loader loader;
    EditText Wishmsage;

/*

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wishkaro);
*/

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View v= inflater.inflate(R.layout.activity_wishkaro, container, false);

        loader = new Loader(getActivity(), android.R.style.Theme_Translucent_NoTitleBar);

        Wishmsage=v.findViewById(R.id.Wishmsage);
        btsubmit=v.findViewById(R.id.btsubmit);
        btsubmit.setOnClickListener(this);

        return v;


    }

    @Override
    public void onClick(View view) {

        if(view==btsubmit){

if(Wishmsage.getText().toString().trim().equalsIgnoreCase("")){
    Toast.makeText(getActivity(), "Wish Massage Can not be Empty", Toast.LENGTH_SHORT).show();


}else {
     
            if (UtilMethods.INSTANCE.isNetworkAvialable(getActivity())) {

                loader.show();
                loader.setCancelable(false);
                loader.setCanceledOnTouchOutside(false);

                UtilMethods.INSTANCE.Sendwish(getActivity(),  Wishmsage.getText().toString().trim(),loader);

            } else {
                UtilMethods.INSTANCE.NetworkError(getActivity(), getResources().getString(R.string.network_error_title),
                        getResources().getString(R.string.network_error_message));
            }
}


        }
    }
}
