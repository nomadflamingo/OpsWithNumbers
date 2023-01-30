public class BasedNumber {
    private final int[] value;
    private final int base;

    private final static String allowedChars = "0123456789ABCDEF";
    private final static int maxAllowedBase = allowedChars.length();

    public BasedNumber(String value, int base) {
        checkBase(base);
        this.value = new int[value.length()];
        this.base = base;

        // populate value array
        int j = 0;
        for (int i = value.length()-1; i > -1; i--) {
            char c = value.charAt(i);
            if (Character.isDigit(c) && c - '0' < base) {
                this.value[j] = c - '0';
            } else if (c < allowedChars.charAt(10) || c > allowedChars.charAt(base-1)) {  // throw error if char is not in allowedChars array
                throw new IllegalArgumentException();
            } else {
                this.value[j] = c - 'A' + 10;
            }
            j++;
        }
    }

    private BasedNumber(int[] value, int base) {
        checkBase(base);
        checkValues(value, base);

        this.value = value;
        this.base = base;
    }

    /** Checks that values are compatible with base and if not throws an IllegalArgumentException **/
    private void checkValues(int[] value, int base) {
        for(int a : value) {
            if (a > base-1) throw new IllegalArgumentException("Entry in the value array shouldn't be greater that the max number in given base");
        }
    }

    /** Checks that base is positive and less than the maximum allowed base and if not throws an IllegalArgumentException **/
    private void checkBase(int base) {
        if (base <= 0) throw new IllegalArgumentException("Base should be greater than 0");
        if (base > maxAllowedBase) throw new IllegalArgumentException("Base should not be greater than " + maxAllowedBase);
    }

    public BasedNumber plus(BasedNumber x) {
        if (x.base != this.base) throw new IllegalArgumentException("Cannot add numbers of different bases");
        int flag = 0;

        int[] a = this.value.length > x.value.length ? this.value : x.value;  // find the longest value
        int[] b = (a == this.value) ? x.value : this.value;

        int resultCapacity = a.length + 1;
        int[] result = new int[resultCapacity];

        int k = 0;
        for (int i = 0; i < b.length; i++) {
            int sum = a[i] + b[i] + flag;
            result[k] = sum % this.base;
            flag = sum / this.base;
            if (flag > 1) System.out.println("Something went very wrong!!!");
            k++;
        }
        for (int i = b.length; i < a.length; i++) {
            int sum = a[i] + flag;
            result[k] = sum % this.base;
            flag = sum / this.base;
            k++;
        }
        result[k] += flag;
        if (result[k] == 0) result = removeLeadingZeroes(result);
        return new BasedNumber(result, this.base);
    }

    public BasedNumber plus(BasedNumber[] xs) {
        BasedNumber sum = this;
        for (BasedNumber x : xs) {
            sum = sum.plus(x);
        }
        return sum;
    }

    public BasedNumber multiply(BasedNumber x) {
        if (this.base != x.base) throw new IllegalArgumentException("Cannot multiply numbers of different bases");

        int flag;
        int[] a = this.value.length > x.value.length ? this.value : x.value;  // find the longest value
        int[] b = (a == this.value) ? x.value : this.value;
        int[] result = new int[a.length + b.length];

        for (int outer = 0; outer < b.length; outer++) {
            for (int inner = 0; inner < a.length; inner++) {
                flag = result[outer+inner] / this.base;
                result[outer+inner] %= this.base;

                int product = b[outer] * a[inner];
                flag += product / this.base;
                product %= this.base;

                result[outer+inner] += product;
                flag += result[outer+inner] / this.base;
                result[outer+inner] %= this.base;
                result[outer+inner+1] += flag;
            }
        }
        if (result[result.length-1] == 0) result = removeLeadingZeroes(result);
        return new BasedNumber(result, this.base);
    }

    public BasedNumber multiply(BasedNumber[] xs) {
        BasedNumber prod = this;
        for (BasedNumber x : xs) {
            prod = prod.multiply(x);
        }
        return prod;
    }

    public BasedNumber toBase(int base) {
        if (this.base == base) return this;
        BasedNumber p = convertDigitToOtherBase(this.base, base);
        BasedNumber multiplier = new BasedNumber("1", base);
        BasedNumber sum = new BasedNumber("0", base);
        for (int a : value) {
            BasedNumber aInOtherBase = convertDigitToOtherBase(a, base);
            aInOtherBase = aInOtherBase.multiply(multiplier);
            sum = sum.plus(aInOtherBase);
            multiplier = multiplier.multiply(p);
        }
        return sum;
    }

    @Override
    public String toString() {
        StringBuilder res = new StringBuilder();
        for (int i = value.length - 1; i >= 0; i--) {
            int c = value[i];
            res.append(allowedChars.charAt(c));
        }
        return res.toString();
    }

    private BasedNumber convertDigitToOtherBase(int a, int base) {
        int[] aValuesInOtherBase = new int[nOfDigitsInBase(a, base)];
        for (int i = 0; a > 0; i++) {
            aValuesInOtherBase[i] = a % base;
            a /= base;
        }
        return new BasedNumber(aValuesInOtherBase, base);
    }

    private int[] removeLeadingZeroes(int[] arr) {
        int firstNotZero = arr.length - 1;
        while (arr[firstNotZero] == 0) firstNotZero--;
        int[] newArr = new int[firstNotZero+1];
        for (int i = 0; i < newArr.length; i++) {
            newArr[i] = arr[i];
        }
        return newArr;
    }

    private static int nOfDigitsInBase(int x, int base) {
        int res = 0;
        if (x < 0) throw new IllegalArgumentException("x cannot be negative");
        if (x <= 1) return 1;
        while (x > 1) {
            x /= base;
            res++;
        }
        return res;
    }

    // test client
    public static void main(String[] args) {

    }
}
