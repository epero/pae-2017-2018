package biz.ucc;

import java.time.LocalDate;
import java.util.List;
import biz.contact.ContactBiz.Etat;
import biz.contact.ContactDto;
import biz.entreprise.EntrepriseDto;
import biz.pdc.PersonneContactDto;
import biz.stage.StageBiz;
import biz.stage.StageDto;
import biz.user.UserBiz;
import biz.user.UserDto;
import dal.dao.ContactDao;
import dal.dao.EntrepriseDao;
import dal.dao.PersonneContactDao;
import dal.dao.StageDao;
import dal.dao.UserDao;
import dal.services.DalServices;
import exceptions.BizException;
import exceptions.OptimisticLockException;
import util.AppContext.DependanceInjection;
import util.MonLogger;
import util.Util;

public class StageUccImpl implements StageUcc {

  @DependanceInjection
  private StageDao stageDao;
  @DependanceInjection
  private DalServices dalServices;
  @DependanceInjection
  private UserDao userDao;
  @DependanceInjection
  private PersonneContactDao pcDao;
  @DependanceInjection
  private ContactDao contactDao;
  @DependanceInjection
  private EntrepriseDao entrepriseDao;
  @DependanceInjection
  private MonLogger monLogger;

  @Override
  public StageDto confirmDataStage(StageDto stage, int idContact, int contactNumVersion, int idUser,
      int userNumVersion) {
    Util.checkObject(stage);

    StageBiz stageAVerif = (StageBiz) stage;
    Util.checkFormatString(stageAVerif.getAdresse(), StageBiz.MAX_CARACTERES_ADRESSE,
        "Le format de l'adresse est incorrect");
    if (stageAVerif.getBoite() != null) {
      Util.checkFormatString(stageAVerif.getBoite(), StageBiz.MAX_CARACTERES_BOITE,
          "Le format de la boite est incorrect");
    }
    Util.checkFormatString(stageAVerif.getCodePostal(), StageBiz.MAX_CARACTERES_CODE_POSTAL,
        "Le format du code postal est incorrect");
    Util.checkFormatString(stageAVerif.getVille(), StageBiz.MAX_CARACTERES_VILLE,
        "Le format de l'adresse est incorrect");
    Util.checkFormatDate(stageAVerif.getDateSignature(), "Le format de la date est incorrect");

    stage.setAnneeAcademique(Util.localDateToYear(LocalDate.now()));
    stage.setNumVersion(1);
    StageDto stageEnDb = null;

    try {
      dalServices.startTransaction();

      UserDto userDb = userDao.getUser(idUser);
      EntrepriseDto entrepriseDb = entrepriseDao.getEntreprise(stage.getEntreprise());
      PersonneContactDto pcDb = pcDao.getPersonneContact(stage.getResponsable());
      ContactDto contactDb = contactDao.getContact(idContact);

      if (userDb == null || entrepriseDb == null || pcDb == null || contactDb == null) {
        throw new BizException(
            "L'entreprise, l'utilisateur, le responsable ou le contact n'existe pas");
      }

      if (userDb.getEstAdmin()) {
        throw new BizException("Un professeur ne peut pas créer un stage pour sa part");
      }

      if (!userDb.getAnneeAcademique().equals(Util.localDateToYear(LocalDate.now()))) {
        throw new BizException(
            "L'année académique ne correspond pas à l'année académique courante");
      }

      if (contactNumVersion != contactDb.getNumVersion()) {
        throw new OptimisticLockException("Le contact a été modifié entre temps", contactDb);
      }

      if (userNumVersion != userDb.getNumVersion()) {
        throw new OptimisticLockException("L'utilisateur à été modifié entre temps", userDb);
      }

      if (stageDao.getStage(idUser) != null) {
        throw new BizException("Vous ne pouvez plus créer de stage");
      }

      if (!pcDao.personneDeContactAppartientEntreprise(stage.getResponsable(),
          stage.getEntreprise())) {
        throw new BizException("Le responsable n'appartient pas à l'entreprise");
      }

      if (contactDao.getContacts(idUser, Etat.STAGE_EN_ORDRE.getNumEtat()).size() != 0) {
        throw new BizException(
            "Création de stage impossible s'il existe un contact stage en ordre");
      }

      List<ContactDto> contactsAcceptes = contactDao.getContacts(idUser, Etat.ACCEPTE.getNumEtat());
      if (contactsAcceptes.size() != 1) {
        throw new BizException(
            "Création de stage uniquement possible avec un et un seul contact accepté");
      }
      ContactDto contactAccepteDb = contactsAcceptes.get(0);

      if (contactAccepteDb.getIdContact() != idContact) {
        throw new BizException("Le contact et le stage ne correspond pas");
      }

      if (contactAccepteDb.getEntreprise() != (stage.getEntreprise())) {
        throw new BizException("L'entreprise du stage et du contact accepté ne correspondent pas");
      }


      stage.setUtilisateur(idUser);
      stageEnDb = stageDao.insertStage(stage);
      contactAccepteDb.setEtat(Etat.STAGE_EN_ORDRE.getNumEtat());
      contactDao.updateContact(contactAccepteDb);
      userDb.setEtatPlusAvance(UserBiz.EtatPlusAvance.STAGE_EN_ORDRE.getNumEtat());
      stageEnDb.setUtilisateurDto(userDao.updateUser(userDb));

    } catch (Exception ex) {
      monLogger.getMonLog().fine(Util.stackTraceToString(ex));
      dalServices.rollback();
      throw ex;
    } finally {
      dalServices.commitTransaction();
    }

    return stageEnDb;
  }


  @Override
  public StageDto visualiserStage(int idUtilisateur) {
    StageDto stage;

    try {
      dalServices.startTransaction();
      if (userDao.getUser(idUtilisateur) == null) {
        throw new BizException("Cet utilisateur n'existe pas");
      }
      stage = stageDao.getStage(idUtilisateur);
      if (stage == null) {
        throw new BizException("Pas de stage pour cet utilisateur");
      }
    } catch (Exception ex) {
      monLogger.getMonLog().fine(Util.stackTraceToString(ex));
      dalServices.rollback();
      throw ex;
    } finally {
      dalServices.commitTransaction();
    }
    return stage;
  }

}
