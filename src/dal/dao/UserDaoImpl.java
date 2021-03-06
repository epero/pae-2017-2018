package dal.dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import biz.user.UserDto;
import exceptions.FatalException;
import util.AppContext.DependanceInjection;
import util.MonLogger;
import util.Util;


class UserDaoImpl extends DaoGenerique<UserDto> implements UserDao {

  @DependanceInjection
  private MonLogger monLogger;

  @Override
  public UserDto getUser(String pseudo) {

    PreparedStatement preparedStatement;
    List<UserDto> users;

    try {
      preparedStatement = super.dalBackendServices
          .getPreparedStatement(appContext.getValueProp("query_get_user_par_pseudo"));
      setPreparedStatement(preparedStatement, pseudo);
      try (ResultSet resultSet = preparedStatement.executeQuery()) {
        users = setResultSet(resultSet);
      }
    } catch (SQLException ex) {
      monLogger.getMonLog().fine(Util.stackTraceToString(ex));
      throw new FatalException(
          "Erreur lors de la récupération de l'utilisateur dans la base de données.");
    }
    if (users == null || users.isEmpty()) {
      return null;
    }
    return users.get(0);
  }

  @Override
  public UserDto getUser(int idUtilisateur) {
    return super.get(idUtilisateur);
  }

  @Override
  public List<UserDto> getAllUsers(String anneeAcademique) {
    PreparedStatement ps = super.dalBackendServices
        .getPreparedStatement(appContext.getValueProp("query_get_all_users"));
    setPreparedStatement(ps, anneeAcademique);

    List<UserDto> users;

    try (ResultSet rs = ps.executeQuery()) {
      users = setResultSet(rs);

    } catch (SQLException ex) {
      monLogger.getMonLog().fine(Util.stackTraceToString(ex));
      throw new FatalException(
          "Erreur lors de la récupération des entreprises dans la base de données.");
    }
    return users;
  }

  public UserDto updateUser(UserDto user) {
    return super.update(user);
  }


  @Override
  public boolean pseudoUserExiste(String pseudo) {
    PreparedStatement ps = super.dalBackendServices
        .getPreparedStatement(appContext.getValueProp("query_pseudo_user_existe"));
    setPreparedStatement(ps, pseudo);
    try (ResultSet rs = ps.executeQuery()) {
      if (rs.next()) {
        return true;
      } else {
        return false;
      }
    } catch (SQLException ex) {
      monLogger.getMonLog().fine(Util.stackTraceToString(ex));
      throw new FatalException("Erreur acces Db");
    }
  }

  @Override
  public boolean emailUserExiste(String email) {
    PreparedStatement ps = super.dalBackendServices
        .getPreparedStatement(appContext.getValueProp("query_email_user_existe"));
    setPreparedStatement(ps, email);
    try (ResultSet resultSet = ps.executeQuery()) {
      if (resultSet.next()) {
        return true;
      } else {
        return false;
      }
    } catch (SQLException ex) {
      monLogger.getMonLog().fine(Util.stackTraceToString(ex));
      throw new FatalException("Erreur acces Db");
    }
  }

  @Override
  public UserDto insertUser(UserDto user) {
    return super.insert(user);
  }

  @Override
  public HashMap<String, Integer> getStudentsStats(String anneeAcademique) {
    PreparedStatement preparedStatement;
    HashMap<String, Integer> stats;
    try {
      preparedStatement = super.dalBackendServices
          .getPreparedStatement(appContext.getValueProp("query_get_student_stats"));
      setPreparedStatement(preparedStatement, anneeAcademique);
      try (ResultSet resultSet = preparedStatement.executeQuery()) {
        if (resultSet.next()) {
          stats = new HashMap();
          stats.put(resultSet.getMetaData().getColumnName(2), resultSet.getInt(2));
          stats.put(resultSet.getMetaData().getColumnName(3), resultSet.getInt(3));
          stats.put(resultSet.getMetaData().getColumnName(4), resultSet.getInt(4));
          stats.put(resultSet.getMetaData().getColumnName(5), resultSet.getInt(5));
          stats.put(resultSet.getMetaData().getColumnName(6), resultSet.getInt(6));
        } else {
          throw new FatalException("Erreur de programmation");
        }
      }
    } catch (SQLException ex) {
      monLogger.getMonLog().fine(Util.stackTraceToString(ex));
      throw new FatalException("Erreur lors de la récupération des stats dans la base de données.");
    }
    if (stats == null || stats.isEmpty()) {
      throw new FatalException("Erreur de programmation");
    }
    return stats;
  }

  @Override
  public HashMap<String, Integer> getStudentStats(int idUser) {
    PreparedStatement preparedStatement;
    HashMap<String, Integer> stats;
    try {
      preparedStatement = super.dalBackendServices
          .getPreparedStatement(appContext.getValueProp("query_get_students_stats_par_id_user"));
      setPreparedStatement(preparedStatement, idUser);
      try (ResultSet resultSet = preparedStatement.executeQuery()) {
        if (resultSet.next()) {
          stats = new HashMap();
          stats.put(resultSet.getMetaData().getColumnName(2), resultSet.getInt(2));
          stats.put(resultSet.getMetaData().getColumnName(3), resultSet.getInt(3));
          stats.put(resultSet.getMetaData().getColumnName(4), resultSet.getInt(4));
        } else {
          throw new FatalException("Erreur de programmation");
        }
      }
    } catch (SQLException ex) {
      monLogger.getMonLog().fine(Util.stackTraceToString(ex));
      throw new FatalException("Erreur lors de la récupération des stats dans la base de données.");
    }
    if (stats == null || stats.isEmpty()) {
      throw new FatalException("Erreur de progrmmation");
    }
    return stats;
  }

}
