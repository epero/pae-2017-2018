package biz.ucc;

import java.util.HashMap;
import java.util.List;
import biz.entreprise.EntrepriseDto;

public interface EntrepriseUcc {

  /**
   * Récupère toutes les entreprises disponibles dans la base de données.
   * 
   * @return un liste d'objet EntrepriseDto si présent dans la base de données, null sinon
   */
  List<EntrepriseDto> visualiserEntreprises();

  /**
   * Récupère chaque entreprise présente dans la base de données ainsi que son nombre d'étudiants
   * pris en stage en fonction de l'année académique.
   * 
   * @param anneeAcademique filtre pour chaque entreprise le nombre d'étudiants pris en stage par
   *        année académique
   * @return une liste de clés - valeurs propres à une EntrepriseDto
   */
  List<HashMap<String, Object>> visualiserEntreprisesAsProf(String anneeAcademique);

  /**
   * Récupère une entreprise en base de données.
   * 
   * @param idEntreprise l'identifiant de l'entreprise
   * @return un objet de type EntrepriseDto si l'entreprise existe, null sinon
   */
  EntrepriseDto getEntreprise(int idEntreprise);

  /**
   * Change l'état de l'entreprise en "blacklisté".
   * 
   * @param idEntreprise l'id de l'entreprise à blacklister
   * @return un objet de type EntrepriseDto si l'entreprise a été blacklisté, null sinon
   */
  EntrepriseDto blacklistEntreprise(int idEntreprise, int entNumVersion);

  /**
   * Insere une entreprise dans la db.
   * 
   * @param entreprise l'entreprise à inserer
   * @return l'entreprise inséree si l'insertion a réussie, null sinon
   */
  EntrepriseDto insertEntreprise(EntrepriseDto entreprise);

  /**
   * Récupère les années académique présentes en base de données selon la table utilisateurs.
   * 
   * @return une liste d'années académiques, sans doublons
   */
  List<String> getAnneesAcademiques();

  /**
   * Fusionne l'entreprise 1 avec l'entreprise 2 (l'entreprise 2 est celle qui reste 'active').
   * 
   * @param idEntreprise1 l'id de l'entreprise 1
   * @param idEntreprise2 l'id de l'entreprise 2
   * @return L'entreprise restante (entreprise 2) si la fusion a réussie, null sinon
   */
  EntrepriseDto fusionnerEntreprise1AvecEntreprise2(int idEntreprise1, int numVersEntr1,
      int idEntreprise2, int numVersEntr2);


}
