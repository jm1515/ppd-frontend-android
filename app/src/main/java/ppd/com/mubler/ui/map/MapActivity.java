package ppd.com.mubler.ui.map;

import android.app.Activity;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.android.material.navigation.NavigationView;
import com.mapbox.android.core.permissions.PermissionsListener;
import com.mapbox.android.core.permissions.PermissionsManager;
import com.mapbox.api.directions.v5.models.DirectionsResponse;
import com.mapbox.api.directions.v5.models.DirectionsRoute;
import com.mapbox.api.geocoding.v5.models.CarmenFeature;
import com.mapbox.geojson.Feature;
import com.mapbox.geojson.FeatureCollection;
import com.mapbox.geojson.Point;
import com.mapbox.mapboxsdk.Mapbox;
import com.mapbox.mapboxsdk.camera.CameraPosition;
import com.mapbox.mapboxsdk.camera.CameraUpdateFactory;
import com.mapbox.mapboxsdk.geometry.LatLng;
import com.mapbox.mapboxsdk.location.LocationComponent;
import com.mapbox.mapboxsdk.location.modes.CameraMode;
import com.mapbox.mapboxsdk.location.modes.RenderMode;
import com.mapbox.mapboxsdk.maps.MapView;
import com.mapbox.mapboxsdk.maps.MapboxMap;
import com.mapbox.mapboxsdk.maps.OnMapReadyCallback;
import com.mapbox.mapboxsdk.maps.Style;
import com.mapbox.mapboxsdk.plugins.places.autocomplete.PlaceAutocomplete;
import com.mapbox.mapboxsdk.plugins.places.autocomplete.model.PlaceOptions;
import com.mapbox.mapboxsdk.style.layers.SymbolLayer;
import com.mapbox.mapboxsdk.style.sources.GeoJsonSource;
import com.mapbox.services.android.navigation.ui.v5.NavigationLauncher;
import com.mapbox.services.android.navigation.ui.v5.NavigationLauncherOptions;
import com.mapbox.services.android.navigation.ui.v5.route.NavigationMapRoute;
import com.mapbox.services.android.navigation.v5.navigation.NavigationRoute;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import ppd.com.mubler.R;
import ppd.com.mubler.data.APIClient;
import ppd.com.mubler.data.APIInterface;
import ppd.com.mubler.data.entity.Request;
import ppd.com.mubler.ui.BaseActivity;
import ppd.com.mubler.ui.request_recap.RequestRecapActivity;
import ppd.com.mubler.ui.user.UserSession;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.iconImage;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.iconOffset;

public class MapActivity extends BaseActivity implements OnMapReadyCallback,PermissionsListener {

    private static final int REQUEST_CODE_AUTOCOMPLETE = 1;
    private LinearLayout layoutButtonsOffer;
    private Button buttonCreateOffer;
    private ImageButton buttonDeleteOffer;
    private MapView mapView;
    private PermissionsManager permissionsManager;
    private MapboxMap mapboxMap;
    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private String geojsonSourceLayerId = "geojsonSourceLayerId";
    private String symbolIconId = "symbolIconId";
    private DirectionsRoute currentRoute;
    private Point origin, destination;
    private NavigationMapRoute navigationMapRoute;
    private SymbolLayer symbolLayerDestination;
    private ImageView s_image, l_image, xl_image;
    private String selectedTransport;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Mapbox.getInstance(this, getString(R.string.map_access_token));
        setContentView(R.layout.map_layout);

        layoutButtonsOffer = findViewById(R.id.layoutButtonsOffer);
        buttonCreateOffer = findViewById(R.id.buttonCreateOffer);
        buttonDeleteOffer = findViewById(R.id.buttonDeleteOffer);
        mapView = findViewById(R.id.map_view);

        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("");
        setDrawer(drawerLayout, navigationView, toolbar);

        mapView.onCreate(savedInstanceState);
        mapView.getMapAsync(this);

