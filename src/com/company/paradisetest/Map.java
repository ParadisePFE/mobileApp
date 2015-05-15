package com.company.paradisetest;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.company.paradisetest.metier.Seller;
import com.company.paradisetest.metier.ServiceParadise;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnCameraChangeListener;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.GoogleMap.OnMapClickListener;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

public class Map extends FragmentActivity implements
		OnMapClickListener, android.view.View.OnClickListener {
	private GoogleMap mMap;
	private LatLng latLng;
	private ImageButton btnFind;
	private EditText etLocation;
	private Geocoder geocoder;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.map);
		geocoder = new Geocoder(getBaseContext());
		setUpMapIfNeeded();

		btnFind = (ImageButton) findViewById(R.id.btn_find);
		btnFind.setOnClickListener(this);

		etLocation = (EditText) findViewById(R.id.etLocation);

		double lat = latLng.latitude;
		double lng = latLng.longitude;
		try {
			List<Seller> sellers = new ServiceParadise().getCloseSellers(lat,
					lng);
			List<Marker> markers = new ArrayList<Marker>();
			if (sellers.size() > 0) {
				for (Seller seller : sellers) {
					Marker marker = mMap
							.addMarker(new MarkerOptions()
									.position(
											new LatLng(seller.getLat(), seller
													.getLng()))
									.title(seller.getName())
									.icon(BitmapDescriptorFactory
											.fromResource(R.drawable.icon_marker)));

					markers.add(marker);
				}
				LatLngBounds.Builder b = new LatLngBounds.Builder();
				for (Marker marker : markers) {
					b.include(marker.getPosition());
				}
				final LatLngBounds bounds = adjustBoundsForMaxZoomLevel(b
						.build());
				mMap.setOnCameraChangeListener(new OnCameraChangeListener() {

					@Override
					public void onCameraChange(CameraPosition arg0) {
						// Move camera.
						mMap.animateCamera(CameraUpdateFactory.newLatLngBounds(
								bounds, 15));
						// Remove listener to prevent position reset on camera
						// move.
						mMap.setOnCameraChangeListener(null);
					}
				});
			} else {
				Toast.makeText(this, "NO CLOSE SELLERS !!", 2000).show();
			}
		} catch (Exception e) {
			Toast.makeText(this, "Unable to connect the server !!", 4000)
					.show();
		}

	}

	private LatLngBounds adjustBoundsForMaxZoomLevel(LatLngBounds bounds) {
		LatLng sw = bounds.southwest;
		LatLng ne = bounds.northeast;
		double deltaLat = Math.abs(sw.latitude - ne.latitude);
		double deltaLon = Math.abs(sw.longitude - ne.longitude);

		final double zoomN = 0.005; // minimum zoom coefficient
		if (deltaLat < zoomN) {
			sw = new LatLng(sw.latitude - (zoomN - deltaLat / 2), sw.longitude);
			ne = new LatLng(ne.latitude + (zoomN - deltaLat / 2), ne.longitude);
			bounds = new LatLngBounds(sw, ne);
		} else if (deltaLon < zoomN) {
			sw = new LatLng(sw.latitude, sw.longitude - (zoomN - deltaLon / 2));
			ne = new LatLng(ne.latitude, ne.longitude + (zoomN - deltaLon / 2));
			bounds = new LatLngBounds(sw, ne);
		}

		return bounds;
	}

	private void setUpMapIfNeeded() {
		if (mMap == null) {
			mMap = ((SupportMapFragment) getSupportFragmentManager()
					.findFragmentById(R.id.map)).getMap();
			if (mMap != null) {
				setUpMap();
			}
		}
	}

	private void setUpMap() {
		LocationManager locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
		Criteria criteria = new Criteria();
		String provider = locationManager.getBestProvider(criteria, true);
		Location myLocation = locationManager.getLastKnownLocation(provider);
		System.out.println("lat ::::: " + myLocation.getLatitude());
		mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
		double latitude = myLocation.getLatitude();
		double longitude = myLocation.getLongitude();
		latLng = new LatLng(latitude, longitude);
		mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
		mMap.animateCamera(CameraUpdateFactory.zoomTo(20));
		Marker m = mMap.addMarker(new MarkerOptions().position(latLng).title(
				"You are here!"));
		m.showInfoWindow();
		mMap.setOnMapClickListener(this);
	}

	@Override
	public void onMapClick(LatLng point) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub

		List<Address> addresses = new ArrayList<Address>();
		String locationName = etLocation.getText().toString();

		if (locationName != null && !locationName.equals("")) {
			try {
				addresses = geocoder.getFromLocationName(locationName, 2);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				addresses = null;
			}

			if (addresses == null || addresses.size() == 0) {
				Toast.makeText(this, "No Location found !", 3000).show();
			} else {
				Address address = addresses.get(0);
				latLng = new LatLng(address.getLatitude(),
						address.getLongitude());
				String addressText = String.format(
						"%s, %s",
						address.getMaxAddressLineIndex() > 0 ? address
								.getAddressLine(0) : "", address
								.getCountryName());
				mMap.clear();
				Marker marker = mMap.addMarker(new MarkerOptions().position(
						latLng).title(addressText));
				marker.showInfoWindow();
				mMap.animateCamera(CameraUpdateFactory.newLatLng(latLng));
				List<Seller> sellers = new ServiceParadise().getCloseSellers(
						latLng.latitude, latLng.longitude);
				List<Marker> markers = new ArrayList<Marker>();
				markers.add(marker);
				if (sellers.size() > 0) {
					for (Seller seller : sellers) {
						marker = mMap.addMarker(new MarkerOptions()
								.position(
										new LatLng(seller.getLat(), seller
												.getLng()))
								.title(seller.getName())
								.icon(BitmapDescriptorFactory
										.fromResource(R.drawable.icon_marker)));

						markers.add(marker);
					}
					LatLngBounds.Builder b = new LatLngBounds.Builder();
					for (Marker mrkr : markers) {
						b.include(mrkr.getPosition());
					}
					final LatLngBounds bounds = adjustBoundsForMaxZoomLevel(b
							.build());
					mMap.setOnCameraChangeListener(new OnCameraChangeListener() {

						@Override
						public void onCameraChange(CameraPosition arg0) {
							// Move camera.
							mMap.animateCamera(CameraUpdateFactory
									.newLatLngBounds(bounds, 20));
							// Remove listener to prevent position reset on
							// camera move.
							mMap.setOnCameraChangeListener(null);
						}
					});
				} else {
					Toast.makeText(this, "NO CLOSE SELLERS !!", 2000).show();
				}

			}
		}

	}

}
