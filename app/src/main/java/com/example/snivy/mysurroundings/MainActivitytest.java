//package com.example.snivy.mysurroundings;
//
//import android.Manifest;
//import android.content.pm.PackageManager;
//import android.os.Bundle;
//import android.support.design.widget.NavigationView;
//import android.support.v4.app.ActivityCompat;
//import android.support.v4.content.ContextCompat;
//import android.support.v4.view.GravityCompat;
//import android.support.v4.widget.DrawerLayout;
//import android.support.v7.app.ActionBarDrawerToggle;
//import android.support.v7.app.AppCompatActivity;
//import android.support.v7.widget.SearchView;
//import android.support.v7.widget.Toolbar;
//import android.view.Menu;
//import android.view.MenuItem;
//import android.widget.TextView;
//import android.widget.Toast;
//
//import com.baidu.location.BDLocation;
//import com.baidu.location.BDLocationListener;
//import com.baidu.location.LocationClient;
//import com.baidu.location.LocationClientOption;
//import com.baidu.mapapi.SDKInitializer;
//import com.baidu.mapapi.map.BaiduMap;
//import com.baidu.mapapi.map.BitmapDescriptor;
//import com.baidu.mapapi.map.BitmapDescriptorFactory;
//import com.baidu.mapapi.map.MapStatusUpdate;
//import com.baidu.mapapi.map.MapStatusUpdateFactory;
//import com.baidu.mapapi.map.MapView;
//import com.baidu.mapapi.map.Marker;
//import com.baidu.mapapi.map.MarkerOptions;
//import com.baidu.mapapi.map.MyLocationData;
//import com.baidu.mapapi.map.OverlayOptions;
//import com.baidu.mapapi.model.LatLng;
//
//import java.util.ArrayList;
//import java.util.List;
//
//public class MainActivitytest extends AppCompatActivity
//        implements NavigationView.OnNavigationItemSelectedListener {
//
//    public LocationClient mLocationClient;
//
//    private TextView positionText;
//
//    private MapView mapView;
//
//    private BaiduMap baiduMap;
//
//    private boolean isFirstLocate = true;
//
//    private DrawerLayout mDrawerLayout;
//
//    private SearchView mSearchView;
//
//    private PointSet mPointSet;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        mLocationClient = new LocationClient(getApplicationContext());
//        mLocationClient.registerLocationListener(new MyLocationListener());
//        SDKInitializer.initialize(getApplicationContext());
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_main);
//        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);
//
////        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
////        fab.setOnClickListener(new View.OnClickListener() {
////            @Override
////            public void onClick(View view) {
////                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
////                        .setAction("Action", null).show();
////            }
////        });
//
////        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
////        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
////                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
////        drawer.addDrawerListener(toggle);
////        toggle.syncState();
////
////        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
////        navigationView.setNavigationItemSelectedListener(this);
//
//
//        SDKInitializer.initialize(getApplicationContext());
//        mapView = (MapView) findViewById(R.id.bmapView);
//        baiduMap = mapView.getMap();
//        baiduMap.setMyLocationEnabled(true);
////        positionText = (TextView) findViewById(R.id.position_text_view);
//        List<String> permissionList = new ArrayList<>();
//        if (ContextCompat.checkSelfPermission(MainActivitytest.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
//            permissionList.add(Manifest.permission.ACCESS_FINE_LOCATION);
//        }
//        if (ContextCompat.checkSelfPermission(MainActivitytest.this, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
//            permissionList.add(Manifest.permission.READ_PHONE_STATE);
//        }
//        if (ContextCompat.checkSelfPermission(MainActivitytest.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
//            permissionList.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
//        }
//        if (!permissionList.isEmpty()) {
//            String [] permissions = permissionList.toArray(new String[permissionList.size()]);
//            ActivityCompat.requestPermissions(MainActivitytest.this, permissions, 1);
//        } else {
//            requestLocation();
//        }
//
//
////        mPointSet = new PointSet();
////        printPoints(mPointSet.getAllPoints());
////        baiduMap.setOnMarkerClickListener(new BaiduMap.OnMarkerClickListener() {
////            @Override
////            public boolean onMarkerClick(Marker marker) {
////                Toast.makeText(MainActivitytest.this,  "被点击了！", Toast.LENGTH_SHORT).show();
////                return true;
////            }
////        });
//
//
////        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recycle_view);
////        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
////        recyclerView.setLayoutManager(layoutManager);
////        InformationAdapter adapter = new InformationAdapter(mPointSet.getAllPoints());
////        recyclerView.setAdapter(adapter);
//    }
//
//    @Override
//    public void onBackPressed() {
//        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
//        if (drawer.isDrawerOpen(GravityCompat.START)) {
//            drawer.closeDrawer(GravityCompat.START);
//        } else {
//            super.onBackPressed();
//        }
//    }
//
//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.main, menu);
//        return true;
//    }
//
//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        // Handle action bar item clicks here. The action bar will
//        // automatically handle clicks on the Home/Up button, so long
//        // as you specify a parent activity in AndroidManifest.xml.
//        int id = item.getItemId();
//
//        //noinspection SimplifiableIfStatement
////        if (id == R.id.action_settings) {
////            return true;
////        }
//
//        return super.onOptionsItemSelected(item);
//    }
//
//    @SuppressWarnings("StatementWithEmptyBody")
//    @Override
//    public boolean onNavigationItemSelected(MenuItem item) {
//        // Handle navigation view item clicks here.
//        int id = item.getItemId();
//
//        if (id == R.id.nav_camera) {
//            // Handle the camera action
//        } else if (id == R.id.nav_gallery) {
//
//        } else if (id == R.id.nav_slideshow) {
//
//        } else if (id == R.id.nav_manage) {
//
//        } else if (id == R.id.nav_share) {
//
//        } else if (id == R.id.nav_send) {
//
//        }
//
//        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
//        drawer.closeDrawer(GravityCompat.START);
//        return true;
//    }
//
//    private void navigateTo(BDLocation location) {
//        if (isFirstLocate) {
//            Toast.makeText(this, "nav to " + location.getAddrStr(), Toast.LENGTH_SHORT).show();
//            LatLng ll = new LatLng(location.getLatitude(), location.getLongitude());
//            MapStatusUpdate update = MapStatusUpdateFactory.newLatLng(ll);
//            baiduMap.animateMapStatus(update);
//            update = MapStatusUpdateFactory.zoomTo(16f);
////            baiduMap.animateMapStatus(update);
//            isFirstLocate = false;
//        }
//        MyLocationData.Builder locationBuilder = new MyLocationData.
//                Builder();
//        locationBuilder.latitude(location.getLatitude());
//        locationBuilder.longitude(location.getLongitude());
//        MyLocationData locationData = locationBuilder.build();
//        baiduMap.setMyLocationData(locationData);
//    }
//
//    private void requestLocation() {
//        initLocation();
//        mLocationClient.start();
//    }
//
//    private void initLocation(){
//        LocationClientOption option = new LocationClientOption();
//        option.setScanSpan(5000);
//        option.setIsNeedAddress(true);
//        mLocationClient.setLocOption(option);
//    }
//
//    @Override
//    protected void onResume() {
//        super.onResume();
//        mapView.onResume();
//    }
//
//    @Override
//    protected void onPause() {
//        super.onPause();
//        mapView.onPause();
//    }
//
//    @Override
//    protected void onDestroy() {
//        super.onDestroy();
//        mLocationClient.stop();
//        mapView.onDestroy();
//        baiduMap.setMyLocationEnabled(false);
//    }
//
//    @Override
//    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
//        switch (requestCode) {
//            case 1:
//                if (grantResults.length > 0) {
//                    for (int result : grantResults) {
//                        if (result != PackageManager.PERMISSION_GRANTED) {
//                            Toast.makeText(this, "必须同意所有权限才能使用本程序", Toast.LENGTH_SHORT).show();
//                            finish();
//                            return;
//                        }
//                    }
//                    requestLocation();
//                } else {
//                    Toast.makeText(this, "发生未知错误", Toast.LENGTH_SHORT).show();
//                    finish();
//                }
//                break;
//            default:
//        }
//    }
//
//    public class MyLocationListener implements BDLocationListener {
//
//        @Override
//        public void onReceiveLocation(BDLocation location) {
////            StringBuilder currentPosition = new StringBuilder();
////            currentPosition.append("纬度：").append(location.getLatitude()).append("\n");
////            currentPosition.append("经线：").append(location.getLongitude()).append("\n");
////            currentPosition.append("国家：").append(location.getCountry()).append("\n");
////            currentPosition.append("省：").append(location.getProvince()).append("\n");
////            currentPosition.append("市：").append(location.getCity()).append("\n");
////            currentPosition.append("区：").append(location.getDistrict()).append("\n");
////            currentPosition.append("街道：").append(location.getStreet()).append("\n");
////            currentPosition.append("定位方式：");
////            if (location.getLocType() == BDLocation.TypeGpsLocation) {
////                currentPosition.append("GPS");
////            } else if (location.getLocType() == BDLocation.TypeNetWorkLocation) {
////                currentPosition.append("网络");
////            }
////            positionText.setText(currentPosition);
//            if (location.getLocType() == BDLocation.TypeGpsLocation
//                    || location.getLocType() == BDLocation.TypeNetWorkLocation) {
//                navigateTo(location);
//            }
//        }
//
//    }
//
//    private void printPoints(List<Point> points) {
//
//        for (int i = 0; i < points.size(); i++) {
//            LatLng bPoint = new LatLng(points.get(i).x, points.get(i).y);
//            BitmapDescriptor bitmap = BitmapDescriptorFactory
//                    .fromResource(R.drawable.icon_gcoding);
//
//            OverlayOptions option = new MarkerOptions()
//                    .position(bPoint)
//                    .icon(bitmap);//在地图上添加Marker，并显示
//
//            Marker marker = (Marker) baiduMap.addOverlay(option);
//            Bundle bundle = new Bundle();
//            bundle.putInt("ID", i);
//            marker.setExtraInfo(bundle);
//        }
//    }
//}