        buttonDeleteOffer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setActivityComponent();

            }
        });
        buttonCreateOffer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createRequest();
            }
        });

        s_image = findViewById(R.id.s_button);
        s_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.i("TAG", "S selected");
                selectedTransport = "S";
                s_image.setBackground(getResources().getDrawable(R.drawable.circle_selected_type));
                l_image.setBackground(getResources().getDrawable(R.drawable.circle));
                xl_image.setBackground(getResources().getDrawable(R.drawable.circle));
            }
        });
        l_image = findViewById(R.id.l_button);
        l_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.i("TAG", "L selected");
                selectedTransport = "L";
                l_image.setBackground(getResources().getDrawable(R.drawable.circle_selected_type));
                s_image.setBackground(getResources().getDrawable(R.drawable.circle));
                xl_image.setBackground(getResources().getDrawable(R.drawable.circle));
            }
        });
        xl_image = findViewById(R.id.xl_button);
        xl_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.i("TAG", "XL selected");
                selectedTransport = "XL";
                xl_image.setBackground(getResources().getDrawable(R.drawable.circle_selected_type));
                s_image.setBackground(getResources().getDrawable(R.drawable.circle));
                l_image.setBackground(getResources().getDrawable(R.drawable.circle));
            }
        });
    }

    private void setActivityComponent() {
        s_image.setBackground(getResources().getDrawable(R.drawable.circle));
        l_image.setBackground(getResources().getDrawable(R.drawable.circle));
        xl_image.setBackground(getResources().getDrawable(R.drawable.circle));
        layoutButtonsOffer.setVisibility(View.GONE);
        navigationMapRoute.removeRoute();
        mapboxMap.getStyle().removeLayer(symbolLayerDestination);
        returnToOriginPosition();
        buttonDeleteOffer.setVisibility(View.INVISIBLE);
        findViewById(R.id.fab_location_search).setVisibility(View.VISIBLE);
    }

    private void returnToOriginPosition(){
        mapboxMap.animateCamera(CameraUpdateFactory.newCameraPosition(
                new CameraPosition.Builder()
                        .target(new LatLng((origin.latitude()),
                                (origin.longitude())))
                        .zoom(14)
                        .build()), 4000);
    }

    @Override
    public void onMapReady(@NonNull final MapboxMap mapboxMap) {
        this.mapboxMap = mapboxMap;

        initMap(mapboxMap);
    }

    private void initMap(@NonNull MapboxMap mapboxMap) {
        mapboxMap.setStyle(Style.MAPBOX_STREETS, new Style.OnStyleLoaded() {
            @Override
            public void onStyleLoaded(@NonNull Style style) {

                enableLocationComponent(style);

                initSearchFab();

                // Add the symbol layer icon to map for future use
                style.addImage(symbolIconId, BitmapFactory.decodeResource(
                        MapActivity.this.getResources(), R.drawable.blue_marker_view));

                // Create an empty GeoJSON source using the empty feature collection
                setUpSource(style);

            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                drawerLayout.openDrawer(GravityCompat.START);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings( {"MissingPermission"})
    private void enableLocationComponent(@NonNull Style loadedMapStyle) {
        // Check if permissions are enabled and if not request
        if (PermissionsManager.areLocationPermissionsGranted(this)) {

            // Get an instance of the component
            LocationComponent locationComponent = mapboxMap.getLocationComponent();

            // Activate with options
            locationComponent.activateLocationComponent(this, loadedMapStyle);

            // Enable to make component visible
            locationComponent.setLocationComponentEnabled(true);

            // Set the component's camera mode
            locationComponent.setCameraMode(CameraMode.TRACKING);

            // Set the component's render mode
            locationComponent.setRenderMode(RenderMode.COMPASS);

            this.origin = Point.fromLngLat(this.mapboxMap.getLocationComponent().getLastKnownLocation().getLongitude(),
                    this.mapboxMap.getLocationComponent().getLastKnownLocation().getLatitude());
        } else {
            permissionsManager = new PermissionsManager(this);
            permissionsManager.requestLocationPermissions(this);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        permissionsManager.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    public void onExplanationNeeded(List<String> permissionsToExplain) {
        Toast.makeText(this, R.string.map_user_location_permission_explanation, Toast.LENGTH_LONG).show();
    }

    @Override
    public void onPermissionResult(boolean granted) {
        if (granted) {
            mapboxMap.setStyle(new Style.Builder().fromUrl(Style.MAPBOX_STREETS), new Style.OnStyleLoaded() {
                @Override
                public void onStyleLoaded(@NonNull Style style) {
                    enableLocationComponent(style);
                }
            });
        } else {
            Toast.makeText(this, R.string.map_user_location_permission_not_granted, Toast.LENGTH_LONG).show();
            finish();
        }
    }

    private void initSearchFab() {
        findViewById(R.id.fab_location_search).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new PlaceAutocomplete.IntentBuilder()
                        .accessToken(getString(R.string.map_access_token))
                        .placeOptions(PlaceOptions.builder()
                                .backgroundColor(Color.parseColor("#EEEEEE"))
                                .limit(10)
                                .build(PlaceOptions.MODE_CARDS))
                        .build(MapActivity.this);
                startActivityForResult(intent, REQUEST_CODE_AUTOCOMPLETE);
            }
        });
        returnToOriginPosition();
    }

    private void setUpSource(@NonNull Style loadedMapStyle) {
        loadedMapStyle.addSource(new GeoJsonSource(geojsonSourceLayerId));
    }

    private void setupLayer(@NonNull Style loadedMapStyle) {
        symbolLayerDestination = new SymbolLayer("SYMBOL_LAYER_ID", geojsonSourceLayerId).withProperties(
                iconImage(symbolIconId),
                iconOffset(new Float[] {0f, -8f})
        );
        loadedMapStyle.addLayer(symbolLayerDestination);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK && requestCode == REQUEST_CODE_AUTOCOMPLETE) {

            // Set up a new symbol layer for displaying the searched location's feature coordinates
            setupLayer(this.mapboxMap.getStyle());

            // Retrieve selected location's CarmenFeature
            CarmenFeature selectedCarmenFeature = PlaceAutocomplete.getPlace(data);

            // Create a new FeatureCollection and add a new Feature to it using selectedCarmenFeature above.
            // Then retrieve and update the source designated for showing a selected location's symbol layer icon

            if (mapboxMap != null) {
                Style style = mapboxMap.getStyle();
                if (style != null) {
                    GeoJsonSource source = style.getSourceAs(geojsonSourceLayerId);
                    if (source != null) {
                        source.setGeoJson(FeatureCollection.fromFeatures(
                                new Feature[] {Feature.fromJson(selectedCarmenFeature.toJson())}));
                    }

                    // Move map camera to the selected location
                    mapboxMap.animateCamera(CameraUpdateFactory.newCameraPosition(
                            new CameraPosition.Builder()
                                    .target(new LatLng(((Point) selectedCarmenFeature.geometry()).latitude(),
                                            ((Point) selectedCarmenFeature.geometry()).longitude()))
                                    .zoom(14)
                                    .build()), 4000);
                }
                this.destination = Point.fromLngLat(((Point)selectedCarmenFeature.geometry()).longitude(),
                        ((Point) selectedCarmenFeature.geometry()).latitude());
                findViewById(R.id.fab_location_search).setVisibility(View.INVISIBLE);
                layoutButtonsOffer.setVisibility(View.VISIBLE);
                buttonDeleteOffer.setVisibility(View.VISIBLE);
                getRoute(origin, destination);
            }
        }

    }

    /**
     * Make a request to the Mapbox Directions API. Once successful, pass the route to the
     * route layer.
     *
     * @param origin      the starting point of the route
     * @param destination the desired finish point of the route
     */
    private void getRoute(Point origin, Point destination) {

        NavigationRoute.builder(this)
                .accessToken(getString(R.string.map_access_token))
                .origin(origin)
                .destination(destination)
                .build()
                .getRoute(new Callback<DirectionsResponse>() {
                    @Override
                    public void onResponse(Call<DirectionsResponse> call, Response<DirectionsResponse> response) {
                        currentRoute = response.body().routes().get(0);

                        if(navigationMapRoute != null){
                            navigationMapRoute.removeRoute();
                        }
                        else{
                            navigationMapRoute = new NavigationMapRoute(null, mapView, mapboxMap);
                        }
                        navigationMapRoute.addRoute(currentRoute);
                    }

                    @Override
                    public void onFailure(Call<DirectionsResponse> call, Throwable t) {

                    }
                });
    }


    @Override
    @SuppressWarnings( {"MissingPermission"})
    public void onStart() {
        super.onStart();
        mapView.onStart();
    }

    @Override
    public void onResume() {
        super.onResume();
        mapView.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        mapView.onPause();
    }

    @Override
    public void onStop() {
        super.onStop();
        mapView.onStop();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mapView.onLowMemory();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mapView.onSaveInstanceState(outState);
    }

    private void createRequest(){
        if (this.selectedTransport == null){
            return;
        }
        APIInterface apiInterface = APIClient.getClient().create(APIInterface.class);
        UserSession userSession = UserSession.getInstance();
        Call<Request> call = apiInterface.postRequest(userSession.getToken(), new Request(0.0f, 0.0f, 0.0f, 0.0f, this.selectedTransport));
        call.enqueue(new retrofit2.Callback<Request>() {
            @Override
            public void onResponse(Call<Request> call, retrofit2.Response<Request> response) {
                Log.i("TAG", "Server response for setUserSession http return code : " + response.code());

                if (response.code()  != 201){
                    call.cancel();
                    Log.e("TAG", "post request call failed ! " + response.code());
                    return;
                }
                selectedTransport = null;
                Request request = response.body();
                if (request == null) {
                    Log.e("TAG", "error parsing response");
                    return;
                }
                goToRecap(request);
            }

            @Override
            public void onFailure(Call<Request> call, Throwable t) {
                call.cancel();
                Log.e("TAG", "post request fail !" + t.getMessage());
                return;
            }
        });
    }

    private void goToRecap(Request request){
        Intent intentRequestRecap = new Intent(this, RequestRecapActivity.class);
        intentRequestRecap.putExtra("request_size", request.getVehiculeTypeName());
        intentRequestRecap.putExtra("request_price", request.getPrice());
        startActivity(intentRequestRecap);
    }
}
