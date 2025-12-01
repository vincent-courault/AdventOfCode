package adventofcode;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public interface Divider<T> {
    List<T> toList(String s);

    public static class Int implements Divider<Integer> {
        public List<Integer> toList(String s, String delim) {
            return Arrays.stream(s.split(delim)).mapToInt(Integer::parseInt).boxed().collect(Collectors.toList());
        }

        @Override
        public List<Integer> toList(String s) {
            return toList(s, " ");
        }
    }

    public static class Character implements Divider<java.lang.Character> {
        @Override
        public List<java.lang.Character> toList(String s) {
            return s.chars().mapToObj(c -> (char) c).collect(Collectors.toList());
        }
    }
}
