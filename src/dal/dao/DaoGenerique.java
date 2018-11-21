package dal.dao;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import biz.factory.BizFactory;
import dal.services.DalBackendServices;
import exceptions.FatalException;
import exceptions.OptimisticLockException;
import util.AppContext;
import util.AppContext.DependanceInjection;
import util.MonLogger;
import util.Util;

public abstract class DaoGenerique<E> {

  @DependanceInjection
  protected DalBackendServices dalBackendServices;

  @DependanceInjection
  protected AppContext appContext;

  @DependanceInjection
  protected BizFactory factory;

  @DependanceInjection
  private MonLogger monLogger;

  private Class<E> classe;
  private String nomClasse;
  private String nomTableDb;

  private void initDaoGenerique() {
    if (classe == null) {
      classe = (Class<E>) appContext.getClassValueProp(
          ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[0]
              .getTypeName());
    }
    nomClasse = classe.getSimpleName();
    if (classe == appContext.getClassValueProp("biz.user.UserDto")) {
      nomClasse = appContext.getValueProp("traduction_user_dto");
    }
    if (classe == appContext.getClassValueProp("biz.pdc.PersonneContactDto")) {
      nomTableDb = appContext.getValueProp("traduction_pdc_dto");
    } else {
      nomTableDb = nomClasse.substring(0, nomClasse.length() - 4).toLowerCase() + "s";
    }
  }

  /**
   * Insert un élément dans la base de données.
   * 
   * @param element L'objet à rajouter dans la base de données
   * @return l'élement inséré
   */
  public E insert(E element) {
    initDaoGenerique();
    Map<String, Object> atts = getAttributeNamesAndValues(element);
    Deque<Object> values = new ArrayDeque<Object>();

    StringBuffer buf = new StringBuffer("INSERT INTO stagify." + nomTableDb + "(");
    for (Entry<String, Object> entry : atts.entrySet()) {
      if (!entry.getKey()
          .equals("id_" + nomClasse.substring(0, nomClasse.length() - 4)
              .replaceAll("(.)([A-Z])", "$1_$2").toLowerCase())
          && !entry.getKey().contains("dto")) {
        buf.append(entry.getKey() + ", ");
        if (entry.getValue() == null) {
          values.addLast(appContext.getValueProp("null_indicator"));
        } else {
          values.addLast(entry.getValue());
        }
      }
    }

    buf.setLength(buf.length() - 2);
    buf.append(") VALUES(");
    for (int i = 0; i < values.size(); i++) {
      buf.append("?, ");
    }
    buf.setLength(buf.length() - 2);
    buf.append(")");
    String query = buf.toString();

    PreparedStatement ps = dalBackendServices.getPreparedStatementForInsert(query);
    setPreparedStatementObjects(ps, values);

    try {
      int codeRetour = ps.executeUpdate();
      if (codeRetour == 1) {
        try (ResultSet generatedKeys = ps.getGeneratedKeys()) {
          if (generatedKeys.next()) {
            Method method = null;
            method = element.getClass().getDeclaredMethod(
                "setId" + nomClasse.substring(0, nomClasse.length() - 4), int.class);
            method.setAccessible(true);
            method.invoke(element, generatedKeys.getInt(1));
          }
        } catch (NoSuchMethodException | SecurityException | IllegalAccessException
            | IllegalArgumentException | InvocationTargetException e1) {
          monLogger.getMonLog().severe(Util.stackTraceToString(e1));
          throw new FatalException("Erreur de programmation");
        }
        return element;
      } else {
        throw new FatalException("Erreur de programmation");
      }
    } catch (SQLException ex) {
      monLogger.getMonLog().fine(Util.stackTraceToString(ex));
      throw new FatalException("Erreur lors de l'accès à la DB");
    }
  }

