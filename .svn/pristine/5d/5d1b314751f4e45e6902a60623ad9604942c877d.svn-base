package biz.ucc;

import java.util.List;
import biz.entreprise.EntrepriseDto;
import biz.pdc.PersonneContactBiz;
import biz.pdc.PersonneContactDto;
import dal.dao.EntrepriseDao;
import dal.dao.PersonneContactDao;
import dal.services.DalServices;
import exceptions.BizException;
import util.AppContext.DependanceInjection;
import util.MonLogger;
import util.Util;

class PersonneContactUccImpl implements PersonneContactUcc {

  @DependanceInjection
  private PersonneContactDao personneContactDao;

  @DependanceInjection
  private EntrepriseDao entrepriseDao;

  @DependanceInjection
  private DalServices dalServices;

  @DependanceInjection
  private MonLogger monLogger;

  @Override
  public List<PersonneContactDto> visualiserPersonnesContact(int idEntreprise) {

    try {
      dalServices.startTransaction();
      EntrepriseDto entrepriseAVerifier = entrepriseDao.getEntreprise(idEntreprise);
      if (entrepriseAVerifier == null) {
        throw new BizException("L'entreprise n'existe pas");
      }
      return personneContactDao
          .listerPersonnesContactByIdEntreprise(entrepriseAVerifier.getIdEntreprise());
    } catch (Exception ex) {
      monLogger.getMonLog().fine(Util.stackTraceToString(ex));
      dalServices.rollback();
      throw ex;
    } finally {
      dalServices.commitTransaction();
    }
  }

  public PersonneContactDto creerPersonneContact(PersonneContactDto personneContact) {
    Util.checkObject(personneContact);
    Util.checkFormatString(personneContact.getNom(), PersonneContactBiz.MAX_CARACTERES_NOM,
        "Le format du nom est incorrect");
    Util.checkFormatString(personneContact.getPrenom(), PersonneContactBiz.MAX_CARACTERES_PRENOM,
        "Le format du prénom est incorrect");
    Util.checkFormatString(personneContact.getTel(), PersonneContactBiz.MAX_CARACTERES_TEL,
        PersonneContactBiz.REGEX_TEL, "Le format du tel est incorrect");
    Util.checkFormatString(personneContact.getEmail(), PersonneContactBiz.MAX_CARACTERES_EMAIL,
        PersonneContactBiz.REGEX_EMAIL, "Le format de l'email est incorrect");
    personneContact.setNumVersion(1);

    try {
      dalServices.startTransaction();
      EntrepriseDto entrepriseDb = entrepriseDao.getEntreprise(personneContact.getEntreprise());
      if (entrepriseDb == null) {
        throw new BizException("L'entreprise n'existe pas");
      }
      return personneContactDao.insertPersonneContact(personneContact);
    } catch (Exception ex) {
      monLogger.getMonLog().fine(Util.stackTraceToString(ex));
      dalServices.rollback();
      throw ex;
    } finally {
      dalServices.commitTransaction();
    }
  }

  @Override
  public List<PersonneContactDto> visualiserResponsablesStage() {
    try {
      dalServices.startTransaction();
      return personneContactDao.getResponsablesStage();
    } catch (Exception ex) {
      monLogger.getMonLog().fine(Util.stackTraceToString(ex));
      dalServices.rollback();
      throw ex;
    } finally {
      dalServices.commitTransaction();
    }
  }
}
