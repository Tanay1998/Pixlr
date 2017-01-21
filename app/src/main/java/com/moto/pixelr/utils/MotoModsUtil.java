package com.moto.pixelr.utils;

import android.content.Context;
import com.moto.pixelr.R;
import com.motorola.mod.ModManager;

/**
 * Created by Michael Yoon Huh on 1/21/2017.
 *
 * Reference: https://www.element14.com/community/groups/moto-mods/blog/2016/08/08/moto-mods-quick-start-guide
 */

public class MotoModsUtil {

    /**
     * Verifies that the Moto Mod service is installed and enabled on this to determine
     * whether the device supports Moto Mods or not. Status can be one of the following
     * values: SUCCESS, SERVICE_MISSING, SERVICE_UPDATING, SERVICE_VERSION_UPDATE_REQUIRED,
     * SERVICE_DISABLED, SERVICE_INVALID.
     */
    public static String isDeviceMotoModReady(Context context) {

        int service = ModManager.isModServicesAvailable(context);
        String status;
        switch (service) {
            case ModManager.SUCCESS:
                status = "";
                break;
            case ModManager.SERVICE_MISSING:
                status = context.getString(R.string.SERVICE_MISSING);
                break;
            case ModManager.SERVICE_UPDATING:
                status = context.getString(R.string.SERVICE_UPDATING);
                break;
            case ModManager.SERVICE_VERSION_UPDATE_REQUIRED:
                status = context.getString(R.string.SERVICE_VERSION_UPDATE_REQUIRED);
                break;
            case ModManager.SERVICE_DISABLED:
                status = context.getString(R.string.SERVICE_DISABLED);
                break;
            case ModManager.SERVICE_INVALID:
                status = context.getString(R.string.SERVICE_INVALID);
                break;
            default:
                status = context.getString(R.string.SERVICE_NOT_AVAILABLE);
                break;
        }
        return status;
    }
}
