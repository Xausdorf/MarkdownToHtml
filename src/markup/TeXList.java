package markup;

import java.util.List;

public abstract class TeXList implements MarkList {
    private final List<ListItem> listItems;
    private final String environment;

    protected TeXList(List<ListItem> listItems, String environment) {
        this.listItems = listItems;
        this.environment = environment;
    }

    @Override
    public void toTex(StringBuilder stringBuilder) {
        stringBuilder.append("\\begin{");
        stringBuilder.append(environment);
        stringBuilder.append("}");
        for (ListItem listItem : listItems) {
            listItem.toTex(stringBuilder);
        }
        stringBuilder.append("\\end{");
        stringBuilder.append(environment);
        stringBuilder.append("}");
    }
}
