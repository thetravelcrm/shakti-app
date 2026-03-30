package com.Shakti.Shakti.Dashbord.ui;

import android.os.Bundle;

import androidx.appcompat.widget.AppCompatTextView;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.Shakti.Shakti.Utils.Loader;
import com.Shakti.Shakti.Utils.UtilMethods;
import com.Shakti.Shakti.R;

import cn.pedant.SweetAlert.SweetAlertDialog;
import de.hdodenhof.circleimageview.CircleImageView;

public class ProfileFragment extends Fragment implements View.OnClickListener {

    AppCompatTextView EditProfile,aboutus,Privacypolicy,share,logout,Walletpayment,leadsaccepte;
    CircleImageView customerimage;

    Loader loader;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
         View v= inflater.inflate(R.layout.activity_profile, container, false);



        return v;
    }







    @Override
    public void onClick(View view) {






   if(view==Walletpayment){







         }


  if(view==customerimage){





        }

       if(view==aboutus){




        }


 if(view==Privacypolicy){





        }


 if(view==share){




        }

 if(view==logout){

     final SweetAlertDialog alertDialog = new SweetAlertDialog(getActivity());
     alertDialog.setTitle("Alert!");
     alertDialog.setContentText("Do you want to logout?");
     alertDialog.setCancelButton("Cancel", new SweetAlertDialog.OnSweetClickListener() {
         @Override
         public void onClick(SweetAlertDialog sweetAlertDialog) {
             alertDialog.dismiss();
         }
     });

     alertDialog.setConfirmButton("Yes", new SweetAlertDialog.OnSweetClickListener() {
         @Override
         public void onClick(SweetAlertDialog sweetAlertDialog) {

             UtilMethods.INSTANCE.logout(getActivity());
         }
     });

     alertDialog.show();

        }
    }


}