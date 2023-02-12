package markup;

import java.util.List;

public class UnorderedList extends TeXList {
    public UnorderedList(List<ListItem> listItems) {
        super(listItems, "itemize");
    }
}
