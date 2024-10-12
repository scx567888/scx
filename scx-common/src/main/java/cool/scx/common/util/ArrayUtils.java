package cool.scx.common.util;

import java.lang.reflect.Array;
import java.util.*;

/**
 * 提供一些 Array 的方法, 也有一些 List 相关的方法
 */
public final class ArrayUtils {

    //************  toPrimitive START *****************

    public static byte[] toPrimitive(Byte... w) {
        var p = new byte[w.length];
        for (var i = 0; i < w.length; i = i + 1) {
            p[i] = w[i];
        }
        return p;
    }

    public static short[] toPrimitive(Short... w) {
        var p = new short[w.length];
        for (var i = 0; i < w.length; i = i + 1) {
            p[i] = w[i];
        }
        return p;
    }

    public static int[] toPrimitive(Integer... w) {
        var p = new int[w.length];
        for (var i = 0; i < w.length; i = i + 1) {
            p[i] = w[i];
        }
        return p;
    }

    public static long[] toPrimitive(Long... w) {
        var p = new long[w.length];
        for (var i = 0; i < w.length; i = i + 1) {
            p[i] = w[i];
        }
        return p;
    }

    public static float[] toPrimitive(Float... w) {
        var p = new float[w.length];
        for (var i = 0; i < w.length; i = i + 1) {
            p[i] = w[i];
        }
        return p;
    }

    public static double[] toPrimitive(Double... w) {
        var p = new double[w.length];
        for (var i = 0; i < w.length; i = i + 1) {
            p[i] = w[i];
        }
        return p;
    }

    public static boolean[] toPrimitive(Boolean... w) {
        var p = new boolean[w.length];
        for (var i = 0; i < w.length; i = i + 1) {
            p[i] = w[i];
        }
        return p;
    }

    public static char[] toPrimitive(Character... w) {
        var p = new char[w.length];
        for (var i = 0; i < w.length; i = i + 1) {
            p[i] = w[i];
        }
        return p;
    }

    //************  toPrimitive END *****************


    //********************** toWrapper START *************************

    public static Byte[] toWrapper(byte... p) {
        var w = new Byte[p.length];
        for (int i = 0; i < p.length; i = i + 1) {
            w[i] = p[i];
        }
        return w;
    }

    public static Short[] toWrapper(short... p) {
        var w = new Short[p.length];
        for (int i = 0; i < p.length; i = i + 1) {
            w[i] = p[i];
        }
        return w;
    }

    public static Integer[] toWrapper(int... p) {
        var w = new Integer[p.length];
        for (int i = 0; i < p.length; i = i + 1) {
            w[i] = p[i];
        }
        return w;
    }

    public static Long[] toWrapper(long... p) {
        var w = new Long[p.length];
        for (int i = 0; i < p.length; i = i + 1) {
            w[i] = p[i];
        }
        return w;
    }

    public static Float[] toWrapper(float... p) {
        var w = new Float[p.length];
        for (int i = 0; i < p.length; i = i + 1) {
            w[i] = p[i];
        }
        return w;
    }

    public static Double[] toWrapper(double... p) {
        var w = new Double[p.length];
        for (int i = 0; i < p.length; i = i + 1) {
            w[i] = p[i];
        }
        return w;
    }

    public static Boolean[] toWrapper(boolean... p) {
        var w = new Boolean[p.length];
        for (int i = 0; i < p.length; i = i + 1) {
            w[i] = p[i];
        }
        return w;
    }

    public static Character[] toWrapper(char... p) {
        var w = new Character[p.length];
        for (int i = 0; i < p.length; i = i + 1) {
            w[i] = p[i];
        }
        return w;
    }

    //********************** toWrapper END *************************


    //************* swap START ***************

    public static void swap(byte[] arr, int i, int j) {
        var tmp = arr[i];
        arr[i] = arr[j];
        arr[j] = tmp;
    }

    public static void swap(short[] arr, int i, int j) {
        var tmp = arr[i];
        arr[i] = arr[j];
        arr[j] = tmp;
    }

