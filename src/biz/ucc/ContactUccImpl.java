package biz.ucc;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import biz.contact.ContactBiz;
import biz.contact.ContactDto;
import biz.factory.BizFactory;
import biz.user.UserBiz;
import biz.user.UserDto;
import dal.dao.ContactDao;
import dal.dao.EntrepriseDao;
import dal.dao.PersonneContactDao;
import dal.dao.UserDao;
import dal.services.DalServices;
import exceptions.BizException;
import exceptions.OptimisticLockException;
import util.AppContext.DependanceInjection;
import util.MonLogger;
import util.Util;

public class ContactUccImpl implements ContactUcc {
  @DependanceInjection
  private ContactDao contactDao;
  @DependanceInjection
  private PersonneContactDao personneContactDao;
  @DependanceInjection
  private UserDao userDao;
  @DependanceInjection
  private EntrepriseDao entrepriseDao;
  @DependanceInjection
  private DalServices dalServices;
  @DependanceInjection
  private BizFactory factory;
  @DependanceInjection
  private MonLogger monLogger;


  @Override
  public List<ContactDto> listerContactsUtilisateur(int idUtilisateur) {
    List<ContactDto> listeContactDto;

    try {
      dalServices.startTransaction();
      UserDto userDb = userDao.getUser(idUtilisateur);
      if (userDb == null) {
        throw new BizException("L'utilisateur n'existe pas");
      }
      if (userDb.getEstAdmin()) {
        throw new BizException("Un professeur n'a pas de contacts");
      }
      listeContactDto = contactDao.listerContactsPourUtilisateur(idUtilisateur);
    } catch (Exception ex) {
      monLogger.getMonLog().fine(Util.stackTraceToString(ex));
      dalServices.rollback();
      throw ex;
    } finally {
      dalServices.commitTransaction();
    }
    return listeContactDto;
  }

  @Override
  public ContactDto creerContactUtilisateur(int idUser, int userNumVersion, int idEntreprise,
      Object idPersonneContact) {

    try {
      dalServices.startTransaction();
      UserDto userDb = userDao.getUser(idUser);
      if (userDb == null || entrepriseDao.getEntreprise(idEntreprise) == null) {
        throw new BizException("L'entreprise ou l'utilisateur n'existe pas");
      }
      if (userDb.getEstAdmin()) {
        throw new BizException("Un professeur ne peut pas créer des contacts");
      }
      if (!userDb.getAnneeAcademique().equals(Util.localDateToYear(LocalDate.now()))) {
        throw new BizException(
            "L'année académique ne correspond pas à l'année académique courante");
      }
      if (userNumVersion != userDb.getNumVersion()) {
        throw new OptimisticLockException("L'utilisateur à été modifié entre temps", userDb);
      }
      if (contactDao.existeContactsAccepteOuEnOrdre(idUser)) {
        throw new BizException("Vous ne pouvez plus initier de contact");
      }
      if (contactDao.existeContactForEntrepriseEtUser(idUser, idEntreprise)) {
        throw new BizException("Vous avez déjà initié un contact avec cette entreprise");
      }
      ContactDto newContact = factory.getContactVide();
      newContact.setEtat(ContactBiz.Etat.INITIE.getNumEtat());
      newContact.setAnneeAcademique(Util.localDateToYear(LocalDate.now()));
      newContact.setNumVersion(1);
      newContact.setEntreprise(idEntreprise);
      newContact.setUtilisateur(idUser);

      if (idPersonneContact != null) {
        Util.checkNumerique("" + idPersonneContact);
        if (!personneContactDao.personneDeContactAppartientEntreprise(
            Integer.parseInt("" + idPersonneContact), idEntreprise)) {
          throw new BizException("Cette personne de contact n'appartient pas à cette entreprise");
        }
        newContact.setPersonneContact(Integer.parseInt("" + idPersonneContact));
      }
      contactDao.insertContact(newContact);
      userDb.setNbContacts(userDb.getNbContacts() + 1);
      userDb.setEtatPlusAvance(UserBiz.EtatPlusAvance.INITIE.getNumEtat());
      userDb = userDao.updateUser(userDb);
      newContact.setUtilisateurDto(userDb);
      return newContact;
    } catch (Exception ex) {
      monLogger.getMonLog().fine(Util.stackTraceToString(ex));
      dalServices.rollback();
      throw ex;
    } finally {
      dalServices.commitTransaction();
    }
  }

