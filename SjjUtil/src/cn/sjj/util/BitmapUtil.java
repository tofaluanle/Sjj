package cn.sjj.util;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;
import android.graphics.Canvas;
import android.graphics.LinearGradient;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Shader.TileMode;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.NinePatchDrawable;
import android.media.ExifInterface;
import android.util.TypedValue;
import android.view.View;

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileDescriptor;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import javax.microedition.khronos.egl.EGL10;
import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.egl.EGLContext;
import javax.microedition.khronos.egl.EGLDisplay;

public class BitmapUtil {

    public static int computeSampleSize(BitmapFactory.Options options, int minSideLength, int maxNumOfPixels) {
        int initialSize = computeInitialSampleSize(options, minSideLength, maxNumOfPixels);
        int roundedSize;
        if (initialSize <= 8) {
            roundedSize = 1;
            while (roundedSize < initialSize) {
                roundedSize <<= 1;
            }
        } else {
            roundedSize = (initialSize + 7) / 8 * 8;
        }

        return roundedSize;
    }

    private static int computeInitialSampleSize(BitmapFactory.Options options, int minSideLength, int maxNumOfPixels) {
        double w = options.outWidth;
        double h = options.outHeight;

        int lowerBound = (maxNumOfPixels == -1) ? 1 : (int) Math.ceil(Math.sqrt(w * h / maxNumOfPixels));
        int upperBound = (minSideLength == -1) ? 128 : (int) Math.min(Math.floor(w / minSideLength), Math.floor(h / minSideLength));

        if (upperBound < lowerBound) {
            // return the larger one when there is no overlapping zone.
            return lowerBound;
        }

        if ((maxNumOfPixels == -1) && (minSideLength == -1)) {
            return 1;
        } else if (minSideLength == -1) {
            return lowerBound;
        } else {
            return upperBound;
        }
    }

    /**
     * @param is
     * @param length         流的总长度
     * @param minSideLength  一般传-1
     * @param maxNumOfPixels
     * @param unit           用来指定maxNumOfPixels的单位，单位使用
     *                       {@link TypedValue}
     * @param context
     * @return
     */
    public static Bitmap tryGetBitmap(InputStream is, int length, int minSideLength, int maxNumOfPixels, int unit, Context context) {
        return tryGetBitmap(is, length, minSideLength, (int) TypedValue.applyDimension(unit, maxNumOfPixels, context.getResources().getDisplayMetrics()));
    }


