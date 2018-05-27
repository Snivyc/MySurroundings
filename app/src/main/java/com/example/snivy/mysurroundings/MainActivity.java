package com.example.snivy.mysurroundings;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;

import android.os.Bundle;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.SDKInitializer;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.LogoPosition;
import com.baidu.mapapi.map.MapPoi;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.Marker;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.navi.BaiduMapAppNotSupportNaviException;
import com.baidu.mapapi.navi.BaiduMapNavigation;
import com.baidu.mapapi.navi.NaviParaOption;
import com.baidu.mapapi.utils.route.BaiduMapRoutePlan;
import com.baidu.mapapi.utils.route.RouteParaOption;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends BaseActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    public LocationClient mLocationClient;

//    private TextView positionText;

    private MapView mapView;

    private BaiduMap baiduMap;

    private boolean isFirstLocate = true;

    private DrawerLayout mDrawerLayout;

    private SearchView mSearchView;

    private PointSet mPointSet;

    private View mBottomLayout;
    public BottomSheetBehavior mBottomSheetBehavior;

    public int clickedPointID;

    private boolean isPointClicked;

    private RecyclerView mRecyclerView;

    public MyApp myApp;

    private ArrayList<Marker> markersList;

    private BitmapDescriptor bitmap;

    private BitmapDescriptor bitmapI;

    private NavigationView navigationView;

    @Override
    protected void onStart() {
        super.onStart();
        navigationView.setCheckedItem(R.id.nav_home);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        mLocationClient = new LocationClient(getApplicationContext());
        mLocationClient.registerLocationListener(new MyLocationListener());
        SDKInitializer.initialize(getApplicationContext());
        myApp = (MyApp)getApplicationContext();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        //初始化fab按钮
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                BDLocation location= mLocationClient.getLastKnownLocation();
                LatLng ll = new LatLng(location.getLatitude(), location.getLongitude());
                MapStatusUpdate update = MapStatusUpdateFactory.newLatLng(ll);
                baiduMap.animateMapStatus(update);
            }
        });

        //初始化导航栏
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        //初始化navigationView
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        View headerView = navigationView.getHeaderView(0);
        TextView accountName = (TextView)headerView.findViewById(R.id.account_name);
        accountName.setText(myApp.getAccount());



//        SDKInitializer.initialize(getApplicationContext());
        //初始化百度地图
        mapView = (MapView) findViewById(R.id.bmapView);
        mapView.setLogoPosition(LogoPosition.logoPostionleftTop);
        mapView. showScaleControl(false);
        mapView. showZoomControls(false);
        baiduMap = mapView.getMap();
        baiduMap.setMyLocationEnabled(true);
        baiduMap.setOnMarkerClickListener(new BaiduMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                point_clicked(marker.getExtraInfo().getInt("ID"));
                return true;
            }
        });
        baiduMap.setOnMapClickListener(new BaiduMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                if (isPointClicked == true) {
                    undo_point_clicked();
                }
            }
            @Override
            public boolean onMapPoiClick(MapPoi mapPoi) {
                if (isPointClicked == true) {
                    undo_point_clicked();
                }
                return false;
            }
        });
