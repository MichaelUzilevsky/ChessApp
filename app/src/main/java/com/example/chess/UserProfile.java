package com.example.chess;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class UserProfile extends AppCompatActivity {
    private TextInputLayout username, password, fullName, email, phone;
    private TextView main_Username, main_name;
    private String _USERNAME, _NAME, _EMAIL, _PHONE, _PASSWORD;
    private DatabaseReference reference;
    Boolean taken = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);

        reference = FirebaseDatabase.getInstance().getReference("Users");

        username = findViewById(R.id.username);
        password = findViewById(R.id.password);
        fullName = findViewById(R.id.fullname);
        email = findViewById(R.id.email);
        phone = findViewById(R.id.phone);

        main_Username = findViewById(R.id.username_label);
        main_name = findViewById(R.id.fullname_label);

        showUserData();

        fullName.getEditText().addTextChangedListener(new ValidationTextWatcher(fullName));
        username.getEditText().addTextChangedListener(new ValidationTextWatcher(username));
        email.getEditText().addTextChangedListener(new ValidationTextWatcher(email));
        phone.getEditText().addTextChangedListener(new ValidationTextWatcher(phone));
        password.getEditText().addTextChangedListener(new ValidationTextWatcher(password));
    }

    private void showUserData() {
        Intent intent = getIntent();
        _USERNAME = intent.getStringExtra("username");
        _PASSWORD = intent.getStringExtra("password");
        _EMAIL = intent.getStringExtra("email");
        _PHONE = intent.getStringExtra("phone");
        _NAME = intent.getStringExtra("name");

        fullName.getEditText().setText(_NAME);
        username.getEditText().setText(_USERNAME);
        email.getEditText().setText(_EMAIL);
        phone.getEditText().setText(_PHONE);
        password.getEditText().setText(_PASSWORD);

        main_name.setText(_NAME);
        main_Username.setText(_USERNAME);
    }

    public void update(View view) {
        if(!change_username() | !change_email() | !change_name() | !change_password() | !change_phone()){
            Toast.makeText(this, "Invalid Data", Toast.LENGTH_SHORT).show();
        }
        else {

        }
    }

    public Boolean checkIfExsist(){

        if(_USERNAME.equals(username.getEditText().getText().toString()))
            return false;

        Query checkUser = reference.orderByChild("username").equalTo(username.getEditText().getText().toString().trim());
        checkUser.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(!snapshot.exists()){
                    taken = true;
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        return  taken;
    }

    private  Boolean change_username(){
        if(_USERNAME.equals(username.getEditText().getText().toString()))
            return true;

        if(validateUsername()) {

            reference.child(_USERNAME).child("username").setValue(username.getEditText().getText().toString());
            _USERNAME = username.getEditText().getText().toString();
            main_Username.setText(_USERNAME);
            return true;
        }

        return false;
    }

    private  Boolean change_email(){
        if(_EMAIL.equals(email.getEditText().getText().toString()))
            return true;

        if(validateEmail()) {
            reference.child(_USERNAME).child("password").setValue(password.getEditText().getText().toString());
            _EMAIL = email.getEditText().getText().toString();
            return true;
        }
        return false;
    }

    private  Boolean change_phone(){
        if(_PHONE.equals(phone.getEditText().getText().toString()))
            return true;

        if(validatePhone()) {
            reference.child(_USERNAME).child("phone").setValue(phone.getEditText().getText().toString());
            _PHONE = phone.getEditText().getText().toString();
            return true;
        }
        return false;
    }

    private  Boolean change_name(){
        if(_NAME.equals(fullName.getEditText().getText().toString()))
            return true;

        if(validateName()) {
            reference.child(_USERNAME).child("name").setValue(fullName.getEditText().getText().toString());
            _NAME = fullName.getEditText().getText().toString();
            main_name.setText(_NAME);
            return true;
        }
        return false;
    }

    private  Boolean change_password(){
        if(_PASSWORD.equals(password.getEditText().getText().toString()))
            return true;

        if(validatePassword()) {
            reference.child(_USERNAME).child("password").setValue(password.getEditText().getText().toString());
            _PASSWORD = password.getEditText().getText().toString();
            return true;
        }
        return false;
    }

    private Boolean validateName(){
        String val = fullName.getEditText().getText().toString().trim();
        if(val.isEmpty()) {
            fullName.setError("Filed cannot be empty");
            return false;
        }
        else {
            fullName.setError(null);
            fullName.setErrorEnabled(false);
            return  true;
        }
    }

    private Boolean validateUsername(){
        String val = username.getEditText().getText().toString().trim();
        String noSpace = "\\A\\w{6,20}\\z";
        if(val.isEmpty()) {
            username.setError("Filed cannot be empty");
            return false;
        }
        else if(val.length() < 6 ){
            username.setError("Username is too short, minimum 6 characters");
            return false;
        }
        else if(val.length() > 12){
            username.setError("Username is too long, max 12 characters");
            return false;
        }
        else if(!val.matches(noSpace)){
            username.setError("Spaces are not allowed");
            return false;
        }
        else if(checkIfExsist()){
            username.setError("username is taken");
            return false;
        }
        else {
            username.setError(null);
            username.setErrorEnabled(false);
            return  true;
        }
    }

    private Boolean validateEmail(){
        String emailStyle = "^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$";
        String val = email.getEditText().getText().toString().trim();
        if(val.isEmpty()) {
            email.setError("Filed cannot be empty");
            return false;
        }
        else if(!val.matches(emailStyle)){
            email.setError("Invalid email address");
            return false;
        }
        else {
            email.setError(null);
            email.setErrorEnabled(false);
            return  true;
        }
    }

    private Boolean validatePhone(){
        String val = phone.getEditText().getText().toString().trim();
        if(val.isEmpty()) {
            phone.setError("Filed cannot be empty");
            return false;
        }
        else {
            phone.setError(null);
            phone.setErrorEnabled(false);
            return  true;
        }
    }

    private Boolean validatePassword(){
        String val = password.getEditText().getText().toString().trim();
        String passwordStyle = "^(?=.*\\d)(?=.*[a-z])(?=.*[A-Z])(?=.*[a-zA-Z]).{8,12}$";
        if(val.isEmpty()) {
            password.setError("Filed cannot be empty");
            return false;
        }
        else if(val.length() < 8 ){
            password.setError("Password is too short, minimum 8 characters");
            return false;
        }
        else if(val.length() > 16){
            password.setError("Password is too long, max 16 characters");
            return false;
        }
        else if(!val.matches(passwordStyle)){
            password.setError("Password must include special characters, digits, upper and lower case letters");
            return false;
        }
        else {
            password.setError(null);
            password.setErrorEnabled(false);
            return  true;
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
                case R.id.fullname:
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