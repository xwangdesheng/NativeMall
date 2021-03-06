package com.awake.dreaming.activity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomSheetBehavior;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Display;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import com.awake.dreaming.R;
import com.awake.dreaming.adapter.ImageSelectorAdapter;
import com.awake.dreaming.datas.Folder;
import com.awake.dreaming.datas.Image;
import com.awake.dreaming.fragment.BottomSheetFragment;
import com.awake.dreaming.utils.ImageUtils;
import com.awake.dreaming.utils.StatusBarUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by wangdesheng on 2017/10/27 0027.
 */

public class ImageSelectorActivity extends BaseActivity {

    private RecyclerView recyclerImg;
    private ImageSelectorAdapter adapter;
    private BottomSheetBehavior behavior;
    private ArrayList<Folder> folder;
    private Display display;
    private static final int READ_PIC_CODE = 0;
    private static final int OPEN_CAMERA_CODE = 1;
    private static final int TAKE_PIC_RESULT_CODE = 2;
    private static String TAG = "ImageSelectorActivity";

    private TextView folderName;
    private TextView preview;
    private Map<Integer, Object> previewList = new HashMap<>();

    @Override
    protected int getLayout() {
        return R.layout.activity_image_selector;
    }

    @Override
    protected void setLayout() {

        StatusBarUtils.transparentStatusBar(this);
        overridePendingTransition(R.anim.animation_x_on, R.anim.animation_x_off);
        recyclerImg = (RecyclerView) findViewById(R.id.rl_img_selector);

        View bottomSheet = findViewById(R.id.rl_bottomSheet);
        behavior = BottomSheetBehavior.from(bottomSheet);
        behavior.setState(BottomSheetBehavior.STATE_HIDDEN);

        findViewById(R.id.img_back).setOnClickListener(this);
        findViewById(R.id.tv_complete).setOnClickListener(this);
        folderName = (TextView) findViewById(R.id.tv_all_images);
        preview = (TextView) findViewById(R.id.tv_preview);

        folderName.setOnClickListener(this);
        preview.setOnClickListener(this);

        WindowManager windowManager = (WindowManager) getSystemService(WINDOW_SERVICE);
        display = windowManager.getDefaultDisplay();
        checkPermission();
    }

    @Override
    protected void onViewClick(View view) {
        switch (view.getId()){
            case R.id.tv_complete:{
                toast("Complete");
                break;
            }
            case R.id.img_back:{
                this.finish();
                break;
            }
            case R.id.tv_all_images:{
                if (behavior.getState() == BottomSheetBehavior.STATE_HIDDEN){
                    //if BottomSheet is invisibility that setting status for visibility.
                    behavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
                    //delivery array to BottomSheetFragment.
                    BottomSheetFragment fragment = new BottomSheetFragment();
                    Bundle bundle = new Bundle();
                    bundle.putParcelableArrayList("key",folder);
                    fragment.setArguments(bundle);
                    //set fragment.
                    FragmentManager fm = getSupportFragmentManager();
                    FragmentTransaction ft = fm.beginTransaction();
                    ft.replace(R.id.fl_bottom_folder, fragment);
                    ft.commit();
                }
                break;
            }
            case R.id.tv_preview:{
                //toast("Preview");
                if (previewList.isEmpty()) {
                    toast("sb，没有选择照片");
                }else {

                }
            }
        }
    }

    /**
     * check permission and load image.
     */
    private void checkPermission(){
        int writePermission = ContextCompat.checkSelfPermission(getApplication(),
                Manifest.permission.WRITE_EXTERNAL_STORAGE);
        if (writePermission == PackageManager.PERMISSION_GRANTED){
            //has permission, can load image.
            loadImage();
        }else {
            //no permission that apply for it.
            ActivityCompat.requestPermissions(ImageSelectorActivity.this,
                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, READ_PIC_CODE);
        }
    }

