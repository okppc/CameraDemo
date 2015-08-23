package com.eebbk.camerademo;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.Toast;

import com.eebbk.utils.DatabaseManager;
import com.eebbk.utils.PicDatabase;

public class PictureInfo extends Activity {
	
	private GridView mGridView;
	private List<Bitmap> mPictureList;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_camera_view);
		
		
		
		//��ͼƬ�б�������ݼ���
		mPictureList = new ArrayList<Bitmap>();  
		mPictureList = loadPicData();
		
	    mGridView = (GridView) findViewById(R.id.gv_show_picture);
	    mGridView.setAdapter(new MyGridviewAdapter(this));
	    
		Intent i = getIntent();
		byte[] bytes = i.getByteArrayExtra("picture");
		//mShowImage.setImageBitmap(BitmapFactory.decodeByteArray(bytes, 0, bytes.length));
	}
	
	//��list�б��м�������
	public List<Bitmap> loadPicData(){
		List<Bitmap> mList = new ArrayList<Bitmap>();
		Cursor mCursor = DatabaseManager.queryDB(PicDatabase.Table_Name_Pic, null, null, null, null, null, PicDatabase.Pic_Name);
		if(mCursor != null){
			while(mCursor.moveToNext()){
				String name = mCursor.getString(mCursor.getColumnIndex(PicDatabase.Pic_Name));
				String path = mCursor.getString(mCursor.getColumnIndex(PicDatabase.Pic_Path));
				BitmapFactory.Options opts = new BitmapFactory.Options();
				opts.inJustDecodeBounds = true;//������ͼƬ��ֻ��ȡ���Ŀ����Ϣ
				opts.outHeight = 100;//���ø�
				opts.outWidth = 75;//���ÿ�
				opts.inSampleSize = 4;//�õ�һ����߶�Ϊԭͼ1/4������ͼ��Ϊ�˽�ʡ�ڴ�
				opts.inJustDecodeBounds = false;//��ʼ����ͼƬ
				Bitmap mBp = BitmapFactory.decodeFile(path, opts);
				mList.add(mBp);
			}
			Toast.makeText(this, "ͼƬ���еĳ���Ϊ��"+mList.size(), Toast.LENGTH_SHORT).show();
		}
		
		return mList;
	}
	
	public void xianshi(View view){
		
	}
	
	
	//��ʾGridView����
	class MyGridviewAdapter extends BaseAdapter{
		
		private LayoutInflater mInflate;
		private Context context;
		
		public MyGridviewAdapter(Context context) {
			this.context = context;
			mInflate = LayoutInflater.from(context);
		}
		
		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return mPictureList.size();
		}

		@Override
		public Object getItem(int position) {
			// TODO Auto-generated method stub
			//return mPictureList.get(position);
			return position;
		}

		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
//			Holder holder;
//			if(convertView == null){
//				holder = new Holder();
//				convertView = mInflate.inflate(R.layout.pic_gridview_style, null);
//				holder.mImage = (ImageView) convertView.findViewById(R.id.gv_picture);
//				convertView.setTag(holder);
//			}else{
//				holder = (Holder) convertView.getTag();
//			}
//			
//			Bitmap bm = mPictureList.get(position);
//			holder.mImage.setImageBitmap(bm);
			
			ImageView mView;
			if(convertView == null){
				mView = new ImageView(context);
				mView.setImageBitmap(mPictureList.get(position));
			}else{
				mView = (ImageView) convertView;
			}
			
			return mView;
		}
		
		
	}
	
	class Holder{
		ImageView mImage;
	}
	
	
	
	
	
	
}
