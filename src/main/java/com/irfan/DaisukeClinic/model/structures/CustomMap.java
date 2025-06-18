package com.irfan.DaisukeClinic.model.structures;

import com.irfan.DaisukeClinic.model.structures.MyLinkedList;
import com.irfan.DaisukeClinic.model.Appointment;

public class CustomMap {
    private static class Entry {
        String key;
        MyLinkedList<Appointment> value;
        Entry next;

        Entry(String key, MyLinkedList<Appointment> value) {
            this.key = key;
            this.value = value;
            this.next = null;
        }
    }

    private Entry[] table;
    private int size;
    private int capacity;

    public CustomMap() {
        capacity = 16;
        table = new Entry[capacity];
        size = 0;
    }

    private int hash(String key) {
        int hash = 0;
        for (int i = 0; i < key.length(); i++) {
            hash = (hash * 31 + key.charAt(i)) % capacity;
        }
        return hash < 0 ? hash + capacity : hash;
    }

    public void put(String key, MyLinkedList<Appointment> value) {
        int index = hash(key);
        Entry entry = table[index];

        while (entry != null) {
            if (entry.key.equals(key)) {
                entry.value = value;
                return;
            }
            entry = entry.next;
        }

        if ((double) size >= capacity * 0.75) {
            resize();
            index = hash(key);
        }

        Entry newEntry = new Entry(key, value);
        newEntry.next = table[index];
        table[index] = newEntry;
        size++;
    }

    public MyLinkedList<Appointment> get(String key) {
        int index = hash(key);
        Entry entry = table[index];
        while (entry != null) {
            if (entry.key.equals(key)) {
                return entry.value;
            }
            entry = entry.next;
        }
        return null;
    }

    private void resize() {
        int oldCapacity = capacity;
        capacity *= 2;
        Entry[] newTable = new Entry[capacity];

        Entry[] oldTable = table;

        table = newTable;
        size = 0;

        for (int i = 0; i < oldCapacity; i++) {
            Entry current = oldTable[i];
            while (current != null) {
                Entry nextEntry = current.next;
                put(current.key, current.value);
                current = nextEntry;
            }
        }
    }

    public int size() {
        return size;
    }

    public boolean containsKey(String key) {
        return get(key) != null;
    }
}