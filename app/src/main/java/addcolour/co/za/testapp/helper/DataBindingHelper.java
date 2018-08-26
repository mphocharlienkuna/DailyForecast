package addcolour.co.za.testapp.helper;

import android.databinding.BindingAdapter;
import android.text.TextUtils;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CenterInside;
import com.bumptech.glide.request.RequestOptions;

import addcolour.co.za.testapp.R;

public class DataBindingHelper {

    private static String CLOUDY = "Clouds";
    private static String RAINY = "Rain";
    private static String SUNNY = "Clear";

    private static Integer imagePath;

    @BindingAdapter({"textView"})
    public static void setTextView(TextView textView, double value) {
        textView.setText(String.valueOf(Math.round(value) + "" + (char) 0x00B0));
    }

    @BindingAdapter({"relativeLayout"})
    public static void setRelativeLayout(RelativeLayout relativeLayout, String main) {
        if (!TextUtils.isEmpty(main)) {
            relativeLayout.setBackgroundResource(getBackground(main));
        }
    }

    private static Integer getBackground(String img) {
        if (img.equalsIgnoreCase(CLOUDY)) {
            imagePath = R.color.cloudy;
        } else if (img.equalsIgnoreCase(RAINY)) {
            imagePath = R.color.rainy;
        } else if (img.equalsIgnoreCase(SUNNY)) {
            imagePath = R.color.sunny;
        }

        return imagePath;
    }

    @BindingAdapter({"imageViewBackground"})
    public static void setImageViewBackground(ImageView imageView, String main) {
        if (!TextUtils.isEmpty(main)) {
            Glide.with(imageView.getContext()).load(getImagePathBackground(main))
                    .apply(new RequestOptions().fitCenter()).into(imageView);
        }
    }

    private static Integer getImagePathBackground(String img) {
        if (img.equalsIgnoreCase(CLOUDY)) {
            imagePath = R.drawable.forest_cloudy;
        } else if (img.equalsIgnoreCase(RAINY)) {
            imagePath = R.drawable.forest_rainy;
        } else if (img.equalsIgnoreCase(SUNNY)) {
            imagePath = R.drawable.forest_sunny;
        }

        return imagePath;
    }

    @BindingAdapter("imageView")
    public static void setImageView(ImageView imageView, String main) {
        if (!TextUtils.isEmpty(main)) {
            Glide.with(imageView.getContext()).load(getImagePath(main))
                    .apply(new RequestOptions().fitCenter().transform(new CenterInside()))
                    .into(imageView);
        }
    }

    private static Integer getImagePath(String img) {

        if (img.equalsIgnoreCase(CLOUDY)) {
            imagePath = R.drawable.ic_partly_sunny;
        } else if (img.equalsIgnoreCase(RAINY)) {
            imagePath = R.drawable.ic_rain;
        } else if (img.equalsIgnoreCase(SUNNY)) {
            imagePath = R.drawable.ic_clear;
        }

        return imagePath;
    }
}