    public static void swap(int[] arr, int i, int j) {
        var tmp = arr[i];
        arr[i] = arr[j];
        arr[j] = tmp;
    }

    public static void swap(long[] arr, int i, int j) {
        var tmp = arr[i];
        arr[i] = arr[j];
        arr[j] = tmp;
    }

    public static void swap(float[] arr, int i, int j) {
        var tmp = arr[i];
        arr[i] = arr[j];
        arr[j] = tmp;
    }

    public static void swap(double[] arr, int i, int j) {
        var tmp = arr[i];
        arr[i] = arr[j];
        arr[j] = tmp;
    }

    public static void swap(boolean[] arr, int i, int j) {
        var tmp = arr[i];
        arr[i] = arr[j];
        arr[j] = tmp;
    }

    public static void swap(char[] arr, int i, int j) {
        var tmp = arr[i];
        arr[i] = arr[j];
        arr[j] = tmp;
    }

    public static void swap(Object[] arr, int i, int j) {
        var tmp = arr[i];
        arr[i] = arr[j];
        arr[j] = tmp;
    }

    //************* swap END ***************


    //************* shuffle START ***************

    public static void shuffle(byte[] arr) {
        for (var i = arr.length; i > 1; i = i - 1) {
            swap(arr, i - 1, RandomUtils.randomInt(i));
        }
    }

    public static void shuffle(short[] arr) {
        for (var i = arr.length; i > 1; i = i - 1) {
            swap(arr, i - 1, RandomUtils.randomInt(i));
        }
    }

    public static void shuffle(int[] arr) {
        for (var i = arr.length; i > 1; i = i - 1) {
            swap(arr, i - 1, RandomUtils.randomInt(i));
        }
    }

    public static void shuffle(long[] arr) {
        for (var i = arr.length; i > 1; i = i - 1) {
            swap(arr, i - 1, RandomUtils.randomInt(i));
        }
    }

    public static void shuffle(float[] arr) {
        for (var i = arr.length; i > 1; i = i - 1) {
            swap(arr, i - 1, RandomUtils.randomInt(i));
        }
    }

    public static void shuffle(double[] arr) {
        for (var i = arr.length; i > 1; i = i - 1) {
            swap(arr, i - 1, RandomUtils.randomInt(i));
        }
    }

    public static void shuffle(boolean[] arr) {
        for (var i = arr.length; i > 1; i = i - 1) {
            swap(arr, i - 1, RandomUtils.randomInt(i));
        }
    }

    public static void shuffle(char[] arr) {
        for (var i = arr.length; i > 1; i = i - 1) {
            swap(arr, i - 1, RandomUtils.randomInt(i));
        }
    }

    public static void shuffle(Object[] arr) {
        for (var i = arr.length; i > 1; i = i - 1) {
            swap(arr, i - 1, RandomUtils.randomInt(i));
        }
    }

    //************* shuffle END ***************


    //************* reverse START ***************

    public static void reverse(byte[] arr) {
        for (int i = 0, mid = arr.length / 2; i < mid; i = i + 1) {
            swap(arr, i, arr.length - i - 1);
        }
    }

    public static void reverse(short[] arr) {
        for (int i = 0, mid = arr.length / 2; i < mid; i = i + 1) {
            swap(arr, i, arr.length - i - 1);
        }
    }

    public static void reverse(int[] arr) {
        for (int i = 0, mid = arr.length / 2; i < mid; i = i + 1) {
            swap(arr, i, arr.length - i - 1);
        }
    }

    public static void reverse(long[] arr) {
        for (int i = 0, mid = arr.length / 2; i < mid; i = i + 1) {
            swap(arr, i, arr.length - i - 1);
        }
    }

    public static void reverse(float[] arr) {
        for (int i = 0, mid = arr.length / 2; i < mid; i = i + 1) {
            swap(arr, i, arr.length - i - 1);
        }
    }

    public static void reverse(double[] arr) {
        for (int i = 0, mid = arr.length / 2; i < mid; i = i + 1) {
            swap(arr, i, arr.length - i - 1);
        }
    }

