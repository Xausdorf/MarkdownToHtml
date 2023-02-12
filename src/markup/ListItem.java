package markup;

import java.util.List;

public class ListItem {
    private final List<MarkList> markLists;

    public ListItem(List<MarkList> markLists) {
        this.markLists = markLists;
    }

    public void toTex(StringBuilder string) {
        string.append("\\item ");
        for (MarkList markList : markLists) {
            markList.toTex(string);
        }
    }
}
