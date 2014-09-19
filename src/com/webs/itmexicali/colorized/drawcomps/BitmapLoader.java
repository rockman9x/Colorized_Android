package com.webs.itmexicali.colorized.drawcomps;

import java.util.HashMap;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;

public class BitmapLoader {
	
	static HashMap<String,Bitmap> bitmaps=new HashMap<String,Bitmap>();

	
	/** load the origial Bitmap from resource ID and return it 
	 * @param ctx context to get the resource from its Id
	 * @param resId the id of the resource to resize
	 * @param saveOnCache if true, it will be kept for future faster access
	 * @return the requested Bitmap */
	public static Bitmap getImage(Context ctx, int resId, boolean saveOnCache) {
		String key = Integer.toString(resId);
		if (bitmaps.containsKey(key))
			return bitmaps.get(key);
		
        Bitmap BitmapOrg = BitmapFactory.decodeResource(ctx.getResources(),resId);
        
        bitmaps.put(key, BitmapOrg);
        return BitmapOrg;

    }
	
	/** load the origial Bitmap from resource ID, resize it and return it 
	 * @param ctx context to get the resource from its Id
	 * @param resId the id of the resource to resize
	 * @param saveOnCache if true, it will be kept for future faster access
	 * @param w the width size to be set
	 * @param h the height size to be set
	 * @return the requested bitmap resized*/
	public static Bitmap resizeImage(Context ctx, int resId, boolean saveOnCache ,float w, float h) {
		String key = Integer.toString(resId)+"_"+w+"_"+h;
		if (bitmaps.containsKey(key))
			return bitmaps.get(key);
		
        Bitmap BitmapOrg = BitmapFactory.decodeResource(ctx.getResources(),resId);
        
        bitmaps.put(key, resizeImage(BitmapOrg, w, h));
        return bitmaps.get(key);

    }
	
	/** Resize a resource image to be shown on canvas */
	public static Bitmap resizeImage(Bitmap BitmapOrg, float w, float h) {        
        int width = BitmapOrg.getWidth();
        int height = BitmapOrg.getHeight();
        float newWidth = w;
        float newHeight = h;

        // calculate the scale
        float scaleWidth = ((float) newWidth) / width;
        float scaleHeight = ((float) newHeight) / height;

        // create a matrix for the manipulation
        Matrix matrix = new Matrix();
        // resize the Bitmap
        matrix.postScale(scaleWidth, scaleHeight);
        // if you want to rotate the Bitmap
        // matrix.postRotate(45);

        // recreate the new Bitmap
        Bitmap resizedBitmap = Bitmap.createBitmap(BitmapOrg, 0, 0, width, height, matrix, true);

        // make a Drawable from Bitmap to allow to set the Bitmap
        // to the ImageView, ImageButton or what ever
        return resizedBitmap;

      }
}
