package com.ph30891.asm_ph30891_gd2.view;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.ph30891.asm_ph30891_gd2.R;
import com.ph30891.asm_ph30891_gd2.databinding.ActivitySignUpBinding;
import com.ph30891.asm_ph30891_gd2.model.Response;
import com.ph30891.asm_ph30891_gd2.model.User;
import com.ph30891.asm_ph30891_gd2.networking.HttpRequest;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;

public class SignUpActivity extends AppCompatActivity {
    private ActivitySignUpBinding binding;
    private HttpRequest httpRequest;
    private File file;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySignUpBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        httpRequest = new HttpRequest();
        init();
    }

    private void init() {
        binding.btnBack.setOnClickListener(v -> onBackPressed());
        binding.imvAvatar.setOnClickListener(v -> {
            openChooseImage();
        });
        binding.btnSignUp.setOnClickListener(v -> {
            String username = String.valueOf(binding.edUsername.getText());
            String pass = String.valueOf(binding.edPass.getText());
            String email = String.valueOf(binding.edEmail.getText());
            String name = String.valueOf(binding.edName.getText());
            int role = 1;
            if (validate(username, pass, email, name)) {
                handleSignUp(username, pass, email, name, role);
            }
        });
    }

    private void handleSignUp(String username, String pass, String email, String name, int role) {
        loading(true);
        RequestBody _username = RequestBody.create(MediaType.parse("multipart/form-data"), username);
        RequestBody _password = RequestBody.create(MediaType.parse("multipart/form-data"), pass);
        RequestBody _email = RequestBody.create(MediaType.parse("multipart/form-data"), email);
        RequestBody _name = RequestBody.create(MediaType.parse("multipart/form-data"), name);
        RequestBody _role = RequestBody.create(MediaType.parse("multipart/form-data"), String.valueOf(role));
        MultipartBody.Part mPart;
        if (file != null) {
            RequestBody responseFile = RequestBody.create(MediaType.parse("image/*"), file);
            mPart = MultipartBody.Part.createFormData("avatar", file.getName(), responseFile);
        } else {
            mPart = null;
        }
        httpRequest.calAPI().register(_username, _password, _email, _name, _role, mPart).enqueue(responseRegister);
    }

    Callback<Response<User>> responseRegister = new Callback<Response<User>>() {
        @Override
        public void onResponse(Call<Response<User>> call, retrofit2.Response<Response<User>> response) {
            if(response.isSuccessful()){
                if(response.body().getStatus() == 200){
                    loading(false);
                    Toast.makeText(SignUpActivity.this, "Register successfully", Toast.LENGTH_SHORT).show();
                    finish();
                }
            }
        }

        @Override
        public void onFailure(Call<Response<User>> call, Throwable t) {
            Log.e("Error register", "onFailure: "+t.getMessage());
        }
    };

    private File createFileFromUri(Uri path, String name) {
        File _file = new File(SignUpActivity.this.getFilesDir(), name + ".jpg");
        try {
            InputStream is = SignUpActivity.this.getContentResolver().openInputStream(path);
            OutputStream ous = new FileOutputStream(_file);
            byte[] buf = new byte[1024];
            int len;
            while ((len = is.read(buf)) > 0) {
                ous.write(buf, 0, len);
            }
            ous.close();
            is.close();
            return _file;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    private void openChooseImage() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
            Intent intent = new Intent();
            intent.setType("image/*");
            intent.setAction(Intent.ACTION_GET_CONTENT);
            getImage.launch(intent);
        }else {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 1);
        }
    }

    ActivityResultLauncher getImage = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
        @Override
        public void onActivityResult(ActivityResult o) {
            if (o.getResultCode() == Activity.RESULT_OK) {
                Intent data = o.getData();
                Uri imagePath = data.getData();
                file = createFileFromUri(imagePath, "avatar");
                Glide.with(SignUpActivity.this).load(file)
                        .centerCrop()
                        .circleCrop()
                        .diskCacheStrategy(DiskCacheStrategy.NONE)
                        .error(R.drawable.image)
                        .into(binding.imvAvatar);
            }
        }
    });

    private boolean validate(String username, String pass, String email, String name) {
        if (TextUtils.isEmpty(username)) {
            binding.tilUsername.setError("Please enter a username");
            return false;
        } else {
            binding.tilUsername.setError(null);
        }
        if (TextUtils.isEmpty(pass) || pass.length() < 6) {
            binding.tilPass.setError("Password must be at least 6 characters long");
            return false;
        } else {
            binding.tilPass.setError(null);
        }
        if (TextUtils.isEmpty(email) || !Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            binding.tilEmail.setError("Email is not a valid email address");
            return false;
        } else {
            binding.tilEmail.setError(null);
        }
        if (TextUtils.isEmpty(name)) {
            binding.tilName.setError("Enter your name");
            return false;
        } else {
            binding.tilName.setError(null);
        }
        return true;
    }

    private void loading(boolean isLoading) {
        if (isLoading) {
            binding.progressBar.setVisibility(View.VISIBLE);
            binding.btnSignUp.setEnabled(false);
        } else {
            binding.progressBar.setVisibility(View.INVISIBLE);
            binding.btnSignUp.setEnabled(true);
        }
    }
}