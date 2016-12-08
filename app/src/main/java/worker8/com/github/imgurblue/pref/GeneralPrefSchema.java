package worker8.com.github.imgurblue.pref;


import com.rejasupotaro.android.kvs.annotations.Key;
import com.rejasupotaro.android.kvs.annotations.Table;

@Table(name = "general")
public abstract class GeneralPrefSchema {
    @Key(name = "last_visited_sub")
    final String lastVisitedSub = "r/pics";
}
