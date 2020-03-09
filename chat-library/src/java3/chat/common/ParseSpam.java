package java3.chat.common;

import java.util.concurrent.CopyOnWriteArrayList;

public class ParseSpam {
    private CopyOnWriteArrayList<String> listSpam;
    private final String replacement = "carrot";

    public ParseSpam (CopyOnWriteArrayList<String> listSpam) {
        this.listSpam = listSpam;
    }

    public String changeSpamWord(String msg) {
        CopyOnWriteArrayList<String> listWordsMsg = new CopyOnWriteArrayList<>();

        StringBuffer sb = new StringBuffer();
        for (String word : msg.split(" ")) {
            if (!listSpam.contains(word)) {
                sb.append(word);
                sb.append(" ");
            } else {
                sb.append(replacement);
                sb.append(" ");
            }
        }

        return sb.toString();
    }
}
