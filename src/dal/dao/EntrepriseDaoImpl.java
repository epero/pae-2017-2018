package dal.dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import biz.entreprise.EntrepriseDto;
import exceptions.FatalException;
import util.AppContext.DependanceInjection;
import util.MonLogger;
import util.Util;

public class EntrepriseDaoImpl extends DaoGenerique<EntrepriseDto> implements EntrepriseDao {

  @DependanceInjection
  private MonLogger monLogger;

  @Override
  public List<EntrepriseDto> getAllEntreprises() {
    PreparedStatement ps = super.dalBackendServices
        .getPreparedStatement(appContext.getValueProp("query_get_all_entreprises"));
    List<EntrepriseDto> entreprises;

    try (ResultSet rs = ps.executeQuery()) {
      entreprises = setResultSet(rs);
    } catch (SQLException ex) {
      monLogger.getMonLog().fine(Util.stackTraceToString(ex));
      throw new FatalException(
          "Erreur lors de la récupération des entreprises dans la base de données.");
    }
    return entreprises;
  }

  @Override
  public List<HashMap<String, Object>> getAllEntreprisesWithNumberOfStudents(
      String anneeAcademique) {
    PreparedStatement ps;
    HashMap<String, Object> map;
    List<HashMap<String, Object>> entreprises = new ArrayList<>();

    ps = super.dalBackendServices.getPreparedStatement(
        appContext.getValueProp("query_get_all_entreprises_with_number_of_students"));
    setPreparedStatement(ps, anneeAcademique);
    try (ResultSet resultSet = ps.executeQuery()) {
      while (resultSet.next()) {
        map = getKeyValueEntrepriseDto(resultSet);
        entreprises.add(map);
      }
    } catch (SQLException ex) {
      monLogger.getMonLog().fine(Util.stackTraceToString(ex));
      throw new FatalException(
          "Erreur lors de la récupération des entreprises dans la base de données.");
    }
    return entreprises;
  }

  private HashMap<String, Object> getKeyValueEntrepriseDto(ResultSet rs) throws SQLException {
    HashMap<String, Object> map = new HashMap<String, Object>();
    map.put("idEntreprise", rs.getInt(1));
    map.put("denomination", rs.getString(2));
    map.put("adresse", rs.getString(3));
    map.put("numero", rs.getString(4));
    map.put("boite", rs.getString(5));
    map.put("codePostal", rs.getString(6));
    map.put("ville", rs.getString(7));
    map.put("email", rs.getString(8));
    map.put("tel", rs.getString(9));
    map.put("estBlackListe", rs.getBoolean(10));
    map.put("est_supprime", rs.getBoolean(11));
    map.put("numVersion", rs.getInt(12));
    map.put("nEtudiants", rs.getInt(13));
    return map;
  }

  @Override
  public EntrepriseDto getEntreprise(int idEntreprise) {
    return super.get(idEntreprise);
  }


  @Override
  public EntrepriseDto insertEntreprise(EntrepriseDto entreprise) {
    return super.insert(entreprise);
  }

  @Override
  public boolean denominationEntrepriseExiste(String denomination) {
    PreparedStatement ps = super.dalBackendServices
        .getPreparedStatement(appContext.getValueProp("query_denmination_entrprise_existe"));
    setPreparedStatement(ps, denomination);
    try (ResultSet rs = ps.executeQuery()) {
      if (rs.next()) {
        int count = rs.getInt(1);
        if (count > 1) {
          throw new FatalException("duplicate PK table entreprises");
        }
        return count == 1;
      } else {
        return false;
      }
    } catch (SQLException ex) {
      monLogger.getMonLog().fine(Util.stackTraceToString(ex));
      throw new FatalException("Erreur lors de l'accès à la DB");

    }
  }

  @Override
  public List<String> getAnneesAcademiques() {
    PreparedStatement ps = super.dalBackendServices
        .getPreparedStatement(appContext.getValueProp("query_get_annees_academiques"));
    List<String> anneesAcademiques = new ArrayList<String>();

    try (ResultSet rs = ps.executeQuery()) {
      while (rs.next()) {
        anneesAcademiques.add(rs.getString(1));
      }
    } catch (SQLException ex) {
      monLogger.getMonLog().fine(Util.stackTraceToString(ex));
      throw new FatalException(
          "Erreur lors de la récupération des années académique dans la base de données.");
    }
    return anneesAcademiques;
  }


  public EntrepriseDto updateEntreprise(EntrepriseDto entreprise) {
    EntrepriseDto entrepriseDto = super.update(entreprise);
    return entrepriseDto;
  }
}
