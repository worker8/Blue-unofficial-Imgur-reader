package worker8.com.github.imgurdiscovery.imgur_wrapper;

import java.util.ArrayList;

/**
 * Imgur specific constants
 */
public class ImgurConstant {
    /**
     * This is the list of <i>imgur sections</i> that is used in the {@link android.support.design.widget.NavigationView NavigationView}
     */
    public static ArrayList<String> sectionList;

    static {
        sectionList = new ArrayList<>();
        sectionList.add("Hot");//0
        sectionList.add("Top");//1
        sectionList.add("User");//2
        sectionList.add("r/aww");//3
        sectionList.add("r/pics");//4
        sectionList.add("r/EarthPorn");//5
        sectionList.add("r/art");//6
        sectionList.add("r/gifs");
        sectionList.add("r/pics");
        sectionList.add("r/funny");
        sectionList.add("r/ExposurePorn");
        sectionList.add("r/ITookAPicture");
        sectionList.add("r/MildlyInteresting");
        sectionList.add("r/awwnverts");
        sectionList.add("r/corgi");
    }

}