  /**
   * Met à jour l'élement dans la base de données.
   * 
   * @param element l'élément à mettre à jour
   * @return l'élément mis à jour
   */
  public E update(E element) {
    initDaoGenerique();

    Map<String, Object> atts = getAttributeNamesAndValues(element);
    Deque<Object> values = new ArrayDeque<Object>();

    StringBuffer buf = new StringBuffer("UPDATE stagify." + nomTableDb + " SET ");
    for (Entry<String, Object> entry : atts.entrySet()) {
      if ((!entry.getKey()
          .equals("id_" + nomClasse.substring(0, nomClasse.length() - 4)
              .replaceAll("(.)([A-Z])", "$1_$2").toLowerCase())
          && !entry.getKey().contains("dto")) && !entry.getKey().equals("num_version")) {
        buf.append(entry.getKey() + " = ? , ");
        if (entry.getValue() == null) {
          values.addLast(appContext.getValueProp("null_indicator"));
        } else {
          values.addLast(entry.getValue());
        }
      }
    }
    buf.append(
        "num_version = num_version+1 WHERE id_" + nomClasse.substring(0, nomClasse.length() - 4)
            .replaceAll("(.)([A-Z])", "$1_$2").toLowerCase() + "= ? AND num_version = ?");
    PreparedStatement ps = dalBackendServices.getPreparedStatement(buf.toString());
    int nbrValues = values.size();
    setPreparedStatementObjects(ps, values);

    try {
      ps.setObject(nbrValues + 1, atts.get("id_" + nomClasse.substring(0, nomClasse.length() - 4)
          .replaceAll("(.)([A-Z])", "$1_$2").toLowerCase()));
      ps.setObject(nbrValues + 2, atts.get("num_version"));
    } catch (SQLException ex) {
      monLogger.getMonLog().fine(Util.stackTraceToString(ex));
      throw new FatalException("Erreur lors de l'accès à la DB");
    }

    try {
      int codeRetour = ps.executeUpdate();
      if (codeRetour == 1) {
        Method methSetNumVersion = null;

        try {
          methSetNumVersion = element.getClass().getDeclaredMethod("setNumVersion", int.class);
          methSetNumVersion.setAccessible(true);
          methSetNumVersion.invoke(element, Integer.parseInt(atts.get("num_version") + "") + 1);
        } catch (NoSuchMethodException | SecurityException | IllegalAccessException
            | IllegalArgumentException | InvocationTargetException e1) {
          monLogger.getMonLog().severe(Util.stackTraceToString(e1));
          throw new FatalException("Erreur de programmation");
        }
        return element;
      } else if (codeRetour == 0) {
        throw new OptimisticLockException(
            "Modifications depuis le chargement de cette page, ressayez",
            get(Integer
                .parseInt("" + atts.get("id_" + nomClasse.substring(0, nomClasse.length() - 4)
                    .replaceAll("(.)([A-Z])", "$1_$2").toLowerCase()))));
      } else {
        throw new FatalException("Erreur de programmation");
      }
    } catch (SQLException ex) {
      monLogger.getMonLog().fine(Util.stackTraceToString(ex));
      throw new FatalException("Erreur lors de l'accès DB update()");
    }
  }

  /**
   * Récupère l'élément en base de données via son identifiant.
   * 
   * @param id le numéro de l'objet en base de données
   * @return l'élément récupéré
   */
  public E get(int id) {
    initDaoGenerique();

    String query = "SELECT * FROM stagify." + nomTableDb + " WHERE id_" + nomClasse
        .substring(0, nomClasse.length() - 4).replaceAll("(.)([A-Z])", "$1_$2").toLowerCase()
        + " = ?";

    List<E> objects = null;
    try {
      PreparedStatement ps = dalBackendServices.getPreparedStatement(query);
      ps.setInt(1, id);
      try (ResultSet resultSet = ps.executeQuery()) {
        objects = setResultSet(resultSet);
      }
    } catch (SQLException ex) {
      monLogger.getMonLog().fine(Util.stackTraceToString(ex));
      throw new FatalException("Erreur lors de l'accès à la DB");
    }
    if (objects == null || objects.size() > 1) {
      throw new FatalException("Erreur de programmation");
    }
    if (objects.isEmpty()) {
      return null;
    }
    return objects.get(0);
  }

  public List<Object> getAll() {
    return null;
  }

