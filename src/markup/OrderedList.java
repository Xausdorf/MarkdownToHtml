package markup;

import java.util.List;

public class OrderedList extends TeXList {
    public OrderedList(List<ListItem> listItems) {
        super(listItems, "enumerate");
    }
}
