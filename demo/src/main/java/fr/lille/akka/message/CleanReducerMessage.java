package fr.lille.akka.message;

import scala.Serializable;

public class CleanReducerMessage implements Serializable {
    private static final long serialVersionUID = 44L;
    private boolean deletedAll=false;

    public CleanReducerMessage(boolean deletedAll) {
        super();
        this.deletedAll = deletedAll;
    }

    public boolean isDeletedAll() {
        return deletedAll;
    }

    public void setDeletedAll(boolean deletedAll) {
        this.deletedAll = deletedAll;
    }

}
