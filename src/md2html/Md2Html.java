package md2html;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.*;

public class Md2Html {
    private final static int MAX_TAG_LENGTH = 2;
    private final static int MIN_TAG_LENGTH = 1;
    private final static int BRACKET_LENGTH = 1;
    private final static Map<String, String> HTML_TAGS = new HashMap<>(Map.of(
            "*", "em",
            "_", "em",
            "**", "strong",
            "__", "strong",
            "--", "s",
            "`", "code",
            "[", "a"
    ));
    private final static Map<Character, String> ENTITIES = new HashMap<>(Map.of(
            '"', "&quot;",
            '&', "&amp;",
            '<', "&lt;",
            '>', "&gt;"
    ));

    private static boolean isTag(String string) {
        return HTML_TAGS.containsKey(string);
    }

    private static boolean isEntity(char character) {
        return ENTITIES.containsKey(character);
    }

    private static void addToString(StringBuilder addend, String tag, StringBuilder result) {
        result.append("<").append(tag).append(">");
        result.append(addend);
        result.append("</").append(tag).append(">");
    }

    private static int parseText(String text, int start, StringBuilder current, StringBuilder result, String tag) {
        for (int i = start; i < text.length(); i++) {
            boolean isThereTag = false;
            if (!tag.equals(")")) {
                for (int j = MAX_TAG_LENGTH; j >= MIN_TAG_LENGTH; j--) {
                    if (i + j > text.length()) {
                        continue;
                    }
                    String tempTag = text.substring(i, i + j);
                    if (isTag(tempTag)) {
                        if (tempTag.equals("[")) {
                            StringBuilder linkText = new StringBuilder();
                            int endOfText = parseText(text, i + BRACKET_LENGTH,
                                    new StringBuilder(), linkText, "]");
                            if (endOfText < text.length() && text.charAt(endOfText) == '(') {
                                StringBuilder link = new StringBuilder();
                                int endOfLink = parseText(text, endOfText + BRACKET_LENGTH,
                                        new StringBuilder(), link, ")");
                                if (text.charAt(endOfLink - BRACKET_LENGTH) == ')') {
                                    current.append("<a href='").append(link).
                                            append("'>").append(linkText).append("</a>");
                                } else {
                                    current.append("[").append(linkText).append("]").append("(").append(link);
                                }
                                i = endOfLink - BRACKET_LENGTH;
                            } else {
                                current.append("[").append(linkText);
                                if (text.charAt(endOfText - BRACKET_LENGTH) == ']') {
                                    current.append("]");
                                }
                                i = endOfText - BRACKET_LENGTH;
                            }
                        } else if (tempTag.equals(tag)) {
                            addToString(current, HTML_TAGS.get(tempTag), result);
                            return i + j;
                        } else {
                            i = parseText(text, i + j, new StringBuilder(), current, tempTag) - 1;
                        }
                        isThereTag = true;
                        break;
                    }
                }
            }
            if (isThereTag) {
                continue;
            }
            if (text.substring(i, i + BRACKET_LENGTH).equals(tag)) { // in case of tag = "]" or "]"
                result.append(current);
                return i + BRACKET_LENGTH;
            }
            if (text.charAt(i) == '\\' && (i + 1) < text.length()
                    && !Character.isWhitespace(text.charAt(i + 1))) {
                i++;
                current.append(text.charAt(i));
            } else {
                if (isEntity(text.charAt(i))) {
                    current.append(ENTITIES.get(text.charAt(i)));
                } else {
                    current.append(text.charAt(i));
                }
            }
        }
        result.append(tag).append(current);
        return text.length();
    }

    private static void parseLine(StringBuilder stringBuilder, StringBuilder result) {
        if (stringBuilder.isEmpty()) {
            return;
        }
        int headingLevel = 0;
        for (int i = 0; i < stringBuilder.length(); i++) {
            if (stringBuilder.charAt(i) == '#') {
                headingLevel++;
            } else {
                break;
            }
        }
        if (headingLevel > 0 && headingLevel < stringBuilder.length()
                && Character.isWhitespace(stringBuilder.charAt(headingLevel))) {
            result.append("<h").append(headingLevel).append(">");
            parseText(stringBuilder.substring(headingLevel + 1, stringBuilder.length() - 1),
                    0, new StringBuilder(), result, "");
            result.append("</h").append(headingLevel).append(">\n");
        } else {
            result.append("<p>");
            parseText(stringBuilder.substring(0, stringBuilder.length() - 1),
                    0, new StringBuilder(), result, "");
            result.append("</p>\n");
        }
    }

    public static void main(String[] args) {
        StringBuilder result = new StringBuilder();
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(
                    new FileInputStream(args[0]),
                    StandardCharsets.UTF_8
            ));
            try {
                StringBuilder currentString = new StringBuilder();
                String line = reader.readLine();
                while (line != null) {
                    if (!line.equals("")) {
                        currentString.append(line);
                        currentString.append("\n");
                    } else {
                        parseLine(currentString, result);
                        currentString = new StringBuilder();
                    }
                    line = reader.readLine();
                }
                parseLine(currentString, result);

            } finally {
                reader.close();
            }
            try {
                BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(
                        new FileOutputStream(args[1]),
                        StandardCharsets.UTF_8
                ));
                try {
                    writer.write(result.toString());
                } finally {
                    writer.close();
                }
            } catch (IOException e) {
                System.out.println("Output error: " + e.getMessage());
            }
        } catch (FileNotFoundException e) {
            System.out.println("File not found: " + e.getMessage());
        } catch (IOException e) {
            System.out.println("Input error: " + e.getMessage());
        } catch (ArrayIndexOutOfBoundsException e) {
            System.out.println("Too few arguments: " + e.getMessage());
        }
    }
}
