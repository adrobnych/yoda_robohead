package com.codegemz.elfi.coreapp.helper.building_instruction;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;

/**
 * Created by adrobnych on 8/14/15.
 */
public class BuildingInstructionHelper {
    private static String ELFI_SMALL_BI_URL = "http://elfirobotics.com/ELFi-small/Building%20Instructions%20%5BELFi-small%5D.html";
    private Context context;

    public BuildingInstructionHelper(Context ctx){
        this.context = ctx;
    }
    public void showInstruction(){
        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(ELFI_SMALL_BI_URL));
        context.startActivity(browserIntent);
    }
}
