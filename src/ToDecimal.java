public class ToDecimal {
    private static final int maxPower = 16;
    private static final String charArray = "0123456789ABCDEF";

    public static int convert(String number, int fromPower) {
        if (fromPower > maxPower) throw new IllegalArgumentException();

        int result = 0;
        int multiplier = 1;

        for (int i = number.length() - 1; i > -1; i--) {
            char digitSymbol = number.charAt(i);
            for (int j = 0; j < charArray.length(); j++) {
                if (digitSymbol == charArray.charAt(j)) result += j * multiplier;
            }
            multiplier *= fromPower;
        }
        return result;
    }

    //public static float convert(String)

    public static void main(String[] args) {
        int r = convert("233", 6);
        int b = convert("A4E", 16);
        System.out.println(r);
        System.out.println(b);
    }
}
