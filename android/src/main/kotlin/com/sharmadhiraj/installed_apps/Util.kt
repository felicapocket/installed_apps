package com.sharmadhiraj.installed_apps

import android.content.Context
import android.content.pm.ApplicationInfo
import android.content.pm.PackageInfo
import android.content.pm.PackageManager
import android.os.Build.VERSION.SDK_INT
import android.os.Build.VERSION_CODES.P
import java.io.File

class Util {
    companion object {
        fun convertAppToMap(
            packageManager: PackageManager,
            app: ApplicationInfo,
            withIcon: Boolean,
            platformType: PlatformType?,
        ): HashMap<String, Any?> {
            val map = HashMap<String, Any?>()
            try {
                val packageInfo = packageManager.getPackageInfo(app.packageName, 0)
                val builtWith = platformType?.value ?: packageInfo.applicationInfo?.let {
                    BuiltWithUtil.getPlatform(it)
                }
                val installedTimestamp = packageInfo.applicationInfo?.sourceDir?.let {
                    File(it).lastModified()
                }
                map["name"] = packageManager.getApplicationLabel(app)
                map["package_name"] = app.packageName
                map["icon"] =
                    if (withIcon) DrawableUtil.drawableToByteArray(app.loadIcon(packageManager))
                    else ByteArray(0)
                map["version_name"] = packageInfo.versionName
                map["version_code"] = getVersionCode(packageInfo)
                map["built_with"] = builtWith
                map["installed_timestamp"] = installedTimestamp
                return map
            } catch (t: Throwable) {
                return map
            }
        }

        fun getPackageManager(context: Context): PackageManager {
            return context.packageManager
        }

        @Suppress("DEPRECATION")
        private fun getVersionCode(packageInfo: PackageInfo): Long {
            return if (SDK_INT < P) packageInfo.versionCode.toLong()
            else packageInfo.longVersionCode
        }
    }
}