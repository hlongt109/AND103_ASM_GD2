package com.ph30891.asm_ph30891_gd2.view;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.ph30891.asm_ph30891_gd2.R;
import com.ph30891.asm_ph30891_gd2.databinding.ActivityLoginBinding;
import com.ph30891.asm_ph30891_gd2.model.Response;
import com.ph30891.asm_ph30891_gd2.model.User;
import com.ph30891.asm_ph30891_gd2.networking.HttpRequest;

import retrofit2.Call;
import retrofit2.Callback;

public class LoginActivity extends AppCompatActivity {
    private ActivityLoginBinding binding;
    private HttpRequest httpRequest;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        httpRequest = new HttpRequest();
        init();
    }
    private void init() {
        binding.btnSignUp.setOnClickListener(v -> {
            startActivity(new Intent(this, SignUpActivity.class));
        });

        binding.btnLogin.setOnClickListener(v -> {
            String username = String.valueOf(binding.edUsername.getText());
            String password = String.valueOf(binding.edPass.getText());
            if(validate(username,password)){
                handleLogin(username,password);
            }
        });
    }
    private void handleLogin(String username, String password){
        loading(true);
        User user = new User();
        user.setUsername(username);
        user.setPassword(password);
        httpRequest.calAPI().login(user).enqueue(responseLogin);
    }
    Callback<Response<User>> responseLogin = new Callback<Response<User>>() {
        @Override
        public void onResponse(Call<Response<User>> call, retrofit2.Response<Response<User>> response) {
            if(response.isSuccessful()){
                if (response.body().getStatus() == 200){
                    int role = response.body().getData().getRole();
                    if (role == 0){
                        saveUserInfor(response.body().getToken(),response.body().getRefreshToken(),response.body().getData().get_id(),response.body().getData().getName());
                        loading(false);
                        Toast.makeText(LoginActivity.this, "Welcom "+response.body().getData().getName(), Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(LoginActivity.this, MainAdminActivity.class));
                        finish();
                    }
                    if (role == 1){
                        saveUserInfor(response.body().getToken(),response.body().getRefreshToken(),response.body().getData().get_id(),response.body().getData().getName());
                        loading(false);
                        Toast.makeText(LoginActivity.this, "Welcom "+response.body().getData().getName(), Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(LoginActivity.this, MainUserActivity.class));
                        finish();
                    }
                }
            }
        }

        @Override
        public void onFailure(Call<Response<User>> call, Throwable t) {
            Log.e("Login error", "onFailure: "+t.getMessage());
        }
    };
    private boolean validate(String username, String password){
        if(TextUtils.isEmpty(username)){
            binding.tilUsername.setError("Please enter username");
            return false;
        }else {
            binding.tilUsername.setError(null);
        }
        if(TextUtils.isEmpty(password)){
            binding.tilPass.setError("Enter your password");
            return false;
        }else {
            binding.tilPass.setError(null);
        }
        return true;
    }
    private void loading(boolean isLoading){
        if(isLoading){
            binding.progressBar.setVisibility(View.VISIBLE);
            binding.btnLogin.setEnabled(false);
        }else {
            binding.progressBar.setVisibility(View.INVISIBLE);
            binding.btnLogin.setEnabled(false);
        }
    }
    private void saveUserInfor(String token, String refreshToken, String userId,String name){
        SharedPreferences preferences = getSharedPreferences("INFO",LoginActivity.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("token", token);
        editor.putString("refreshToken", refreshToken);
        editor.putString("id", userId);
        editor.putString("name", name);
        editor.apply();
    }
}