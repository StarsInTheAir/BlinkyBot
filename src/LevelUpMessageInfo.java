import java.util.regex.Pattern;

public interface LevelUpMessageInfo {

    Pattern levelUpMessageContentRegex = Pattern.compile("^GG <@!?\\d{18}>, you just advanced to level \\d+!$");

    static int getLevelInLevelUpMessageContent(final String content) {
        final String levelUpMessageWithExclamationMark = content.split(" ")[7];
        return Integer.parseInt(levelUpMessageWithExclamationMark.substring(0, levelUpMessageWithExclamationMark.length() - 1));
    }
}