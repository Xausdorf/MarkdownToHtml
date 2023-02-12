package markup;

import java.util.List;

public class Paragraph implements MarkList {
    private final List<Markup> markups;

    public Paragraph(List<Markup> markups) {
        this.markups = markups;
    }

    public void toMarkdown(StringBuilder stringBuilder) {
        for (Markup markup : markups) {
            markup.toMarkdown(stringBuilder);
        }
    }

    @Override
    public void toTex(StringBuilder stringBuilder) {
        for (Markup markup : markups) {
            markup.toTex(stringBuilder);
        }
    }
}