  private void setPreparedStatementObjects(PreparedStatement ps, Deque<Object> objects) {
    int nbrValues = objects.size();
    for (int i = 1; i <= nbrValues; i++) {
      try {
        Object att = objects.pollFirst();
        if (att.getClass() == LocalDate.class) {
          Timestamp date = Timestamp.valueOf(((LocalDate) att).atStartOfDay());
          ps.setObject(i, date);
        } else if (att.equals(appContext.getValueProp("null_indicator"))) {
          ps.setObject(i, null);
        } else {
          ps.setObject(i, att);
        }
      } catch (SQLException ex) {
        monLogger.getMonLog().fine(Util.stackTraceToString(ex));
        throw new FatalException("Erreur lors de l'accès DB update()");
      }
    }
  }

  private Map<String, Object> getAttributeNamesAndValues(Object object) {
    Map<String, Object> atts = new HashMap<String, Object>();
    for (Field field : classe.getDeclaredFields()) {
      field.setAccessible(true);
      try {
        atts.put(field.getName().replaceAll("(.)([A-Z])", "$1_$2").toLowerCase(),
            field.get(object));
      } catch (IllegalArgumentException | IllegalAccessException ex) {
        monLogger.getMonLog().severe(Util.stackTraceToString(ex));
        throw new FatalException("Erreur de programmation");
      }

    }
    return atts;
  }

  /**
   * Méthode qui set un nombre arbitraire d'objets sur un PreparedStatement. La méthode itère sur
   * chaque objet passé en paramètre et appele la méthode setObject() sur le PreparedStatement.
   * 
   * @param ps un PreparedStatement déjà créé
   * @param objects un nombre arbitraire d'objets de type Object contenant l'input de l'utilisateur
   */
  public void setPreparedStatement(PreparedStatement ps, Object... objects) {
    try {
      // le premier indice est à 1
      for (int i = 1; i <= objects.length; i++) {
        ps.setObject(i, objects[i - 1]);
      }
    } catch (SQLException ex) {
      monLogger.getMonLog()
          .severe("Erreur lors du prepareStatement\n\t" + Util.stackTraceToString(ex));
      throw new FatalException("Erreur lors de la customisation du preparedStatement");
    }
  }

  /**
   * Méthode qui : * vérifie s'il existe au moins un tuple pour un ResultSet et renvoie false le cas
   * contraire; * parcourt chaque tuple de ce ResultSet grace à un objet metadata. Pour chaque
   * tuple, la méthode : * récupère et renomme le nom de la colonne (un champ dans la BD) * recupère
   * par introspection les méthodes de la classe instanciée passée en paramètre. Pour chaque méthode
   * propres à l'instance, la méthode : * vérifie qu'il s'agit d'un setter et qu'il correspond au
   * nom de la colonne * set sur l'instance la valeur de la colonne du tuple en cours.
   * 
   * @param rs un ResultSet, résultat d'un preparedStatement.executeQuery()
   * @return une liste de Object remplie ou vide
   */
  public <E> List<E> setResultSet(ResultSet rs) {
    initDaoGenerique();
    try {
      ResultSetMetaData rsmd = rs.getMetaData(); // objet metadata
      List<E> liste = new ArrayList<E>();
      List<Method> setters = new ArrayList<Method>();
      if (rs.next()) {
        E instance = (E) classe.newInstance();
        for (Method method : classe.getDeclaredMethods()) {
          if (method.getName().startsWith("set")) {
            setters.add(method);
          }
        }
        do {
          instance = (E) classe.newInstance();
          for (int i = 1; i <= rsmd.getColumnCount(); i++) {
            String nomColonne = rsmd.getColumnName(i).replace("_", "").toLowerCase();
            for (Method setter : setters) {
              if (nomColonne.equals(setter.getName().toLowerCase().substring(3))) {
                if (rs.getObject(i) == null) {
                  // passer à l'itération suivante
                  continue;
                }
                if (rs.getObject(i).getClass().getName().equals("java.sql.Timestamp")) {
                  Timestamp ts = (Timestamp) rs.getObject(i);
                  setter.invoke(instance, ts.toLocalDateTime().toLocalDate());
                } else {
                  setter.invoke(instance, rs.getObject(i));
                }
              }
            }
          }
          liste.add(instance);
        } while (rs.next());
      }
      return liste;

    } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException
        | SQLException | InstantiationException ex) {
      monLogger.getMonLog().severe("Accès à la DB impossible\n\t" + Util.stackTraceToString(ex));
      throw new FatalException("Accès à la DB impossible");
    }
  }

}