    /**
     * callback of handle permission
     */
    @Override
    public void onRequestPermissionsResult
    (int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case READ_PIC_CODE:{
                if (grantResults.length > 0 && grantResults[0]==PackageManager.PERMISSION_GRANTED){
                    //allow permission, loading image.
                    loadImage();
                }else {
                    //refuse permission and pop prompt dialog that there no permission.
                    Toast.makeText(ImageSelectorActivity.this, "No Permission", Toast.LENGTH_SHORT).show();
                }
                break;
            }
            case OPEN_CAMERA_CODE:{
                if (grantResults.length>0 && grantResults[0]==PackageManager.PERMISSION_GRANTED){
                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    startActivityForResult(intent, 0);
                }
                break;
            }
        }
    }

    /**
     * loading image method.
     */
    private void loadImage() {
        ImageUtils.loadImageForSDCard(this, new ImageUtils.CallBack() {
            @Override
            public void onSuccess(ArrayList<Folder> folders, final ArrayList<Image> images) {
                folder = folders;
                Log.e(TAG, "扫描到的图片数量: " + folders.get(1).getImages().size() );
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        recyclerImg.setLayoutManager
                                (new GridLayoutManager(ImageSelectorActivity.this, 3));
                        setImages(images, "全部图片");

                        itemClick();
                        checkedBox();
                    }
                });
            }
        });
    }

    /**
     * setting adapter
     */
    public void setImages(ArrayList<Image> images, String name) {
        adapter = new ImageSelectorAdapter(ImageSelectorActivity.this, images, display);
        recyclerImg.setAdapter(adapter);

        folderName.setText(name);

        if (behavior.getState() == BottomSheetBehavior.STATE_COLLAPSED ||
                behavior.getState() == BottomSheetBehavior.STATE_EXPANDED) {
            behavior.setState(BottomSheetBehavior.STATE_HIDDEN);
        }

        if (!previewList.isEmpty()) {
            previewList.clear();
            preview.setText("预览");
        }

        Log.e(TAG, "setImages: " + previewList.size());

        itemClick();
        checkedBox();
    }

    /**
     * Item click event in grid list.
     */
    private void itemClick() {
        adapter.setOnItemClickListener(new ImageSelectorAdapter.OnItemClickListener() {
            @Override
            public void itemClick(ImageSelectorAdapter.ViewHolder holder,
                                  int position, String path, boolean isYes) {
                if (isYes){
                    openCamera();
                }else {
                    Log.e(TAG, "点击到的图片的路径: " + path );
                    Intent intent = new Intent(ImageSelectorActivity.this, PreviewActivity.class);
                    intent.putExtra("path", path);
                    startActivity(intent);
                }
            }
        });
    }

    /**
     * CheckBox widget is checked.
     */
    private void checkedBox() {
        adapter.setOnCheckBoxListener(new ImageSelectorAdapter.OnCheckBoxListener() {
            @Override
            public void checked(boolean checked, int position, String path) {
                if (checked) {
                    previewList.put(position, path);
                    int size = previewList.size();
                    preview.setText("预览(" + size +")");
//                    Log.e(TAG, "选中时的大小: " + size );
//                    Log.e(TAG, "选中时的内容: " + previewList.get(position) );
                }else {
                    //Log.e(TAG, "取消时的内容: " + previewList.get(position) );
                    previewList.remove(position);
                    int size = previewList.size();
                    if (size == 0) {
                        preview.setText("预览");
                    }else {
                        preview.setText("预览(" + size +")");
                    }
                    //Log.e(TAG, "取消时的大小: " + size );
                }
            }
        });
    }

    /**
     * open camera method
     */
    private void openCamera(){
        int cameraPermission = ContextCompat.checkSelfPermission(getApplication(),
                Manifest.permission.CAMERA);
        if (cameraPermission == PackageManager.PERMISSION_GRANTED){
            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            startActivityForResult(intent, TAKE_PIC_RESULT_CODE);
        }else {
            ActivityCompat.requestPermissions(ImageSelectorActivity.this,
                    new String[]{Manifest.permission.CAMERA}, OPEN_CAMERA_CODE);
        }
    }

    /**
     * click back key and BottomSheet's status that is setting STATUS_HIDDEN
     */
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK
                && (behavior.getState() == BottomSheetBehavior.STATE_COLLAPSED
                || behavior.getState() == BottomSheetBehavior.STATE_EXPANDED)){
            behavior.setState(BottomSheetBehavior.STATE_HIDDEN);
        }else {
            finish();
        }
        return false;
    }

    private void toast(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }
}
