package com.example.chess;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Pair;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class SignUp extends AppCompatActivity {
    private Button signup, toSignUp;
    private TextView main, slogan;
    private TextInputLayout regUsername, regPassword, regFullName, regEmail, regPhone;
    private FirebaseDatabase root;
    private DatabaseReference users_reference, username_reference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        main = findViewById(R.id.logo_name);
        slogan = findViewById(R.id.slogan_name);
        regUsername = findViewById(R.id.username);
        regPassword = findViewById(R.id.password);
        regFullName = findViewById(R.id.fullname_label);
        regEmail = findViewById(R.id.email);
        regPhone = findViewById(R.id.phone);
        signup = findViewById(R.id.go);
        toSignUp = findViewById(R.id.toOther);

        root = FirebaseDatabase.getInstance();
        users_reference = root.getReference().child("Users");
        username_reference = root.getReference().child("Users");

        regFullName.getEditText().addTextChangedListener(new ValidationTextWatcher(regFullName));
        regUsername.getEditText().addTextChangedListener(new ValidationTextWatcher(regUsername));
        regEmail.getEditText().addTextChangedListener(new ValidationTextWatcher(regEmail));
        regPhone.getEditText().addTextChangedListener(new ValidationTextWatcher(regPhone));
        regPassword.getEditText().addTextChangedListener(new ValidationTextWatcher(regPassword));

    }


    private Boolean validateName(){
        String val = regFullName.getEditText().getText().toString().trim();
        if(val.isEmpty()) {
            regFullName.setError("Filed cannot be empty");
            return false;
        }
        else {
            regFullName.setError(null);
            regFullName.setErrorEnabled(false);
            return  true;
        }
    }

    private Boolean validateUsername(){
        String val = regUsername.getEditText().getText().toString().trim();
        String noSpace = "\\A\\w{6,20}\\z";


        if(val.isEmpty()) {
            regUsername.setError("Filed cannot be empty");
            return false;
        }
        else if(val.length() < 6 ){
            regUsername.setError("Username is too short, minimum 6 characters");
            return false;
        }
        else if(val.length() > 12){
            regUsername.setError("Username is too long, max 12 characters");
            return false;
        }
        else if(!val.matches(noSpace)){
            regUsername.setError("Spaces are not allowed");
            return false;
        }
        else {
            regUsername.setError(null);
            regUsername.setErrorEnabled(false);
            return true;
        }
    }

    private Boolean validateEmail(){
        String emailStyle = "^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$";
        String val = regEmail.getEditText().getText().toString().trim();
        if(val.isEmpty()) {
            regEmail.setError("Filed cannot be empty");
            return false;
        }
        else if(!val.matches(emailStyle)){
            regEmail.setError("Invalid email address");
            return false;
        }
        else {
            regEmail.setError(null);
            regEmail.setErrorEnabled(false);
            return  true;
        }
    }

    private Boolean validatePhone(){
        String val = regPhone.getEditText().getText().toString().trim();
        if(val.isEmpty()) {
            regPhone.setError("Filed cannot be empty");
            return false;
        }
        else {
            regPhone.setError(null);
            regPhone.setErrorEnabled(false);
            return  true;
        }
    }

    private Boolean validatePassword(){
        String val = regPassword.getEditText().getText().toString().trim();
        String passwordStyle = "^(?=.*\\d)(?=.*[a-z])(?=.*[A-Z])(?=.*[a-zA-Z]).{8,12}$";
        if(val.isEmpty()) {
            regPassword.setError("Filed cannot be empty");
            return false;
        }
        else if(val.length() < 8 ){
            regPassword.setError("Password is too short, minimum 8 characters");
            return false;
        }
        else if(val.length() > 16){
            regPassword.setError("Password is too long, max 16 characters");
            return false;
        }
        else if(!val.matches(passwordStyle)){
            regPassword.setError("Password must include special characters, digits, upper and lower case letters");
            return false;
        }
        else {
            regPassword.setError(null);
            regPassword.setErrorEnabled(false);
            return  true;
        }
    }

    public void toLogin(View view) {

        Pair[] pairs = new Pair[6];
        pairs[0] = new Pair<View, String>(main, "logo_txt");
        pairs[1] = new Pair<View, String>(slogan, "slogan");
        pairs[2] = new Pair<View, String>(regUsername, "username");
        pairs[3] = new Pair<View, String>(regPassword, "password");
        pairs[4] = new Pair<View, String>(signup, "go_btn");
        pairs[5] = new Pair<View, String>(toSignUp, "toOther");


        ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(SignUp.this, pairs);
        startActivity(new Intent(SignUp.this, Login.class), options.toBundle());
    }

    public void saveData(View view) {

        if(!validateName() | !validateUsername() | !validateEmail() | !validatePhone() | !validatePassword()) {
        }
        else {

            Query checkUser = username_reference.orderByChild("username").equalTo(regUsername.getEditText().getText().toString().trim());
            checkUser.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if(!snapshot.exists()){
                        regUsername.setError(null);
                        regUsername.setErrorEnabled(false);
                        String name = regFullName.getEditText().getText().toString().trim();
                        String username = regUsername.getEditText().getText().toString().trim();
                        String email = regEmail.getEditText().getText().toString().trim();
                        String phone = regPhone.getEditText().getText().toString().trim();
                        String password = regPassword.getEditText().getText().toString().trim();

                        User user = new User(name, username, email, phone, password);


                        users_reference.child(regUsername.getEditText().getText().toString()).setValue(user);

                        Intent intent = new Intent(SignUp.this, Login.class);

                        Pair[] pairs = new Pair[6];
                        pairs[0] = new Pair<View, String>(main, "logo_txt");
                        pairs[1] = new Pair<View, String>(slogan, "slogan");
                        pairs[2] = new Pair<View, String>(regUsername, "username");
                        pairs[3] = new Pair<View, String>(regPassword, "password");
                        pairs[4] = new Pair<View, String>(signup, "go_btn");
                        pairs[5] = new Pair<View, String>(toSignUp, "toOther");

                        ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(SignUp.this, pairs);

                        intent.putExtra("username", regUsername.getEditText().getText().toString().trim());
                        intent.putExtra("password", regPassword.getEditText().getText().toString().trim());

                        startActivity(intent, options.toBundle());;
                    }
                    else {
                        regUsername.setError("Username is taken");
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });

        }
    }

    private class ValidationTextWatcher implements TextWatcher {
        private View view;
        private ValidationTextWatcher(View view) {
            this.view = view;
        }
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        }
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        }
        public void afterTextChanged(Editable editable) {
            switch (view.getId()) {
                case R.id.fullname_label:
                    validateName();
                    break;
                case R.id.username:
                    validateUsername();
                    break;
                case R.id.email:
                    validateEmail();
                    break;
                case R.id.phone:
                    validatePhone();
                    break;
                case R.id.password:
                    validatePassword();
                    break;
            }
        }
    }

}