    public static void reverse(boolean[] arr) {
        for (int i = 0, mid = arr.length / 2; i < mid; i = i + 1) {
            swap(arr, i, arr.length - i - 1);
        }
    }

    public static void reverse(char[] arr) {
        for (int i = 0, mid = arr.length / 2; i < mid; i = i + 1) {
            swap(arr, i, arr.length - i - 1);
        }
    }

    public static void reverse(Object[] arr) {
        for (int i = 0, mid = arr.length / 2; i < mid; i = i + 1) {
            swap(arr, i, arr.length - i - 1);
        }
    }

    //************* reverse END ***************


    //************* indexOf START ***************

    public static int indexOf(byte[] a, int startPosition, int maxLength, byte... a1) {
        var endIndex = Math.min(a.length, startPosition + maxLength) - a1.length;
        for (var i = startPosition; i <= endIndex; i = i + 1) {
            var found = true;
            for (var j = 0; j < a1.length; j = j + 1) {
                if (a[i + j] != a1[j]) {
                    found = false;
                    break;
                }
            }
            if (found) {
                return i;
            }
        }
        return -1;
    }

    public static int indexOf(short[] a, int startPosition, int maxLength, short... a1) {
        var endIndex = Math.min(a.length, startPosition + maxLength) - a1.length;
        for (var i = startPosition; i <= endIndex; i = i + 1) {
            var found = true;
            for (var j = 0; j < a1.length; j = j + 1) {
                if (a[i + j] != a1[j]) {
                    found = false;
                    break;
                }
            }
            if (found) {
                return i;
            }
        }
        return -1;
    }

    public static int indexOf(int[] a, int startPosition, int maxLength, int... a1) {
        var endIndex = Math.min(a.length, startPosition + maxLength) - a1.length;
        for (var i = startPosition; i <= endIndex; i = i + 1) {
            var found = true;
            for (var j = 0; j < a1.length; j = j + 1) {
                if (a[i + j] != a1[j]) {
                    found = false;
                    break;
                }
            }
            if (found) {
                return i;
            }
        }
        return -1;
    }

    public static int indexOf(long[] a, int startPosition, int maxLength, long... a1) {
        var endIndex = Math.min(a.length, startPosition + maxLength) - a1.length;
        for (var i = startPosition; i <= endIndex; i = i + 1) {
            var found = true;
            for (var j = 0; j < a1.length; j = j + 1) {
                if (a[i + j] != a1[j]) {
                    found = false;
                    break;
                }
            }
            if (found) {
                return i;
            }
        }
        return -1;
    }

    public static int indexOf(float[] a, int startPosition, int maxLength, float... a1) {
        var endIndex = Math.min(a.length, startPosition + maxLength) - a1.length;
        for (var i = startPosition; i <= endIndex; i = i + 1) {
            var found = true;
            for (var j = 0; j < a1.length; j = j + 1) {
                if (a[i + j] != a1[j]) {
                    found = false;
                    break;
                }
            }
            if (found) {
                return i;
            }
        }
        return -1;
    }

    public static int indexOf(double[] a, int startPosition, int maxLength, double... a1) {
        var endIndex = Math.min(a.length, startPosition + maxLength) - a1.length;
        for (var i = startPosition; i <= endIndex; i = i + 1) {
            var found = true;
            for (var j = 0; j < a1.length; j = j + 1) {
                if (a[i + j] != a1[j]) {
                    found = false;
                    break;
                }
            }
            if (found) {
                return i;
            }
        }
        return -1;
    }

    public static int indexOf(boolean[] a, int startPosition, int maxLength, boolean... a1) {
        var endIndex = Math.min(a.length, startPosition + maxLength) - a1.length;
        for (var i = startPosition; i <= endIndex; i = i + 1) {
            var found = true;
            for (var j = 0; j < a1.length; j = j + 1) {
                if (a[i + j] != a1[j]) {
                    found = false;
                    break;
                }
            }
            if (found) {
                return i;
            }
        }
        return -1;
    }

