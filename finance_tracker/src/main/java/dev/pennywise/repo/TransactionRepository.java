package main.java.dev.pennywise.repo;
import finance_tracker.model.TransactionRecord;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.function.Predicate;

/*
 - Simple in-memory repository demonstrating:
 - Arrays/ArrayList usage
 - defensive copying when returning lists
 - variable args and overloading in add methods
*/

public class TransactionRepository {
    private final List<TransactionRecord> storage = new ArrayList<>();

    public void add(TransactionRecord tr){
        storage.add(tr);
    }

// variable args and overloading example
    public void add(TransactionRecord...trs){
      Collections.addAll(storage, trs);
    }   

    public List<TransactionRecord> all() {
        // defensive copy
        return List.copyOf(storage);
    }

    public List<TransactionRecord> filter(Predicate<TransactionRecord> predicate) {
        var result = new ArrayList<TransactionRecord>();
        for (TransactionRecord t : storage) {
            if (predicate.test(t)) result.add(t);
        }
        return List.copyOf(result);
    }

    public void clear(){
        storage.clear();
    }
}