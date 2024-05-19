package markov;

import java.util.*;
import java.io.*;

public class TMarkov {
    private String text;
    private final int k;
    private final Map<String, List<String>> markovChain;
    private final Random random;

    public TMarkov(String text, int k) {
        this.text = text;
        this.k = k;
        this.markovChain = new HashMap<>();
        this.random = new Random();
        buildMarkovChain();
    }

    public void loadTextFromFile(String filePath) throws IOException {
        StringBuilder sb = new StringBuilder();
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                sb.append(line);
            }
        }
        this.text = sb.toString();
        buildMarkovChain();
    }

    private void buildMarkovChain() {
        markovChain.clear();
        for (int i = 0; i <= text.length() - k; i++) {
            String kevent = text.substring(i, i + k);
            String nextEvent = (i + k < text.length()) ? Character.toString(text.charAt(i + k)) : "";
            markovChain.putIfAbsent(kevent, new ArrayList<>());
            markovChain.get(kevent).add(nextEvent);
        }
    }

    public int freq(String kevent) {
        return markovChain.getOrDefault(kevent, Collections.emptyList()).size();
    }

    public int freq(String kevent, String c) {
        List<String> events = markovChain.getOrDefault(kevent, Collections.emptyList());
        int count = 0;
        for (String event : events) {
            if (event.equals(c)) {
                count++;
            }
        }
        return count;
    }

    public String rand(String kevent) {
        List<String> events = markovChain.getOrDefault(kevent, Collections.emptyList());
        if (events.isEmpty()) {
            return "";
        }
        return events.get(random.nextInt(events.size()));
    }

    public String gen(String kevent, int T) {
        StringBuilder generatedText = new StringBuilder(kevent);
        String currentEvent = kevent;
        while (generatedText.length() < T) {
            String nextEvent = rand(currentEvent);
            if (nextEvent.isEmpty()) {
                int start = random.nextInt(text.length() - k + 1);
                currentEvent = text.substring(start, start + k);
                nextEvent = rand(currentEvent);
            }
            generatedText.append(nextEvent);
            currentEvent = generatedText.substring(generatedText.length() - k);
        }
        return generatedText.toString();
    }
}