    public static int indexOf(char[] a, int startPosition, int maxLength, char... a1) {
        var endIndex = Math.min(a.length, startPosition + maxLength) - a1.length;
        for (var i = startPosition; i <= endIndex; i = i + 1) {
            var found = true;
            for (var j = 0; j < a1.length; j = j + 1) {
                if (a[i + j] != a1[j]) {
                    found = false;
                    break;
                }
            }
            if (found) {
                return i;
            }
        }
        return -1;
    }

    public static int indexOf(Object[] a, int startPosition, int maxLength, Object... a1) {
        var endIndex = Math.min(a.length, startPosition + maxLength) - a1.length;
        for (var i = startPosition; i <= endIndex; i = i + 1) {
            var found = true;
            for (var j = 0; j < a1.length; j = j + 1) {
                if (!Objects.equals(a[i + j], a1[j])) {
                    found = false;
                    break;
                }
            }
            if (found) {
                return i;
            }
        }
        return -1;
    }

    public static int indexOf(byte[] a, byte... a1) {
        return indexOf(a, 0, a.length, a1);
    }

    public static int indexOf(short[] a, short... a1) {
        return indexOf(a, 0, a.length, a1);
    }

    public static int indexOf(int[] a, int... a1) {
        return indexOf(a, 0, a.length, a1);
    }

    public static int indexOf(long[] a, long... a1) {
        return indexOf(a, 0, a.length, a1);
    }

    public static int indexOf(float[] a, float... a1) {
        return indexOf(a, 0, a.length, a1);
    }

    public static int indexOf(double[] a, double... a1) {
        return indexOf(a, 0, a.length, a1);
    }

    public static int indexOf(boolean[] a, boolean... a1) {
        return indexOf(a, 0, a.length, a1);
    }

    public static int indexOf(char[] a, char... a1) {
        return indexOf(a, 0, a.length, a1);
    }

    public static int indexOf(Object[] a, Object... a1) {
        return indexOf(a, 0, a.length, a1);
    }

    public static int indexOf(byte[] a, byte a1) {
        for (var i = 0; i < a.length; i = i + 1) {
            if (a[i] == a1) {
                return i;
            }
        }
        return -1;
    }

    public static int indexOf(short[] a, short a1) {
        for (var i = 0; i < a.length; i = i + 1) {
            if (a[i] == a1) {
                return i;
            }
        }
        return -1;
    }

    public static int indexOf(int[] a, int a1) {
        for (var i = 0; i < a.length; i = i + 1) {
            if (a[i] == a1) {
                return i;
            }
        }
        return -1;
    }

    public static int indexOf(long[] a, long a1) {
        for (var i = 0; i < a.length; i = i + 1) {
            if (a[i] == a1) {
                return i;
            }
        }
        return -1;
    }

    public static int indexOf(float[] a, float a1) {
        for (var i = 0; i < a.length; i = i + 1) {
            if (a[i] == a1) {
                return i;
            }
        }
        return -1;
    }

    public static int indexOf(double[] a, double a1) {
        for (var i = 0; i < a.length; i = i + 1) {
            if (a[i] == a1) {
                return i;
            }
        }
        return -1;
    }

    public static int indexOf(boolean[] a, boolean a1) {
        for (var i = 0; i < a.length; i = i + 1) {
            if (a[i] == a1) {
                return i;
            }
        }
        return -1;
    }

    public static int indexOf(char[] a, char a1) {
        for (var i = 0; i < a.length; i = i + 1) {
            if (a[i] == a1) {
                return i;
            }
        }
        return -1;
    }

    public static int indexOf(Object[] a, Object a1) {
        for (var i = 0; i < a.length; i = i + 1) {
            if (Objects.equals(a[i], a1)) {
                return i;
            }
        }
        return -1;
    }

    public static int indexOf(byte[] a, int startPosition, int maxLength, byte a1) {
        var endIndex = Math.min(a.length, startPosition + maxLength);
        for (var i = startPosition; i < endIndex; i = i + 1) {
            if (a[i] == a1) {
                return i;
            }
        }
        return -1;
    }

