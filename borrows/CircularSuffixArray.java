import java.util.Arrays;

public class CircularSuffixArray {

    private String originalString;
    private CircularSuffix[] suffixArray;

    private static class CircularSuffix implements Comparable<CircularSuffix>{
        private String suffix;
        private int position;

        private CircularSuffix(String s, int position) {
            this.suffix = s;
            this.position = position;
        }

        private int getPosition() {
            return position;
        }

        @Override
        public int compareTo(CircularSuffixArray.CircularSuffix o) {
            int otherPosition = o.getPosition();

            for (int i = 0; i < suffix.length(); i++) {
                char currentChar = charOnPosition(position, i);
                char otherChar = charOnPosition(otherPosition, i);

                if (currentChar > otherChar) {
                    return 1;
                } else if (currentChar < otherChar) {
                    return -1;
                }
            }

            return 0;
        }

        private char charOnPosition(int position, int i) {
            int currentPosition = position + i;
            if (currentPosition >= suffix.length()) currentPosition -= suffix.length();

            return suffix.charAt(currentPosition);
        }

        @Override
        public String toString() {
            return suffix.substring(position) + suffix.substring(0, position);
        }
    }

    // circular suffix array of s
    public CircularSuffixArray(String s) {
        if (s == null) throw new IllegalArgumentException("constructor can not be null");

        this.originalString = s;
        this.suffixArray = new CircularSuffix[s.length()];

        fillSuffixArray();
    }

    private void fillSuffixArray() {
        for (int pos = 0; pos < length(); pos++) {
            suffixArray[pos] = new CircularSuffix(originalString, pos);
        }

        Arrays.sort(suffixArray);
    }

    // length of s
    public int length() {
        return originalString.length();
    }

    // returns index of ith sorted suffix
    public int index(int i) {
        if (i < 0 || i >= length()) throw new IllegalArgumentException("index outside range: 0.." + length());

        return suffixArray[i].getPosition();
    }

    @Override
    public String toString() {
        StringBuilder result = new StringBuilder();
        for (CircularSuffix el : suffixArray) {
            result.append("[").append(el.toString()).append("]\n");
        }
        result.append("Length: ").append(length());

        return result.toString();
    }

    // unit testing (required)
    public static void main(String[] args) {

        String test = "ABRACADABRA!";

        CircularSuffixArray array = new CircularSuffixArray(test);

        System.out.print(array);
        System.out.print("\nIndex 11: " + array.index(11));
    }
}