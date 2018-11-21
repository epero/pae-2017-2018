package dal.dao;

import java.util.List;
import biz.stage.StageDto;

public interface StageDao {

  /**
   * Récupère et renvoie un stage pour un utilisateur donné.
   * 
   * @param idUtilisateur l'id de l'utilisateur
   * @return l'objet StageDto correspondant correspondant à l'id si la personne de stage existe,
   *         null sinon
   */
  public StageDto getStage(int idUtilisateur);

  /**
   * Insère un stage dans la base de données.
   * 
   * @param stage le stage à insérer
   * @return l'objet StageDto correspondant à l'insertion
   */
  public StageDto insertStage(StageDto stage);

  /**
   * Récupère et renvoie la liste de stages dont l'id de l'entreprise est passé en paramètre.
   * 
   * @param idEntreprise l'id de l'entreprise liée au stage
   * @return une liste de stages de l'entreprise, une liste vide s'il n'y a pas de stages pour cette
   *         entreprise
   */
  public List<StageDto> listerStagesPourEntreprise(int idEntreprise);

  /**
   * Met à jour l'entreprise du stage dont l'id et le numero de version sont passé en paramètre.
   * 
   * @param stageDto un objet de type StageDto représentant le stage
   * @return true si le transfert s'est bien déroulé, throws exception sinon
   */

  public StageDto updateStage(StageDto stageDto);
}
