package reactiongame.support;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import reactiongame.infrastructure.web.ReactionGameException;

import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

import static reactiongame.infrastructure.web.ReactionGameExceptionStatus.INTERNAL_SERVER_ERROR;

public final class Randoms {

    private static final Logger log = LoggerFactory.getLogger(Randoms.class);

    private static final String ALPHANUMERIC = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";

    private static final Random random = ThreadLocalRandom.current();

    private Randoms() {
    }

    public static String generateAlphanumeric(final int length) {
        if (length < 1) {
            log.error("length must be greater than 0 [length={}]", length);
            throw new ReactionGameException(INTERNAL_SERVER_ERROR);
        }

        final var builder = new StringBuilder(length);
        for (int i = 0; i < length; i++) {
            final var index = (int) (ALPHANUMERIC.length() * Math.random());
            builder.append(ALPHANUMERIC.charAt(index));
        }
        return builder.toString();
    }

    public static int randomInt(final int max) {
        return randomInt(0, max);
    }

    public static int randomInt(
            final int min,
            final int max
    ) {
        return random.nextInt(min, max);
    }

    public static boolean randomBoolean() {
        return random.nextBoolean();
    }
}
