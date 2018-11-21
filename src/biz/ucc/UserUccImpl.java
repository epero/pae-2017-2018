package biz.ucc;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import biz.user.UserBiz;
import biz.user.UserDto;
import dal.dao.UserDao;
import dal.services.DalServices;
import exceptions.BizException;
import util.AppContext.DependanceInjection;
import util.MonLogger;
import util.Util;

class UserUccImpl implements UserUcc {

  @DependanceInjection
  private UserDao userDao;

  @DependanceInjection
  private DalServices dalServices;

  @DependanceInjection
  private MonLogger monLogger;


  @Override
  public UserDto seConnecter(String pseudo, String mdp) {
    Util.checkFormatString(pseudo, UserBiz.MAX_CARACTERES_PSEUDO,
        "Le format du champ pseudo est incorrect");
    Util.checkFormatString(mdp, UserBiz.MAX_CARACTERES_MDP, "Le format du champ mdp est incorrect");
    UserBiz userEnDb;
    try {
      dalServices.startTransaction();
      userEnDb = (UserBiz) userDao.getUser(pseudo);
    } catch (Exception ex) {
      monLogger.getMonLog().fine(Util.stackTraceToString(ex));
      dalServices.rollback();
      throw ex;
    } finally {
      dalServices.commitTransaction();
    }
    if (userEnDb != null && userEnDb.verifierMotDePasse(mdp)) {
      return userEnDb;
    }
    return null;
  }

  @Override
  public UserDto sinscrire(UserDto user) {
    Util.checkObject(user);

    Util.checkFormatString(user.getPseudo(), UserBiz.MAX_CARACTERES_PSEUDO,
        "Le format du Pseudo est incorrect");
    Util.checkFormatString(user.getMdp(), UserBiz.MAX_CARACTERES_MDP,
        "Le format du mdp est incorrect");
    Util.checkFormatString(user.getPrenom(), UserBiz.MAX_CARACTERES_PRENOM,
        "Le format du prenom est incorrect");
    Util.checkFormatString(user.getNom(), UserBiz.MAX_CARACTERES_NOM,
        "Le format du nom est incorrect");
    Util.checkFormatString(user.getEmail(), UserBiz.MAX_CARACTERES_EMAIL, UserBiz.REGEX_EMAIL,
        "Le format de l'email est incorrect");
    Util.checkFormatString(user.getTel(), UserBiz.MAX_CARACTERES_TEL, UserBiz.REGEX_TEL,
        "Le format du numero de tel est incorrect");
    Util.checkFormatDate(user.getDateNaissance(), "Le format de la date est incorrect");
    user.setDateInscription(LocalDate.now());
    user.setAnneeAcademique(Util.localDateToYear(user.getDateInscription()));
    user.setMdp(Util.hashpw(user.getMdp()));
    user.setEtatPlusAvance(UserBiz.EtatPlusAvance.AUCUN_CONTACT.getNumEtat());
    user.setNumVersion(1);

    try {
      dalServices.startTransaction();
      if (userDao.pseudoUserExiste(user.getPseudo())) {
        throw new BizException("Ce pseudo existe déjà");
      }
      if (userDao.emailUserExiste(user.getEmail())) {
        throw new BizException("Cet email est déjà utilisé");
      }
      return userDao.insertUser(user);
    } catch (Exception ex) {
      monLogger.getMonLog().fine(Util.stackTraceToString(ex));
      dalServices.rollback();
      throw ex;
    } finally {
      dalServices.commitTransaction();
    }
  }

