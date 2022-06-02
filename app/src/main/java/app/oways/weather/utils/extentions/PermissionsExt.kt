package app.oways.weather.utils.extentions

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment

private fun Context.getAccessFineLocationPermission() = ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)

private fun Context.getAccessCoarseLocationPermission() = ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)

fun Context.areAccessFineAndCoarseLocationPermissionsGranted() = getAccessFineLocationPermission() == PackageManager.PERMISSION_GRANTED && getAccessCoarseLocationPermission() == PackageManager.PERMISSION_GRANTED

fun Fragment.shouldShowLocationPermissionsRationaleDialog() = !shouldShowRequestPermissionRationale(
    Manifest.permission.ACCESS_FINE_LOCATION) || !shouldShowRequestPermissionRationale(Manifest.permission.ACCESS_COARSE_LOCATION)

fun getLocationPermissionArray() = if (isAndroid10Running())
    arrayOf(Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_BACKGROUND_LOCATION)
else
    arrayOf(Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION)

private fun isAndroid10Running() = Build.VERSION.SDK_INT == Build.VERSION_CODES.Q

fun isAndroid11Running() = Build.VERSION.SDK_INT == Build.VERSION_CODES.R