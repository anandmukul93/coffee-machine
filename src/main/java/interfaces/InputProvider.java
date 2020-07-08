package interfaces;

import entities.Order;

public interface InputProvider<T extends Order> {
    boolean hasNextInput();

    T getNextInput();
}
