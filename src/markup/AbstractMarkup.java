package markup;

import java.util.List;

public abstract class AbstractMarkup implements Markup {
    private final List<Markup> markups;
    private final String tag;
    private final String texTag;

    protected AbstractMarkup(List<Markup> markups, String tag, String texTag) {
        this.markups = markups;
        this.tag = tag;
        this.texTag = texTag;
    }

    @Override
    public void toMarkdown(StringBuilder stringBuilder) {
        stringBuilder.append(tag);
        for (Markup markup : markups) {
            markup.toMarkdown(stringBuilder);
        }
        stringBuilder.append(tag);
    }

    @Override
    public void toTex(StringBuilder stringBuilder) {
        stringBuilder.append("\\");
        stringBuilder.append(texTag);
        stringBuilder.append("{");
        for (Markup markup : markups) {
            markup.toTex(stringBuilder);
        }
        stringBuilder.append("}");
    }
}
