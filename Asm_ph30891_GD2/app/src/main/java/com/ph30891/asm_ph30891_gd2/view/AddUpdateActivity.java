package com.ph30891.asm_ph30891_gd2.view;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.bumptech.glide.Glide;
import com.ph30891.asm_ph30891_gd2.adapter.ItemImageAdapter;
import com.ph30891.asm_ph30891_gd2.adapter.ItemImageDetailAdapter;
import com.ph30891.asm_ph30891_gd2.databinding.ActivityAddUpdateBinding;

import com.ph30891.asm_ph30891_gd2.R;
import com.ph30891.asm_ph30891_gd2.model.Distributor;
import com.ph30891.asm_ph30891_gd2.model.Fruit;
import com.ph30891.asm_ph30891_gd2.model.Response;
import com.ph30891.asm_ph30891_gd2.networking.HttpRequest;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;

public class AddUpdateActivity extends AppCompatActivity {
    private ActivityAddUpdateBinding binding;
    private HttpRequest httpRequest;
    private String id_distributor;
    private ArrayList<Distributor> distributorsList = new ArrayList<>();
    private ArrayList<File> ds_image = new ArrayList<>();
    private SharedPreferences sharedPreferences;
    private String token;
    private boolean isAdd = true;
    String idUpdate = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAddUpdateBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        sharedPreferences = getSharedPreferences("INFO", MainAdminActivity.MODE_PRIVATE);
        token = sharedPreferences.getString("token", "");
        httpRequest = new HttpRequest(token);