  @Override
  public UserDto updateEtatContacts(List<ContactDto> listeContactsDto, int idUser,
      int userNumVersion) {
    Util.checkObject(listeContactsDto);
    if (listeContactsDto.isEmpty()) {
      throw new BizException("Vous n'avez modifié aucun contact");
    }
    Collections.sort(listeContactsDto, (t1, t2) -> t1.getEtat() - (t2.getEtat()));
    UserDto userDb;
    try {
      dalServices.startTransaction();

      userDb = userDao.getUser(idUser);
      if (userDb == null) {
        throw new BizException("L'utilisateur n'existe pas");
      }
      if (userDb.getEstAdmin()) {
        throw new BizException("Un professeur n'a pas de contacts");
      }
      if (!userDb.getAnneeAcademique().equals(Util.localDateToYear(LocalDate.now()))) {
        throw new BizException(
            "L'année académique ne correspond pas à l'année académique courante");
      }
      if (userNumVersion != userDb.getNumVersion()) {
        throw new OptimisticLockException("L'utilisateur à été modifié entre temps", userDb);
      }
      int etatPlusAvance = 0;
      for (ContactDto contactDto : listeContactsDto) {
        etatPlusAvance = contactDto.getEtat();
        boolean updateEstValide = false;
        // checks pour voir si on peut faire l'update(si le contact courant est refuse ou stage en
        // ordre on ne peut plus changer d'etat)
        ContactDto contactDbInitial = contactDao.getContact(contactDto.getIdContact());
        if (contactDbInitial == null) {
          throw new BizException("Un ou plusieurs contacts n'existent pas");
        }
        if (contactDbInitial.getNumVersion() != contactDto.getNumVersion()) {
          throw new OptimisticLockException(
              "Un contact que vous voulez modifier a déjà été modifié entre temps",
              contactDbInitial);
        }
        int etatInitial = contactDbInitial.getEtat();
        if (etatInitial == ContactBiz.Etat.INITIE.getNumEtat()) {
          if (contactDto.getEtat() == ContactBiz.Etat.PRIS.getNumEtat()
              || contactDto.getEtat() == ContactBiz.Etat.ACCEPTE.getNumEtat()
              || contactDto.getEtat() == ContactBiz.Etat.REFUSE.getNumEtat()) {
            updateEstValide = true;
          }
        } else if (etatInitial == ContactBiz.Etat.PRIS.getNumEtat()) {
          if (contactDto.getEtat() == ContactBiz.Etat.ACCEPTE.getNumEtat()
              || contactDto.getEtat() == ContactBiz.Etat.REFUSE.getNumEtat()) {
            updateEstValide = true;
          }
        } else if (etatInitial == ContactBiz.Etat.ACCEPTE.getNumEtat()) {
          if (contactDto.getEtat() == ContactBiz.Etat.STAGE_EN_ORDRE.getNumEtat()) {
            updateEstValide = true;
          }
        }

        // check si il existe pas deja un autre contact à l'état accepte ou stage en ordre
        if (contactDao.existeContactAvecEtat(idUser, ContactBiz.Etat.ACCEPTE.getNumEtat())
            || contactDao.existeContactAvecEtat(idUser,
                ContactBiz.Etat.STAGE_EN_ORDRE.getNumEtat())) {
          throw new BizException(
              "Impossible d'avoir plusieurs contacts acceptés et/ou stage en ordre. Recommencez ");
        }
        if (updateEstValide) {
          contactDbInitial.setEtat(contactDto.getEtat());
          contactDao.updateContact(contactDbInitial);
        } else {
          throw new BizException("Action interdite sur un ou plusieurs contacts");
        }
      }

      if (etatPlusAvance != userDb.getEtatPlusAvance()) {
        if (etatPlusAvance == UserBiz.EtatPlusAvance.REFUSE.getNumEtat()) {
          if (contactDao.listerContactDiffDeEtat(idUser, etatPlusAvance).isEmpty()) {
            userDb.setEtatPlusAvance(etatPlusAvance);
          }
        } else if (etatPlusAvance != UserBiz.EtatPlusAvance.AUCUN_CONTACT.getNumEtat()) {
          userDb.setEtatPlusAvance(etatPlusAvance);
        }
      }
      return userDao.updateUser(userDb);
    } catch (Exception ex) {
      monLogger.getMonLog().fine(Util.stackTraceToString(ex));
      dalServices.rollback();
      throw ex;
    } finally {
      dalServices.commitTransaction();
    }
  }



}
