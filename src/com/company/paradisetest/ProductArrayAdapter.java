package com.company.paradisetest;

import java.io.InputStream;
import java.net.URL;
import java.util.List;

import com.company.paradisetest.metier.Product;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class ProductArrayAdapter extends ArrayAdapter<Product>{
	private List<Product> products;
	public ProductArrayAdapter(Context context, int textViewResourceId,
			List<Product> values){
		super(context, textViewResourceId, values);
		this.products=values;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		
	  try{	
		LayoutInflater inflater=(LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		final View rowView=inflater.inflate(R.layout.list_item,parent,false);
		final Typeface robotoL = Typeface.createFromAsset(rowView.getContext().getAssets(), "font/Roboto-Light.ttf");
		final Typeface robotoM = Typeface.createFromAsset(rowView.getContext().getAssets(), "font/Roboto-Medium.ttf");
		final Typeface aguafina = Typeface.createFromAsset(rowView.getContext().getAssets(), "font/AguafinaScript-Regular.ttf");
		final Typeface robotoR = Typeface.createFromAsset(rowView.getContext().getAssets(), "font/Roboto-Regular.ttf");
		final Typeface robotoT = Typeface.createFromAsset(rowView.getContext().getAssets(), "font/Roboto-Thin.ttf");
		TextView productTitle=(TextView) rowView.findViewById(R.id.productTitle);
		productTitle.setTypeface(robotoM);
		TextView productDesc=(TextView) rowView.findViewById(R.id.productDesc);
		productDesc.setTypeface(robotoL);
		TextView productPrice=(TextView) rowView.findViewById(R.id.productPrice);
		productPrice.setTypeface(aguafina);
		TextView hiddenId=(TextView) rowView.findViewById(R.id.hiddenId);
		hiddenId.setText(products.get(position).getId()+"");
		
		ImageView productImg=(ImageView) rowView.findViewById(R.id.productImg);
		String title = products.get(position).getName();
		if(title.length()>20)
			title = title.substring(0, 17)+"...";
		productTitle.setText(title);
		String desc = products.get(position).getDescription();
		if(desc.length()>130)
			desc = desc.substring(0, 127)+"...";
		productDesc.setText(desc);
		productPrice.setText(products.get(position).getPrice()+" ");
		
		OnClickListener showPopup = new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				final Dialog dialog =new Dialog(rowView.getContext());
				dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
				dialog.setContentView(R.layout.product_popup);
				TextView title = (TextView) dialog.findViewById(R.id.popProductTitle);
				TextView subTitle = (TextView) dialog.findViewById(R.id.popProductSubTitle);
				TextView composTitle = (TextView) dialog.findViewById(R.id.compositionTitle);
				TextView compos = (TextView) dialog.findViewById(R.id.composition);
				
				title.setTypeface(robotoR);
				subTitle.setTypeface(robotoT);
				composTitle.setTypeface(robotoR);
				compos.setTypeface(robotoL);
				
				dialog.getWindow().getAttributes().windowAnimations = R.style.dialog_animation;
				
				ImageButton fermer = (ImageButton) dialog.findViewById(R.id.btnFermer);
				fermer.setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						dialog.hide();
					}
				});
				dialog.show();
				
			}
		};
		LinearLayout productText=(LinearLayout) rowView.findViewById(R.id.productText);
		productText.setOnClickListener(showPopup);
		productImg.setOnClickListener(showPopup);
		
		//imageView.setImageResource(R.drawable.ic_launcher);
		InputStream is=new URL(products.get(position).getImg()).openStream();
		Bitmap bitmap=BitmapFactory.decodeStream(is);
		productImg.setImageBitmap(bitmap);
		return rowView;
	  }
	  catch(Exception e){
		  e.printStackTrace();
		  return null;
	  }
	}

}
