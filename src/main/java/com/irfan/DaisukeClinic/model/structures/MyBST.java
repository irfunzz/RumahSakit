package com.irfan.DaisukeClinic.model.structures;

public class MyBST<T extends Comparable<T>> {
    private class Node {
        T data;
        Node left, right;
        int height;

        Node(T data) {
            this.data = data;
            left = right = null;
            height = 1;
        }
    }

    private Node root;

    public MyBST() {
        root = null;
    }

    // --- Helper Methods ---
    private int height(Node node) {
        return (node == null) ? 0 : node.height;
    }

    private int getBalance(Node node) {
        return (node == null) ? 0 : height(node.left) - height(node.right);
    }

    private void updateHeight(Node node) {
        if (node != null) {
            node.height = 1 + Math.max(height(node.left), height(node.right));
        }
    }

    // --- Rotations ---
    // Right Rotate (Rotasi Kanan)
    private Node rightRotate(Node y) {
        Node x = y.left;
        Node T2 = x.right;

        // Perform rotation
        x.right = y;
        y.left = T2;

        // Update heights
        updateHeight(y);
        updateHeight(x);

        return x;
    }

    // Left Rotate (Rotasi Kiri)
    private Node leftRotate(Node x) {
        Node y = x.right;
        Node T2 = y.left;

        // Perform rotation
        y.left = x;
        x.right = T2;

        // Update heights
        updateHeight(x);
        updateHeight(y);

        return y;
    }

    // --- Insert Operation ---
    public void insert(T data) {
        root = insertRecursive(root, data);
    }

    private Node insertRecursive(Node node, T data) {
        if (node == null) {
            return new Node(data);
        }

        int cmp = data.compareTo(node.data);
        if (cmp < 0) {
            node.left = insertRecursive(node.left, data);
        } else if (cmp > 0) {
            node.right = insertRecursive(node.right, data);
        } else {
            return node;
        }

        updateHeight(node);

        int balance = getBalance(node);

        if (balance > 1 && data.compareTo(node.left.data) < 0) {
            return rightRotate(node);
        }

        if (balance < -1 && data.compareTo(node.right.data) > 0) {
            return leftRotate(node);
        }

        if (balance > 1 && data.compareTo(node.left.data) > 0) {
            node.left = leftRotate(node.left);
            return rightRotate(node);
        }

        if (balance < -1 && data.compareTo(node.right.data) < 0) {
            node.right = rightRotate(node.right);
            return leftRotate(node);
        }

        return node;
    }

    // --- Delete Operation ---
    public void delete(T data) {
        root = deleteRecursive(root, data);
    }

    private Node deleteRecursive(Node rootNode, T data) {
        if (rootNode == null) {
            return rootNode;
        }

        int cmp = data.compareTo(rootNode.data);
        if (cmp < 0) {
            rootNode.left = deleteRecursive(rootNode.left, data);
        } else if (cmp > 0) {
            rootNode.right = deleteRecursive(rootNode.right, data);
        } else {
            if ((rootNode.left == null) || (rootNode.right == null)) {
                Node temp = (rootNode.left != null) ? rootNode.left : rootNode.right;

                if (temp == null) {
                    return null;
                } else {
                    return temp;
                }
            } else {
                Node temp = findMin(rootNode.right);
                rootNode.data = temp.data;
                rootNode.right = deleteRecursive(rootNode.right, temp.data);
            }
        }

        if (rootNode == null) {
            return rootNode;
        }

        updateHeight(rootNode);

        int balance = getBalance(rootNode);

        if (balance > 1 && getBalance(rootNode.left) >= 0) {
            return rightRotate(rootNode);
        }

        if (balance > 1 && getBalance(rootNode.left) < 0) {
            rootNode.left = leftRotate(rootNode.left);
            return rightRotate(rootNode);
        }

        if (balance < -1 && getBalance(rootNode.right) <= 0) {
            return leftRotate(rootNode);
        }

        if (balance < -1 && getBalance(rootNode.right) > 0) {
            rootNode.right = rightRotate(rootNode.right);
            return leftRotate(rootNode);
        }

        return rootNode;
    }

    private Node findMin(Node node) {
        Node current = node;
        while (current.left != null) {
            current = current.left;
        }
        return current;
    }

    // --- Search Operation ---
    public T search(T data) {
        Node found = searchRecursive(root, data);
        return found != null ? found.data : null;
    }

    private Node searchRecursive(Node current, T data) {
        if (current == null) {
            return null;
        }
        int cmp = data.compareTo(current.data);
        if (cmp == 0) {
            return current;
        } else if (cmp < 0) {
            return searchRecursive(current.left, data);
        } else {
            return searchRecursive(current.right, data);
        }
    }

    // --- In-Order Display ---
    public void inOrderDisplay() {
        if (root == null) {
            System.out.println("Tree is empty.");
            return;
        }
        inOrderRecursive(root);
    }

    private void inOrderRecursive(Node node) {
        if (node != null) {
            inOrderRecursive(node.left);
            System.out.println(node.data);
            inOrderRecursive(node.right);
        }
    }
}