//        positionText = (TextView) findViewById(R.id.position_text_view);


        //请求权限
        List<String> permissionList = new ArrayList<>();
        if (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            permissionList.add(Manifest.permission.ACCESS_FINE_LOCATION);
        }
        if (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
            permissionList.add(Manifest.permission.READ_PHONE_STATE);
        }
        if (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            permissionList.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
        }
        if (!permissionList.isEmpty()) {
            String [] permissions = permissionList.toArray(new String[permissionList.size()]);
            ActivityCompat.requestPermissions(MainActivity.this, permissions, 1);
        } else {
            requestLocation();
        }


        View pointInfo = findViewById(R.id.point_info);
        pointInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                move_to_clicked_point();
            }
        });

        //初始化导航按钮
        View satnavButton = findViewById(R.id.point_satnav_button);
        satnavButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mBottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
            }
        });
        View carNavButton = findViewById(R.id.point_car_nav_button);
        carNavButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                BDLocation location= mLocationClient.getLastKnownLocation();
                LatLng myll = new LatLng(location.getLatitude(), location.getLongitude());
                Point point = mPointSet.getPoint(clickedPointID);
                LatLng toll = new LatLng(point.x, point.y);
                NaviParaOption para = new NaviParaOption()
                        .startPoint(myll).endPoint(toll);
                try {
                    BaiduMapNavigation.openBaiduMapNavi(para, MainActivity.this);
                } catch (BaiduMapAppNotSupportNaviException e) {
                    e.printStackTrace();
                }
            }
        });
        View busNavButton = findViewById(R.id.point_bus_nav_button);
        busNavButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                BDLocation location= mLocationClient.getLastKnownLocation();
                LatLng myll = new LatLng(location.getLatitude(), location.getLongitude());
                Point point = mPointSet.getPoint(clickedPointID);
                LatLng toll = new LatLng(point.x, point.y);
                RouteParaOption para = new RouteParaOption()
                        .startPoint(myll)
                        .endPoint(toll)
                        .busStrategyType(RouteParaOption.EBusStrategyType.bus_recommend_way);
                try {
                    BaiduMapRoutePlan.openBaiduMapTransitRoute(para, MainActivity.this);
                } catch (Exception e) {
                    e.printStackTrace();
                }

                //结束调启功能时调用finish方法以释放相关资源
                BaiduMapRoutePlan.finish(MainActivity.this);
            }
        });
        View walkNavButton = findViewById(R.id.point_walk_nav_button);
        walkNavButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                BDLocation location= mLocationClient.getLastKnownLocation();
                LatLng myll = new LatLng(location.getLatitude(), location.getLongitude());
                Point point = mPointSet.getPoint(clickedPointID);
                LatLng toll = new LatLng(point.x, point.y);
                NaviParaOption para = new NaviParaOption()
                        .startPoint(myll).endPoint(toll);
                try {
                    BaiduMapNavigation.openBaiduMapWalkNavi(para, MainActivity.this);
                } catch (BaiduMapAppNotSupportNaviException e) {
                    e.printStackTrace();
                }
            }
        });
        View bikeNavButton = findViewById(R.id.point_bike_nav_button);
        bikeNavButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                BDLocation location= mLocationClient.getLastKnownLocation();
                LatLng myll = new LatLng(location.getLatitude(), location.getLongitude());
                Point point = mPointSet.getPoint(clickedPointID);
                LatLng toll = new LatLng(point.x, point.y);
                NaviParaOption para = new NaviParaOption()
                        .startPoint(myll).endPoint(toll);
                try {
                    BaiduMapNavigation.openBaiduMapBikeNavi(para, MainActivity.this);
                } catch (BaiduMapAppNotSupportNaviException e) {
                    e.printStackTrace();
                }
            }
        });





        mBottomLayout = findViewById(R.id.bottom_sheet);
        //2.把这个底部菜单和一个BottomSheetBehavior关联起来
        mBottomSheetBehavior = BottomSheetBehavior.from(mBottomLayout);


        isPointClicked = false;
        clickedPointID = -1;


        //初始化RecyclerView
        mRecyclerView = (RecyclerView) findViewById(R.id.recycle_view);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.addItemDecoration(new DividerItemDecoration(this,DividerItemDecoration.VERTICAL){});


        //初始化marker相关
        markersList = new ArrayList<>();
        bitmap = BitmapDescriptorFactory
                .fromResource(R.drawable.icon_gcoding);
        bitmapI = BitmapDescriptorFactory
                .fromResource(R.drawable.icon_marki);

        //初始化mPointSet
        mPointSet = new PointSet(this);

    }

    public void move_to_clicked_point() {
        Point point = mPointSet.getPoint(clickedPointID);
        LatLng ll = new LatLng(point.x, point.y);
        MapStatusUpdate update = MapStatusUpdateFactory.newLatLngZoom(ll,16f);
        baiduMap.animateMapStatus(update);
    }

