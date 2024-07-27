package libext;

import java.io.BufferedReader;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Objects;

public class Lists {

  private Lists() {
  }

  public static ArrayList<String> newArrayList(BufferedReader reader) {
    Objects.requireNonNull(reader, "no valid reader provided");
    ArrayList<String> list = new ArrayList<>();
    boolean success = Readers.addAll(list, reader);
    if(!success) {
      throw new RuntimeException("failed to add any element");
    }
    return list;
  }

  public static LinkedList<String> newLinkedList(BufferedReader reader) {
    Objects.requireNonNull(reader, "no valid reader provided");
    LinkedList<String> list = new LinkedList<>();
    boolean success = Readers.addAll(list, reader);
    if(!success) {
      throw new RuntimeException("failed to add any element");
    }
    return list;
  }
}
