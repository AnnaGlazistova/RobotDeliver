import java.util.*;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;

public class Main {
    public static final Map<Integer, Integer> sizeToFreq = new HashMap<>();

    public static void main(String[] args) throws ExecutionException, InterruptedException {
        final Callable<Integer> logic = () -> {
            String line = generateRoute("RLRFR", 100);
            int countR = 0;
            for (char element : line.toCharArray()) {
                if (element == 'R') {
                    countR++;
                }
            }
            return countR;
        };

        for (int i = 0; i < 1000; i++) {
            final FutureTask<Integer> futureTask = new FutureTask<>(logic);
            new Thread(futureTask).start();
            final int result = futureTask.get();

            synchronized (sizeToFreq) {
                if (sizeToFreq.containsKey(result)) {
                    sizeToFreq.put(result, sizeToFreq.get(result) + 1);
                } else {
                    sizeToFreq.put(result, 1);
                }
            }
        }

        synchronized (sizeToFreq) {
            int keyOfMaxValue = sizeToFreq.entrySet().stream().max((entry1, entry2) -> entry1.getValue() > entry2.getValue() ? 1 : -1).get().getKey();
            System.out.println("Самое частое число повторений R " + keyOfMaxValue + " встретилось " + sizeToFreq.get(keyOfMaxValue) + " раз(а), остальные же: ");
            sizeToFreq.remove(keyOfMaxValue);
            Iterator it = sizeToFreq.entrySet().iterator();
            while (it.hasNext()) {
                Map.Entry pair = (Map.Entry) it.next();
                System.out.println("- " + pair.getKey() + " встретилось " + pair.getValue() + " раз(а)");
            }
        }
    }

    public static String generateRoute(String letters, int length) {
        Random random = new Random();
        StringBuilder route = new StringBuilder();
        for (int i = 0; i < length; i++) {
            route.append(letters.charAt(random.nextInt(letters.length())));
        }
        return route.toString();
    }
}