    public static int indexOf(short[] a, int startPosition, int maxLength, short a1) {
        var endIndex = Math.min(a.length, startPosition + maxLength);
        for (var i = startPosition; i < endIndex; i = i + 1) {
            if (a[i] == a1) {
                return i;
            }
        }
        return -1;
    }

    public static int indexOf(int[] a, int startPosition, int maxLength, int a1) {
        var endIndex = Math.min(a.length, startPosition + maxLength);
        for (var i = startPosition; i < endIndex; i = i + 1) {
            if (a[i] == a1) {
                return i;
            }
        }
        return -1;
    }

    public static int indexOf(long[] a, int startPosition, int maxLength, long a1) {
        var endIndex = Math.min(a.length, startPosition + maxLength);
        for (var i = startPosition; i < endIndex; i = i + 1) {
            if (a[i] == a1) {
                return i;
            }
        }
        return -1;
    }

    public static int indexOf(float[] a, int startPosition, int maxLength, float a1) {
        var endIndex = Math.min(a.length, startPosition + maxLength);
        for (var i = startPosition; i < endIndex; i = i + 1) {
            if (a[i] == a1) {
                return i;
            }
        }
        return -1;
    }

    public static int indexOf(double[] a, int startPosition, int maxLength, double a1) {
        var endIndex = Math.min(a.length, startPosition + maxLength);
        for (var i = startPosition; i < endIndex; i = i + 1) {
            if (a[i] == a1) {
                return i;
            }
        }
        return -1;
    }

    public static int indexOf(boolean[] a, int startPosition, int maxLength, boolean a1) {
        var endIndex = Math.min(a.length, startPosition + maxLength);
        for (var i = startPosition; i < endIndex; i = i + 1) {
            if (a[i] == a1) {
                return i;
            }
        }
        return -1;
    }

    public static int indexOf(char[] a, int startPosition, int maxLength, char a1) {
        var endIndex = Math.min(a.length, startPosition + maxLength);
        for (var i = startPosition; i < endIndex; i = i + 1) {
            if (a[i] == a1) {
                return i;
            }
        }
        return -1;
    }

    public static int indexOf(Object[] a, int startPosition, int maxLength, Object a1) {
        var endIndex = Math.min(a.length, startPosition + maxLength);
        for (var i = startPosition; i < endIndex; i = i + 1) {
            if (Objects.equals(a[i], a1)) {
                return i;
            }
        }
        return -1;
    }

    //************* indexOf END ***************


    //********************* concat START *********************

    public static byte[] concat(byte[] first, byte... second) {
        var result = Arrays.copyOf(first, first.length + second.length);
        System.arraycopy(second, 0, result, first.length, second.length);
        return result;
    }

    public static short[] concat(short[] first, short... second) {
        var result = Arrays.copyOf(first, first.length + second.length);
        System.arraycopy(second, 0, result, first.length, second.length);
        return result;
    }

    public static int[] concat(int[] first, int... second) {
        var result = Arrays.copyOf(first, first.length + second.length);
        System.arraycopy(second, 0, result, first.length, second.length);
        return result;
    }

    public static long[] concat(long[] first, long... second) {
        var result = Arrays.copyOf(first, first.length + second.length);
        System.arraycopy(second, 0, result, first.length, second.length);
        return result;
    }

    public static float[] concat(float[] first, float... second) {
        var result = Arrays.copyOf(first, first.length + second.length);
        System.arraycopy(second, 0, result, first.length, second.length);
        return result;
    }

    public static double[] concat(double[] first, double... second) {
        var result = Arrays.copyOf(first, first.length + second.length);
        System.arraycopy(second, 0, result, first.length, second.length);
        return result;
    }

    public static boolean[] concat(boolean[] first, boolean... second) {
        var result = Arrays.copyOf(first, first.length + second.length);
        System.arraycopy(second, 0, result, first.length, second.length);
        return result;
    }

    public static char[] concat(char[] first, char... second) {
        var result = Arrays.copyOf(first, first.length + second.length);
        System.arraycopy(second, 0, result, first.length, second.length);
        return result;
    }

