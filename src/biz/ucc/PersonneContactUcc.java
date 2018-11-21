package biz.ucc;

import java.util.List;
import biz.pdc.PersonneContactDto;

public interface PersonneContactUcc {
  /**
   * Récupère et renvoie une liste de personnes de contact pour une entreprise donnée.
   * 
   * @param idEntreprise l'id de l'entreprise concernée
   * @return une liste de PersonneContactDto remplie ou vide
   */
  List<PersonneContactDto> visualiserPersonnesContact(int idEntreprise);

  /**
   * Crée une personne de contact.
   * 
   * @param personneContact la nouvelle personne de contact
   * @return un objet de type PersonneContactDto représentant la nouvelle personne insérée
   */
  PersonneContactDto creerPersonneContact(PersonneContactDto personneContact);

  /**
   * Récupère et renvoie une liste de responsables de stage.
   * 
   * @return un liste de PersonneContactDto
   */
  List<PersonneContactDto> visualiserResponsablesStage();
}
