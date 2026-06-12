import org.fife.ui.rsyntaxtextarea.*;
import javax.swing.text.Segment;
import java.util.*;

public class RUTokenMaker extends AbstractTokenMaker {

    private static final Set<String> KEYWORDS = new HashSet<>(Arrays.asList(
            "agar", "warna", "jabtak", "ghumphir", "wapas",
            "jamaat", "upar", "yeh", "kaam", "rakho",
            "sach", "jhoot", "khaali", "likho", "aur", "ya"
    ));

    @Override
    public TokenMap getWordsToHighlight() {
        TokenMap map = new TokenMap();
        for (String kw : KEYWORDS) {
            map.put(kw, Token.RESERVED_WORD);
        }
        return map;
    }

    @Override
    public Token getTokenList(Segment text, int initialTokenType, int startOffset) {
        resetTokenList();

        char[] array = text.array;
        int offset = text.offset;
        int end = offset + text.count;

        int currentTokenStart = offset;
        int currentTokenType = initialTokenType;

        for (int i = offset; i < end; i++) {
            char c = array[i];

            switch (currentTokenType) {

                case Token.NULL: {
                    currentTokenStart = i;

                    if (c == '"') {
                        currentTokenType = Token.LITERAL_STRING_DOUBLE_QUOTE;
                    } else if (c == '/' && i + 1 < end && array[i + 1] == '/') {
                        addToken(text, currentTokenStart, end - 1,
                                Token.COMMENT_EOL, startOffset + currentTokenStart - offset);
                        return firstToken;
                    } else if (c >= '0' && c <= '9') {
                        currentTokenType = Token.LITERAL_NUMBER_DECIMAL_INT;
                    } else if (Character.isLetter(c) || c == '_') {
                        currentTokenType = Token.IDENTIFIER;
                    } else if ("+-*/=!<>".indexOf(c) >= 0) {
                        addToken(text, i, i, Token.OPERATOR, startOffset + i - offset);
                    } else if ("(){}.,;".indexOf(c) >= 0) {
                        addToken(text, i, i, Token.SEPARATOR, startOffset + i - offset);
                    } else if (c == ' ' || c == '\t') {
                        currentTokenType = Token.WHITESPACE;
                    }
                    break;
                }

                case Token.WHITESPACE: {
                    if (c != ' ' && c != '\t') {
                        addToken(text, currentTokenStart, i - 1,
                                Token.WHITESPACE, startOffset + currentTokenStart - offset);
                        currentTokenStart = i;
                        currentTokenType = Token.NULL;
                        i--;
                    }
                    break;
                }

                case Token.LITERAL_STRING_DOUBLE_QUOTE: {
                    if (c == '"') {
                        addToken(text, currentTokenStart, i,
                                Token.LITERAL_STRING_DOUBLE_QUOTE,
                                startOffset + currentTokenStart - offset);
                        currentTokenType = Token.NULL;
                    } else if (c == '\\' && i + 1 < end) {
                        i++; // skip escaped char
                    }
                    break;
                }

                case Token.LITERAL_NUMBER_DECIMAL_INT: {
                    if ((c < '0' || c > '9') && c != '.') {
                        addToken(text, currentTokenStart, i - 1,
                                Token.LITERAL_NUMBER_DECIMAL_INT,
                                startOffset + currentTokenStart - offset);
                        currentTokenStart = i;
                        currentTokenType = Token.NULL;
                        i--;
                    }
                    break;
                }

                case Token.IDENTIFIER: {
                    if (!Character.isLetterOrDigit(c) && c != '_') {
                        String word = new String(array, currentTokenStart, i - currentTokenStart);
                        int type = KEYWORDS.contains(word) ? Token.RESERVED_WORD : Token.IDENTIFIER;
                        addToken(text, currentTokenStart, i - 1,
                                type, startOffset + currentTokenStart - offset);
                        currentTokenStart = i;
                        currentTokenType = Token.NULL;
                        i--;
                    }
                    break;
                }
            }
        }


        switch (currentTokenType) {
            case Token.NULL:
                addNullToken();
                break;
            case Token.IDENTIFIER: {
                String word = new String(array, currentTokenStart, end - currentTokenStart);
                int type = KEYWORDS.contains(word) ? Token.RESERVED_WORD : Token.IDENTIFIER;
                addToken(text, currentTokenStart, end - 1, type,
                        startOffset + currentTokenStart - offset);
                addNullToken();
                break;
            }
            case Token.LITERAL_STRING_DOUBLE_QUOTE:
                addToken(text, currentTokenStart, end - 1,
                        Token.ERROR_STRING_DOUBLE, startOffset + currentTokenStart - offset);
                return firstToken; // signals: still inside string on next line
            default:
                if (currentTokenStart < end) {
                    addToken(text, currentTokenStart, end - 1, currentTokenType,
                            startOffset + currentTokenStart - offset);
                }
                addNullToken();
                break;
        }

        return firstToken;
    }
}