    @SafeVarargs
    public static <T> T[] concat(T[] first, T... second) {
        T[] result = Arrays.copyOf(first, first.length + second.length);
        System.arraycopy(second, 0, result, first.length, second.length);
        return result;
    }

    //********************* concat END *********************


    //********************* splitArray START *****************

    public static byte[][] splitArray(byte[] arr, int sliceSize) {
        var numOfSlices = numOfSlices(arr.length, sliceSize);
        var result = new byte[numOfSlices][];
        for (int i = 0; i < numOfSlices; i = i + 1) {
            var start = i * sliceSize;
            var end = Math.min(start + sliceSize, arr.length);
            result[i] = Arrays.copyOfRange(arr, start, end);
        }
        return result;
    }

    public static short[][] splitArray(short[] arr, int sliceSize) {
        var numOfSlices = numOfSlices(arr.length, sliceSize);
        var result = new short[numOfSlices][];
        for (int i = 0; i < numOfSlices; i = i + 1) {
            var start = i * sliceSize;
            var end = Math.min(start + sliceSize, arr.length);
            result[i] = Arrays.copyOfRange(arr, start, end);
        }
        return result;
    }

    public static int[][] splitArray(int[] arr, int sliceSize) {
        var numOfSlices = numOfSlices(arr.length, sliceSize);
        var result = new int[numOfSlices][];
        for (int i = 0; i < numOfSlices; i = i + 1) {
            var start = i * sliceSize;
            var end = Math.min(start + sliceSize, arr.length);
            result[i] = Arrays.copyOfRange(arr, start, end);
        }
        return result;
    }

    public static long[][] splitArray(long[] arr, int sliceSize) {
        var numOfSlices = numOfSlices(arr.length, sliceSize);
        var result = new long[numOfSlices][];
        for (int i = 0; i < numOfSlices; i = i + 1) {
            var start = i * sliceSize;
            var end = Math.min(start + sliceSize, arr.length);
            result[i] = Arrays.copyOfRange(arr, start, end);
        }
        return result;
    }

    public static float[][] splitArray(float[] arr, int sliceSize) {
        var numOfSlices = numOfSlices(arr.length, sliceSize);
        var result = new float[numOfSlices][];
        for (int i = 0; i < numOfSlices; i = i + 1) {
            var start = i * sliceSize;
            var end = Math.min(start + sliceSize, arr.length);
            result[i] = Arrays.copyOfRange(arr, start, end);
        }
        return result;
    }

    public static double[][] splitArray(double[] arr, int sliceSize) {
        var numOfSlices = numOfSlices(arr.length, sliceSize);
        var result = new double[numOfSlices][];
        for (int i = 0; i < numOfSlices; i = i + 1) {
            var start = i * sliceSize;
            var end = Math.min(start + sliceSize, arr.length);
            result[i] = Arrays.copyOfRange(arr, start, end);
        }
        return result;
    }

    public static boolean[][] splitArray(boolean[] arr, int sliceSize) {
        var numOfSlices = numOfSlices(arr.length, sliceSize);
        var result = new boolean[numOfSlices][];
        for (int i = 0; i < numOfSlices; i = i + 1) {
            var start = i * sliceSize;
            var end = Math.min(start + sliceSize, arr.length);
            result[i] = Arrays.copyOfRange(arr, start, end);
        }
        return result;
    }

    public static char[][] splitArray(char[] arr, int sliceSize) {
        var numOfSlices = numOfSlices(arr.length, sliceSize);
        var result = new char[numOfSlices][];
        for (int i = 0; i < numOfSlices; i = i + 1) {
            var start = i * sliceSize;
            var end = Math.min(start + sliceSize, arr.length);
            result[i] = Arrays.copyOfRange(arr, start, end);
        }
        return result;
    }

    @SuppressWarnings("unchecked")
    public static <T> T[][] splitArray(T[] arr, int sliceSize) {
        int numOfSlices = numOfSlices(arr.length, sliceSize);
        var result = (T[][]) Array.newInstance(arr.getClass().getComponentType(), numOfSlices, 0);
        for (int i = 0; i < numOfSlices; i = i + 1) {
            var start = i * sliceSize;
            var end = Math.min(start + sliceSize, arr.length);
            result[i] = Arrays.copyOfRange(arr, start, end);
        }
        return result;
    }

