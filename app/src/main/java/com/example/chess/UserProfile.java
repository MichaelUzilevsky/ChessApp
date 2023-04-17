package com.example.chess;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Base64;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class UserProfile extends AppCompatActivity {
    private TextInputLayout username, password, fullName, email, phone;
    private TextView username_label, name_label;
    //current user data
    private String _USERNAME, _NAME, _EMAIL, _PHONE, _PASSWORD, _IMAGE;
    private int _SCORE;
    private DatabaseReference reference;
    private Boolean taken = false;
    private SharedPreferences pref;
    private ImageView img;
    private int REQUEST_CAMERA = 1, SELECT_FILE = 0;
    private Bitmap bitmap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        reference = FirebaseDatabase.getInstance().getReference().child("Users");

        username = findViewById(R.id.username);
        password = findViewById(R.id.password);
        fullName = findViewById(R.id.fullname);
        email = findViewById(R.id.email);
        phone = findViewById(R.id.phone);
        img = findViewById(R.id.profile_image);

        img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectImage();
            }
        });

        username_label = findViewById(R.id.username_label);
        name_label = findViewById(R.id.fullname_label);

        pref = getSharedPreferences("User_Data", 0);

        showUserData();

        //on change listener
        fullName.getEditText().addTextChangedListener(new ValidationTextWatcher(fullName));
        username.getEditText().addTextChangedListener(new ValidationTextWatcher(username));
        email.getEditText().addTextChangedListener(new ValidationTextWatcher(email));
        phone.getEditText().addTextChangedListener(new ValidationTextWatcher(phone));
        password.getEditText().addTextChangedListener(new ValidationTextWatcher(password));
    }

    private void selectImage() {

        final CharSequence[] items = {"Camera", "Gallery"};

        AlertDialog.Builder builder = new AlertDialog.Builder(UserProfile.this);
        builder.setTitle("Add Image");

        builder.setItems(items, new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if (items[i].equals("Camera")) {

                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    startActivityForResult(intent, REQUEST_CAMERA);

                } else if (items[i].equals("Gallery")) {

                    Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    intent.setType("image/*");
                    startActivityForResult(intent, SELECT_FILE);

                }
            }
        });
        builder.show();

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == Activity.RESULT_OK) {

            if (requestCode == REQUEST_CAMERA) {
                bitmap = (Bitmap) data.getExtras().get("data");
                img.setImageBitmap(bitmap);

            } else if (requestCode == SELECT_FILE) {

                Uri selectedImageUri = data.getData();
                try {
                    bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), selectedImageUri);
                    img.setImageBitmap(bitmap);
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }

        }
    }

    public String convertBitmapToString(Bitmap bm) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bm.compress(Bitmap.CompressFormat.PNG, 100, baos); //bm is the bitmap object
        byte[] b = baos.toByteArray();
        return Base64.encodeToString(b, Base64.DEFAULT);
    }

    public Bitmap convertStringToBitmap(String encoded) {
        if (encoded == null)
            return null;
        byte[] imageAsBytes = Base64.decode(encoded.getBytes(), Base64.DEFAULT);
        return BitmapFactory.decodeByteArray(imageAsBytes, 0, imageAsBytes.length);
    }

    private void showUserData() {
        _USERNAME = pref.getString("username", null);
        _PASSWORD = pref.getString("password", null);
        _EMAIL = pref.getString("email", null);
        _PHONE = pref.getString("phone", null);
        _NAME = pref.getString("name", null);
        _SCORE = pref.getInt("score", 0);
        _IMAGE = pref.getString("image", "");

        fullName.getEditText().setText(_NAME);
        username.getEditText().setText(_USERNAME);
        email.getEditText().setText(_EMAIL);
        phone.getEditText().setText(_PHONE);
        password.getEditText().setText(_PASSWORD);

        name_label.setText(_NAME);
        username_label.setText(_USERNAME);

        if (!_IMAGE.equals(""))
            img.setImageBitmap(convertStringToBitmap(_IMAGE));
        else
            img.setImageResource(R.drawable.guest_icon);
    }

    public void update(View view) {
        if (!change_email() | !change_name() | !change_password() | !change_phone() | !change_image()) {
            Toast.makeText(this, "Invalid Data", Toast.LENGTH_SHORT).show();
        } else {
            checkIfExsistAndChange();
            Toast.makeText(UserProfile.this, "Data Saved Successfully", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(UserProfile.this, GameOptions.class));
            finish();
        }
    }

    protected Boolean checkIfExsistAndChange() {

        if (_USERNAME.equals(username.getEditText().getText().toString()))
            return false;

        Query checkUser = reference.orderByChild("username").equalTo(username.getEditText().getText().toString().trim());
        checkUser.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (!snapshot.exists()) {
                    username.setError(null);
                    username.setErrorEnabled(false);
                    change_username();
                } else {
                    username.setError("Username is taken");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        return taken;
    }

    private Boolean change_username() {
        if (_USERNAME.equals(username.getEditText().getText().toString()))
            return true;

        else if (validateUsername()) {
            User user = new User(_NAME, username.getEditText().getText().toString(), _EMAIL, _PHONE, _PASSWORD, convertBitmapToString(bitmap), _SCORE);
            reference.child(username.getEditText().getText().toString()).setValue(user);

            //remove
            Query remove = FirebaseDatabase.getInstance().getReference().child("Users").orderByChild("username").equalTo(_USERNAME);
            remove.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    for (DataSnapshot data : snapshot.getChildren()) {
                        data.getRef().removeValue();
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });

            _USERNAME = username.getEditText().getText().toString();
            _IMAGE = convertBitmapToString(bitmap);
            username_label.setText(_USERNAME);

            SharedPreferences.Editor editor = pref.edit();
            editor.putString("username", _USERNAME);
            editor.putString("image", _IMAGE);
            editor.apply();

            return true;
        }

        return false;
    }

    private Boolean change_email() {
        if (_EMAIL.equals(email.getEditText().getText().toString()))
            return true;

        if (validateEmail()) {
            reference.child(_USERNAME).child("email").setValue(email.getEditText().getText().toString());
            _EMAIL = email.getEditText().getText().toString();

            SharedPreferences.Editor editor = pref.edit();
            editor.putString("email", _EMAIL);
            editor.apply();

            return true;
        }
        return false;
    }

    private Boolean change_image() {
        if(bitmap!= null) {
            _IMAGE = convertBitmapToString(bitmap);
            reference.child(_USERNAME).child("image").setValue(_IMAGE);
            SharedPreferences.Editor editor = pref.edit();
            editor.putString("image", _IMAGE);
            editor.apply();
        }
bitmap=null;
        return true;

    }

    private Boolean change_phone() {
        if (_PHONE.equals(phone.getEditText().getText().toString()))
            return true;

        if (validatePhone()) {
            reference.child(_USERNAME).child("phone").setValue(phone.getEditText().getText().toString());
            _PHONE = phone.getEditText().getText().toString();

            SharedPreferences.Editor editor = pref.edit();
            editor.putString("phone", _PHONE);
            editor.apply();

            return true;
        }
        return false;
    }

    private Boolean change_name() {
        if (_NAME.equals(fullName.getEditText().getText().toString()))
            return true;

        if (validateName()) {
            reference.child(_USERNAME).child("name").setValue(fullName.getEditText().getText().toString());
            _NAME = fullName.getEditText().getText().toString();
            name_label.setText(_NAME);

            SharedPreferences.Editor editor = pref.edit();
            editor.putString("name", _NAME);
            editor.apply();

            return true;
        }
        return false;
    }

    private Boolean change_password() {
        if (_PASSWORD.equals(password.getEditText().getText().toString()))
            return true;

        if (validatePassword()) {
            reference.child(_USERNAME).child("password").setValue(password.getEditText().getText().toString());
            _PASSWORD = password.getEditText().getText().toString();

            SharedPreferences.Editor editor = pref.edit();
            editor.putString("password", _PASSWORD);
            editor.apply();

            return true;
        }
        return false;
    }

    private Boolean validateName() {
        String val = fullName.getEditText().getText().toString().trim();
        if (val.isEmpty()) {
            fullName.setError("Filed cannot be empty");
            return false;
        } else {
            fullName.setError(null);
            fullName.setErrorEnabled(false);
            return true;
        }
    }

    private Boolean validateUsername() {
        String val = username.getEditText().getText().toString().trim();
        String noSpace = "\\A\\w{6,20}\\z";
        if (val.isEmpty()) {
            username.setError("Filed cannot be empty");
            return false;
        } else if (val.length() < 6) {
            username.setError("Username is too short, minimum 6 characters");
            return false;
        } else if (val.length() > 12) {
            username.setError("Username is too long, max 12 characters");
            return false;
        } else if (!val.matches(noSpace)) {
            username.setError("Spaces are not allowed");
            return false;
        } else {
            username.setError(null);
            username.setErrorEnabled(false);
            return true;
        }
    }

    private Boolean validateEmail() {
        String emailStyle = "^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$";
        String val = email.getEditText().getText().toString().trim();
        if (val.isEmpty()) {
            email.setError("Filed cannot be empty");
            return false;
        } else if (!val.matches(emailStyle)) {
            email.setError("Invalid email address");
            return false;
        } else {
            email.setError(null);
            email.setErrorEnabled(false);
            return true;
        }
    }

    private Boolean validatePhone() {
        String val = phone.getEditText().getText().toString().trim();
        if (val.isEmpty()) {
            phone.setError("Filed cannot be empty");
            return false;
        } else {
            phone.setError(null);
            phone.setErrorEnabled(false);
            return true;
        }
    }

    private Boolean validatePassword() {
        String val = password.getEditText().getText().toString().trim();
        String passwordStyle = "^(?=.*\\d)(?=.*[a-z])(?=.*[A-Z])(?=.*[a-zA-Z]).{8,12}$";
        if (val.isEmpty()) {
            password.setError("Filed cannot be empty");
            return false;
        } else if (val.length() < 8) {
            password.setError("Password is too short, minimum 8 characters");
            return false;
        } else if (val.length() > 16) {
            password.setError("Password is too long, max 16 characters");
            return false;
        } else if (!val.matches(passwordStyle)) {
            password.setError("Password must include special characters, digits, upper and lower case letters");
            return false;
        } else {
            password.setError(null);
            password.setErrorEnabled(false);
            return true;
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