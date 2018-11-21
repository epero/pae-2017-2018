package dal.services;

import java.sql.PreparedStatement;

public class MockDalServicesImpl implements DalServices, DalBackendServices {

  public PreparedStatement getPreparedStatement(String query) {
    return null;
  }

  @Override
  public void startTransaction() {

  }

  @Override
  public void commitTransaction() {

  }

  @Override
  public void rollback() {

  }

  @Override
  public PreparedStatement getPreparedStatementForInsert(String query) {
    return null;
  }
}