  public UserDto updateInfoUtilisateur(int idUser, int userNumVersion, String nom, String prenom,
      LocalDate dateNaissance, String email, String tel) {

    Util.checkFormatString(prenom, UserBiz.MAX_CARACTERES_PRENOM,
        "Le format du prenom est incorrect");
    Util.checkFormatString(nom, UserBiz.MAX_CARACTERES_NOM, "Le format du nom est incorrect");
    Util.checkFormatString(email, UserBiz.MAX_CARACTERES_EMAIL, UserBiz.REGEX_EMAIL,
        "Le format de l'email est incorrect");
    Util.checkFormatString(tel, UserBiz.MAX_CARACTERES_TEL, UserBiz.REGEX_TEL,
        "Le format du numero de tel est incorrect");
    Util.checkFormatDate(dateNaissance, "Le format de la date est incorrect");

    try {
      dalServices.startTransaction();
      UserDto userDb = userDao.getUser(idUser);
      if (userDb == null) {
        throw new BizException("Cet utilisateur n'existe pas");
      }
      userDb.setNom(nom);
      userDb.setPrenom(prenom);
      userDb.setEmail(email);
      userDb.setDateNaissance(dateNaissance);
      userDb.setTel(tel);
      userDb.setNumVersion(userNumVersion);
      return userDao.updateUser(userDb);
    } catch (Exception ex) {
      monLogger.getMonLog().fine(Util.stackTraceToString(ex));
      dalServices.rollback();
      throw ex;
    } finally {
      dalServices.commitTransaction();
    }
  }

  public UserDto updateMdpUtilisateur(int idUser, int userNumVersion, String mdpActuel,
      String newMotDePasse1, String newMotDePasse2) {

    Util.checkFormatString(newMotDePasse1, UserBiz.MAX_CARACTERES_MDP,
        "Le format du  nouveau mdp est incorrect");
    if (!newMotDePasse1.equals(newMotDePasse2)) {
      throw new BizException("Les mots de passe ne correspondent pas");
    }

    try {
      dalServices.startTransaction();
      UserDto userDb = userDao.getUser(idUser);
      if (userDb == null) {
        throw new BizException("Cet utilisateur n'existe pas");
      }
      if (!Util.checkpw(mdpActuel, userDb.getMdp())) {
        throw new BizException("Mot de passe actuel éronné");
      }
      String newPswdHashed = Util.hashpw(newMotDePasse1);
      userDb.setMdp(newPswdHashed);
      userDb.setNumVersion(userNumVersion);
      return userDao.updateUser(userDb);
    } catch (Exception ex) {
      monLogger.getMonLog().fine(Util.stackTraceToString(ex));
      dalServices.rollback();
      throw ex;
    } finally {
      dalServices.commitTransaction();
    }
  }

  @Override
  public UserDto trouverUtilisateurById(int idUser) {

    try {
      dalServices.startTransaction();
      return userDao.getUser(idUser);
    } catch (Exception ex) {
      monLogger.getMonLog().fine(Util.stackTraceToString(ex));
      dalServices.rollback();
      throw ex;
    } finally {
      dalServices.commitTransaction();
    }
  }

  @Override
  public HashMap<String, Integer> getStudentsStats() {
    try {
      dalServices.startTransaction();
      return userDao.getStudentsStats(Util.localDateToYear(LocalDate.now()));
    } catch (Exception ex) {
      monLogger.getMonLog().fine(Util.stackTraceToString(ex));
      dalServices.rollback();
      throw ex;
    } finally {
      dalServices.commitTransaction();
    }
  }

  @Override
  public HashMap<String, Integer> getStudentStats(int idUser) {
    try {
      dalServices.startTransaction();
      if (userDao.getUser(idUser) == null) {
        throw new BizException("L'utilisateur n'existe pas");
      }
      return userDao.getStudentStats(idUser);
    } catch (Exception ex) {
      monLogger.getMonLog().fine(Util.stackTraceToString(ex));
      dalServices.rollback();
      throw ex;
    } finally {
      dalServices.commitTransaction();
    }
  }

  @Override
  public List<UserDto> visualiserUsersAnneeCourante() {


    try {
      dalServices.startTransaction();
      return userDao.getAllUsers(Util.localDateToYear(LocalDate.now()));
    } catch (Exception ex) {
      monLogger.getMonLog().fine(Util.stackTraceToString(ex));
      dalServices.rollback();
      throw ex;
    } finally {
      dalServices.commitTransaction();
    }
  }
}
