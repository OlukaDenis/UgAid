package com.app.ugaid.view.ui.donate;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.app.ugaid.R;
import com.app.ugaid.utils.Config;
import com.paypal.android.sdk.payments.PayPalConfiguration;
import com.paypal.android.sdk.payments.PayPalPayment;
import com.paypal.android.sdk.payments.PayPalService;
import com.paypal.android.sdk.payments.PaymentActivity;
import com.paypal.android.sdk.payments.PaymentConfirmation;

import org.json.JSONException;
import org.json.JSONObject;

import java.math.BigDecimal;
import java.util.Objects;

import static android.app.Activity.RESULT_OK;
import static com.app.ugaid.utils.Config.PAYPAL_REQUEST_CODE;

public class FundsDonationFragment extends Fragment {

    private DonateViewModel donateViewModel;
    private static PayPalConfiguration config = new PayPalConfiguration()
            .environment(PayPalConfiguration.ENVIRONMENT_SANDBOX)
            .clientId(Config.PAYPAL_CLIENT_ID);
    private TextInputEditText etAmount;
    private Button btn_donate_now;
    private String amount = "";
    private AlertDialog.Builder builder;
    private Resources res;
    private static final String TAG = "FundsDonationFragment";
    private FirebaseAnalytics mFirebaseAnalytics;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View root;
        if (Config.isNetworkAvailable(getContext())) {
            root = inflater.inflate(R.layout.fragment_funds_donation, container, false);

            //Init firebase analytics
            mFirebaseAnalytics = FirebaseAnalytics.getInstance(getActivity());
            mFirebaseAnalytics.setCurrentScreen(getActivity(), this.getClass().getSimpleName(), this.getClass().getSimpleName());

            //start paypal service
            Intent intent = new Intent(getActivity(), PayPalService.class);
            intent.putExtra(PayPalService.EXTRA_PAYPAL_CONFIGURATION, config);
            getActivity().startService(intent);

            etAmount = root.findViewById(R.id.donate_amount);
            btn_donate_now = root.findViewById(R.id.btn_donate_now);

            res = getResources();

            btn_donate_now.setOnClickListener(v -> processPayment());
        } else {
            root = inflater.inflate(R.layout.fragment_no_connection, container, false);
        }