        setDataOnViewUpdate();
        init();
    }

    private void setDataOnViewUpdate() {
        Fruit fruit = (Fruit) getIntent().getSerializableExtra("fruit");
        if (fruit != null) {
            isAdd = false;
            idUpdate = fruit.get_id();
            setDataImage(fruit.getImages());
            binding.edName.setText(fruit.getName());
            binding.edDes.setText(fruit.getDescription());
            binding.edPrice.setText(String.valueOf(fruit.getPrice()));
            binding.edQuantity.setText(String.valueOf(fruit.getQuantity()));

            int distributorPosition;
            for (int i = 0; i < distributorsList.size(); i++) {
                if (distributorsList.get(i).getId().equals(fruit.getDistributorId())) {
                    distributorPosition = i;
                    binding.spnDistributor.setSelection(distributorPosition);
                    break;
                }
            }

            if (fruit.getStatus() == 0) {
                binding.rdoSelling.setChecked(true);
            } else if (fruit.getStatus() == 1) {
                binding.rdoNotYetSold.setChecked(true);
            }
        } else {
            isAdd = true;
        }
    }

    private void init() {
        httpRequest.calAPI().getListDistributor().enqueue(getDistributorAPI);
        binding.spnDistributor.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Distributor distributor = distributorsList.get(position);
                id_distributor = distributor.getId();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        binding.spnDistributor.setSelection(0);
        binding.imageF.setOnClickListener(v -> {
            chooseImage();
        });
        binding.btnSave.setOnClickListener(v -> {
            if(isAdd){
                handleAdd();
            }else {
                handleUpdate();
            }
        });
    }
    private void handleAdd(){
        Map<String , RequestBody> mapRequestBody = new HashMap<>();
        String _name = binding.edName.getText().toString().trim();
        String _quantity = binding.edQuantity.getText().toString().trim();
        String _price = binding.edPrice.getText().toString().trim();
        String _status = "";
        if(binding.rdoSelling.isChecked()){
            _status = "0".trim();
        } else if (binding.rdoNotYetSold.isChecked()) {
            _status = "1".trim();
        }
        String _description = binding.edDes.getText().toString().trim();

        mapRequestBody.put("name", getRequestBody(_name));
        mapRequestBody.put("quantity", getRequestBody(_quantity));
        mapRequestBody.put("price", getRequestBody(_price));
        mapRequestBody.put("status", getRequestBody(_status));
        mapRequestBody.put("description", getRequestBody(_description));
        mapRequestBody.put("id_distributor", getRequestBody(id_distributor));
        ArrayList<MultipartBody.Part> _ds_image = new ArrayList<>();
        ds_image.forEach(file1 -> {
            RequestBody requestFile = RequestBody.create(MediaType.parse("image/*"),file1);
            MultipartBody.Part multipartBodyPart = MultipartBody.Part.createFormData("image", file1.getName(),requestFile);
            _ds_image.add(multipartBodyPart);
        });
        httpRequest.calAPI().addFruitWithFileImage(mapRequestBody, _ds_image).enqueue(responseFruit);
    }

    private void handleUpdate(){
        Map<String , RequestBody> mapRequestBody = new HashMap<>();
        String _name = binding.edName.getText().toString().trim();
        String _quantity = binding.edQuantity.getText().toString().trim();
        String _price = binding.edPrice.getText().toString().trim();
        String _status = "";
        if(binding.rdoSelling.isChecked()){
            _status = "0".trim();
        } else if (binding.rdoNotYetSold.isChecked()) {
            _status = "1".trim();
        }
        String _description = binding.edDes.getText().toString().trim();

        mapRequestBody.put("name", getRequestBody(_name));
        mapRequestBody.put("quantity", getRequestBody(_quantity));
        mapRequestBody.put("price", getRequestBody(_price));
        mapRequestBody.put("status", getRequestBody(_status));
        mapRequestBody.put("description", getRequestBody(_description));
        mapRequestBody.put("id_distributor", getRequestBody(id_distributor));
        ArrayList<MultipartBody.Part> _ds_image = new ArrayList<>();
        ds_image.forEach(file1 -> {
            RequestBody requestFile = RequestBody.create(MediaType.parse("image/*"),file1);
            MultipartBody.Part multipartBodyPart = MultipartBody.Part.createFormData("image", file1.getName(),requestFile);
            _ds_image.add(multipartBodyPart);
        });
        // Gọi phương thức cập nhật dữ liệu
        if(ds_image != null){
            Fruit fruit = new Fruit();
            fruit.setName(_name); fruit.setQuantity(Integer.parseInt(_quantity));
            fruit.setPrice(Double.parseDouble(_price)); fruit.setStatus(Integer.parseInt(_status));
            fruit.setDescription(_description);
            httpRequest.calAPI().updateFruitById(idUpdate,mapRequestBody,_ds_image).enqueue(updateFruitApi);
        }else {
            Fruit fruit = new Fruit();
            fruit.setName(_name); fruit.setQuantity(Integer.parseInt(_quantity));
            fruit.setPrice(Double.parseDouble(_price)); fruit.setStatus(Integer.parseInt(_status));
            fruit.setDescription(_description);

            httpRequest.calAPI().updateFruitByIdNotImg(idUpdate, fruit).enqueue(updateFruitApi);
        }
    }
    private void setDataImage(ArrayList<String> images) {
        ItemImageAdapter adapter = new ItemImageAdapter(this, images);
        binding.rcvImage.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        binding.rcvImage.setAdapter(adapter);
        Glide.with(this).load(images.get(0)).centerCrop().error(R.drawable.image).into(binding.imageF);
        adapter.notifyDataSetChanged();
    }
    private RequestBody getRequestBody(String value) {
        return RequestBody.create(MediaType.parse("multipart/form-data"),value);
    }
    Callback<Response<ArrayList<Distributor>>> getDistributorAPI = new Callback<Response<ArrayList<Distributor>>>() {
        @Override
        public void onResponse(Call<Response<ArrayList<Distributor>>> call, retrofit2.Response<Response<ArrayList<Distributor>>> response) {
            if (response.isSuccessful()) {
                if (response.body().getStatus() == 200) {
                    distributorsList = response.body().getData();
                    String[] items = new String[distributorsList.size()];

                    for (int i = 0; i < distributorsList.size(); i++) {
                        items[i] = distributorsList.get(i).getName();
                    }
                    ArrayAdapter<String> adapterSpin = new ArrayAdapter<>(AddUpdateActivity.this, android.R.layout.simple_spinner_item, items);
                    adapterSpin.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    binding.spnDistributor.setAdapter(adapterSpin);
                }
            }
        }

        @Override
        public void onFailure(Call<Response<ArrayList<Distributor>>> call, Throwable t) {

        }
    };

    private void chooseImage() {
        Log.d("imageeeeeeee", "chooseAvatar: " + 11111);
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
        getImage.launch(intent);

    }

    ActivityResultLauncher<Intent> getImage = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult o) {
                    if (o.getResultCode() == Activity.RESULT_OK) {
                        ds_image.clear();
                        Intent data = o.getData();
                        if (data.getClipData() != null) {
                            int count = data.getClipData().getItemCount();
                            for (int i = 0; i < count; i++) {
                                Uri imageUri = data.getClipData().getItemAt(i).getUri();

                                File file = createFileFormUri(imageUri, "image" + i);
                                ds_image.add(file);
                            }
                        } else if (data.getData() != null) {
                            Uri imageUri = data.getData();
                            File file = createFileFormUri(imageUri, "image");
                            ds_image.add(file);
                        }
                        showImageChoose(ds_image);
                        Glide.with(AddUpdateActivity.this)
                                .load(ds_image.get(0))
                                .thumbnail(Glide.with(AddUpdateActivity.this).load(R.drawable.image))
                                .centerCrop()
                                .into(binding.imageF);
                    }
                }
            });
    private File createFileFormUri(Uri path, String name) {
        File _file = new File(AddUpdateActivity.this.getCacheDir(), name + ".png");
        try {
            InputStream in = AddUpdateActivity.this.getContentResolver().openInputStream(path);
            OutputStream out = new FileOutputStream(_file);
            byte[] buf = new byte[1024];
            int len;
            while ((len = in.read(buf)) > 0) {
                out.write(buf, 0, len);
            }
            out.close();
            in.close();
            Log.d("123123", "createFileFormUri: " + _file);
            return _file;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }
    public void showImageChoose(ArrayList<File> images) {
        ItemImageDetailAdapter adapter = new ItemImageDetailAdapter(this, images);
        binding.rcvImage.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,false));
        binding.rcvImage.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }
    Callback<Response<Fruit>> responseFruit = new Callback<Response<Fruit>>() {
        @Override
        public void onResponse(Call<Response<Fruit>> call, retrofit2.Response<Response<Fruit>> response) {
            if (response.isSuccessful()) {
                Log.d("adddddd", "onResponse: " + response.body().getStatus());
                if (response.body().getStatus()==200) {
                    Toast.makeText(AddUpdateActivity.this, "Thêm mới thành công", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(AddUpdateActivity.this, MainAdminActivity.class));
                    finish();
                }
            }
        }

        @Override
        public void onFailure(Call<Response<Fruit>> call, Throwable t) {
            Log.e("Add fruit with image fail", "onFailure: "+t.getMessage() );
        }
    };
    Callback<Response<Fruit>> updateFruitApi = new Callback<Response<Fruit>>() {
        @Override
        public void onResponse(Call<Response<Fruit>> call, retrofit2.Response<Response<Fruit>> response) {
            if(response.isSuccessful()){
                if(response.body().getStatus() == 200){
                    Toast.makeText(AddUpdateActivity.this, "Update successfully", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(AddUpdateActivity.this,MainAdminActivity.class));
                    finish();
                }
            }
        }

        @Override
        public void onFailure(Call<Response<Fruit>> call, Throwable t) {

        }
    };
}