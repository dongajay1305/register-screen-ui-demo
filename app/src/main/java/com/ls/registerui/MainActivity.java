package com.ls.registerui;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Rect;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.Layout;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Patterns;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, View.OnFocusChangeListener {


    EditText etLastName, etFirstName, etEmail, etMobile, etPassword;
    LinearLayout llName, llEmail, llMobile, llPassword, llPasswordStrength;
    TextView tvbtnRegister,tvLogin, tvTandC;
    TextView tvErrorName, tvErrorEmail, tvErrorMobile, tvErrorPassword, tvPasswordStrength;
    ImageButton ibEye;
    ImageView ivStrength;

    boolean bvalName = false,bvalEmail = false, bvalMobile = false, bvalPassword = false;

    static int STRONG_PASSWORD = 5;

    LinearLayout llPopup;
    int height;
    final int sdk = android.os.Build.VERSION.SDK_INT;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register_layout);

        llPopup = findViewById(R.id.ll_popup);

        ViewTreeObserver vto = llPopup.getViewTreeObserver();
        vto.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN) {
                    llPopup.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                } else {
                    llPopup.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                }
                int width  = llPopup.getMeasuredWidth();
                 height = llPopup.getMeasuredHeight();

            }
        });

        etLastName = findViewById(R.id.et_last_name);
        etFirstName = findViewById(R.id.et_first_name);
        etEmail = findViewById(R.id.et_email);
        etMobile = findViewById(R.id.et_mobile);
        etPassword = findViewById(R.id.et_password);
        etPassword.setTransformationMethod(new PasswordTransformationMethod());

        llName = findViewById(R.id.ll_name);
        llEmail = findViewById(R.id.ll_email);
        llMobile = findViewById(R.id.ll_mobile);
        llPassword = findViewById(R.id.ll_password);
        llPasswordStrength = findViewById(R.id.ll_password_strength);

        tvErrorName = findViewById(R.id.tv_name_error);
        tvErrorEmail = findViewById(R.id.tv_email_error);
        tvErrorMobile = findViewById(R.id.tv_mobile_error);
        tvErrorPassword = findViewById(R.id.tv_password_error);

        tvbtnRegister = findViewById(R.id.tvbtn_register);
        tvTandC =findViewById(R.id.tv_tandc_two);
        tvLogin = findViewById(R.id.tvbtn_login);

        tvPasswordStrength = findViewById(R.id.tv_password_strength);

        ibEye = findViewById(R.id.ib_eye);
        ivStrength = findViewById(R.id.iv_strength);

        //add text watchers
        etFirstName.addTextChangedListener(new GenericTextWatcher(etFirstName));
        etLastName.addTextChangedListener(new GenericTextWatcher(etLastName));
        etEmail.addTextChangedListener(new GenericTextWatcher(etEmail));
        etMobile.addTextChangedListener(new GenericTextWatcher(etMobile));
        etPassword.addTextChangedListener(new GenericTextWatcher(etPassword));

        //add focus change listeners
        etFirstName.setOnFocusChangeListener(this);
        etLastName.setOnFocusChangeListener(this);
        etEmail.setOnFocusChangeListener(this);
        etMobile.setOnFocusChangeListener(this);
        etPassword.setOnFocusChangeListener(this);
        tvTandC.setOnClickListener(this);
        tvLogin.setOnClickListener(this);
        llPasswordStrength.setOnClickListener(this);

        ibEye.setOnClickListener(this);
        tvbtnRegister.setOnClickListener(this);

        checkRegisterButtonforEnable();

    }

    boolean passwordvisible = false;
    @Override
    public void onClick(View v) {

        switch (v.getId()){

            case R.id.ib_eye:

                if(passwordvisible) {
                    etPassword.setTransformationMethod(PasswordTransformationMethod.getInstance());
                }else{
                    etPassword.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                }
                passwordvisible = !passwordvisible;
                break;

            case R.id.tvbtn_register:
                break;

            case R.id.tvbtn_login:
                Intent i = new Intent(MainActivity.this, Login.class);
                startActivity(i);
                overridePendingTransition(R.anim.from_middle, R.anim.to_middle);
                finish();
                break;

            case R.id.tv_tandc_two:
                startActivity(new Intent(this, Terms_Conditions.class));
                break;

            case R.id.ll_password_strength:
                showPopupForPasswordStrength();
                break;


        }

    }


    public void checkRegisterButtonforEnable(){
        if(bvalEmail==true&&bvalName==true&&bvalMobile==true&&bvalPassword==true){
            tvbtnRegister.setAlpha(1f);
        }else{
            tvbtnRegister.setAlpha(0.5f);
        }
    }

    public static boolean isValidEmail(CharSequence target) {
        return (!TextUtils.isEmpty(target) && Patterns.EMAIL_ADDRESS.matcher(target).matches());
    }


    public static boolean isValidMobile(CharSequence target) {

        if(target.length()==10){
            return true;
        }else{
            return false;
        }
    }

    public static boolean isValidName(CharSequence target) {

        if(target.length()>0 && target.toString().matches("[a-zA-Z.? ]*")){
            return true;
        }else{
            return false;
        }
    }

    boolean bpassMinChar = false, bpassLowerCase = false, bpassUpperCase = false,bpassDigit = false,bpassSpecialChar = false;

    public int getPasswordStrength(CharSequence target) {
        int password_strength = 0;

        if(target.length()>=8){
            password_strength+=1;
            bpassMinChar = true;
        }else{
            bpassMinChar = false;
        }

        if(target.toString().matches("(.*[a-z].*)")){
            password_strength+=1;
            bpassLowerCase = true;
        }else{
            bpassLowerCase = false;
        }

        if(target.toString().matches("(.*[A-Z].*)")){
            password_strength+=1;
            bpassUpperCase = true;
        }else{
            bpassUpperCase = false;
        }


        if(target.toString().matches("(.*\\d.*)")){
            password_strength+=1;
            bpassDigit = true;
        }else{
            bpassDigit = false;
        }

        //for special character
        Pattern regex = Pattern.compile("[$&+,:;=\\\\?@#|/'<>.^*()%!-]");
        if (regex.matcher(target.toString()).find()) {
           password_strength+=1;
           bpassSpecialChar = true;
        }else{
            bpassSpecialChar = false;
        }

//        //for no commonly used password
//        Pattern p = Pattern.compile("((\\\\w)\\\\2+)+");
//        if (!p.matcher(target.toString()).find())
//        {
//            password_strength+=1;
//        }

    return password_strength;
    }


    @Override
    public void onFocusChange(View v, boolean hasFocus) {

        switch (v.getId()){

            case R.id.et_first_name:



                if (!hasFocus) {
                    String fname = etFirstName.getText().toString();

                    if(isValidName(fname)){
                        tvErrorName.setVisibility(View.GONE);
                        bvalName = true;
                        if(sdk < android.os.Build.VERSION_CODES.JELLY_BEAN) {
                            llName.setBackgroundDrawable(ContextCompat.getDrawable(MainActivity.this, R.drawable.rounded_drawable_background));
                        } else {
                            llName.setBackground(ContextCompat.getDrawable(MainActivity.this, R.drawable.rounded_drawable_background));
                        }

                    }else{
                        tvErrorName.setVisibility(View.VISIBLE);
                        bvalName = false;
                        if(sdk < android.os.Build.VERSION_CODES.JELLY_BEAN) {
                            llName.setBackgroundDrawable(ContextCompat.getDrawable(MainActivity.this, R.drawable.rounded_drawable_background_error));
                        } else {
                            llName.setBackground(ContextCompat.getDrawable(MainActivity.this, R.drawable.rounded_drawable_background_error));
                        }
                    }
                }else{

                }

                break;

            case R.id.et_last_name:

                if (!hasFocus) {

                    String lname = etLastName.getText().toString();

                    if(isValidName(lname)){
                        tvErrorName.setVisibility(View.GONE);
                        bvalName = true;
                        if(sdk < android.os.Build.VERSION_CODES.JELLY_BEAN) {
                            llName.setBackgroundDrawable(ContextCompat.getDrawable(MainActivity.this, R.drawable.rounded_drawable_background));
                        } else {
                            llName.setBackground(ContextCompat.getDrawable(MainActivity.this, R.drawable.rounded_drawable_background));
                        }
                    }else{
                        tvErrorName.setVisibility(View.VISIBLE);
                        bvalName = false;
                        if(sdk < android.os.Build.VERSION_CODES.JELLY_BEAN) {
                            llName.setBackgroundDrawable(ContextCompat.getDrawable(MainActivity.this, R.drawable.rounded_drawable_background_error));
                        } else {
                            llName.setBackground(ContextCompat.getDrawable(MainActivity.this, R.drawable.rounded_drawable_background_error));
                        }
                    }

                }else{

                }

                break;

            case R.id.et_email:

                if (!hasFocus) {
                    String email = etEmail.getText().toString();

                    if(isValidEmail(email)){
                        tvErrorEmail.setVisibility(View.GONE);
                        bvalEmail = true;
                        if(sdk < android.os.Build.VERSION_CODES.JELLY_BEAN) {
                            llEmail.setBackgroundDrawable(ContextCompat.getDrawable(MainActivity.this, R.drawable.rounded_drawable_background));
                        } else {
                            llEmail.setBackground(ContextCompat.getDrawable(MainActivity.this, R.drawable.rounded_drawable_background));
                        }
                    }else{
                        tvErrorEmail.setVisibility(View.VISIBLE);
                        bvalEmail = false;
                        if(sdk < android.os.Build.VERSION_CODES.JELLY_BEAN) {
                            llEmail.setBackgroundDrawable(ContextCompat.getDrawable(MainActivity.this, R.drawable.rounded_drawable_background_error));
                        } else {
                            llEmail.setBackground(ContextCompat.getDrawable(MainActivity.this, R.drawable.rounded_drawable_background_error));
                        }
                    }
                }else{

                }

                break;

            case R.id.et_mobile:

                if (!hasFocus) {
                    String mobile = etMobile.getText().toString();
                    if(isValidMobile(mobile)){
                        tvErrorMobile.setVisibility(View.GONE);
                        bvalMobile = true;
                        if(sdk < android.os.Build.VERSION_CODES.JELLY_BEAN) {
                            llMobile.setBackgroundDrawable(ContextCompat.getDrawable(MainActivity.this, R.drawable.rounded_drawable_background));
                        } else {
                            llMobile.setBackground(ContextCompat.getDrawable(MainActivity.this, R.drawable.rounded_drawable_background));
                        }
                    }else{
                        tvErrorMobile.setVisibility(View.VISIBLE);
                        bvalMobile = false;
                        if(sdk < android.os.Build.VERSION_CODES.JELLY_BEAN) {
                            llMobile.setBackgroundDrawable(ContextCompat.getDrawable(MainActivity.this, R.drawable.rounded_drawable_background_error));
                        } else {
                            llMobile.setBackground(ContextCompat.getDrawable(MainActivity.this, R.drawable.rounded_drawable_background_error));
                        }
                    }
                }else{

                }

                break;

            case R.id.et_password:

                if (!hasFocus) {
                    //Toast.makeText(MainActivity.this, "Focus Lose", Toast.LENGTH_SHORT).show();
                    String password = etPassword.getText().toString();
                    int pass_strength = getPasswordStrength(password);

                    if(pass_strength==STRONG_PASSWORD){
                        tvErrorPassword.setVisibility(View.GONE);
                        bvalPassword = true;
                        if(sdk < android.os.Build.VERSION_CODES.JELLY_BEAN) {
                            llPassword.setBackgroundDrawable(ContextCompat.getDrawable(MainActivity.this, R.drawable.rounded_drawable_background));
                        } else {
                            llPassword.setBackground(ContextCompat.getDrawable(MainActivity.this, R.drawable.rounded_drawable_background));
                        }
                    }else{
                        tvErrorPassword.setVisibility(View.VISIBLE);
                        bvalPassword = false;
                        if(sdk < android.os.Build.VERSION_CODES.JELLY_BEAN) {
                            llPassword.setBackgroundDrawable(ContextCompat.getDrawable(MainActivity.this, R.drawable.rounded_drawable_background_error));
                        } else {
                            llPassword.setBackground(ContextCompat.getDrawable(MainActivity.this, R.drawable.rounded_drawable_background_error));
                        }
                    }
                }else{
                    //Toast.makeText(MainActivity.this, "Get Focus", Toast.LENGTH_SHORT).show();
                }

                break;
        }
    }


    private class GenericTextWatcher implements TextWatcher{

        private View view;
        private GenericTextWatcher(View view) {
            this.view = view;
        }

        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

        public void afterTextChanged(Editable editable) {
            String text = editable.toString();
            switch (view.getId()){

                case R.id.et_first_name:
                        String fname = editable.toString();

                        if(isValidName(fname)){
                            tvErrorName.setVisibility(View.GONE);
                            bvalName = true;
                            if(sdk < android.os.Build.VERSION_CODES.JELLY_BEAN) {
                                llName.setBackgroundDrawable(ContextCompat.getDrawable(MainActivity.this, R.drawable.rounded_drawable_background));
                            } else {
                                llName.setBackground(ContextCompat.getDrawable(MainActivity.this, R.drawable.rounded_drawable_background));
                            }
                        }else{
                            //tvErrorName.setVisibility(View.VISIBLE);
                            bvalName = false;
                        }

                    break;

                case R.id.et_last_name:
                        String lname = editable.toString();

                        if(isValidName(lname)){
                            tvErrorName.setVisibility(View.GONE);
                            bvalName = true;
                            if(sdk < android.os.Build.VERSION_CODES.JELLY_BEAN) {
                                llName.setBackgroundDrawable(ContextCompat.getDrawable(MainActivity.this, R.drawable.rounded_drawable_background));
                            } else {
                                llName.setBackground(ContextCompat.getDrawable(MainActivity.this, R.drawable.rounded_drawable_background));
                            }
                        }else{
                            //tvErrorName.setVisibility(View.VISIBLE);
                            bvalName = false;
                        }

                    break;

                case R.id.et_email:

                        String email = editable.toString();

                        if(isValidEmail(email)){
                            tvErrorEmail.setVisibility(View.GONE);
                            bvalEmail = true;
                            if(sdk < android.os.Build.VERSION_CODES.JELLY_BEAN) {
                                llEmail.setBackgroundDrawable(ContextCompat.getDrawable(MainActivity.this, R.drawable.rounded_drawable_background));
                            } else {
                                llEmail.setBackground(ContextCompat.getDrawable(MainActivity.this, R.drawable.rounded_drawable_background));
                            }

                        }else{
                            //tvErrorEmail.setVisibility(View.VISIBLE);
                            bvalEmail = false;
                        }
                    break;

                case R.id.et_mobile:

                        String mobile = editable.toString();
                        if(isValidMobile(mobile)){
                            tvErrorMobile.setVisibility(View.GONE);
                            bvalMobile = true;
                            if(sdk < android.os.Build.VERSION_CODES.JELLY_BEAN) {
                                llMobile.setBackgroundDrawable(ContextCompat.getDrawable(MainActivity.this, R.drawable.rounded_drawable_background));
                            } else {
                                llMobile.setBackground(ContextCompat.getDrawable(MainActivity.this, R.drawable.rounded_drawable_background));
                            }
                        }else{
                            //tvErrorMobile.setVisibility(View.VISIBLE);
                            bvalMobile = false;
                        }
                    break;

                case R.id.et_password:

                        String password = editable.toString();
                        int pass_strength = getPasswordStrength(password);
                        if(pass_strength==STRONG_PASSWORD){
                            tvErrorPassword.setVisibility(View.GONE);
                            bvalPassword = true;
                            if(sdk < android.os.Build.VERSION_CODES.JELLY_BEAN) {
                                llPassword.setBackgroundDrawable(ContextCompat.getDrawable(MainActivity.this, R.drawable.rounded_drawable_background));
                            } else {
                                llPassword.setBackground(ContextCompat.getDrawable(MainActivity.this, R.drawable.rounded_drawable_background));
                            }
                        }else{
                            //tvErrorPassword.setVisibility(View.VISIBLE);
                            bvalPassword = false;
                        }

                   if(pass_strength==0 ||password.length()==0){
                        ivStrength.setImageResource(R.mipmap.group_blank);
                        tvPasswordStrength.setText("STRENGTH");
                    }else if(pass_strength==1){
                        ivStrength.setImageResource(R.mipmap.group_weak);
                        tvPasswordStrength.setText("WEEK");
                    } else if(pass_strength==2){
                        ivStrength.setImageResource(R.mipmap.group_weak);
                        tvPasswordStrength.setText("WEEK");
                    } else if(pass_strength==3){
                        ivStrength.setImageResource(R.mipmap.group_good);
                        tvPasswordStrength.setText("GOOD");
                    } else if(pass_strength==4){
                        ivStrength.setImageResource(R.mipmap.group_good);
                        tvPasswordStrength.setText("GOOD");
                    } else if(pass_strength==5){
                        ivStrength.setImageResource(R.mipmap.group_strong);
                        tvPasswordStrength.setText("STRONG");
                    } /*else if(pass_strength==6){
                       ivStrength.setImageResource(R.mipmap.group_strong);
                       tvPasswordStrength.setText("STRONG");
                    }*/
                    break;
            }
            checkRegisterButtonforEnable();
        }
    }

    private PopupWindow mPopupWindow;


    public void showPopupForPasswordStrength(){
        LayoutInflater inflater = (LayoutInflater) this.getSystemService(LAYOUT_INFLATER_SERVICE);

        //Inflate the custom layout/view
        View customView = inflater.inflate(R.layout.popup_layout,null);
        ImageView ivMinChar, ivUC, ivLC, ivNum, ivSpecialCharacter;

        ivMinChar = customView.findViewById(R.id.iv_minchar);
        ivUC = customView.findViewById(R.id.iv_uppercase);
        ivLC = customView.findViewById(R.id.iv_lowercase);
        ivNum = customView.findViewById(R.id.iv_number);
        ivSpecialCharacter = customView.findViewById(R.id.iv_special_char);



        if(bpassMinChar){
            ivMinChar.setImageResource(R.mipmap.tick);
        }else{
            ivMinChar.setImageResource(R.mipmap.untick);
        }

        if(bpassLowerCase){
            ivLC.setImageResource(R.mipmap.tick);
        }else{
            ivLC.setImageResource(R.mipmap.untick);
        }

        if(bpassUpperCase){
            ivUC.setImageResource(R.mipmap.tick);
        }else{
            ivUC.setImageResource(R.mipmap.untick);
        }

        if(bpassDigit){
            ivNum.setImageResource(R.mipmap.tick);
        }else{
            ivNum.setImageResource(R.mipmap.untick);
        }

        if(bpassSpecialChar){
            ivSpecialCharacter.setImageResource(R.mipmap.tick);
        }else{
            ivSpecialCharacter.setImageResource(R.mipmap.untick);
        }

        mPopupWindow = new PopupWindow(
                customView,
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        );

        //Closes the popup window when touch outside. This method was written informatively in Google's docs.
        mPopupWindow.setOutsideTouchable(true);

        //Set focus true to prevent a touch event to go to a below view (main layout), which works like a dialog with 'cancel' property => Try it! And you will know what I mean.
        mPopupWindow.setFocusable(true);

        //Removes default background.
        mPopupWindow.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        // Set an elevation value for popup window
        // Call requires API level 21
        if(Build.VERSION.SDK_INT>=21){
            mPopupWindow.setElevation(5.0f);
        }

        //mPopupWindow.showAtLocation(tvPasswordStrength, Gravity.NO_GRAVITY,r.top,r.left);
        mPopupWindow.showAsDropDown(tvPasswordStrength, 0, -(llPasswordStrength.getHeight() + height));
        //mPopupWindow.showAsDropDown(tvPasswordStrength);
        //mPopupWindow.showAtLocation(tvPasswordStrength, Gravity.LEFT, 60, 2000);
    }

}
