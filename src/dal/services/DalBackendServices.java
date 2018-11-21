package dal.services;

import java.sql.PreparedStatement;

public interface DalBackendServices {

  /*
   * Récupère le PrepareStatement donné via la query.
   * 
   * @param query Une chaîne de caractère représentant la query
   * 
   * @return Un objet de type PreparedStatement
   */
  public PreparedStatement getPreparedStatement(String query);

  /**
   * Récupère le PreparedStatement pour une insertion via la query.
   * 
   * @param query Une chaîne de caractère représentant la query
   * @return Un objet de type PreparedStatement
   */
  public PreparedStatement getPreparedStatementForInsert(String query);

}
