package com.irfan.DaisukeClinic.model.structures;

import com.irfan.DaisukeClinic.model.Appointment;
import java.util.EmptyStackException;

public class MyPriorityQueue {
    private Appointment[] heap;
    private int size;
    private int capacity;

    @SuppressWarnings("unchecked")
    public MyPriorityQueue(int capacity) {
        this.capacity = capacity;
        this.heap = new Appointment[capacity];
        this.size = 0;
    }

    public boolean isEmpty() {
        return size == 0;
    }

    public boolean isFull() {
        return size == capacity;
    }

    public void enqueue(Appointment appointment) {
        if (isFull()) {
            System.out.println("Priority Queue is full. Cannot enqueue appointment.");
            return;
        }
        heap[size] = appointment;
        heapifyUp(size);
        size++;
    }

    public Appointment dequeue() {
        if (isEmpty()) {
            System.out.println("Priority Queue is empty. No appointment to dequeue.");
            throw new EmptyStackException();
        }
        Appointment minAppointment = heap[0];
        heap[0] = heap[size - 1];
        heap[size - 1] = null;
        size--;
        if (size > 0) {
            heapifyDown(0);
        }
        return minAppointment;
    }

    public Appointment peek() {
        if (isEmpty()) {
            return null;
        }
        return heap[0];
    }

    public int size() {
        return size;
    }

    public Appointment[] toArray() {
        if (isEmpty()) {
            return null;
        }
        Appointment[] result = new Appointment[size];
        for (int i = 0; i < size; i++) {
            result[i] = heap[i];
        }
        return result;
    }

    public void displayAll() {
        if (isEmpty()) {
            System.out.println("Priority Queue is empty.");
            return;
        }
        System.out.println("Priority Queue elements (Heap Order):");
        for (int i = 0; i < size; i++) {
            System.out.println(heap[i]);
        }
        System.out.println("Note: Displayed in heap order, not chronological order.");
    }

    // --- Heap Helper Methods (Min-Heap) ---
    private int getParentIndex(int i) { return (i - 1) / 2; }
    private int getLeftChildIndex(int i) { return 2 * i + 1; }
    private int getRightChildIndex(int i) { return 2 * i + 2; }

    private boolean hasParent(int i) { return getParentIndex(i) >= 0; }
    private boolean hasLeftChild(int i) { return getLeftChildIndex(i) < size; }
    private boolean hasRightChild(int i) { return getRightChildIndex(i) < size; }

    private Appointment parent(int i) { return heap[getParentIndex(i)]; }
    private Appointment leftChild(int i) { return heap[getLeftChildIndex(i)]; }
    private Appointment rightChild(int i) { return heap[getRightChildIndex(i)]; }

    private void swap(int index1, int index2) {
        Appointment temp = heap[index1];
        heap[index1] = heap[index2];
        heap[index2] = temp;
    }

    private void heapifyUp(int index) {
        while (hasParent(index) && heap[index].getAppointmentTime().isBefore(parent(index).getAppointmentTime())) {
            swap(index, getParentIndex(index));
            index = getParentIndex(index);
        }
    }

    private void heapifyDown(int index) {
        int smallestChildIndex;
        while (hasLeftChild(index)) {
            smallestChildIndex = getLeftChildIndex(index);
            if (hasRightChild(index) && rightChild(index).getAppointmentTime().isBefore(leftChild(index).getAppointmentTime())) {
                smallestChildIndex = getRightChildIndex(index);
            }

            if (heap[index].getAppointmentTime().isBefore(heap[smallestChildIndex].getAppointmentTime()) ||
                    heap[index].getAppointmentTime().isEqual(heap[smallestChildIndex].getAppointmentTime())) {
                break;
            } else {
                swap(index, smallestChildIndex);
            }
            index = smallestChildIndex;
        }
    }
}