package biz.ucc;

import java.util.List;
import biz.contact.ContactDto;
import biz.user.UserDto;

public interface ContactUcc {
  /**
   * récupère et renvoie une liste de contacts pour un utilisateur donné.
   *
   * @param utilisateur ,l'utilisateur dont on recherche les contacts
   * @return une liste de contacts de l'utilisateur, cette liste est vide s'il n'y a pas de contact
   *         pour cet utilisateur
   */
  public List<ContactDto> listerContactsUtilisateur(int utilisateur);

  public ContactDto creerContactUtilisateur(int user, int userNumVersion, int idEntreprise,
      Object idPersonneContact);

  /**
   * Modifie l'état du contact dont l'id est passé en paramètre.
   * 
   * @param contacts la liste de ContactDto à mettre à jour
   * @param idUser l'id de l'utilisateur qui possède les contacts
   * @param userNumVersion le numéro de version de l'utilisateur
   * @return le contact modifié si l'update a réussi, null sinon // A MODIFIER
   */
  public UserDto updateEtatContacts(List<ContactDto> contacts, int idUser, int userNumVersion);



}
