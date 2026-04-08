package cool.scx.common.util;

import java.lang.reflect.Array;
import java.util.*;

import static cool.scx.common.util.RandomUtils.randomInt;

/// ArrayUtils
///
/// @author scx567888
/// @version 0.0.1
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

    public static void shuffle(byte... arr) {
        for (var i = arr.length; i > 1; i = i - 1) {
            swap(arr, i - 1, randomInt(i));
        }
    }

    public static void shuffle(short... arr) {
        for (var i = arr.length; i > 1; i = i - 1) {
            swap(arr, i - 1, randomInt(i));
        }
    }

    public static void shuffle(int... arr) {
        for (var i = arr.length; i > 1; i = i - 1) {
            swap(arr, i - 1, randomInt(i));
        }
    }

    public static void shuffle(long... arr) {
        for (var i = arr.length; i > 1; i = i - 1) {
            swap(arr, i - 1, randomInt(i));
        }
    }

    public static void shuffle(float... arr) {
        for (var i = arr.length; i > 1; i = i - 1) {
            swap(arr, i - 1, randomInt(i));
        }
    }

    public static void shuffle(double... arr) {
        for (var i = arr.length; i > 1; i = i - 1) {
            swap(arr, i - 1, randomInt(i));
        }
    }

    public static void shuffle(boolean... arr) {
        for (var i = arr.length; i > 1; i = i - 1) {
            swap(arr, i - 1, randomInt(i));
        }
    }

    public static void shuffle(char... arr) {
        for (var i = arr.length; i > 1; i = i - 1) {
            swap(arr, i - 1, randomInt(i));
        }
    }

    public static void shuffle(Object... arr) {
        for (var i = arr.length; i > 1; i = i - 1) {
            swap(arr, i - 1, randomInt(i));
        }
    }

    //************* shuffle END ***************


    //************* reverse START ***************

    public static void reverse(byte... arr) {
        for (int i = 0, mid = arr.length / 2; i < mid; i = i + 1) {
            swap(arr, i, arr.length - i - 1);
        }
    }

    public static void reverse(short... arr) {
        for (int i = 0, mid = arr.length / 2; i < mid; i = i + 1) {
            swap(arr, i, arr.length - i - 1);
        }
    }

    public static void reverse(int... arr) {
        for (int i = 0, mid = arr.length / 2; i < mid; i = i + 1) {
            swap(arr, i, arr.length - i - 1);
        }
    }

    public static void reverse(long... arr) {
        for (int i = 0, mid = arr.length / 2; i < mid; i = i + 1) {
            swap(arr, i, arr.length - i - 1);
        }
    }

    public static void reverse(float... arr) {
        for (int i = 0, mid = arr.length / 2; i < mid; i = i + 1) {
            swap(arr, i, arr.length - i - 1);
        }
    }

    public static void reverse(double... arr) {
        for (int i = 0, mid = arr.length / 2; i < mid; i = i + 1) {
            swap(arr, i, arr.length - i - 1);
        }
    }

    public static void reverse(boolean... arr) {
        for (int i = 0, mid = arr.length / 2; i < mid; i = i + 1) {
            swap(arr, i, arr.length - i - 1);
        }
    }

    public static void reverse(char... arr) {
        for (int i = 0, mid = arr.length / 2; i < mid; i = i + 1) {
            swap(arr, i, arr.length - i - 1);
        }
    }

    public static void reverse(Object... arr) {
        for (int i = 0, mid = arr.length / 2; i < mid; i = i + 1) {
            swap(arr, i, arr.length - i - 1);
        }
    }

    //************* reverse END ***************


    //************* indexOf START ***************

    public static int indexOf(byte[] a, int startPosition, int maxLength, byte... b) {
        var endIndex = Math.min(a.length, startPosition + maxLength) - b.length;
        for (var i = startPosition; i <= endIndex; i = i + 1) {
            var found = true;
            for (var j = 0; j < b.length; j = j + 1) {
                if (a[i + j] != b[j]) {
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

    public static int indexOf(short[] a, int startPosition, int maxLength, short... b) {
        var endIndex = Math.min(a.length, startPosition + maxLength) - b.length;
        for (var i = startPosition; i <= endIndex; i = i + 1) {
            var found = true;
            for (var j = 0; j < b.length; j = j + 1) {
                if (a[i + j] != b[j]) {
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

    public static int indexOf(int[] a, int startPosition, int maxLength, int... b) {
        var endIndex = Math.min(a.length, startPosition + maxLength) - b.length;
        for (var i = startPosition; i <= endIndex; i = i + 1) {
            var found = true;
            for (var j = 0; j < b.length; j = j + 1) {
                if (a[i + j] != b[j]) {
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

    public static int indexOf(long[] a, int startPosition, int maxLength, long... b) {
        var endIndex = Math.min(a.length, startPosition + maxLength) - b.length;
        for (var i = startPosition; i <= endIndex; i = i + 1) {
            var found = true;
            for (var j = 0; j < b.length; j = j + 1) {
                if (a[i + j] != b[j]) {
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

    public static int indexOf(float[] a, int startPosition, int maxLength, float... b) {
        var endIndex = Math.min(a.length, startPosition + maxLength) - b.length;
        for (var i = startPosition; i <= endIndex; i = i + 1) {
            var found = true;
            for (var j = 0; j < b.length; j = j + 1) {
                if (a[i + j] != b[j]) {
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

    public static int indexOf(double[] a, int startPosition, int maxLength, double... b) {
        var endIndex = Math.min(a.length, startPosition + maxLength) - b.length;
        for (var i = startPosition; i <= endIndex; i = i + 1) {
            var found = true;
            for (var j = 0; j < b.length; j = j + 1) {
                if (a[i + j] != b[j]) {
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

    public static int indexOf(boolean[] a, int startPosition, int maxLength, boolean... b) {
        var endIndex = Math.min(a.length, startPosition + maxLength) - b.length;
        for (var i = startPosition; i <= endIndex; i = i + 1) {
            var found = true;
            for (var j = 0; j < b.length; j = j + 1) {
                if (a[i + j] != b[j]) {
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

    public static int indexOf(char[] a, int startPosition, int maxLength, char... b) {
        var endIndex = Math.min(a.length, startPosition + maxLength) - b.length;
        for (var i = startPosition; i <= endIndex; i = i + 1) {
            var found = true;
            for (var j = 0; j < b.length; j = j + 1) {
                if (a[i + j] != b[j]) {
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

    public static int indexOf(Object[] a, int startPosition, int maxLength, Object... b) {
        var endIndex = Math.min(a.length, startPosition + maxLength) - b.length;
        for (var i = startPosition; i <= endIndex; i = i + 1) {
            var found = true;
            for (var j = 0; j < b.length; j = j + 1) {
                if (!Objects.equals(a[i + j], b[j])) {
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

    public static int indexOf(byte[] a, byte... b) {
        return indexOf(a, 0, a.length, b);
    }

    public static int indexOf(short[] a, short... b) {
        return indexOf(a, 0, a.length, b);
    }

    public static int indexOf(int[] a, int... b) {
        return indexOf(a, 0, a.length, b);
    }

    public static int indexOf(long[] a, long... b) {
        return indexOf(a, 0, a.length, b);
    }

    public static int indexOf(float[] a, float... b) {
        return indexOf(a, 0, a.length, b);
    }

    public static int indexOf(double[] a, double... b) {
        return indexOf(a, 0, a.length, b);
    }

    public static int indexOf(boolean[] a, boolean... b) {
        return indexOf(a, 0, a.length, b);
    }

    public static int indexOf(char[] a, char... b) {
        return indexOf(a, 0, a.length, b);
    }

    public static int indexOf(Object[] a, Object... b) {
        return indexOf(a, 0, a.length, b);
    }

    public static int indexOf(byte[] a, int startPosition, int maxLength, byte b) {
        var endIndex = Math.min(a.length, startPosition + maxLength);
        for (var i = startPosition; i < endIndex; i = i + 1) {
            if (a[i] == b) {
                return i;
            }
        }
        return -1;
    }

    public static int indexOf(short[] a, int startPosition, int maxLength, short b) {
        var endIndex = Math.min(a.length, startPosition + maxLength);
        for (var i = startPosition; i < endIndex; i = i + 1) {
            if (a[i] == b) {
                return i;
            }
        }
        return -1;
    }

    public static int indexOf(int[] a, int startPosition, int maxLength, int b) {
        var endIndex = Math.min(a.length, startPosition + maxLength);
        for (var i = startPosition; i < endIndex; i = i + 1) {
            if (a[i] == b) {
                return i;
            }
        }
        return -1;
    }

    public static int indexOf(long[] a, int startPosition, int maxLength, long b) {
        var endIndex = Math.min(a.length, startPosition + maxLength);
        for (var i = startPosition; i < endIndex; i = i + 1) {
            if (a[i] == b) {
                return i;
            }
        }
        return -1;
    }

    public static int indexOf(float[] a, int startPosition, int maxLength, float b) {
        var endIndex = Math.min(a.length, startPosition + maxLength);
        for (var i = startPosition; i < endIndex; i = i + 1) {
            if (a[i] == b) {
                return i;
            }
        }
        return -1;
    }

    public static int indexOf(double[] a, int startPosition, int maxLength, double b) {
        var endIndex = Math.min(a.length, startPosition + maxLength);
        for (var i = startPosition; i < endIndex; i = i + 1) {
            if (a[i] == b) {
                return i;
            }
        }
        return -1;
    }

    public static int indexOf(boolean[] a, int startPosition, int maxLength, boolean b) {
        var endIndex = Math.min(a.length, startPosition + maxLength);
        for (var i = startPosition; i < endIndex; i = i + 1) {
            if (a[i] == b) {
                return i;
            }
        }
        return -1;
    }

    public static int indexOf(char[] a, int startPosition, int maxLength, char b) {
        var endIndex = Math.min(a.length, startPosition + maxLength);
        for (var i = startPosition; i < endIndex; i = i + 1) {
            if (a[i] == b) {
                return i;
            }
        }
        return -1;
    }

    public static int indexOf(Object[] a, int startPosition, int maxLength, Object b) {
        var endIndex = Math.min(a.length, startPosition + maxLength);
        for (var i = startPosition; i < endIndex; i = i + 1) {
            if (Objects.equals(a[i], b)) {
                return i;
            }
        }
        return -1;
    }

    public static int indexOf(byte[] a, byte b) {
        return indexOf(a, 0, a.length, b);
    }

    public static int indexOf(short[] a, short b) {
        return indexOf(a, 0, a.length, b);
    }

    public static int indexOf(int[] a, int b) {
        return indexOf(a, 0, a.length, b);
    }

    public static int indexOf(long[] a, long b) {
        return indexOf(a, 0, a.length, b);
    }

    public static int indexOf(float[] a, float b) {
        return indexOf(a, 0, a.length, b);
    }

    public static int indexOf(double[] a, double b) {
        return indexOf(a, 0, a.length, b);
    }

    public static int indexOf(boolean[] a, boolean b) {
        return indexOf(a, 0, a.length, b);
    }

    public static int indexOf(char[] a, char b) {
        return indexOf(a, 0, a.length, b);
    }

    public static int indexOf(Object[] a, Object b) {
        return indexOf(a, 0, a.length, b);
    }

    //************* indexOf END ***************


    //********************* concat START *********************

    public static byte[] concat(byte[]... arrays) {
        int totalLength = 0;
        for (var arr : arrays) {
            if (arr != null) {
                totalLength += arr.length;
            }
        }
        var result = new byte[totalLength];
        var pos = 0;
        for (var arr : arrays) {
            if (arr != null) {
                System.arraycopy(arr, 0, result, pos, arr.length);
                pos += arr.length;
            }
        }
        return result;
    }

    public static short[] concat(short[]... arrays) {
        int totalLength = 0;
        for (var arr : arrays) {
            if (arr != null) {
                totalLength += arr.length;
            }
        }
        var result = new short[totalLength];
        var pos = 0;
        for (var arr : arrays) {
            if (arr != null) {
                System.arraycopy(arr, 0, result, pos, arr.length);
                pos += arr.length;
            }
        }
        return result;
    }

    public static int[] concat(int[]... arrays) {
        int totalLength = 0;
        for (var arr : arrays) {
            if (arr != null) {
                totalLength += arr.length;
            }
        }
        var result = new int[totalLength];
        var pos = 0;
        for (var arr : arrays) {
            if (arr != null) {
                System.arraycopy(arr, 0, result, pos, arr.length);
                pos += arr.length;
            }
        }
        return result;
    }

    public static long[] concat(long[]... arrays) {
        int totalLength = 0;
        for (var arr : arrays) {
            if (arr != null) {
                totalLength += arr.length;
            }
        }
        var result = new long[totalLength];
        var pos = 0;
        for (var arr : arrays) {
            if (arr != null) {
                System.arraycopy(arr, 0, result, pos, arr.length);
                pos += arr.length;
            }
        }
        return result;
    }

    public static float[] concat(float[]... arrays) {
        int totalLength = 0;
        for (var arr : arrays) {
            if (arr != null) {
                totalLength += arr.length;
            }
        }
        var result = new float[totalLength];
        var pos = 0;
        for (var arr : arrays) {
            if (arr != null) {
                System.arraycopy(arr, 0, result, pos, arr.length);
                pos += arr.length;
            }
        }
        return result;
    }

    public static double[] concat(double[]... arrays) {
        int totalLength = 0;
        for (var arr : arrays) {
            if (arr != null) {
                totalLength += arr.length;
            }
        }
        var result = new double[totalLength];
        var pos = 0;
        for (var arr : arrays) {
            if (arr != null) {
                System.arraycopy(arr, 0, result, pos, arr.length);
                pos += arr.length;
            }
        }
        return result;
    }

    public static boolean[] concat(boolean[]... arrays) {
        int totalLength = 0;
        for (var arr : arrays) {
            if (arr != null) {
                totalLength += arr.length;
            }
        }
        var result = new boolean[totalLength];
        var pos = 0;
        for (var arr : arrays) {
            if (arr != null) {
                System.arraycopy(arr, 0, result, pos, arr.length);
                pos += arr.length;
            }
        }
        return result;
    }

    public static char[] concat(char[]... arrays) {
        int totalLength = 0;
        for (var arr : arrays) {
            if (arr != null) {
                totalLength += arr.length;
            }
        }
        var result = new char[totalLength];
        var pos = 0;
        for (var arr : arrays) {
            if (arr != null) {
                System.arraycopy(arr, 0, result, pos, arr.length);
                pos += arr.length;
            }
        }
        return result;
    }

    /// 该方法可以正确处理数组协变.
    /// 因为 `arrays` 参数在编译期会被推断为传入数组的最小公共父类 (即协变后的上界类型),
    /// 所以可以通过 `arrays.getClass().componentType().componentType()` 来获取推断后的组件类型.
    @SuppressWarnings("unchecked")
    public static <T> T[] concat(T[]... arrays) {
        int totalLength = 0;
        for (var arr : arrays) {
            if (arr != null) {
                totalLength += arr.length;
            }
        }
        var result = (T[]) Array.newInstance(arrays.getClass().componentType().componentType(), totalLength);
        var pos = 0;
        for (var arr : arrays) {
            if (arr != null) {
                System.arraycopy(arr, 0, result, pos, arr.length);
                pos += arr.length;
            }
        }
        return result;
    }


    //********************* concat END *********************


    //********************* splitArray START *****************

    /// 计算 长度可以被分割为几个子长度 (向上取整)
    private static int numOfSlices(int length, int n) {
        return (length + n - 1) / n;
    }

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

    /// 按照指定长度切割数组
    @SuppressWarnings("unchecked")
    public static <T> T[][] splitArray(T[] arr, int sliceSize) {
        int numOfSlices = numOfSlices(arr.length, sliceSize);
        var result = (T[][]) Array.newInstance(arr.getClass().componentType(), numOfSlices, 0);
        for (int i = 0; i < numOfSlices; i = i + 1) {
            var start = i * sliceSize;
            var end = Math.min(start + sliceSize, arr.length);
            result[i] = Arrays.copyOfRange(arr, start, end);
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

    /// 按照指定份数切割 数组, 注意和 {@link ArrayUtils#splitArray(Object[], int)} 进行区分
    public static <T> T[][] splitArrayN(T[] arr, int n) {
        return splitArray(arr, numOfSlices(arr.length, n));
    }

    //********************* splitArrayN END *****************


    //********************* subArray START *****************

    public static void subArrayCheck(int fromIndex, int toIndex, int size) {
        if (fromIndex < 0) {
            throw new ArrayIndexOutOfBoundsException("fromIndex = " + fromIndex);
        }
        if (toIndex > size) {
            throw new ArrayIndexOutOfBoundsException("toIndex = " + toIndex);
        }
        if (fromIndex > toIndex) {
            throw new ArrayIndexOutOfBoundsException("fromIndex(" + fromIndex + ") > toIndex(" + toIndex + ")");
        }
    }

    public static byte[] subArray(byte[] array, int fromIndex, int toIndex) {
        subArrayCheck(fromIndex, toIndex, array.length);
        var subArray = new byte[toIndex - fromIndex];
        System.arraycopy(array, fromIndex, subArray, 0, subArray.length);
        return subArray;
    }

    public static short[] subArray(short[] array, int fromIndex, int toIndex) {
        subArrayCheck(fromIndex, toIndex, array.length);
        var subArray = new short[toIndex - fromIndex];
        System.arraycopy(array, fromIndex, subArray, 0, subArray.length);
        return subArray;
    }

    public static int[] subArray(int[] array, int fromIndex, int toIndex) {
        subArrayCheck(fromIndex, toIndex, array.length);
        var subArray = new int[toIndex - fromIndex];
        System.arraycopy(array, fromIndex, subArray, 0, subArray.length);
        return subArray;
    }

    public static long[] subArray(long[] array, int fromIndex, int toIndex) {
        subArrayCheck(fromIndex, toIndex, array.length);
        var subArray = new long[toIndex - fromIndex];
        System.arraycopy(array, fromIndex, subArray, 0, subArray.length);
        return subArray;
    }

    public static float[] subArray(float[] array, int fromIndex, int toIndex) {
        subArrayCheck(fromIndex, toIndex, array.length);
        var subArray = new float[toIndex - fromIndex];
        System.arraycopy(array, fromIndex, subArray, 0, subArray.length);
        return subArray;
    }

    public static double[] subArray(double[] array, int fromIndex, int toIndex) {
        subArrayCheck(fromIndex, toIndex, array.length);
        var subArray = new double[toIndex - fromIndex];
        System.arraycopy(array, fromIndex, subArray, 0, subArray.length);
        return subArray;
    }

    public static boolean[] subArray(boolean[] array, int fromIndex, int toIndex) {
        subArrayCheck(fromIndex, toIndex, array.length);
        var subArray = new boolean[toIndex - fromIndex];
        System.arraycopy(array, fromIndex, subArray, 0, subArray.length);
        return subArray;
    }

    public static char[] subArray(char[] array, int fromIndex, int toIndex) {
        subArrayCheck(fromIndex, toIndex, array.length);
        var subArray = new char[toIndex - fromIndex];
        System.arraycopy(array, fromIndex, subArray, 0, subArray.length);
        return subArray;
    }

    @SuppressWarnings("unchecked")
    public static <T> T[] subArray(T[] array, int fromIndex, int toIndex) {
        subArrayCheck(fromIndex, toIndex, array.length);
        var subArray = (T[]) Array.newInstance(array.getClass().componentType(), toIndex - fromIndex);
        System.arraycopy(array, fromIndex, subArray, 0, subArray.length);
        return subArray;
    }

    public static byte[] safeSubArray(byte[] array, int fromIndex, int toIndex) {
        if (fromIndex < 0) {
            fromIndex = 0;
        }
        if (toIndex > array.length) {
            toIndex = array.length;
        }
        if (fromIndex >= toIndex) {
            return new byte[]{};
        }
        var subArray = new byte[toIndex - fromIndex];
        System.arraycopy(array, fromIndex, subArray, 0, subArray.length);
        return subArray;
    }

    public static short[] safeSubArray(short[] array, int fromIndex, int toIndex) {
        if (fromIndex < 0) {
            fromIndex = 0;
        }
        if (toIndex > array.length) {
            toIndex = array.length;
        }
        if (fromIndex >= toIndex) {
            return new short[]{};
        }
        var subArray = new short[toIndex - fromIndex];
        System.arraycopy(array, fromIndex, subArray, 0, subArray.length);
        return subArray;
    }

    public static int[] safeSubArray(int[] array, int fromIndex, int toIndex) {
        if (fromIndex < 0) {
            fromIndex = 0;
        }
        if (toIndex > array.length) {
            toIndex = array.length;
        }
        if (fromIndex >= toIndex) {
            return new int[]{};
        }
        var subArray = new int[toIndex - fromIndex];
        System.arraycopy(array, fromIndex, subArray, 0, subArray.length);
        return subArray;
    }

    public static long[] safeSubArray(long[] array, int fromIndex, int toIndex) {
        if (fromIndex < 0) {
            fromIndex = 0;
        }
        if (toIndex > array.length) {
            toIndex = array.length;
        }
        if (fromIndex >= toIndex) {
            return new long[]{};
        }
        var subArray = new long[toIndex - fromIndex];
        System.arraycopy(array, fromIndex, subArray, 0, subArray.length);
        return subArray;
    }

    public static float[] safeSubArray(float[] array, int fromIndex, int toIndex) {
        if (fromIndex < 0) {
            fromIndex = 0;
        }
        if (toIndex > array.length) {
            toIndex = array.length;
        }
        if (fromIndex >= toIndex) {
            return new float[]{};
        }
        var subArray = new float[toIndex - fromIndex];
        System.arraycopy(array, fromIndex, subArray, 0, subArray.length);
        return subArray;
    }

    public static double[] safeSubArray(double[] array, int fromIndex, int toIndex) {
        if (fromIndex < 0) {
            fromIndex = 0;
        }
        if (toIndex > array.length) {
            toIndex = array.length;
        }
        if (fromIndex >= toIndex) {
            return new double[]{};
        }
        var subArray = new double[toIndex - fromIndex];
        System.arraycopy(array, fromIndex, subArray, 0, subArray.length);
        return subArray;
    }

    public static boolean[] safeSubArray(boolean[] array, int fromIndex, int toIndex) {
        if (fromIndex < 0) {
            fromIndex = 0;
        }
        if (toIndex > array.length) {
            toIndex = array.length;
        }
        if (fromIndex >= toIndex) {
            return new boolean[]{};
        }
        var subArray = new boolean[toIndex - fromIndex];
        System.arraycopy(array, fromIndex, subArray, 0, subArray.length);
        return subArray;
    }

    public static char[] safeSubArray(char[] array, int fromIndex, int toIndex) {
        if (fromIndex < 0) {
            fromIndex = 0;
        }
        if (toIndex > array.length) {
            toIndex = array.length;
        }
        if (fromIndex >= toIndex) {
            return new char[]{};
        }
        var subArray = new char[toIndex - fromIndex];
        System.arraycopy(array, fromIndex, subArray, 0, subArray.length);
        return subArray;
    }

    @SuppressWarnings("unchecked")
    public static <T> T[] safeSubArray(T[] array, int fromIndex, int toIndex) {
        if (fromIndex < 0) {
            fromIndex = 0;
        }
        if (toIndex > array.length) {
            toIndex = array.length;
        }
        if (fromIndex >= toIndex) {
            return (T[]) Array.newInstance(array.getClass().componentType(), 0);
        }
        var subArray = (T[]) Array.newInstance(array.getClass().componentType(), toIndex - fromIndex);
        System.arraycopy(array, fromIndex, subArray, 0, subArray.length);
        return subArray;
    }

    //********************* subArray END *****************


    //********************* 其他功能 START *****************

    /// 按照指定长度切割 List, 注意和 [#splitListN(List,int)]} 进行区分
    public static <T> List<List<T>> splitList(List<T> list, int size) {
        List<List<T>> result = new ArrayList<>();
        for (int i = 0; i < list.size(); i = i + size) {
            int end = Math.min(i + size, list.size());
            result.add(list.subList(i, end));
        }
        return result;
    }

    /// 按照指定份数切割 List, 注意和 [#splitList(List,int)] 进行区分
    public static <T> List<List<T>> splitListN(List<T> list, int n) {
        return splitList(list, numOfSlices(list.size(), n));
    }

    public static <T> List<T> subList(List<T> list, int fromIndex, int toIndex) {
        return list.subList(fromIndex, toIndex);
    }

    public static <T> List<T> safeSubList(List<T> list, int fromIndex, int toIndex) {
        if (fromIndex < 0) {
            fromIndex = 0;
        }
        if (toIndex > list.size()) {
            toIndex = list.size();
        }
        if (fromIndex >= toIndex) {
            return List.of();
        }
        return list.subList(fromIndex, toIndex);
    }

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

    //********************* 其他功能 END *****************

}