    /**
     * @param is
     * @param length         流的总长度
     * @param minSideLength  一般传-1
     * @param maxNumOfPixels
     * @return
     */
    public static Bitmap tryGetBitmap(InputStream is, int length, int minSideLength, int maxNumOfPixels) {
        if (is == null) {
            return null;
        }
        BufferedInputStream bis = new BufferedInputStream(is);
        Bitmap bmp = null;
        try {
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inJustDecodeBounds = true;
            bis.mark(length);
            BitmapFactory.decodeStream(bis, null, options);
            bis.reset();
            options.inSampleSize = computeSampleSize(options, minSideLength, maxNumOfPixels);
            // Looger.i( "options.inSampleSize: " +
            // options.inSampleSize);
            try {
                options.inJustDecodeBounds = false;
                bmp = BitmapFactory.decodeStream(bis, null, options);
            } catch (OutOfMemoryError err) {
                err.printStackTrace();
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (null != bis) {
                try {
                    bis.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return bmp;
    }


    /**
     * @param imgFile
     * @param minSideLength  一般传-1
     * @param maxNumOfPixels
     * @param unit           用来指定maxNumOfPixels的单位，单位使用
     *                       {@link TypedValue}
     * @param context
     * @return
     */
    public static Bitmap tryGetBitmap(String imgFile, int minSideLength, int maxNumOfPixels, int unit, Context context) {
        return tryGetBitmap(imgFile, minSideLength, (int) TypedValue.applyDimension(unit, maxNumOfPixels, context.getResources().getDisplayMetrics()));
    }

    /**
     * 改变了BitmapFactory.decode的方式
     *
     * @param imgFile
     * @param minSideLength  一般传-1
     * @param maxNumOfPixels
     * @return
     */
    public static Bitmap tryGetBitmap(String imgFile, int minSideLength, int maxNumOfPixels) {
        // Looger.i( "可用内存：" +
        // MemoryManager.getAvailableInternalMemorySize());
        if (imgFile == null || imgFile.length() == 0)
            return null;
        FileInputStream fis = null;
        Bitmap bmp = null;
        try {
            fis = new FileInputStream(imgFile);
            FileDescriptor fd = fis.getFD();
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inJustDecodeBounds = true;
            // BitmapFactory.decodeFile(imgFile, options);
            BitmapFactory.decodeFileDescriptor(fd, null, options);
            options.inSampleSize = computeSampleSize(options, minSideLength,
                    maxNumOfPixels);
            // Looger.i( "options.inSampleSize: " +
            // options.inSampleSize);
            try {
                // 这里一定要将其设置回false，因为之前我们将其设置成了true
                // 设置inJustDecodeBounds为true后，decodeFile并不分配空间，即，BitmapFactory解码出来的Bitmap为Null,但可计算出原始图片的长度和宽度
                options.inJustDecodeBounds = false;
                bmp = BitmapFactory.decodeFileDescriptor(fd, null, options);
            } catch (OutOfMemoryError err) {
                err.printStackTrace();
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (null != fis) {
                try {
                    fis.close();
                    fis = null;
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return bmp;
    }

    /**
     * @param res
     * @param id
     * @param minSideLength  一般传-1
     * @param maxNumOfPixels
     * @param unit           用来指定maxNumOfPixels的单位，单位使用
     *                       {@link TypedValue}
     * @return
     */
    public static Bitmap tryGetBitmap(Resources res, int id, int minSideLength, int maxNumOfPixels, int unit) {
        return tryGetBitmap(res, id, minSideLength, (int) TypedValue.applyDimension(unit, maxNumOfPixels, res.getDisplayMetrics()));
    }

    /**
     * @param res
     * @param id
     * @param minSideLength  一般传-1
     * @param maxNumOfPixels
     * @return
     */
    public static Bitmap tryGetBitmap(Resources res, int id, int minSideLength, int maxNumOfPixels) {
        Bitmap bmp = null;
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        // BitmapFactory.decodeFile(imgFile, options);
        BitmapFactory.decodeResource(res, id, options);
        options.inSampleSize = computeSampleSize(options, minSideLength,
                maxNumOfPixels);
        // Looger.i( "options.inSampleSize: " +
        // options.inSampleSize);
        try {
            // 这里一定要将其设置回false，因为之前我们将其设置成了true
            // 设置inJustDecodeBounds为true后，decodeFile并不分配空间，即，BitmapFactory解码出来的Bitmap为Null,但可计算出原始图片的长度和宽度
            options.inJustDecodeBounds = false;
            bmp = BitmapFactory.decodeResource(res, id, options);
        } catch (OutOfMemoryError err) {
            err.printStackTrace();
        }
        return bmp;
    }

    public static Bitmap tryGetBitmap_old(String imgFile, int minSideLength, int maxNumOfPixels) {

        if (imgFile == null || imgFile.length() == 0)
            return null;
        try {
            FileDescriptor fd = new FileInputStream(imgFile).getFD();
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inJustDecodeBounds = true;
            // BitmapFactory.decodeFile(imgFile, options);
            BitmapFactory.decodeFileDescriptor(fd, null, options);
            options.inSampleSize = computeSampleSize(options, minSideLength,
                    maxNumOfPixels);
            try {
                // 这里一定要将其设置回false，因为之前我们将其设置成了true
                // 设置inJustDecodeBounds为true后，decodeFile并不分配空间，即，BitmapFactory解码出来的Bitmap为Null,但可计算出原始图片的长度和宽度
                options.inJustDecodeBounds = false;
                Bitmap bmp = BitmapFactory.decodeFile(imgFile, options);
                return bmp == null ? null : bmp;
            } catch (OutOfMemoryError err) {
                err.printStackTrace();
                return null;
            }

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

    }

    /**
     * 从view 得到图片
     *
     * @param view
     * @return
     */
    public static Bitmap getBitmapFromView(View view) {
        view.destroyDrawingCache();
        view.measure(View.MeasureSpec.makeMeasureSpec(0,
                View.MeasureSpec.UNSPECIFIED), View.MeasureSpec
                .makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));
        view.layout(0, 0, view.getMeasuredWidth(), view.getMeasuredHeight());
        view.setDrawingCacheEnabled(true);
        Bitmap bitmap = view.getDrawingCache(true);
        return bitmap;
    }

    public static Bitmap toRoundCorner(Bitmap bitmap, int pixels) {
        Bitmap output = Bitmap.createBitmap(bitmap.getWidth(),
                bitmap.getHeight(), Config.ARGB_8888);
        Canvas canvas = new Canvas(output);
        final int color = 0xff424242;
        final Paint paint = new Paint();
        final Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
        final RectF rectF = new RectF(rect);
        final float roundPx = pixels;
        paint.setAntiAlias(true);
        canvas.drawARGB(0, 0, 0, 0);
        paint.setColor(color);
        canvas.drawRoundRect(rectF, roundPx, roundPx, paint);
        paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));
        canvas.drawBitmap(bitmap, rect, rect, paint);
        return output;
    }

    // http://dyh7077063.iteye.com/blog/970672
    // ↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓

    public static byte[] Bitmap2Bytes(Bitmap bm) {
        return Bitmap2Bytes(bm, Bitmap.CompressFormat.PNG);
    }

    public static byte[] Bitmap2Bytes(Bitmap bm, Bitmap.CompressFormat format) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bm.compress(format, 100, baos);
        return baos.toByteArray();
    }

    public static Bitmap Bytes2Bimap(byte[] b) {
        if (b.length != 0) {
            return BitmapFactory.decodeByteArray(b, 0, b.length);
        } else {
            return null;
        }
    }

    public static Bitmap zoomBitmap(Bitmap bitmap, int width, int height) {
        int w = bitmap.getWidth();
        int h = bitmap.getHeight();
        Matrix matrix = new Matrix();
        float scaleWidth = ((float) width / w);
        float scaleHeight = ((float) height / h);
        matrix.postScale(scaleWidth, scaleHeight);
        Bitmap newbmp = Bitmap.createBitmap(bitmap, 0, 0, w, h, matrix, true);
        return newbmp;
    }

    public static Bitmap drawableToBitmap(Drawable drawable) {
        // 取 drawable 的长宽
        int w = drawable.getIntrinsicWidth();
        int h = drawable.getIntrinsicHeight();

        // 取 drawable 的颜色格式
        Bitmap.Config config = drawable.getOpacity() != PixelFormat.OPAQUE ? Bitmap.Config.ARGB_8888
                : Bitmap.Config.RGB_565;
        // 建立对应 bitmap
        Bitmap bitmap = Bitmap.createBitmap(w, h, config);
        // 建立对应 bitmap 的画布
        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0, 0, w, h);
        // 把 drawable 内容画到画布中
        drawable.draw(canvas);
        return bitmap;
    }

    /**
     * Drawable 转 bitmap
     *
     * @param drawable
     * @return
     */
    public static Bitmap drawable2Bitmap(Drawable drawable) {
        if (drawable instanceof BitmapDrawable) {
            return ((BitmapDrawable) drawable).getBitmap();
        } else if (drawable instanceof NinePatchDrawable) {
            Bitmap bitmap = Bitmap
                    .createBitmap(
                            drawable.getIntrinsicWidth(),
                            drawable.getIntrinsicHeight(),
                            drawable.getOpacity() != PixelFormat.OPAQUE ? Bitmap.Config.ARGB_8888
                                    : Bitmap.Config.RGB_565);
            Canvas canvas = new Canvas(bitmap);
            drawable.setBounds(0, 0, drawable.getIntrinsicWidth(),
                    drawable.getIntrinsicHeight());
            drawable.draw(canvas);
            return bitmap;
        } else {
            return null;
        }
    }

    public static Bitmap getRoundedCornerBitmap(Bitmap bitmap, float roundPx) {
        int w = bitmap.getWidth();
        int h = bitmap.getHeight();
        Bitmap output = Bitmap.createBitmap(w, h, Config.ARGB_8888);
        Canvas canvas = new Canvas(output);
        final int color = 0xff424242;
        final Paint paint = new Paint();
        final Rect rect = new Rect(0, 0, w, h);
        final RectF rectF = new RectF(rect);
        paint.setAntiAlias(true);
        canvas.drawARGB(0, 0, 0, 0);
        paint.setColor(color);
        canvas.drawRoundRect(rectF, roundPx, roundPx, paint);
        paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));
        canvas.drawBitmap(bitmap, rect, rect, paint);

        return output;
    }

    public static Bitmap createReflectionImageWithOrigin(Bitmap bitmap) {
        final int reflectionGap = 4;
        int w = bitmap.getWidth();
        int h = bitmap.getHeight();

        Matrix matrix = new Matrix();
        matrix.preScale(1, -1);

        Bitmap reflectionImage = Bitmap.createBitmap(bitmap, 0, h / 2, w,
                h / 2, matrix, false);

        Bitmap bitmapWithReflection = Bitmap.createBitmap(w, (h + h / 2),
                Config.ARGB_8888);

        Canvas canvas = new Canvas(bitmapWithReflection);
        canvas.drawBitmap(bitmap, 0, 0, null);
        Paint deafalutPaint = new Paint();
        canvas.drawRect(0, h, w, h + reflectionGap, deafalutPaint);

        canvas.drawBitmap(reflectionImage, 0, h + reflectionGap, null);

        Paint paint = new Paint();
        LinearGradient shader = new LinearGradient(0, bitmap.getHeight(), 0,
                bitmapWithReflection.getHeight() + reflectionGap, 0x70ffffff,
                0x00ffffff, TileMode.CLAMP);
        paint.setShader(shader);
        // Set the Transfer mode to be porter duff and destination in
        paint.setXfermode(new PorterDuffXfermode(Mode.DST_IN));
        // Draw a rectangle using the paint with our linear gradient
        canvas.drawRect(0, h, w, bitmapWithReflection.getHeight()
                + reflectionGap, paint);

        return bitmapWithReflection;
    }

    public static Bitmap createReflectedBitmap(Bitmap srcBitmap) {
        if (null == srcBitmap) {
            return null;
        }

        // The gap between the reflection bitmap and original bitmap.
        final int REFLECTION_GAP = 4;

        int srcWidth = srcBitmap.getWidth();
        int srcHeight = srcBitmap.getHeight();
        int reflectionWidth = srcBitmap.getWidth();
        int reflectionHeight = srcBitmap.getHeight() / 2;

        if (0 == srcWidth || srcHeight == 0) {
            return null;
        }

        // The matrix
        Matrix matrix = new Matrix();
        matrix.preScale(1, -1);

        try {
            // The reflection bitmap, width is same with original's, height is
            // half of original's.
            Bitmap reflectionBitmap = Bitmap.createBitmap(srcBitmap, 0,
                    srcHeight / 2, srcWidth, srcHeight / 2, matrix, false);

            if (null == reflectionBitmap) {
                return null;
            }

            // Create the bitmap which contains original and reflection bitmap.
            Bitmap bitmapWithReflection = Bitmap.createBitmap(reflectionWidth,
                    srcHeight + reflectionHeight + REFLECTION_GAP,
                    Config.ARGB_8888);

            if (null == bitmapWithReflection) {
                return null;
            }

            // Prepare the canvas to draw stuff.
            Canvas canvas = new Canvas(bitmapWithReflection);

            // Draw the original bitmap.
            canvas.drawBitmap(srcBitmap, 0, 0, null);

            // Draw the reflection bitmap.
            canvas.drawBitmap(reflectionBitmap, 0, srcHeight + REFLECTION_GAP,
                    null);

            Paint paint = new Paint();
            paint.setAntiAlias(true);
            LinearGradient shader = new LinearGradient(0, srcHeight, 0,
                    bitmapWithReflection.getHeight() + REFLECTION_GAP,
                    0x70FFFFFF, 0x00FFFFFF, TileMode.MIRROR);
            paint.setShader(shader);
            paint.setXfermode(new PorterDuffXfermode(
                    android.graphics.PorterDuff.Mode.DST_IN));

            // Draw the linear shader.
            canvas.drawRect(0, srcHeight, srcWidth,
                    bitmapWithReflection.getHeight() + REFLECTION_GAP, paint);

            return bitmapWithReflection;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    public static Drawable zoomDrawable(Drawable drawable, int w, int h) {
        int width = drawable.getIntrinsicWidth();
        int height = drawable.getIntrinsicHeight();
        // drawable转换成bitmap
        Bitmap oldbmp = drawableToBitmap(drawable);
        // 创建操作图片用的Matrix对象
        Matrix matrix = new Matrix();
        // 计算缩放比例
        float sx = ((float) w / width);
        float sy = ((float) h / height);
        // 设置缩放比例
        matrix.postScale(sx, sy);
        // 建立新的bitmap，其内容是对原bitmap的缩放后的图
        Bitmap newbmp = Bitmap.createBitmap(oldbmp, 0, 0, width, height,
                matrix, true);
        return new BitmapDrawable(newbmp);
    }

    // ↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑
    // http://dyh7077063.iteye.com/blog/970672

    /**
     * 计算图片的宽高
     *
     * @param path
     * @return
     * @throws Exception
     * @author 宋疆疆
     * @date 2014年7月2日 上午11:34:03
     */
    public static int[] getBitmapSize(String path) {
        Options opts = new Options();
        opts.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(path, opts);
        return new int[]{opts.outWidth, opts.outHeight};
    }

    /**
     * 计算图片的宽高
     *
     * @return
     * @throws Exception
     * @author 宋疆疆
     * @date 2014年7月2日 上午11:34:03
     */
    public static int[] getBitmapSize(Resources res, int id) {
        Options opts = new Options();
        opts.inJustDecodeBounds = true;
        BitmapFactory.decodeResource(res, id, opts);
        return new int[]{opts.outWidth, opts.outHeight};
    }

    public static boolean saveBitmap(Bitmap bitmap, String path) {
        return saveBitmap(bitmap, path, Bitmap.CompressFormat.PNG);
    }

    public static boolean saveBitmap(Bitmap bitmap, String path, Bitmap.CompressFormat format) {
        return saveBitmap(bitmap, path, format, 100);
    }

    public static boolean saveBitmap(Bitmap bitmap, String path, Bitmap.CompressFormat format, int quality) {
        if (bitmap == null) {
            return false;
        }
        FileOutputStream out = null;
        try {
            boolean newFile = FileUtil.createFile(path);
            if (!newFile) {
                return false;
            }

            out = new FileOutputStream(path);
            boolean compress = bitmap.compress(format, quality, out);
            out.flush();
            return compress;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (out != null) {
                try {
                    out.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return false;
    }

    /**
     * 将彩色图转换为黑白图
     *
     * @return 返回转换好的位图
     */
    public static Bitmap convertToBlackWhite(Bitmap bmp) {
        int width = bmp.getWidth(); // 获取位图的宽
        int height = bmp.getHeight(); // 获取位图的高

        int[] pixels = new int[width * height]; // 通过位图的大小创建像素点数组

        bmp.getPixels(pixels, 0, width, 0, 0, width, height);
        //int alpha = 0xFF << 24;
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                int grey = pixels[width * i + j];

                int alpha = ((grey & 0xFF000000));
                int red = ((grey & 0x00FF0000) >> 16);
                int green = ((grey & 0x0000FF00) >> 8);
                int blue = (grey & 0x000000FF);

                grey = (int) (red * 0.3 + green * 0.59 + blue * 0.11);
                grey = alpha | (grey << 16) | (grey << 8) | grey;
                pixels[width * i + j] = grey;
            }
        }
        Bitmap newBmp = Bitmap.createBitmap(width, height, Config.ARGB_8888);
        newBmp.setPixels(pixels, 0, width, 0, 0, width, height);
        return newBmp;
    }

    /**
     * 读取图片的旋转的角度
     *
     * @param path 图片绝对路径
     * @return 图片的旋转角度
     */
    public static int getBitmapDegree(String path) {
        int degree = 0;
        try {
            // 从指定路径下读取图片，并获取其EXIF信息
            ExifInterface exifInterface = new ExifInterface(path);
            // 获取图片的旋转信息
            int orientation = exifInterface.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);
            switch (orientation) {
                case ExifInterface.ORIENTATION_ROTATE_90:
                    degree = 90;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_180:
                    degree = 180;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_270:
                    degree = 270;
                    break;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return degree;
    }


    /**
     * 将图片按照某个角度进行旋转
     *
     * @param bm     需要旋转的图片
     * @param degree 旋转角度
     * @return 旋转后的图片
     */
    public static Bitmap rotateBitmapByDegree(Bitmap bm, int degree) {
        Bitmap returnBm = null;

        // 根据旋转角度，生成旋转矩阵
        Matrix matrix = new Matrix();
        matrix.postRotate(degree);
        try {
            // 将原始图片按照旋转矩阵进行旋转，并得到新的图片
            returnBm = Bitmap.createBitmap(bm, 0, 0, bm.getWidth(), bm.getHeight(), matrix, true);
        } catch (OutOfMemoryError e) {
            e.printStackTrace();
        }
        if (returnBm == null) {
            returnBm = bm;
        }
        if (bm != returnBm) {
            bm.recycle();
        }
        return returnBm;
    }

    private static int maxTextureSize = -1;

    public static int getMaxTextureSize() {
        if (maxTextureSize != -1) {
            return maxTextureSize;
        }
        // Safe minimum default size
        final int IMAGE_MAX_BITMAP_DIMENSION = 2048;

        // Get EGL Display
        EGL10 egl = (EGL10) EGLContext.getEGL();
        EGLDisplay display = egl.eglGetDisplay(EGL10.EGL_DEFAULT_DISPLAY);

        // Initialise
        int[] version = new int[2];
        egl.eglInitialize(display, version);

        // Query total number of configurations
        int[] totalConfigurations = new int[1];
        egl.eglGetConfigs(display, null, 0, totalConfigurations);

        // Query actual list configurations
        EGLConfig[] configurationsList = new EGLConfig[totalConfigurations[0]];
        egl.eglGetConfigs(display, configurationsList, totalConfigurations[0], totalConfigurations);

        int[] textureSize = new int[1];
        int maximumTextureSize = 0;

        // Iterate through all the configurations to located the maximum texture size
        for (int i = 0; i < totalConfigurations[0]; i++) {
            // Only need to check for width since opengl textures are always squared
            egl.eglGetConfigAttrib(display, configurationsList[i], EGL10.EGL_MAX_PBUFFER_WIDTH, textureSize);

            // Keep track of the maximum texture size
            if (maximumTextureSize < textureSize[0])
                maximumTextureSize = textureSize[0];
        }

        // Release
        egl.eglTerminate(display);

        // Return largest texture size found, or default
        maxTextureSize = Math.max(maximumTextureSize, IMAGE_MAX_BITMAP_DIMENSION);
        return maxTextureSize;
    }

    public static class BitmapCompressInfo {
        public Bitmap bitmap;
        public int quality;
    }

    public static BitmapCompressInfo compressImage(Bitmap image, int maxSize) {
        return compressImage(image, maxSize, 100, 10);
    }

    public static BitmapCompressInfo compressImage(Bitmap image, int maxSize, int startQuality, int decrementStep) {
        ByteArrayOutputStream baos = null;
        ByteArrayInputStream isBm = null;
        BitmapCompressInfo info = new BitmapCompressInfo();
        try {
            baos = new ByteArrayOutputStream();
            image.compress(Bitmap.CompressFormat.JPEG, startQuality, baos);//质量压缩方法，这里100表示不压缩，把压缩后的数据存放到baos中
            int options = startQuality;
            while (baos.toByteArray().length / 1024 > maxSize) {  //循环判断如果压缩后图片是否大于maxSizekb,大于继续压缩
                if (options <= 2) {
                    break;
                }
                baos.reset();//重置baos即清空baos
                image.compress(Bitmap.CompressFormat.JPEG, options, baos);//这里压缩options%，把压缩后的数据存放到baos中
                options -= decrementStep;//每次都减少10
                if (options <= 0) {
                    options = 2;
                }
            }
            info.quality = options;
            isBm = new ByteArrayInputStream(baos.toByteArray());//把压缩后的数据baos存放到ByteArrayInputStream中
            Options decodeOptions = new Options();
            decodeOptions.inPreferredConfig = Bitmap.Config.RGB_565;
            Bitmap bitmap = BitmapFactory.decodeStream(isBm, null, decodeOptions);//把ByteArrayInputStream数据生成图片
            info.bitmap = bitmap;
            return info;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (isBm != null) {
                try {
                    isBm.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (baos != null) {
                try {
                    baos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return null;
    }

    public static byte[] compressImageReturnBytes(Bitmap image, int maxSize) {
        return compressImageReturnBytes(image, maxSize, 100, 10);
    }

    public static byte[] compressImageReturnBytes(Bitmap image, int maxSize, int startQuality, int decrementStep) {
        ByteArrayOutputStream baos = null;
        try {
            baos = new ByteArrayOutputStream();
            image.compress(Bitmap.CompressFormat.JPEG, startQuality, baos);//质量压缩方法，这里100表示不压缩，把压缩后的数据存放到baos中
            int options = startQuality;
            while (baos.toByteArray().length / 1024 > maxSize) {  //循环判断如果压缩后图片是否大于maxSizekb,大于继续压缩
                if (options <= 2) {
                    break;
                }
                baos.reset();//重置baos即清空baos
                image.compress(Bitmap.CompressFormat.JPEG, options, baos);//这里压缩options%，把压缩后的数据存放到baos中
                options -= decrementStep;//每次都减少10
                if (options <= 0) {
                    options = 2;
                }
            }
            return baos.toByteArray();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (baos != null) {
                try {
                    baos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return null;
    }

    public static boolean isImage(String path) {
        return isImage(new File(path));
    }

    public static boolean isImage(File file) {
        if (file == null || !file.exists()) {
            return false;
        }
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(file.getPath(), options);
        boolean isImage = options.outWidth != -1 && options.outHeight != -1;
        isImage &= options.outWidth != 0 && options.outHeight != 0;
        return isImage;
    }

}
