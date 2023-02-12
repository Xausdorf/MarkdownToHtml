package markup;

public class Text implements Markup {
    private final String text;

    public Text(String text) {
        this.text = text;
    }

    @Override
    public void toMarkdown(StringBuilder stringBuilder) {
        stringBuilder.append(text);
    }

    @Override
    public void toTex(StringBuilder stringBuilder) {
        stringBuilder.append(text);
    }
}