        return root;
    }

    private void processPayment() {
        amount = etAmount.getText().toString();
        if (amount.isEmpty()){
            etAmount.setError("Please enter the amount to donate");
        } else {
            PayPalPayment payPalPayment = new PayPalPayment(new BigDecimal(amount), "USD",
                    "Donate", PayPalPayment.PAYMENT_INTENT_SALE);
            Intent intent = new Intent(getActivity(), PaymentActivity.class);
            intent.putExtra(PayPalService.EXTRA_PAYPAL_CONFIGURATION, config);
            intent.putExtra(PaymentActivity.EXTRA_PAYMENT, payPalPayment);
            startActivityForResult(intent, PAYPAL_REQUEST_CODE);
            etAmount.setText("");

            Bundle bundle = new Bundle();
            bundle.putString(FirebaseAnalytics.Param.CURRENCY, "Naira");
            bundle.putString(FirebaseAnalytics.Param.VALUE, amount);
            bundle.putString(FirebaseAnalytics.Param.ITEM_NAME, "Donation");
            mFirebaseAnalytics.logEvent(FirebaseAnalytics.Event.SELECT_CONTENT, bundle);
        }
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PAYPAL_REQUEST_CODE){
            if (resultCode == RESULT_OK){
                PaymentConfirmation confirmation = data.getParcelableExtra(PaymentActivity.EXTRA_RESULT_CONFIRMATION);
                if (confirmation != null){
                    try {
                        String paymentDetails = confirmation.toJSONObject().toString(4);
                        Log.i(TAG, "Payment Details: " +paymentDetails+"< = >"+amount);

                        JSONObject jsonObject = new JSONObject(paymentDetails);
                        JSONObject response = jsonObject.getJSONObject("response");
                        String payment_status = response.getString("state");

                        if (payment_status.equals("approved")) {
                            openSuccessDialog(payment_status, amount);
                        } else {
                            openFailureDialog(payment_status, amount);
                        }

                    } catch (JSONException e){
                        e.printStackTrace();
                        Log.e(TAG, "onActivityResult: ",e );
                    }
                }
            } else if (resultCode == Activity.RESULT_CANCELED)
                Toast.makeText(getActivity(), "Cancel", Toast.LENGTH_SHORT).show();
        } else if (resultCode == PaymentActivity.RESULT_EXTRAS_INVALID)
            Toast.makeText(getActivity(), "Invalid", Toast.LENGTH_SHORT).show();

    }

    private void openFailureDialog(String payment_status, String amount) {
        builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = this.getLayoutInflater();
        View details_layout = inflater.inflate(R.layout.layout_payment_details, null);
        builder.setView(details_layout);
        builder.setCancelable(false);

        ImageView mImage = details_layout.findViewById(R.id.payment_image);
        TextView mStatus = details_layout.findViewById(R.id.tv_payment_status);
        Button mBtnAction = details_layout.findViewById(R.id.btn_action_payment);
        TextView mAmount = details_layout.findViewById(R.id.tv_payment_amount);
        etAmount.setText("");

        mStatus.setText(String.format(res.getString(R.string.payment_failed), payment_status));
        mStatus.setTextColor(res.getColor(R.color.red));
        mImage.setBackgroundResource(R.drawable.ic_cancel);
        mBtnAction.setBackground(res.getDrawable(R.drawable.error_button));

        mAmount.setVisibility(View.GONE);

        final AlertDialog dialog = builder.show();
        mBtnAction.setText(R.string.btn_fail);
        mBtnAction.setOnClickListener(v -> {
            dialog.dismiss();
        });
    }

    private void openSuccessDialog(String payment_status, String amount) {
        builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = this.getLayoutInflater();
        View details_layout = inflater.inflate(R.layout.layout_payment_details, null);
        builder.setView(details_layout);
        builder.setCancelable(false);

        ImageView mImage = details_layout.findViewById(R.id.payment_image);
        TextView mStatus = details_layout.findViewById(R.id.tv_payment_status);
        Button mBtnAction = details_layout.findViewById(R.id.btn_action_payment);
        TextView mAmount = details_layout.findViewById(R.id.tv_payment_amount);
        etAmount.setText("");

        mStatus.setText(String.format(res.getString(R.string.payment_successful), payment_status));
        mStatus.setTextColor(res.getColor(R.color.green));
        mImage.setBackgroundResource(R.drawable.ic_check);
        mAmount.setText(String.format(res.getString(R.string.payment_amount), amount));

        final AlertDialog dialog = builder.show();
        mBtnAction.setText(R.string.btn_success);
        mBtnAction.setOnClickListener(v -> {
            dialog.dismiss();
            openHomeFragment();
        });


    }

//    private void openResultDialog(String paymentDetails, String amount) {
//
//
//
//
//        try {
//
//
//
//
//            } else {
//                mStatus.setText(String.format(res.getString(R.string.payment_failed), payment_status));
//                mStatus.setTextColor(res.getColor(R.color.red));
//                mImage.setBackgroundResource(R.drawable.ic_cancel);
//                mBtnAction.setBackground(res.getDrawable(R.drawable.error_button));
//                mBtnAction.setText(R.string.btn_fail);
//                mAmount.setVisibility(View.GONE);
//                builder.setOnDismissListener(DialogInterface::dismiss);
//            }
//
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
//
//
//    }

    private void openDonateFragment() {
        NavController navController = Navigation.findNavController(Objects.requireNonNull(getActivity()), R.id.nav_host_fragment);
        navController.navigate(R.id.nav_donate);
    }

    private void openHomeFragment() {
        NavController navController = Navigation.findNavController(Objects.requireNonNull(getActivity()), R.id.nav_host_fragment);
        navController.navigate(R.id.nav_home);
    }

    @Override
    public void onDestroy() {
        getActivity().stopService(new Intent(getActivity(), PayPalService.class));
        super.onDestroy();
    }
}