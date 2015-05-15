package com.company.paradisetest;

import java.util.List;
import com.company.paradisetest.metier.Product;
import com.company.paradisetest.metier.ServiceParadise;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ActivityOptions;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity {
	private ListView productsList;
	private ServiceParadise service;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		service = new ServiceParadise();
		Typeface robotoL = Typeface.createFromAsset(this.getAssets(), "font/Roboto-Light.ttf");
		Typeface robotoM = Typeface.createFromAsset(this.getAssets(), "font/Roboto-Medium.ttf");
		Typeface aguafina = Typeface.createFromAsset(this.getAssets(), "font/AguafinaScript-Regular.ttf");
		Typeface robotoR = Typeface.createFromAsset(this.getAssets(), "font/Roboto-Regular.ttf");
		Typeface robotoT = Typeface.createFromAsset(this.getAssets(), "font/Roboto-Thin.ttf");
		
		TextView appTitle = (TextView) findViewById(R.id.appTitle);
		
		appTitle.setTypeface(robotoR);
		this.productsList = (ListView) findViewById(R.id.listProducts);
		View header = getLayoutInflater().inflate(R.layout.list_header, null);
		TextView frontProducts = (TextView) header.findViewById(R.id.frontProducts);
		frontProducts.setTypeface(robotoR);
		this.productsList.addHeaderView(header);
		List<Product> products = service.getProducts();
		ProductArrayAdapter adapter = new ProductArrayAdapter(this, R.layout.list_item, products);
		this.productsList.setAdapter(adapter);
		
		OnClickListener moveToMap = new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent mapActivity = new Intent(MainActivity.this, Map.class);
				startActivity(mapActivity);
				overridePendingTransition(R.anim.animation, R.anim.animation2);
			}
		};
		
		ImageButton search = (ImageButton) findViewById(R.id.search);
		search.setOnClickListener(moveToMap);
	}
	

}
