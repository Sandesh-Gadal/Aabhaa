package com.example.aabhaa.views.Fragments;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.aabhaa.controllers.ControllerCallback;
import com.example.aabhaa.controllers.EditProfileController;
import com.example.aabhaa.databinding.FragmentOtpVerificationBinding;
import com.example.aabhaa.views.ForgotpasswordActivity;

public class OtpVerificationFragment extends Fragment {

    private FragmentOtpVerificationBinding binding;

    public EditProfileController editProfileController;
    String emailToSend ;

    private static final long RESEND_INTERVAL_MS = 60 * 1000; // 60 seconds
    private CountDownTimer resendTimer;
    private CountDownTimer otpTimer;

    public OtpVerificationFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding= FragmentOtpVerificationBinding.inflate(inflater, container, false);
        this.editProfileController = new EditProfileController(requireContext());
        // Get email from arguments
        if (getArguments() != null && getArguments().containsKey("email")) {
            String email = getArguments().getString("email");
            emailToSend = email;
            String maskedEmail = maskEmail(email);
            Log.d("view","maksed email"+maskedEmail+"email"+email);
            binding.tvOtpInfo.setText("Please enter the verification code sent to " + maskedEmail);
        }

        // Start 120s timer
        startOtpTimer();
        setupOtpInputs();
        return binding.getRoot();


    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        binding.emailSend.setText(emailToSend);


        binding.tvResend.setOnClickListener(v -> {
            editProfileController.sendOtpToEmail(binding.emailSend, new ControllerCallback() {
                @Override
                public void onSuccess(String message) {
                    if (getActivity() == null) return;
                    getActivity().runOnUiThread(() -> {
                        Toast.makeText(getActivity(), message, Toast.LENGTH_LONG).show();
                        if (otpTimer != null) {
                            otpTimer.cancel();
                        }
                        startOtpTimer();
                        binding.otpDigit1.setText(null);
                        binding.otpDigit2.setText(null);
                        binding.otpDigit3.setText(null);
                        binding.otpDigit4.setText(null);

                        startResendCooldown();
                    });
                }

                @Override
                public void onError(String error) {
                    if (getActivity() == null) return;
                    getActivity().runOnUiThread(() ->
                            Toast.makeText(getActivity(), error, Toast.LENGTH_LONG).show());
                }
            });
        });

        binding.btnSubmitOtp.setOnClickListener(v -> {
            String otp = binding.otpDigit1.getText().toString().trim() +
                    binding.otpDigit2.getText().toString().trim() +
                    binding.otpDigit3.getText().toString().trim() +
                    binding.otpDigit4.getText().toString().trim();


            if (otp.length() < 4) {
                Toast.makeText(getContext(), "Please enter all 4 digits of the OTP", Toast.LENGTH_SHORT).show();
                return;
            }

            editProfileController.verifyOtp(emailToSend, otp, new ControllerCallback() {

                @Override
                public void onSuccess(String message) {
                    Log.d("otp"," i am here in the success");
                    if (getActivity() == null) return;
                    getActivity().runOnUiThread(() -> {
//                    Toast.makeText(getContext(), message, Toast.LENGTH_LONG).show();

                    Intent intent = new Intent(requireContext(), ForgotpasswordActivity.class);
                    intent.putExtra("start_position", 2);
                    intent.putExtra("email", emailToSend);
                    startActivity(intent);

                    // Set state activated to true to apply "success" background
                    binding.otpDigit1.setText(null);
                    binding.otpDigit2.setText(null);
                    binding.otpDigit3.setText(null);
                    binding.otpDigit4.setText(null);
                    });
                }

                @Override
                public void onError(String errorMessage) {
                    Log.d("otp"," i am here in the error bodayy");

//                    Toast.makeText(getContext(), errorMessage, Toast.LENGTH_LONG).show();
                    binding.otpDigit1.setSelected(true);
                    binding.otpDigit1.setActivated(false);

                    binding.otpDigit2.setSelected(true);
                    binding.otpDigit2.setActivated(false);

                    binding.otpDigit3.setSelected(true);
                    binding.otpDigit3.setActivated(false);

                    binding.otpDigit4.setSelected(true);
                    binding.otpDigit4.setActivated(false);

                    binding.otpDigit1.setText(null);
                    binding.otpDigit2.setText(null);
                    binding.otpDigit3.setText(null);
                    binding.otpDigit4.setText(null);
                }
            });
        });

    }



    private void startResendCooldown() {
        binding.tvResend.setClickable(false);
        binding.tvResend.setEnabled(false); // optional, disables interaction fully

        resendTimer = new CountDownTimer(RESEND_INTERVAL_MS, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                int secondsLeft = (int) (millisUntilFinished / 1000);
                binding.tvResend.setText("Wait " + secondsLeft + " sec before sending");
            }

            @Override
            public void onFinish() {
                binding.tvResend.setText("Don't receive code? Re-send");
                binding.tvResend.setClickable(true);
                binding.tvResend.setEnabled(true);
            }
        }.start();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (resendTimer != null) {
            resendTimer.cancel();
        }
    }


    private void startOtpTimer() {
        otpTimer = new CountDownTimer(120000, 1000) { // 120 seconds, tick every 1 sec
            public void onTick(long millisUntilFinished) {
                binding.tvOtpExpire.setText("OTP expires in " + (millisUntilFinished / 1000) + " sec");
            }

            public void onFinish() {
                binding.tvOtpExpire.setText("OTP expired");
            }
        }.start();
    }

    private String maskEmail(String email) {
        if (email == null || !email.contains("@")) return email;
        String[] parts = email.split("@");
        String username = parts[0];
        String domain = parts[1];

        if (username.length() > 3) {
            return username.substring(0, 3) + "******@" + domain;
        } else {
            return username + "******@" + domain;
        }
    }

    private void setupOtpInputs() {
        EditText otp1 = binding.otpDigit1;
        EditText otp2 = binding.otpDigit2;
        EditText otp3 = binding.otpDigit3;
        EditText otp4 = binding.otpDigit4;

        // Move to next box when a digit is entered
        otp1.addTextChangedListener(new GenericTextWatcher(otp1, otp2));
        otp2.addTextChangedListener(new GenericTextWatcher(otp2, otp3));
        otp3.addTextChangedListener(new GenericTextWatcher(otp3, otp4));
        otp4.addTextChangedListener(new GenericTextWatcher(otp4, null)); // Last one doesn't move forward
    }

    private  class GenericTextWatcher implements TextWatcher {
        private final EditText currentView;
        private final EditText nextView;

        public GenericTextWatcher(EditText currentView, EditText nextView) {
            this.currentView = currentView;
            this.nextView = nextView;
        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            currentView.setActivated(false);
            currentView.setSelected(false);
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            if (s.length() == 1 && nextView != null) {
                nextView.requestFocus();
            }
        }

        @Override
        public void afterTextChanged(Editable s) {}
    }

}