//    public void point_clicked(int ID) {
//        if (isPointClicked == false) {
////                    Toast.makeText(MainActivity.this,  "被点击了！", Toast.LENGTH_SHORT).show();
//            Marker marker = markersList.get(ID);
//            marker.setIcon(bitmapI);
//            isPointClicked = true;
//            mBottomSheetBehavior.setHideable(true);
//            mBottomSheetBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);
//            View tView = findViewById(R.id.point_info_layout);
//            tView.setVisibility(View.VISIBLE);
//            tView = findViewById(R.id.recycle_view);
//            tView.setVisibility(View.GONE);
//            tView = findViewById(R.id.show_list);
//            tView.setVisibility(View.GONE);
//            TextView textView = findViewById(R.id.point_info);
//            textView.setText(mPointSet.getPoint(clickedPointID).getInformation());
//            mBottomSheetBehavior.setPeekHeight(500);
//            mBottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
//            mBottomSheetBehavior.setHideable(false);
//        } else {
//            TextView textView = findViewById(R.id.point_info);
//            textView.setText(mPointSet.getPoint(clickedPointID).getInformation());
//        }
//        clickedPointID = ID;
//    }

    public void point_clicked(int tempID) {
        Marker marker = markersList.get(tempID);
        if (clickedPointID == tempID) {
            LatLng position = marker.getPosition();
            MapStatusUpdate update = MapStatusUpdateFactory.newLatLng(position);
            baiduMap.animateMapStatus(update);
        } else {
            marker.setIcon(bitmapI);
            TextView textView = findViewById(R.id.point_info);
            textView.setText(mPointSet.getPoint(tempID).getInformation());
            textView = findViewById(R.id.point_distance);
            textView.setText(mPointSet.getPoint(tempID).getDistance()+"m");
            if (!isPointClicked) {
//                    Toast.makeText(MainActivity.this,  "被点击了！", Toast.LENGTH_SHORT).show();
                isPointClicked = true;

                View tView = findViewById(R.id.point_info_layout);
                tView.setVisibility(View.VISIBLE);
                tView = findViewById(R.id.recycle_view);
                tView.setVisibility(View.GONE);
                tView = findViewById(R.id.show_list);
                tView.setVisibility(View.GONE);

                float scale = MainActivity.this.getResources().getDisplayMetrics().density;
                mBottomSheetBehavior.setPeekHeight((int)(50 * scale + 0.5f));
            } else {

                marker = markersList.get(clickedPointID);
                marker.setIcon(bitmap);
            }
            clickedPointID = tempID;
        }
    }


    public void undo_point_clicked(){
        if (isPointClicked) {
            isPointClicked = false;
            Marker marker = markersList.get(clickedPointID);
            marker.setIcon(bitmap);
            clickedPointID = -1;
            float scale = MainActivity.this.getResources().getDisplayMetrics().density;
            mBottomSheetBehavior.setPeekHeight((int) (50 * scale + 0.5f));
            mBottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
            View tView = findViewById(R.id.show_list);
            tView.setVisibility(View.VISIBLE);
            tView = findViewById(R.id.point_info_layout);
            tView.setVisibility(View.GONE);
            tView = findViewById(R.id.recycle_view);
            tView.setVisibility(View.VISIBLE);
        } else {
            mBottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
        }
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else if (isPointClicked == true) {
            undo_point_clicked();
        } else if (mBottomSheetBehavior.getState() == BottomSheetBehavior.STATE_EXPANDED) {
            mBottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        MenuItem searchItem = menu.findItem(R.id.menu_search);
        //通过MenuItem得到SearchView
        mSearchView = (SearchView) MenuItemCompat.getActionView(searchItem);
        mSearchView.setOnCloseListener(new SearchView.OnCloseListener() {
            @Override
            public boolean onClose() {
                mPointSet.reflashAllPoints();
                return false;
            }
        });
        mSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                mPointSet.reflashAllPoints(s);
//                if (mSearchView != null) {
//                    // 得到输入管理对象
//                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
//                    if (imm != null) {
//                        // 这将让键盘在所有的情况下都被隐藏，但是一般我们在点击搜索按钮后，输入法都会乖乖的自动隐藏的。
//                        imm.hideSoftInputFromWindow(mSearchView.getWindowToken(), 0); // 输入法如果是显示状态，那么就隐藏输入法
//                    }
                    mSearchView.clearFocus(); // 不获取焦点
//                }
                return false;
            }
            @Override
            public boolean onQueryTextChange(String s) {
                return true;
            }
        });
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
//        if (id == R.id.action_settings) {
//            return true;
//        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        if (id == R.id.nav_quit) {
            Intent intent = new Intent("com.example.snivy.mysurroundings.FORCE_OFFLINE");
            sendBroadcast(intent);
        } else if (id == R.id.nav_submit) {
            Intent intent = new Intent(MainActivity.this, SubmitInfoActivity.class);
            BDLocation location= mLocationClient.getLastKnownLocation();
            LatLng ll = new LatLng(location.getLatitude(), location.getLongitude());
            intent.putExtra("x", ll.latitude);
            intent.putExtra("y", ll.longitude);
            startActivity(intent);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void navigateTo(BDLocation location) {
        if (isFirstLocate) {
            LatLng ll = new LatLng(location.getLatitude(), location.getLongitude());
            MapStatusUpdate update = MapStatusUpdateFactory.newLatLngZoom(ll,15f);
            baiduMap.animateMapStatus(update);
            mPointSet.reflashAllPoints();
            isFirstLocate = false;
        }
        MyLocationData.Builder locationBuilder = new MyLocationData.
                Builder();
        locationBuilder.latitude(location.getLatitude());
        locationBuilder.longitude(location.getLongitude());
        MyLocationData locationData = locationBuilder.build();
        baiduMap.setMyLocationData(locationData);
    }

    private void requestLocation() {
        initLocation();
        mLocationClient.start();
    }

    private void initLocation(){
        LocationClientOption option = new LocationClientOption();
        option.setScanSpan(5000);
        option.setCoorType("bd09ll");
        mLocationClient.setLocOption(option);
    }

    @Override
    protected void onResume() {
        super.onResume();
        isFirstLocate = true;
        mapView.onResume();
//        BDLocation location= mLocationClient.getLastKnownLocation();
//        LatLng ll = new LatLng(location.getLatitude(), location.getLongitude());
//        MapStatusUpdate update = MapStatusUpdateFactory.newLatLng(ll);
//        baiduMap.animateMapStatus(update);

    }

    @Override
    protected void onPause() {
        super.onPause();
        mapView.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mLocationClient.stop();
        mapView.onDestroy();
        baiduMap.setMyLocationEnabled(false);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case 1:
                if (grantResults.length > 0) {
                    for (int result : grantResults) {
                        if (result != PackageManager.PERMISSION_GRANTED) {
                            Toast.makeText(this, "必须同意所有权限才能使用本程序", Toast.LENGTH_SHORT).show();
                            finish();
                            return;
                        }
                    }
                    requestLocation();
                } else {
                    Toast.makeText(this, "发生未知错误", Toast.LENGTH_SHORT).show();
                    finish();
                }
                break;
            default:
        }
    }

    public class MyLocationListener implements BDLocationListener {
        @Override
        public void onReceiveLocation(BDLocation location) {
            if (location.getLocType() == BDLocation.TypeGpsLocation
                    || location.getLocType() == BDLocation.TypeNetWorkLocation) {
                navigateTo(location);
            }
        }
    }

    public void printPoints() {
        markersList.clear();
        baiduMap.clear();
        List<Point> points = mPointSet.getAllPoints();

        for (int i = 0; i < points.size(); i++) {
            LatLng bPoint = new LatLng(points.get(i).x, points.get(i).y);
            OverlayOptions option = new MarkerOptions()
                    .position(bPoint)
                    .icon(bitmap);//在地图上添加Marker，并显示

            Marker marker = (Marker) baiduMap.addOverlay(option);
            markersList.add(marker);
            Bundle bundle = new Bundle();
            bundle.putInt("ID", i);
            marker.setExtraInfo(bundle);
        }

        InformationAdapter adapter = new InformationAdapter(mPointSet.getAllPoints(), mLocationClient,this);
        mRecyclerView.setAdapter(adapter);
    }
}