    /**
     * 按照指定长度切割 List, 注意和 {@link ArrayUtils#splitListN(List, int)}} 进行区分
     *
     * @param list list
     * @param size 每份的长度
     * @param <T>  T
     * @return 切割后的 list
     */
    public static <T> List<List<T>> splitList(List<T> list, int size) {
        List<List<T>> result = new ArrayList<>();
        for (int i = 0; i < list.size(); i = i + size) {
            int end = Math.min(i + size, list.size());
            result.add(list.subList(i, end));
        }
        return result;
    }

    //********************* splitArray END *****************

    //********************* splitArrayN START *****************

    public static byte[][] splitArrayN(byte[] arr, int n) {
        return splitArray(arr, numOfSlices(arr.length, n));
    }

    public static short[][] splitArrayN(short[] arr, int n) {
        return splitArray(arr, numOfSlices(arr.length, n));
    }

    public static int[][] splitArrayN(int[] arr, int n) {
        return splitArray(arr, numOfSlices(arr.length, n));
    }

    public static long[][] splitArrayN(long[] arr, int n) {
        return splitArray(arr, numOfSlices(arr.length, n));
    }

    public static float[][] splitArrayN(float[] arr, int n) {
        return splitArray(arr, numOfSlices(arr.length, n));
    }

    public static double[][] splitArrayN(double[] arr, int n) {
        return splitArray(arr, numOfSlices(arr.length, n));
    }

    public static boolean[][] splitArrayN(boolean[] arr, int n) {
        return splitArray(arr, numOfSlices(arr.length, n));
    }

    public static char[][] splitArrayN(char[] arr, int n) {
        return splitArray(arr, numOfSlices(arr.length, n));
    }

    /**
     * 按照指定份数切割 List, 注意和 {@link ArrayUtils#splitList(List, int)} 进行区分
     *
     * @param arr arr
     * @param n   份数
     * @param <T> T
     * @return 切割后的 list
     */
    public static <T> T[][] splitArrayN(T[] arr, int n) {
        return splitArray(arr, numOfSlices(arr.length, n));
    }

    /**
     * 按照指定份数切割 List, 注意和 {@link ArrayUtils#splitList(List, int)} 进行区分
     *
     * @param list list
     * @param n    份数
     * @param <T>  T
     * @return 切割后的 list
     */
    public static <T> List<List<T>> splitListN(List<T> list, int n) {
        return splitList(list, numOfSlices(list.size(), n));
    }

    //********************* splitArrayN END *****************

    public static Object[] toObjectArray(Object source) {
        if (source instanceof Object[] objectArr) {
            return objectArr;
        }
        if (source == null) {
            return new Object[0];
        }
        if (source instanceof Collection<?> collection) {
            return collection.toArray();
        }
        if (source.getClass().isArray()) {
            return switch (source) {
                case byte[] arr -> toWrapper(arr);
                case short[] arr -> toWrapper(arr);
                case int[] arr -> toWrapper(arr);
                case long[] arr -> toWrapper(arr);
                case float[] arr -> toWrapper(arr);
                case double[] arr -> toWrapper(arr);
                case boolean[] arr -> toWrapper(arr);
                case char[] arr -> toWrapper(arr);
                default -> throw new IllegalStateException("错误值 : " + source);
            };
        }
        throw new IllegalArgumentException("源数据无法转换为数组对象 !!!");
    }

    public static long[] toLongArray(int... intArray) {
        var longArray = new long[intArray.length];
        for (int i = 0; i < intArray.length; i = i + 1) {
            longArray[i] = intArray[i];
        }
        return longArray;
    }

    /**
     * 计算 长度可以被分割为几个子长度 (向上取整)
     *
     * @param length l
     * @param n      n
     * @return c
     */
    public static int numOfSlices(int length, int n) {
        return length % n > 0 ? length / n + 1 : length / n;
    }

}
