package ru.somehost.javaexamples.effectivecache;

import java.util.concurrent.CyclicBarrier;
import java.util.regex.Pattern;

public class TestCacheHelper {

    public static CyclicBarrier createCyclicBarrier(long sleep){
        return new CyclicBarrier(5, ()->{
            try {
                System.out.println("CyclicBarrier");
                Thread.sleep(sleep);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });
    }

    public static Computable<String, String> createComputable(long sleepisNumeric){
        return new Computable<String, String>() {
            public String compute(String arg) throws InterruptedException {
                System.out.println(String.format("Start calculate for %s", arg));
                try {
                    if (arg == null || arg.isEmpty()) {
                        return null;
                    }
                    if (isNumeric(arg)) {
                        Thread.sleep(sleepisNumeric);
                    } else {
                        throw new RuntimeException("CalculateError");
                    }
                    return reverseString(arg);
                } finally {
                    System.out.println(String.format("Fininsh calculate for %s", arg));
                }
            }
        };
    }



    private static final Pattern pattern = Pattern.compile("-?\\d+(\\.\\d+)?");
    public static boolean isNumeric(String strNum) {
        if (strNum == null) {
            return false;
        }
        return pattern.matcher(strNum).matches();
    }

    public static String reverseString(String arg) {
        byte[] strAsByteArray = arg.getBytes();

        byte[] result = new byte[strAsByteArray.length];

        for (int i = 0; i < strAsByteArray.length; i++)
            result[i] = strAsByteArray[strAsByteArray.length - i - 1];

        return new String(result);
    }
}
