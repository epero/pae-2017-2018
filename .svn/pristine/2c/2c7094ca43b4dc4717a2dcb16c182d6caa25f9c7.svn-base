package exceptions;

public class OptimisticLockException extends RuntimeException {

  private Object objetEnDb;

  public OptimisticLockException(String message, Object objetEnDb) {
    super(message);
    this.objetEnDb = objetEnDb;
  }

  public OptimisticLockException() {

  }

  public OptimisticLockException(String message) {
    super(message);

  }

  public Object getObjetEnDb() {
    return objetEnDb;
  }

}
