package dal.dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import biz.pdc.PersonneContactDto;
import exceptions.FatalException;
import util.AppContext.DependanceInjection;
import util.MonLogger;
import util.Util;


class PersonneContactDaoImpl extends DaoGenerique<PersonneContactDto>
    implements PersonneContactDao {

  @DependanceInjection
  private EntrepriseDao entrepriseDao;

  @DependanceInjection
  private MonLogger monLogger;

  @Override
  public List<PersonneContactDto> listerPersonnesContactByIdEntreprise(int idEntreprise) {
    PreparedStatement ps = super.dalBackendServices.getPreparedStatement(
        appContext.getValueProp("query_get_personnes_contact_by_id_entreprise"));
    List<PersonneContactDto> personnes;
    setPreparedStatement(ps, idEntreprise);

    try (ResultSet rs = ps.executeQuery()) {
      personnes = setResultSet(rs);

    } catch (SQLException ex) {
      monLogger.getMonLog().fine(Util.stackTraceToString(ex));
      throw new FatalException(
          "Erreur lors de la récupération des personnes de contact dans la base de données.");
    }
    return personnes;
  }

  public PersonneContactDto insertPersonneContact(PersonneContactDto personneContact) {
    return super.insert(personneContact);
  }

  @Override
  public PersonneContactDto getPersonneContact(int idPersonneContact) {
    return super.get(idPersonneContact);
  }

  @Override
  public boolean personneDeContactAppartientEntreprise(int idPersonneContact, int idEntreprise) {
    PreparedStatement ps = super.dalBackendServices.getPreparedStatement(
        appContext.getValueProp("query_personne_de_contact_appartient_entreprise"));
    setPreparedStatement(ps, idPersonneContact, idEntreprise);

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
  public List<PersonneContactDto> getResponsablesStage() {
    PreparedStatement ps = super.dalBackendServices
        .getPreparedStatement(appContext.getValueProp("query_get_responsables_stage"));

    List<PersonneContactDto> responsables;

    try (ResultSet rs = ps.executeQuery()) {
      responsables = setResultSet(rs);
    } catch (SQLException ex) {
      monLogger.getMonLog().fine(Util.stackTraceToString(ex));
      throw new FatalException(
          "Erreur lors de la récupération des entreprises dans la base de données.");
    }
    for (PersonneContactDto personneContactDto : responsables) {
      personneContactDto
          .setEntrepriseDto(entrepriseDao.getEntreprise(personneContactDto.getEntreprise()));
    }
    return responsables;
  }

  public PersonneContactDto updatePersonneContact(PersonneContactDto personneContact) {
    return super.update(personneContact);
  }

}
