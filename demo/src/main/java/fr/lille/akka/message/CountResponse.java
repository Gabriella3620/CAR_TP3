package fr.lille.akka.message;

import scala.Serializable;

import java.io.Serial;

public class CountResponse implements Serializable {
    @Serial
    private static final long serialVersionUID = 4L;
    private final int count;

    public CountResponse(int count) {
        this.count = count;
    }

    public int getCount() {
        return count;
    }
}
