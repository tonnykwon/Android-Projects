package functions;

import android.app.Application;
import android.content.Context;
import android.graphics.Typeface;

import java.lang.reflect.Field;

/**
 * Created by tony on 2016-02-25.
 */
public class FontApplication extends Application {
    @Override
    public void onCreate() {
        setDefaultFont(this, "DEFAULT", "SeoulNamsanM.otf");
        setDefaultFont(this, "SANS_SERIF", "SeoulNamsanM.otf");
        setDefaultFont(this, "SERIF", "SeoulNamsanM.otf");
    }

    public static void setDefaultFont(Context ctx,
                                      String staticTypefaceFieldName, String fontAssetName) {
        final Typeface regular = Typeface.createFromAsset(ctx.getAssets(),
                fontAssetName);
        replaceFont(staticTypefaceFieldName, regular);
    }

    protected static void replaceFont(String staticTypefaceFieldName,
                                      final Typeface newTypeface) {
        try {
            final Field StaticField = Typeface.class
                    .getDeclaredField(staticTypefaceFieldName);
            StaticField.setAccessible(true);
            StaticField.set(null, newTypeface);
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }
}
