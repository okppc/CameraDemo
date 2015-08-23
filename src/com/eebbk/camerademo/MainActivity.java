package com.eebbk.camerademo;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.graphics.ImageFormat;
import android.hardware.Camera;
import android.hardware.Camera.Parameters;
import android.hardware.Camera.PictureCallback;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceHolder.Callback;
import android.view.SurfaceView;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;

import com.eebbk.utils.DatabaseManager;
import com.eebbk.utils.DateFormatUtils;
import com.eebbk.utils.PicDatabase;

public class MainActivity extends Activity implements OnClickListener {

	private Button mTakePicture;
	private SurfaceView mSurView;
	private Camera mCamera;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		//�������ݿ�
		DatabaseManager.getInstance(this);
		
		mTakePicture = (Button) findViewById(R.id.btn_take_picture);
		mTakePicture.setOnClickListener(this);
		
		
		mSurView = (SurfaceView) findViewById(R.id.sv_show_picture);
		mSurView.getHolder().setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
		mSurView.getHolder().setFixedSize(240, 320);
		mSurView.getHolder().setKeepScreenOn(true);
		mSurView.getHolder().addCallback(new SurfaceCallback());
		
		
	}


	@Override
	public void onClick(View v) {
		switch(v.getId()){
		case R.id.btn_take_picture:

			mCamera.takePicture(null, null, new MyPictureCallback());//������
			Log.i("aaa", "���������յķ���");

			break;
		}

	}

	//SurfaceView�Ļص�����
	class SurfaceCallback implements Callback{

		//�ഴ����ʱ����ã���һЩ��ʼ���Ĺ���
		@Override
		public void surfaceCreated(SurfaceHolder holder) {
			try {  
				mCamera = Camera.open(); // ������ͷ  
				mCamera.setPreviewDisplay(holder); // ����������ʾ����Ӱ���SurfaceHolder���� 
				Parameters mParamters = mCamera.getParameters(); // ��ȡ�������  
				mParamters.setPictureFormat(ImageFormat.JPEG); // ����ͼƬ��ʽ 
				mParamters.set("orientation", "portrait");
				mParamters.set("rotation", 90);//������ʾ��ͼƬ������
				
				
				
				
				mCamera.setDisplayOrientation(90);//��������ʱԤ������������
				mCamera.setParameters(mParamters);//�����Ӧ����Щ����
				
				mCamera.startPreview(); // ��ʼԤ��  
			} catch (Exception e) {  
				e.printStackTrace();  
			}  

		}
		//SurfaceView�����ı��ʱ���Զ�����
		@Override
		public void surfaceChanged(SurfaceHolder holder, int format, int width,
				int height) {

			Parameters mParamters = mCamera.getParameters(); // ��ȡ�������  
//			mParamters.setPictureFormat(ImageFormat.JPEG); // ����ͼƬ��ʽ 
//			//mParamters.set("orientation", "portrait");
//			mParamters.set("rotation", 90);
			mParamters.setPreviewSize(width, height); // ����Ԥ����С  
			mParamters.setPreviewFrameRate(11);  //����ÿ����ʾ4֡  
			mParamters.setPictureSize(width, height); // ���ñ����ͼƬ�ߴ�  
			mParamters.setJpegQuality(100); // ������Ƭ���� 
			//mCamera.setParameters(mParamters);
			//			try {
			//				mCamera.setPreviewDisplay(holder);
			//			} catch (IOException e) {
			//				// TODO Auto-generated catch block
			//				e.printStackTrace();
			//			}
			//			mCamera.startPreview();
			
			

		}

		@Override
		public void surfaceDestroyed(SurfaceHolder holder) {
			if(mCamera != null){
				mCamera.release();
				mCamera = null;
			}

		}

	}



	//ͼƬ�Ļص�����
	class MyPictureCallback implements PictureCallback{

		@Override
		public void onPictureTaken(byte[] data, Camera camera) {
			// TODO Auto-generated method stub
			//Toast.makeText(MainActivity.this, "������Ƭ����", Toast.LENGTH_SHORT).show();
//			saveImageToSDcard(data);
			new MyTask().execute(data);
			
			Log.i("aaa", "������Ƭ�ˣ�");
			Intent i = new Intent(MainActivity.this,PictureInfo.class);
			i.putExtra("picture", data);
			startActivity(i);
			//camera.startPreview();


		}

	}


	
	
	//�������ݵ��ļ�����
	public boolean saveImageToSDcard(byte[] bytes){
		//�ڸ�Ŀ¼�´���һ���ļ��У������洢ͼƬ
		
		String path = Environment.getExternalStorageDirectory().getPath()+File.separatorChar+"MyCamera"+File.separatorChar;
		File mFileFolder = new File(path);
		//���Ŀ¼�����ڣ������༶Ŀ¼
				if(!mFileFolder.exists()){
					mFileFolder.mkdirs();
				}
		String mFileName = "IMG"+DateFormatUtils.formatCurrentTimeToString(System.currentTimeMillis())+".jpg";
		File mFile = new File(mFileFolder, mFileName);
		ContentValues cv = new ContentValues();
		cv.put(PicDatabase.Pic_Name, mFileName);
		cv.put(PicDatabase.Pic_Path, path+mFileName);
		DatabaseManager.insertDB(PicDatabase.Table_Name_Pic, null, cv);
		//����ͼƬ���ļ�����
			FileOutputStream fos;
			try {
				fos = new FileOutputStream(mFile);
				fos.write(bytes);
				fos.flush();
				fos.close();
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				return false;
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				return false;
			}
		return true;
	}
	
	//�첽��Ϣ����
	class MyTask extends AsyncTask<byte[], Void, Boolean>{
		
		//�ں�̨����ʼִ��ǰ���ã����ڽ���һЩ    �����ϵ�    ��ʼ��
		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
		}
		
		//ִ�к�̨��ʱ����
		@Override
		protected Boolean doInBackground(byte[]... params) {
			boolean saved = saveImageToSDcard(params[0]);
			if(!saved){
				return false;
			}
			
			return true;
			
		}
		
		//ÿ����̨���������publicProgress����������ͻᱻ����һ�Σ����ڸ���UI
		@Override
		protected void onProgressUpdate(Void... values) {
			// TODO Auto-generated method stub
			super.onProgressUpdate(values);
		}
		
		//����̨����ִ����ϲ�   ͨ��return���  ���з���ʱ����������ͻᱻ����,���Ը���UI������һЩ�ƺ���
		@Override
		protected void onPostExecute(Boolean result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			if(result){
				Toast.makeText(MainActivity.this, "ͼƬ��̨����ɹ�", Toast.LENGTH_SHORT).show();
			}else{
				Toast.makeText(MainActivity.this, "ͼƬ��̨����ʧ��", Toast.LENGTH_SHORT).show();
			}
			
		}

		
	}
	


}
